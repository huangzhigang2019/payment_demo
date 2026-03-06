/*
 *  ===========================================================================================
 *  = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *     This software is supplied under the terms of a license agreement or nondisclosure
 *     agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *     disclosed except in accordance with the terms in that agreement.
 *          Copyright (C) 2020 -? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 *  Description: // Detail description about the function of this module,
 *               // interfaces with the other modules, and dependencies.
 *  Revision History:
 *  Date	               Author	                   Action
 *  2020/06/19 	         JackHuang           	      Create
 *  ===========================================================================================
 */
package com.paxsz.demo.emvdemo.trans;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.pax.commonlib.utils.convert.ConvertHelper;
import com.paxsz.demo.emvdemo.R;
import com.paxsz.demo.emvdemo.trans.timerecord.entity.ApduTimeRecord;
import com.paxsz.demo.emvdemo.util.TimeRecordUtils;

import java.util.ArrayList;


public class ApduTimeRecordActivity extends AppCompatActivity {
    private TextView apduTimeRecordTotalText;
    private TextView apduTimeRecordDetailText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apdu_time_record);
        apduTimeRecordTotalText = findViewById(R.id.apdu_time_record_total);
        apduTimeRecordDetailText = findViewById(R.id.apdu_time_record_detail);
        apduTimeRecordDetailText.setMovementMethod(ScrollingMovementMethod.getInstance());
        updateApduTimeRecord();
    }


    private void updateApduTimeRecord(){
        long apduTotalMs = 0;
        long commandApduMs = 0;

        ArrayList<ApduTimeRecord> list = TimeRecordUtils.getApduTimeRecords();
        String total = "Total Ms（Apdu + Process）: " + (list.get(list.size() -1).getFinishTimeMs() - list.get(0).getStartTimeMs() + "\n");
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("The detail of first tap apdu:\n");
        for(int i = 0; i < list.size(); i++){
            stringBuffer.append("\n");
            stringBuffer.append("Command: " + ConvertHelper.getConvert().bcdToStr(list.get(i).getCommand()) + "\n");
            stringBuffer.append("Command Describe: " + TimeRecordUtils.getCommandString(list.get(i).getCommand()) + "\n");
            commandApduMs = list.get(i).getFinishTimeMs() - list.get(i).getStartTimeMs();
            stringBuffer.append("Command Total Ms: " + commandApduMs + "\n");
            apduTotalMs += commandApduMs;
        }
        stringBuffer.append("\n" + "Apdu Total Ms: " + apduTotalMs);
        apduTimeRecordTotalText.setText(total);
        apduTimeRecordDetailText.setText(stringBuffer);
    }
}
