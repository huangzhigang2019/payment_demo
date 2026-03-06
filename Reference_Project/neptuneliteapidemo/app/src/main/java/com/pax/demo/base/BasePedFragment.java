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

package com.pax.demo.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pax.dal.entity.EPedType;
import com.pax.demo.R;
import com.pax.demo.modules.ped.PedFragment;

/**
 * @author Uni.W
 * @date 2019/3/1 15:07
 */
public abstract class BasePedFragment extends BaseFragment {

    public static final String PED_TYPE = "PED_TYPE";

    protected EPedType pedType;

    protected void fragmentSelect(Fragment fragment, EPedType pedType) {
        FragmentManager fManager = getFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(PED_TYPE, pedType.name());
        fragment.setArguments(bundle);
        if(fragment instanceof PedFragment){
            transaction.replace(R.id.parent_layout, fragment);
        }else{
            transaction.replace(R.id.fragment_screen, fragment);
        }
        transaction.commit();
    }

    @CallSuper
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null) {
            pedType = EPedType.valueOf(getArguments().getString(PED_TYPE));
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
