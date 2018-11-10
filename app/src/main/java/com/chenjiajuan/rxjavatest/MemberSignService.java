package com.chenjiajuan.rxjavatest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * create by chenjiajuan on 2018/9/11
 */
public class MemberSignService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG","onCreate....");
        SignDialog signDialog=new SignDialog(this);
        signDialog.show();

    }

    @Override
    public boolean onUnbind(Intent intent) {
        super.onUnbind(intent);
        Log.e("TAG","onUnbind....");
        return  true;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
