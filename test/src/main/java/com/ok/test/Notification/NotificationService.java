package com.ok.test.Notification;

import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

public class NotificationService extends NotificationListenerService  {

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        //通知栏来消息的回调
        Log.i("SevenNLS","Notification posted");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        //通知栏消息移除的回调
        Log.i("SevenNLS","Notification removed");
    }
}
