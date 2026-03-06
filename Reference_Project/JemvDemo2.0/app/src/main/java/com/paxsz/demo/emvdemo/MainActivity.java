/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2020-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date	                 Author	                Action
 * 2020/5/12  	         Qinny Zhou           	Create/Add/Modify/Delete
 * ===========================================================================================
 */
package com.paxsz.demo.emvdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.commonlib.utils.LogUtils;
import com.paxsz.demo.emvdemo.trans.TransProcessActivity;
import com.paxsz.demo.emvdemo.util.CurrencyConverter;
import com.paxsz.demo.emvdemo.view.keyboard.DigitKeyboard;
import com.paxsz.demo.emvdemo.view.keyboard.EditorActionListener;
import com.paxsz.demo.emvdemo.view.keyboard.EnterAmountTextWatcher;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";
    private TextView transTypeText;
    private EditText transAmtEdit;
    private EditText otherAmtEdit;
    private Spinner typeSpinner;
    private RelativeLayout tranAmtLayout;
    private LinearLayout settingLayout;
    private DigitKeyboard mDigitKeyboard;
    private String[] transTypeArr;
    private int currentIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.i(TAG, "onResume");
        clearView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtils.i(TAG, "onStop");
        clearView();
    }

    private void initView() {
        mDigitKeyboard = new DigitKeyboard(MainActivity.this);
        transTypeText = findViewById(R.id.tv_trans_type);
        transAmtEdit = findViewById(R.id.edit_trans_amt);
        otherAmtEdit = findViewById(R.id.edit_other_amt);
        findViewById(R.id.layout_trans_type).setOnClickListener(this);
        tranAmtLayout = findViewById(R.id.layout_trans_amt);
        tranAmtLayout.setOnClickListener(this);
        findViewById(R.id.layout_other_amt).setOnClickListener(this);
        resetView(transAmtEdit);
        transTypeArr = getResources().getStringArray(R.array.tran_type);
        ArrayAdapter<String> transTypeAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_item, transTypeArr);
        typeSpinner = findViewById(R.id.spinner_type);
        typeSpinner.setAdapter(transTypeAdapter);
        transTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setSelection(0);
        transTypeText.setText(transTypeArr[0]);
        currentIndex = 0;
        findViewById(R.id.tv_type_label).setOnClickListener(this);
        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currentIndex = i;

                if (i == 2) {//cashback
                    findViewById(R.id.layout_other_amt).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.layout_other_amt).setVisibility(View.INVISIBLE);
                }
                if (i == 3) {//inquiry
                    tranAmtLayout.setVisibility(View.INVISIBLE);
                } else {
                    tranAmtLayout.setVisibility(View.VISIBLE);
                }
                transTypeText.setText(transTypeArr[i]);
                transAmtEdit.setText("");
                otherAmtEdit.setText("");


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // onNothingSelected
            }
        });

        EditorActionListener editorActionListener = new EditorActionListener() {
            @Override
            protected void onKeyOk(TextView view) {
                LogUtils.i(TAG, "onKeyOk: " + view.getId() + ",amount str:" + CurrencyConverter.parse(transAmtEdit.getText().toString()));
                LogUtils.i(TAG, "trans amount:" + transAmtEdit.getText() + ",other amount:" + otherAmtEdit.getText().toString() + ",trans type:" + transTypeText.getText().toString());
                Intent toTransProcessIntent = new Intent(MainActivity.this, TransProcessActivity.class);
                toTransProcessIntent.putExtra(TransProcessActivity.EXTRA_TRANS_TYPE, getCurrTxnTypeCode());
                toTransProcessIntent.putExtra(TransProcessActivity.EXTRA_TRANS_AMOUNT, CurrencyConverter.parse(transAmtEdit.getText().toString()));
                toTransProcessIntent.putExtra(TransProcessActivity.EXTRA_OTHER_AMOUNT, CurrencyConverter.parse(otherAmtEdit.getText().toString()));
                MainActivity.this.startActivity(toTransProcessIntent);

            }

        };
        EnterAmountTextWatcher enterAmountTextWatcher = new EnterAmountTextWatcher();
        transAmtEdit.setOnEditorActionListener(editorActionListener);
        transAmtEdit.addTextChangedListener(enterAmountTextWatcher);

        otherAmtEdit.setOnEditorActionListener(editorActionListener);
        otherAmtEdit.addTextChangedListener(new EnterAmountTextWatcher());

        settingLayout = findViewById(R.id.layout_setting);
        settingLayout.setOnClickListener(this);
    }

    private byte getCurrTxnTypeCode() {
        byte typeCode = 0x00;
        switch (currentIndex) {
            case 0:
                typeCode = 0x00;//sale
                break;
            case 1:
                typeCode = 0x20;//refund
                break;
            case 2:
                typeCode = 0x09;//cashback
                break;
            case 3:
                typeCode = 0x30;//inquiry
                break;
        }
        return typeCode;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_trans_amt:
                resetView(transAmtEdit);
                break;
            case R.id.layout_other_amt:
                resetView(otherAmtEdit);
                break;
            case R.id.layout_setting:
                Intent toSettingPage = new Intent(MainActivity.this, SettingsActivity.class);
                ActivityOptionsCompat optionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this, settingLayout, "transitionPage");
                startActivity(toSettingPage, optionsCompat.toBundle());
                break;
            case R.id.tv_type_label:
                typeSpinner.performClick();
                break;
            default:
                break;
        }
    }

    private void clearView() {
        typeSpinner.setSelection(0);
        transAmtEdit.setText("");
        otherAmtEdit.setText("");
        if (tranAmtLayout.getVisibility() == View.INVISIBLE) {
            tranAmtLayout.setVisibility(View.VISIBLE);
        }
        resetView(transAmtEdit);
    }

    private void resetView(EditText editText) {
        transTypeText.setTextColor(getResources().getColor(R.color.black_overlay));
        transAmtEdit.setTextColor(getResources().getColor(R.color.black_overlay));
        otherAmtEdit.setTextColor(getResources().getColor(R.color.black_overlay));
        editText.setTextColor(getResources().getColor(R.color.colorAccent));
        mDigitKeyboard.attachTo(editText);

    }
}
