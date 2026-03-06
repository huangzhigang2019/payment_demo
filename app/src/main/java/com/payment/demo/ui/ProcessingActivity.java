package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    public static final String EXTRA_READ_TYPE = "read_type";
    public static final String EXTRA_CARD_INFO = "card_info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        long amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        int readType = getIntent().getIntExtra(EXTRA_READ_TYPE, 0);
        String cardInfo = getIntent().getStringExtra(EXTRA_CARD_INFO);

        final TextView status = findViewById(R.id.processing_status);
        status.setText(R.string.processing);

        EmvProcessor.PromptCallback promptCb = text -> runOnUiThread(() -> status.setText(text));

        PaymentDemoApp.getApp().runInBackground(() -> {
            ProcessingResult pr = new EmvProcessor(this).process(amountCent, readType, cardInfo, promptCb);
            Transaction t = new Transaction();
            t.setAmountCent(amountCent);
            t.setStartTimeMs(System.currentTimeMillis() - 5000);
            t.setEndTimeMs(System.currentTimeMillis());
            t.setReadType(readType);
            t.setResultStatus(pr.getResultStatus());
            t.setFailReason(pr.getDisplaySummary());

            PaymentDemoApp.getApp().runOnUiThread(() -> {
                Intent i = new Intent(this, ResultActivity.class);
                i.putExtra(ResultActivity.EXTRA_TRANSACTION_AMOUNT_CENT, t.getAmountCent());
                i.putExtra(ResultActivity.EXTRA_TRANSACTION_START, t.getStartTimeMs());
                i.putExtra(ResultActivity.EXTRA_TRANSACTION_END, t.getEndTimeMs());
                i.putExtra(ResultActivity.EXTRA_RESULT_STATUS, t.getResultStatus().name());
                i.putExtra(ResultActivity.EXTRA_RESULT_REASON, t.getFailReason());
                startActivity(i);
                finish();
            });
        });
    }

}
