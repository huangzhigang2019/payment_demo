package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pax.dal.entity.EReaderType;

import com.payment.demo.R;
import com.payment.demo.app.PaymentDemoApp;
import com.payment.demo.card.CardReaderManager;
import com.payment.demo.config.ConfigStatusProvider;
import com.payment.demo.config.TerminalConfigStatus;
import com.payment.demo.trans.CardReadResult;

/**
 * T009/T012: 读卡引导 — 显示插卡/挥卡提示，取消，超时/成功跳转.
 */
public class ReadCardActivity extends AppCompatActivity {
    public static final String EXTRA_AMOUNT_CENT = "amount_cent";

    private TextView promptText;
    private Button cancelBtn;
    private long amountCent;
    private CardReaderManager cardReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        promptText = findViewById(R.id.read_card_prompt);
        cancelBtn = findViewById(R.id.btn_cancel);

        updatePromptForAvailableModes();
        cancelBtn.setOnClickListener(v -> onCancel());

        cardReader = new CardReaderManager(PaymentDemoApp.getApp());
        byte modeMask = buildModeMask();
        cardReader.startDetect(60_000, modeMask, result -> {
            PaymentDemoApp.getApp().runOnUiThread(() -> onDetectResult(result));
        });
    }

    private void onDetectResult(CardReadResult result) {
        if (result.isSuccess()) {
            Intent i = new Intent(this, ProcessingActivity.class);
            i.putExtra(ProcessingActivity.EXTRA_AMOUNT_CENT, amountCent);
            i.putExtra(ProcessingActivity.EXTRA_READ_TYPE, result.getReadType());
            i.putExtra(ProcessingActivity.EXTRA_CARD_INFO, result.getCardInfo());
            startActivity(i);
            finish();
            return;
        }
        // Failure: stay on page, show message, offer Retry / Back / 换方式 (no finish)
        if (result.getFailCode() == CardReadResult.RET_TIMEOUT) {
            promptText.setText(getString(R.string.read_card_timeout) + "\n" + getString(R.string.try_different_mode));
        } else if (result.getFailCode() == CardReadResult.RET_INIT_FAILED) {
            promptText.setText(getString(R.string.read_card_init_failed) + "\n" + getString(R.string.try_different_mode)
                    + (result.getFailReason() != null ? "\n" + result.getFailReason() : ""));
        } else if (result.getFailCode() == CardReadResult.RET_CANCEL) {
            promptText.setText(getString(R.string.trans_cancelled));
        } else {
            String reason = result.getFailReason();
            boolean maybeCardRemoved = reason != null && (reason.contains("remove") || reason.contains("移") || reason.contains("REVERSE"));
            promptText.setText(maybeCardRemoved ? getString(R.string.card_removed_retry)
                    : (reason != null ? reason : getString(R.string.trans_failed)) + "\n" + getString(R.string.try_different_mode));
        }
        cancelBtn.setText(R.string.back);
        cancelBtn.setOnClickListener(v -> finish());
        Button retryBtn = findViewById(R.id.btn_retry);
        if (retryBtn != null) {
            retryBtn.setVisibility(View.VISIBLE);
            retryBtn.setOnClickListener(v -> startDetectAgain());
        }
    }

    private void startDetectAgain() {
        Button retryBtn = findViewById(R.id.btn_retry);
        if (retryBtn != null) retryBtn.setVisibility(View.GONE);
        updatePromptForAvailableModes();
        cancelBtn.setText(R.string.cancel);
        cancelBtn.setOnClickListener(v -> onCancel());
        if (cardReader != null) cardReader.stopDetect();
        cardReader = new CardReaderManager(PaymentDemoApp.getApp());
        byte modeMask = buildModeMask();
        cardReader.startDetect(60_000, modeMask, result -> PaymentDemoApp.getApp().runOnUiThread(() -> onDetectResult(result)));
    }

    private byte buildModeMask() {
        TerminalConfigStatus status = ConfigStatusProvider.getInstance(this).getStatus();
        byte mode = 0;
        if (status.isPiccAvailable()) mode |= EReaderType.PICC.getEReaderType();
        if (status.isIccAvailable()) mode |= EReaderType.ICC.getEReaderType();
        if (status.isMagAvailable()) mode |= EReaderType.MAG.getEReaderType();
        return mode;
    }

    private void updatePromptForAvailableModes() {
        TerminalConfigStatus status = ConfigStatusProvider.getInstance(this).getStatus();
        boolean picc = status.isPiccAvailable();
        boolean icc = status.isIccAvailable();
        boolean mag = status.isMagAvailable();
        int resId;
        if (picc && icc && mag) resId = R.string.read_mode_all;
        else if (picc && icc) resId = R.string.read_mode_picc_icc;
        else if (picc && mag) resId = R.string.read_mode_picc_mag;
        else if (icc && mag) resId = R.string.read_mode_icc_mag;
        else if (picc) resId = R.string.read_mode_picc;
        else if (icc) resId = R.string.read_mode_icc;
        else if (mag) resId = R.string.read_mode_mag;
        else resId = R.string.read_mode_none;
        promptText.setText(resId);
    }

    private void onCancel() {
        if (cardReader != null) cardReader.stopDetect();
        finish();
    }

    @Override
    protected void onDestroy() {
        if (cardReader != null) cardReader.stopDetect();
        super.onDestroy();
    }
}
