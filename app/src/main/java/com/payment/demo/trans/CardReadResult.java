package com.payment.demo.trans;

/**
 * T007: CardReadResult entity (data-model).
 * 介质类型、是否成功、失败原因、卡片标识信息
 */
public class CardReadResult {
    public static final int RET_OK = 0;
    public static final int RET_TIMEOUT = 1;
    public static final int RET_CANCEL = 2;
    public static final int RET_INIT_FAILED = 3;
    public static final int RET_OTHER = 4;

    private int readType;       // PICC/ICC/Mag 对应值
    private boolean success;
    private int failCode;       // RET_*
    private String cardInfo;    // 可选，如 PAN、track2 等
    private String failReason;  // 失败时展示用

    public int getReadType() { return readType; }
    public void setReadType(int readType) { this.readType = readType; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getFailCode() { return failCode; }
    public void setFailCode(int failCode) { this.failCode = failCode; }

    public String getCardInfo() { return cardInfo; }
    public void setCardInfo(String cardInfo) { this.cardInfo = cardInfo; }

    public String getFailReason() { return failReason; }
    public void setFailReason(String failReason) { this.failReason = failReason; }
}
