package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.demo.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 输入小费界面。小费上限为原金额的 10%，总金额实时更新。
 */
public class TipActivity extends AppCompatActivity {

    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";

    private TextView originalAmountDisplay;
    private TextView tipAmountDisplay;
    private TextView totalAmountDisplay;

    private long amountCent;
    private long tipCent;

    private static final long MAX_CENT = 99999999L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip);

        amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        if (amountCent <= 0) {
            Toast.makeText(this, R.string.amount_invalid, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        originalAmountDisplay = findViewById(R.id.original_amount_display);
        tipAmountDisplay = findViewById(R.id.tip_amount_display);
        totalAmountDisplay = findViewById(R.id.total_amount_display);

        tipCent = 0;
        updateDisplays();

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());

        int[] digitIds = {R.id.btn_0, R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4,
                R.id.btn_5, R.id.btn_6, R.id.btn_7, R.id.btn_8, R.id.btn_9};
        for (int i = 0; i < digitIds.length; i++) {
            final int digit = i;
            findViewById(digitIds[i]).setOnClickListener(v -> appendDigit(digit));
        }

        findViewById(R.id.btn_00).setOnClickListener(v -> append00());
        findViewById(R.id.btn_000).setOnClickListener(v -> append000());

        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            tipCent = 0;
            updateDisplays();
        });

        findViewById(R.id.btn_done).setOnClickListener(v -> onDone());
    }

    private long getTipMaxCent() {
        return amountCent / 10; // 10% max
    }

    private void appendDigit(int digit) {
        long newTip = tipCent * 10 + digit;
        if (newTip > getTipMaxCent()) {
            Toast.makeText(this, R.string.tip_exceeds_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        if (newTip > MAX_CENT) return;
        tipCent = newTip;
        updateDisplays();
    }

    private void append00() {
        long newTip = tipCent * 100;
        if (newTip > getTipMaxCent()) {
            Toast.makeText(this, R.string.tip_exceeds_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        if (newTip > MAX_CENT) return;
        tipCent = newTip;
        updateDisplays();
    }

    private void append000() {
        long newTip = tipCent * 1000;
        if (newTip > getTipMaxCent()) {
            Toast.makeText(this, R.string.tip_exceeds_limit, Toast.LENGTH_SHORT).show();
            return;
        }
        if (newTip > MAX_CENT) return;
        tipCent = newTip;
        updateDisplays();
    }

    private void updateDisplays() {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");

        originalAmountDisplay.setText(df.format(amountCent / 100.0));
        tipAmountDisplay.setText(df.format(tipCent / 100.0));
        totalAmountDisplay.setText(df.format((amountCent + tipCent) / 100.0));
    }

    private void onDone() {
        Intent i = new Intent(this, ReadCardActivity.class);
        i.putExtra(ReadCardActivity.EXTRA_AMOUNT_CENT, amountCent);
        i.putExtra(ReadCardActivity.EXTRA_TIP_CENT, tipCent);
        startActivity(i);
        finish();
    }
}
