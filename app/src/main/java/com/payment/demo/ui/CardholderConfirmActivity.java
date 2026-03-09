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
 * 持卡人确认界面。显示脱敏卡号（来自 EMV 流程），CANCEL 返回主界面，CONFIRM 进入 SignatureActivity。
 */
public class CardholderConfirmActivity extends AppCompatActivity {

    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";
    public static final String EXTRA_READ_TYPE = "read_type";
    public static final String EXTRA_CARD_INFO = "card_info";
    public static final String EXTRA_MASKED_CARD_NO = "masked_card_no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cardholder_confirm);

        long amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        long tipCent = getIntent().getLongExtra(EXTRA_TIP_CENT, 0);
        int readType = getIntent().getIntExtra(EXTRA_READ_TYPE, 0);
        String cardInfo = getIntent().getStringExtra(EXTRA_CARD_INFO);
        String maskedCardNo = getIntent().getStringExtra(EXTRA_MASKED_CARD_NO);

        TextView amountText = findViewById(R.id.amount_text);
        TextView cardNumberDisplay = findViewById(R.id.card_number_display);

        double totalDollars = (amountCent + tipCent) / 100.0;
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");
        amountText.setText(df.format(totalDollars));

        String masked = (maskedCardNo != null && !maskedCardNo.isEmpty()) ? maskedCardNo : maskCardInfo(cardInfo);
        cardNumberDisplay.setText(masked);

        findViewById(R.id.btn_back).setOnClickListener(v -> onCancel());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> onCancel());
        findViewById(R.id.btn_confirm).setOnClickListener(v -> {
            Intent i = new Intent(this, SignatureActivity.class);
            i.putExtra(SignatureActivity.EXTRA_AMOUNT_CENT, amountCent);
            i.putExtra(SignatureActivity.EXTRA_TIP_CENT, tipCent);
            i.putExtra(SignatureActivity.EXTRA_READ_TYPE, readType);
            i.putExtra(SignatureActivity.EXTRA_CARD_INFO, cardInfo);
            i.putExtra(SignatureActivity.EXTRA_MASKED_CARD_NO, masked);
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
        if (s.isEmpty()) return null;
        // 纯数字：取前16位（标准PAN长度）
        if (s.matches("\\d+")) {
            return s.length() >= 8 ? s.substring(0, Math.min(16, s.length())) : null;
        }
        // Track2 格式: PAN + 'D'/'d'/'=' + 后续
        int sep = -1;
        int eq = s.indexOf('=');
        int dUpper = s.indexOf('D');
        int dLower = s.indexOf('d');
        int d = (dUpper >= 0 && dLower >= 0) ? Math.min(dUpper, dLower)
                : (dUpper >= 0 ? dUpper : (dLower >= 0 ? dLower : -1));
        if (eq >= 0 || d >= 0) {
            sep = (eq >= 0 && d >= 0) ? Math.min(eq, d) : (eq >= 0 ? eq : d);
        }
        if (sep >= 0) {
            String numPart = s.substring(0, sep).replaceAll("\\D", "");
            if (numPart.length() >= 8) return numPart.substring(0, Math.min(16, numPart.length()));
        }
        // 兜底：取所有数字的前16位
        String digits = s.replaceAll("\\D", "");
        return digits.length() >= 8 ? digits.substring(0, Math.min(16, digits.length())) : null;
    }

    private void onCancel() {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        finish();
    }
}
