/*
 * ===========================================================================================
 * = COPYRIGHT
 *          PAX Computer Technology (Shenzhen) Co., Ltd. PROPRIETARY INFORMATION
 *   This software is supplied under the terms of a license agreement or nondisclosure
 *   agreement with PAX Computer Technology (Shenzhen) Co., Ltd. and may not be copied or
 *   disclosed except in accordance with the terms in that agreement.
 *     Copyright (C) 2017-2023 PAX Computer Technology (Shenzhen) Co., Ltd. All rights reserved.
 * Description: // Detail description about the function of this module,
 *             // interfaces with the other modules, and dependencies.
 * Revision History:
 * Date                         Author                        Action
 * 2017/12/20                   PAX                     Create/Add/Modify/Delete
 * ===========================================================================================
 */

package com.pax.demo.modules.comm;

import com.pax.dal.IComm;
import com.pax.dal.IComm.EConnectStatus;
import com.pax.dal.entity.EUartPort;
import com.pax.dal.entity.UartParam;
import com.pax.dal.exceptions.CommException;
import com.pax.demo.base.DemoApp;
import com.pax.demo.util.BaseTester;

public class UartTester extends BaseTester {

    private static UartTester commTester;

    private IComm uartComm;

    private UartTester() {
        String model = android.os.Build.MODEL.toString();
        UartParam uartParam = new UartParam();
        uartParam.setPort(EUartPort.COM1);
        // uartParam.setPort(model.equals("A920") ? (EUartPort.USBDEV) : (EUartPort.COM1));
        uartParam.setAttr("9600,8,n,1");
        // uartParam.setAttr("PAXDEV");

        // uartComm = getObject.getIDals().getCommManager().getUartComm(uartParam);
        uartComm = DemoApp.getDal().getCommManager().getUartComm(uartParam);
        logTrue("getUartComm" + uartParam.getPort().toString());
        // comm = IppUser.getInstance().getDal().getCommManager().getUartComm(uartParam);

    };

    public static UartTester getInstance() {
        if (commTester == null) {
            commTester = new UartTester();
        }
        return commTester;
    }

    public void connect() {
        if (uartComm != null) {
            try {
                if (uartComm.getConnectStatus() == EConnectStatus.DISCONNECTED) {
                    uartComm.connect();
                    logTrue("Connect");
                } else {
                    logTrue("have connected");
                }
            } catch (CommException e) {
                logErr("Connect", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void send(byte[] data) {
        if (uartComm != null) {
            try {
                connect();
                if (uartComm.getConnectStatus() == EConnectStatus.CONNECTED) {
                    uartComm.send(data);
                    logTrue("Send");
                } else {
                    logErr("Send", "please connect first");
                }
            } catch (CommException e) {
                logErr("Send", e.getMessage());
                e.printStackTrace();
            } finally {
                // disConnect();
            }
        }
    }

    public void sendDataUnlimited(byte[] data) {
        if (uartComm != null) {
            try {
                connect();
                if (uartComm.getConnectStatus() == EConnectStatus.CONNECTED) {
                    uartComm.sendDataUnlimited(data);
                    logTrue("Send");
                } else {
                    logErr("Send", "please connect first");
                }
            } catch (CommException e) {
                logErr("Send", e.getMessage());
                e.printStackTrace();
            } finally {
                // disConnect();
            }
        }
    }
    public byte[] recv(int len) {
        if (uartComm != null) {
            try {
                connect();
                if (uartComm.getConnectStatus() == EConnectStatus.CONNECTED) {
                    byte[] result = uartComm.recv(len);
                    logTrue("Recv");
                    return result;
                } else {
                    logErr("Send", "please connect first");
                    return null;
                }
            } catch (CommException e) {
                e.printStackTrace();
                logErr("Recv", e.getMessage());
                return null;
            }
        }
        return null;
    }

    public byte[] recvNonBlocking() {
        if (uartComm != null) {
            try {
                connect();
                if (uartComm.getConnectStatus() == EConnectStatus.CONNECTED) {
                    byte[] result = uartComm.recvNonBlocking();
                    logTrue("recvNonBlocking");
                    return result;
                } else {
                    logErr("recvNonBlocking", "please connect first");
                    return null;
                }
            } catch (CommException e) {
                e.printStackTrace();
                logErr("recvNonBlocking", e.getMessage());
                return null;
            }
        }
        return null;
    }

    public void disConnect() {
        if (uartComm != null) {
            try {
                if (uartComm.getConnectStatus() == EConnectStatus.CONNECTED)
                    uartComm.disconnect();
                logTrue("DisConnect");
                // commTester = null;
                // uartComm = null;
            } catch (CommException e) {
                e.printStackTrace();
                logErr("DisConnect", e.getMessage());
            }
        }
    }

    public void setConnectTimeout(int timeout) {
        if (uartComm != null) {
            uartComm.setConnectTimeout(timeout);
            logTrue("setConnectTimeout");
        }
    }

    public void cancelRecv() {
        if (uartComm != null) {
            uartComm.cancelRecv();
            logTrue("cancelRecv");
            disConnect();
        }
    }

    public void reset() {
        if (uartComm != null) {
            uartComm.reset();
            logTrue("reset");
        }
    }

    public void setSendTimeout(int timeout) {
        if (uartComm != null) {
            uartComm.setSendTimeout(timeout);
            logTrue("setSendTimeout");
        }
    }

    public void setRecvTimeout(int timeout) {
        if (uartComm != null) {
            uartComm.setRecvTimeout(timeout);
            logTrue("setRecvTimeout");
        }
    }


}
