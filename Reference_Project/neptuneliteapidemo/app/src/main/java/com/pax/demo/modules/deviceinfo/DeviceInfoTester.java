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

package com.pax.demo.modules.deviceinfo;

import android.widget.Toast;

import com.pax.dal.IDeviceInfo;
import com.pax.dal.IDeviceInfo.ESupported;
import com.pax.dal.entity.AppNetworkStats;
import com.pax.dal.entity.BatterySipper;
import com.pax.dal.entity.DeviceInfo;
import com.pax.dal.entity.EmmcInfo;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;
import com.pax.demo.util.Convert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DeviceInfoTester extends BaseTester{

    private IDeviceInfo deviceInfo;
    private static DeviceInfoTester tester;
    
    private DeviceInfoTester(){
        deviceInfo = DemoApp.getDal().getDeviceInfo();
    }
    
    public static DeviceInfoTester getInstance(){
        if(tester == null){
            tester = new DeviceInfoTester();
        }
        return tester;
    }
    
    public String getModuleSupported(){
        Map<Integer, ESupported> moduleSupported = deviceInfo.getModuleSupported();
        logTrue("getModuleSupported");
        String supportStr = "ModuleSupported:\n";
        supportStr += ("MODULE_BT:"+moduleSupported.get(IDeviceInfo.MODULE_BT).name()+"\n");
        supportStr += ("MODULE_CASH_BOX:"+moduleSupported.get(IDeviceInfo.MODULE_CASH_BOX).name()+"\n");
        supportStr += ("MODULE_CUSTOMER_DISPLAY:"+moduleSupported.get(IDeviceInfo.MODULE_CUSTOMER_DISPLAY).name()+"\n");
        supportStr += ("MODULE_ETHERNET:"+moduleSupported.get(IDeviceInfo.MODULE_ETHERNET).name()+"\n");
        supportStr += ("MODULE_FINGERPRINT_READER:"+moduleSupported.get(IDeviceInfo.MODULE_FINGERPRINT_READER).name()+"\n");
        supportStr += ("MODULE_G_SENSOR:"+moduleSupported.get(IDeviceInfo.MODULE_G_SENSOR).name()+"\n");
        supportStr += ("MODULE_HDMI:"+moduleSupported.get(IDeviceInfo.MODULE_HDMI).name()+"\n");
        supportStr += ("MODULE_ICC:"+moduleSupported.get(IDeviceInfo.MODULE_ICC).name()+"\n");
        supportStr += ("MODULE_ID_CARD_READER:"+moduleSupported.get(IDeviceInfo.MODULE_ID_CARD_READER).name()+"\n");
        supportStr += ("MODULE_KEYBOARD:"+moduleSupported.get(IDeviceInfo.MODULE_KEYBOARD).name()+"\n");
        supportStr += ("MODULE_MAG:"+moduleSupported.get(IDeviceInfo.MODULE_MAG).name()+"\n");
        supportStr += ("MODULE_PED:"+moduleSupported.get(IDeviceInfo.MODULE_PED).name()+"\n");
        supportStr += ("MODULE_PICC:"+moduleSupported.get(IDeviceInfo.MODULE_PICC).name()+"\n");
        supportStr += ("MODULE_PRINTER:"+moduleSupported.get(IDeviceInfo.MODULE_PRINTER).name()+"\n");
        supportStr += ("MODULE_SM:"+moduleSupported.get(IDeviceInfo.MODULE_SM).name()+"\n");
        supportStr += ("MODULE_MODEM:"+moduleSupported.get(IDeviceInfo.MODULE_MODEM).name()+"\n");
        supportStr += ("MODULE_SCANNER_HW:"+moduleSupported.get(IDeviceInfo.MODULE_SCANNER_HW).name()+"\n");
        supportStr += ("MODULE_WIFI:"+moduleSupported.get(IDeviceInfo.MODULE_WIFI).name()+"\n");
        supportStr += ("MODULE_WIFI5G:"+moduleSupported.get(IDeviceInfo.MODULE_WIFI5G).name()+"\n");
        supportStr += ("MODULE_FRONT_PICC:"+moduleSupported.get(IDeviceInfo.MODULE_FRONT_PICC).name()+"\n");
        supportStr += ("MODULE_SD_CARD_SLOT:"+moduleSupported.get(IDeviceInfo.MODULE_SD_CARD_SLOT).name()+"\n");
        if (moduleSupported.size() >=21) {
            supportStr += ("MODULE_SIM_CARD_SLOT:"+moduleSupported.get(IDeviceInfo.MODULE_SIM_CARD_SLOT).name()+"\n");
        }
        return supportStr;
    }
    
    public String getUsageCount(){
        String usageCount = "usageCount:\n";
        usageCount += ("MODULE_MAG:"+deviceInfo.getUsageCount(IDeviceInfo.MODULE_MAG)+"\n");
        usageCount += ("MODULE_ICC:"+deviceInfo.getUsageCount(IDeviceInfo.MODULE_ICC)+"\n");
        usageCount += ("MODULE_PICC:"+deviceInfo.getUsageCount(IDeviceInfo.MODULE_PICC)+"\n");
        logTrue("getUsageCount");
        return usageCount ;
    }
    
    public String getFailCount(){
        String failCount = "failCount:\n";
        failCount += ("MODULE_MAG:"+deviceInfo.getFailCount(IDeviceInfo.MODULE_MAG)+"\n");
        failCount += ("MODULE_ICC:"+deviceInfo.getFailCount(IDeviceInfo.MODULE_ICC)+"\n");
        failCount += ("MODULE_PICC:"+deviceInfo.getFailCount(IDeviceInfo.MODULE_PICC)+"\n");
        logTrue("getFailCount");
        return failCount ;
    }


    public String getBatteryUsages(){
        StringBuilder batteryUsages = new StringBuilder("batteryUsages:\n");
        List<BatterySipper> list = deviceInfo.getBatteryUsages();
        if (list == null || list.isEmpty()){
            batteryUsages = new StringBuilder("get battery usages failed");
            logErr("getBatteryUsages", "get battery usages failed");
        } else {
            for (BatterySipper batterySipper : list) {
                batteryUsages.append("Package name:").append(batterySipper.getPackageName()).append(" Type:").append(batterySipper.getType())
                        .append(" Percent:").append(batterySipper.getPercent()).append(" Value:").append(batterySipper.getValue()).append("\n");
            }
            logTrue("getBatteryUsages");
        }
        return batteryUsages.toString();
    }

    public String getPrinterStatus(){
        String printerStatus = "printerStatus:\n";
        printerStatus += deviceInfo.getPrinterStatus();
        logTrue("getPrinterStatus");
        return printerStatus;
    }

    public String getTrafficOfEachApp(int type){
        StringBuilder trafficOfEachApp = new StringBuilder("trafficOfEachApp:\n");
        long end = System.currentTimeMillis();
        long start = end - 3 * 24 * 3600 * 1000;
        Map<String, Long> map = deviceInfo.getTrafficOfEachApp(type, start, end);
        if (map == null || map.isEmpty()){
            trafficOfEachApp = new StringBuilder("get traffic failed");
            logErr("getTrafficOfEachApp", "get traffic failed");
        } else {
            for (Map.Entry entry: map.entrySet()){
                trafficOfEachApp.append("app name:").append(entry.getKey().toString()).append(" traffic:").
                        append(entry.getValue().toString()).append("\n");
            }
            logTrue("getTrafficOfEachApp");
        }
        return trafficOfEachApp.toString();
    }

    public String getDeviceInfo(){
        String strDeviceInfo = "deviceInfo:\n";
        try {
            DeviceInfo info = deviceInfo.getDeviceInfo();
            strDeviceInfo += ("BLEVersion:" + Convert.bytes2String(info.getBLEVersion()) + "\n");
            strDeviceInfo += ("deviceType:" + info.getDeviceType() + "\n");
            strDeviceInfo += ("iccReadSlotNum:" + info.getIccReadSlotNum() + "\n");
            strDeviceInfo += ("iccReaderSlotList:" + Convert.getInstance().bcdToStr(info.getIccReaderSlotList()) + "\n");
            strDeviceInfo += ("magReaderCombined:" + info.getMagReaderCombined() + "\n");
            strDeviceInfo += ("pinpadPortsNum:" + info.getPinpadPortsNum() + "\n");
            strDeviceInfo += ("pinpadPorts:" + Convert.getInstance().bcdToStr(info.getPinpadPorts()) + "\n");
            strDeviceInfo += ("platformId:" + info.getPlatformId() + "\n");
            strDeviceInfo += ("printerMaxDotLine:" + info.getPrinterMaxDotLine() + "\n");
            strDeviceInfo += ("printerMaxPageWidth:" + info.getPrinterMaxPageWidth() + "\n");
            strDeviceInfo += ("printerStep:" + info.getPrinterStep() + "\n");
            strDeviceInfo += ("printerType:" + info.getPrinterType() + "\n");
            strDeviceInfo += ("RS232Ports:" + Convert.getInstance().bcdToStr(info.getRS232Ports()) + "\n");
            strDeviceInfo += ("RS232PortsNum:" + info.getRS232PortsNum() + "\n");
            strDeviceInfo += ("usbDevPort:" + info.getUsbDevPort() + "\n");
            strDeviceInfo += ("usbHostPort:" + info.getUsbHostPort() + "\n");
            strDeviceInfo += ("usbHostPortExt:" + Convert.getInstance().bcdToStr(info.getUsbHostPortExt()) + "\n");
            strDeviceInfo += ("reserved:" + Convert.getInstance().bcdToStr(info.getReserved()) + "\n");
        } catch (Exception e) {
            e.printStackTrace();
            strDeviceInfo = e.getLocalizedMessage();
            logErr("getDeviceInfo", e.getLocalizedMessage());
        }
        return strDeviceInfo;
    }


    public String getBatteryCycleCount(){
        String batteryCycleCount = "batteryCycleCount:\n";
        try {
            batteryCycleCount += deviceInfo.getBatteryCycleCount();
            logTrue("getBatteryCycleCount");
        } catch (Exception e) {
            e.printStackTrace();
            batteryCycleCount = e.getLocalizedMessage();
            logErr("getBatteryCycleCount", e.getLocalizedMessage());
        }
        return batteryCycleCount;
    }

    public EmmcInfo[] getEmmcLifeInfo(){
        EmmcInfo[] info = null;
        try {
            info = deviceInfo.getEmmcLifeInfo();
            logTrue("getEmmcLifeInfo");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("getEmmcLifeInfo", e.getLocalizedMessage());
        }
        return info;
    }

    public long getFrontCameraOpenCount(){
        long info = 0;
        try {
            info = deviceInfo.getFrontCameraOpenCount();
            logTrue("getFrontCameraOpenCount");
            Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            logErr("getFrontCameraOpenCount", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return info;
    }

    public long getRearCameraOpenCount(){
        long info = 0;
        try {
            info = deviceInfo.getRearCameraOpenCount();
            logTrue("getRearCameraOpenCount");
            Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            logErr("getRearCameraOpenCount", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return info;
    }

    public AppNetworkStats getAppNetworkStats(String app){
        AppNetworkStats mAppNetworkStats = null;
        try {
            mAppNetworkStats = deviceInfo.getAppNetworkStats(app);
            logTrue("getAppNetworkStats");
            Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            logErr("getAppNetworkStats", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return mAppNetworkStats;
    }

    public void cleanAppCache(String packageName, IDeviceInfo.ResultCallback resultCallback){
        try {
            deviceInfo.cleanAppCache(packageName,resultCallback);
            logTrue("cleanAppCache");
            Toast.makeText(DemoApp.appContext,"succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            logErr("cleanAppCache", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
