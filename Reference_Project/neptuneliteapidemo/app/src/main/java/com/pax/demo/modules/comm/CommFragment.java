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

package com.pax.demo.modules.comm;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.util.BackListAdapter;

public class CommFragment extends BaseFragment implements OnItemClickListener {

    private GridView consoleGridView;
    private BackListAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        consoleGridView = (GridView) view.findViewById(R.id.fragment_gridview);
        adapter = new BackListAdapter(Arrays.asList(getResources().getStringArray(R.array.Comm)), getActivity());
        consoleGridView.setAdapter(adapter);
        consoleGridView.setOnItemClickListener(this);
        return view;
    }

    @SuppressLint("NewApi")
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
        switch (position) {
            case 0:
                fragmentSelect(new UartFragment());
                break;
            case 1:
                fragmentSelect(new ModemFragment());
                break;
            case 2:
                fragmentSelect(new ChannelFragment());
                break;
            case 3:
                fragmentSelect(new BluetoothFragment());
                break;
            case 4:
                fragmentSelect(new UartPortListFragment());
                break;
            case 5:
                fragmentSelect(new SetRouteFragment());
                break;
            case 6:
                fragmentSelect(new SwitchApnFragment());
                break;
            case 7:
                fragmentSelect(new LanConfigFragment());
                break;
            case 8:
                fragmentSelect(new CurrentApnFragment());
                break;
            case 9:
                fragmentSelect(new RouteListFragment());
                break;
            case 10:
                fragmentSelect(new EthernetIfaceStateFragment());
                break;
            case 11:
                fragmentSelect(new MultiPathFragment());
                break;
            default:
                break;
        }
    }
}
