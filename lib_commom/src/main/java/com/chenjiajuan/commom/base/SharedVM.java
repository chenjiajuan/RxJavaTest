package com.chenjiajuan.commom.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.os.Bundle;


/**
 * create by chenjiajuan on 2018/10/4
 */
public class SharedVM extends ViewModel {
    private MutableLiveData<Bundle> shareData=new MutableLiveData<>();
    public MutableLiveData<Bundle> getShareData(){
        return shareData;
    }

    public interface  ISharedData<T> extends LifecycleOwner{
        void postData(T t);
        void setData(T t);

        MutableLiveData<T>  getSharedData();

        void observerResult(final  LifecycleOwner lifecycleOwner, final Observer<T> observer);
    }

    static abstract  class  AbsSharedData implements ISharedData<Bundle>{
        @Override
        public void observerResult(final LifecycleOwner lifecycleOwner, final Observer<Bundle> observer) {
            switch (getLifecycle().getCurrentState()){
                case INITIALIZED:
                    getLifecycle().addObserver(new LifecycleObserver() {
                        @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
                        public void onChange(){
                            getSharedData().removeObservers(lifecycleOwner);
                            getSharedData().observe(lifecycleOwner,observer);
                            getLifecycle().removeObserver(this);
                        }
                    });
                    break;
                case DESTROYED:
                    break;
                default:
                    getSharedData().removeObservers(lifecycleOwner);
                    getSharedData().observe(lifecycleOwner,observer);
            }
        }
    }

}
