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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createUrgentNotificationChannel();
        setContentView(R.layout.activity_main);
        BgStart bgStart = new OverLayImpl();
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
                systemStyle(MainActivity.this, "", NOTIFY_FLAGS);
                startService(new Intent(MainActivity.this, NotificationService.class));
                Intent intent = new Intent(MainActivity.this, B.class);
                new OverLayImpl().startActivity(MainActivity.this, intent, B.class.getName());
            }
        }, 1000);
    }

    private void createUrgentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setLightColor(Color.RED);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            nm.createNotificationChannel(channel);
        }
    }

    public void systemStyle(Context context, String content, int id) {
        boolean darkNotiFicationBar = NotificationsUtils.isDarkNotiFicationBar(context);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notify_message_large);
        //设置通知栏背景
        remoteViews.setTextColor(R.id.appNameTextView, darkNotiFicationBar == true ? Color.WHITE : Color.BLACK);
        remoteViews.setTextColor(R.id.content_TextView, darkNotiFicationBar == true ? Color.WHITE : Color.BLACK);
        remoteViews.setTextColor(R.id.content_time, darkNotiFicationBar == true ? Color.WHITE : Color.BLACK);

        remoteViews.setTextViewTextSize(R.id.content_TextView, COMPLEX_UNIT_SP, 14);
        remoteViews.setTextViewTextSize(R.id.content_time, COMPLEX_UNIT_SP, 14);
        remoteViews.setTextViewTextSize(R.id.title, COMPLEX_UNIT_SP, 16);
        //添加内容
        remoteViews.setTextViewText(R.id.content_TextView, "房间名 【" + content + "】");
        remoteViews.setImageViewResource(R.id.ImageView, R.mipmap.ic_launcher);
        remoteViews.setTextViewText(R.id.title, "正在房间中");
        remoteViews.setTextViewText(R.id.content_time, "");
        //实例化通知栏构造器。
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        //系统收到通知时，通知栏上面显示的文字。
        mBuilder.setTicker("正在房间中");
//        mBuilder.setDefaults(Notification.DEFAULT_ALL);
//        mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        //显示在通知栏上的小图标
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        Intent notifyIntent = new Intent(this, B.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pedNotify = PendingIntent.getActivity(this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setFullScreenIntent(pedNotify, true);
//        mBuilder.setContentIntent(pedNotify);//点击通知栏后的意图
        mBuilder.setAutoCancel(false);//设置这个标志当用户单击面板就可以让通知将自动取消
        //设置为不可清除模式
        mBuilder.setOngoing(true);
        //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
        Notification notification = mBuilder.build();
        notification.contentView = remoteViews;
        nm.notify(id, notification);
    }
}
