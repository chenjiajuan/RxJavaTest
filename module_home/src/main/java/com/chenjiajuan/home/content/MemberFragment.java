package com.chenjiajuan.home.content;


import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chenjiajuan.commom.base.BaseFragment;
import com.chenjiajuan.module_home.R;

/**
 * create by chenjiajuan on 2018/10/5
 */
public class MemberFragment extends BaseFragment{

    private static final String TAG=MemberFragment.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.fragment_member;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obserableSelf();
    }

    public void obserableSelf(){
        observerResult(this, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle bundle) {
                Log.e(TAG,"bundle : "+bundle);

            }
        });
    }
}
