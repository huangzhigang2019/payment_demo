
package com.pax.demo.modules.customerdisplay;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.TextView;

import com.pax.dal.ICustomerDisplay;
import com.pax.dal.IWLCustomerDisplay;
import com.pax.dal.entity.CustomerDisplayInfo;
import com.pax.dal.entity.FontInfo;
import com.pax.dal.entity.WLLcdCusorPosition;
import com.pax.dal.entity.WLLcdDisplaySize;
import com.pax.dal.exceptions.CustomerDisplayDevException;
import com.pax.dal.exceptions.WLCustomerDisplayDevException;
import com.pax.demo.R;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;
import com.pax.demo.util.FloatView;

import java.io.ByteArrayOutputStream;

public class WLCustomerDisPlayTester extends BaseTester {

    private static WLCustomerDisPlayTester customerDisPlayTester;
    private IWLCustomerDisplay iCustomerDisplay = null;
    static Context mContext;
    static TextView mTextView;

    private WLCustomerDisPlayTester() {
        iCustomerDisplay = DemoApp.getDal().getWLCustomerDisplay();
    }

    public static WLCustomerDisPlayTester getInstance(Context context, TextView textView) {
        if (customerDisPlayTester == null) {
            customerDisPlayTester = new WLCustomerDisPlayTester();
            mContext = context;
            mTextView = textView;
        }
        return customerDisPlayTester;
    }

    @Override
    public void logTrue(String method) {
        super.logTrue(method);
        mTextView.setText(method + " Successful");
    }

    @Override
    public void logErr(String method, String errString) {
        super.logErr(method,errString);
        mTextView.setText(method + errString);
    }

    public void scrInit() {
        try {
            iCustomerDisplay.scrInit();
            logTrue("scrInit");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrInit fail:" , e.toString());
        }
    }

    public void scrClrLine() {
        try {
            iCustomerDisplay.scrClrLine((byte) 1, (byte) 1);
            logTrue("scrClrLine");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrClrLine fail:" , e.toString());
        }
    }

    public void scrGotoxyEx() {
        try {
            iCustomerDisplay.scrGotoxyEx(1, 1);
            logTrue("scrGotoxyEx");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrGotoxyEx fail:", e.toString());
        }
    }

    public void scrGotoxy() {
        try {
            iCustomerDisplay.scrGotoxy((byte) 1, (byte) 1);
            logTrue("scrGotoxy");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrGotoxy fail:" , e.toString());
        }
    }

    public void scrLcdDisplay() {
        try {
            iCustomerDisplay.scrLcdDisplay((byte)32,(byte)0,(byte)0x01,"WorldLine");
            logTrue("scrLcdDisplay");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrLcdDisplay fail:" , e.toString());
        }
    }

    public void scrCls() {
        try {
            iCustomerDisplay.scrCls();
            logTrue("scrCls");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrCls fail:" , e.toString());
        }
    }

    public void scrGray() {
        try {
            iCustomerDisplay.scrGray(7);
            logTrue("scrGray");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrGray fail:" , e.toString());
        }
    }

    public void scrBackLight() {
        try {
            iCustomerDisplay.scrBackLight((byte) 1);
            logTrue("scrBackLight");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrBackLight fail:" , e.toString());
        }
    }

    public void scrAttrSet() {
        try {
            iCustomerDisplay.scrAttrSet((byte) 0);
            logTrue("scrAttrSet");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrAttrSet fail:" , e.toString());
        }
    }

    public void scrRestore() {
        try {
            iCustomerDisplay.scrRestore((byte) 0);
            logTrue("scrRestore");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrRestore fail:" , e.toString());
        }
    }

    public void scrGetLcdSize() {
        try {
            WLLcdDisplaySize mWLLcdDisplaySize = iCustomerDisplay.scrGetLcdSize();
            logTrue("scrGetLcdSize.getWidth:"+mWLLcdDisplaySize.getWidth());
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrGetLcdSize fail:" , e.toString());
        }
    }

    public void scrGetxyEx() {
        try {
            WLLcdCusorPosition mWLLcdCusorPosition = iCustomerDisplay.scrGetxyEx();
            logTrue("scrGetxyEx.getPixelX:"+mWLLcdCusorPosition.getPixelX());
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrGetxyEx fail:" , e.toString());
        }
    }

