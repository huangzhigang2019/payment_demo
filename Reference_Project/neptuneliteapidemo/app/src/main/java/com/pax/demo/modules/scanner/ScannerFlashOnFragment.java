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

package com.pax.demo.modules.scanner;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pax.dal.entity.EScannerType;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.modules.scannerhw.ScannerHwTester;

public class ScannerFlashOnFragment extends BaseFragment {
    private TextView textView;
    private Button turnOn, turnOff;
    private EScannerType scannerType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner_flash_on, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_scanner_flash_result);
        scannerType = EScannerType.valueOf(getArguments().getString("scannerType"));
        turnOn = view.findViewById(R.id.fragment_scanner_flash_on_bt);
        turnOff = view.findViewById(R.id.fragment_scanner_flash_off_bt);
        turnOn.setOnClickListener(view1 -> {
            textView.setText(String.valueOf(ScannerTester.getInstance(scannerType).setFlashOn(true)));
        });
        turnOff.setOnClickListener(view1 -> {
            textView.setText(String.valueOf(ScannerTester.getInstance(scannerType).setFlashOn(false)));
        });

        return view;
    }
}
