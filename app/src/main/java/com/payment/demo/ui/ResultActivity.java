package com.payment.demo.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;

import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.demo.R;

/**
 * T009/T015: 结果页 — 展示成功/失败图标与标题，失败时3秒倒计时自动返回.
 */
public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSACTION_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TRANSACTION_START = "start_ms";
    public static final String EXTRA_TRANSACTION_END = "end_ms";
    public static final String EXTRA_RESULT_STATUS = "result_status";
    public static final String EXTRA_RESULT_REASON = "result_reason";

    private static final int COUNTDOWN_SECONDS = 3;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String status = getIntent().getStringExtra(EXTRA_RESULT_STATUS);
        boolean isSuccess = "SUCCESS".equals(status);

        ImageView iconView = findViewById(R.id.result_icon);
        TextView titleView = findViewById(R.id.result_title);
        TextView countdownView = findViewById(R.id.result_countdown);
        TextView returnBtn = findViewById(R.id.btn_return);

        iconView.setImageResource(isSuccess ? R.drawable.sale_demo_successful_transaction : R.drawable.sale_demo_deal_failed);
        titleView.setText(isSuccess ? R.string.transaction_approved : R.string.transaction_failed);

        returnBtn.setOnClickListener(v -> onReturn());

        if (isSuccess) {
            countdownView.setVisibility(View.GONE);
        } else {
            countdownView.setVisibility(View.VISIBLE);
            startCountdown(countdownView);
        }
    }

    private void startCountdown(TextView countdownView) {
        final int[] remaining = {COUNTDOWN_SECONDS};
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                if (remaining[0] <= 0) {
                    onReturn();
                    return;
                }
                String template = getString(R.string.return_in_seconds);
                String full = String.format(template, remaining[0]);
                SpannableString ss = new SpannableString(full);
                int numStart = full.indexOf(String.valueOf(remaining[0]));
                int numEnd = numStart + String.valueOf(remaining[0]).length();
                if (numStart >= 0 && numEnd <= full.length()) {
                    ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ResultActivity.this, R.color.lanhu_blue)), numStart, numEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ss.setSpan(new StyleSpan(Typeface.BOLD), numStart, numEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                countdownView.setText(ss);
                remaining[0]--;
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(countdownRunnable);
    }

    private void onReturn() {
        if (countdownRunnable != null) {
            handler.removeCallbacks(countdownRunnable);
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        if (countdownRunnable != null) {
            handler.removeCallbacks(countdownRunnable);
        }
        super.onDestroy();
    }
}
