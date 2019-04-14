package com.chenjiajuan.home;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.chenjiajuan.commom.Action;
import com.chenjiajuan.commom.YzFragmentManager;
import com.chenjiajuan.commom.base.BaseFragment;
import com.chenjiajuan.commom.weight.BorderedRadioButton;
import com.chenjiajuan.home.content.AddGoodsFragment;
import com.chenjiajuan.home.content.MemberFragment;
import com.chenjiajuan.home.content.RecomeFragment;
import com.chenjiajuan.module_home.R;
import com.chenjiajuan.module_home.R2;
import com.jakewharton.rxbinding.widget.RxRadioGroup;

import butterknife.BindView;
import rx.Subscription;
import rx.functions.Action1;

/**
 * create by chenjiajuan on 2018/10/4
 */
public class ContentFragment extends BaseFragment {
    private static final String TAG=ContentFragment.class.getSimpleName();

    RadioGroup radioGroup;


    @BindView(R2.id.rbRecomLayout)
    BorderedRadioButton rbRecomLayout;

    @BindView(R2.id.rbAddGoodsLayout)
    BorderedRadioButton rbAddGoodsLayout;


    @BindView(R2.id.rbMemberLayout)
    BorderedRadioButton rbMemberLayout;


    private Bundle mBundle;

    private BaseFragment recomeFragment;

    private BaseFragment memberFragment;

    private BaseFragment addGoodsFragment;

    @Override
    protected int getLayout() {
        return R.layout.fragment_content;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        radioGroup=view.findViewById(R.id.radioGroup);
         mBundle=getArguments();
        obserableSelf();
        listenRadioButton();

    }

    @Override
    public void onNewInstance(Bundle bundle) {
        super.onNewInstance(bundle);
    }

    private void listenRadioButton(){
        Subscription subscription= RxRadioGroup.checkedChanges(radioGroup)
                .subscribe(new Action1<Integer>(){
                    @Override
                    public void call(Integer integer) {
                        if (integer==R.id.rbRecomLayout){
                            rbAddGoodsLayout.setCheckedState(false);
                            rbMemberLayout.setCheckedState(false);
                            rbRecomLayout.setCheckedState(true);
                            recomeFragment=getYzFragmentManager().show(getContext(),R.id.fragment_replace,
                                    RecomeFragment.class,mBundle, YzFragmentManager.LAUNCH_PULL);
                            obsverable(recomeFragment);


                        }else if (integer==R.id.rbMemberLayout){
                            rbRecomLayout.setCheckedState(false);
                            rbAddGoodsLayout.setCheckedState(false);
                            rbMemberLayout.setCheckedState(true);
                            memberFragment=getYzFragmentManager().show(getContext(),R.id.fragment_replace,
                                    MemberFragment.class,mBundle,YzFragmentManager.LAUNCH_PULL);
                            obsverable(memberFragment);


                        }else if (integer==R.id.rbAddGoodsLayout){
                            rbRecomLayout.setCheckedState(false);
                            rbMemberLayout.setCheckedState(false);
                            rbAddGoodsLayout.setCheckedState(true);
                             addGoodsFragment=getYzFragmentManager().show(getContext(),R.id.fragment_replace,
                                    AddGoodsFragment.class,mBundle,YzFragmentManager.LAUNCH_PULL);
                            obsverable(addGoodsFragment);
                        }

                    }
                },new Action1<Throwable>(){

                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * RecomeFragment postData -->ContentFragment的observerResult内注册该fragemnt，接受到消息
     * ContentFragment接收到后，在调用postData -->它在BaseActivity内注册了监听，所以BaseActivity内接收到了消息
     * BaseActivity内又调用了onUpBundle，返回给上层HomeActivity
     *  ---->再本页面自身也注册了一个监听，所以也会得到该消息
     *
     *  如果两个子页（B，C）面要通信，则必须通过其共同的父级（A）页面
     *
     *  B->A->C
     *
     *  在A内注册了B、C的监听，A 在postData时，C优先收到，收到后，可以根据Action，判断需要做什么逻辑，
     *    如果需要对C做操作，则再post到
     *
     * @param baseFragment
     */
    public  void  obsverable(final BaseFragment baseFragment){
        baseFragment.observerResult(baseFragment, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle bundle) {
                Log.e(TAG,"接受到消息，bundle : "+bundle.toString());
                if (bundle.containsKey(Action.ACTION_CONTENT_RECOMD)){
                    if (recomeFragment!=null){
                        //接受到来自注册页面的某个消息，需要传给其他页面，比如Recome页面，则获取到recom页面，调用它的postData
                      //  Bundle bundle1=new Bundle();
                        //bundle1.putString("11111","11111");
                        //recomeFragment.postData(bundle1);

                        //直接向bundle塞进数据,Recome也可以接受到bundle
                        bundle.putString(Action.ACTION_INFO_QUERY_DATA,Action.ACTION_INFO_QUERY_DATA);
                        recomeFragment.postData(bundle);



                    }

                }else if (bundle.containsKey(Action.ACTION_INFO_TO_MENU)){
                   // postData(bundle); 调用自身的数据传递方法，//此时调用baseFragment的方法，这个fragment在BaseActivity内被注册了，
                    postData(bundle);
                }


            }
        });

    }


    /**
     * 发送一个当前页面new出来的bundle，那么当前页面监听者可以接收到--->因为跟跳转的页面没有任何关系
     *  Bundle bundle=new Bundle();
        bundle.putString(Action.ACTION_CONTENT,Action.ACTION_CONTENT);
        postData(bundle);
     */

    public void  obserableSelf(){
        observerResult(this, new Observer<Bundle>() {
            @Override
            public void onChanged(@Nullable Bundle bundle) {
                Log.e(TAG,"self ,  bundle :  "+bundle.toString());
               // bundle.putString("22222222222","2222222222222");
                //postData(bundle);
            }
        });
    }

}
