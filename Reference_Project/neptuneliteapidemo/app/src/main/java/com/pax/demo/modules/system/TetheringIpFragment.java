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

public class TetheringIpFragment extends BaseFragment  implements View.OnClickListener{

    private TextView tv_get;
    private EditText et_set,et_get;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_tethering_ip_control, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        Button  setButton,getButton;
        tv_get = (TextView) view.findViewById(R.id.fragment_sys_get_tv);
        et_set = (EditText) view.findViewById(R.id.fragment_sys_set_et);
        et_get = (EditText) view.findViewById(R.id.fragment_sys_get_et);
        getButton = (Button) view.findViewById(R.id.fragment_sys_get_bt);
        setButton = (Button) view.findViewById(R.id.fragment_sys_set_btn);

        getButton.setOnClickListener(this);
        setButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fragment_sys_get_bt) {
            tv_get.setText(SysTester.getInstance().getTetheringIp(Integer.parseInt(et_get.getText().toString())));
        }
        if (v.getId() == R.id.fragment_sys_set_btn) {
            if(SysTester.getInstance().setTetheringIp(Integer.parseInt(et_get.getText().toString()),String.valueOf(et_set.getText()))){
                Toast.makeText(getContext(),"Set Successful",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getContext(),"Setup failure",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
