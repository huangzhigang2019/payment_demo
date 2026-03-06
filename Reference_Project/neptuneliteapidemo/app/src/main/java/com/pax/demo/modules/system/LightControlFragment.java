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

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class LightControlFragment extends BaseFragment  implements View.OnClickListener{

    private EditText et_type;
    private EditText et_mode;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_light_control, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        Button  setButton;
        et_type = (EditText) view.findViewById(R.id.fragment_sys_light_control_type);
        et_mode = (EditText) view.findViewById(R.id.fragment_sys_light_control_mode);
        setButton = (Button) view.findViewById(R.id.fragment_sys_light_control_btn);

        setButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_sys_light_control_btn) {
            byte mode = 0;
            byte type = 0;
            if (!et_mode.getText().toString().equals("")) {
                mode = (byte) Integer.parseInt(et_mode.getText().toString().trim());
            }
            if (!et_type.getText().toString().equals("")) {
                type = (byte) Integer.parseInt(et_type.getText().toString().trim());
            }
            SysTester.getInstance().lightControl(type, mode);
        }
    }
}
