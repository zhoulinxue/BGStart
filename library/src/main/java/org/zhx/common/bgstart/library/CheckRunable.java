package org.zhx.common.bgstart.library;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;

import org.zhx.common.bgstart.library.api.ShowSource;
import org.zhx.common.bgstart.library.utils.Miui;
import org.zhx.common.bgstart.library.utils.PermissionUtil;

/**
 * Copyright (C), 2015-2020
 * FileName: CheckRunable
 * Author: zx
 * Date: 2020/4/17 14:40
 * Description:
 */
public class CheckRunable implements Runnable {
    private String mClassName;
    private Intent mIntent;
    private boolean mPostDelayIsRunning;
    private StartType mType;
    private ShowSource mSource;
    private Activity mContext;

    public CheckRunable(String mClassName, Intent mIntent, Activity mContext) {
        this.mClassName = mClassName;
        this.mIntent = mIntent;
        this.mContext = mContext;
    }

    @Override
    public void run() {
        mPostDelayIsRunning = false;
        // 判断要打开的Activity是不是已经在栈顶了
        if (!isActivityOnTop()) {
            //TODO 判断rom
//            if (Miui.isMIUI()) {
//                mSource = new NotifySource();
//            } else {
//                switch (mType) {
//                    case FLOAT_WINDOW:
//                        if (!PermissionUtil.hasPermission(mContext)) {
//                            mSource = new FloatSource();
//                        }
//                        break;
//                }
//            }

        }
    }

    private boolean isActivityOnTop() {
        boolean result = false;
        String topActivityName = CustomActivityManager.getTopActivity();
        if (!TextUtils.isEmpty(topActivityName)) {
            if (topActivityName.contains(mClassName)) {
                result = true;
            }
        }
        return result;
    }

    public void setPostDelayIsRunning(boolean postDelayIsRunning) {
        this.mPostDelayIsRunning = postDelayIsRunning;
    }

    public boolean isPostDelayIsRunning() {
        return mPostDelayIsRunning;
    }

}
