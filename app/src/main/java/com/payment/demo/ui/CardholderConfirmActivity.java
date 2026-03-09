package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.payment.demo.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 持卡人确认界面。显示脱敏卡号，CANCEL 返回主界面，CONFIRM 进入 ProcessingActivity。
 */
public class CardholderConfirmActivity extends AppCompatActivity {

    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";
    public static final String EXTRA_READ_TYPE = "read_type";
    public static final String EXTRA_CARD_INFO = "card_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardholder_confirm);

        long amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        long tipCent = getIntent().getLongExtra(EXTRA_TIP_CENT, 0);
        int readType = getIntent().getIntExtra(EXTRA_READ_TYPE, 0);
        String cardInfo = getIntent().getStringExtra(EXTRA_CARD_INFO);

        TextView amountText = findViewById(R.id.amount_text);
        TextView cardNumberDisplay = findViewById(R.id.card_number_display);

        double totalDollars = (amountCent + tipCent) / 100.0;
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");
        amountText.setText(df.format(totalDollars));

        String masked = maskCardInfo(cardInfo);
        cardNumberDisplay.setText(masked);

        findViewById(R.id.btn_back).setOnClickListener(v -> onCancel());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> onCancel());
        findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            Intent i = new Intent(this, ProcessingActivity.class);
            i.putExtra(ProcessingActivity.EXTRA_AMOUNT_CENT, amountCent);
            i.putExtra(ProcessingActivity.EXTRA_TIP_CENT, tipCent);
            i.putExtra(ProcessingActivity.EXTRA_READ_TYPE, readType);
            i.putExtra(ProcessingActivity.EXTRA_CARD_INFO, cardInfo);
            startActivity(i);
            finish();
        });
    }

    public static String maskCardInfo(String cardInfo) {
        if (cardInfo == null || cardInfo.isEmpty()) return "**** **** **** ****";
        String pan = extractPan(cardInfo);
        if (pan == null || pan.length() < 8) return "**** **** **** ****";
        return pan.substring(0, 4) + " **** **** " + pan.substring(pan.length() - 4);
    }

    private static String extractPan(String cardInfo) {
        if (cardInfo == null) return null;
        String s = cardInfo.trim();
        if (s.matches("\\d+")) return s;
        if (s.contains("=") || s.contains("D")) {
            int eq = s.indexOf('=');
            int d = s.indexOf('D');
            int end = eq >= 0 ? eq : (d >= 0 ? d : s.length());
            String numPart = s.substring(0, end).replaceAll("\\D", "");
            if (numPart.length() >= 13) return numPart;
        }
        return s.replaceAll("\\D", "").length() >= 13 ? s.replaceAll("\\D", "") : s;
    }

    private void onCancel() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
