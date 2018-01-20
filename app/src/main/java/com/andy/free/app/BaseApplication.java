package com.andy.free.app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.andy.free.service.PreLoadX5Service;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsDownloader;
import com.tencent.smtt.sdk.TbsListener;

/**
 * Created by Administrator on 2017/10/14.
 */

public class BaseApplication extends Application {
    @Override
    public void onCreate() {

        initX5c();
    }

    private void initX5() {

        Intent intent=new Intent(this,PreLoadX5Service.class);
        startService(intent);
    }
    private void initX5c(){

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("initX5c", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };

        QbSdk.setTbsListener(
                new TbsListener() {
                    @Override
                    public void onDownloadFinish(int i) {
                        Log.d("initX5c", "onDownloadFinish");
                    }
                    @Override
                    public void onInstallFinish(int i) {
                        Log.d("initX5c", "onInstallFinish");
                    }
                    @Override
                    public void onDownloadProgress(int i) {
                        Log.d("initX5c", "onDownloadProgress:" + i);
                    }
                });

        if (!QbSdk.isTbsCoreInited()){
            QbSdk.preInit(getApplicationContext(),cb);
        }

        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);


    }
}
