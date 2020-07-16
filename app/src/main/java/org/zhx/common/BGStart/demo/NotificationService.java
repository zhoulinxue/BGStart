package org.zhx.common.BGStart.demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import org.zhx.common.bgstart.library.utils.NotificationsUtils;

import static android.util.TypedValue.COMPLEX_UNIT_SP;

/**
 * pakage :com.gaea.kiki.service
 * auther :zx
 * creatTime: 2019/3/18
 */
public class NotificationService extends Service {
    public static String MESSAGE = "notifycation_message";
    public static String EXTRAS = "notifycaton_extras";
    public static final String ACTIONS = "notifycation_action";
    //通知管理器
    private NotificationManager nm;
    public static int NOTIFY_FLAGS = 1000118;
    private static final String CHANNEL_ID = "12345";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createUrgentNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("NotificationService", "onStartCommand");
        String action = intent.getAction();
        systemStyle(NotificationService.this, "", NOTIFY_FLAGS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            stopSelf();
//            stopForeground(STOP_FOREGROUND_REMOVE);
//        } else {
//            nm.cancel(NOTIFY_FLAGS);
//        }
        return START_NOT_STICKY;
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
        mBuilder.setDefaults(Notification.DEFAULT_ALL);
        mBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
        //显示在通知栏上的小图标
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setWhen(System.currentTimeMillis());
        Intent notifyIntent = new Intent(this, TargetActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pedNotify = PendingIntent.getActivity(this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pedNotify);//点击通知栏后的意图
        mBuilder.setAutoCancel(false);//设置这个标志当用户单击面板就可以让通知将自动取消
        //设置为不可清除模式
        mBuilder.setOngoing(true);
        //显示通知，id必须不重复，否则新的通知会覆盖旧的通知（利用这一特性，可以对通知进行更新）
        Notification notification = mBuilder.build();
        notification.contentView = remoteViews;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(id, notification);
        } else {
            nm.notify(id, notification);
        }
    }

}
