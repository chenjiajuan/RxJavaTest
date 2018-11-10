package com.chenjiajuan.rxjavatest;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.TextView;

import com.chenjiajuan.home.HomeActivity;

public class StartActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //startActivity(new Intent(this, HomeActivity.class));
        textView1=findViewById(R.id.tv1);
        textView2=findViewById(R.id.tv2);
        setTextView1();
        setTextView2();
    }

    private void setTextView1(){
        String  textPoint=getString(R.string.shoppingguide_member_ai_score)+8.6+"分";
        String  text1=textPoint.substring(5,textPoint.length()-1);
        String  text2=textPoint.substring(textPoint.length()-1,textPoint.length());
        Log.e("TAG","textPoint -"+textPoint+" , text1 -"+text1+" ,text2 -"+text2);
        SpannableString spannableString=new SpannableString(textPoint);
        spannableString.setSpan(new AbsoluteSizeSpan(40),5,textPoint.length()-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.RED),5,textPoint.length()-1,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),5,textPoint.length()-1,SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new AbsoluteSizeSpan(24),textPoint.length()-1,textPoint.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLUE),textPoint.length()-1,textPoint.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD),textPoint.length()-1,textPoint.length(),SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
       textView1.setText(spannableString);
    }
    private void setTextView2(){
        String  textPoint=getString(R.string.shoppingguide_member_ai_score_null);
        String text1=textPoint.substring(textPoint.length()-1,textPoint.length());
        Log.e("TAG","text1 -"+text1);

    }


}
