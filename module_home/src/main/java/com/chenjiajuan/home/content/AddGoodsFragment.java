package com.chenjiajuan.home.content;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.chenjiajuan.commom.Action;
import com.chenjiajuan.commom.base.BaseFragment;
import com.chenjiajuan.module_home.R;

/**
 * create by chenjiajuan on 2018/10/5
 */
public class AddGoodsFragment extends BaseFragment{
    private static final String TAG=AddGoodsFragment.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.fragment_add_goods;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obserableSelf();

        initView();
    }

    /**
     * 当该页面加载商品完成时，向另一个页面MenuFragment发送通知
     */
    private void initView() {
        Bundle bundle=new Bundle();
        bundle.putString(Action.ACTION_INFO_TO_MENU,Action.ACTION_INFO_TO_MENU);
        postData(bundle);

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
