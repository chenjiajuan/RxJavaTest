package com.chenjiajuan.shop;

/**
 * create by chenjiajuan on 2018/9/11
 */
public interface CObserver<T> {

  void onCompleted();
  void onError(Throwable throwable);
  void onNext(T var1);

}
