package org.zhx.common.bgstart.library;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author LongpingZou
 * @date 2019/3/11
 */
public final class BridgeActivity extends Activity {
    public static final String PERMISSION = "has_permission";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemAlertWindow page = new SystemAlertWindow(this);
        page.start(SystemAlertWindow.REQUEST_OVERLY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SystemAlertWindow.REQUEST_OVERLY == requestCode) {
            if (PermissionUtil.hasPermissionOnActivityResult(this)
                    || (Miui.isMIUI() && Miui.isAllowed(this))) {
                Intent intent = new Intent(BridgeBroadcast.SUC);
                sendBroadcast(intent);
            } else {
                Intent intent = new Intent(BridgeBroadcast.FAIL);
                sendBroadcast(intent);
            }
        }
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}