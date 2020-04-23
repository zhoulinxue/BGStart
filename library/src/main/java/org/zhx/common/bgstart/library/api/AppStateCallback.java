package org.zhx.common.bgstart.library.api;

import android.app.Activity;

/**
 * Copyright (C), 2015-2020
 * FileName: AppStateCallback
 * Author: zx
 * Date: 2020/4/17 11:18
 * Description:
 */
public interface AppStateCallback {
    public void onForground(Activity activity);

    public void onBackground(Activity activity);
}
