package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.payment.demo.R;
import com.payment.demo.app.PaymentDemoApp;

/**
 * T009/T011/T012: 主入口 — 金额输入、DAL 就绪校验、开始交易、进入设置.
 */
public class MainActivity extends AppCompatActivity {

    private EditText amountEdit;
    private Button startBtn;
    private Button settingsBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountEdit = findViewById(R.id.amount_edit);
        startBtn = findViewById(R.id.btn_start_transaction);
        settingsBtn = findViewById(R.id.btn_settings);

        startBtn.setOnClickListener(v -> onStartTransaction());
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    private void onStartTransaction() {
        String input = amountEdit.getText() != null ? amountEdit.getText().toString().trim() : "";
        if (input.isEmpty()) {
            Toast.makeText(this, R.string.amount_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        long cent;
        try {
            double d = Double.parseDouble(input);
            if (d <= 0) {
                Toast.makeText(this, R.string.amount_invalid, Toast.LENGTH_SHORT).show();
                return;
            }
            cent = Math.round(d * 100);
            if (cent > 99999999) { // 上限 999999.99 元
                Toast.makeText(this, R.string.amount_too_large, Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, R.string.amount_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        // T012: DAL 就绪校验
        if (PaymentDemoApp.getApp().getDal() == null) {
            Toast.makeText(this, R.string.device_not_ready, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, ReadCardActivity.class);
        i.putExtra(ReadCardActivity.EXTRA_AMOUNT_CENT, cent);
        startActivity(i);
    }
}
