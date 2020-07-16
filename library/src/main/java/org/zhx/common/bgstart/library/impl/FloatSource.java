package org.zhx.common.bgstart.library.impl;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import org.zhx.common.bgstart.library.R;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.api.ShowSource;

/**
 * Copyright (C), 2015-2020
 * FileName: FloatSource
 * Author: zx
 * Date: 2020/4/17 15:17
 * Description:
 */
public class FloatSource implements ShowSource {

    @Override
    public void show(Activity context, final PermissionLisenter permissionListener) {
        new AlertDialog.Builder(context).setCancelable(false)
                .setTitle(R.string.title_dialog)
                .setMessage(R.string.message_overlay_failed)
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO go setting page
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
                })
                .show();
    }
}
