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

public class AllowBtOpenAirplaneFragment extends BaseFragment implements View.OnClickListener {

    private Button enable, disable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_allow_bt_open_airplan, container, false);
        enable = (Button) view.findViewById(R.id.fragment_sys_allow_bt_open_airplan_enable);
        disable = (Button) view.findViewById(R.id.fragment_sys_allow_bt_open_airplan_disable);

        enable.setOnClickListener(this);
        disable.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_sys_allow_bt_open_airplan_enable:
                SysTester.getInstance().allowBtOpenAirplane(true);
                break;

            case R.id.fragment_sys_allow_bt_open_airplan_disable:
                SysTester.getInstance().allowBtOpenAirplane(false);
                break;
            default:
                break;
        }
    }
}
