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

package com.pax.demo.modules.deviceinfo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class TrafficFragment extends BaseFragment implements OnClickListener, AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private Button getTraffic;
    private TextView resultText;
    private int type = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_device_traffic, container, false);
        spinner = (Spinner) view.findViewById(R.id.fragment_device_traffic_spinner);
        getTraffic = (Button) view.findViewById(R.id.fragment_device_traffic_button);
        resultText = (TextView) view.findViewById(R.id.fragment_device_traffic_result_text);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.selectNetWork));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        getTraffic.setOnClickListener(this);

        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                // wifi
                type = 1;
                break;
            case 1:
                // mobile
                type = 2;
                break;
            case 2:
                // ethernet
                type = 3;
                break;

            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // channelType = EChannelType.WIFI;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_device_traffic_button:
                resultText.setText(DeviceInfoTester.getInstance().getTrafficOfEachApp(type));
                break;
            default:
                break;
        }

    }
}
