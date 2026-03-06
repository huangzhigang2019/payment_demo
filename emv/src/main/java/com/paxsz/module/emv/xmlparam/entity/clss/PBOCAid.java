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

package com.paxsz.module.emv.xmlparam.entity.clss;

public class PBOCAid {
    private String appName;
    private byte[] aid = new byte[0];
    private byte selFlag;
    private byte targetPer;
    private byte maxTargetPer;
    private long floorLimit;
    private byte floorLimitCheckFlg;
    private long rdClssTxnLmt;
    private byte rdClssTxnLmtFlg;
    private long rdClssFLmt;
    private byte rdClssFLmtFlg;
    private long rdCVMLmt;
    private byte rdCVMLmtFlg;
    private boolean randTransSel;
    private boolean velocityCheck;
    private int threshold;
    private byte[] tacDenial = new byte[0];
    private byte[] tacOnline = new byte[0];
    private byte[] tacDefault = new byte[0];
    private byte[] acquirerId = new byte[0];
    private byte[] dDOL = new byte[0];
    private byte[] tDOL = new byte[0];
    private byte[] readerTTQ = new byte[0];
    private byte[] version = new byte[0];

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public byte[] getAid() {
        return aid;
    }

    public void setAid(byte[] aid) {
        this.aid = aid;
    }

    public byte getSelFlag() {
        return selFlag;
    }

    public void setSelFlag(byte selFlag) {
        this.selFlag = selFlag;
    }

    public byte getTargetPer() {
        return targetPer;
    }

    public void setTargetPer(byte targetPer) {
        this.targetPer = targetPer;
    }

    public byte getMaxTargetPer() {
        return maxTargetPer;
    }

    public void setMaxTargetPer(byte maxTargetPer) {
        this.maxTargetPer = maxTargetPer;
    }

    public long getFloorLimit() {
        return floorLimit;
    }

    public void setFloorLimit(long floorLimit) {
        this.floorLimit = floorLimit;
    }

    public byte isFloorLimitCheckFlg() {
        return floorLimitCheckFlg;
    }

    public void setFloorLimitCheckFlg(byte floorLimitCheckFlg) {
        this.floorLimitCheckFlg = floorLimitCheckFlg;
    }

    public long getRdClssTxnLmt() {
        return rdClssTxnLmt;
    }

    public void setRdClssTxnLmt(long rdClssTxnLmt) {
        this.rdClssTxnLmt = rdClssTxnLmt;
    }

    public byte isRdClssTxnLmtFlg() {
        return rdClssTxnLmtFlg;
    }

    public void setRdClssTxnLmtFlg(byte rdClssTxnLmtFlg) {
        this.rdClssTxnLmtFlg = rdClssTxnLmtFlg;
    }

    public long getRdClssFLmt() {
        return rdClssFLmt;
    }

    public void setRdClssFLmt(long rdClssFLmt) {
        this.rdClssFLmt = rdClssFLmt;
    }

    public byte isRdClssFLmtFlg() {
        return rdClssFLmtFlg;
    }

    public void setRdClssFLmtFlg(byte rdClssFLmtFlg) {
        this.rdClssFLmtFlg = rdClssFLmtFlg;
    }

    public long getRdCVMLmt() {
        return rdCVMLmt;
    }

    public void setRdCVMLmt(long rdCVMLmt) {
        this.rdCVMLmt = rdCVMLmt;
    }

    public byte isRdCVMLmtFlg() {
        return rdCVMLmtFlg;
    }

    public void setRdCVMLmtFlg(byte rdCVMLmtFlg) {
        this.rdCVMLmtFlg = rdCVMLmtFlg;
    }

    public boolean isRandTransSel() {
        return randTransSel;
    }

    public void setRandTransSel(boolean randTransSel) {
        this.randTransSel = randTransSel;
    }

    public boolean isVelocityCheck() {
        return velocityCheck;
    }

    public void setVelocityCheck(boolean velocityCheck) {
        this.velocityCheck = velocityCheck;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public byte[] getTacDenial() {
        return tacDenial;
    }

    public void setTacDenial(byte[] tacDenial) {
        this.tacDenial = tacDenial;
    }

    public byte[] getTacOnline() {
        return tacOnline;
    }

    public void setTacOnline(byte[] tacOnline) {
        this.tacOnline = tacOnline;
    }

    public byte[] getTacDefault() {
        return tacDefault;
    }

    public void setTacDefault(byte[] tacDefault) {
        this.tacDefault = tacDefault;
    }

    public byte[] getAcquirerId() {
        return acquirerId;
    }

    public void setAcquirerId(byte[] acquirerId) {
        this.acquirerId = acquirerId;
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

    public byte[] getReaderTTQ() {
        return readerTTQ;
    }

    public void setReaderTTQ(byte[] readerTTQ) {
        this.readerTTQ = readerTTQ;
    }

    public byte[] getVersion() {
        return version;
    }

    public void setVersion(byte[] version) {
        this.version = version;
    }
}
