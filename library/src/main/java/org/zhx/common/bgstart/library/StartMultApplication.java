package org.zhx.common.bgstart.library;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import org.zhx.common.bgstart.library.api.AppStateCallback;

/**
 * Copyright (C), 2015-2020
 * FileName: StartMultApplication
 * Author: zx
 * Date: 2020/4/17 11:03
 * Description:
 */
public abstract class StartMultApplication extends MultiDexApplication implements Application.ActivityLifecycleCallbacks, AppStateCallback {
    private int activityCount = 0;
    private String TAG = StartApplication.class.getSimpleName();
    private static StartMultApplication instance;
    public static StartMultApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        activityCount++;
        if (activityCount == 1) {
            //TODO  前台
            Log.e(TAG, "APP:forground    pkg: " + getPackageName());
            onForground(activity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        CustomActivityManager.setTopActivity(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        activityCount--;
        if (activityCount <= 0) {
            //TODO 后台
            CustomActivityManager.clearTopActivity();
            Log.e(TAG, "APP:onBackground    pkg: " + getPackageName());
            onBackground(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }

}
