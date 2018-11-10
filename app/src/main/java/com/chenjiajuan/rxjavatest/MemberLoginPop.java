package com.chenjiajuan.rxjavatest;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * create by chenjiajuan on 2018/9/15
 */
public class MemberLoginPop extends PopupWindow {
    private Button mMemberLoginLookBtn;
    private LinearLayout mCancelMemberInfoLayout;

    public MemberLoginPop(Context context) {
        this(context,null);
    }

    public MemberLoginPop(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MemberLoginPop(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popupwindow_member_login_layout, null);
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setTouchable(true);
        this.setFocusable(false);
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(null);
        this.setAnimationStyle(R.style.member_login_popwindow);

    }

}
