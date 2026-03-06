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

package com.pax.demo.modules.system;

import java.util.Arrays;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;
import com.pax.demo.util.BackListAdapter;

public class SystemFragment extends BaseFragment implements OnItemClickListener {

    private LinearLayout screenLayout;
    private GridView consoleGridView;
    private BackListAdapter adapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        screenLayout = (LinearLayout) view.findViewById(R.id.fragment_screen);
        consoleGridView = (GridView) view.findViewById(R.id.fragment_gridview);

        adapter = new BackListAdapter(Arrays.asList(getResources().getStringArray(R.array.Sys)), getActivity());
        consoleGridView.setAdapter(adapter);
        consoleGridView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.setPos(position);
        adapter.notifyDataSetChanged();
        switch (position) {
            case 0:
                fragmentSelect(new BeepFragment());
                break;
            case 1:
                fragmentSelect(new TermInfoFragment());
                break;
            case 2:
                fragmentSelect(new GetRadomFragment());
                break;
            case 3:
                fragmentSelect(new DevInterfaceVerFragment());
                break;
            case 4:
                fragmentSelect(new NavigationBarFragment());
                break;
            case 5:
                fragmentSelect(new StatusBarFragment());
                break;
            case 6:
                fragmentSelect(new PowerFragment());
                break;
            case 7:
                fragmentSelect(new SettingsNeedPWFragment());
                break;
            case 8:
                fragmentSelect(new SwitchTouchModeFragment());
                break;
            case 9:
                fragmentSelect(new GetAppLogsFragment());
                break;
            case 10:
                fragmentSelect(new ReadTUSNFragment());
                break;
            case 11:
                fragmentSelect(new BaseInfoFragment());
                break;
            case 12:
                fragmentSelect(new SystemLanguageFragment());
                break;
            case 13:
                fragmentSelect(new GetPNFragment());
                break;
            case 14:
                fragmentSelect(new ScreenBrightnessFragment());
                break;
            case 15:
                fragmentSelect(new UsbModeFragment());
                break;
            case 16:
                fragmentSelect(new VolumeKeyFragment());
                break;
            case 17:
                fragmentSelect(new LocationFragment());
                break;
            case 18:
                fragmentSelect(new GoToSleepFragment());
                break;
            case 19:
                fragmentSelect(new SecurityInfoFragment());
                break;
            case 20:
                fragmentSelect(new RebootTimeFragment());
                break;
            case 21:
                fragmentSelect(new ScreenshotFragment());
                break;
            case 22:
                fragmentSelect(new LightControlFragment());
                break;
            case 23:
                fragmentSelect(new TetheringIpFragment());
                break;
            case 24:
                fragmentSelect(new SetChargerScreenDisabledFragment());
                break;
            case 25:
                fragmentSelect(new SetApplicationNeedPasswordFragment());
                break;
            case 26:
                fragmentSelect(new PosMenuFragment());
                break;
            case 27:
                fragmentSelect(new BatteryManagerFragment());
                break;
            case 28:
                fragmentSelect(new SetAppPowerSaveConfigWithTypeFragment());
                break;
            case 29:
                fragmentSelect(new GlobalAirPlanFragment());
                break;
            case 30:
                fragmentSelect(new LongPowerKeyFragment());
                break;
            case 31:
                fragmentSelect(new GlobalSilentFragment());
                break;
            case 32:
                fragmentSelect(new SmartSavingModeWhenChargingFragment());
                break;
            case 33:
                fragmentSelect(new ShowCarrierNameFragment());
                break;
            case 34:
                fragmentSelect(new UsbHostDisableFragment());
                break;
            case 35:
                fragmentSelect(new WifiWakeupEnabledFragment());
                break;
            case 36:
                SysTester.getInstance().getShowBatteryLevel();
                break;
            case 37:
                fragmentSelect(new ScreenRotationFragment());
                break;
            case 38:
                fragmentSelect(new UpdateSettingsPasswordFragment());
                break;
            case 39:
                fragmentSelect(new AllowAccessContactsBtPairingFragment());
            case 40:
                fragmentSelect(new AllowBtOpenAirplaneFragment());
                break;
            default:
                break;
        }
    }
}
