package com.chenjiajuan.rxjavatest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;


import java.util.concurrent.TimeUnit;


import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 短信验证码倒计时
 */
public class MessageActivity extends AppCompatActivity {

    EditText edPhone;

    TextView tvPost;


    private Observable observable;
    private Disposable disposable;
    private int MAX_TIME=10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        edPhone=this.findViewById(R.id.edPhone);
        tvPost=this.findViewById(R.id.tvPost);
        try {
            RxView.enabled(tvPost).accept(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        observable=RxView.clicks(tvPost)
                .throttleFirst(1, TimeUnit.SECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Object, ObservableSource<Boolean>>() {
                    @Override
                    public ObservableSource<Boolean> apply(Object o) throws Exception {
                        String phone=edPhone.getText().toString();
                        if (TextUtils.isEmpty(phone)){
                            Toast.makeText(MessageActivity.this,"手机号码不能为空",Toast.LENGTH_LONG).show();
                            return  Observable.empty();
                        }

                        return Observable.just(true);
                    }
                })
                 .flatMap(new Function<Boolean, ObservableSource<?>>() {
                     @Override
                     public ObservableSource<?> apply(Boolean aBoolean) throws Exception {
                         RxTextView.text(tvPost).accept("剩余"+MAX_TIME+"秒");
                         RxView.enabled(tvPost).accept(false);
                         return Observable.interval(1, TimeUnit.SECONDS, Schedulers.io())
                                 .take(MAX_TIME)
                                 .map(new Function<Long, Long>() {
                                     @Override
                                     public Long apply(Long aLong) throws Exception {
                                         return MAX_TIME-(aLong+1);
                                     }
                                 });
                     }
                 }).observeOn(AndroidSchedulers.mainThread());
        Consumer<Long> consumer=new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if (aLong==0){
                    RxView.enabled(tvPost).accept(true);
                    RxTextView.text(tvPost).accept("重新发送验证码");
                }else {
                    RxTextView.text(tvPost).accept("剩余"+aLong+"秒");
                }

            }
        };

        disposable=observable.subscribe(consumer);


    }

}
