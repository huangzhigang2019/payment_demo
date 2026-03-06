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
 * Date                  Author	                 Action
 * 20200525  	         JackHuang               Create
 * ===========================================================================================
 */

package com.paxsz.module.emv.param;

import com.pax.commonlib.utils.LogUtils;
import com.pax.commonlib.utils.convert.ConvertHelper;
import com.paxsz.module.emv.xmlparam.entity.clss.AmexParam;
import com.paxsz.module.emv.xmlparam.entity.clss.PBOCAid;
import com.paxsz.module.emv.xmlparam.entity.clss.PayPassAid;
import com.paxsz.module.emv.xmlparam.entity.clss.PayWaveParam;
import com.paxsz.module.emv.xmlparam.entity.common.CapkParam;
import com.paxsz.module.emv.xmlparam.entity.common.Config;
import com.paxsz.module.emv.xmlparam.entity.contact.EmvAid;

import java.util.ArrayList;
import java.util.List;

public class EmvProcessParam {

    private EmvTransParam emvTransParam;
    private ArrayList<EmvAid> emvAidList;
    private ArrayList<PayPassAid> payPassAidList;
    private PayWaveParam payWaveParam;
    private AmexParam amexParam;
    private List<PBOCAid> pbocAidList;
    private CapkParam capkParam;
    private Config termConfig;

    private EmvProcessParam(Builder builder) {
        this.emvTransParam = builder.emvTransParam;
        this.capkParam = builder.capkParam;
        this.emvAidList = builder.emvAidList;
        this.payPassAidList = builder.payPassAidList;
        this.payWaveParam = builder.payWaveParam;
        this.termConfig = builder.termConfig;
        this.amexParam = builder.amexParam;
        this.pbocAidList = builder.pbocAidList;
        LogUtils.i("EmvProcessParam", "code: " + ConvertHelper.getConvert().bcdToStr(this.termConfig.getTerminalCountryCode()) +
                ",builder. code:" + ConvertHelper.getConvert().bcdToStr(this.termConfig.getTerminalCountryCode()));

    }

    public EmvTransParam getEmvTransParam() {
        return emvTransParam;
    }

    public CapkParam getCapkParam() {
        return capkParam;
    }

    public ArrayList<EmvAid> getEmvAidList() {
        return emvAidList;
    }

    public ArrayList<PayPassAid> getPayPassAidList() {
        return payPassAidList;
    }

    public PayWaveParam getPayWaveParam() {
        return payWaveParam;
    }

    public AmexParam getAmexParam() {
        return amexParam;
    }

    public List<PBOCAid> getPbocAidList() {
        return pbocAidList;
    }

    public Config getTermConfig() {
        LogUtils.i("getTermConfig", "code: " + ConvertHelper.getConvert().bcdToStr(termConfig.getTerminalCountryCode()));
        LogUtils.i("getTermConfig", "2 code: " + ConvertHelper.getConvert().bcdToStr(this.termConfig.getTerminalCountryCode()));
        return termConfig;
    }

    public final static class Builder {
        private EmvTransParam emvTransParam;

        private ArrayList<EmvAid> emvAidList;
        private ArrayList<PayPassAid> payPassAidList;
        private PayWaveParam payWaveParam;
        private AmexParam amexParam;
        private List<PBOCAid> pbocAidList;

        private CapkParam capkParam;
        private Config termConfig;

        public Builder(EmvTransParam transParam, Config termConfigParam, CapkParam capkParam) {
            this.emvTransParam = transParam;
            this.termConfig = termConfigParam;
            LogUtils.i("Builder", "code: " + ConvertHelper.getConvert().bcdToStr(termConfig.getTerminalCountryCode()));
            this.capkParam = capkParam;
        }

        public Builder setEmvAidList(ArrayList<EmvAid> emvAidList) {
            this.emvAidList = emvAidList;
            return this;
        }

        public Builder setPayPassAidList(ArrayList<PayPassAid> payPassAidList) {
            this.payPassAidList = payPassAidList;
            return this;
        }

        public Builder setPayWaveParam(PayWaveParam payWaveParam) {
            this.payWaveParam = payWaveParam;
            return this;
        }

        public Builder setAmexParam(AmexParam amexParam) {
            this.amexParam = amexParam;
            return this;
        }

        public Builder setPBOCParam(List<PBOCAid> pbocParam) {
            this.pbocAidList = pbocParam;
            return this;
        }

        public EmvProcessParam create() {
            return new EmvProcessParam(this);
        }

    }


}
