package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.payment.demo.R;

/**
 * 签名界面。CLEAR 清空，SAVE 保存签名并跳转 PrintPreviewActivity。
 */
public class SignatureActivity extends AppCompatActivity {

    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";
    public static final String EXTRA_READ_TYPE = "read_type";
    public static final String EXTRA_CARD_INFO = "card_info";
    public static final String EXTRA_MASKED_CARD_NO = "masked_card_no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        SignatureView signatureView = findViewById(R.id.signature_view);

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
        findViewById(R.id.btn_clear).setOnClickListener(v -> signatureView.clear());
        findViewById(R.id.btn_save).setOnClickListener(v -> {
            if (!signatureView.hasSignature()) {
                Toast.makeText(this, R.string.please_sign_first, Toast.LENGTH_SHORT).show();
                return;
            }
            byte[] signatureData = signatureView.exportToJpegBytes(80);

            Intent i = new Intent(this, PrintPreviewActivity.class);
            i.putExtra(PrintPreviewActivity.EXTRA_AMOUNT_CENT, getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0));
            i.putExtra(PrintPreviewActivity.EXTRA_TIP_CENT, getIntent().getLongExtra(EXTRA_TIP_CENT, 0));
            i.putExtra(PrintPreviewActivity.EXTRA_READ_TYPE, getIntent().getIntExtra(EXTRA_READ_TYPE, 0));
            i.putExtra(PrintPreviewActivity.EXTRA_CARD_INFO, getIntent().getStringExtra(EXTRA_CARD_INFO));
            i.putExtra(PrintPreviewActivity.EXTRA_MASKED_CARD_NO, getIntent().getStringExtra(EXTRA_MASKED_CARD_NO));
            i.putExtra(PrintPreviewActivity.EXTRA_SIGNATURE_DATA, signatureData);
            startActivity(i);
            finish();
        });
    }
}
