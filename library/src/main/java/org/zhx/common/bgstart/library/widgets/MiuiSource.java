package org.zhx.common.bgstart.library.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.zhx.common.bgstart.library.R;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.ShowSource;

/**
 * Copyright (C), 2015-2020
 * FileName: MiuiSource
 * Author: zx
 * Date: 2020/4/17 15:16
 * Description:
 */
public class MiuiSource implements ShowSource {

    @Override
    public void show(final Activity context, final PermissionLisenter permissionListener) {
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.message_background_failed)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO go to setting page
                        if(permissionListener!=null){
                            permissionListener.onGranted();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (permissionListener != null)
                            permissionListener.cancel();
                    }
                }).show();
    }
}
