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
import android.widget.AdapterView;
import android.widget.GridView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.util.BackListAdapter;

import java.util.Arrays;

/**
 * @author JQChen.
 * @date on 2019/8/26.
 */
public class ScannerHwFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    private BackListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        GridView gridView = view.findViewById(R.id.fragment_gridview);
        adapter = new BackListAdapter(Arrays.asList(getResources().getStringArray(R.array.scannerhw)), getActivity());
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setPos(position);
        adapter.notifyDataSetChanged();
        switch (position) {
            case 0:
                fragmentSelect(new ScannerHwStartFragment());
                break;
            case 1:
                fragmentSelect(new ScannerHwStopFragment());
                break;
            case 2:
                fragmentSelect(new ScannerHwBarcodeFragment());
                break;
            case 3:
                fragmentSelect(new ScannerHwConfigFragment());
                break;
            case 4:
                fragmentSelect(new ScannerHwReadRawFragment());
                break;
            case 5:
                fragmentSelect(new ScannerHwSupportFormatFragment());
                break;
            case 6:
                fragmentSelect(new ScannerHwPreviewFormatFragment());
                break;
            default:
                break;
        }
    }
}
