package com.chenjiajuan.rxjavatest;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.chenjaijuan.routerannotation.BindView;
import com.chenjiajuan.moudledao.DaoActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends Activity {
    private static final String TAG=MainActivity.class.getSimpleName();
    private CompositeSubscription compositeSubscription=new CompositeSubscription();
    private Subscription subscription;
    private LinearLayout ll_bten;
    private long count=0;
    private int MAX_TIME=10;
    private ImageView iv_image;
    public static String SDPATH = Environment.getExternalStorageDirectory() + File.separator;
    public static String APP_PATH = SDPATH + "retail" + File.separator;
    public static String IMAGE_PATH = APP_PATH + "img" + File.separator;

    @BindView(R.id.ll_bten)
    LinearLayout mLinearLAyout;

    @BindView(R.id.btn_start)
    Button btnStart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ll_bten=findViewById(R.id.ll_bten);
        iv_image=findViewById(R.id.iv_image);
        iv_image.setDrawingCacheEnabled(true);
        iv_image.buildDrawingCache();
        Bitmap bitmap = iv_image.getDrawingCache();
        String url="https://h5.youzan.com/v2/goods/2fqdfzlx94883?from_source=retail_sales&from_params=sales_id~907572682!sales_ch~" +
                "online!sales_act_id~1!sales_display_id~440670912!store_kdt_id~18938611!online_kdt_id~18938611!head_kdt_id~18938611!spread_type~1";
       // URLRequest("40666106",url);
       // testList();

    }

    public static Map<String, String> URLRequest(String kdtId,String URL)
    {
        Map<String, String> mapRequest = new HashMap<String, String>();
        mapRequest.put("kdtId",kdtId);
        String[] arrSplit=null;
        String strUrlParam=null;
        if(TextUtils.isEmpty(URL)){
            return null;
        }
        String strURL=URL.trim().toLowerCase();
        String[] arrForm = strURL.split("[?]");
        if(arrForm.length>1)
        {
            String strings=arrForm[0];
            String index="goods/";
            int text=strings.indexOf(index);
            if (strings.length()>text){
                String alias=strings.substring(text+index.length());
                mapRequest.put("alias",alias);
            }
            if(arrForm[1]!=null)
            {
                strUrlParam=arrForm[1];
            }
        }
        if(strUrlParam==null)
        {
            return mapRequest;
        }
        arrSplit=strUrlParam.split("[&]");
        for(String strSplit:arrSplit)
        {
            String[] arrSplitEqual=null;
            arrSplitEqual= strSplit.split("[=]");
            if(arrSplitEqual.length>1)
            {
                mapRequest.put(arrSplitEqual[0], arrSplitEqual[1]);

            }
            else
            {
                if(arrSplitEqual[0]!="")
                {
                    mapRequest.put(arrSplitEqual[0], "");
                }
            }
        }
        Log.e(TAG,mapRequest.toString());
        return mapRequest;
    }


    public static void testFile(Bitmap bitmap){

        String fileName =getCurrentTimeFormatToDate() + ".jpg";
        String path = getImageFilePath(fileName);
        Log.e(TAG,"bitmap : "+bitmap +", path : "+path);
        File file=saveBitmap(path,bitmap);
        Log.e(TAG,"file :"+file);
    }

    public static String getImageFilePath(String fileName) {
        File directory = new File(IMAGE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return IMAGE_PATH + "IMG_" + fileName;
    }
    public static File saveBitmap(String path, Bitmap bitmap) {
        File f = new File(path);
        f.deleteOnExit();
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if (fOut != null) {
                fOut.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (fOut != null) {
                fOut.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    public static String getCurrentTimeFormatToDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA);
        return dateFormat.format(new Date(System.currentTimeMillis()));
    }

    private void testList(){
      List<String> list=new ArrayList<>();
        list.add("1");
        list.add("2");
        list.add("23e234");
        String a=list.toString();
        List<String> list2= Arrays.asList(a);
        for (String b:list2){
            if (b.equals("2")){
                Log.e(TAG,"1111111111");
            }
        }
    }



    public Subscription pollQrSignCodeHide(final long termEnd){
        //轮询十次，当返回为true时轮询结束
       subscription=Observable.interval(1, TimeUnit.SECONDS)
               .flatMap(new Func1<Long, Observable<Boolean>>() {
                   @Override
                   public Observable<Boolean> call(Long aLong) {
                       Log.e(TAG,"along : "+aLong);
                            count=aLong;
                       if (aLong==12){
                           return Observable.just(true);
                       }
                       return Observable.just(false);
                   }
               })
               .take(MAX_TIME)
               .takeUntil(new Func1<Boolean, Boolean>() {
                   @Override
                   public Boolean call(Boolean aLong) {
                       return aLong;
                   }
               }).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {
                        if (count+1==MAX_TIME){
                            //轮询超时，在此处处理结果
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean along) {
                        if (along){
                            //轮询返回true在此处处理结果
                            Log.e(TAG,"onNext : "+Thread.currentThread());
                        }


                    }
                });
       return subscription;

    }

    public void startInterval(View view){
//        if (subscription==null||compositeSubscription.isUnsubscribed()){
//            Log.e(TAG,"return ");
//            return;
//        }
        //compositeSubscription.add(pollQrSignCodeHide(1536984005));

        Intent intent=new Intent();
        intent.setClass(MainActivity.this,DaoActivity.class);
        startActivity(intent);

    }

    public void stopInterval(View view){
      compositeSubscription.clear();

    }




    private void starService(){
        Intent  intent=new Intent();
        intent.setPackage(this.getPackageName());
        intent.setAction("com.chenjiajuan.signdialog");
        bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void unBinder(){
        if (serviceConnection!=null){
            unbindService(serviceConnection);
        }
    }


}
