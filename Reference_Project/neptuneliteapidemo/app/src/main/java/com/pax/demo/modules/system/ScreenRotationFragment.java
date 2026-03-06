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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class ScreenRotationFragment extends BaseFragment implements OnClickListener {

    private Button enableBt, disableBt;
    TextView tv_ScreenRotation;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_screen_rotation, container, false);
        enableBt = (Button) view.findViewById(R.id.fragment_sys_enable);
        disableBt = (Button) view.findViewById(R.id.fragment_sys_disable);
        tv_ScreenRotation = (TextView) view.findViewById(R.id.tv_screen_rotation);

        tv_ScreenRotation.setText("getScreenRotation:"+ SysTester.getInstance().getScreenRotation());

        enableBt.setOnClickListener(this);
        disableBt.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_sys_enable:
                SysTester.getInstance().setScreenRotation(90);
                break;
            case R.id.fragment_sys_disable:
                SysTester.getInstance().setScreenRotation(180);
                break;
            default:
                break;
        }
        tv_ScreenRotation.setText("getScreenRotation:"+ SysTester.getInstance().getScreenRotation());
    }
}
