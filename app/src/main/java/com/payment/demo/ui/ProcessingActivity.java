package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.pax.dal.entity.EReaderType;
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
    /** 来自确认卡号页，成功后直接进入签名 */
    public static final String EXTRA_FROM_CARDHOLDER_CONFIRM = "from_cardholder_confirm";
    /** PICC 确认后联机阶段传入的脱敏卡号，供跳转 Signature 时使用 */
    public static final String EXTRA_MASKED_CARD_NO = "masked_card_no";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        long amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        long tipCent = getIntent().getLongExtra(EXTRA_TIP_CENT, 0);
        int readType = getIntent().getIntExtra(EXTRA_READ_TYPE, 0);
        String cardInfo = getIntent().getStringExtra(EXTRA_CARD_INFO);
        boolean fromCardholderConfirm = getIntent().getBooleanExtra(EXTRA_FROM_CARDHOLDER_CONFIRM, false);
        String maskedCardNoFromIntent = getIntent().getStringExtra(EXTRA_MASKED_CARD_NO);

        final TextView status = findViewById(R.id.processing_status);
        final ProgressBar spinner = findViewById(R.id.processing_spinner);
        final ImageView iconView = findViewById(R.id.processing_icon);

        boolean isIcc = (readType & EReaderType.ICC.getEReaderType()) == EReaderType.ICC.getEReaderType();
        boolean isPicc = (readType & EReaderType.PICC.getEReaderType()) == EReaderType.PICC.getEReaderType();
        if (fromCardholderConfirm && (isPicc || isIcc)) {
            // ICC/PICC 确认后联机阶段：显示「联机处理中」
            status.setText(R.string.online_processing);
        } else if (isPicc || isIcc) {
            // ICC/PICC 读卡阶段（EMV 读取 PAN/ARQC）：显示「正在读卡」
            status.setText(R.string.card_reading);
        } else {
            status.setText(R.string.processing);
        }
        if (spinner != null) spinner.setVisibility(View.VISIBLE);
        if (iconView != null) iconView.setVisibility(View.GONE);

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
            // ICC/PICC 确认后联机阶段：EMV 已完成，直接视为成功进入签名
            if (fromCardholderConfirm && (isPicc || isIcc)) {
                String masked = maskedCardNoFromIntent != null ? maskedCardNoFromIntent
                        : CardholderConfirmActivity.maskCardInfo(cardInfo);
                PaymentDemoApp.getApp().runOnUiThread(() -> {
                    Intent i = new Intent(this, SignatureActivity.class);
                    i.putExtra(SignatureActivity.EXTRA_AMOUNT_CENT, amountCent);
                    i.putExtra(SignatureActivity.EXTRA_TIP_CENT, tipCent);
                    i.putExtra(SignatureActivity.EXTRA_READ_TYPE, readType);
                    i.putExtra(SignatureActivity.EXTRA_CARD_INFO, cardInfo);
                    i.putExtra(SignatureActivity.EXTRA_MASKED_CARD_NO, masked);
                    startActivity(i);
                    finish();
                });
                return;
            }
            long totalCent = amountCent + tipCent;
            ProcessingResult pr = new EmvProcessor(this).process(totalCent, readType, cardInfo, promptCb);
            Transaction t = new Transaction();
            t.setAmountCent(amountCent);
            t.setTipAmountCent(tipCent);
            t.setStartTimeMs(System.currentTimeMillis() - 5000);
            t.setEndTimeMs(System.currentTimeMillis());
            t.setReadType(readType);
            t.setResultStatus(pr.getResultStatus());
            t.setFailReason(pr.getDisplaySummary());

            PaymentDemoApp.getApp().runOnUiThread(() -> {
                if (pr.getResultStatus() == com.payment.demo.trans.TransResultStatus.NEED_CARDHOLDER_CONFIRM) {
                    // PICC 需联机：先确认卡号，确认后再联机
                    String masked = pr.getPan() != null ? pr.getPan()
                            : com.payment.demo.ui.CardholderConfirmActivity.maskCardInfo(cardInfo);
                    Intent i = new Intent(this, CardholderConfirmActivity.class);
                    i.putExtra(CardholderConfirmActivity.EXTRA_AMOUNT_CENT, amountCent);
                    i.putExtra(CardholderConfirmActivity.EXTRA_TIP_CENT, tipCent);
                    i.putExtra(CardholderConfirmActivity.EXTRA_READ_TYPE, readType);
                    i.putExtra(CardholderConfirmActivity.EXTRA_CARD_INFO, cardInfo);
                    i.putExtra(CardholderConfirmActivity.EXTRA_MASKED_CARD_NO, masked);
                    i.putExtra(CardholderConfirmActivity.EXTRA_BEFORE_ONLINE, true);
                    startActivity(i);
                } else if (pr.getResultStatus() == com.payment.demo.trans.TransResultStatus.SUCCESS) {
                    if (fromCardholderConfirm) {
                        // MAG 先确认再联机 / PICC 先确认再联机：已确认，直接进入签名
                        String masked = pr.getPan() != null ? pr.getPan()
                                : com.payment.demo.ui.CardholderConfirmActivity.maskCardInfo(cardInfo);
                        Intent i = new Intent(this, SignatureActivity.class);
                        i.putExtra(SignatureActivity.EXTRA_AMOUNT_CENT, amountCent);
                        i.putExtra(SignatureActivity.EXTRA_TIP_CENT, tipCent);
                        i.putExtra(SignatureActivity.EXTRA_READ_TYPE, readType);
                        i.putExtra(SignatureActivity.EXTRA_CARD_INFO, cardInfo);
                        i.putExtra(SignatureActivity.EXTRA_MASKED_CARD_NO, masked);
                        startActivity(i);
                    } else {
                        String maskedCardNo = pr.getPan() != null ? pr.getPan()
                                : com.payment.demo.ui.CardholderConfirmActivity.maskCardInfo(cardInfo);
                        Intent i = new Intent(this, CardholderConfirmActivity.class);
                        i.putExtra(CardholderConfirmActivity.EXTRA_AMOUNT_CENT, amountCent);
                        i.putExtra(CardholderConfirmActivity.EXTRA_TIP_CENT, tipCent);
                        i.putExtra(CardholderConfirmActivity.EXTRA_READ_TYPE, readType);
                        i.putExtra(CardholderConfirmActivity.EXTRA_CARD_INFO, cardInfo);
                        i.putExtra(CardholderConfirmActivity.EXTRA_MASKED_CARD_NO, maskedCardNo);
                        startActivity(i);
                    }
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
