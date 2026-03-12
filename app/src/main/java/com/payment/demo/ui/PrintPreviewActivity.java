package com.payment.demo.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.payment.demo.R;
import com.payment.demo.app.PaymentDemoApp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 打印预览界面。展示小票内容（卡号、金额、小费、合计、时间、签名）。
 * OK 尝试打印；CANCEL 直接返回结果页。
 */
public class PrintPreviewActivity extends AppCompatActivity {

    public static final String EXTRA_AMOUNT_CENT = "amount_cent";
    public static final String EXTRA_TIP_CENT = "tip_cent";
    public static final String EXTRA_READ_TYPE = "read_type";
    public static final String EXTRA_CARD_INFO = "card_info";
    public static final String EXTRA_MASKED_CARD_NO = "masked_card_no";
    public static final String EXTRA_SIGNATURE_DATA = "signature_data";

    private long amountCent;
    private long tipCent;
    private String maskedCardNo;
    private byte[] signatureData;
    private Button btnPrint;
    private boolean printInProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_preview);

        amountCent = getIntent().getLongExtra(EXTRA_AMOUNT_CENT, 0);
        tipCent = getIntent().getLongExtra(EXTRA_TIP_CENT, 0);
        maskedCardNo = getIntent().getStringExtra(EXTRA_MASKED_CARD_NO);
        signatureData = getIntent().getByteArrayExtra(EXTRA_SIGNATURE_DATA);

        if (maskedCardNo == null || maskedCardNo.isEmpty()) {
            maskedCardNo = "**** **** **** ****";
        }

        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");

        TextView cardNo = findViewById(R.id.receipt_card_no);
        TextView amountTv = findViewById(R.id.receipt_amount);
        TextView tipTv = findViewById(R.id.receipt_tip);
        TextView totalTv = findViewById(R.id.receipt_total);
        TextView timeTv = findViewById(R.id.receipt_time);
        ImageView sigView = findViewById(R.id.receipt_signature);

        cardNo.setText(getString(R.string.receipt_card_label) + " " + maskedCardNo);
        amountTv.setText(getString(R.string.receipt_amount_label) + " " + df.format(amountCent / 100.0));
        tipTv.setText(getString(R.string.receipt_tip_label) + " " + df.format(tipCent / 100.0));
        totalTv.setText(getString(R.string.receipt_total_label) + " " + df.format((amountCent + tipCent) / 100.0));
        timeTv.setText(getString(R.string.receipt_time_label) + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));

        if (signatureData != null && signatureData.length > 0) {
            Bitmap bmp = BitmapFactory.decodeByteArray(signatureData, 0, signatureData.length);
            if (bmp != null) {
                sigView.setImageBitmap(bmp);
                sigView.setVisibility(android.view.View.VISIBLE);
            } else {
                sigView.setVisibility(android.view.View.GONE);
            }
        } else {
            sigView.setVisibility(android.view.View.GONE);
        }

        btnPrint = findViewById(R.id.btn_ok);
        findViewById(R.id.btn_back).setOnClickListener(v -> goToResult());
        findViewById(R.id.btn_cancel).setOnClickListener(v -> goToResult());
        btnPrint.setOnClickListener(v -> doPrint());
    }

    private void doPrint() {
        if (printInProgress) return;
        printInProgress = true;
        btnPrint.setEnabled(false);
        btnPrint.setText(R.string.printing_in_progress);
        // PAX printer API may require main thread; run print on UI thread to avoid "no print" on some devices.
        runOnUiThread(() -> {
            boolean printed = tryPrint();
            printInProgress = false;
            btnPrint.setEnabled(true);
            btnPrint.setText(R.string.print_receipt);
            if (printed) {
                Toast.makeText(this, R.string.print_complete, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.printer_not_available, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean tryPrint() {
        try {
            Object dal = PaymentDemoApp.getApp().getDal();
            if (dal == null) return false;
            java.lang.reflect.Method getPrinter = dal.getClass().getMethod("getPrinter");
            Object printer = getPrinter.invoke(dal);
            if (printer == null) return false;
            Class<?> printerClass = printer.getClass();
            java.lang.reflect.Method init = printerClass.getMethod("init");
            init.invoke(printer);
            String receipt = buildReceiptText();
            java.lang.reflect.Method printStr = printerClass.getMethod("printStr", String.class, String.class);
            printStr.invoke(printer, receipt, "");
            java.lang.reflect.Method start = printerClass.getMethod("start");
            start.invoke(printer);
            return true;
        } catch (Throwable t) {
            android.util.Log.w("PrintPreview", "Print failed", t);
            return false;
        }
    }

    private String buildReceiptText() {
        DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
        df.applyPattern("$ #,##0.00");
        String timeStr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(getString(R.string.receipt_card_label)).append(" ").append(maskedCardNo).append("\n");
        sb.append(getString(R.string.receipt_amount_label)).append(" ").append(df.format(amountCent / 100.0)).append("\n");
        sb.append(getString(R.string.receipt_tip_label)).append(" ").append(df.format(tipCent / 100.0)).append("\n");
        sb.append(getString(R.string.receipt_total_label)).append(" ").append(df.format((amountCent + tipCent) / 100.0)).append("\n");
        sb.append(getString(R.string.receipt_time_label)).append(" ").append(timeStr).append("\n");
        sb.append("--- ").append(getString(R.string.signature_title)).append(" ---\n");
        return sb.toString();
    }

    private void goToResult() {
        Intent i = new Intent(this, ResultActivity.class);
        i.putExtra(ResultActivity.EXTRA_TRANSACTION_AMOUNT_CENT, amountCent + tipCent);
        i.putExtra(ResultActivity.EXTRA_TRANSACTION_START, System.currentTimeMillis() - 5000);
        i.putExtra(ResultActivity.EXTRA_TRANSACTION_END, System.currentTimeMillis());
        i.putExtra(ResultActivity.EXTRA_RESULT_STATUS, "SUCCESS");
        i.putExtra(ResultActivity.EXTRA_RESULT_REASON, "Transaction Approved");
        startActivity(i);
        finish();
    }
}
