package org.zhx.common.BGStart.demo;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import org.zhx.common.bgstart.library.BgManager;
import org.zhx.common.bgstart.library.api.AppStateCallback;
import org.zhx.common.bgstart.library.impl.BgStart;

/**
 * Copyright (C), 2015-2020
 * FileName: App
 * Author: zx
 * Date: 2020/4/17 11:46
 * Description:
 */
public class App extends Application {
    private Handler mHandler = new Handler();
    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        // 以下 三种初始化方式  3选1 即可
//        BgManager.getInstance().init(this);
        // 如果您的应用 实现了  Application.ActivityLifecycleCallbacks 接口
        //BgManager.getInstance().init(this,null,this);
        //如果你想监听 app 前后台 状态切换
        BgManager.getInstance().init(this,  null, new AppStateCallback() {
            @Override
            public void onForground(Activity activity) {

            }

            @Override
            public void onBackground(Activity activity) {
//模拟 后台 启动页面  当 按下 home 建 1秒后 弹出 界面
//                mHandler.removeCallbacks(null);
//                mHandler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("BgStart", "开始跳转 " + System.currentTimeMillis());
//                        Intent intent = new Intent(mContext, TargetActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        BgStart.getInstance().startActivity(mContext, intent, TargetActivity.class.getName());
//                    }
//                }, 1000);
            }
        });
    }

//    private NotificationCompat.Builder getNotifyBuilder() {
//        //实例化通知栏构造器。
//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, BgStart.CHANNEL_ID);
//        //系统收到通知时，通知栏上面显示的文字。
//        mBuilder.setTicker("正在房间中");
//        //显示在通知栏上的小图标
//        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
//        mBuilder.setWhen(System.currentTimeMillis());
//        RemoteViews headsUpView = new RemoteViews(getPackageName(), R.layout.custom_heads_up_layout);
//        mBuilder.setCustomHeadsUpContentView(headsUpView);
//        return mBuilder;
//    }
}
