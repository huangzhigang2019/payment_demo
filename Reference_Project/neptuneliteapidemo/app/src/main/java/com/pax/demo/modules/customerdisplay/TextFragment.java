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

package com.pax.demo.modules.customerdisplay;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

/**
 * @author Charles
 * @date 6/28/2022
 */
public class TextFragment extends BaseFragment {
    private EditText et_x, et_y, et_charset, et_text;
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cus_text, container, false);
        et_x = view.findViewById(R.id.et_x);
        et_y = view.findViewById(R.id.et_y);
        et_x.setText("0");
        et_y.setText("0");
        et_charset = view.findViewById(R.id.et_charset);
        et_text = view.findViewById(R.id.et_text);
        button = view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = Integer.parseInt(et_x.getText().toString().trim());
                int y = Integer.parseInt(et_y.getText().toString().trim());
                String text = et_text.getText().toString();
                String charset = et_charset.getText().toString().trim();
                CustomerDisPlayTester.getInstance().setText(x, y, text, (charset.equals("")?null:charset));
            }
        });

        return view;
    }
}
