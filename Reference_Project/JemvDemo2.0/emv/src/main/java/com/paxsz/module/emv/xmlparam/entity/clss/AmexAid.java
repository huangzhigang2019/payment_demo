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

package com.paxsz.module.emv.xmlparam.entity.clss;

public class AmexAid {
    //Application selection flag (partial matching PART_MATCH(0), full matching FULL_MATCH(1) or Extended Selection SUPPORT_EXTEND(2))
    private byte partialAIDSelection;
    private byte[] applicationID = new byte[0];
    private boolean useLocalAIDName;
    private String localAIDName;

    //tag 9F09, Application Version Number
    private byte[] termAIDVer = new byte[0];
    private byte[] tACDenial = new byte[0];
    private byte[] tACOnline = new byte[0];
    private byte[] tACDefault = new byte[0];

    //tag 9F49 Discretionary Data Object List
    private byte[] dDOL = new byte[0];

    //tag 97 Transaction Certificate Data Object List
    private byte[] tDOL = new byte[0];

    //tag 9F6D A proprietary data element with bits 8, 7, and 4 only used to indicate a terminal’s capability to support Kernel 4 mag-stripe or EMV contactless.
    private byte exPayRdCap;

    //CVM Required floor limit
    private long clssCVMLimit;

    //contactless transaction limit
    private long clssTransLimit;

    //contactless Offline limit
    private long clssFloorLimit;

    //Whether to check the floor limit
    private byte clssFloorLimitCheck;

    //the terminal whether to support the optimization mode transaction flag
    private byte supportOptTrans;

    //Specifies the range in which the unpredictable number must be generated in
    private byte[] unRange = new byte[0];

    //tag 9F6E, Terminal Transaction Capabilities
    private byte[] termTransCap;

    //1: support Delayed Authorization; 0: not support Delayed Authorization
    private byte delayAuthSupport;

    // tag 9F35 Indicates terminal's communication capability, and its operational control.
    private byte terminalType;

    //tag: 9F33. Indicates the card data input, CVM, and security capabilities of the terminal.
    private byte[] termCap;

    //tag: 9F40. Indicates the data input and output capabilities of the terminal
    private byte[] termAddCap;

    private byte[] acquirerId;

    public byte getPartialAIDSelection() {
        return partialAIDSelection;
    }

    public void setPartialAIDSelection(byte partialAIDSelection) {
        this.partialAIDSelection = partialAIDSelection;
    }

    public byte[] getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(byte[] applicationID) {
        this.applicationID = applicationID;
    }

    public boolean isUseLocalAIDName() {
        return useLocalAIDName;
    }

    public void setUseLocalAIDName(boolean useLocalAIDName) {
        this.useLocalAIDName = useLocalAIDName;
    }

    public String getLocalAIDName() {
        return localAIDName;
    }

    public void setLocalAIDName(String localAIDName) {
        this.localAIDName = localAIDName;
    }

    public byte[] getTermAIDVer() {
        return termAIDVer;
    }

    public void setTermAIDVer(byte[] termAIDVer) {
        this.termAIDVer = termAIDVer;
    }

    public byte[] gettACDenial() {
        return tACDenial;
    }

    public void settACDenial(byte[] tACDenial) {
        this.tACDenial = tACDenial;
    }

    public byte[] gettACOnline() {
        return tACOnline;
    }

    public void settACOnline(byte[] tACOnline) {
        this.tACOnline = tACOnline;
    }

    public byte[] gettACDefault() {
        return tACDefault;
    }

    public void settACDefault(byte[] tACDefault) {
        this.tACDefault = tACDefault;
    }

    public byte[] getdDOL() {
        return dDOL;
    }

    public void setdDOL(byte[] dDOL) {
        this.dDOL = dDOL;
    }

    public byte[] gettDOL() {
        return tDOL;
    }

    public void settDOL(byte[] tDOL) {
        this.tDOL = tDOL;
    }

    public byte getExPayRdCap() {
        return exPayRdCap;
    }

    public void setExPayRdCap(byte exPayRdCap) {
        this.exPayRdCap = exPayRdCap;
    }

    public long getClssCVMLimit() {
        return clssCVMLimit;
    }

    public void setClssCVMLimit(long clssCVMLimit) {
        this.clssCVMLimit = clssCVMLimit;
    }

    public long getClssTransLimit() {
        return clssTransLimit;
    }

    public void setClssTransLimit(long clssTransLimit) {
        this.clssTransLimit = clssTransLimit;
    }

    public long getClssFloorLimit() {
        return clssFloorLimit;
    }

    public void setClssFloorLimit(long clssFloorLimit) {
        this.clssFloorLimit = clssFloorLimit;
    }

    public byte getClssFloorLimitCheck() {
        return clssFloorLimitCheck;
    }

    public void setClssFloorLimitCheck(byte clssFloorLimitCheck) {
        this.clssFloorLimitCheck = clssFloorLimitCheck;
    }

    public byte getSupportOptTrans() {
        return supportOptTrans;
    }

    public void setSupportOptTrans(byte supportOptTrans) {
        this.supportOptTrans = supportOptTrans;
    }

    public byte[] getUnRange() {
        return unRange;
    }

    public void setUnRange(byte[] unRange) {
        this.unRange = unRange;
    }

    public byte[] getTermTransCap() {
        return termTransCap;
    }

    public void setTermTransCap(byte[] termTransCap) {
        this.termTransCap = termTransCap;
    }

    public byte getDelayAuthSupport() {
        return delayAuthSupport;
    }

    public void setDelayAuthSupport(byte delayAuthSupport) {
        this.delayAuthSupport = delayAuthSupport;
    }

    public byte getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(byte terminalType) {
        this.terminalType = terminalType;
    }

    public byte[] getTermCap() {
        return termCap;
    }

    public void setTermCap(byte[] termCap) {
        this.termCap = termCap;
    }

    public byte[] getTermAddCap() {
        return termAddCap;
    }

    public void setTermAddCap(byte[] termAddCap) {
        this.termAddCap = termAddCap;
    }

    public byte[] getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(byte[] acquirerId) {
        this.acquirerId = acquirerId;
    }
}
