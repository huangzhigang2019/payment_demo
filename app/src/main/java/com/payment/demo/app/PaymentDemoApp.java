package com.payment.demo.app;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.pax.dal.IDAL;
import com.payment.demo.config.ConfigStatusProvider;
import com.paxsz.module.emv.process.EmvBase;
import com.paxsz.module.pos.Sdk;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Application: init DAL (NeptuneLiteAPI), provide background/UI thread runners.
 * T005: Application 子类，getDal()，捕获初始化异常以免崩溃。
 */
public class PaymentDemoApp extends Application {
    private static final String TAG = "PaymentDemoApp";

    private static PaymentDemoApp instance;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private final ExecutorService backgroundExecutor = Executors.newSingleThreadExecutor();

    private IDAL dal;
    private boolean dalInitDone;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initDal();
        initEmvLibs();
        ConfigStatusProvider.getInstance(this).init();
    }

    /** 加载 EMV/Clss native 库，必须在首次使用 ClssProcess 前完成 */
    private void initEmvLibs() {
        backgroundExecutor.execute(() -> {
            try {
                EmvBase.loadLibrary();
                Log.d(TAG, "EMV libs loaded");
            } catch (Throwable t) {
                Log.e(TAG, "EMV loadLibrary failed", t);
            }
        });
    }

    private void initDal() {
        backgroundExecutor.execute(() -> {
            try {
                IDAL d = Sdk.getInstance(PaymentDemoApp.this).getDal();
                mainHandler.post(() -> {
                    dal = d;
                    dalInitDone = true;
                    Log.d(TAG, "DAL init OK");
                });
            } catch (Throwable t) {
                Log.e(TAG, "DAL init failed", t);
                mainHandler.post(() -> {
                    dal = null;
                    dalInitDone = true;
                });
            }
        });
    }

    public static PaymentDemoApp getApp() {
        return instance;
    }

    /** @return IDAL or null if not available (e.g. not on PAX device) */
    public IDAL getDal() {
        if (!dalInitDone) {
            return null;
        }
        if (dal == null) {
            try {
                dal = Sdk.getInstance(this).getDal();
            } catch (Throwable t) {
                Log.e(TAG, "getDal", t);
            }
        }
        return dal;
    }

    public void runInBackground(Runnable r) {
        backgroundExecutor.execute(r);
    }

    public void runOnUiThread(Runnable r) {
        mainHandler.post(r);
    }
}
