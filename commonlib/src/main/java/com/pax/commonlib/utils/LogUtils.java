/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2019-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                  Author	                 Action
 * 20190108  	         guanjw                  Create
 * ===========================================================================================
 */

package com.pax.commonlib.utils;

import android.util.Log;

import com.pax.commonlib.BuildConfig;

public class LogUtils {
    private static final boolean IS_DEBUG = BuildConfig.DEBUG;

    private LogUtils() {

    }

    public static void e(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.e(tag, getStackInfo(msg.toString()));
        }
    }

    public static void w(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.w(tag, getStackInfo(msg.toString()));
        }
    }

    public static void i(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.i(tag, getStackInfo(msg.toString()));
        }
    }

    public static void d(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.d(tag, getStackInfo(msg.toString()));
        }
    }

    public static void v(String tag, Object msg) {
        if (IS_DEBUG) {
            Log.v(tag, getStackInfo(msg.toString()));
        }
    }

    public static void e(String tag, String msg, Throwable th) {
        Log.e(tag, getStackInfo(msg), th);
    }

    public static void e(String tag, Exception exception) {
        Log.e(tag, getStackInfo(""), exception);
    }

    public static void w(String tag, String msg, Throwable th) {
        if (IS_DEBUG) {
            Log.w(tag, getStackInfo(msg), th);
        }
    }

    public static void i(String tag, String msg, Throwable th) {
        if (IS_DEBUG) {
            Log.i(tag, getStackInfo(msg), th);
        }
    }

    public static void d(String tag, String msg, Throwable th) {
        if (IS_DEBUG) {
            Log.d(tag, getStackInfo(msg), th);
        }
    }

    public static void v(String tag, String msg, Throwable th) {
        if (IS_DEBUG) {
            Log.v(tag, getStackInfo(msg), th);
        }
    }

    private static String getStackInfo(String tag) {
        StackTraceElement[] stacks = Thread.currentThread().getStackTrace();
        if (stacks != null && stacks.length >= 5) {
            StackTraceElement current = stacks[4];
            String file = current.getFileName();
            String method = current.getMethodName();
            int line = current.getLineNumber();
            return "[LogUtils]" + method + ":(" + file + ":" + line + "): " + tag + "";
        }
        return "LogUtils:[" + tag + "]";
    }

}

