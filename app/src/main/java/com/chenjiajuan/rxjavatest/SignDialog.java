package com.chenjiajuan.rxjavatest;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

/**
 * create by chenjiajuan on 2018/9/11
 */
public class SignDialog extends Dialog {


    public SignDialog(@NonNull Context context) {
        this(context,R.style.member_signDialog);
    }

    public SignDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView();
    }

    private void initView() {
        setContentView(R.layout.member_view_sign_dialog);
        setCanceledOnTouchOutside(false);
    }


}
