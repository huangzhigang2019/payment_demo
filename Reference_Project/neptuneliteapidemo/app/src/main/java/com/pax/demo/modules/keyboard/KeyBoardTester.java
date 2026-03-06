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

package com.pax.demo.modules.keyboard;

import com.pax.dal.IKeyBoard;
import com.pax.dal.entity.EKeyBoardType;
import com.pax.dal.entity.EKeyCode;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class KeyBoardTester extends BaseTester {

    private IKeyBoard keyBoard;
    private static KeyBoardTester cameraTester;

    private KeyBoardTester() {
        keyBoard = DemoApp.getDal().getKeyBoard();
    }

    public static KeyBoardTester getInstance() {
        if (cameraTester == null) {
            cameraTester = new KeyBoardTester();
        }
        return cameraTester;
    }

    public boolean isHit() {
        boolean hit = keyBoard.isHit();
        logTrue("isHit");
        return hit;
    }

    public void clear() {
        keyBoard.clear();
        logTrue("clear");
    }

    public EKeyCode getKey() {
        EKeyCode keyCode = keyBoard.getKey();
        logTrue("getKey");
        return keyCode;
    }

    public void setMute(boolean isMute) {
        keyBoard.setMute(isMute);
        logTrue("setMute");
    }

    public String setLight(byte mode, byte volume) {
        String resultStr = "setLight:\n";
        try {
            keyBoard.setLight(mode, volume);
            resultStr += "success";
            logTrue("setLight");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr += e.getLocalizedMessage();
            logErr("setLight,", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public String setVolume(int volumes) {
        String resultStr = "setVolume:\n";
        try {
            keyBoard.setVolume(volumes);
            resultStr += "success";
            logTrue("setVolume");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr += e.getLocalizedMessage();
            logErr("setVolume,", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public String setVolume(EKeyBoardType type, int volume) {
        String resultStr = "setVolume:\n";
        try {
            keyBoard.setVolume(type,volume);
            resultStr += "success";
            logTrue("setVolume");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr += e.getLocalizedMessage();
            logErr("setVolume,", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public String setMute(EKeyBoardType type,boolean mute) {
        String resultStr = "setMute:\n";
        try {
            keyBoard.setMute(type,mute);
            resultStr += "success";
            logTrue("setMute");
        } catch (Exception e) {
            e.printStackTrace();
            resultStr += e.getLocalizedMessage();
            logErr("setMute,", e.getLocalizedMessage());
        }
        return resultStr;
    }

    public int getVolume() {
        int volume = -1;
        try {
            volume = keyBoard.getVolume();
            logTrue("getVolume");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("getVolume,", e.getLocalizedMessage());
        }
        return volume;
    }

    public boolean getMute() {
        boolean mute = false;
        try {
            mute = keyBoard.getMute();
            logTrue("getMute");
        } catch (Exception e) {
            e.printStackTrace();
            logErr("getMute,", e.getLocalizedMessage());
        }
        return mute;
    }
}
