package com.chenjiajuan.home;

import android.os.Bundle;
import android.util.Log;

import com.chenjiajuan.commom.Action;
import com.chenjiajuan.commom.base.BaseActivity;
import com.chenjiajuan.commom.base.BaseFragment;
import com.chenjiajuan.commom.YzFragmentManager;
import com.chenjiajuan.module_home.R;

public class HomeActivity extends BaseActivity {

    private static final String TAG=HomeActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        inflateMenuFragment();
        inflateContentFragment();
    }

    public void  inflateMenuFragment(){
        Bundle bundle=new Bundle();
        bundle.putString(Action.ACTION_HOME_ACTIVITY,Action.ACTION_HOME_ACTIVITY);
        bundle.putString(Action.ACTION_MENU,Action.ACTION_MENU);
        BaseFragment baseFragment=getYzFragmentManager().show(HomeActivity.this,
                R.id.frameLayout_left,MenuFragment.class,bundle,
                YzFragmentManager.LAUNCH_REPLACE);
        setObserve(baseFragment);
    }

    public void  inflateContentFragment(){
        Bundle bundle=new Bundle();
        bundle.putString(Action.ACTION_HOME_ACTIVITY,Action.ACTION_HOME_ACTIVITY);
        bundle.putString(Action.ACTION_CONTENT,Action.ACTION_CONTENT);
        BaseFragment baseFragment=getYzFragmentManager().show(HomeActivity.this,
                R.id.frameLayout_right,ContentFragment.class,bundle,YzFragmentManager.LAUNCH_REPLACE);
        setObserve(baseFragment);
    }


    @Override
    protected void onUpBundle(Bundle bundle) {
        Log.e(TAG,"bundle : "+bundle.toString() );
    }
}
