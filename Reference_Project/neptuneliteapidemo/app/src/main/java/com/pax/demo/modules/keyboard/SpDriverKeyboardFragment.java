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

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.dal.entity.EKeyBoardType;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class SpDriverKeyboardFragment extends BaseFragment {
    private TextView textView,fragment_mute_text;
    private Button button,fragment_get_volume_bt,fragment_set_mute_bt,fragment_set_unmute_bt,fragment_get_mute_bt;
    private EditText etVolume;
    private int volume = 20;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sp_keyboard, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_keyboard_set_volume_text);
        button = view.findViewById(R.id.fragment_set_volume_bt);
        etVolume = view.findViewById(R.id.fragment_set_volume_edit);
        fragment_get_mute_bt = view.findViewById(R.id.fragment_get_mute_bt);

        fragment_get_volume_bt = view.findViewById(R.id.fragment_get_volume_bt);

        fragment_set_mute_bt = view.findViewById(R.id.fragment_set_mute_bt);
        fragment_set_unmute_bt = view.findViewById(R.id.fragment_set_unmute_bt);
        fragment_get_mute_bt = view.findViewById(R.id.fragment_get_mute_bt);

        button.setOnClickListener(view1 -> {
            if (!etVolume.getText().toString().equals("")) {
                volume = Integer.parseInt(etVolume.getText().toString().trim());
            }
            textView.setText(KeyBoardTester.getInstance().setVolume(EKeyBoardType.SP_DRIVER_KEYBOARD,volume));
        });

        fragment_get_volume_bt.setOnClickListener(view1 -> {
            textView.setText(""+KeyBoardTester.getInstance().getVolume());
        });

        fragment_set_mute_bt.setOnClickListener(view1 -> {
            textView.setText(KeyBoardTester.getInstance().setMute(EKeyBoardType.SP_DRIVER_KEYBOARD,true));
        });

        fragment_set_unmute_bt.setOnClickListener(view1 -> {
            textView.setText(KeyBoardTester.getInstance().setMute(EKeyBoardType.SP_DRIVER_KEYBOARD,false));
        });

        fragment_get_mute_bt.setOnClickListener(view1 -> {
            textView.setText("getMute:"+KeyBoardTester.getInstance().getMute());
        });

        return view;
    }
}
