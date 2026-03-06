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

package com.pax.demo.modules.icc;

import com.pax.dal.IIcc;
import com.pax.dal.exceptions.IccDevException;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class IccTester extends BaseTester {

    private static IccTester iccTester;

    private IIcc icc;

    private IccTester() {
        icc = DemoApp.getDal().getIcc();
    }

    public static IccTester getInstance() {
        if (iccTester == null) {
            iccTester = new IccTester();
        }
        return iccTester;
    }

    public byte[] init(byte slot) {
        byte[] initRes = null;
        try {
            initRes = icc.init(slot);
            logTrue("init");
            return initRes;
        } catch (IccDevException e) {
            e.printStackTrace();
            logErr("init", e.toString());
            return null;
        }
    }

    public boolean detect(byte slot) {
        boolean res = false;
        try {
            res = icc.detect(slot);
            logTrue("detect");
            return res;
        } catch (IccDevException e) {
            e.printStackTrace();
            logErr("detect", e.toString());
            return res;
        }
    }

    public void close(byte slot) {
        try {
            icc.close(slot);
            logTrue("close");
        } catch (IccDevException e) {
            e.printStackTrace();
            logErr("close", e.toString());
        }
    }

    public void autoResp(byte slot, boolean autoresp) {
        try {
            icc.autoResp(slot, autoresp);
            logTrue("autoResp");
        } catch (IccDevException e) {
            e.printStackTrace();
            logErr("autoResp", e.toString());
        }
    }

    public byte[] isoCommand(byte slot, byte[] send) {
        try {
            byte[] resp = icc.isoCommand(slot, send);
            logTrue("isoCommand");
            return resp;
        } catch (IccDevException e) {
            e.printStackTrace();
            logErr("isoCommand", e.toString());
            return null;
        }
    }
    
    public void light(boolean flag){
        try {
            icc.light(flag);
            logTrue("light");
        } catch (IccDevException e) {
            e.printStackTrace();
            logErr("light", e.toString());
        }
    }
}
