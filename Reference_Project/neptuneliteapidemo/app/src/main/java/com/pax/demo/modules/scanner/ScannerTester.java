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

package com.pax.demo.modules.scanner;

import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.pax.dal.IScanner;
import com.pax.dal.IScanner.IScanListener;
import com.pax.dal.entity.EScannerType;
import com.pax.dal.entity.ScanResult;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

import java.util.HashMap;
import java.util.Map;

public class ScannerTester extends BaseTester {
    private static ScannerTester cameraTester;

    private static EScannerType scannerType;

    private IScanner scanner;

    private ScannerTester(EScannerType type) {
        ScannerTester.scannerType = type;
        logTrue(scannerType.name());
        scanner = DemoApp.getDal().getScanner(scannerType);
    }

    public static ScannerTester getInstance(EScannerType type) {
        if (cameraTester == null || type != scannerType) {
            cameraTester = new ScannerTester(type);
        }
        return cameraTester;
    }

    public void scan(final Handler handler,int timeout) {
        scanner.open();
        logTrue("open");
        setTimeOut(timeout);
        scanner.setContinuousTimes(1);
        scanner.setContinuousInterval(1000);
        scanner.start(new IScanListener() {

            @Override
            public void onRead(ScanResult result) {
                logTrue("read:" + result.getContent());
                Message message = Message.obtain();
                message.what = 0;
                message.obj = result.getContent();
                handler.sendMessage(message);
            }

            @Override
            public void onFinish() {
                logTrue("onFinish");
                close();
            }

            @Override
            public void onCancel() {
                logTrue("onCancel");
                close();
            }
        });

        logTrue("start");
    }

    public void close() {
        scanner.close();
        logTrue("close");
    }
    
    public void setTimeOut(int timeout){
        scanner.setTimeOut(timeout);
        logTrue("setTimeOut");
    }

    public boolean setFlashOn(boolean isOn) {
        boolean result = scanner.setFlashOn(isOn);
        if (result) {
            logTrue("setFlashOn");
        } else {
            logErr("setFlashOn", "set flash on failed");
        }
        return result;
    }

    public boolean setScannerType(int type) {
        boolean result = scanner.setScannerType(type);
        if (result) {
            logTrue("setScannerType");
        } else {
            logErr("setScannerType", "set scanner type failed");
        }
        return result;
    }

    public boolean setBarcodeParam(String type,boolean open) {
        Map param = new HashMap<>();
        param.put(type,open);
        scanner.open();
        boolean result = scanner.setBarcodeParam(param);
        if (result) {
            logTrue("setBarcodeParam");
            Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
        } else {
            logErr("setBarcodeParam", "setBarcodeParam failed");
            Toast.makeText(DemoApp.appContext,"error",Toast.LENGTH_SHORT).show();
        }
        scanner.close();
        return result;
    }

}
