package com.seven.notificationlistenerdemo.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.example.notificationlistenerdemo.R;
import com.seven.notificationlistenerdemo.service.NotificationMonitor;
import com.seven.notificationlistenerdemo.view.recycleview.style.DividerItemDecoration;
import com.seven.notificationlistenerdemo.view.recycleview.adapter.MsgAdapter;
import com.seven.notificationlistenerdemo.view.recycleview.bean.NotificationInfo;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private static final String TAG = "SevenNLS";
    private static final String TAG_PRE = "["+MainActivity.class.getSimpleName()+"] ";
    private static final int EVENT_SHOW_CREATE_NOS = 0;
    private static final int EVENT_LIST_CURRENT_NOS = 1;
    public static final int  UNIQUE_NOTIFICATION_ID = 1;
    private boolean isEnabledNLS = false;
    private RecyclerView mRecyclerView;
    private MsgAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateNotificationReceiver = new UpdateNotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NLS_SEND);
        registerReceiver(updateNotificationReceiver, filter);
        initList();
    }

    private void initList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_main_notification);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter = new MsgAdapter(this, mCacheNotificationInfo));
        mAdapter.setOnItemClickLitener(new MsgAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mCacheNotificationInfo==null){
                    return;
                }
                PendingIntent pendingIntent = mCacheNotificationInfo.get(position).pendingIntent;
                if(pendingIntent!=null){
                    try {
                        pendingIntent.send();
                        mAdapter.removeData(position);
                    } catch (PendingIntent.CanceledException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    public void buttonOnClicked(View view) {
        switch (view.getId()) {
            case R.id.btnCreateNotify:
                logNLS("Create notifications...");
                createNotification(this);
                break;
            case R.id.btnEnableUnEnableNotify:
                logNLS("Enable/UnEnable notification...");
                openNotificationAccess();
                break;
            case R.id.btnGotoSetting:
                startActivity(new Intent(this,SettingActivity.class));
            default:
                break;
        }
    }

    /**
     * 创建并设置本Notification
     * @param context
     */
    private void createNotification(Context context) {
        /**实例化NotificationManger获取系统服务*/
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        /**设置通知点击的跳转意图*/
        Intent resultIntent = new Intent(this,MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,resultIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        /**设置具体的通知 */
        NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(context);
            /*设置large icon*/
            ncBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_launcher));
            /*设置small icon*/
            ncBuilder.setSmallIcon(R.drawable.ic_launcher);
            /*设置title*/
            ncBuilder.setContentTitle("title");
            /*设置详细文本*/
            ncBuilder.setContentText("content");
            /*发出通知在status bar进行提醒*/
            ncBuilder.setTicker("消息刚来，显示到最上面");
            /*发出通知的时间*/
            ncBuilder.setWhen(System.currentTimeMillis());
            /*添加到常驻通知*/
            ncBuilder.setOngoing(true);
            /*设置点击后通知消息消失*/
            ncBuilder.setAutoCancel(true);
            /*设置通知数量*/
            ncBuilder.setNumber(10);
            /*设置点击后是否取消*/
            ncBuilder.setAutoCancel(false);
            /*设置点击的跳转意图*/
            ncBuilder.setContentIntent(pendingIntent);
        manager.notify(UNIQUE_NOTIFICATION_ID,ncBuilder.build());
    }

    /**
     * 发送广播通知NotificationServiceListener取消掉（某个还是全部）消息
     * @param context
     * @param isCancelAll
     */
    private void cancelNotification(Context context, boolean isCancelAll) {
        Intent intent = new Intent();
        intent.setAction(NotificationMonitor.ACTION_NLS_CONTROL);
        if (isCancelAll) {
            intent.putExtra("command", "cancel_all");
        }else {
            intent.putExtra("command", "cancel_last");
        }
        context.sendBroadcast(intent);
    }

    /*******************创建广播接受者，接受Service传递过来的数据*******************/
    public static ArrayList<NotificationInfo> mCacheNotificationInfo = new ArrayList<>();
    public static final int mMaxNotificationSize = 20;//存储最新的20条
    private UpdateNotificationReceiver updateNotificationReceiver;
    public static final String ACTION_NLS_SEND = "com.seven.notificationlistenerdemo.NLSSEND";//接受service传递来集合
    private class UpdateNotificationReceiver extends BroadcastReceiver {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action;
            if (intent != null && intent.getAction() != null) {
                action = intent.getAction();
                if (action.equals(ACTION_NLS_SEND)) {
                    int flag = intent.getIntExtra("flag", -1);
                    StatusBarNotification sbn = intent.getParcelableExtra("sbn");
                    if(sbn!=null){
                        NotificationInfo notificationInfo = new NotificationInfo();
                        Bundle extras = sbn.getNotification().extras;
                        notificationInfo.icon = extras.getParcelable(Notification.EXTRA_LARGE_ICON);
                        notificationInfo.title = extras.getString(Notification.EXTRA_TITLE);
                        notificationInfo.content = extras.getString(Notification.EXTRA_TEXT);
                        notificationInfo.time = System.currentTimeMillis()+"";
                        notificationInfo.pendingIntent = sbn.getNotification().contentIntent;
                        if(flag==NotificationMonitor.POSTED_FLAG){
                            if(mCacheNotificationInfo.size()>mMaxNotificationSize){
                                mCacheNotificationInfo.remove(mMaxNotificationSize-1);//移除最后一条
                            }
                            mCacheNotificationInfo.add(notificationInfo);
                            mAdapter.notifyDataSetChanged();
                        }else if(flag==NotificationMonitor.REMOVE_FLAG){
                            if(mCacheNotificationInfo.size()>=1){
                                mCacheNotificationInfo.remove(notificationInfo);
                            }
                        }else if(flag==-1){
                            logNLS("为什么没有获取到Flag");
                        }
                    }
                }
            }
        }
    }

    /******************* 获取通知权功能并开启Notification服务 *******************/
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";

    /**
     * 1.1当界面获取焦点时，判断是否有通知权
     */
    @Override
    protected void onResume() {
        super.onResume();
        isEnabledNLS = isEnabled();
        logNLS("isEnabledNLS = " + isEnabledNLS);
        if (!isEnabledNLS) {
            showConfirmDialog();
        }
    }

    /**
     * 1.2判断通知权字典里面是否有该app
     * @return
     */
    private boolean isEnabled() {
        String pkgName = getPackageName();
        final String flat = Settings.Secure.getString(getContentResolver(),
                ENABLED_NOTIFICATION_LISTENERS);
        if (!TextUtils.isEmpty(flat)) {
            final String[] names = flat.split(":");
            for (int i = 0; i < names.length; i++) {
                final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                if (cn != null) {
                    if (TextUtils.equals(pkgName, cn.getPackageName())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    /**
     * 1.3弹出开启通知使用权对话框
     */
    private void showConfirmDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Please enable NotificationMonitor access")
                .setTitle("Notification Access")
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setCancelable(true)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                openNotificationAccess();
                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // do nothing
                            }
                        })
                .create().show();
    }
    /**
     * 1.4跳转到使用权设置页面
     */
    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(updateNotificationReceiver!=null){
            unregisterReceiver(updateNotificationReceiver);
        }
    }
    private void logNLS(Object object) {
        Log.i(TAG, TAG_PRE+object);
    }
}