    public void scrPlot() {
        try {
            iCustomerDisplay.scrPlot((byte) 50,(byte) 50,(byte) 1);
            iCustomerDisplay.scrPlot((byte) 51,(byte) 51,(byte) 1);
            iCustomerDisplay.scrPlot((byte) 52,(byte) 52,(byte) 1);
            iCustomerDisplay.scrPlot((byte) 53,(byte) 53,(byte) 1);
            logTrue("scrPlot");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrPlot fail:" , e.toString());
        }
    }

    public void scrDrLogo() {
        try {

            iCustomerDisplay.scrCls();
            iCustomerDisplay.scrGray(7);
            iCustomerDisplay.scrDrLogo(paxLogo);
            logTrue("scrDrLogo");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrDrLogo fail:" , e.toString());
        }
    }

    public void scrDrLogoEx() {
        try {
            iCustomerDisplay.scrDrLogoEx(30,0,paxLogo);
            logTrue("scrDrLogoEx");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrDrLogoEx fail:" , e.toString());
        }
    }

    public void scrDrawRect() {
        try {
            iCustomerDisplay.scrDrawRect(0,0,100,100);
            logTrue("scrDrawRect");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrDrawRect fail:" , e.toString());
        }
    }

    public void scrClrRect() {
        try {
            iCustomerDisplay.scrClrRect(0,0,100,100);
            logTrue("scrClrRect");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrClrRect fail:" , e.toString());
        }
    }

   /* public void scrSelectFont() {
        try {
            FontInfo fontInfo = new FontInfo();
            fontInfo.setCharSet(1);
            fontInfo.setWidth(6);
            fontInfo.setHeight(8);
            fontInfo.setItalic(false);
            fontInfo.setbBold(false);

            FontInfo fontInfo2 = new FontInfo();
            fontInfo.setCharSet(10);
            fontInfo.setWidth(16);
            fontInfo.setHeight(16);
            fontInfo.setItalic(false);
            fontInfo.setbBold(false);

            iCustomerDisplay.scrSelectFont(fontInfo,fontInfo2);
            logTrue("scrSelectFont");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrSelectFont fail:" , e.toString());
        }
    }*/

    public void scrSpaceSet() {
        try {
            iCustomerDisplay.scrSpaceSet(3,3);
            logTrue("scrSpaceSet");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrSpaceSet fail:" , e.toString());
        }
    }

    public void scrUninit() {
        try {
            iCustomerDisplay.scrUninit();
            logTrue("scrUninit");
        } catch (WLCustomerDisplayDevException e) {
            e.printStackTrace();
            logErr("scrUninit fail:" , e.toString());
        }
    }

