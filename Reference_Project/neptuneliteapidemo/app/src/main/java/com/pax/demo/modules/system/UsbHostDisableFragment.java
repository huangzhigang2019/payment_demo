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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class UsbHostDisableFragment extends BaseFragment implements OnClickListener {

    private Button enableBt, disableBt;
    TextView tv_result;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_usb_host, container, false);
        enableBt = (Button) view.findViewById(R.id.fragment_sys_enable);
        disableBt = (Button) view.findViewById(R.id.fragment_sys_disable);
        tv_result = (TextView) view.findViewById(R.id.tv_result);
        tv_result.setText("isUsbHostDisabled:"+String.valueOf(SysTester.getInstance().isUsbHostDisabled()));
        enableBt.setOnClickListener(this);
        disableBt.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_sys_enable:
                SysTester.getInstance().setUsbHostDisable(false);
                break;
            case R.id.fragment_sys_disable:
                SysTester.getInstance().setUsbHostDisable(true);
                break;
            default:
                break;
        }
        tv_result.setText("isUsbHostDisabled:"+String.valueOf(SysTester.getInstance().isUsbHostDisabled()));
    }
}
