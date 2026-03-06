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

package com.pax.demo.modules.scannerhw;

import com.pax.dal.IPrinter;
import com.pax.dal.IScannerHw;
import com.pax.dal.entity.ScanResultRaw;
import com.pax.dal.exceptions.ScannerHwDevException;
import com.pax.demo.base.DemoApp;
import com.pax.demo.modules.printer.PrinterTester;
import com.pax.demo.util.BaseTester;
import com.pax.demo.util.Convert;

import java.util.Map;

public class ScannerHwTester extends BaseTester {

    private static ScannerHwTester scannerHwTester;
    private IScannerHw scannerHw;

    private ScannerHwTester() {
        scannerHw = DemoApp.getDal().getScannerHw();
    }

    public static ScannerHwTester getInstance() {
        if (scannerHwTester == null) {
            scannerHwTester = new ScannerHwTester();
        }
        return scannerHwTester;
    }


    public String getBarcodeMap() {
        StringBuilder barcodeMap = new StringBuilder("barcodeMap:\n");
        try {
            Map<String, Integer> map = scannerHw.getBarcodeMap();
            if (map == null || map.isEmpty()) {
                barcodeMap = new StringBuilder("get barcode map failed");
                logErr("getBarcodeMap", "get barcode map failed");
            }else {
                for (Map.Entry entry: map.entrySet()){
                    barcodeMap.append("barcode type:").append(entry.getKey().toString()).append(" status:").
                            append(entry.getValue().toString()).append("\n");
                }
                logTrue("getBarcodeMap");
            }
        } catch (ScannerHwDevException e) {
            e.printStackTrace();
            barcodeMap.append(e.getLocalizedMessage());
            logErr("getBarcodeMap", e.getLocalizedMessage());
        }
        return barcodeMap.toString();
    }

    public String getConfig() {
        StringBuilder config = new StringBuilder("config:\n");
        try {
            Map<String, String> map = scannerHw.getConfig();
            if (map == null || map.isEmpty()) {
                config = new StringBuilder("get config failed");
                logErr("getBarcodeMap", "get config failed");
            }else {
                for (Map.Entry entry: map.entrySet()){
                    config.append("config:").append(entry.getKey().toString()).append(" status:").
                            append(entry.getValue().toString()).append("\n");
                }
                logTrue("getConfig");
            }
        } catch (ScannerHwDevException e) {
            e.printStackTrace();
            config.append(e.getLocalizedMessage());
            logErr("getConfig", e.getLocalizedMessage());
        }
        return config.toString();
    }

    public String readRaw() {
        String result = "readRaw:\n";
        try {
            ScanResultRaw scanResultRaw = scannerHw.readRaw(9900);
            result += "content:";
            result += Convert.bytes2String(scanResultRaw.getContent()) + "\n";
            result += "format:";
            result += scanResultRaw.getFormat();
            logTrue("readRaw");
        } catch (ScannerHwDevException e) {
            e.printStackTrace();
            result += e.getLocalizedMessage();
            logErr("readRaw", e.getLocalizedMessage());
        }
        return result;
    }

    public String getSupportFormat () {
        StringBuilder result = new StringBuilder("getSupportFormat:\n");
        try {
            int[] formatList = scannerHw.getSupportFormat();
            if (formatList == null || formatList.length == 0) {
                result.append("get support format failed");
                logErr("getSupportFormat", "get support format failed");
            } else {
                for (int i : formatList) {
                    result.append(" ").append(i);
                }
                logTrue("getSupportFormat");
            }
        } catch (ScannerHwDevException e) {
            e.printStackTrace();
            result.append(e.getLocalizedMessage());
            logErr("getSupportFormat", e.getLocalizedMessage());
        }
        return result.toString();
    }

    public String getPreviewFormat () {
        StringBuilder result = new StringBuilder("getPreviewFormat:\n");
        try {
            int format = scannerHw.getPreviewFormat();
            result.append(format);
            logTrue("getPreviewFormat");
        } catch (ScannerHwDevException e) {
            e.printStackTrace();
            result.append(e.getLocalizedMessage());
            logErr("getPreviewFormat", e.getLocalizedMessage());
        }
        return result.toString();
    }

    public String setPreviewFormat(int format) {
        String result;
        try {
            scannerHw.setPreviewFormat(format);
            result = "success";
            logTrue("setPreviewFormat");
        } catch (ScannerHwDevException e) {
            e.printStackTrace();
            result = e.getLocalizedMessage();
            logErr("setPreviewFormat",e.getLocalizedMessage());
        }
        return result;
    }

}
