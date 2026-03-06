/*
 * ===========================================================================================
 *     COPYRIGHT
 *           PAX Computer Technology(Shenzhen) CO., LTD PROPRIETARY INFORMATION
 *    This software is supplied under the terms of a license agreement or nondisclosure
 *    agreement with PAX Computer Technology(Shenzhen) CO., LTD and may not be copied or
 *    disclosed except in accordance with the terms in that agreement.
 *      Copyright (C) 2020-? PAX Computer Technology(Shenzhen) CO., LTD All rights reserved.
 *  Description: // Detail description about the function of this module,
 *              // interfaces with the other modules, and dependencies.
 *  Revision History:
 *  Date                  Author	                 Action
 *   20201207  	           John Cai                 Create
 * ===========================================================================================
 */

package com.paxsz.module.emv.xmlparam.parser.pull;

import android.support.annotation.NonNull;
import android.util.Xml;

import com.pax.commonlib.utils.LogUtils;
import com.pax.commonlib.utils.convert.ConvertHelper;
import com.pax.commonlib.utils.convert.IConvert;
import com.paxsz.module.emv.xmlparam.entity.clss.AmexAid;
import com.paxsz.module.emv.xmlparam.entity.clss.AmexParam;
import com.paxsz.module.emv.xmlparam.entity.clss.ExPayDRL;
import com.paxsz.module.emv.xmlparam.parser.IXmlParser;
import com.paxsz.module.emv.xmlparam.parser.ParseException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.paxsz.module.emv.xmlparam.parser.ParseException.CODE_FILE_OPEN_ERR;
import static com.paxsz.module.emv.xmlparam.parser.ParseException.CODE_NODE_NOT_FOUND;

public class AmexPullParser implements IXmlParser<AmexParam> {

    private static final String TAG = "AmexPullParser";
    private IConvert convert;

