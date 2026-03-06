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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BasePedFragment;
import com.pax.demo.modules.system.SysTester;

public class PinMuteFragment extends BasePedFragment implements View.OnClickListener{


    private Button mute, unmute;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ped_pin_mute, container, false);
        mute = (Button) view.findViewById(R.id.fragment_ped_mute);
        unmute = (Button) view.findViewById(R.id.fragment_ped_unmute);

        mute.setOnClickListener(this);
        unmute.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_ped_mute:
                PedTester.getInstance(pedType).setPinMute(true);
                break;

            case R.id.fragment_ped_unmute:
                PedTester.getInstance(pedType).setPinMute(false);
                break;
            default:
                break;
        }
    }
}
