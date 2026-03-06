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

package com.pax.demo.modules.printer;

import android.graphics.Bitmap;

import com.pax.dal.IPrinter;
import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.dal.exceptions.PrinterDevException;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class PrinterTester extends BaseTester {

    private static PrinterTester printerTester;
    private IPrinter printer;

    private PrinterTester() {
        printer = DemoApp.getDal().getPrinter();
    }

    public static PrinterTester getInstance() {
        if (printerTester == null) {
            printerTester = new PrinterTester();
        }
        return printerTester;
    }

    public void init() {
        try {
            printer.init();
            logTrue("init");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("init", e.toString());
        }
    }

    public String getStatus() {
        try {
            int status = printer.getStatus();
            logTrue("getStatus");
            return statusCode2Str(status);
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getStatus", e.toString());
            return "";
        }

    }

    public void fontSet(EFontTypeAscii asciiFontType, EFontTypeExtCode cFontType) {
        try {
            printer.fontSet(asciiFontType, cFontType);
            logTrue("fontSet");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("fontSet", e.toString());
        }

    }

    public void spaceSet(byte wordSpace, byte lineSpace) {
        try {
            printer.spaceSet(wordSpace, lineSpace);
            logTrue("spaceSet");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("spaceSet", e.toString());
        }
    }

    public void printStr(String str, String charset) {
        try {
            printer.printStr(str, charset);
            logTrue("printStr");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("printStr", e.toString());
        }

    }

    public void step(int b) {
        try {
            printer.step(b);
            logTrue("setStep");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setStep", e.toString());
        }
    }

    public void printBitmap(Bitmap bitmap) {
        try {
            printer.printBitmap(bitmap);
            logTrue("printBitmap");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("printBitmap", e.toString());
        }
    }

    public String start() {
        try {
            int res = printer.start();
            logTrue("start");
            return statusCode2Str(res);
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("start", e.toString());
            return "";
        }

    }

    public void leftIndents(short indent) {
        try {
            printer.leftIndent(indent);
            logTrue("leftIndent");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("leftIndent", e.toString());
        }
    }

    public int getDotLine() {
        try {
            int dotLine = printer.getDotLine();
            logTrue("getDotLine");
            return dotLine;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getDotLine", e.toString());
            return -2;
        }
    }

    public void setGray(int level) {
        try {
            printer.setGray(level);
            logTrue("setGray");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setGray", e.toString());
        }

    }

    public void setDoubleWidth(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleWidth(isAscDouble, isLocalDouble);
            logTrue("doubleWidth");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("doubleWidth", e.toString());
        }
    }

    public void setDoubleHeight(boolean isAscDouble, boolean isLocalDouble) {
        try {
            printer.doubleHeight(isAscDouble, isLocalDouble);
            logTrue("doubleHeight");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("doubleHeight", e.toString());
        }

    }

    public void setInvert(boolean isInvert) {
        try {
            printer.invert(isInvert);
            logTrue("setInvert");
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setInvert", e.toString());
        }

    }

    public String cutPaper(int mode) {
        try {
            printer.cutPaper(mode);
            logTrue("cutPaper");
            return "cut paper successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("cutPaper", e.toString());
            return e.toString();
        }
    }

    public String getCutMode() {
        String resultStr = "";
        try {
            int mode = printer.getCutMode();
            logTrue("getCutMode");
            switch (mode) {
                case 0:
                    resultStr = "Only support full paper cut";
                    break;
                case 1:
                    resultStr = "Only support partial paper cutting ";
                    break;
                case 2:
                    resultStr = "support partial paper and full paper cutting ";
                    break;
                case -1:
                    resultStr = "No cutting knife,not support";
                    break;
                default:
                    break;
            }
            return resultStr;
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getCutMode", e.toString());
            return e.toString();
        }
    }

    public String preSetCutMode(int mode) {
        try {
            printer.presetCutPaper(mode);
            logTrue("preSetCutMode");
            return "set cut paper mode successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("getCutMode", e.toString());
            return e.getLocalizedMessage();
        }
    }

    public String statusCode2Str(int status) {
        String res = "";
        switch (status) {
            case 0:
                res = "Success ";
                break;
            case 1:
                res = "Printer is busy ";
                break;
            case 2:
                res = "Out of paper ";
                break;
            case 3:
                res = "The format of print data packet error ";
                break;
            case 4:
                res = "Printer malfunctions ";
                break;
            case 8:
                res = "Printer over heats ";
                break;
            case 9:
                res = "Printer voltage is too low";
                break;
            case -16:
                res = "Printing is unfinished ";
                break;
            case -4:
                res = " The printer has not installed font library ";
                break;
            case -2:
                res = "Data package is too long ";
                break;
            default:
                break;
        }
        return res;
    }

    public String printColorBitmapWithMonoThreshold(Bitmap bitmap, int grayThreshold) {
        try {
            printer.printColorBitmapWithMonoThreshold(bitmap,grayThreshold);
            logTrue("printColorBitmapWithMonoThreshold");
            return "printColorBitmapWithMonoThreshold successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("printColorBitmapWithMonoThreshold", e.toString());
            return e.getLocalizedMessage();
        }
    }

    public String printColorBitmap(Bitmap bitmap) {
        try {
            printer.printColorBitmap(bitmap);
            logTrue("printColorBitmap");
            return "printColorBitmap successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("printColorBitmap", e.toString());
            return e.getLocalizedMessage();
        }
    }

    public String setColorGray(int blackLevel,int colorLevel) {
        try {
            printer.setColorGray(blackLevel,colorLevel);
            logTrue("setColorGray");
            return "setColorGray successful";
        } catch (PrinterDevException e) {
            e.printStackTrace();
            logErr("setColorGray", e.toString());
            return e.getLocalizedMessage();
        }
    }
}
