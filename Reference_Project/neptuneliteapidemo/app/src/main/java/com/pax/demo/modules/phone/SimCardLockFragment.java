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

package com.pax.demo.modules.phone;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class SimCardLockFragment extends BaseFragment {

    private TextView textView;
    private EditText editText;
    private Button button;
    private int subId = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_simcard_lock, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_phone_sim_lock_text);
        editText= view.findViewById(R.id.fragment_phone_sim_lock_edit);
        button = view.findViewById(R.id.fragment_phone_sim_lock_bt);
        textView.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (!editText.getText().toString().trim().equals("")) {
            subId = Integer.parseInt(editText.getText().toString().trim());
        }
        button.setOnClickListener(view1 -> {
            textView.setText(String.valueOf(PhoneManagerTester.getInstance().isSimCardLock(subId)));
        });
        return view;
    }
}
