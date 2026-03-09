package com.payment.demo.trans;

/**
 * T007: Transaction entity (data-model).
 * 金额、开始/结束时间、介质类型、结果状态、失败原因摘要、参考号
 */
public class Transaction {
    private long amountCent;           // 金额（分）
    private long tipAmountCent;        // 小费金额（分）
    private long startTimeMs;
    private long endTimeMs;
    private int readType;               // 介质类型，与 EReaderType 对应
    private TransResultStatus resultStatus;
    private String failReason;
    private String referenceNo;
    private String maskedCardNo;        // 脱敏卡号
    private byte[] signatureData;       // 签名位图字节（JPEG 压缩）

    public long getAmountCent() { return amountCent; }
    public void setAmountCent(long amountCent) { this.amountCent = amountCent; }

    public long getTipAmountCent() { return tipAmountCent; }
    public void setTipAmountCent(long tipAmountCent) { this.tipAmountCent = tipAmountCent; }

    public long getStartTimeMs() { return startTimeMs; }
    public void setStartTimeMs(long startTimeMs) { this.startTimeMs = startTimeMs; }

    public long getEndTimeMs() { return endTimeMs; }
    public void setEndTimeMs(long endTimeMs) { this.endTimeMs = endTimeMs; }

    public int getReadType() { return readType; }
    public void setReadType(int readType) { this.readType = readType; }

    public TransResultStatus getResultStatus() { return resultStatus; }
    public void setResultStatus(TransResultStatus resultStatus) { this.resultStatus = resultStatus; }

    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }

    public String getReferenceNo() { return referenceNo; }
    public void setReferenceNo(String referenceNo) { this.referenceNo = referenceNo; }

    public String getMaskedCardNo() { return maskedCardNo; }
    public void setMaskedCardNo(String maskedCardNo) { this.maskedCardNo = maskedCardNo; }

    public byte[] getSignatureData() { return signatureData; }
    public void setSignatureData(byte[] signatureData) { this.signatureData = signatureData; }
}
