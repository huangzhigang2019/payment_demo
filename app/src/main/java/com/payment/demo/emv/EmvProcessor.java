package com.payment.demo.emv;

import android.content.Context;

import com.pax.dal.entity.EReaderType;
import com.pax.jemv.clcommon.RetCode;
import com.pax.jemv.device.DeviceManager;
import com.paxsz.module.emv.param.EmvProcessParam;
import com.paxsz.module.emv.param.EmvTransParam;
import com.paxsz.module.emv.process.contact.CandidateAID;
import com.paxsz.module.emv.process.contact.EmvProcess;
import com.paxsz.module.emv.process.contact.IEmvTransProcessListener;
import com.paxsz.module.emv.process.EmvBase;
import com.paxsz.module.emv.process.contactless.ClssProcess;
import com.paxsz.module.emv.process.contactless.IClssStatusListener;
import com.paxsz.module.emv.process.entity.TransResult;
import com.paxsz.module.emv.process.enums.TransResultEnum;
import com.paxsz.module.emv.process.IStatusListener;
import com.payment.demo.app.PaymentDemoApp;
import com.payment.demo.trans.ProcessingResult;
import com.payment.demo.trans.TransResultStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * T015: EMV 处理封装 — 按介质类型调用 EmvProcess/ClssProcess，输出 ProcessingResult。
 */
public class EmvProcessor {
    private final Context context;
    private final EmvParamManager paramManager;

    public EmvProcessor(Context context) {
        this.context = context.getApplicationContext();
        this.paramManager = EmvParamManager.getInstance(context);
    }

    /** T021: 可选回调，当需要用户移开卡片时更新 UI 提示 */
    public interface PromptCallback {
        void onPrompt(String text);
    }

    /**
     * 执行 EMV 处理。MAG 仅展示；ICC 用 EmvProcess；PICC 用 ClssProcess。
     */
    public ProcessingResult process(long amountCent, int readType, String cardInfo) {
        return process(amountCent, readType, cardInfo, null);
    }

    /**
     * 执行 EMV 处理，支持移卡等提示回调。
     */
    public ProcessingResult process(long amountCent, int readType, String cardInfo, PromptCallback promptCallback) {
        ProcessingResult pr = new ProcessingResult();

        // MAG: 无 EMV，直接成功展示
        if (isMag(readType)) {
            pr.setResultStatus(TransResultStatus.SUCCESS);
            pr.setResultCode(0);
            pr.setDisplaySummary(cardInfo != null && !cardInfo.isEmpty()
                    ? "磁条卡: " + maskPan(cardInfo) : "磁条交易完成");
            return pr;
        }

        if (!paramManager.isLoaded()) {
            pr.setResultStatus(TransResultStatus.FAILED);
            pr.setResultCode(-1);
            pr.setDisplaySummary("EMV 参数未加载");
            return pr;
        }

        if (PaymentDemoApp.getApp().getDal() == null) {
            pr.setResultStatus(TransResultStatus.FAILED);
            pr.setResultCode(-1);
            pr.setDisplaySummary("设备未就绪");
            return pr;
        }

        // 确保 EMV/Clss native 库已加载（防御性，Application 已异步加载）
        try { EmvBase.loadLibrary(); } catch (Throwable ignored) { }

        DeviceManager.getInstance().setIDevice(DeviceImplPayment.getInstance());

        EmvTransParam transParam = buildTransParam(amountCent);
        int preRet = doPreTrans(transParam, isIcc(readType));
        if (preRet != RetCode.EMV_OK) {
            pr.setResultStatus(TransResultStatus.FAILED);
            pr.setResultCode(preRet);
            pr.setDisplaySummary("EMV 预处理失败: " + preRet);
            return pr;
        }

        final PromptCallback cb = promptCallback;
        TransResult tr;
        if (isIcc(readType)) {
            EmvProcess.getInstance().registerEmvProcessListener(emvListener);
            tr = EmvProcess.getInstance().startTransProcess();
        } else {
            ClssProcess.getInstance().registerClssStatusListener(new IClssStatusListener() {
                @Override
                public void onRemoveCard() {
                    if (cb != null) cb.onPrompt(context.getString(com.payment.demo.R.string.remove_card_prompt));
                    while (true) {
                        try {
                            PaymentDemoApp.getApp().getDal().getPicc(com.pax.dal.entity.EPiccType.INTERNAL)
                                    .remove(com.pax.dal.entity.EPiccRemoveMode.REMOVE, (byte) 0);
                            break;
                        } catch (Throwable e) {
                            try { Thread.sleep(200); } catch (InterruptedException ie) { break; }
                        }
                    }
                }
                @Override
                public boolean needSeePhone() { return false; }
            });
            ClssProcess.getInstance().registerStatusListener(statusListener);
            tr = ClssProcess.getInstance().startTransProcess();
        }

        return mapTransResult(tr, pr);
    }

