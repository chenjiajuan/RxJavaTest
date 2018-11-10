package com.chenjiajuan.commom.base;
import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.chenjiajuan.commom.Action;
import com.chenjiajuan.commom.YzFragmentManager;

/**
 * create by chenjiajuan on 2018/10/4
 */
public abstract  class BaseActivity extends AppCompatActivity {
    private static final String TAG=BaseActivity.class.getSimpleName();
    private YzFragmentManager mYzFragmentManager;
    protected YzFragmentManager getYzFragmentManager() {
        if (mYzFragmentManager == null) {
            mYzFragmentManager = new YzFragmentManager(getSupportFragmentManager());
        }
        return mYzFragmentManager;
    }

    public void  setObserve(BaseFragment fragment){
        fragment.observerResult(this, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle bundle) {
                Log.e(TAG,"bundle : "+bundle.toString());
                if (bundle.containsKey(Action.ACTION_MENU)){
                    onUpBundle(bundle);
                }
            }
        });
    }

    protected abstract void onUpBundle( Bundle bundle);
}

