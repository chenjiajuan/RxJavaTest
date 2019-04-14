package com.chenjiajuan.commom.util;

import android.content.Context;

import com.chenjiajuan.commom.entity.DaoMaster;
import com.chenjiajuan.commom.entity.DaoSession;
import com.chenjiajuan.commom.entity.StudentDao;

public class GreenUtil {

    private static GreenUtil insatcne;
    private Context context;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static GreenUtil getInsatcne(Context context) {
        if (insatcne==null){
            insatcne=new GreenUtil(context);
        }
        return insatcne;
    }

    GreenUtil(Context context){
        this.context=context;
    }

    public StudentDao getStuDao(){
        DaoMaster.DevOpenHelper devOpenHelper=new DaoMaster.DevOpenHelper(context,"cjj.db",null);
        daoMaster=new DaoMaster(devOpenHelper.getWritableDatabase());
        daoSession=daoMaster.newSession();
        return daoSession.getStudentDao();
    }
}
