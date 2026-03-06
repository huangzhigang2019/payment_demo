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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class RebootTimeFragment extends BaseFragment implements View.OnClickListener {

    private EditText editText;
    private TextView textView;
    private Button getButton, setButton, enableButton, disableButton;

    private String rebootTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_reboot_time, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        editText = (EditText) view.findViewById(R.id.fragment_sys_reboot_time_et);
        textView = (TextView) view.findViewById(R.id.fragment_sys_reboot_time_tv);
        getButton = (Button) view.findViewById(R.id.fragment_sys_reboot_time_get_bt);
        setButton = (Button) view.findViewById(R.id.fragment_sys_reboot_time_set_bt);
        enableButton = (Button) view.findViewById(R.id.fragment_sys_reboot_time_enable);
        disableButton = (Button) view.findViewById(R.id.fragment_sys_reboot_time_disable);

        enableButton.setOnClickListener(this);
        disableButton.setOnClickListener(this);
        getButton.setOnClickListener(this);
        setButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_sys_reboot_time_get_bt:
                textView.setText(SysTester.getInstance().getRebootTime());
                break;
            case R.id.fragment_sys_reboot_time_set_bt:
                rebootTime = editText.getText().toString();
                if (rebootTime != null && rebootTime.length() > 0) {
                    SysTester.getInstance().setRebootTime(rebootTime);
                }else{
                    Toast.makeText(getActivity(), "input invalid", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.fragment_sys_reboot_time_enable:
                SysTester.getInstance().setRebootTimeEnable(true);
                break;
            case R.id.fragment_sys_reboot_time_disable:
                SysTester.getInstance().setRebootTimeEnable(false);
            default:
                break;
        }
    }
}
