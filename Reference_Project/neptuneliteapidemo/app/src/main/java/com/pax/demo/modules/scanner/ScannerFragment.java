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

package com.pax.demo.modules.scanner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pax.dal.entity.EScannerType;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.util.BackListAdapter;

import java.util.Arrays;

public class ScannerFragment extends BaseFragment implements OnItemClickListener {
    private GridView consoleGridView;
    private BackListAdapter adapter;
    private EScannerType scannerType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        scannerType = EScannerType.valueOf(getArguments().getString("scannerType"));
        consoleGridView = (GridView) view.findViewById(R.id.fragment_gridview);
        adapter = new BackListAdapter(Arrays.asList(getResources().getStringArray(R.array.Scanner)), getActivity());
        consoleGridView.setAdapter(adapter);
        consoleGridView.setOnItemClickListener(this);
        return view;
    }

    private void fragmentSelect(Fragment fragment, EScannerType scannerType) {
        FragmentManager fManager = getChildFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("scannerType", scannerType.name());
        fragment.setArguments(bundle);
        transaction.replace(R.id.fragment_screen, fragment);
        transaction.commit();
    }

    @Override
    public void onDestroyView() {
        if (getChildFragmentManager().getBackStackEntryCount() > 0) {
            getChildFragmentManager().popBackStackImmediate();
        }
        super.onDestroyView();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setPos(position);
        adapter.notifyDataSetChanged();
        if (scannerType == null) {
            return;
        }
        switch (position) {
            case 0:
                fragmentSelect(new ScanFragment(), scannerType);
                break;
            case 1:
                fragmentSelect(new ScannerFlashOnFragment(), scannerType);
                break;
            case 2:
                fragmentSelect(new ScannerTypeFragment(), scannerType);
                break;
            case 3:
                fragmentSelect(new BarcodeParamFragment(), scannerType);
                break;
            default:
                break;
        }
    }
}
