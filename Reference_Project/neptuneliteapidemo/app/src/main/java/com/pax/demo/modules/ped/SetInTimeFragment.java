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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BasePedFragment;

public class SetInTimeFragment extends BasePedFragment {

    private TextView writeview;
    private Button button;
    private EditText editText;
    private TextView textView;
    private EditText editText2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ped_base_layout, container, false);
        writeview = (TextView) view.findViewById(R.id.fragment_ped_base_result);
        textView = (TextView) view.findViewById(R.id.fragment_ped_base_title);
        textView.setText(getString(R.string.ped_input_intervaltime));
        button = (Button) view.findViewById(R.id.fragment_ped_base_action);
        editText = (EditText) view.findViewById(R.id.fragment_ped_base_edit_1);
        editText2 = (EditText) view.findViewById(R.id.fragment_ped_base_edit_2);
        editText.setText("4000");
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String num = editText.getText().toString().trim();
                String num2 = editText2.getText().toString().trim();
                if (PedTester.getInstance(pedType).setIntervalTime(num, num2)) {
                    writeview.setText(getString(R.string.ped_intervaltime_success));
                } else {
                    writeview.setText(getString(R.string.ped_intervaltime_fail));
                }

            }
        });

        return view;
    }
}
