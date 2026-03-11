package com.payment.demo.trans;

/** Result status for transaction (top-level to avoid R8/D8 issues with inner enum). */
public enum TransResultStatus {
    SUCCESS,
    FAILED,
    CANCELLED,
    TIMEOUT,
    /** PICC 需联机：已读卡获 PAN，需先确认卡号再调用 completeTransProcess */
    NEED_CARDHOLDER_CONFIRM
}
