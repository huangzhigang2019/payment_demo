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

package com.pax.demo.modules.ped;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BasePedFragment;
import com.pax.demo.util.Convert;

public class SetPinBeepFragment extends BasePedFragment implements OnClickListener {

    private Button button;
    private EditText editText,editText2;
    private TextView tv_result;
    private boolean isShowBox = false, landscape = false, isRandom = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ped_setpinbeep, container, false);

        button = (Button) view.findViewById(R.id.fragment_ped_base_action);
        editText = (EditText) view.findViewById(R.id.fragment_ped_base_edit_1);
        editText2 = (EditText) view.findViewById(R.id.fragment_ped_base_edit_2);
        tv_result = (TextView) view.findViewById(R.id.tv_result);

        button.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_ped_base_action:
                try {
                    PedTester.getInstance(pedType).setPinBeep(Integer.parseInt(editText.getText().toString()),Integer.parseInt(editText2.getText().toString()));
                    tv_result.setText(getString(R.string.ped_set_key_success));
                }catch (Exception e){
                    e.printStackTrace();
                    tv_result.setText(getString(R.string.ped_set_key_fail));
                }
                break;
            default:
                break;
        }
    }
}
