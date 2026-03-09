package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.demo.R;
import com.payment.demo.app.PaymentDemoApp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 主入口 — 金额键盘输入、DAL 就绪校验、开始交易、进入设置.
 */
public class MainActivity extends AppCompatActivity {

    private TextView amountDisplay;
    private long amountCent;

    private static final long MAX_CENT = 99999999L; // 999999.99

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        amountDisplay = findViewById(R.id.amount_display);
        amountCent = 0;
        updateAmountDisplay();

        int[] digitIds = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9};
        for (int i = 0; i < digitIds.length; i++) {
            final int digit = i;
            findViewById(digitIds[i]).setOnClickListener(v -> appendDigit(digit));
        }

        findViewById(R.id.btn_00).setOnClickListener(v -> append00());
        findViewById(R.id.btn_000).setOnClickListener(v -> append000());

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            amountCent = 0;
            updateAmountDisplay();
        });

        findViewById(R.id.btn_done).setOnClickListener(v -> onStartTransaction());

        ImageButton settingsBtn = findViewById(R.id.btn_settings);
        settingsBtn.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
    }

    @Override
    protected void onResume() {
        super.onResume();
        amountCent = 0;
        updateAmountDisplay();
    }

    private void appendDigit(int digit) {
        if (amountCent > MAX_CENT / 10) return;
        amountCent = amountCent * 10 + digit;
        updateAmountDisplay();
    }

    private void append00() {
        if (amountCent > MAX_CENT / 100) return;
        amountCent = amountCent * 100;
        updateAmountDisplay();
    }

    private void append000() {
        if (amountCent > MAX_CENT / 1000) return;
        amountCent = amountCent * 1000;
        updateAmountDisplay();
    }

    private void updateAmountDisplay() {
        double dollars = amountCent / 100.0;
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");
        amountDisplay.setText(df.format(dollars));
    }

    private void onStartTransaction() {
        if (amountCent <= 0) {
            Toast.makeText(this, R.string.amount_invalid, Toast.LENGTH_SHORT).show();
            return;
        }
        if (amountCent > MAX_CENT) {
            Toast.makeText(this, R.string.amount_too_large, Toast.LENGTH_SHORT).show();
            return;
        }
        if (PaymentDemoApp.getApp().getDal() == null) {
            Toast.makeText(this, R.string.device_not_ready, Toast.LENGTH_SHORT).show();
            return;
        }
        Intent i = new Intent(this, TipActivity.class);
        i.putExtra(TipActivity.EXTRA_AMOUNT_CENT, amountCent);
        startActivity(i);
    }
}
