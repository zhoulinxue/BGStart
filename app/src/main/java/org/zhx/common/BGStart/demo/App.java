package org.zhx.common.BGStart.demo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import org.zhx.common.bgstart.library.BgManager;
import org.zhx.common.bgstart.library.api.AppStateCallback;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.ShowSource;
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
//        BgManager.getInstance().init(this,activityLifecycleCallbacks);
        //如果你想监听 app 前后台 状态切换
//        BgManager.getInstance().init(this, new AppStateCallback() {
//            @Override
//            public void onForground(Activity activity) {
//
//            }
//
//            @Override
//            public void onBackground(Activity activity) {
//               //模拟 后台 启动页面  当 按下 home 建 1秒后 弹出 界面
//            }
//        });
        // 自定义   权限 弹出框
        BgManager.getInstance().init(this, new ShowSource() {
            @Override
            public void show(Activity context, final PermissionLisenter permissionListener) {
                // 弹出 权限提醒界面
                new AlertDialog.Builder(context).setCancelable(false)
                        .setTitle(org.zhx.common.bgstart.library.R.string.title_dialog)
                        .setMessage(org.zhx.common.bgstart.library.R.string.message_overlay_failed)
                        .setPositiveButton(org.zhx.common.bgstart.library.R.string.setting, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //TODO go setting page
                                if (permissionListener != null) {
                                    permissionListener.onGranted();
                                }
                            }
                        })
                        .setNegativeButton(org.zhx.common.bgstart.library.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (permissionListener != null)
                                    permissionListener.cancel();
                            }
                        })
                        .show();
            }
        });
    }
}