    @Override
    public AmexParam parse(@NonNull InputStream inputStream) throws ParseException {
        XmlPullParser parser = Xml.newPullParser();
        AmexParam amexParam;
        convert = ConvertHelper.getConvert();

        int eventType;
        try {
            parser.setInput(inputStream, StandardCharsets.UTF_8.displayName());
            eventType = parser.getEventType();
            amexParam = new AmexParam();
        } catch (XmlPullParserException e) {
            LogUtils.e(TAG, e);
            throw new ParseException(CODE_FILE_OPEN_ERR, "Set input stream fail");
        }

        AmexAid aid = null;
        ExPayDRL drl = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String tag = parser.getName();
                    if (tag.equals("AID")) {
                        aid = new AmexAid();
                    } else if (tag.equals("EXPRESSPAYDRL")) {
                        drl = new ExPayDRL();
                    } else if (aid != null) {
                        handleAidText(tag, parser, aid);
                    } else if (drl != null) {
                        handleDrlText(tag, parser, drl);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tag = parser.getName();
                    if (tag.equals("AID")) {
                        amexParam.addAid(aid);
                        aid = null;
                    } else if (tag.equals("EXPRESSPAYDRL")) {
                        amexParam.addDrl(drl);
                        drl = null;
                    }
                    break;
                default:
            }
            try {
                eventType = parser.next();
            } catch (IOException | XmlPullParserException e) {
                LogUtils.e(TAG, e);
            }
        }
        return amexParam;
    }

    private void handleAidText(String tag, XmlPullParser parser, AmexAid aid) throws ParseException {
        String text = "";

        try {
            text = parser.nextText();
        } catch (IOException | XmlPullParserException e) {
            LogUtils.e(TAG, e);
        }
        switch (tag) {
            case "PartialAIDSelection":
                aid.setPartialAIDSelection(Byte.parseByte(text));
                break;
            case "ApplicationID":
                aid.setApplicationID(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "IfUseLocalAIDName":
                aid.setUseLocalAIDName(text.equals("1"));
                break;
            case "LocalAIDName":
                aid.setLocalAIDName(text);
                break;
            case "AcquirerId":
                aid.setAcquirerId(text.getBytes());
                break;
            case "TerminalAIDVersion":
                aid.setTermAIDVer(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TACDenial":
                aid.settACDenial(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TACOnline":
                aid.settACOnline(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TACDefault":
                aid.settACDefault(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "DDOL":
                aid.setdDOL(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TDOL":
                aid.settDOL(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "ExpresspayReaderCapabilities":
                aid.setExPayRdCap(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT)[0]);
                break;
            case "ContactlessCVMLimit":
                aid.setClssCVMLimit(Long.parseLong(text));
                break;
            case "ContactlessTransactionLimit":
                aid.setClssTransLimit(Long.parseLong(text));
                break;
            case "ContactlessFloorLimit":
                aid.setClssFloorLimit(Long.parseLong(text));
                break;
            case "ContactlessFloorLimitCheck":
                aid.setClssFloorLimitCheck(Byte.parseByte(text));
                break;
            case "TerminalSupportOptimizationModeTransaction":
                aid.setSupportOptTrans(Byte.parseByte(text));
                break;
            case "UnpredictableNumberRange":
                aid.setUnRange(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_LEFT));
                break;
            case "TerminalTransactionCapability":
                aid.setTermTransCap(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "DelayAuthorizationSupport":
                aid.setDelayAuthSupport(Byte.parseByte(text));
                break;
            case "TerminalType":
                aid.setTerminalType(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT)[0]);
                break;
            case "TerminalCapability":
                aid.setTermCap(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TerminalAdditionalCapability":
                aid.setTermAddCap(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            default:
                throw new ParseException(CODE_NODE_NOT_FOUND, "node " + text + " not exist");
        }
    }

    private void handleDrlText(String tag, XmlPullParser parser, ExPayDRL drl) throws ParseException {
        String text = "";

        try {
            text = parser.nextText();
        } catch (XmlPullParserException | IOException e) {
            LogUtils.e(TAG, e);
        }

        switch (tag) {
            case "DynamicLimitSet":
                drl.setDynamicLimitSet(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_LEFT)[0]);
                break;
            case "RdClssTxnLmt":
                String num = convert.getPaddedNumber(Long.parseLong(text), 12);
                drl.setRdClssTxnLmt(convert.strToBcd(num, IConvert.EPaddingPosition.PADDING_LEFT));
                break;
            case "RdCVMLmt":
                num = convert.getPaddedNumber(Long.parseLong(text), 12);
                drl.setRdCVMLmt(convert.strToBcd(num, IConvert.EPaddingPosition.PADDING_LEFT));
                break;
            case "RdClssFloorLmt":
                num = convert.getPaddedNumber(Long.parseLong(text), 12);
                drl.setRdClssFloorLmt(convert.strToBcd(num, IConvert.EPaddingPosition.PADDING_LEFT));
                break;
            case "TermFloorLmt":
                num = convert.getPaddedNumber(Long.parseLong(text), 12);
                drl.setTermFloorLmt(convert.strToBcd(num, IConvert.EPaddingPosition.PADDING_LEFT));
                break;
            case "ProgramId":
                drl.setProgramId(text.getBytes());
                break;
            case "ProgramIdLen":
                drl.setProgramIdLen(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_LEFT)[0]);
                break;
            case "RdClssFloorLmtFlg":
                drl.setRdClssFloorLmtFlg(Byte.parseByte(text));
                break;
            case "RdClssTxnLmtFlg":
                drl.setRdClssTxnLmtFlg(Byte.parseByte(text));
                break;
            case "RdCVMLmtFlg":
                drl.setRdCVMLmtFlg(Byte.parseByte(text));
                break;
            case "TermFloorLmtFlg":
                drl.setTermFloorLmtFlg(Byte.parseByte(text));
                break;
            case "StatusCheckFlg":
                drl.setStatusCheckFlg(Byte.parseByte(text));
                break;
            case "AmtZeroNoAllowed":
                drl.setAmtZeroNoAllowed(Byte.parseByte(text));
                break;
            default:
                throw new ParseException(CODE_NODE_NOT_FOUND, "node " + text + " not exist");
        }
    }
}
