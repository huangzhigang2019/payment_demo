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

package com.pax.demo.modules.idreader;

import android.os.Handler;

import com.pax.dal.IIDReader;
import com.pax.dal.IIDReader.IDReadListener;
import com.pax.dal.entity.IDReadResult;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class IDReaderTest extends BaseTester {

    private static IDReaderTest idReaderTest;
    private IIDReader reader;

    private IDReaderTest() {
        reader = DemoApp.getDal().getIDReader();
    }

    public synchronized static IDReaderTest getInstance() {
        if (idReaderTest == null) {
            idReaderTest = new IDReaderTest();
        }
        return idReaderTest;
    }

    public void startRead(final Handler handler) {
        if (null != reader){
            reader.start(new IDReadListener() {

                @Override
                public void onRead(IDReadResult result) {
                    handler.obtainMessage(0, result).sendToTarget();
                    logTrue("onRead");
                }

                @Override
                public void onComplete() {
                    //handler.obtainMessage(0, "exit").sendToTarget();
                    logTrue("onComplete");
                }
            });
        }else{
            logTrue("reader is null");
        }
    }
}
