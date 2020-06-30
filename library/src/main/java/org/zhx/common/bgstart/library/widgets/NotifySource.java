package org.zhx.common.bgstart.library.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.zhx.common.bgstart.library.R;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.ShowSource;

/**
 * @ProjectName: BGStart
 * @Package: org.zhx.common.bgstart.library.widgets
 * @ClassName: NotifySource
 * @Description:java类作用描述
 * @Author: zhouxue
 * @CreateDate: 2020/6/28 10:47
 * @UpdateUser: 更新者
 * @UpdateDate: 2020/6/28 10:47
 * @UpdateRemark: 更新说明
 * @Version:1.0
 */
public class NotifySource implements ShowSource {
    @Override
    public void show(Activity context, final PermissionLisenter permissionListener) {
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.message_notify_failed)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO go setting page
                        if (permissionListener != null) {
                            permissionListener.onGranted();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (permissionListener != null) {
                            permissionListener.cancel();
                        }
                    }
                })
                .show();
    }
}
