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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.pax.dal.entity.EPedType;
import com.pax.demo.R;
import com.pax.demo.base.BasePedFragment;

public class PedSelectFragment extends BasePedFragment {

    private Button internalBt;
    private Button externalBt;
    private CheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_select_type, container, false);

        internalBt = (Button) view.findViewById(R.id.fragment_select_bt1);
        externalBt = (Button) view.findViewById(R.id.fragment_select_bt2);

        checkBox = (CheckBox) view.findViewById(R.id.fragment_select_checkbox);
        checkBox.setVisibility(View.INVISIBLE);

        internalBt.setText(getString(R.string.ped_type_INTERNAL));
        internalBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                PedTester.PEDMODE = checkBox.isChecked() ? 1 : 0;
                fragmentSelect(new PedFragment(), EPedType.INTERNAL);
            }
        });

        externalBt.setText(getString(R.string.ped_type_EXTERNAL));
        externalBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                PedTester.PEDMODE = checkBox.isChecked() ? 1 : 0;
                fragmentSelect(new PedFragment(), EPedType.EXTERNAL_TYPEA);
            }
        });
        return view;
    }

}
