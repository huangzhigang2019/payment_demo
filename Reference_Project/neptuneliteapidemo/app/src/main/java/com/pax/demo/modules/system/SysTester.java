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

package com.pax.demo.modules.system;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.Locale;
import java.util.Map;

import com.pax.dal.ISys;
import com.pax.dal.entity.ASCaller;
import com.pax.dal.entity.BaseInfo;
import com.pax.dal.entity.EBeepMode;
import com.pax.dal.entity.ENavigationKey;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.dal.entity.ETouchMode;
import com.pax.dal.entity.PosMenu;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;
import com.pax.demo.util.Convert;
import com.pax.neptunelite.api.NeptuneLiteUser;

import javax.crypto.Cipher;

public class SysTester extends BaseTester {

    private static SysTester sysTester;
    private ISys iSys = null;

    private SysTester() {
        iSys = DemoApp.getDal().getSys();
    }

    public static SysTester getInstance() {
        if (sysTester == null) {
            sysTester = new SysTester();
        }
        return sysTester;
    }

    public void beep(final EBeepMode beepMode, final int delayTime) {
        iSys.beep(beepMode, delayTime);
        logTrue("beep");
    }

    public String getTerminfo() {
        Map<ETermInfoKey, String> termInfo = iSys.getTermInfo();
        logTrue("getTerminfo");
        StringBuilder termInfoStr = new StringBuilder();
        for (ETermInfoKey key : ETermInfoKey.values()) {
            termInfoStr.append(key.name() + ":" + termInfo.get(key) + "\n");
        }
        return termInfoStr.toString();
    }

    public String getRadom(int len) {
        byte[] random = iSys.getRandom(len);
        if (random != null) {
            logTrue("getRadom");
            return Convert.getInstance().bcdToStr(random);
        } else {
            logErr("getRadom", "return null");
            return "null";
        }

    }

    public String getDevInterfaceVer() {
        String verString = iSys.getDevInterfaceVer();
        logTrue("getDevInterfaceVer");
        return "version of device interface:" + verString;
    }

    public void showNavigationBar(boolean flag) {
        iSys.showNavigationBar(flag);
        logTrue("showNavigationBar");
    }

    public void enableNavigationBar(boolean flag) {
        iSys.enableNavigationBar(flag);
        logTrue("enableNavigationBar");
    }

    public void enableNavigationKey(ENavigationKey navigationKey, boolean flag) {
        iSys.enableNavigationKey(navigationKey, flag);
        logTrue("enableNavigationKey");
    }

    public boolean isNavigationBarVisible() {
        boolean res = true;
        res = iSys.isNavigationBarVisible();
        logTrue("isNavigationBarVisible");
        return res;
    }

    public boolean isNavigationBarEnabled() {
        boolean res = true;
        res = iSys.isNavigationBarEnabled();
        logTrue("isNavigationBarEnabled");
        return res;
    }

    public boolean isNavigationKeyEnabled(ENavigationKey navigationKey) {
        boolean res = true;
        res = iSys.isNavigationKeyEnabled(navigationKey);
        logTrue("isNavigationKeyEnabled");
        return res;
    }

    public void showStatusBar(boolean flag) {
        iSys.showStatusBar(flag);
        logTrue("showStatusBar");
    }

    public void enableStatusBar(boolean flag) {
        iSys.enableStatusBar(flag);
        logTrue("enableStatusBar");
    }

    public boolean isStatusBarEnabled() {
        boolean res = true;
        res = iSys.isStatusBarEnabled();
        logTrue("isStatusBarEnabled");
        return res;
    }

    public boolean isStatusBarVisible() {
        boolean res = true;
        res = iSys.isStatusBarVisible();
        logTrue("isStatusBarVisible");
        return res;
    }

    public void resetStatusBar() {
        iSys.resetStatusBar();
        logTrue("resetStatusBar");
    }

    public void enablePowerKey(boolean flag) {
        iSys.enablePowerKey(flag);
        logTrue("enablePowerKey");
    }

    public boolean isPowerKeyEnabled() {
        boolean res = true;
        res = iSys.isPowerKeyEnabled();
        logTrue("isPowerKeyEnabled");
        return res;
    }

    public void setSettingsNeedPassword(boolean flag) {
        iSys.setSettingsNeedPassword(flag);
        logTrue("setSettingsNeedPassword");
    }

    public void reboot() {
        iSys.reboot();
        logTrue("reboot");
    }

    public void shutdown() {
        iSys.shutdown();
        logTrue("shutdown");
    }

    public void switchTouchMode(ETouchMode touchMode) {
        iSys.switchTouchMode(touchMode);
        logTrue("switchTouchMode");
    }

