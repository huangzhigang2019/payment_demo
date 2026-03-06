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

package com.pax.demo.modules.scannerhw;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.dal.entity.EChannelType;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.modules.deviceinfo.DeviceInfoTester;

public class ScannerHwPreviewFormatFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button setBt, getBt;
    private TextView resultText;
    private int format = 0;
    private String resultString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scannerhw_preview, container, false);
        spinner = (Spinner) view.findViewById(R.id.fragment_scannerhw_spinner);
        setBt = (Button) view.findViewById(R.id.fragment_scannerhw_set_preview_bt);
        getBt = (Button) view.findViewById(R.id.fragment_scannerhw_get_preview_bt);
        resultText = (TextView) view.findViewById(R.id.fragment_scannerhw_result_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.previewFormat));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        setBt.setOnClickListener(this);
        getBt.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_scannerhw_set_preview_bt:
                resultString = ScannerHwTester.getInstance().setPreviewFormat(format);
                resultText.setText(resultString);
                break;
            case R.id.fragment_scannerhw_get_preview_bt:
                resultString = ScannerHwTester.getInstance().getPreviewFormat();
                resultText.setText(resultString);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                // YUV
                format = 0;
                break;
            case 1:
                // RAW
                format = 1;
                break;
            case 2:
                // special mode
                format = 2;
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
