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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AmexParam {
    private final List<AmexAid> amexAids = new ArrayList<>();
    private final List<ExPayDRL> amexDRLs = new ArrayList<>();

    /**
     * Add the aid to the parameter list if it is not contained
     * @param aid Aid to be added
     */
    public void addAid(AmexAid aid) {
        if (contains(aid) == null)
            amexAids.add(aid);
    }

    /**
     * Add the dynamic reader limit to the parameter list if it is not contained
     * @param drl Dynamic reader limit to be added
     */
    public void addDrl(ExPayDRL drl) {
        if (contains(drl) == null) {
            amexDRLs.add(drl);
        }
    }

    public List<AmexAid> getAmexAids() {
        return amexAids;
    }

    public List<ExPayDRL> getAmexDRLs() {
        return amexDRLs;
    }

    /**
     * Check if current aid list contains the aid to be add
     * @return Return the element aid if it is contained in the list, or null if not
     */
    public AmexAid contains(AmexAid aid) {
        if (amexAids.contains(aid)) {
            return aid;
        } else {
            for (AmexAid aid1 : amexAids) {
                if (Arrays.equals(aid.getApplicationID(), aid1.getApplicationID())) {
                    return aid1;
                }
            }
        }

        return null;
    }

    public ExPayDRL contains(ExPayDRL drl) {
        if (amexDRLs.contains(drl)) {
            return drl;
        } else {
            for (ExPayDRL drl1 : amexDRLs) {
                if (Arrays.equals(drl.getProgramId(), drl1.getProgramId())) {
                    return drl1;
                }
            }
        }

        return null;
    }

}
