/*
 *  ===========================================================================================
 *  = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or nondisclosure
 *     agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *     disclosed except in accordance with the terms in that agreement.
 *          Copyright (C) 2020 -? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 *  Description: // Detail description about the function of this module,
 *               // interfaces with the other modules, and dependencies.
 *  Revision History:
 *  Date	               Author	                   Action
 *  2020/06/01 	         Qinny Zhou           	      Create
 *  ===========================================================================================
 */

package com.paxsz.demo.emvdemo.trans.mvp.cardprocess;

import android.os.ConditionVariable;
import android.os.SystemClock;

import com.pax.commonlib.utils.LogUtils;
import com.pax.commonlib.utils.convert.ConvertHelper;
import com.pax.dal.entity.EPiccType;
import com.pax.dal.exceptions.PiccDevException;
import com.pax.jemv.clcommon.ByteArray;
import com.pax.jemv.clcommon.RetCode;
import com.pax.jemv.device.DeviceManager;
import com.paxsz.demo.emvdemo.app.EmvDemoApp;
import com.paxsz.demo.emvdemo.entity.EnterPinResult;
import com.paxsz.demo.emvdemo.manager.EEnterPinType;
import com.paxsz.demo.emvdemo.trans.mvp.BasePresenter;
import com.paxsz.demo.emvdemo.trans.mvp.DeviceImplNeptune;
import com.paxsz.demo.emvdemo.util.CardInfoUtils;
import com.paxsz.module.emv.param.EmvProcessParam;
import com.paxsz.module.emv.param.EmvTransParam;
import com.paxsz.module.emv.process.IStatusListener;
import com.paxsz.module.emv.process.contact.CandidateAID;
import com.paxsz.module.emv.process.contact.EmvProcess;
import com.paxsz.module.emv.process.contact.IEmvTransProcessListener;
import com.paxsz.module.emv.process.contactless.ClssProcess;
import com.paxsz.module.emv.process.contactless.IClssStatusListener;
import com.paxsz.module.emv.process.entity.IssuerRspData;
import com.paxsz.module.emv.process.entity.TransResult;
import com.paxsz.module.emv.xmlparam.entity.common.CapkParam;
import com.paxsz.module.emv.xmlparam.entity.common.Config;

import java.util.List;

import static com.pax.dal.entity.EBeepMode.FREQUENCE_LEVEL_5;
import static com.pax.dal.entity.EPiccRemoveMode.REMOVE;

public class TransProcessPresenter extends BasePresenter<TransProcessContract.View> implements TransProcessContract.Presenter {
    private static final String TAG = "TransProcessPresenter";
    private DeviceImplNeptune deviceImplNeptune;
    private ConditionVariable enterPinCv = new ConditionVariable();
    private ConditionVariable appSelectCv = new ConditionVariable();
    private int enterPinRet = 0;
    private int appSelectRet = 0;
    private EnterPinTask enterPinTask;
    private boolean needShowRemoveCard = true;//if need to show removecard msg
    public boolean needSeePhone;

    private IEmvTransProcessListener emvTransProcessListener = new IEmvTransProcessListener() {
        @Override
        public int onWaitAppSelect(boolean isFirstSelect, List<CandidateAID> candList) {
            if (candList == null || candList.size() == 0) {
                return RetCode.EMV_NO_APP;
            }
            AppSelectTask selectAppTask = new AppSelectTask();
            selectAppTask.registerAppSelectListener(selectRetCode -> {
                appSelectRet = selectRetCode;
                appSelectCv.open();
            });
            selectAppTask.startSelectApp(isFirstSelect, candList);
            appSelectCv.block();
            return appSelectRet;
        }

        @Override
        public int onCardHolderPwd(boolean bOnlinePin, boolean supportPINByPass, int leftTimes, byte[] pinData) {
            LogUtils.w(TAG, "onCardHolderPwd, current thread " + Thread.currentThread().getName() + ", id:" + Thread.currentThread().getId());
            enterPinProcess(true, bOnlinePin, supportPINByPass, leftTimes);
            enterPinCv.block();
            return enterPinRet;
        }
    };

