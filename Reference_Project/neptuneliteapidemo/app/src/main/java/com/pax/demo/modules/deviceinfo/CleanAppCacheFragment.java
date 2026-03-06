
package com.pax.demo.modules.deviceinfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.dal.IDeviceInfo;
import com.pax.dal.entity.AppNetworkStats;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class CleanAppCacheFragment extends BaseFragment {
    private TextView textView;
    private Button bt_clean_app_cache;
    private EditText et_app;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clean_app_cache, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_textview);
        bt_clean_app_cache = (Button) view.findViewById(R.id.bt_clean_app_cache);
        et_app = (EditText) view.findViewById(R.id.et_app);
        bt_clean_app_cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String app = et_app.getText().toString();
                if(app.isEmpty()){
                    app = "com.pax.demo";
                }
                DeviceInfoTester.getInstance().cleanAppCache(app, new IDeviceInfo.ResultCallback() {
                    @Override
                    public void onResult(boolean b) {
                        textView.setText("cleanAppCache:"+b);
                    }
                });
            }
        });

        return view;
    }
}