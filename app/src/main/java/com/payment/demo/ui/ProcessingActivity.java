package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.payment.demo.R;
import com.payment.demo.app.PaymentDemoApp;
import com.payment.demo.emv.EmvProcessor;
import com.payment.demo.trans.ProcessingResult;
import com.payment.demo.trans.Transaction;

/**
 * T009/T014: 处理中 — 显示处理中，调用 EMV（或占位），跳转结果页.
 */
public class ProcessingActivity extends AppCompatActivity {
    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";
    public static final String EXTRA_READ_TYPE = "read_type";
    public static final String EXTRA_CARD_INFO = "card_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        long amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        long tipCent = getIntent().getLongExtra(EXTRA_TIP_CENT, 0);
        int readType = getIntent().getIntExtra(EXTRA_READ_TYPE, 0);
        String cardInfo = getIntent().getStringExtra(EXTRA_CARD_INFO);

        final TextView status = findViewById(R.id.processing_status);
        final ProgressBar spinner = findViewById(R.id.processing_spinner);
        final ImageView iconView = findViewById(R.id.processing_icon);
        status.setText(R.string.processing);

        EmvProcessor.PromptCallback promptCb = text -> runOnUiThread(() -> {
            status.setText(text);
            boolean isPin = text != null && (text.contains("PIN") || text.contains("密码") || text.contains("请输入"));
            if (spinner != null) spinner.setVisibility(isPin ? View.GONE : View.VISIBLE);
            if (iconView != null) {
                iconView.setVisibility(isPin ? View.VISIBLE : View.GONE);
                if (isPin) iconView.setImageResource(R.drawable.sale_demo_pin_screen);
            }
        });

        PaymentDemoApp.getApp().runInBackground(() -> {
            ProcessingResult pr = new EmvProcessor(this).process(amountCent, readType, cardInfo, promptCb);
            Transaction t = new Transaction();
            t.setAmountCent(amountCent);
            t.setTipAmountCent(tipCent);
            t.setStartTimeMs(System.currentTimeMillis() - 5000);
            t.setEndTimeMs(System.currentTimeMillis());
            t.setReadType(readType);
            t.setResultStatus(pr.getResultStatus());
            t.setFailReason(pr.getDisplaySummary());

            PaymentDemoApp.getApp().runOnUiThread(() -> {
                if (pr.getResultStatus() == com.payment.demo.trans.TransResultStatus.SUCCESS) {
                    String maskedCardNo = com.payment.demo.ui.CardholderConfirmActivity.maskCardInfo(cardInfo);
                    Intent i = new Intent(this, SignatureActivity.class);
                    i.putExtra(SignatureActivity.EXTRA_AMOUNT_CENT, amountCent);
                    i.putExtra(SignatureActivity.EXTRA_TIP_CENT, tipCent);
                    i.putExtra(SignatureActivity.EXTRA_READ_TYPE, readType);
                    i.putExtra(SignatureActivity.EXTRA_CARD_INFO, cardInfo);
                    i.putExtra(SignatureActivity.EXTRA_MASKED_CARD_NO, maskedCardNo);
                    startActivity(i);
                } else {
                    Intent i = new Intent(this, ResultActivity.class);
                    i.putExtra(ResultActivity.EXTRA_TRANSACTION_AMOUNT_CENT, t.getAmountCent() + tipCent);
                    i.putExtra(ResultActivity.EXTRA_TRANSACTION_START, t.getStartTimeMs());
                    i.putExtra(ResultActivity.EXTRA_TRANSACTION_END, t.getEndTimeMs());
                    i.putExtra(ResultActivity.EXTRA_RESULT_STATUS, t.getResultStatus().name());
                    i.putExtra(ResultActivity.EXTRA_RESULT_REASON, t.getFailReason());
                    startActivity(i);
                }
                finish();
            });
        });
    }

}
