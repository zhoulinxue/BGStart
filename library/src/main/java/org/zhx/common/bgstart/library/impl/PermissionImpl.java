package org.zhx.common.bgstart.library.impl;

import android.app.Activity;

import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.PermissionServer;
import org.zhx.common.bgstart.library.api.ShowSource;
import org.zhx.common.bgstart.library.utils.Miui;
import org.zhx.common.bgstart.library.utils.PermissionUtil;
import org.zhx.common.bgstart.library.widgets.MiuiSource;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library.impl
 * @ClassName: PermissionImpl
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/7/16 10:02
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/7/16 10:02
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public class PermissionImpl implements PermissionServer {
    private ShowSource mSource;

    @Override
    public void checkPermisstion(final Activity activity, final PermissionLisenter lisenter) {
        if (Miui.isMIUI()) {
            if (Miui.isAllowed(activity)) {
                if (lisenter != null) {
                    lisenter.onGranted();
                }
            } else {
                mSource = new MiuiSource();
            }
        } else if (PermissionUtil.hasPermission(activity)) {
            if (lisenter != null) {
                lisenter.onGranted();
            }
        } else {
            mSource = new FloatSource();
        }
        mSource.show(activity, lisenter);
    }
}
