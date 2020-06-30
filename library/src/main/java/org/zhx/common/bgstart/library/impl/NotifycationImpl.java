package org.zhx.common.bgstart.library.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import org.zhx.common.bgstart.library.NotifySource;
import org.zhx.common.bgstart.library.api.BgStart;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.utils.NotificationsUtils;
import org.zhx.common.bgstart.library.utils.PermissionUtil;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library
 * @ClassName: NotifaycationImpl
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/6/28 10:41
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/28 10:41
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public class NotifycationImpl implements BgStart {

    @Override
    public void startActivity(Activity context, Intent intent, String className) {
        //TODO  show  notifycation  View
    }

    @Override
    public void requestStartPermisstion(final Activity context, final PermissionLisenter lisenter) {
        if (NotificationsUtils.isNotificationEnabled(context)) {
            if (lisenter != null) {
                lisenter.onGranted();
            }
        } else {
            new NotifySource().show(context, new PermissionLisenter() {
                @Override
                public void onGranted() {
                    req(context, lisenter);
                }

                @Override
                public void cancel() {
                    if (lisenter != null) {
                        lisenter.cancel();
                    }
                }

                @Override
                public void onDenied() {

                }
            });
        }
    }

    @Override
    public void req(Activity activity, PermissionLisenter lisenter) {
        NotificationsUtils.setNotifycationEnable(activity);
    }
}
