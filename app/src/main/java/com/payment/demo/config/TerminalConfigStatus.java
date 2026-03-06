package com.payment.demo.config;

/**
 * T006: Terminal/Config Status (data-model).
 * 是否可交易、参数是否加载、读卡模块是否可用、版本信息
 * T019: 各读卡方式（PICC/ICC/MAG）可用性
 */
public class TerminalConfigStatus {
    private boolean tradable;
    private boolean paramLoaded;
    private boolean cardReaderAvailable;
    private boolean piccAvailable;
    private boolean iccAvailable;
    private boolean magAvailable;
    private String appVersion;
    private String kernelVersion;

    public boolean isTradable() { return tradable; }
    public void setTradable(boolean tradable) { this.tradable = tradable; }

    public boolean isParamLoaded() { return paramLoaded; }
    public void setParamLoaded(boolean paramLoaded) { this.paramLoaded = paramLoaded; }

    public boolean isCardReaderAvailable() { return cardReaderAvailable; }
    public void setCardReaderAvailable(boolean cardReaderAvailable) { this.cardReaderAvailable = cardReaderAvailable; }

    public boolean isPiccAvailable() { return piccAvailable; }
    public void setPiccAvailable(boolean piccAvailable) { this.piccAvailable = piccAvailable; }

    public boolean isIccAvailable() { return iccAvailable; }
    public void setIccAvailable(boolean iccAvailable) { this.iccAvailable = iccAvailable; }

    public boolean isMagAvailable() { return magAvailable; }
    public void setMagAvailable(boolean magAvailable) { this.magAvailable = magAvailable; }

    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }

    public String getKernelVersion() { return kernelVersion; }
    public void setKernelVersion(String kernelVersion) { this.kernelVersion = kernelVersion; }
}
