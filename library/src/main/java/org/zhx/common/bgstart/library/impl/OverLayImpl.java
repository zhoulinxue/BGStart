package org.zhx.common.bgstart.library.impl;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;

import org.zhx.common.bgstart.library.BridgeActivity;
import org.zhx.common.bgstart.library.BridgeBroadcast;
import org.zhx.common.bgstart.library.CheckRunable;
import org.zhx.common.bgstart.library.StartType;
import org.zhx.common.bgstart.library.SystemAlertWindow;
import org.zhx.common.bgstart.library.api.BgStart;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.utils.Miui;
import org.zhx.common.bgstart.library.utils.PermissionUtil;
import org.zhx.common.bgstart.library.widgets.MiuiSource;

/**
 * Copyright (C), 2015-2020
 * FileName: IBgStartImpl
 * Author: zx
 * Date: 2020/4/17 15:02
 * Description:
 */
public class OverLayImpl implements BgStart {
    private static final int TIME_DELAY = 600;
    private Handler mHhandler = new Handler();
    private CheckRunable mRunnable;

    @Override
    public void startActivity(Activity context, Intent intent, String className) {
        if (context == null || intent == null || TextUtils.isEmpty(className)) {
            return;
        }
        if (Miui.isMIUI()) {
            if (Miui.isAllowed(context)) {
                context.startActivity(intent);
            } else {
                //TODO custom notify
            }
        } else if (PermissionUtil.hasPermission(context)) {
            context.startActivity(intent);
        } else {
            context.startActivity(intent);
            //如果未打开界面  TIME_DELAY  时间后 弹出提示
            if (mRunnable == null) {
                mRunnable = new CheckRunable(className, intent, context);
            }
            if (mRunnable.isPostDelayIsRunning()) {
                mHhandler.removeCallbacks(mRunnable);
            }
            mRunnable.setPostDelayIsRunning(true);
            mHhandler.postDelayed(mRunnable, TIME_DELAY);
        }

    }

    @Override
    public void requestStartPermisstion(final Activity activity, final PermissionLisenter lisenter) {
        if (Miui.isMIUI()) {
            if (Miui.isAllowed(activity)) {
                if (lisenter != null) {
                    lisenter.onGranted();
                }
            } else {
                new MiuiSource().show(activity, new PermissionLisenter() {
                    @Override
                    public void onGranted() {
                        req(activity, lisenter);
                    }

                    @Override
                    public void cancel() {
                        if (lisenter != null) {
                            lisenter.cancel();
                        }
                    }

                    @Override
                    public void onDenied() {

                    }
                });
            }
        } else if (PermissionUtil.hasPermission(activity) && !Miui.isMIUI()) {
            if (lisenter != null) {
                lisenter.onGranted();
            }
        } else {
            req(activity, lisenter);
        }
    }

    @Override
    public void req(Activity activity, PermissionLisenter lisenter) {
        BridgeBroadcast bridgeBroadcast = new BridgeBroadcast(lisenter);
        bridgeBroadcast.register(activity);
        Intent intent = new Intent(activity, BridgeActivity.class);
        activity.startActivityForResult(intent, SystemAlertWindow.REQUEST_OVERLY);
    }
}
