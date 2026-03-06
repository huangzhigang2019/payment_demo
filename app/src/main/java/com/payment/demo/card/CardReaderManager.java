package com.payment.demo.card;

import com.pax.dal.IIcc;
import com.pax.dal.IMag;
import com.pax.dal.IPicc;
import com.pax.dal.entity.EDetectMode;
import com.pax.dal.entity.EReaderType;
import com.pax.dal.entity.PiccCardInfo;
import com.pax.dal.entity.TrackData;
import com.pax.dal.exceptions.IccDevException;
import com.pax.dal.exceptions.MagDevException;
import com.pax.dal.exceptions.PiccDevException;
import com.payment.demo.app.PaymentDemoApp;
import com.payment.demo.trans.CardReadResult;

/**
 * T011: 读卡模块 — 封装 NeptuneLiteAPI 轮询，超时与停止，回调 CardReadResult.
 */
public class CardReaderManager {
    private static final byte SLOT_ICC = 0;

    private final PaymentDemoApp app;
    private volatile boolean stopRequested;
    private volatile boolean readersOpened;  // true only after open() succeeded
    private volatile boolean readersClosed; // prevent double-close triggering native
    private Thread pollThread;
    private IPicc piccInternal;
    private IMag mag;
    private IIcc icc;

    public CardReaderManager(PaymentDemoApp app) {
        this.app = app;
    }

    public interface Callback {
        void onResult(CardReadResult result);
    }

    /** 使用全部可用模式 (PICC|ICC|MAG) 轮询 */
    public void startDetect(int timeoutMs, Callback callback) {
        startDetect(timeoutMs, (byte) (EReaderType.PICC.getEReaderType()
                | EReaderType.ICC.getEReaderType()
                | EReaderType.MAG.getEReaderType()), callback);
    }

    /** T019: 仅轮询指定模式，屏蔽不支持项 */
    public void startDetect(int timeoutMs, byte modeMask, Callback callback) {
        stopRequested = false;
        pollThread = new Thread(() -> {
            CardReadResult result = doPoll(timeoutMs, modeMask);
            if (callback != null) callback.onResult(result);
        });
        pollThread.start();
    }

    public void stopDetect() {
        stopRequested = true;
        if (pollThread != null && pollThread.isAlive()) {
            pollThread.interrupt();
        }
        safeCloseReaders();
    }

    private CardReadResult doPoll(int timeoutMs) {
        return doPoll(timeoutMs, (byte) (EReaderType.PICC.getEReaderType()
                | EReaderType.ICC.getEReaderType()
                | EReaderType.MAG.getEReaderType()));
    }