    private IClssStatusListener clssStatusListener = new IClssStatusListener() {

        @Override
        public void onRemoveCard() {
            while (!isCardRemove()){
                if(needShowRemoveCard) {
                    EmvDemoApp.getApp().runOnUiThread(() -> {
                        mView.onRemoveCard();
                        needShowRemoveCard = false;
                    });
                }
            }
        }

        @Override
        public boolean needSeePhone() {
            return needSeePhone;
        }
    };

    private IStatusListener statusListener = new IStatusListener() {
        @Override
        public void onReadCardOk() {
            EmvDemoApp.getApp().runOnUiThread(() -> {
                mView.onReadCardOK();
            });
            EmvDemoApp.getApp().getDal().getSys().beep(FREQUENCE_LEVEL_5, 100);
            SystemClock.sleep(750);//blue yellow green clss light remain lit for a minimum of approximately 750ms
        }
    };

    public TransProcessPresenter() {
        deviceImplNeptune = DeviceImplNeptune.getInstance();
        DeviceManager.getInstance().setIDevice(deviceImplNeptune);
    }

    private boolean isCardRemove() {
        try {
//            LogUtils.d(TAG, "isCardRemove");
            EmvDemoApp.getApp().getDal().getPicc(EPiccType.INTERNAL).remove(REMOVE, (byte) 0);
        } catch (PiccDevException e) {
//            LogUtils.e(TAG, "isCardRemove : " + e.getMessage());
            return false;
        }
        return true;
    }

    private void enterPinProcess(boolean isICC, boolean bOnlinePin, boolean supportByPass, int leftTimes) {
        if (enterPinTask != null) {
            enterPinTask.unregisterListener();
            enterPinTask = null;
        }
        LogUtils.w(TAG, "isOnlinePin:" + bOnlinePin + ",leftTimes:" + leftTimes);
        enterPinTask = new EnterPinTask();
        String enterPinPrompt = "Please Enter PIN";
        if (bOnlinePin) {
            String pan = "";
            if (isICC) {
                ByteArray byteArray = new ByteArray();
                EmvProcess.getInstance().getTlv(0x57, byteArray);
                String strTrack2 = CardInfoUtils.getTrack2FromTag57(byteArray.data);
                pan = CardInfoUtils.getPan(strTrack2);
            } else {
                String strTrack2 = ClssProcess.getInstance().getTrack2();
                LogUtils.d(TAG,"ClssProcess getTrack2() = " + strTrack2);
                pan = CardInfoUtils.getPan(strTrack2);
                LogUtils.d(TAG,"ClssProcess getPan() = " + pan);
            }

            enterPinTask.setOnlinePINParam(pan, supportByPass);
            enterPinTask.setEnterPinType(EEnterPinType.ONLINE_PIN);
        } else {
            enterPinPrompt = enterPinPrompt + "(" + leftTimes + ")";
            enterPinTask.setEnterPinType(EEnterPinType.OFFLINE_PCI_MODE);
            String expectLen = (supportByPass ? "0," : "") + "4,5,6,7,8,9,10,11,12";
            if (deviceImplNeptune != null) {
                deviceImplNeptune.setPinInputParam(expectLen.getBytes(), 30 * 1000);
            }
        }
        mView.onUpdatePinLen("");
        enterPinTask.registerListener(new EnterPinTask.IEnterPinListener() {
            @Override
            public void onUpdatePinLen(String pinLen) {
                mView.onUpdatePinLen(pinLen);
            }

            @Override
            public String getEnteredPin() {
                return mView.getEnteredPin();
            }

            @Override
            public void onEnterPinFinish(EnterPinResult enterPinResult) {
                if (enterPinResult.getRet() == EnterPinResult.RET_OFFLINE_PIN_READY) {
                    enterPinRet = EnterPinResult.RET_SUCC;
                } else {
                    enterPinRet = enterPinResult.getRet();
                    mView.onEnterPinFinish(enterPinRet);
                    LogUtils.i(TAG, "onEnterPinFinish, enterPinRet:" + enterPinRet);
                }
                enterPinCv.open();
            }
        });
        mView.onStartEnterPin(enterPinPrompt);
        enterPinTask.startEnterPin();
    }

