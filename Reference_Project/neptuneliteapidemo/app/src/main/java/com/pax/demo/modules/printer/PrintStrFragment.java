/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology (Shenzhen) Co., Ltd. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology (Shenzhen) Co., Ltd. and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2017-2023 PAX Computer Technology (Shenzhen) Co., Ltd. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                         Author                        Action
 * 2017/12/20                   PAX                     Create/Add/Modify/Delete
 * ===========================================================================================
 */

package com.pax.demo.modules.printer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.pax.dal.entity.EFontTypeAscii;
import com.pax.dal.entity.EFontTypeExtCode;
import com.pax.demo.R;

public class PrintStrFragment extends Fragment implements OnClickListener {

    private Spinner asciiSpinner, extCodeSpinner;
    private Button addBitMapBt, doubleWidthBt, doubleHeightBt, invertBt, getStatusBt, getDotLineBt, startBt
            ,fragment_print_color_bitmap_with_mono_threshold_bt
            ,fragment_print_color_bitmap_bt
            ,fragment_set_color_gray_bt;
    private EditText wordSpaceEt, lineSpaceEt, leftIndentEt, setGrayEt, stepEt, printStrEt
            ,fragment_blackLevel_et,fragment_colorLevel_et;
    private TextView statusTv, getDotLineTv;

