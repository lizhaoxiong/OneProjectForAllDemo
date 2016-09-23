package com.seven.notificationlistenerdemo.service;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.seven.notificationlistenerdemo.activity.MainActivity;
import com.seven.notificationlistenerdemo.utils.PreferencesUtils;

/**
 *
 * Not to do:
 *  1.bug onNotificationPosted不走，重启后就可以走了，可能原因
 *    服务并没有开启，为什么服务没有开启，看onListenerConnected方法是否执行了
 *    以及开启服务的那个Intent没有执行
 *  2.
 */

public class NotificationMonitor extends NotificationListenerService {
    private static final String TAG = "NotificationMonitor";
    //和service通信字段
    public static final String ACTION_NLS_CONTROL = "com.seven.notificationlistenerdemo.NLSCONTROL";
    public static StatusBarNotification mPostedNotification;//新进来的一条msg
    public static StatusBarNotification mRemovedNotification;//移除的一条msg
    public static final int POSTED_FLAG=0;
    public static final int REMOVE_FLAG=1;
    public static Boolean Interception_Switch = true;//所有消息是否在通知栏显示（拦截吗）
    private CancelNotificationReceiver mReceiver = new CancelNotificationReceiver();

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NLS_CONTROL);
        registerReceiver(mReceiver, filter);
        onListenerConnected();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }


    /*************** notification变化回调并传递给界面 **************/
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG,"Class:NotificationMonitor,Method:onNotificationPosted,sbn.key---"+sbn.getKey());
        /** 1.当新消息发过来时，首先判断防拦截功能是否开启，如果没开启，不做任何处理 return */
        if(!Interception_Switch){
            return;
        }
        cancelNotificationBypkg(sbn);
    }

    private void cancelNotificationBypkg(StatusBarNotification sbn){
        /** 拿到该消息的包名，去匹配所有报名中开启拦截的包名集合。没有return */
        String white_table_value = PreferencesUtils.getSharePrefStr(this, "white_table", sbn.getPackageName());
        if(white_table_value!="0"){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                cancelNotification(sbn.getKey());
            }else{
                cancelNotification(sbn.getPackageName(),sbn.getTag(),sbn.getId());
            }
            //将POSTED_FLAG和sbn传递给Activity
            /** 3.如果有，那么将该条消息拦截，并且传递给Activity */
            updateCurrentNotifications(POSTED_FLAG,sbn);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        updateCurrentNotifications(REMOVE_FLAG, sbn);
    }

    /**
     * Post Remove 消息 广播传递给界面
     * @param flag
     * @param sbn
     */
    private void updateCurrentNotifications(int flag, StatusBarNotification sbn) {
        Intent intent = new Intent(MainActivity.ACTION_NLS_SEND);
        Bundle bundle = new Bundle();
        bundle.putInt("flag",flag);
        bundle.putParcelable("sbn",sbn);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }

    private class CancelNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent != null && intent.getAction() != null) {
                action = intent.getAction();
                if (action.equals(ACTION_NLS_CONTROL)) {
                    String command = intent.getStringExtra("command");
                    Boolean open = intent.getBooleanExtra("Intercept",true);
                    Interception_Switch = open;
                }
            }
        }
    }

    @Override
    public void onListenerConnected() {
        super.onListenerConnected();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver!=null){
            unregisterReceiver(mReceiver);
            mReceiver=null;
        }
    }

    /**
     * 开关防打扰功能
     *  1.当消息来时，判断防打扰功能是否开启
     *      1.1 如果开启了，该消息的应用包名是否在已开启拦截包名的集合中
     *          1.2 在，根据包名拦截，不在就不用管
     *  2.what i should do?
     *      2.1 包名的拦截的集合
     *      2.2 根据包名如何拦截方法
     *      2.3
     * @param sbn
     */

}
