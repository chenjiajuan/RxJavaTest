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
public class RecomeFragment extends BaseFragment {
    private static final String TAG=RecomeFragment.class.getSimpleName();
    private Bundle mBundle;

    @Override
    protected int getLayout() {
        return R.layout.fragment_recome;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.e("TAG","RecomeFragment");
        mBundle=getArguments();
        mBundle.putString(Action.ACTION_CONTENT_RECOMD,Action.ACTION_CONTENT_RECOMD);
//        Bundle bundle=new Bundle();
//        bundle.putString(Action.ACTION_CONTENT_RECOMD,Action.ACTION_CONTENT_RECOMD);
        postData(mBundle);
        //obserableSelf();
    }

    /**
     * 直接new出来一个bundle，当前页面的observer会接收到,如果想让上一个页面也接收到，此处必须取消对本页面的自我注册
     *
     *  E/RecomeFragment: self bundle : Bundle[{ACTION_CONTENT_RECOMD=ACTION_CONTENT_RECOMD}]
     *
     *  E/ContentFragment: 接受到消息，bundle : Bundle[{ACTION_CONTENT_RECOMD=ACTION_CONTENT_RECOMD}]
     *
     */
    public void obserableSelf(){
        observerResult(this, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle bundle) {
                Log.e(TAG,"self bundle : "+bundle);

            }
        });
    }
}