    byte[] paxLogo={
            (byte)0x04,
            (byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,
            (byte)0x80,(byte)0xc0,(byte)0xc0,(byte)0xe0,(byte)0xe0,(byte)0xe0,(byte)0xf0,(byte)0xf0,(byte)0xf0,(byte)0xf8,(byte)0xf8,(byte)0xe8,(byte)0x40,(byte)0x40,(byte)0x00,(byte)0x20,
            (byte)0x20,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0xc0,(byte)0xc0,(byte)0xc0,(byte)0xc0,(byte)0xe0,(byte)0xe0,(byte)0xe0,(byte)0xe0,(byte)0xf0,(byte)0xf0,
            (byte)0xf0,(byte)0xf0,(byte)0xf0,(byte)0x70,(byte)0x70,(byte)0x70,(byte)0x70,(byte)0x38,(byte)0x38,(byte)0x38,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,(byte)0x30,
            (byte)0x20,(byte)0x60,(byte)0xe0,(byte)0xc0,(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x12,(byte)0x02,(byte)0x21,(byte)0x1e,(byte)0x00,(byte)0x00,(byte)0x2a,(byte)0x34,(byte)0x20,
            (byte)0x12,(byte)0x0c,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,

            (byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0xc0,(byte)0xe0,(byte)0xf0,(byte)0xf8,(byte)0xf8,(byte)0xfc,(byte)0xfe,(byte)0x7e,(byte)0x7f,(byte)0x3f,(byte)0x1f,
            (byte)0x1f,(byte)0x0f,(byte)0x0f,(byte)0x07,(byte)0x87,(byte)0xc3,(byte)0xc3,(byte)0xe1,(byte)0xe1,(byte)0xf0,(byte)0xf8,(byte)0xf8,(byte)0xfc,(byte)0xfc,(byte)0xfe,(byte)0x7e,
            (byte)0x7e,(byte)0x3f,(byte)0x3f,(byte)0x1f,(byte)0x1f,(byte)0x0f,(byte)0x0f,(byte)0x07,(byte)0x07,(byte)0x07,(byte)0x83,(byte)0xc3,(byte)0xc3,(byte)0xa1,(byte)0x01,(byte)0x01,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0xc0,(byte)0xe0,(byte)0xf0,
            (byte)0xf8,(byte)0xfc,(byte)0xff,(byte)0x7f,(byte)0x3f,(byte)0x06,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,

            (byte)0x00,(byte)0x80,
            (byte)0x60,(byte)0xfc,(byte)0xfe,(byte)0x7f,(byte)0x3f,(byte)0x0f,(byte)0x0f,(byte)0x07,(byte)0x03,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,
            (byte)0x0c,(byte)0x1e,(byte)0x1e,(byte)0x3f,(byte)0x3f,(byte)0x1f,(byte)0x0f,(byte)0x8f,(byte)0x87,(byte)0x87,(byte)0xc3,(byte)0xc1,(byte)0xc1,(byte)0xe0,(byte)0xe0,(byte)0xf0,
            (byte)0xf0,(byte)0xf0,(byte)0xf8,(byte)0xf8,(byte)0xfc,(byte)0x7c,(byte)0x7e,(byte)0x3e,(byte)0x3f,(byte)0x3f,(byte)0x1f,(byte)0x0f,(byte)0x0f,(byte)0x87,(byte)0x87,(byte)0xc3,
            (byte)0xc1,(byte)0xe0,(byte)0xe0,(byte)0xf0,(byte)0xf0,(byte)0xf8,(byte)0xf8,(byte)0xfc,(byte)0xfe,(byte)0x7e,(byte)0x7f,(byte)0x3f,(byte)0x1f,(byte)0x1f,(byte)0x0f,(byte)0x07,
            (byte)0x03,(byte)0x83,(byte)0x81,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,
            (byte)0x88,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x00,
            (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x80,(byte)0x00,

            (byte)0x00,(byte)0x80,
            (byte)0x00,(byte)0x01,(byte)0x03,(byte)0x06,(byte)0x04,(byte)0x04,(byte)0x0c,(byte)0x08,(byte)0x08,(byte)0x08,(byte)0x0c,(byte)0x0c,(byte)0x0c,(byte)0x0c,(byte)0x0c,(byte)0x0c,
            (byte)0x0e,(byte)0x0e,(byte)0x0e,(byte)0x0e,(byte)0x0f,(byte)0x0f,(byte)0x0f,(byte)0x07,(byte)0x07,(byte)0x07,(byte)0x07,(byte)0x07,(byte)0x03,(byte)0x03,(byte)0x03,(byte)0x01,
            (byte)0x01,(byte)0x01,(byte)0x08,(byte)0x08,(byte)0x08,(byte)0x0c,(byte)0x1c,(byte)0x3c,(byte)0x3e,(byte)0x1e,(byte)0x1f,(byte)0x1f,(byte)0x1f,(byte)0x0f,(byte)0x0f,(byte)0x0f,
            (byte)0x07,(byte)0x07,(byte)0x07,(byte)0x03,(byte)0x03,(byte)0x01,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x40,(byte)0x60,(byte)0x78,(byte)0x7c,
            (byte)0x7f,(byte)0x7f,(byte)0x3f,(byte)0x1f,(byte)0x13,(byte)0x11,(byte)0x10,(byte)0x10,(byte)0x10,(byte)0x10,(byte)0x10,(byte)0x10,(byte)0x10,(byte)0x09,(byte)0x49,(byte)0x4f,
            (byte)0x6f,(byte)0x6f,(byte)0x77,(byte)0x37,(byte)0x38,(byte)0x38,(byte)0x3c,(byte)0x3c,(byte)0x2e,(byte)0x26,(byte)0x27,(byte)0x23,(byte)0x23,(byte)0x21,(byte)0x7f,(byte)0x7f,
            (byte)0x7f,(byte)0x7f,(byte)0x7f,(byte)0x7f,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x40,(byte)0x40,(byte)0x61,(byte)0x61,(byte)0x63,(byte)0x77,(byte)0x7f,(byte)0x3f,
            (byte)0x3f,(byte)0x1e,(byte)0x1c,(byte)0x3e,(byte)0x7e,(byte)0x7f,(byte)0x77,(byte)0x67,(byte)0x63,(byte)0x43,(byte)0x41,(byte)0x01,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00
    };
}