    private boolean containBitmap = false, doubleWidth = false, doubleHeight = false, invert = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_printer_printstr, container, false);
        initView(view);

        ArrayAdapter<EFontTypeAscii> asciiAdapter = new ArrayAdapter<EFontTypeAscii>(getActivity(),
                android.R.layout.simple_spinner_item, EFontTypeAscii.values());
        asciiSpinner.setAdapter(asciiAdapter);
        ArrayAdapter<EFontTypeExtCode> extCodeAdapter = new ArrayAdapter<EFontTypeExtCode>(getActivity(),
                android.R.layout.simple_spinner_item, EFontTypeExtCode.values());
        extCodeSpinner.setAdapter(extCodeAdapter);

        return view;
    }

    private void initView(View view) {
        asciiSpinner = (Spinner) view.findViewById(R.id.fragment_printer_spinner_ascii);
        extCodeSpinner = (Spinner) view.findViewById(R.id.fragment_printer_spinner_extcode);

        addBitMapBt = (Button) view.findViewById(R.id.fragment_printer_bitmap);
        doubleWidthBt = (Button) view.findViewById(R.id.fragment_printer_doublewidth_bt);
        doubleHeightBt = (Button) view.findViewById(R.id.fragment_printer_doubleheight_bt);
        invertBt = (Button) view.findViewById(R.id.fragment_printer_invert_bt);
        getStatusBt = (Button) view.findViewById(R.id.fragment_printer_status_bt);
        getDotLineBt = (Button) view.findViewById(R.id.fragment_printer_getdotline_bt);
        startBt = (Button) view.findViewById(R.id.fragment_printer_start);

        fragment_blackLevel_et = (EditText) view.findViewById(R.id.fragment_blackLevel_et);
        fragment_colorLevel_et = (EditText) view.findViewById(R.id.fragment_colorLevel_et);

        fragment_print_color_bitmap_with_mono_threshold_bt = (Button) view.findViewById(R.id.fragment_print_color_bitmap_with_mono_threshold_bt);
        fragment_print_color_bitmap_bt = (Button) view.findViewById(R.id.fragment_print_color_bitmap_bt);
        fragment_set_color_gray_bt = (Button) view.findViewById(R.id.fragment_set_color_gray_bt);

        addBitMapBt.setOnClickListener(this);
        doubleWidthBt.setOnClickListener(this);
        doubleHeightBt.setOnClickListener(this);
        invertBt.setOnClickListener(this);
        getStatusBt.setOnClickListener(this);
        getDotLineBt.setOnClickListener(this);
        startBt.setOnClickListener(this);

        fragment_print_color_bitmap_with_mono_threshold_bt.setOnClickListener(this);
        fragment_print_color_bitmap_bt.setOnClickListener(this);
        fragment_set_color_gray_bt.setOnClickListener(this);

        wordSpaceEt = (EditText) view.findViewById(R.id.fragment_printer_spaceset_word_et);
        lineSpaceEt = (EditText) view.findViewById(R.id.fragment_printer_spaceset_line_et);
        leftIndentEt = (EditText) view.findViewById(R.id.fragment_printer_leftindent_et);
        setGrayEt = (EditText) view.findViewById(R.id.fragment_printer_setgray_et);
        stepEt = (EditText) view.findViewById(R.id.fragment_printer_step_et);
        printStrEt = (EditText) view.findViewById(R.id.fragment_printer_prnstr_et);
        statusTv = (TextView) view.findViewById(R.id.fragment_printer_status_tv);
        getDotLineTv = (TextView) view.findViewById(R.id.fragment_printer_getdotline_tv);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fragment_printer_bitmap:
                if (containBitmap) {
                    addBitMapBt.setText(getResources().getString(R.string.printer_bitmap_no));
                    containBitmap = false;
                } else {
                    addBitMapBt.setText(getResources().getString(R.string.printer_bitmap_contain));
                    containBitmap = true;
                }
                break;
            case R.id.fragment_printer_doublewidth_bt:
                if (doubleWidth) {
                    doubleWidthBt.setText(getResources().getString(R.string.printer_normal_width));
                    doubleWidth = false;
                } else {
                    doubleWidthBt.setText(getResources().getString(R.string.printer_double_width));
                    doubleWidth = true;
                }
                break;
            case R.id.fragment_printer_doubleheight_bt:
                if (doubleHeight) {
                    doubleHeightBt.setText(getResources().getString(R.string.printer_normal_height));
                    doubleHeight = false;
                } else {
                    doubleHeightBt.setText(getResources().getString(R.string.printer_double_height));
                    doubleHeight = true;
                }
                break;
            case R.id.fragment_printer_invert_bt:
                if (invert) {
                    invertBt.setText(getResources().getString(R.string.printer_normal));
                    invert = false;
                } else {
                    invertBt.setText(getResources().getString(R.string.printer_invert));
                    invert = true;
                }
                break;
            case R.id.fragment_printer_status_bt:
                statusTv.setText(PrinterTester.getInstance().getStatus());
                break;
            case R.id.fragment_printer_getdotline_bt:
                getDotLineTv.setText(PrinterTester.getInstance().getDotLine() + "");
                break;
            case R.id.fragment_printer_start:
                new Thread(new Runnable() {
                    public void run() {
                        PrinterTester.getInstance().init();
                        PrinterTester.getInstance().fontSet((EFontTypeAscii) asciiSpinner.getSelectedItem(),
                                (EFontTypeExtCode) extCodeSpinner.getSelectedItem());
                        Options options = new Options();
                        options.inScaled = false;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rect,options);
//                        Log.i("Test", "width:"+bitmap.getWidth()+"height:"+bitmap.getHeight());
                        if (containBitmap) {
                            PrinterTester.getInstance().printBitmap(bitmap);
                        }
                        PrinterTester.getInstance().spaceSet(Byte.parseByte(wordSpaceEt.getText().toString()),
                                Byte.parseByte(lineSpaceEt.getText().toString()));
                        PrinterTester.getInstance().leftIndents(Short.parseShort(leftIndentEt.getText().toString()));
                        PrinterTester.getInstance().setGray(Integer.parseInt(setGrayEt.getText().toString()));
                        if (doubleWidth) {
                            PrinterTester.getInstance().setDoubleWidth(doubleWidth, doubleWidth);
                        }
                        if (doubleHeight) {
                            PrinterTester.getInstance().setDoubleHeight(doubleHeight, doubleHeight);
                        }
                        PrinterTester.getInstance().setInvert(invert);
                        String str = printStrEt.getText().toString();
                        if (str != null && str.length() > 0)
                            PrinterTester.getInstance().printStr(str, null);
                        PrinterTester.getInstance().step(Integer.parseInt(stepEt.getText().toString().trim()));

                        getDotLineTv.post(new Runnable() {
                            public void run() {
                                getDotLineTv.setText(PrinterTester.getInstance().getDotLine() + "");
                            }
                        });
                        final String status = PrinterTester.getInstance().start();
                        statusTv.post(new Runnable() {
                            public void run() {
                                statusTv.setText(status);
                            }
                        });
                    }
                }).start();

                break;
            case R.id.fragment_print_color_bitmap_with_mono_threshold_bt:
                new Thread(new Runnable() {
                    public void run() {
                        PrinterTester.getInstance().init();
                        PrinterTester.getInstance().fontSet((EFontTypeAscii) asciiSpinner.getSelectedItem(),
                                (EFontTypeExtCode) extCodeSpinner.getSelectedItem());
                        Options options = new Options();
                        options.inScaled = false;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rect,options);
                        PrinterTester.getInstance().printColorBitmapWithMonoThreshold(bitmap,100);
                    }
                }).start();
                break;
            case R.id.fragment_print_color_bitmap_bt:
                new Thread(new Runnable() {
                    public void run() {
                        PrinterTester.getInstance().init();
                        PrinterTester.getInstance().fontSet((EFontTypeAscii) asciiSpinner.getSelectedItem(),
                                (EFontTypeExtCode) extCodeSpinner.getSelectedItem());
                        Options options = new Options();
                        options.inScaled = false;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rect,options);
                        PrinterTester.getInstance().printColorBitmap(bitmap);
                    }
                }).start();
                break;
            case R.id.fragment_set_color_gray_bt:
                int black = 100;
                int color = 100;
                try {
                    black = Integer.parseInt(fragment_blackLevel_et.getText().toString());
                    color = Integer.parseInt(fragment_colorLevel_et.getText().toString());
                }catch (Exception e){
                }
                PrinterTester.getInstance().setColorGray(black,color);
                break;
            default:
                break;
        }
    }
}
