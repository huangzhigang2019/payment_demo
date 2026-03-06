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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.util.BackListAdapter;

import java.util.Arrays;

/**
 * @author Charles
 * @date 6/28/2022
 */
public class CustomerDisplayFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private LinearLayout screenLayout;
    private GridView consoleGridView;
    private BackListAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        screenLayout = (LinearLayout) view.findViewById(R.id.fragment_screen);
        consoleGridView = (GridView) view.findViewById(R.id.fragment_gridview);

        adapter = new BackListAdapter(Arrays.asList(getResources().getStringArray(R.array.customerdisplay)), getActivity());
        consoleGridView.setAdapter(adapter);
        consoleGridView.setOnItemClickListener(this);
        return view;
    }
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        adapter.setPos(i);
        adapter.notifyDataSetChanged();
        switch (i) {
            case 0:
                fragmentSelect(new PropertyFragment());
                break;
            case 1:
                fragmentSelect(new BrightnessFragment());
                break;
            case 2:
                fragmentSelect(new BackgroudImageFragment());
                break;
            case 3:
                fragmentSelect(new BackgroundSolidFragment());
                break;
            case 4:
                fragmentSelect(new TextFragment());
                break;
            case 5:
                fragmentSelect(new ClearFragment());
                break;
            case 6:
                fragmentSelect(new FontFragment());
                break;
            case 7:
                fragmentSelect(new SignBoardFragment());
                break;
            case 8:
                fragmentSelect(new SignBoardImageFragment());
                break;
            case 9:
                fragmentSelect(new DefaultBackgroundImageFragment());
                break;
            default:
                break;
        }
    }
}
