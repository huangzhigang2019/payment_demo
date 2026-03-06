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

package com.pax.demo.modules.customerdisplay;

import android.graphics.Bitmap;
import android.widget.Toast;

import com.pax.dal.ICustomerDisplay;
import com.pax.dal.ISys;
import com.pax.dal.entity.CustomerDisplayInfo;
import com.pax.dal.entity.ETermInfoKey;
import com.pax.dal.exceptions.CustomerDisplayDevException;
import com.pax.demo.base.DemoApp;
import com.pax.demo.modules.system.SysTester;
import com.pax.demo.util.BaseTester;

import java.util.Map;

/**
 * @author Charles
 * @date 6/28/2022
 */
public class CustomerDisPlayTester extends BaseTester {

    private static CustomerDisPlayTester customerDisPlayTester;
    private ICustomerDisplay iCustomerDisplay = null;

    private CustomerDisPlayTester() {
        iCustomerDisplay = DemoApp.getDal().getCustomerDisplay();
    }

    public static CustomerDisPlayTester getInstance() {
        if (customerDisPlayTester == null) {
            customerDisPlayTester = new CustomerDisPlayTester();
        }
        return customerDisPlayTester;
    }


    public String getProperty() {
        try {
            CustomerDisplayInfo customerDisplayInfo = iCustomerDisplay.getProperty();
            logTrue("getProperty");
            return "Width:" + customerDisplayInfo.getWidth() + "\n" +
                    "Height:" + customerDisplayInfo.getHeight() + "\n" +
                    "Bit:" + customerDisplayInfo.getBit() + "\n";
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("getProperty", e.toString());
            return e.getLocalizedMessage();
        }
    }

    public void setBrightness(int level) {
        try {
            iCustomerDisplay.setBrightness(level);
            logTrue("setBrightness");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("setBrightness", e.toString());
        }
    }

    public void setBackgroundImage(int x, int y, Bitmap background) {
        try {
            iCustomerDisplay.setBackgroundImage(x, y, background);
            logTrue("setBackgroundImage");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("setBackgroundImage", e.toString());
        }

    }

    public void setBackgroundSolid(int color) {
        try {
            iCustomerDisplay.setBackgroundSolid(color);
            logTrue("setBackgroundSolid");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("setBackgroundSolid", e.toString());
        }
    }

    public void setText(int x, int y, String text, String charset) {
        try {
            iCustomerDisplay.setText(x, y, text, charset);
            logTrue("setText");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("setText", e.toString());
        }
    }


    public void clear(int x, int y, int width, int height) {
        try {
            iCustomerDisplay.clear(x, y, width, height);
            logTrue("clear");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("clear", e.toString());
        }
    }

    public void setFont(int style, int height) {
        try {
            iCustomerDisplay.setFont(style, height);
            logTrue("setFont");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("setFont", e.toString());
        }
    }

    public void startSignBoard(int timeout) {
        try {
            iCustomerDisplay.startSignBoard(timeout);
            logTrue("startSignBoard");
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("startSignBoard", e.toString());
        }
    }


    public Bitmap getSignBoardImage() {
        try {
            Bitmap bitmap = iCustomerDisplay.getSignBoardImage();
            logTrue("getSignBoardImage");
            return bitmap;
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("getSignBoardImage", e.toString());
            return null;
        }
    }

    public void setDefaultBackgroundImage(Bitmap bitmap) {
        try {
            iCustomerDisplay.setDefaultBackgroundImage(1,bitmap);
            logTrue("setDefaultBackgroundImage");
            Toast.makeText(DemoApp.appContext,"setDefaultBackgroundImage succes",Toast.LENGTH_SHORT).show();
        } catch (CustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("setDefaultBackgroundImage", e.toString());
            Toast.makeText(DemoApp.appContext,"setDefaultBackgroundImage error"+e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}
