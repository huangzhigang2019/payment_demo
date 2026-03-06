package com.payment.demo.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.payment.demo.R;
import com.payment.demo.config.ConfigStatusProvider;
import com.payment.demo.config.TerminalConfigStatus;

/**
 * T009/T020: 设置/信息 — 展示版本、参数是否加载、读卡是否可用.
 */
public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        TerminalConfigStatus status = ConfigStatusProvider.getInstance(this).getStatus();

        TextView info = findViewById(R.id.settings_info);
        StringBuilder sb = new StringBuilder();
        sb.append("应用版本: ").append(status.getAppVersion()).append("\n");
        sb.append("内核版本: ").append(status.getKernelVersion()).append("\n");
        sb.append("参数已加载: ").append(status.isParamLoaded()).append("\n");
        sb.append("读卡可用: ").append(status.isCardReaderAvailable()).append("\n");
        sb.append("  - 挥卡(PICC): ").append(status.isPiccAvailable()).append("\n");
        sb.append("  - 插卡(ICC): ").append(status.isIccAvailable()).append("\n");
        sb.append("  - 刷磁条(MAG): ").append(status.isMagAvailable()).append("\n");
        sb.append("可交易: ").append(status.isTradable());
        info.setText(sb.toString());

        findViewById(R.id.btn_settings_back).setOnClickListener(v -> finish());
    }
}