    private CardReadResult doPoll(int timeoutMs, byte mode) {
        CardReadResult result = new CardReadResult();
        if (app.getDal() == null) {
            result.setSuccess(false);
            result.setFailCode(CardReadResult.RET_INIT_FAILED);
            result.setCardInfo("DAL not available");
            return result;
        }
        if (mode == 0) {
            result.setSuccess(false);
            result.setFailCode(CardReadResult.RET_INIT_FAILED);
            result.setCardInfo("No read mode available");
            return result;
        }
        try {
            piccInternal = app.getDal().getPicc(com.pax.dal.entity.EPiccType.INTERNAL);
            mag = app.getDal().getMag();
            icc = app.getDal().getIcc();
            // PICC init (only if mode includes PICC)
            if ((mode & EReaderType.PICC.getEReaderType()) != 0) {
                piccInternal.close();
                piccInternal.open();
            }
            // MAG init: close -> open -> reset
            if ((mode & EReaderType.MAG.getEReaderType()) != 0) {
                mag.close();
                mag.open();
                mag.reset();
            }
            // ICC init: close slot
            if ((mode & EReaderType.ICC.getEReaderType()) != 0) {
                icc.close(SLOT_ICC);
            }
        } catch (Throwable t) {
            result.setSuccess(false);
            result.setFailCode(CardReadResult.RET_INIT_FAILED);
            String msg = t.getMessage();
            result.setCardInfo("init failed: " + t.getClass().getSimpleName() + (msg != null ? " - " + msg : ""));
            result.setFailReason(result.getCardInfo());
            return result;
        }
        readersOpened = true;

        long startTime = System.currentTimeMillis();
        while (!stopRequested) {
            if (System.currentTimeMillis() - startTime > timeoutMs) {
                result.setSuccess(false);
                result.setFailCode(CardReadResult.RET_TIMEOUT);
                closeReaders(mode);
                return result;
            }
            // MAG: isSwiped -> read track2
            if ((mode & EReaderType.MAG.getEReaderType()) != 0) {
                try {
                    if (mag.isSwiped()) {
                        TrackData info = mag.read();
                        if (info != null && info.getTrack2() != null && !info.getTrack2().isEmpty()) {
                            result.setSuccess(true);
                            result.setReadType(EReaderType.MAG.getEReaderType());
                            result.setCardInfo(info.getTrack2());
                            closeReaders((byte) (0x06 & mode));
                            return result;
                        }
                    }
                } catch (MagDevException e) {
                    result.setSuccess(false);
                    result.setFailCode(CardReadResult.RET_OTHER);
                    result.setCardInfo(e.getMessage());
                    closeReaders((byte) 0x07);
                    return result;
                }
            }
            // ICC: detect -> init
            if ((mode & EReaderType.ICC.getEReaderType()) != 0) {
                try {
                    if (icc.detect(SLOT_ICC)) {
                        byte[] res = icc.init(SLOT_ICC);
                        if (res != null) {
                            result.setSuccess(true);
                            result.setReadType(EReaderType.ICC.getEReaderType());
                            closeReaders((byte) (0x05 & mode));
                            return result;
                        }
                    }
                } catch (IccDevException e) {
                    if (e.getErrCode() == 97) { // ICC_REVERSE: card removed during init, treat as OK
                        result.setSuccess(true);
                        result.setReadType(EReaderType.ICC.getEReaderType());
                        closeReaders((byte) (0x05 & mode));
                        return result;
                    }
                    result.setSuccess(false);
                    result.setFailCode(CardReadResult.RET_OTHER);
                    result.setCardInfo(e.getMessage());
                    closeReaders((byte) 0x07);
                    return result;
                }
            }
            // PICC: detect (sleep 600ms to avoid false positive when swiping MAG)
            if ((mode & EReaderType.PICC.getEReaderType()) != 0) {
                try {
                    PiccCardInfo info = piccInternal.detect(EDetectMode.EMV_AB);
                    try { Thread.sleep(600); } catch (InterruptedException e) {
                        result.setSuccess(false);
                        result.setFailCode(CardReadResult.RET_CANCEL);
                        closeReaders(mode);
                        return result;
                    }
                    if (info != null) {
                        result.setSuccess(true);
                        result.setReadType(EReaderType.PICC.getEReaderType());
                        closeReaders((byte) (0x03 & mode));
                        return result;
                    }
                } catch (PiccDevException e) {
                    result.setSuccess(false);
                    result.setFailCode(CardReadResult.RET_OTHER);
                    result.setCardInfo(e.getMessage());
                    closeReaders((byte) 0x07);
                    return result;
                }
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                result.setSuccess(false);
                result.setFailCode(CardReadResult.RET_CANCEL);
                closeReaders(mode);
                return result;
            }
        }
        result.setSuccess(false);
        result.setFailCode(CardReadResult.RET_CANCEL);
        closeReaders(mode);
        return result;
    }

    /** Only called from doPoll when readers were successfully opened. */
    private void closeReaders(byte flag) {
        if (!readersOpened || readersClosed) return;
        safeCloseReaders();
    }

    /** Tolerate all native/JNI failures; never throw. Only close if we actually opened readers. */
    private void safeCloseReaders() {
        if (readersClosed) return;
        if (!readersOpened) return;
        readersClosed = true;
        if (mag != null) {
            try { mag.close(); } catch (Throwable ignored) { }
            mag = null;
        }
        if (icc != null) {
            try { icc.close(SLOT_ICC); } catch (Throwable ignored) { }
            icc = null;
        }
        if (piccInternal != null) {
            try { piccInternal.close(); } catch (Throwable ignored) { }
            piccInternal = null;
        }
    }
}
