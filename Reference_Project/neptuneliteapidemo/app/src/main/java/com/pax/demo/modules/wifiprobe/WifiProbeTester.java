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

package com.pax.demo.modules.wifiprobe;

import java.util.List;

import android.os.Handler;
import android.os.Message;

import com.pax.dal.IWifiProbe;
import com.pax.dal.IWifiProbe.ProbeListener;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class WifiProbeTester extends BaseTester {

    private static WifiProbeTester probeTester;
    private IWifiProbe wifiProbe;

    private WifiProbeTester() {

    }

    public static synchronized WifiProbeTester getInstance() {
        if (probeTester == null) {
            probeTester = new WifiProbeTester();
        }
        probeTester.wifiProbe = DemoApp.getDal().getWifiProbe();
        return probeTester;
    }

    public void start(final Handler handler) {
        wifiProbe.start(new ProbeListener() {

            @Override
            public void onProbeItem(String probeinfo, String rssi) {
                logTrue("onProbeItem");
                Message.obtain(handler, 0, probeinfo + rssi).sendToTarget();
            }

            @Override
            public void onFinish() {
                logTrue("onFinish");
                Message.obtain(handler, 0, "finished").sendToTarget();
            }

            @Override
            public void onFailure(int reason) {
                logTrue("onFailure");
                Message.obtain(handler, 0, "failed").sendToTarget();
            }
        });
        logTrue("start");
    }

    public void stop() {
        wifiProbe.stop();
        logTrue("stop");
    }

    public int getStatus() {
        int res = wifiProbe.getStatus();
        logTrue("getStatus");
        return res;
    }

    public List<String> getResults() {
        List<String> resList = wifiProbe.getResults();
        logTrue("getResults");
        return resList;
    }
}
