package com.pax.demo.modules.system;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class AllowAccessContactsBtPairingFragment extends BaseFragment implements OnClickListener {

    private Button enable, disable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_allow_access_contacts_bt_pairing, container, false);
        enable = (Button) view.findViewById(R.id.fragment_sys_enable);
        disable = (Button) view.findViewById(R.id.fragment_sys_disable);
        enable.setOnClickListener(this);
        disable.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_sys_enable:
                SysTester.getInstance().allowAccessContactsBtPairing(true);
                break;
            case R.id.fragment_sys_disable:
                SysTester.getInstance().allowAccessContactsBtPairing(false);
                break;
            default:
                break;
        }
    }
}
