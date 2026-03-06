
package com.pax.demo.modules.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pax.dal.entity.ENavigationKey;
import com.pax.dal.entity.PosMenu;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

import java.util.HashMap;
import java.util.Map;

public class PosMenuFragment extends BaseFragment implements OnClickListener, OnItemSelectedListener {
    private Spinner spinner;
    private Button enableBt, visibleBt;

    private String posMenuKey;
    private boolean enaleB = true, visibleB = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sys_bar_key, container, false);

        spinner = (Spinner) view.findViewById(R.id.fragment_sys_bar_key_spinner);
        enableBt = (Button) view.findViewById(R.id.fragment_sys_bar_key_enable_bt);
        visibleBt = (Button) view.findViewById(R.id.fragment_sys_bar_key_visible_bt);

        enaleB = SysTester.getInstance().isNavigationBarEnabled();
        visibleB = SysTester.getInstance().isNavigationBarVisible();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.posmenu));
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(this);
        spinner.setSelection(0);
        enableBt.setOnClickListener(this);
        visibleBt.setOnClickListener(this);

        enableBt.setText("enable");
        visibleBt.setText("disable");
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                posMenuKey = "brightness";
                break;
            case 1:
                posMenuKey = "regulatoryinfo";
                break;
            case 2:
                posMenuKey = "password_settings";
                break;
            default:
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onClick(View v) {
        Map<PosMenu, Boolean> posMenu = new HashMap<>();
        switch (v.getId()) {
            case R.id.fragment_sys_bar_key_enable_bt:
                if("brightness".equals(posMenuKey)){
                    posMenu.put(PosMenu.QS_SETTING,false);
                }else if("regulatoryinfo".equals(posMenuKey)){
                    posMenu.put(PosMenu.QS_REGULATORYINFO,false);
                }else if("password_settings".equals(posMenuKey)){
                    posMenu.put(PosMenu.SHOW_PASSWORD_SETTINGS,false);
                }
                break;

            case R.id.fragment_sys_bar_key_visible_bt:
                if("brightness".equals(posMenuKey)){
                    posMenu.put(PosMenu.QS_SETTING,true);
                }else if("regulatoryinfo".equals(posMenuKey)){
                    posMenu.put(PosMenu.QS_REGULATORYINFO,true);
                }else if("password_settings".equals(posMenuKey)){
                    posMenu.put(PosMenu.SHOW_PASSWORD_SETTINGS,true);
                }
                break;
            default:
                break;
        }
        SysTester.getInstance().disablePosMenu(posMenu);

    }
}