    private boolean isIcc(int readType) {
        return (readType & EReaderType.ICC.getEReaderType()) == EReaderType.ICC.getEReaderType();
    }

    private boolean isMag(int readType) {
        return (readType & EReaderType.MAG.getEReaderType()) == EReaderType.MAG.getEReaderType();
    }

    private EmvTransParam buildTransParam(long amountCent) {
        EmvTransParam p = new EmvTransParam();
        p.setTransType((byte) 0x00); // 消费
        p.setAmount(String.valueOf(amountCent));
        p.setAmountOther("0");
        p.setTerminalID(getTerminalId());
        p.setTransCurrencyCode(new byte[]{0x08, 0x40}); // 840 = USD
        p.setTransCurrencyExponent((byte) 2);
        p.setTransDate(new SimpleDateFormat("yyMMdd", Locale.US).format(new Date()));
        p.setTransTime(new SimpleDateFormat("HHmmss", Locale.US).format(new Date()));
        p.setTransTraceNo("0001");
        return p;
    }

    private String getTerminalId() {
        try {
            Map<com.pax.dal.entity.ETermInfoKey, String> info =
                    PaymentDemoApp.getApp().getDal().getSys().getTermInfo();
            if (info != null) {
                String sn = info.get(com.pax.dal.entity.ETermInfoKey.SN);
                if (sn != null) return sn;
            }
        } catch (Throwable ignored) { }
        return "00000001";
    }

    private int doPreTrans(EmvTransParam transParam, boolean needContact) {
        com.paxsz.module.emv.xmlparam.entity.common.Config cfg = paramManager.getConfigParam();
        com.paxsz.module.emv.xmlparam.entity.common.CapkParam capk = paramManager.getCapkParam();

        if (needContact) {
            int ret = EmvProcess.getInstance().preTransProcess(
                    new EmvProcessParam.Builder(transParam, cfg, capk)
                            .setEmvAidList(paramManager.getEmvAidList())
                            .create());
            if (ret != RetCode.EMV_OK) return ret;
        }

        return ClssProcess.getInstance().preTransProcess(
                new EmvProcessParam.Builder(transParam, cfg, capk)
                        .setPayPassAidList(paramManager.getPayPassAidList())
                        .setPayWaveParam(paramManager.getPayWaveParam())
                        .setAmexParam(paramManager.getAmexParam())
                        .create());
    }

    private final IEmvTransProcessListener emvListener = new IEmvTransProcessListener() {
        @Override
        public int onWaitAppSelect(boolean isFirstSelect, List<CandidateAID> candList) {
            if (candList == null || candList.isEmpty()) return RetCode.EMV_NO_APP;
            return 0; // 选第一个
        }

        @Override
        public int onCardHolderPwd(boolean bOnlinePin, boolean supportPINByPass, int leftTimes, byte[] pinData) {
            return RetCode.EMV_USER_CANCEL; // MVP: 取消 PIN
        }
    };

    private final IStatusListener statusListener = new IStatusListener() {
        @Override
        public void onReadCardOk() {
            try {
                PaymentDemoApp.getApp().getDal().getSys().beep(
                        com.pax.dal.entity.EBeepMode.FREQUENCE_LEVEL_5, 100);
            } catch (Throwable ignored) { }
            try { Thread.sleep(750); } catch (InterruptedException ignored) { }
        }
    };

    private ProcessingResult mapTransResult(TransResult tr, ProcessingResult pr) {
        if (tr == null) {
            pr.setResultStatus(TransResultStatus.FAILED);
            pr.setDisplaySummary("EMV 返回空");
            return pr;
        }
        pr.setResultCode(tr.getResultCode());
        int code = tr.getResultCode();
        if (code == RetCode.EMV_USER_CANCEL) {
            pr.setResultStatus(TransResultStatus.CANCELLED);
            pr.setDisplaySummary(context.getString(com.payment.demo.R.string.trans_cancelled));
            return pr;
        }
        if (code == RetCode.EMV_TIME_OUT) {
            pr.setResultStatus(TransResultStatus.TIMEOUT);
            pr.setDisplaySummary(context.getString(com.payment.demo.R.string.trans_timeout));
            return pr;
        }
        TransResultEnum e = tr.getTransResult();
        if (e == TransResultEnum.RESULT_OFFLINE_APPROVED
                || e == TransResultEnum.RESULT_ONLINE_APPROVED) {
            pr.setResultStatus(TransResultStatus.SUCCESS);
            pr.setDisplaySummary("交易成功");
        } else {
            pr.setResultStatus(TransResultStatus.FAILED);
            pr.setDisplaySummary(e != null ? e.name() : "失败(" + tr.getResultCode() + ")");
        }
        return pr;
    }

    private static String maskPan(String track2) {
        if (track2 == null || track2.length() < 8) return "****";
        return track2.substring(0, 4) + "****" + track2.substring(Math.max(4, track2.length() - 4));
    }
}
