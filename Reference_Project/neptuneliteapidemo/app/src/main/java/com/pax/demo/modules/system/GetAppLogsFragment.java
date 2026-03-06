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
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class GetAppLogsFragment extends BaseFragment {

    private EditText storePath, startDate, endDate;
    private Button getAppLogsBt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_getapplogs, container, false);
        storePath = (EditText) view.findViewById(R.id.fragment_sys_getapplogs_storepath);
        startDate = (EditText) view.findViewById(R.id.fragment_sys_getapplogs_startdate);
        endDate = (EditText) view.findViewById(R.id.fragment_sys_getapplogs_enddate);
        getAppLogsBt = (Button) view.findViewById(R.id.fragment_sys_getapplogs_bt);

        storePath.setText(getActivity().getExternalCacheDir().getAbsolutePath() + "/1.txt");
        startDate.setText(DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()));
        endDate.setText(DateFormat.format("yyyy-MM-dd", System.currentTimeMillis()));

        getAppLogsBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int res = SysTester.getInstance().getAppLogs(storePath.getText().toString(),
                        startDate.getText().toString(), endDate.getText().toString());
                String resStr = "getAppLogs ";
                if (res == 0) {
                    resStr += "successful";
                } else {
                    resStr += "failed";
                }
                Toast.makeText(getActivity(), resStr, Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
