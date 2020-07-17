package org.zhx.common.BGStart.demo;

import android.app.Activity;
import android.app.Application;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import org.zhx.common.bgstart.library.BgManager;
import org.zhx.common.bgstart.library.impl.BgStart;

/**
 * Copyright (C), 2015-2020
 * FileName: App
 * Author: zx
 * Date: 2020/4/17 11:46
 * Description:
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        BgManager.getInstance().init(this);
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
