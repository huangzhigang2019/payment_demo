package com.pax.demo.modules.system;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class UpdateSettingsPasswordFragment extends BaseFragment implements OnClickListener {

    private Button enableBt;
    EditText et_password;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sys_update_settings_password, container, false);
        enableBt = (Button) view.findViewById(R.id.fragment_sys_set);
        et_password = (EditText) view.findViewById(R.id.et_password);

        et_password.setText("pax9876@@");

        enableBt.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_sys_set:
                String password = et_password.getText().toString();
                if(password.isEmpty()){
                    password = "pax9876@@";
                    et_password.setText(password);
                }
                SysTester.getInstance().updateSettingsPasswordHashValue(password);
                break;
            default:
                break;
        }
    }
}
