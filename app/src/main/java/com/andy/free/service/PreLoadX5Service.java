package com.andy.free.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.andy.free.X5PlayWebActivity;
import com.andy.free.x5.X5WebView;
import com.tencent.smtt.sdk.QbSdk;

/**
 * Created by Andy Lau on 2017/10/27.
 */

public class PreLoadX5Service extends Service {

    /*
    * 初始化回调
    * */
    QbSdk.PreInitCallback cb=new QbSdk.PreInitCallback() {
        @Override
        public void onCoreInitFinished() {
            Log.d("PreLoadX5Service","x5 core init Finished!");
        }

        @Override
        public void onViewInitFinished(boolean b) {
            Log.d("PreLoadX5Service","view init Finished "+b+"!");
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initX5();
        preInitX5WebCore();
    }



    /*
    * 预加载一次X5核心
    * */
    private void preInitX5WebCore() {

        if (!QbSdk.isTbsCoreInited()){
            QbSdk.preInit(getApplicationContext(),cb);
        }
    }

    private void initX5() {
        QbSdk.initX5Environment(getApplicationContext(),cb);
    }

}
