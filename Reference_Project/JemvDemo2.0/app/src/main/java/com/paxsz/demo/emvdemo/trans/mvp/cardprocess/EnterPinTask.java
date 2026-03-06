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

import com.pax.commonlib.utils.LogUtils;
import com.pax.dal.IPed;
import com.pax.dal.entity.EKeyCode;
import com.pax.dal.entity.EPedType;
import com.pax.dal.exceptions.EPedDevException;
import com.pax.dal.exceptions.PedDevException;
import com.pax.jemv.clcommon.RetCode;
import com.paxsz.demo.emvdemo.app.EmvDemoApp;
import com.paxsz.demo.emvdemo.entity.EnterPinResult;
import com.paxsz.demo.emvdemo.manager.EEnterPinType;
import com.paxsz.demo.emvdemo.util.CardInfoUtils;
import com.paxsz.demo.emvdemo.util.PedApiUtils;

public class EnterPinTask {
    private static final String TAG = "EnterPinTask";
    private IPed ped;
    private EEnterPinType enterPinType;
    private String onlinePan = "";
    private boolean supportByPass = false;
    private IEnterPinListener listener;

    public EnterPinTask() {
        ped = EmvDemoApp.getApp().getDal().getPed(EPedType.INTERNAL);
    }

    public EnterPinTask registerListener(IEnterPinListener listener) {
        this.listener = listener;
        return this;
    }

    public EnterPinTask setEnterPinType(EEnterPinType enterPinType) {
        this.enterPinType = enterPinType;
        return this;
    }

    public EnterPinTask setOnlinePINParam(String pan, boolean isSupportByPass) {
        this.onlinePan = pan;
        this.supportByPass = isSupportByPass;
        return this;
    }

    public void unregisterListener() {
        this.listener = null;
    }

    public void startEnterPin() {
        if (this.listener == null) {
            return;
        }

        LogUtils.i(TAG, "onlinePan:" + onlinePan + ",enterPinType:" + enterPinType);
        EmvDemoApp.getApp().runInBackground(new Runnable() {
            @Override
            public void run() {
                if (enterPinType == EEnterPinType.ONLINE_PIN) {
                    String pan = CardInfoUtils.getPanBlock(onlinePan, CardInfoUtils.X9_8_WITH_PAN);
                    enterOnlinePin(pan);
                } else if (enterPinType == EEnterPinType.OFFLINE_PCI_MODE) {
                    enterOfflinePin();
                }

            }
        });
    }

    private void enterOfflinePin() {
        try {
            ped.setIntervalTime(1, 1);
            ped.setInputPinListener(new IPed.IPedInputPinListener() {
                @Override
                public void onKeyEvent(EKeyCode eKeyCode) {
                    LogUtils.i(TAG, "enter offline pin:" + eKeyCode);
                    String temp = listener.getEnteredPin();
                    if (eKeyCode == EKeyCode.KEY_CLEAR) {
                        temp = "";
                    } else if (eKeyCode == EKeyCode.KEY_CANCEL) {
                        ped.setInputPinListener(null);
                        listener.onEnterPinFinish(new EnterPinResult(EnterPinResult.RET_CANCEL));
                        return;
                    } else if (eKeyCode == EKeyCode.KEY_ENTER) {
                        if (temp.length() > 3 || temp.length() == 0) {
                            ped.setInputPinListener(null);
                            listener.onEnterPinFinish(new EnterPinResult(EnterPinResult.RET_SUCC));
                            return;
                        }
                    } else {
                        temp += "*";
                    }
                    listener.onUpdatePinLen(temp);
                }
            });
            listener.onEnterPinFinish(new EnterPinResult(EnterPinResult.RET_OFFLINE_PIN_READY));
        } catch (PedDevException e) {
            LogUtils.i(TAG, "enterOfflinePin:" + e);
            if (e.getErrCode() == EPedDevException.PED_ERR_INPUT_TIMEOUT.getErrCodeFromBasement()) {
                listener.onEnterPinFinish(new EnterPinResult(EnterPinResult.RET_TIMEOUT));
            } else {
                listener.onEnterPinFinish(new EnterPinResult(e.getErrCode()));
            }

        }
    }

    private void enterOnlinePin(String panBlock) {
        EnterPinResult pinResult = new EnterPinResult();
        try {
            ped.setIntervalTime(1, 1);
            ped.setInputPinListener(new IPed.IPedInputPinListener() {
                @Override
                public void onKeyEvent(EKeyCode eKeyCode) {
                    String temp;
                    if (eKeyCode == EKeyCode.KEY_CLEAR) {
                        temp = "";
                    } else if (eKeyCode == EKeyCode.KEY_ENTER || eKeyCode == EKeyCode.KEY_CANCEL) {
                        // do nothing
                        return;
                    } else {
                        temp = listener.getEnteredPin();
                        temp += "*";
                    }
                    listener.Clss_CardAuth_Wave(temp);
                }
            });
            byte[] pinBlock = PedApiUtils.getPinBlock(panBlock, supportByPass, 60 * 1000);
            if (pinBlock == null || pinBlock.length == 0) {
                pinResult.setRet(RetCode.EMV_NO_PASSWORD);
            } else {
                pinResult.setRet(EnterPinResult.RET_SUCC);
            }


        } catch (PedDevException e) {
            LogUtils.w(TAG, "EnterPinTask:" + e.getErrCode());
            if (e.getErrCode() == EPedDevException.PED_ERR_INPUT_CANCEL.getErrCodeFromBasement()) {
                pinResult.setRet(EnterPinResult.RET_CANCEL);
            } else if (e.getErrCode() == EPedDevException.PED_ERR_INPUT_TIMEOUT.getErrCodeFromBasement()) {
                pinResult.setRet(EnterPinResult.RET_TIMEOUT);
            } else if (e.getErrCode() == EPedDevException.PED_ERR_NO_KEY.getErrCodeFromBasement()) {
                pinResult.setRet(EnterPinResult.RET_NO_KEY);
            } else {
                pinResult.setRet(RetCode.EMV_RSP_ERR);
            }
        } finally {
            ped.setInputPinListener(null);
        }
        if (listener != null) {
            listener.onEnterPinFinish(pinResult);
        }
    }


    interface IEnterPinListener {
        void onUpdatePinLen(String pin);

        String getEnteredPin();

        void onEnterPinFinish(EnterPinResult enterPinResult);
    }
}
