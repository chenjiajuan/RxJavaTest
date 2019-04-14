package com.chenjiajuan.moudledao;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.chenjiajuan.commom.base.BaseActivity;
import com.chenjiajuan.commom.entity.Student;
import com.chenjiajuan.commom.util.GreenUtil;

import java.util.List;

public class DaoActivity extends BaseActivity {
    private TextView addText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dao);
        //addText=this.findViewById(R.id.addText);
    }

    public void addMessage(View view){
        Student student=new Student(null,"2014071123","温吐艳",12,"男","23");
        long end= GreenUtil.getInsatcne(this).getStuDao().insert(student);
        if (end>0){
            Toast.makeText(this,"成功",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"失败",Toast.LENGTH_SHORT).show();
        }
        GreenUtil.getInsatcne(this).getStuDao().insert(new Student(null,"213334324r23","lalal",12,"女","44"));

        List<Student> studentList=GreenUtil.getInsatcne(this).getStuDao().queryBuilder().list();
        for (Student student1:studentList){
            Log.e("TAG",student1.getId()+"_"+student1.getName()+"_"+student1.getSex()+"_"+student1.getScore());
        }

    }

    @Override
    protected void onUpBundle(Bundle bundle) {

    }
}
