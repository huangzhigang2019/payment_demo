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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class SettingsNeedPWFragment extends BaseFragment {

    private Button needPWBt;
    private boolean needPWB = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_button, container, false);
        needPWBt = (Button) view.findViewById(R.id.fragment_button);

        needPWB = SysTester.getInstance().isPowerKeyEnabled();
        needPWBt.setText(needPWB == true ? "not need" : "need");
        needPWBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SysTester.getInstance().setSettingsNeedPassword(!needPWB);
                needPWB = !needPWB;
                needPWBt.setText(needPWB == true ? "not need" : "need");
            }
        });

        return view;
    }
}
