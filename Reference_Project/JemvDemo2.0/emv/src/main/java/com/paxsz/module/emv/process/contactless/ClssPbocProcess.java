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
 *   20210111  	           John Cai                 Create
 * ===========================================================================================
 */

package com.paxsz.module.emv.process.contactless;

import com.pax.jemv.clcommon.ByteArray;
import com.pax.jemv.clcommon.EMV_CAPK;
import com.pax.jemv.clcommon.EMV_REVOCLIST;
import com.paxsz.module.emv.process.entity.IssuerRspData;
import com.paxsz.module.emv.process.entity.TransResult;

class ClssPbocProcess extends ClssKernelProcess {
    @Override
    protected TransResult startTransProcess() {
        return null;
    }

    @Override
    protected TransResult completeTransProcess(IssuerRspData issuerRspData) {
        return null;
    }

    @Override
    protected int getTlv(int tag, ByteArray value) {
        return 0;
    }

    @Override
    protected int setTlv(int tag, byte[] value) {
        return 0;
    }

    @Override
    protected int addCapkAndRevokeList(EMV_CAPK emvCapk, EMV_REVOCLIST emvRevoclist) {
        return 0;
    }

    @Override
    protected String getTrack2() {
        return null;
    }

    @Override
    protected boolean isNeedSecondTap(IssuerRspData issuerRspData) {
        return false;
    }
}
