package org.zhx.common.bgstart.library;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.zhx.common.bgstart.library.api.AppStateCallback;
import org.zhx.common.bgstart.library.api.ShowSource;
import org.zhx.common.bgstart.library.impl.BgStart;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library
 * @ClassName: BgManager
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/6/28 10:27
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/28 10:27
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public class BgManager implements Application.ActivityLifecycleCallbacks {
    private int activityCount = 0;
    private String TAG = BgManager.class.getSimpleName();
    private static volatile BgManager manager = null;
    private AppStateCallback mCallback;
    private Application.ActivityLifecycleCallbacks mlifcycle;

    public static BgManager getInstance() {
        if (manager == null) {
            synchronized (BgManager.class) {
                if (manager == null) {
                    manager = new BgManager();
                }
            }
        }
        return manager;
    }

    /**
     * @param app
     */
    public void init(Application app) {
        init(app, null, null, null);
    }

    /**
     * app 前后台切换
     *
     * @param app
     * @param callback app 前后台切花
     */
    public void init(Application app, AppStateCallback callback) {
        init(app, callback, null);
    }

    /**
     * activity  生命周期 监听
     *
     * @param app
     * @param callback  activity 生命周期
     */
    public void init(Application app, Application.ActivityLifecycleCallbacks callback) {
        init(app, callback, null, null);
    }

    /**
     * @param app
     * @param lifecycleCallbacks activity 生命周期
     * @param showSource  自定义弹窗
     */
    public void init(Application app, Application.ActivityLifecycleCallbacks lifecycleCallbacks, ShowSource showSource) {
        init(app, lifecycleCallbacks, null, showSource);
    }

    /**
     * app 前后台切换  自定义弹窗
     *
     * @param app
     * @param callback 前后台切换
     * @param source 自定义弹窗
     */
    public void init(Application app, AppStateCallback callback, ShowSource source) {
        init(app, null, callback, source);
    }

    /**
     * app 自定义弹窗
     *
     * @param app
     * @param source 自定义 弹窗
     */
    public void init(Application app, ShowSource source) {
        init(app, null, null, source);
    }

    /**
     *
     * @param app
     * @param lifecycleCallbacks  activity 生命周期
     * @param callback app 前后台切换
     * @param mSource 自定义 弹窗
     */
    public void init(Application app, Application.ActivityLifecycleCallbacks lifecycleCallbacks, AppStateCallback callback, ShowSource mSource) {
        this.mlifcycle = lifecycleCallbacks;
        this.mCallback = callback;
        if (app != null) {
            if (isMainProcess(app)) {
                app.registerActivityLifecycleCallbacks(this);
                BgStart.getInstance().init(app, null, mSource);
            }
        }

    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        if (mlifcycle != null) {
            mlifcycle.onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        if (mlifcycle != null) {
            mlifcycle.onActivityStarted(activity);
        }
        activityCount++;
        if (activityCount == 1) {
            //TODO  前台
            if (mCallback != null) {
                mCallback.onForground(activity);
            }
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (mlifcycle != null) {
            mlifcycle.onActivityResumed(activity);
        }
        CustomActivityManager.setTopActivity(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        if (mlifcycle != null) {
            mlifcycle.onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        if (mlifcycle != null) {
            mlifcycle.onActivityStopped(activity);
        }
        activityCount--;
        if (activityCount <= 0) {
            //TODO 后台
            CustomActivityManager.clearTopActivity();
            if (mCallback != null) {
                mCallback.onBackground(activity);
            }
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        if (mlifcycle != null) {
            mlifcycle.onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (mlifcycle != null) {
            mlifcycle.onActivityDestroyed(activity);
        }
    }

    public boolean isMainProcess(Application application) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return application.getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }
}
