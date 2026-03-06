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

package com.pax.demo.modules.cashdrawer;

import com.pax.dal.ICashDrawer;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class CashDrawerTest extends BaseTester {

    private ICashDrawer cashDrawer;

    private CashDrawerTest() {
        cashDrawer = DemoApp.getDal().getCashDrawer();
    }

    private static class Holder {
        private static final CashDrawerTest instance = new CashDrawerTest();
    }

    public static CashDrawerTest getInstance() {
        return Holder.instance;
    }

    public int open() {
        int ret = cashDrawer.open();
        if (ret == 0) {
            logTrue("open");
        } else {
            logErr("open", "open failed . errCode:" + ret);
        }
        return ret;
    }

}
