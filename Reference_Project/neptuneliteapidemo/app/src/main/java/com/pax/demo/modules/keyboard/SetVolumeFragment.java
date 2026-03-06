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

package com.pax.demo.modules.keyboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class SetVolumeFragment extends BaseFragment {
    private TextView textView;
    private Button button;
    private EditText etVolume;
    private int volume = 20;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard_set_volume, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_keyboard_set_volume_text);
        button = view.findViewById(R.id.fragment_set_volume_bt);
        etVolume = view.findViewById(R.id.fragment_set_volume_edit);

        button.setOnClickListener(view1 -> {
            if (!etVolume.getText().toString().equals("")) {
                volume = Integer.parseInt(etVolume.getText().toString().trim());
            }
            textView.setText(KeyBoardTester.getInstance().setVolume(volume));
        });

        return view;
    }
}
