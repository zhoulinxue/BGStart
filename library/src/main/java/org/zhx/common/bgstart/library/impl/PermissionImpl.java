package org.zhx.common.bgstart.library.impl;

import android.app.Activity;
import android.os.Build;

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
    private static final String MARK = Build.MANUFACTURER.toLowerCase();

    public PermissionImpl(ShowSource mSource) {
        this.mSource = mSource;
    }

    @Override
    public void checkPermisstion(final Activity activity, final PermissionLisenter lisenter, String... params) {
        if (mSource == null)
            if (Miui.isMIUI()) {
                mSource = new MiuiSource();
            } else {
                mSource = new FloatSource();
            }
        boolean isShowNotice = false;
        if ("oppo".equals(MARK)) {
            isShowNotice = true;
        }
        if (params != null && !isShowNotice) {
            for (String str : params) {
                if (MARK.equals(str)) {
                    isShowNotice = true;
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q || isShowNotice) {
            mSource.show(activity, lisenter);
        }
    }
}
