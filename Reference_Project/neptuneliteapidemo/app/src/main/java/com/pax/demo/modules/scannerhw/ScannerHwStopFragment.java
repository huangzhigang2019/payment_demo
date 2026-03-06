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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pax.dal.IScannerHw;
import com.pax.dal.exceptions.ScannerHwDevException;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.base.DemoApp;

/**
 * @author JQChen.
 * @date on 2019/8/26.
 */
public class ScannerHwStopFragment extends BaseFragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        TextView mTextView = view.findViewById(R.id.fragment_textview);
        mTextView.setText(getString(R.string.scanner_stop));
        IScannerHw iScannerHw = DemoApp.getDal().getScannerHw();
        if (null != iScannerHw) {
            try {
                iScannerHw.stop();
                iScannerHw.close();
            } catch (ScannerHwDevException e) {
                e.printStackTrace();
            }
        }
        return view;
    }
}
