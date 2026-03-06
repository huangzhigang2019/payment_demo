package com.pax.demo.modules.scanner;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pax.dal.entity.EScannerType;
import com.pax.demo.R;
import com.pax.demo.base.BaseFragment;

public class BarcodeParamFragment extends BaseFragment implements OnClickListener {

    private EScannerType scannerType = EScannerType.REAR;
    private Button openBt, closeBt;
    private EditText barcode;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scanner_barcode_param, container, false);

        scannerType = EScannerType.valueOf(getArguments().getString("scannerType"));

        openBt = (Button) view.findViewById(R.id.fragment_scanner_open);
        closeBt = (Button) view.findViewById(R.id.fragment_scanner_close);

        barcode = (EditText) view.findViewById(R.id.fragment_scanner_barcode);


        if (scannerType == EScannerType.EXTERNAL) {
            openBt.setVisibility(View.GONE);
            closeBt.setVisibility(View.GONE);
        } else {
            openBt.setOnClickListener(this);
            closeBt.setOnClickListener(this);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ScannerTester.getInstance(EScannerType.FRONT).close();
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_scanner_open:
                ScannerTester.getInstance(EScannerType.REAR).setBarcodeParam(barcode.getText().toString(),true);
                break;
            case R.id.fragment_scanner_close:
                ScannerTester.getInstance(EScannerType.REAR).setBarcodeParam(barcode.getText().toString(),false);
                break;
            default:
                break;
        }
    }
}
