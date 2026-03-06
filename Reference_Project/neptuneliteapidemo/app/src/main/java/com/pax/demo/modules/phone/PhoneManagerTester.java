/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology (Shenzhen) Co., Ltd. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology (Shenzhen) Co., Ltd. and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2017-2023 PAX Computer Technology (Shenzhen) Co., Ltd. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                         Author                        Action
 * 2017/12/20                   PAX                     Create/Add/Modify/Delete
 * ===========================================================================================
 */

package com.pax.demo.modules.phone;

import com.pax.dal.IPhoneManager;
import com.pax.dal.exceptions.PhoneDevException;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class PhoneManagerTester extends BaseTester {

    private static PhoneManagerTester phoneManagerTester;
    private IPhoneManager iPhoneManager = null;

    private PhoneManagerTester() {
        iPhoneManager = DemoApp.getDal().getPhoneManager();
    }

    public static PhoneManagerTester getInstance() {
        if (phoneManagerTester == null) {
            phoneManagerTester = new PhoneManagerTester();
        }
        return phoneManagerTester;
    }


    public String getSubId(int slotId) {
        String resultStr = "";
        try {
            int[] ret = iPhoneManager.getSubId(slotId);
            if (ret == null || ret.length == 0) {
                resultStr = "get sub id failed";
                logErr("getSubId", "get sub id failed");
            } else {
                for (int i : ret) {
                    resultStr  = i + " ";
                }
                logTrue("getSubId");
            }
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getSubId", e.getLocalizedMessage());
        }
        return  resultStr;
    }

    public String getSubscriberId(int slotId) {
        String resultStr = "";
        try {
            int[] ret = iPhoneManager.getSubId(slotId);
            if (ret == null || ret.length == 0) {
                resultStr = "getSubscriberId failed";
                logErr("getSubscriberId", "getSubscriberId failed");
            } else if(ret.length >0 ){
                resultStr = iPhoneManager.getSubscriberId(ret[0]);
                logTrue("getSubscriberId");
            }
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getSubscriberId", e.getLocalizedMessage());
        }
        return  resultStr;
    }

    public String setDefaultDataSubId(int subId) {
        String resultStr = "";
        try {
            iPhoneManager.setDefaultDataSubId(subId);
            resultStr = "success";
            logTrue("setDefaultDataSubId");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("setDefaultDataSubId",e.getLocalizedMessage());
        }
        return resultStr;
    }

    public boolean isAirplaneModeEnable() {
        boolean result;
        try {
            result = iPhoneManager.isAirplaneModeEnable();
            logTrue("isAirplaneModeEnable");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            result = false;
            logErr("isAirplaneModeEnable",e.getLocalizedMessage());
        }
        return result;
    }

    public String enableAirplaneMode(boolean enable) {
        String resultStr = "";
        try {
            iPhoneManager.enableAirplaneMode(enable);
            resultStr = "success";
            logTrue("enableAirplaneMode");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("enableAirplaneMode",e.getLocalizedMessage());
        }
        return resultStr;
    }

    public boolean isSimCardLock(int subId) {
        boolean result;
        try {
            result = iPhoneManager.isSimCardLock(subId);
            logTrue("isSimCardLock");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            result = false;
            logErr("isSimCardLock",e.getLocalizedMessage());
        }
        return result;
    }

    public boolean isDualSim() {
        boolean result;
        try {
            result = iPhoneManager.isDualSim();
            logTrue("isDualSim");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            result = false;
            logErr("isDualSim",e.getLocalizedMessage());
        }
        return result;
    }

    public boolean isVSimDataInUse() {
        boolean result;
        try {
            result = iPhoneManager.isVSimDataInUse();
            logTrue("isVSimDataInUse");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            result = false;
            logErr("isVSimDataInUse",e.getLocalizedMessage());
        }
        return result;
    }

    public String turnOnVSimData() {
        String resultStr = "";
        try {
            iPhoneManager.turnOnVSimData();
            resultStr = "success";
            logTrue("turnOnVSimData");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("turnOnVSimData",e.getLocalizedMessage());
        }
        return resultStr;
    }

    public String turnOffVSimData() {
        String resultStr = "";
        try {
            iPhoneManager.turnOffVSimData();
            resultStr = "success";
            logTrue("turnOffVSimData");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("turnOffVSimData",e.getLocalizedMessage());
        }
        return resultStr;
    }

    public String getSIMInfo() {
        String resultStr = "";
        try {
            String[] ret = iPhoneManager.getSIMInfo();
            if (ret == null || ret.length == 0) {
                resultStr = "get sim info failed";
                logErr("getSIMInfo", "get sim info failed");
            } else {
                resultStr = "DeviceId:" + ret[0] + "\n" + "IMEI:" + ret[1] + "\n" + "ICCID:" + ret[2];
                logTrue("getSIMInfo");
            }
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getSIMInfo", e.getLocalizedMessage());
        }
        return  resultStr;
    }

    public String getSIMInfoWithSlotId(int slotId) {
        String resultStr = "";
        try {
            String[] ret = iPhoneManager.getSIMInfoWithSlotId(slotId);
            if (ret == null || ret.length == 0) {
                resultStr = "get sim info failed";
                logErr("getSIMInfoWithSlotId", "get sim info failed");
            } else {
                resultStr = "DeviceId:" + ret[0] + "\n" + "IMEI:" + ret[1] + "\n" + "ICCID:" + ret[2];
                logTrue("getSIMInfoWithSlotId");
            }
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getSIMInfoWithSlotId", e.getLocalizedMessage());
        }
        return  resultStr;
    }

    public String setNetworkSelectionModeAutomatic(int subId) {
        String resultStr = "";
        try {
            iPhoneManager.setNetworkSelectionModeAutomatic(subId);
            resultStr = "success";
            logTrue("setNetworkSelectionModeAutomatic");
        } catch (PhoneDevException e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("setNetworkSelectionModeAutomatic", e.getLocalizedMessage());
        }
        return resultStr;
    }
}
