package com.chenjiajuan.shop;

/**
 * create by chenjiajuan on 2018/10/10
 */
public class CObservable<T> {
    final OnCSubscribe<T> onCSubscribe;

    private CObservable(OnCSubscribe<T> onCSubscribe){
        this.onCSubscribe=onCSubscribe;
    }

    public static <T> CObservable<T> create(OnCSubscribe<T> onCSubscribe){
        return new CObservable<>(onCSubscribe);
    }

    public void subscribe(CSubscriber<? super T> subscriber){
        subscriber.start();
        onCSubscribe.call(subscriber);
    }

    public interface  OnCSubscribe<T>{
        void call(CSubscriber<? super  T> subscriber);
    }
}
