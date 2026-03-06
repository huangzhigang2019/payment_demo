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

package com.pax.demo.modules.ocr;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

/**
 * @author JQChen.
 * @date on 2020/4/21.
 */
public class OCRFragment extends BaseFragment {
    public static final int CAMER_ID_REAR = 0;
    public static final int CAMER_ID_FRONT = 1;
    private Button rearButton, frontButton;
    private TextView resultTextView;
    private OCRTester ocrTester;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ocr_layout, container, false);
        rearButton = view.findViewById(R.id.ocr_btn1);
        frontButton = view.findViewById(R.id.ocr_btn2);
        resultTextView = view.findViewById(R.id.ocr_tv);
        initTester();
        setClick();
        return view;
    }

    private void setClick() {
        rearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Rear camera
                ocrTester.ocr(CAMER_ID_REAR);
            }
        });
        frontButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // front camera
                ocrTester.ocr(CAMER_ID_FRONT);
            }
        });
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == OCRTester.WHAT_OCR) {
                String s = (String) msg.obj;
                resultTextView.setText(TextUtils.isEmpty(s) ? "result is none!" : s);
            }
        }
    };

    private void initTester() {
        ocrTester = new OCRTester(handler);
    }
}
