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

package com.pax.demo.modules.comm;

import com.pax.dal.IDalCommManager;
import com.pax.dal.entity.ApnInfo;
import com.pax.dal.entity.ERoute;
import com.pax.dal.entity.LanParam;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

import java.util.List;

public class CommTester extends BaseTester {

    private static CommTester tester;
    private IDalCommManager commManager;

    private CommTester() {
        commManager = DemoApp.getDal().getCommManager();
    }

    public synchronized static CommTester getInstance() {
        if (tester == null) {
            tester = new CommTester();
        }
        return tester;
    }

    public boolean enableMultiPath() {
        boolean b = commManager.enableMultiPath();
        logTrue("enableMultiPath");
        return b;
    }

    public boolean disableMultiPath() {
        boolean b = commManager.disableMultiPath();
        logTrue("disableMultiPath");
        return b;
    }

    public boolean setRoute(String ip, ERoute route) {
        boolean b = commManager.setRoute(ip, route);
        logTrue("setRoute");
        return b;
    }

    public int switchApn(String name, String newApn, String username, String password, int authType) {
        int res = commManager.switchAPN(name, newApn, username, password, authType);
        if (res == 1)
            logTrue("switchAPN");
        else
            logErr("switchAPN", "");
        return res;
    }

    public String getLanConfig() {
        String resultStr = "getLanConfig:\n";
        LanParam lanParam = commManager.getLanConfig();
        if (lanParam != null) {
            resultStr += "DHCP:" + lanParam.isDhcp() + "\n";
            resultStr += "localIp:" + lanParam.getLocalIp() + "\n";
            resultStr += "subnetMask" + lanParam.getSubnetMask() + "\n";
            resultStr += "gateway:" + lanParam.getGateway() + "\n";
            resultStr += "dns1:" + lanParam.getDns1() + "\n";
            resultStr += "dns2:" + lanParam.getDns2();
            logTrue("getLanConfig");
        } else {
            resultStr = "get Lan config failed";
            logErr("getLanConfig", "get Lan config failed");
        }
        return resultStr;
    }

    public String getCurrentApn() {
        String resultStr = "getCurrentApn:\n";
        ApnInfo apnInfo = commManager.getCurrentApn();
        if (apnInfo != null) {
            resultStr += apnInfo.toString();
            logTrue("getCurrentApn");
        } else {
            resultStr  = "get apn failed";
            logErr("getCurrentApn", "get apn failed");
        }
        return resultStr;
    }

    public String getRouteList() {
        StringBuilder resultStr = new StringBuilder("getRouteList\n");
        List<String> list = commManager.getRouteList();
        if (list == null || list.isEmpty()) {
            resultStr = new StringBuilder("get route list failed");
            logErr("getRouteList", "get route list failed");
        } else {
            for (String route: list) {
                resultStr.append(route).append("\n");
            }
            logTrue("getRouteList");
        }
        return resultStr.toString();
    }

    public boolean getEthernetIfaceState() {
        boolean b = commManager.getEthernetIfaceState();
        logTrue("getEthernetIfaceState");
        return b;
    }

    public boolean isMultiPathEnabled() {
        boolean b = commManager.isMultiPathEnabled();
        logTrue("isMultiPathEnabled");
        return b;
    }
}
