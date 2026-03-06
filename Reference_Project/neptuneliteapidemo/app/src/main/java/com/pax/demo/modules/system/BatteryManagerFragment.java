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

package com.pax.demo.modules.system;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.base.DemoApp;

public class BatteryManagerFragment extends BaseFragment implements View.OnClickListener {

    private Button enable, disable;

    private Button show, not_show;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_battery, container, false);
        enable = (Button) view.findViewById(R.id.fragment_sys_screen_enable);
        disable = (Button) view.findViewById(R.id.fragment_sys_screen_disable);
        show = (Button) view.findViewById(R.id.fragment_sys_screen_show);
        not_show = (Button) view.findViewById(R.id.fragment_sys_screen_not_show);

        enable.setOnClickListener(this);
        disable.setOnClickListener(this);

        show.setOnClickListener(this);
        not_show.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_sys_screen_enable:
                SysTester.getInstance().setBatteryManagerEnable(true);
                break;
            case R.id.fragment_sys_screen_disable:
                SysTester.getInstance().setBatteryManagerEnable(false);
                break;
            case R.id.fragment_sys_screen_show:
                SysTester.getInstance().enableBatteryLevelShow(true);
                break;
            case R.id.fragment_sys_screen_not_show:
                SysTester.getInstance().enableBatteryLevelShow(false);
                break;
            default:
                break;
        }
    }
}
