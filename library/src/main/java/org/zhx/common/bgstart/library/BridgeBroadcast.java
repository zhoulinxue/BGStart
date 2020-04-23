package org.zhx.common.bgstart.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import org.zhx.common.bgstart.library.api.PermissionLisenter;

/**
 * Copyright (C), 2015-2020
 * FileName: BridgeBroadcast
 * Author: zx
 * Date: 2020/4/20 10:53
 * Description:
 */
public class BridgeBroadcast extends BroadcastReceiver {
    private static final String ACTION = "org.zhx.permission.bridge";
    private PermissionLisenter mLisenter;
    public static final String SUC = "allowed_permisstion";
    public static final String FAIL = "fail_permisstion";


    public BridgeBroadcast(PermissionLisenter mLisenter) {
        this.mLisenter = mLisenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getAction())) {
            switch (intent.getAction()) {
                case SUC:
                    mLisenter.onGranted();
                    unRegister(context);
                    break;
                case FAIL:
                    mLisenter.onDenied();
                    unRegister(context);
                    break;
            }
        }
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(ACTION);
        filter.addAction(SUC);
        filter.addAction(FAIL);
        context.registerReceiver(this, filter);
    }

    public void unRegister(Context context) {
        context.unregisterReceiver(this);
    }
}
