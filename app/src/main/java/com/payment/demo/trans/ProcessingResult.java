package com.payment.demo.trans;

/**
 * T007: ProcessingResult entity (data-model).
 * 结果状态、结果码、展示摘要
 */
public class ProcessingResult {
    private TransResultStatus resultStatus;
    private int resultCode;
    private String displaySummary;
    private String pan;  // 脱敏卡号，用于 CardholderConfirm 显示

    public TransResultStatus getResultStatus() { return resultStatus; }
    public void setResultStatus(TransResultStatus resultStatus) { this.resultStatus = resultStatus; }

    public int getResultCode() { return resultCode; }
    public void setResultCode(int resultCode) { this.resultCode = resultCode; }

    public String getDisplaySummary() { return displaySummary; }
    public void setDisplaySummary(String displaySummary) { this.displaySummary = displaySummary; }

    public String getPan() { return pan; }
    public void setPan(String pan) { this.pan = pan; }
}
