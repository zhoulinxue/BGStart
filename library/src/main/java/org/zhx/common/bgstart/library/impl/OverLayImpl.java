package org.zhx.common.bgstart.library.impl;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import org.zhx.common.bgstart.library.BridgeActivity;
import org.zhx.common.bgstart.library.BridgeBroadcast;
import org.zhx.common.bgstart.library.CheckRunable;
import org.zhx.common.bgstart.library.CustomActivityManager;
import org.zhx.common.bgstart.library.SystemAlertWindow;
import org.zhx.common.bgstart.library.api.ActivityCheckLisenter;
import org.zhx.common.bgstart.library.api.BgStart;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.PermissionServer;
import org.zhx.common.bgstart.library.utils.Miui;
import org.zhx.common.bgstart.library.utils.NotificationsUtils;
import org.zhx.common.bgstart.library.utils.PermissionUtil;

/**
 * Copyright (C), 2015-2020
 * FileName: IBgStartImpl
 * Author: zx
 * Date: 2020/4/17 15:02
 * Description:
 */
public class OverLayImpl implements BgStart {
    private String TAG = "BgStart";
    private static final int TIME_DELAY = 600;
    private Handler mHhandler = new Handler();
    private CheckRunable mRunnable;
    public static int NOTIFY_FLAGS = 1000118;
    private static final String CHANNEL_ID = "12345";
    //通知管理器
    private NotificationManager nm;
    private PermissionServer mServer;

    public OverLayImpl(Context context) {
        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createUrgentNotificationChannel();
        mServer = new PermissionImpl();
    }

    private void createUrgentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            nm.createNotificationChannel(channel);
        }
    }

    @Override
    public void startActivity(final Activity context, final Intent intent, final String className) {
        if (context == null || intent == null || TextUtils.isEmpty(className)) {
            return;
        }
        if (!CustomActivityManager.isAppBackGround()) {
            //应用在前台时 直接 打开页面
            context.startActivity(intent);
            return;
        }
        if (Miui.isMIUI()) {
            //优先 使用 通知权限去打开 界面
            if (NotificationsUtils.isNotificationEnabled(context)) {
                //尝试 使用 notifycation 打开 界面
                notifyMiUi(context, intent);
                //检测 界面是否已经打开
                checkIntent(className, new ActivityCheckLisenter() {
                    @Override
                    public void startResult(boolean isSuc) {
                        // 使用通知 启动 页面失败
                        if (!isSuc) {
                            startMiuiByFloat(context, intent, className);
                        } else {
                            nm.cancel(NOTIFY_FLAGS);
                        }
                    }
                });
            } else {
                startMiuiByFloat(context, intent, className);
            }
        } else if (PermissionUtil.hasPermission(context)) {
            // 是否 获取了 悬浮窗权限
            context.startActivity(intent);
        } else {
            context.startActivity(intent);
            checkIntent(className, new ActivityCheckLisenter() {
                @Override
                public void startResult(boolean isSuc) {
                    if (!isSuc) {
                        Log.e(TAG, className + "androidQ 权限限制 从后台启动页面 请先允许【悬浮窗】 权限...");
                    }
                }
            });
        }

    }

    @Override
    public void requestStartPermisstion(final Activity activity, final PermissionLisenter listener) {
        PermissionLisenter li = new PermissionLisenter() {
            @Override
            public void onGranted() {
                BridgeBroadcast bridgeBroadcast = new BridgeBroadcast(listener);
                bridgeBroadcast.register(activity);
                Intent intent = new Intent(activity, BridgeActivity.class);
                activity.startActivityForResult(intent, SystemAlertWindow.REQUEST_OVERLY);
            }

            @Override
            public void cancel() {
                if (listener != null) {
                    listener.cancel();
                }
            }

            @Override
            public void onDenied() {

            }
        };
        mServer.checkPermisstion(activity, li);
    }

    private void startMiuiByFloat(Activity context, Intent intent, String className) {
        if (Miui.isAllowed(context)) {
            // 已经有 【后台启动页面】
            context.startActivity(intent);
        } else {
            Log.e(TAG, className + "页面启动失败，没有获取 【后台启动页面】 的权限...");
        }
    }

    /**
     * 使用notifycation 打开界面
     *
     * @param activity
     * @param notifyIntent
     */
    private void notifyMiUi(Activity activity, Intent notifyIntent) {
        //实例化通知栏构造器。
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(activity, CHANNEL_ID);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pedNotify = PendingIntent.getActivity(activity, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setFullScreenIntent(pedNotify, true);
        Notification notification = mBuilder.build();
        nm.notify(NOTIFY_FLAGS, notification);
    }

    /**
     * 检测 界面是否已经被打开
     *
     * @param mClassName
     * @param lisenter
     */

    private void checkIntent(String mClassName, ActivityCheckLisenter lisenter) {
        //TIME_DELAY  时间后 检测 是否打开页面
        if (mRunnable == null) {
            mRunnable = new CheckRunable(mClassName, lisenter);
        }
        if (mRunnable.isPostDelayIsRunning()) {
            mHhandler.removeCallbacks(mRunnable);
        }
        mRunnable.setPostDelayIsRunning(true);
        mHhandler.postDelayed(mRunnable, TIME_DELAY);
    }


}
