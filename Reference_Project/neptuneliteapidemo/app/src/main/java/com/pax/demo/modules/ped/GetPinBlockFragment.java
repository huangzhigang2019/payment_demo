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
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.dal.IPed;
import com.pax.dal.entity.EKeyCode;
import com.pax.dal.entity.EPedType;
import com.pax.demo.R;
import com.pax.demo.base.BasePedFragment;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.Convert;

public class GetPinBlockFragment extends BasePedFragment implements OnClickListener {

    private TextView resultText;
    private Button button;
    private EditText editText;

    private Button showInputBoxBt, setLayoutLandscapeBt, randomBt;
    private EditText pedTitleEt;
    private boolean isShowBox = false, landscape = false, isRandom = true;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 0 && msg.obj != null) {
                resultText.setText(getActivity().getResources().getString(R.string.ped_pin_result1) + "\n"
                        + Convert.getInstance().bcdToStr((byte[]) msg.obj));
            } else {
                resultText.setText(getActivity().getResources().getString(R.string.ped_pin_result2));
            }
        };
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ped_getpinblock, container, false);

        resultText = (TextView) view.findViewById(R.id.fragment_ped_base_result);
        button = (Button) view.findViewById(R.id.fragment_ped_base_action);
        editText = (EditText) view.findViewById(R.id.fragment_ped_base_edit_1);
        view.findViewById(R.id.fragment_ped_base_edit_2).setVisibility(View.GONE);

        showInputBoxBt = (Button) view.findViewById(R.id.fragment_ped_getpinblock_showbox);
        showInputBoxBt.setText(isShowBox == true ? "showInputBox" : "hideInputBox");
        pedTitleEt = (EditText) view.findViewById(R.id.fragment_ped_getpinblock_ped_title);

        setLayoutLandscapeBt = (Button) view.findViewById(R.id.fragment_ped_getpinblock_setlandscape);
        setLayoutLandscapeBt.setText(landscape == true ? "Horizontal" : "Vertical");

        randomBt = (Button) view.findViewById(R.id.fragment_ped_getpinblock_setrandom);
        randomBt.setText(isRandom ? "random" : "order");

        setLayoutLandscapeBt.setOnClickListener(this);
        button.setOnClickListener(this);
        showInputBoxBt.setOnClickListener(this);
        randomBt.setOnClickListener(this);

        Button cancelBtn = view.findViewById(R.id.fragment_ped_base_cancel_btn);
        cancelBtn.setVisibility(View.VISIBLE);
        cancelBtn.setOnClickListener(this);

        Button cancelMuteBtn = view.findViewById(R.id.fragment_ped_base_cancelMute_btn);
        cancelMuteBtn.setVisibility(View.VISIBLE);
        cancelMuteBtn.setOnClickListener(this);

        init();
        return view;
    }


    public void init() {
        PedTester.getInstance(pedType).setKeyboardLayoutLandscape(false);
        PedTester.getInstance(pedType).setKeyboardRandom(true);
        PedTester.getInstance(pedType).showInputBox(false, "");
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_ped_base_action:
                String num = editText.getText().toString().trim();
                if (num.length() < 13) {
                    resultText.setText(getActivity().getResources().getString(R.string.ped_number_error));
                    return;
                }
                byte[] panArray = num.substring(num.length() - 13, num.length() - 1).getBytes();
                final byte[] dataIn = new byte[16];
                dataIn[0] = 0x00;
                dataIn[1] = 0x00;
                dataIn[2] = 0x00;
                dataIn[3] = 0x00;
                System.arraycopy(panArray, 0, dataIn, 4, panArray.length);
                resultText.setText(getActivity().getResources().getString(R.string.ped_input_password));

                new Thread(new Runnable() {
                    public void run() {
                        byte[] result = PedTester.getInstance(pedType).getPinBlock(dataIn);
                        Message.obtain(handler, 0, result).sendToTarget();
                    }
                }).start();
                break;
            case R.id.fragment_ped_getpinblock_showbox:
                isShowBox = !isShowBox;
                PedTester.getInstance(pedType).showInputBox(isShowBox, pedTitleEt.getText().toString());
                showInputBoxBt.setText(isShowBox == true ? "showInputBox" : "hideInputBox");
                break;
            case R.id.fragment_ped_getpinblock_setlandscape:
                landscape = !landscape;
                PedTester.getInstance(pedType).setKeyboardLayoutLandscape(landscape);
                setLayoutLandscapeBt.setText(landscape == true ? "Horizontal" : "Vertical");
                break;
            case R.id.fragment_ped_getpinblock_setrandom:
                isRandom = !isRandom;
                PedTester.getInstance(pedType).setKeyboardRandom(isRandom);
                randomBt.setText(isRandom ? "random" : "order");
                break;
            case R.id.fragment_ped_base_cancel_btn:
                PedTester.getInstance(pedType).cancelInput();
            case R.id.fragment_ped_base_cancelMute_btn:
                PedTester.getInstance(pedType).cancelInputMute();
                break;
            default:
                break;
        }
    }
}
