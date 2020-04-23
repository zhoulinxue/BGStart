package org.zhx.common.bgstart.library.api;

/**
 * Copyright (C), 2015-2020
 * FileName: LifecycleListener2
 * Author: zx
 * Date: 2020/4/1 12:51
 * Description:
 */
public interface PermissionLisenter {
    void onGranted();

    void cancel();

    void onDenied();
}
