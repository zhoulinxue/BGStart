package org.zhx.common.BGStart.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.RemoteViews;

import org.zhx.common.bgstart.library.impl.OverLayImpl;
import org.zhx.common.bgstart.library.utils.Miui;
import org.zhx.common.bgstart.library.api.BgStart;
import org.zhx.common.bgstart.library.api.PermissionLisenter;
import org.zhx.common.bgstart.library.utils.NotificationsUtils;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

public class MainActivity extends AppCompatActivity {
    private String TAG = MainActivity.class.getSimpleName();
    //通知管理器
    private NotificationManager nm;
    public static int NOTIFY_FLAGS = 1000118;
    private static final String CHANNEL_ID = "12345";
    private BgStart bgStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bgStart = new OverLayImpl(this);
        Log.e("RAG", Miui.isAllowed(this) + "");
        bgStart.requestStartPermisstion(this, new PermissionLisenter() {
            @Override
            public void onGranted() {
                Log.e(TAG, "onGranted");
            }

            @Override
            public void cancel() {
                Log.e(TAG, "cancel");
            }

            @Override
            public void onDenied() {
                Log.e(TAG, "onDenied");
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //模拟 后台 启动页面  当 按下 home 建 1秒后 弹出 界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, B.class);
                bgStart.startActivity(MainActivity.this, intent, B.class.getName());
            }
        }, 1000);
    }


}
