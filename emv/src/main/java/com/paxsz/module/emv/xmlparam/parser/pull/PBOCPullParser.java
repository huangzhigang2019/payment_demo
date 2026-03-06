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
 *   20210108  	           John Cai                 Create
 * ===========================================================================================
 */

package com.paxsz.module.emv.xmlparam.parser.pull;

import android.support.annotation.NonNull;
import android.util.Xml;

import com.pax.commonlib.utils.LogUtils;
import com.pax.commonlib.utils.convert.ConvertHelper;
import com.pax.commonlib.utils.convert.IConvert;
import com.paxsz.module.emv.xmlparam.entity.clss.PBOCAid;
import com.paxsz.module.emv.xmlparam.parser.IXmlParser;
import com.paxsz.module.emv.xmlparam.parser.ParseException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.paxsz.module.emv.xmlparam.parser.ParseException.CODE_FILE_OPEN_ERR;
import static com.paxsz.module.emv.xmlparam.parser.ParseException.CODE_NODE_NOT_FOUND;

public class PBOCPullParser implements IXmlParser<List<PBOCAid>> {

    private static final String TAG = "PBOCPullParser";
    private IConvert convert;

    @Override
    public List<PBOCAid> parse(@NonNull InputStream inputStream) throws ParseException {
        XmlPullParser parser = Xml.newPullParser();
        List<PBOCAid> aids = null;

        convert = ConvertHelper.getConvert();

        int eventType;
        try {
            parser.setInput(inputStream, StandardCharsets.UTF_8.displayName());
            eventType = parser.getEventType();
        } catch (XmlPullParserException e) {
            LogUtils.e(TAG, e);
            throw new ParseException(CODE_FILE_OPEN_ERR, "Set input stream fail");
        }

        PBOCAid aid = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (parser.getName().equals("AID")) {
                        aid = new PBOCAid();
                    } else if (parser.getName().equals("AIDLIST")) {
                        aids = new ArrayList<>();
                    } else if (aid != null) {
                        parseAid(aid, parser.getName(), parser);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if (parser.getName().equals("AID") && aids != null) {
                        aids.add(aid);
                        aid = null;
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
        return aids;
    }

    private void parseAid(PBOCAid aid, String tag, XmlPullParser parser) throws ParseException {
        String text = "";

        try {
            text = parser.nextText();
        } catch (IOException | XmlPullParserException e) {
            LogUtils.e(TAG, e);
        }

        switch (tag) {
            case "AppName":
                aid.setAppName(text);
                break;
            case "Aid":
                aid.setAid(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_LEFT));
                break;
            case "SelFlag":
                aid.setSelFlag(Byte.parseByte(text));
                break;
            case "TargetPer":
                aid.setTargetPer(Byte.parseByte(text));
                break;
            case "MaxTargetPer":
                aid.setMaxTargetPer(Byte.parseByte(text));
                break;
            case "FloorLimit":
                aid.setFloorLimit(Long.parseLong(text));
                break;
            case "FloorLimitCheckFlg":
                aid.setFloorLimitCheckFlg(Byte.parseByte(text));
                break;
            case "RdClssTxnLmt":
                aid.setRdClssTxnLmt(Long.parseLong(text));
                break;
            case "RdClssTxnLmtFlg":
                aid.setRdClssTxnLmtFlg(Byte.parseByte(text));
                break;
            case "RdClssFLmt":
                aid.setRdClssFLmt(Long.parseLong(text));
                break;
            case "RdClssFLmtFlg":
                aid.setRdClssFLmtFlg(Byte.parseByte(text));
                break;
            case "RdCVMLmt":
                aid.setRdCVMLmt(Long.parseLong(text));
                break;
            case "RdCVMLmtFlg":
                aid.setRdCVMLmtFlg(Byte.parseByte(text));
                break;
            case "RandTransSel":
                aid.setRandTransSel(Boolean.parseBoolean(text));
                break;
            case "VelocityCheck":
                aid.setVelocityCheck(Boolean.parseBoolean(text));
                break;
            case "Threshold":
                aid.setThreshold(Integer.parseInt(text));
                break;
            case "TacDenial":
                aid.setTacDenial(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TacOnline":
                aid.setTacOnline(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TacDefault":
                aid.setTacDefault(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "AcquirerId":
                aid.setAcquirerId(text.getBytes());
                break;
            case "DDOL":
                aid.setdDOL(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "TDOL":
                aid.settDOL(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "ReaderTTQ":
                aid.setReaderTTQ(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            case "Version":
                aid.setVersion(convert.strToBcd(text, IConvert.EPaddingPosition.PADDING_RIGHT));
                break;
            default:
                throw new ParseException(CODE_NODE_NOT_FOUND, "node " + text + " not exist");
        }
    }
}
