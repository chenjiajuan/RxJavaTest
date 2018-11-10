package com.chenjiajuan.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chenjiajuan.commom.Action;
import com.chenjiajuan.commom.base.BaseFragment;
import com.chenjiajuan.module_home.R;

/**
 * create by chenjiajuan on 2018/10/4
 */
public class MenuFragment extends BaseFragment {
    private static final String TAG=MenuFragment.class.getSimpleName();
    @Override
    protected int getLayout() {
        return R.layout.fragment_menu;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle intent=getArguments();
        Log.e(TAG,"intent : "+intent.toString());
        Bundle bundle=new Bundle();
        bundle.putString(Action.ACTION_MENU,Action.ACTION_MENU);
        postData(bundle);
    }


}
