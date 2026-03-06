
package com.pax.demo.modules.deviceinfo;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.dal.entity.AppNetworkStats;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class AppNetworkStatsFragment extends BaseFragment {
    private TextView textView;
    private Button bt_network;
    private EditText et_app;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_network, container, false);
        textView = (TextView) view.findViewById(R.id.fragment_textview);
        bt_network = (Button) view.findViewById(R.id.bt_network);
        et_app = (EditText) view.findViewById(R.id.et_app);
        bt_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String app = et_app.getText().toString();
                if(app.isEmpty()){
                    app = "com.pax.demo";
                }
                AppNetworkStats mAppNetworkStats = DeviceInfoTester.getInstance().getAppNetworkStats(app);
                textView.setText("TotalRxBytes:" +mAppNetworkStats.getTotalRxBytes()+"\n"+
                        "TotalTxBytes:" +mAppNetworkStats.getTotalTxBytes());
            }
        });

        return view;
    }
}