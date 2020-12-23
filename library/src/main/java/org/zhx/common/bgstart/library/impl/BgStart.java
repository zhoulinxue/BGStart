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
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.PermissionServer;
import org.zhx.common.bgstart.library.api.ShowSource;
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
public class BgStart {
    private String TAG = "BgStart";
    private static final int TIME_DELAY = 5000;
    private Handler mHhandler = new Handler();
    private CheckRunable mRunnable;
    public static int NOTIFY_FLAGS = 1000118;
    public static final String CHANNEL_ID = "12345";
    //通知管理器
    private NotificationManager nm;
    private PermissionServer mServer;
    private static BgStart overLay = new BgStart();
    private NotificationCompat.Builder mBuilder;
    private ShowSource mSource;


    public static BgStart getInstance() {
        return overLay;
    }

    public void init(Context context, NotificationCompat.Builder builder, ShowSource mSource) {
        this.mBuilder = builder;
        if (nm == null) {
            nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            createUrgentNotificationChannel();
            mServer = new PermissionImpl(mSource);
        }
    }

    private void createUrgentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "app", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            nm.createNotificationChannel(channel);
        }
    }

    public void startActivity(final Context context, final Intent intent, final String className) {
        if (context == null || intent == null || TextUtils.isEmpty(className)) {
            return;
        }
        if (!CustomActivityManager.isAppBackGround()) {
            //应用在前台时 直接 打开页面
            context.startActivity(intent);
            Log.e(TAG, "前台_跳转成功");
            return;
        }
        if (Miui.isMIUI()) {
            //优先 使用 通知权限去打开 界面
            if (NotificationsUtils.isNotificationEnabled(context) && mBuilder != null) {
                //尝试 使用 notifycation 打开 界面
                Log.e(TAG, "通知_跳转");
                notifyMiUi(context, intent);
                //检测 界面是否已经打开
                checkIntent(className, new ActivityCheckLisenter() {
                    @Override
                    public void startResult(boolean isSuc) {
                        // 使用通知 启动 页面失败
                        if (!isSuc) {
                            startMiuiByFloat(context, intent, className, false);
                        } else {
                            Log.e(TAG, "notify_跳转成功");
                            nm.cancel(NOTIFY_FLAGS);
                        }
                    }
                });
            } else {
                startMiuiByFloat(context, intent, className, true);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (PermissionUtil.hasPermission(context)) {
                    // 是否 获取了 悬浮窗权限
                    context.startActivity(intent);
                    Log.e(TAG, "悬浮窗权限_跳转成功 " + System.currentTimeMillis());
                } else {
                    nomalCheck(context, intent, className);
                }
            } else {
                nomalCheck(context, intent, className);
            }
        }
    }

    private void nomalCheck(Context context, Intent intent, final String className) {
        context.startActivity(intent);
        Log.e(TAG, "普通_跳转成功 " + System.currentTimeMillis());
        checkIntent(className, new ActivityCheckLisenter() {
            @Override
            public void startResult(boolean isSuc) {
                if (!isSuc) {
                    Log.e(TAG, className + "   androidQ 权限限制 从后台启动页面 请先允许【悬浮窗】 权限...");
                }
            }
        });
    }

    public void requestStartPermisstion(final Activity activity, final PermissionLisenter listener, String... params) {
        PermissionLisenter li = new PermissionLisenter() {
            @Override
            public void onGranted() {
                if (Miui.isMIUI()) {
                    PermissionUtil.jumpToPermissionsEditorActivity(activity);
                } else {
                    BridgeBroadcast bridgeBroadcast = new BridgeBroadcast(listener);
                    bridgeBroadcast.register(activity);
                    Intent intent = new Intent(activity, BridgeActivity.class);
                    activity.startActivityForResult(intent, SystemAlertWindow.REQUEST_OVERLY);
                }
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
        if (Miui.isMIUI()) {
            if (Miui.isAllowed(activity)) {
                if (listener != null) {
                    listener.onGranted();
                }
            } else {
                mServer.checkPermisstion(activity, li, params);
            }
        } else if (PermissionUtil.hasPermission(activity)) {
            if (listener != null) {
                listener.onGranted();
            }
        } else {
            mServer.checkPermisstion(activity, li, params);
        }
    }

    /**
     * 新增权限检测
     * @param context
     * @return
     */
    public boolean hasBgStartPermission(Context context) {
        if (Miui.isMIUI()) {
            return Miui.isAllowed(context);
        } else {
            return PermissionUtil.hasPermission(context);
        }
    }


    private void startMiuiByFloat(final Context context, Intent intent, String className, boolean needCheck) {
        if (Miui.isAllowed(context)) {
            // 已经有 【后台启动页面】
            context.startActivity(intent);
            if (needCheck) {
                checkIntent(className, new ActivityCheckLisenter() {
                    @Override
                    public void startResult(boolean isSuc) {
                        if (isSuc) {
                            Log.e(TAG, "Miui_跳转成功 " + System.currentTimeMillis());
                        } else {
                            if (!PermissionUtil.hasPermission(context)) {
                                Log.e(TAG, "Miui_跳转失败, 没有获取 【悬浮窗】 的权限");
                            }
                        }
                    }
                });
            }
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
    private void notifyMiUi(Context activity, Intent notifyIntent) {
        if (mBuilder != null) {
            //实例化通知栏构造器。
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pedNotify = PendingIntent.getActivity(activity, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setFullScreenIntent(pedNotify, true);
            Notification notification = mBuilder.build();
            nm.notify(NOTIFY_FLAGS, notification);
        }
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
        Log.e(TAG, "开始计时  " + System.currentTimeMillis());
        mHhandler.postDelayed(mRunnable, TIME_DELAY);
    }


}
