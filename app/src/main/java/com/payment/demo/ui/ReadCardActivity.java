package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pax.dal.entity.EReaderType;

import com.payment.demo.R;
import com.payment.demo.app.PaymentDemoApp;
import com.payment.demo.card.CardReaderManager;
import com.payment.demo.config.ConfigStatusProvider;
import com.payment.demo.config.TerminalConfigStatus;
import com.payment.demo.trans.CardReadResult;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * T009/T012: 读卡引导 — 显示插卡/挥卡提示，取消，超时/成功跳转.
 */
public class ReadCardActivity extends AppCompatActivity {
    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";

    private TextView promptText;
    private TextView amountText;
    private ImageView iconSwipe;
    private ImageView iconInsert;
    private ImageView iconTap;
    private ImageButton backBtn;
    private Button retryBtn;
    private long amountCent;
    private long tipCent;
    private CardReaderManager cardReader;
    private boolean isFailed;
    private boolean piccSuccessDetected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_card);

        amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        tipCent = getIntent().getLongExtra(EXTRA_TIP_CENT, 0);
        promptText = findViewById(R.id.read_card_prompt);
        amountText = findViewById(R.id.read_card_amount);
        iconSwipe = findViewById(R.id.icon_swipe);
        iconInsert = findViewById(R.id.icon_insert);
        iconTap = findViewById(R.id.icon_tap);
        backBtn = findViewById(R.id.btn_back);
        retryBtn = findViewById(R.id.btn_retry);

        updateAmountDisplay();
        updateIconsForAvailableModes();
        promptText.setText(R.string.please_swipe_insert_tap);

        backBtn.setOnClickListener(v -> onBackOrCancel());
        retryBtn.setOnClickListener(v -> startDetectAgain());

        cardReader = new CardReaderManager(PaymentDemoApp.getApp());
        byte modeMask = buildModeMask();
        cardReader.startDetect(60_000, modeMask, result -> {
            PaymentDemoApp.getApp().runOnUiThread(() -> onDetectResult(result));
        });
    }

    private void updateAmountDisplay() {
        double dollars = (amountCent + tipCent) / 100.0;
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");
        amountText.setText(df.format(dollars));
    }

    private void updateIconsForAvailableModes() {
        TerminalConfigStatus status = ConfigStatusProvider.getInstance(this).getStatus();
        boolean picc = status.isPiccAvailable();
        boolean icc = status.isIccAvailable();
        boolean mag = status.isMagAvailable();

        iconSwipe.setVisibility(mag ? View.VISIBLE : View.GONE);
        iconInsert.setVisibility(icc ? View.VISIBLE : View.GONE);
        iconTap.setVisibility(picc ? View.VISIBLE : View.GONE);

        if (!picc && !icc && !mag) {
            iconSwipe.setVisibility(View.VISIBLE);
            iconInsert.setVisibility(View.VISIBLE);
            iconTap.setVisibility(View.VISIBLE);
        }
    }

    private void onBackOrCancel() {
        if (isFailed) {
            finish();
        } else {
            onCancel();
        }
    }

    private void onDetectResult(CardReadResult result) {
        if (result.isSuccess()) {
            if (result.getReadType() == com.pax.dal.entity.EReaderType.PICC.getEReaderType()) {
                piccSuccessDetected = true;
            }
            // MAG: 先确认卡号再联机（读卡时已有 track2）
            if (result.getReadType() == EReaderType.MAG.getEReaderType() && result.getCardInfo() != null) {
                String masked = CardholderConfirmActivity.maskCardInfo(result.getCardInfo());
                Intent i = new Intent(this, CardholderConfirmActivity.class);
                i.putExtra(CardholderConfirmActivity.EXTRA_AMOUNT_CENT, amountCent);
                i.putExtra(CardholderConfirmActivity.EXTRA_TIP_CENT, tipCent);
                i.putExtra(CardholderConfirmActivity.EXTRA_READ_TYPE, result.getReadType());
                i.putExtra(CardholderConfirmActivity.EXTRA_CARD_INFO, result.getCardInfo());
                i.putExtra(CardholderConfirmActivity.EXTRA_MASKED_CARD_NO, masked);
                i.putExtra(CardholderConfirmActivity.EXTRA_FROM_READ_CARD, true);
                startActivity(i);
                finish();
                return;
            }
            Intent i = new Intent(this, ProcessingActivity.class);
            i.putExtra(ProcessingActivity.EXTRA_AMOUNT_CENT, amountCent);
            i.putExtra(ProcessingActivity.EXTRA_TIP_CENT, tipCent);
            i.putExtra(ProcessingActivity.EXTRA_READ_TYPE, result.getReadType());
            i.putExtra(ProcessingActivity.EXTRA_CARD_INFO, result.getCardInfo());
            startActivity(i);
            finish();
            return;
        }
        isFailed = true;
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
        retryBtn.setVisibility(View.VISIBLE);
    }

    private void startDetectAgain() {
        isFailed = false;
        retryBtn.setVisibility(View.GONE);
        promptText.setText(R.string.please_swipe_insert_tap);
        updateIconsForAvailableModes();
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

    private void onCancel() {
        if (cardReader != null) cardReader.stopDetect();
        finish();
    }

    @Override
    protected void onDestroy() {
        // For PICC success, the reader must stay open for ClssProcess EMV transaction.
        // EmvProcessor will close it after the transaction completes.
        if (cardReader != null && !piccSuccessDetected) cardReader.stopDetect();
        super.onDestroy();
    }
}
