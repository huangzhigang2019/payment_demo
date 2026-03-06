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
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.modules.scannerhw.ScannerHwTester;

public class setLightFragment extends BaseFragment {

    private TextView textView;
    private Button button;
    private EditText etMode, etVolume;
    private byte mode = 1;
    private byte volume = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyboard_set_light, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_set_light_text);
        button = view.findViewById(R.id.fragment_set_light_bt);
        etMode = view.findViewById(R.id.fragment_set_light_mode_edit);
        etVolume = view.findViewById(R.id.fragment_set_light_volume_edit);
        if (!etMode.getText().toString().equals("")) {
            mode = Byte.parseByte(etMode.getText().toString().trim());
        }
        if (!etVolume.getText().toString().equals("")) {
            volume = Byte.parseByte(etVolume.getText().toString().trim());
        }
        button.setOnClickListener(view1 -> {
            textView.setText(KeyBoardTester.getInstance().setLight(mode, volume));
        });

        return view;
    }
}
