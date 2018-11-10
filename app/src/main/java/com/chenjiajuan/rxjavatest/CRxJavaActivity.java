package com.chenjiajuan.rxjavatest;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.chenjiajuan.shop.CObservable;
import com.chenjiajuan.shop.CSubscriber;

/**
 * create by chenjiajuan on 2018/10/10
 */
public class CRxJavaActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showCustomerView();

    }


    /**
     * OnCSubscribe
     * CSubscriber观察者
     */
    private void showCustomerView() {

        CObservable.create(new CObservable.OnCSubscribe<Integer>() {
            @Override
            public void call(CSubscriber<? super Integer> subscriber) {
                //数据的发射者
                subscriber.onNext(1);

            }
        }).subscribe(new CSubscriber<Integer>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(Integer var1) {

            }
        });
    }
}
