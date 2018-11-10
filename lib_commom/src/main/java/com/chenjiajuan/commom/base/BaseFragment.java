package com.chenjiajuan.commom.base;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chenjiajuan.commom.YzFragmentManager;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * create by chenjiajuan on 2018/10/4
 *
 * BaseFragment继承Fragment，实现了SharedVM.IShredData,即实现了LifecycleOwner
 */
public abstract class BaseFragment extends Fragment implements SharedVM.ISharedData<Bundle> {
    private static final String TAG=BaseFragment.class.getSimpleName();
    private YzFragmentManager yzFragmentManager;
    private Unbinder unbinder;

    private SharedVM.ISharedData<Bundle> mSharedData=new SharedVM.AbsSharedData() {
        @Override
        public void postData(Bundle bundle) {
            getSharedData().postValue(bundle);
        }

        @Override
        public void setData(Bundle bundle) {
            getSharedData().setValue(bundle);

        }

        @Override
        public MutableLiveData<Bundle> getSharedData() {
            return ViewModelProviders.of(BaseFragment.this).get(SharedVM.class).getShareData();
        }

        @NonNull
        @Override
        public Lifecycle getLifecycle() {
            return BaseFragment.this.getLifecycle();
        }
    };

    /**
     * 给上层fragment调用，传递数据的犯法，最终调到mSharedData的postData方法：
     *  postData内部，getSharedData.postValue --->getSharedData内部创建了一个MutableLiveData<Bundle>
     *       ViewModelProviders内的作用就是通过BaseFragment和其继承的SharedVM获取到创建好的MutableLiveData，
     *        getShareData其实回去SharedVM中new出来的MutableLiveData.
     * @param bundle
     */
    @Override
    public void postData(Bundle bundle) {
        if (this.getActivity()!=null){
            mSharedData.postData(bundle);
        }

    }

    @Override
    public void setData(Bundle bundle) {
        if (this.getActivity()!=null){
            mSharedData.postData(bundle);
        }
    }

    @Override
    public MutableLiveData<Bundle> getSharedData() {
        return mSharedData.getSharedData();
    }

    /**
     * AbsSharedData抽象类内的方法，继承自ISharedData，实现了内部定义的抽象方法，再次冲洗
     * @param lifecycleOwner
     * @param observer
     */
    @Override
    public void observerResult(LifecycleOwner lifecycleOwner, Observer<Bundle> observer) {
       mSharedData.observerResult(lifecycleOwner,observer);
    }


    public void onNewInstance(Bundle bundle){

    }

    protected YzFragmentManager getYzFragmentManager(){
        if (yzFragmentManager==null){
            yzFragmentManager=new YzFragmentManager(getChildFragmentManager());
        }
        return yzFragmentManager;
    }

    @LayoutRes
    protected abstract  int getLayout();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(getLayout(),container,false);
        view.setClickable(true);
        return  view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (unbinder!=null){
            unbinder.unbind();
        }
        unbinder= ButterKnife.bind(this,view);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
    }
}