    public int getAppLogs(String storePath, String startDate, String endDate) {
        int res = iSys.getAppLogs(storePath, startDate, endDate);
        logTrue("getAppLogs");
        return res;
    }

    public String readTUSN() {
        String res = iSys.readTUSN();
        logTrue("readTUSN");
        return res;
    }
    
    public boolean isOnBase(){
        boolean isOnBase = iSys.isOnBase();
        logTrue("isOnBase");
        return isOnBase;
    }
    
    public BaseInfo getBaseInfo(){
        BaseInfo baseInfo = iSys.getBaseInfo();
        logTrue("getBaseInfo");
        return baseInfo;
    }
    
    public String getSystemLanguage(){
        String language = iSys.getSystemLanguage();
        logTrue("getSystemLanguage");
        return language;
    }
    
    public void setSystemLanguage(Locale locale){
        iSys.setSystemLanguage(locale);
        logTrue("setSystemLanguage");
    }
    
    public String getPN(){
        String pn = iSys.getPN();
        logTrue("getPN");
        return pn;
    }

    public void setScreenBrightness(int level) {
        iSys.setScreenBrightness(level);
        logTrue("setScreenBrightness");
    }

    public String getScreenBrightness() {
        String resultStr = "";
        try {
            resultStr = String.valueOf(iSys.getScreenBrightness());
            logTrue("getScreenBrightness");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getScreenBrightness", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public void setUsbMode(int mode) {
        try {
            iSys.setUsbMode(mode);
            logTrue("setUsbMode");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setUsbMode", e.getLocalizedMessage());
        }
    }

    public String getUsbMode() {
        String resultStr = "";
        try {
            iSys.getUsbMode();
            resultStr = "success";
            logTrue("getUsbMode");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getUsbMode", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public void enableVolumeKey(boolean enable) {
        try {
            iSys.enableVolumeKey(enable);
            logTrue("enableVolumeKey");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("enableVolumeKey", e.getLocalizedMessage());
        }
    }

    public boolean isVolumeKeyEnable() {
        boolean result;
        try {
            result = iSys.isVolumeKeyEnable();
            logTrue("isVolumeKeyEnable");
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
            logErr("isVolumeKeyEnable", e.getLocalizedMessage());
        }
        return result;
    }

    public void enableLocation(boolean enable) {
        try {
            iSys.enableLocation(enable);
            logTrue("enableLocation");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("enableLocation", e.getLocalizedMessage());
        }
    }

    public void goToSleep() {
        try {
            iSys.goToSleep();
            logTrue("goToSleep");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("goToSleep", e.getLocalizedMessage());
        }
    }

    public String getSecurityInfo() {
        String resultStr = "";
        try {
            Bundle bundle = iSys.getSecurityInfo();
            if (bundle == null) {
                resultStr = "get security info failed";
                logErr("getSecurityInfo", "get security info failed");
            }else {
                resultStr += "PCI Version:" + bundle.get("pciver") + "\n";
                resultStr += "Security Version:" + bundle.get("secver") + "\n";
                resultStr += "Firmware Version:" + bundle.get("firmver");
                logTrue("getSecurityInfo");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getSecurityInfo", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public void setRebootTime(String time) {
        try {
            iSys.setRebootTime(time);
            logTrue("setRebootTime");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setRebootTime", e.getLocalizedMessage());
        }
    }

    public String getRebootTime(){
        String resultStr = "";
        try {
            resultStr = iSys.getRebootTime();
            logTrue("getRebootTime");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getRebootTime", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public void setRebootTimeEnable(boolean enable) {
        try {
            iSys.setRebootTimeEnable(enable);
            logTrue("setRebootTimeEnable");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setRebootTimeEnable", e.getLocalizedMessage());
        }
    }

    public void enableScreenshot(boolean enable) {
        try {
            iSys.enableScreenshot(enable);
            logTrue("enableScreenshot");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("enableScreenshot", e.getLocalizedMessage());
        }
    }

    public void lightControl(byte type, byte mode) {
        iSys.lightControl(type, mode);
        logTrue("lightControl");
    }

    public boolean setTetheringIp(int iface, String ip) {
        try {
            iSys.setTetheringIp(iface,ip);
            logTrue("setTetheringIp");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setTetheringIp", e.getLocalizedMessage());
            return false;
        }
    }

    public String getTetheringIp(int iface) {
        String resultStr = "";
        try {
            resultStr = iSys.getTetheringIp(iface);
            logTrue("getTetheringIp");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getTetheringIp", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public void setChargerScreenDisabled(boolean disable) {
        try {
            iSys.setChargerScreenDisabled(disable);
            logTrue("setChargerScreenDisabled");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setChargerScreenDisabled", e.getLocalizedMessage());
        }
    }

    public void setApplicationNeedPassword(String pkgName,String password) {
        try {
            iSys.setApplicationNeedPassword(pkgName,password);
            logTrue("setApplicationNeedPassword");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setApplicationNeedPassword", e.getLocalizedMessage());
        }
    }

    public void setSecScreenBrightness(int brightness) {
        try {
            iSys.setSecScreenBrightness(brightness);
            logTrue("setSecScreenBrightness");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("setSecScreenBrightness", e.getLocalizedMessage());
        }
    }

    public String getSecScreenBrightness() {
        String resultStr = "";
        try {
            resultStr = String.valueOf(iSys.getSecScreenBrightness());
            logTrue("getSecScreenBrightness");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr = e.getLocalizedMessage();
            logErr("getSecScreenBrightness", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public void disablePosMenu(Map<PosMenu, Boolean> posMenu) {
        iSys.disablePosMenu(posMenu);
        logTrue("disablePosMenu");
    }

    public void setBatteryManagerEnable(boolean enable) {
        try {
            iSys.setBatteryManagerEnable(enable);
            logTrue("setBatteryManagerEnable");
        } catch (Exception e) {
            logErr("setBatteryManagerEnable", e.getLocalizedMessage());
        }
    }

    public void enableBatteryLevelShow(boolean enable) {
        try {
            iSys.enableBatteryLevelShow(enable);
            logTrue("enableBatteryLevelShow");
        } catch (Exception e) {
            logErr("enableBatteryLevelShow", e.getLocalizedMessage());
        }
    }

    public void setAppPowerSaveConfigWithType(String packageName, boolean on) {
        try {
            iSys.setAppPowerSaveConfigWithType(packageName,on);
            logTrue("setAppPowerSaveConfigWithType");
            Toast.makeText(DemoApp.appContext,"setAppPowerSaveConfigWithType succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("setAppPowerSaveConfigWithType", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"setAppPowerSaveConfigWithType error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void disableGlobalAirPlane( boolean on) {
        try {
            iSys.disableGlobalAirPlane(on);
            logTrue("disableGlobalAirPlane");
            Toast.makeText(DemoApp.appContext,"disableGlobalAirPlane succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("disableGlobalAirPlane", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"disableGlobalAirPlane error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setLongPowerKeyDisable(boolean on) {
        try {
            iSys.setLongPowerKeyDisable(on);
            logTrue("setLongPowerKeyDisable");
            Toast.makeText(DemoApp.appContext,"setLongPowerKeyDisable succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("setLongPowerKeyDisable", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"setLongPowerKeyDisable error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void disableGlobalSilent(boolean on) {
        try {
            iSys.disableGlobalSilent(on);
            logTrue("disableGlobalSilent");
            Toast.makeText(DemoApp.appContext,"disableGlobalSilent succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("disableGlobalSilent", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"disableGlobalSilent error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setSmartSavingModeWhenCharging(boolean config) {
        try {
            boolean ok = iSys.setSmartSavingModeWhenCharging(config);
            logTrue("setSmartSavingModeWhenCharging");
            Toast.makeText(DemoApp.appContext,"setSmartSavingModeWhenCharging:"+ok,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("setSmartSavingModeWhenCharging", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"setSmartSavingModeWhenCharging error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void showCarrierName(boolean enable) {
        try {
            iSys.showCarrierName(enable);
            logTrue("showCarrierName");
            Toast.makeText(DemoApp.appContext,"showCarrierName succes:"+enable,Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("showCarrierName", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"showCarrierName error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void setUsbHostDisable(boolean disable) {
        try {
            iSys.setUsbHostDisable(disable);
            logTrue("setUsbHostDisable");
            Toast.makeText(DemoApp.appContext,"setUsbHostDisable succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("setUsbHostDisable", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"setUsbHostDisable error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isUsbHostDisabled() {
        try {
            boolean isUsbHostDisabled = iSys.isUsbHostDisabled();
            logTrue("isUsbHostDisabled");
            Toast.makeText(DemoApp.appContext,"isUsbHostDisabled:"+isUsbHostDisabled,Toast.LENGTH_SHORT).show();
            return isUsbHostDisabled;
        } catch (Exception e) {
            logErr("isUsbHostDisabled", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"isUsbHostDisabled error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public void setWifiWakeupEnabled(boolean enable) {
        try {
            iSys.setWifiWakeupEnabled(enable);
            logTrue("setWifiWakeupEnabled");
            Toast.makeText(DemoApp.appContext,"setWifiWakeupEnabled succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("setWifiWakeupEnabled", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"setWifiWakeupEnabled error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public boolean getShowBatteryLevel() {
        try {
            boolean isShowBatteryLevel = iSys.getShowBatteryLevel();
            logTrue("getShowBatteryLevel");
            Toast.makeText(DemoApp.appContext,"isShowBatteryLevel:"+isShowBatteryLevel,Toast.LENGTH_SHORT).show();
            return isShowBatteryLevel;
        } catch (Exception e) {
            logErr("getShowBatteryLevel", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"getShowBatteryLevel error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public int getScreenRotation() {
        try {
            int screenRotation = iSys.getScreenRotation();
            logTrue("getScreenRotation");
            Toast.makeText(DemoApp.appContext,"getScreenRotation:"+screenRotation,Toast.LENGTH_SHORT).show();
            return screenRotation;
        } catch (Exception e) {
            logErr("getScreenRotation", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"getScreenRotation error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
        return -1;
    }

    public void setScreenRotation(int rotation) {
        try {
            iSys.setScreenRotation(rotation);
            logTrue("setScreenRotation");
            Toast.makeText(DemoApp.appContext,"setScreenRotation succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("setScreenRotation", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"setScreenRotation error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void allowAccessContactsBtPairing(boolean flag) {
        try {
            iSys.allowAccessContactsBtPairing(flag);
            logTrue("allowAccessContactsBtPairing");
            Toast.makeText(DemoApp.appContext,"allowAccessContactsBtPairing succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("allowAccessContactsBtPairing", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"allowAccessContactsBtPairing error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void updateSettingsPasswordHashValue(String password) {
        try {
            String passwordhash = getSHA256(password);
            iSys.updateSettingsPasswordHashValue(SignKey, passwordhash, new ASCaller() {
                @Override
                public byte[] getEncryptData(byte[] bytes) {
                    return getData(bytes);
                }
            });
            logTrue("updateSettingsPasswordHashValue");
            Toast.makeText(DemoApp.appContext,"updateSettingsPasswordHashValue succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            logErr("updateSettingsPasswordHashValue", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"updateSettingsPasswordHashValue error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public byte[] SignKey = readAssetFileToByteArray(DemoApp.appContext,"Test_SIG.puk");

    private static String getSHA256(String str){
        MessageDigest messageDigest;
        String encodestr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes("UTF-8"));
            encodestr = bytes2Hex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodestr;
    }

    public byte[] getBytes(String filePath) {
        byte[] buffer = null;
        File file = new File(filePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            Log.e("SysTester", e.getMessage());
        } catch (IOException e) {
            Log.e("SysTester", e.getMessage());
        }
        return buffer;
    }
    public static String bytes2Hex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(byte2Hex(b));
        }
        return hexString.toString();
    }

    public static String byte2Hex(byte b) {
        int unsigned = b < 0 ? b + 256 : b;
        return String.format("%02x", unsigned);
    }

    public byte[] getData(byte[] randoms) {
        try {
            Map<ETermInfoKey, String> dd =  NeptuneLiteUser.getInstance().getDal(DemoApp.appContext).getSys().getTermInfo();

            byte[] mergeArray = null;
            String pvkInfo = "Test";
            mergeArray = (dd.get(ETermInfoKey.SN) + "#" + new String(randoms) + "#" + pvkInfo).getBytes();
            PrivateKey loadPrivateKey = (PrivateKey) deSerializable();
            byte[] encryptData = encypt(mergeArray, loadPrivateKey);
            return encryptData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] encypt(byte[] encryptedData, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher.doFinal(encryptedData);
        } catch (Exception e) {
            Log.e("SysTester","encypt方法调用时，截获到异常：" + e.getMessage());
            return null;
        }
    }

    public static Object deSerializable() throws FileNotFoundException, IOException, ClassNotFoundException {
        AssetManager assetManager = DemoApp.appContext.getAssets();
        InputStream inputStream = assetManager.open("key.pvk");
        ObjectInputStream ois = new ObjectInputStream(inputStream);
        Object readObject = ois.readObject();
        ois.close();
        return readObject;
    }

    public byte[] readAssetFileToByteArray(Context context, String fileName) {
        AssetManager assetManager = context.getAssets(); // 假设这是在一个Activity或Context中调用
        InputStream inputStream = null;
        byte[] buffer = null;
        try {
            inputStream = assetManager.open(fileName);
            int size = inputStream.available();
            buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return buffer;
    }

    public void allowBtOpenAirplane(boolean enable) {
        try {
            iSys.allowBtOpenAirplane(enable);
            logTrue("allowBtOpenAirplane");
            Toast.makeText(DemoApp.appContext,"allowBtOpenAirplane succes",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            logErr("allowBtOpenAirplane", e.getLocalizedMessage());
            Toast.makeText(DemoApp.appContext,"allowBtOpenAirplane error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
