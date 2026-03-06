package com.payment.demo.config;

import android.content.Context;
import android.content.res.AssetManager;

import com.pax.dal.IDAL;
import com.payment.demo.app.PaymentDemoApp;

import java.io.IOException;

/**
 * T006: 配置/状态模块 — 初始化与获取 Terminal/Config Status.
 */
public class ConfigStatusProvider {
    private static ConfigStatusProvider instance;
    private final Context appContext;
    private boolean paramLoaded;

    public static synchronized ConfigStatusProvider getInstance(Context context) {
        if (instance == null) {
            instance = new ConfigStatusProvider(context.getApplicationContext());
        }
        return instance;
    }

    private ConfigStatusProvider(Context appContext) {
        this.appContext = appContext;
    }

    /** 应用启动后调用：加载 assets 参数、检测 DAL */
    public void init() {
        paramLoaded = checkAssetsParam();
    }

    private boolean probeReader(com.pax.dal.IDAL dal, String name, Runnable action) {
        try {
            action.run();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    private boolean checkAssetsParam() {
        AssetManager am = appContext.getAssets();
        try {
            String[] list = am.list("");
            if (list != null) {
                for (String name : list) {
                    if (name != null && (name.endsWith(".contact") || name.endsWith(".clss_mc") || name.endsWith(".capk"))) {
                        return true;
                    }
                }
            }
        } catch (IOException ignored) {
        }
        return false;
    }

    public TerminalConfigStatus getStatus() {
        TerminalConfigStatus s = new TerminalConfigStatus();
        IDAL dal = PaymentDemoApp.getApp().getDal();
        s.setParamLoaded(paramLoaded);
        if (dal != null) {
            s.setPiccAvailable(probeReader(dal, "PICC", () -> {
                try {
                    com.pax.dal.IPicc p = dal.getPicc(com.pax.dal.entity.EPiccType.INTERNAL);
                    p.close();
                    p.open();
                    p.close();
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }));
            s.setIccAvailable(probeReader(dal, "ICC", () -> {
                try {
                    com.pax.dal.IIcc i = dal.getIcc();
                    i.close((byte) 0x00);
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }));
            s.setMagAvailable(probeReader(dal, "MAG", () -> {
                try {
                    com.pax.dal.IMag m = dal.getMag();
                    m.close();
                    m.open();
                    m.reset();
                    m.close();
                } catch (Throwable t) {
                    throw new RuntimeException(t);
                }
            }));
            s.setCardReaderAvailable(s.isPiccAvailable() || s.isIccAvailable() || s.isMagAvailable());
        } else {
            s.setCardReaderAvailable(false);
        }
        s.setTradable(dal != null && paramLoaded && s.isCardReaderAvailable());
        try {
            s.setAppVersion(appContext.getPackageManager().getPackageInfo(appContext.getPackageName(), 0).versionName);
        } catch (Throwable t) {
            s.setAppVersion("1.0.0");
        }
        s.setKernelVersion("—");
        return s;
    }
}
