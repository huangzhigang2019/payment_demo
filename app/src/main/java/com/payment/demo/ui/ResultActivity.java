package com.payment.demo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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
        long amountCent = getIntent().getLongExtra(EXTRA_TRANSACTION_AMOUNT_CENT, 0);
        long startMs = getIntent().getLongExtra(EXTRA_TRANSACTION_START, 0);
        long endMs = getIntent().getLongExtra(EXTRA_TRANSACTION_END, 0);
        ImageView iconView = findViewById(R.id.result_icon);
        TextView amountView = findViewById(R.id.result_amount);
        TextView timeView = findViewById(R.id.result_time);
        TextView titleView = findViewById(R.id.result_title);
        TextView countdownView = findViewById(R.id.result_countdown);
        TextView returnBtn = findViewById(R.id.btn_return);

        setResultIcon(iconView, isSuccess);
        iconView.setContentDescription(getString(isSuccess ? R.string.trans_success : R.string.trans_failed));
        titleView.setText(isSuccess ? R.string.transaction_approved : R.string.transaction_failed);

        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");
        amountView.setText(df.format(amountCent / 100.0));
        if (endMs > 0) {
            timeView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date(endMs)));
            timeView.setVisibility(View.VISIBLE);
        } else {
            timeView.setVisibility(View.GONE);
        }

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

    /**
     * 结果页只需要「成功/失败图标」切图。
     * 现有蓝湖资源里 `sale_demo_success_result` / `sale_demo_failed_result` 是整页截图（带底图），
     * 因此用蓝湖结果页全屏图（`sale_demo_successful_transaction` / `sale_demo_deal_failed`）
     * 按比例裁剪出中间图标区域作为真正的切图展示。
     */
    private void setResultIcon(ImageView iconView, boolean isSuccess) {
        int srcRes = isSuccess ? R.drawable.sale_demo_successful_transaction : R.drawable.sale_demo_deal_failed;
        Bitmap src = BitmapFactory.decodeResource(getResources(), srcRes);
        if (src == null) {
            iconView.setImageResource(isSuccess ? R.drawable.sale_demo_successful_transaction : R.drawable.sale_demo_deal_failed);
            return;
        }
        int w = src.getWidth();
        int h = src.getHeight();

        // Icon roughly sits around upper-middle of the screen in Lanhu assets.
        // Use relative crop to be resolution-agnostic.
        float centerX = 0.5f * w;
        float centerY = 0.36f * h;
        int cropSize = Math.round(0.42f * w); // square crop, sized by width
        int left = Math.max(0, Math.round(centerX - cropSize / 2f));
        int top = Math.max(0, Math.round(centerY - cropSize / 2f));
        int right = Math.min(w, left + cropSize);
        int bottom = Math.min(h, top + cropSize);
        // Adjust if clamped
        left = Math.max(0, right - cropSize);
        top = Math.max(0, bottom - cropSize);

        try {
            Bitmap cropped = Bitmap.createBitmap(src, left, top, right - left, bottom - top);
            iconView.setImageBitmap(cropped);
        } catch (Throwable ignored) {
            iconView.setImageBitmap(src);
        }
    }
}
