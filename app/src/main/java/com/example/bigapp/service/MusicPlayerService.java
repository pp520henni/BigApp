package com.example.bigapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bigapp.R;
import com.example.bigapp.activity.HomePageActivity;

public class MusicPlayerService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Memory", "onBind()");
        throw new UnsupportedOperationException("Not yet implemented");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Memory", "onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Memory", "onStartCommand()");
        int i = intent.getExtras().getInt("cmd");
        if(i == 0) {
            if(!isRemove) {
                Log.d("Memory", "开启");
                createNotification();
            }
            isRemove = true;
        } else {
            //移除前台服务
            if(isRemove) {
                Log.d("Memory", "关闭");
                stopForeground(true);
            }
            isRemove = false;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Memory", "onDestroy");
        if(isRemove) {
            stopForeground(true);
        }
        isRemove = false;
    }
    private static final int NOTIFICATION_DOWNLOAD_PROGRESS_ID = 0x0001;
    private boolean isRemove = false;
    public void createNotification() {
        String CHANNEL_ONE_ID = "com.primedu.cn";
        String CHANNEL_ONE_NAME = "ChannelOne";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.createNotificationChannel(notificationChannel);

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("www.baidu.com"));
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Notification notification = new Notification.Builder(this).setChannelId(CHANNEL_ONE_ID)
                    .setTicker("123")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("我的标题")
                    .setContentText("我的内容")
                    .build();
            notification.flags |= Notification.FLAG_NO_CLEAR;
            startForeground(NOTIFICATION_DOWNLOAD_PROGRESS_ID, notification);
        }
    }
}
