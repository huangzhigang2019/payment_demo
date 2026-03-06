package com.payment.demo.emv;

import android.content.Context;
import android.content.res.AssetManager;

import com.paxsz.module.emv.xmlparam.entity.clss.AmexParam;
import com.paxsz.module.emv.xmlparam.entity.clss.PayPassAid;
import com.paxsz.module.emv.xmlparam.entity.clss.PayWaveParam;
import com.paxsz.module.emv.xmlparam.entity.common.CapkParam;
import com.paxsz.module.emv.xmlparam.entity.common.Config;
import com.paxsz.module.emv.xmlparam.entity.contact.EmvAid;
import com.paxsz.module.emv.xmlparam.parser.pull.AmexPullParser;
import com.paxsz.module.emv.xmlparam.parser.pull.CapkParamPullParser;
import com.paxsz.module.emv.xmlparam.parser.pull.ConfigPullParser;
import com.paxsz.module.emv.xmlparam.parser.pull.EmvAidPullParser;
import com.paxsz.module.emv.xmlparam.parser.pull.PBOCPullParser;
import com.paxsz.module.emv.xmlparam.parser.pull.PayWavePullParser;
import com.paxsz.module.emv.xmlparam.parser.pull.PaypassPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * T015: EMV 参数加载 — 从 assets 读取 capk/config/contact/clss 等参数。
 */
public class EmvParamManager {
    private static final String FILE_CAPK = "capk.capk";
    private static final String FILE_CONFIG = "emv_clss.config";
    private static final String FILE_CONTACT = "emv_param.contact";
    private static final String FILE_PAYWAVE = "paywave_param.clss_wave";
    private static final String FILE_PAYPASS = "paypass_param.clss_mc";
    private static final String FILE_AMEX = "expresspay_param.clss_ae";
    private static final String FILE_PBOC = "pboc_param.clss_pboc";

    private static EmvParamManager instance;
    private CapkParam capkParam;
    private Config configParam;
    private ArrayList<EmvAid> emvAidList;
    private PayWaveParam payWaveParam;
    private ArrayList<PayPassAid> payPassAidList;
    private AmexParam amexParam;
    private List<com.paxsz.module.emv.xmlparam.entity.clss.PBOCAid> pbocParam;
    private boolean loaded;

    public static synchronized EmvParamManager getInstance(Context context) {
        if (instance == null) {
            instance = new EmvParamManager(context.getApplicationContext());
        }
        return instance;
    }

    private EmvParamManager(Context context) {
        loadParams(context.getAssets());
    }

    private void loadParams(AssetManager am) {
        try {
            capkParam = safeParse(am, FILE_CAPK, is -> new CapkParamPullParser().parse(is));
            configParam = safeParse(am, FILE_CONFIG, is -> new ConfigPullParser().parse(is));
            emvAidList = safeParse(am, FILE_CONTACT, is -> new EmvAidPullParser().parse(is));
            payWaveParam = safeParse(am, FILE_PAYWAVE, is -> new PayWavePullParser().parse(is));
            payPassAidList = safeParse(am, FILE_PAYPASS, is -> new PaypassPullParser().parse(is));
            amexParam = safeParse(am, FILE_AMEX, is -> new AmexPullParser().parse(is));
            try {
                InputStream is = am.open(FILE_PBOC);
                pbocParam = new PBOCPullParser().parse(is);
                is.close();
            } catch (Throwable ignored) {
                pbocParam = null;
            }
            loaded = capkParam != null && configParam != null && emvAidList != null
                    && payWaveParam != null && payPassAidList != null && amexParam != null;
        } catch (Throwable t) {
            loaded = false;
        }
    }

    private <T> T safeParse(AssetManager am, String name, Parser<T> p) {
        InputStream is = null;
        try {
            is = am.open(name);
            return p.parse(is);
        } catch (Throwable t) {
            return null;
        } finally {
            if (is != null) try { is.close(); } catch (java.io.IOException ignored) { }
        }
    }

    private interface Parser<T> {
        T parse(InputStream is) throws Exception;
    }

    public boolean isLoaded() { return loaded; }
    public CapkParam getCapkParam() { return capkParam; }
    public Config getConfigParam() { return configParam; }
    public ArrayList<EmvAid> getEmvAidList() { return emvAidList; }
    public PayWaveParam getPayWaveParam() { return payWaveParam; }
    public ArrayList<PayPassAid> getPayPassAidList() { return payPassAidList; }
    public AmexParam getAmexParam() { return amexParam; }
}
