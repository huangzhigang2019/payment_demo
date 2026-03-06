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
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.modules.scannerhw.ScannerHwTester;
import com.pax.demo.modules.system.SysTester;

public class VSimFragment extends BaseFragment implements View.OnClickListener {

    private Button enableBt;
    private boolean enableB = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        enableBt = (Button) view.findViewById(R.id.fragment_button);
        enableBt.setOnClickListener(this);
        // get the statusBar's status
        enableB = PhoneManagerTester.getInstance().isVSimDataInUse();
        enableBt.setText(enableB ? "disable" : "enable");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_button:
                if (enableB) {
                    PhoneManagerTester.getInstance().turnOffVSimData();
                }else {
                    PhoneManagerTester.getInstance().turnOnVSimData();
                }
                enableB = !enableB;
                enableBt.setText(enableB ? "disable" : "enable");
                break;
            default:
                break;
        }
    }
}
