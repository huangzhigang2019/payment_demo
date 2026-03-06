package com.payment.demo.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.payment.demo.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * T009/T015: 结果页 — 展示金额、时间、状态、原因摘要，返回/再试一笔.
 */
public class ResultActivity extends AppCompatActivity {
    public static final String EXTRA_TRANSACTION_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TRANSACTION_START = "start_ms";
    public static final String EXTRA_TRANSACTION_END = "end_ms";
    public static final String EXTRA_RESULT_STATUS = "result_status";
    public static final String EXTRA_RESULT_REASON = "result_reason";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        long amountCent = getIntent().getLongExtra(EXTRA_TRANSACTION_AMOUNT_CENT, 0);
        long startMs = getIntent().getLongExtra(EXTRA_TRANSACTION_START, 0);
        long endMs = getIntent().getLongExtra(EXTRA_TRANSACTION_END, 0);
        String status = getIntent().getStringExtra(EXTRA_RESULT_STATUS);
        String reason = getIntent().getStringExtra(EXTRA_RESULT_REASON);

        ImageView iconView = findViewById(R.id.result_icon);
        TextView amountTv = findViewById(R.id.result_amount);
        TextView timeTv = findViewById(R.id.result_time);
        TextView statusTv = findViewById(R.id.result_status);
        TextView reasonTv = findViewById(R.id.result_reason);

        boolean isSuccess = "SUCCESS".equals(status);
        iconView.setImageResource(isSuccess ? R.drawable.success : R.drawable.failed);

        amountTv.setText(getString(R.string.app_name) + " 金额: " + (amountCent / 100.0) + " 元");
        timeTv.setText("时间: " + formatTime(endMs));
        String statusDisplay = status != null ? mapStatusToDisplay(status) : "—";
        statusTv.setText(statusDisplay);
        reasonTv.setText(reason != null ? reason : "");

        Button backBtn = findViewById(R.id.btn_back);
        backBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
        Button againBtn = findViewById(R.id.btn_try_again);
        againBtn.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    private String formatTime(long ms) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(ms));
    }

    private String mapStatusToDisplay(String status) {
        if (status == null) return "—";
        switch (status) {
            case "SUCCESS": return getString(R.string.trans_success);
            case "CANCELLED": return getString(R.string.trans_cancelled);
            case "TIMEOUT": return getString(R.string.trans_timeout);
            default: return getString(R.string.trans_failed);
        }
    }
}