    @Override
    public void startEmvTrans() {
        EmvProcess.getInstance().registerEmvProcessListener(emvTransProcessListener);
        EmvDemoApp.getApp().runInBackground(() -> {
            TransResult transResult = EmvProcess.getInstance().startTransProcess();
            EmvDemoApp.getApp().runOnUiThread(() -> {
                if (isViewAttached()) {
                    mView.onTransFinish(transResult);
                }
            });
        });
    }

    @Override
    public void startClssTrans() {
        EmvDemoApp.getApp().runInBackground(() -> {
            TransResult transResult = ClssProcess.getInstance().startTransProcess();
            EmvDemoApp.getApp().runOnUiThread(() -> {
                if (isViewAttached()) {
                    mView.onTransFinish(transResult);
                }
            });
        });
    }

    @Override
    public void preTrans(EmvTransParam transParam, boolean needContact) {
        EmvDemoApp.getApp().runInBackground(() -> {
            int ret = 0;
            Config configParam = EmvDemoApp.getParamManager().getConfigParam();
            CapkParam capkParam = EmvDemoApp.getParamManager().getCapkParam();
            LogUtils.d(TAG, "preTrans: terminal country code:" + ConvertHelper.getConvert().bcdToStr(configParam.getTerminalCountryCode()));
            if(needContact) {
                ret = EmvProcess.getInstance().preTransProcess(new EmvProcessParam.Builder(transParam, configParam, capkParam)
                        .setEmvAidList(EmvDemoApp.getParamManager().getEmvAidList())
                        .create());
                LogUtils.d(TAG, "transPreProcess, emv ret:" + ret);
            }

            ret = ClssProcess.getInstance().preTransProcess(new EmvProcessParam.Builder(transParam, configParam, capkParam)
                    .setPayPassAidList(EmvDemoApp.getParamManager().getPayPassAidList())
                    .setPayWaveParam(EmvDemoApp.getParamManager().getPayWaveParam())
                    .setAmexParam(EmvDemoApp.getParamManager().getAmexParam())
                    .create());
            LogUtils.d(TAG, "transPreProcess, clss ret:" + ret);
            needShowRemoveCard = true;
            ClssProcess.getInstance().registerClssStatusListener(clssStatusListener);
            ClssProcess.getInstance().registerStatusListener(statusListener);
        });
    }

    @Override
    public void startMagTrans() {

    }

    @Override
    public void startOnlinePin() {
        // for contactless online pin process
        enterPinProcess(false, true, true, 0);
    }

    @Override
    public void completeEmvTrans(IssuerRspData issuerRspData) {
        EmvDemoApp.getApp().runInBackground(() -> {
            TransResult transResult = EmvProcess.getInstance().completeTransProcess(issuerRspData);
            EmvDemoApp.getApp().runOnUiThread(() -> {
                if (isViewAttached()) {
                    mView.onCompleteTrans(transResult);
                }
            });
        });
    }

    @Override
    public void completeClssTrans(IssuerRspData issuerRspData) {
        EmvDemoApp.getApp().runInBackground(() -> {
            TransResult transResult = ClssProcess.getInstance().completeTransProcess(issuerRspData);
            EmvDemoApp.getApp().runOnUiThread(() -> {
                if (isViewAttached()) {
                    mView.onCompleteTrans(transResult);
                }
            });
        });
    }
}
