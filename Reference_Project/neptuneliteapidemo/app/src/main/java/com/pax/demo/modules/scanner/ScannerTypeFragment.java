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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

import com.pax.dal.entity.EChannelType;
import com.pax.dal.entity.EScannerType;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class ScannerTypeFragment extends BaseFragment implements OnItemSelectedListener {

    private Spinner spinner;
    private Button setTypeBt;
    private TextView resultText;
    private EScannerType scannerType;
    private int type = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scanner_set_type, container, false);
        spinner = (Spinner) view.findViewById(R.id.fragment_scanner_spinner);
        setTypeBt = (Button) view.findViewById(R.id.fragment_scanner_set_type_bt);
        resultText = (TextView) view.findViewById(R.id.fragment_scanner_result_text);
        scannerType = EScannerType.valueOf(getArguments().getString("scannerType"));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.scanType));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);

        setTypeBt.setOnClickListener(view1 -> {
           resultText.setText(String.valueOf(ScannerTester.getInstance(scannerType).setScannerType(type)));
        });

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                // Zxing
                type = 0;
                break;
            case 1:
                // Cortex
                type = 1;
                break;
            case 2:
                // ScanIt
                type = 2;
                break;
            case 3:
                // ScanS
                type = 3;
                break;
            case 4:
                // LiveScan
                type = 4;
                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
