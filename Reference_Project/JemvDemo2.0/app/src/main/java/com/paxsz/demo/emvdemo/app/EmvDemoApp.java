/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) $YEAR-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2020/5/15  	         Qinny Zhou           	Create/Add/Modify/Delete
 * ===========================================================================================
 */
package com.paxsz.demo.emvdemo.app;

import android.app.Application;
import android.os.Handler;

import com.pax.commonlib.utils.LogUtils;
import com.pax.dal.IDAL;
import com.paxsz.demo.emvdemo.manager.ParamManager;
import com.paxsz.demo.emvdemo.util.ThreadPoolManager;
import com.paxsz.module.emv.process.EmvBase;
import com.paxsz.module.pos.Sdk;
import com.squareup.leakcanary.LeakCanary;

import java.util.concurrent.ExecutorService;

public class EmvDemoApp extends Application {
    private static final String TAG = "EmvDemoApp";
    private static EmvDemoApp instance;
    private static ParamManager mParamManager;
    private Handler handler;
    private ExecutorService backgroundExecutor;
    private IDAL dal;


    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        instance = this;
        handler = new Handler();
        registerActivityLifecycleCallbacks(new AppActLifecycleCallback());
        backgroundExecutor = ThreadPoolManager.getInstance().getExecutor();
        initSdkModule();
        initEmvModule();

    }

    private void initSdkModule() {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                LogUtils.d(TAG, " initSdkModule start");
                long startT = System.currentTimeMillis();
                getDalInstance();
                long endT = System.currentTimeMillis();
                LogUtils.d(TAG, "initSdkModule  end:" + (endT - startT));
            }
        });

    }

    private void getDalInstance() {
        dal = Sdk.getInstance(instance).getDal();
    }

    private void initEmvModule() {
        runInBackground(new Runnable() {
            @Override
            public void run() {
                LogUtils.d(TAG, " initEmvModule start");
                long startT = System.currentTimeMillis();
                mParamManager = ParamManager.getInstance(instance);
                long endT = System.currentTimeMillis();
                LogUtils.d(TAG, "initEmvModule  end:" + (endT - startT));
                EmvBase.loadLibrary();
            }
        });

    }


    public static ParamManager getParamManager() {
        return mParamManager;
    }

    public static EmvDemoApp getApp(){
        return instance;
    }
    public void runInBackground(final Runnable runnable) {
        backgroundExecutor.execute(runnable);
    }

    public void runOnUiThread(final Runnable runnable) {
        handler.post(runnable);
    }

    public IDAL getDal() {
        if (dal == null) {
            getDalInstance();
        }
        return dal;
    }

}
