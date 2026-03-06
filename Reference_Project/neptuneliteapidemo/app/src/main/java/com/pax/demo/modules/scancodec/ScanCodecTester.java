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

package com.pax.demo.modules.scancodec;

import android.content.Context;

import com.pax.dal.IScanCodec;
import com.pax.dal.entity.DecodeResult;
import com.pax.dal.entity.DecodeResultRaw;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class ScanCodecTester extends BaseTester {

    private static ScanCodecTester codecTester;
    private IScanCodec scanCodec;

    private ScanCodecTester() {

    }

    public static synchronized ScanCodecTester getInstance() {
        if (codecTester == null) {
            codecTester = new ScanCodecTester();
        }
        codecTester.scanCodec = DemoApp.getDal().getScanCodec();
        return codecTester;
    }

    public void disableFormat(int format) {
        scanCodec.disableFormat(format);
        logTrue("disableFormat");
    }

    public void enableFormat(int format) {
        scanCodec.enableFormat(format);
        logTrue("enableFormat");
    }

    public void init(Context context, int width, int height) {
        scanCodec.init(context, width, height);
        logTrue("init");
    }

    public DecodeResult decode(byte[] data) {
        DecodeResult result = scanCodec.decode(data);
        logTrue("decode");
        return result;
    }

    public DecodeResultRaw decodeRaw(byte[] data) {
        return scanCodec.decodeRaw(data);
    }

    public void release() {
        scanCodec.release();
        logTrue("release");
    }
}
