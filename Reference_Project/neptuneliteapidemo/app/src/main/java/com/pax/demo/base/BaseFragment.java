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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.pax.demo.R;

/**
 * @author Uni.W
 * @date 2019/3/1 15:07
 */
public abstract class BaseFragment extends Fragment {

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(attachLayoutRes(), container, false);
//
//        initData();
//        initView(view);
//
//        return view;
//    }
//
//    @LayoutRes
//    public abstract int attachLayoutRes();
//
//    public abstract void initData();
//
//    public abstract void initView(View view);

    public void fragmentSelect(Fragment fragment) {
        FragmentManager fManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        transaction.replace(R.id.fragment_screen, fragment);
        transaction.commit();
    }

}
