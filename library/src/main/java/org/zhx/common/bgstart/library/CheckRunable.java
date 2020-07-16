package org.zhx.common.bgstart.library;

import android.text.TextUtils;
import android.util.Log;

import org.zhx.common.bgstart.library.api.ActivityCheckLisenter;

/**
 * Copyright (C), 2015-2020
 * FileName: CheckRunable
 * Author: zx
 * Date: 2020/4/17 14:40
 * Description:
 */
public class CheckRunable implements Runnable {
    private String mClassName;
    private boolean mPostDelayIsRunning;
    private ActivityCheckLisenter mLisenter;

    public CheckRunable( String mClassName,ActivityCheckLisenter lisenter) {
        this.mClassName = mClassName;
        this.mLisenter = lisenter;
    }

    @Override
    public void run() {
        mPostDelayIsRunning = false;
        // 判断要打开的Activity是不是已经在栈顶了
        if (mLisenter != null) {
            mLisenter.startResult(isActivityOnTop());
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
        Log.e("BgStart","isActivityOnTop  "+System.currentTimeMillis());
        return result;
    }

    public void setPostDelayIsRunning(boolean postDelayIsRunning) {
        this.mPostDelayIsRunning = postDelayIsRunning;
    }

    public boolean isPostDelayIsRunning() {
        return mPostDelayIsRunning;
    }

}
