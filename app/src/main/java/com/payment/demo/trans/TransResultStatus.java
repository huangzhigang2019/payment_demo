package com.payment.demo.trans;

/** Result status for transaction (top-level to avoid R8/D8 issues with inner enum). */
public enum TransResultStatus { SUCCESS, FAILED, CANCELLED, TIMEOUT }
