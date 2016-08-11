package com.seven.notificationlistenerdemo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.service.notification.StatusBarNotification;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.notificationlistenerdemo.R;

public class MainActivity extends Activity {

    private static final String TAG = "SevenNLS";
    private static final String TAG_PRE = "["+MainActivity.class.getSimpleName()+"] ";
    private static final int EVENT_SHOW_CREATE_NOS = 0;
    private static final int EVENT_LIST_CURRENT_NOS = 1;
    private static final String ENABLED_NOTIFICATION_LISTENERS = "enabled_notification_listeners";
    private static final String ACTION_NOTIFICATION_LISTENER_SETTINGS = "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS";
    private boolean isEnabledNLS = false;
    private TextView mTextView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_SHOW_CREATE_NOS:
                    showCreateNotification();
                    break;
                case EVENT_LIST_CURRENT_NOS:
                    listCurrentNotification();
                    break;

                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);
    }

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
     * 判断通知权字典里面是否有该app
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
     * 2.1
     * @param view
     */
    public void buttonOnClicked(View view) {
        mTextView.setTextColor(Color.BLACK);
        switch (view.getId()) {
            case R.id.btnCreateNotify:
                logNLS("Create notifications...");
                createNotification(this);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_SHOW_CREATE_NOS), 50);
                break;
            case R.id.btnClearLastNotify:
                logNLS("Clear Last notification...");
                clearLastNotification();
                mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_LIST_CURRENT_NOS), 50);
                break;
            case R.id.btnClearAllNotify:
                logNLS("Clear All notifications...");
                clearAllNotifications();
                mHandler.sendMessageDelayed(mHandler.obtainMessage(EVENT_LIST_CURRENT_NOS), 50);
                break;
            case R.id.btnListNotify:
                logNLS("List notifications...");
                listCurrentNotification();
                break;
            case R.id.btnEnableUnEnableNotify:
                logNLS("Enable/UnEnable notification...");
                openNotificationAccess();
                break;
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
            ncBuilder.setContentTitle("内容资讯");
            /*设置详细文本*/
            ncBuilder.setContentText("Notification Listener Service Example");
            /*发出通知在status bar进行提醒*/
            ncBuilder.setTicker("消息刚来，显示到最上面");
            /*发出通知的时间*/
            ncBuilder.setWhen(System.currentTimeMillis());
            /*添加到常驻通知*/
            ncBuilder.setOngoing(false);
            /*设置点击后通知消息消失*/
            ncBuilder.setAutoCancel(true);
            /*设置通知数量*/
            ncBuilder.setNumber(10);
            /*设置点击后是否取消*/
            ncBuilder.setAutoCancel(true);
            /*设置点击的跳转意图*/
            ncBuilder.setContentIntent(pendingIntent);
        manager.notify((int)System.currentTimeMillis(),ncBuilder.build());
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


    private String getCurrentNotificationString() {
        String listNos = "";
        StatusBarNotification[] currentNos = NotificationMonitor.getCurrentNotifications();
        if (currentNos != null) {
            for (int i = 0; i < currentNos.length; i++) {
                listNos = i +" " + currentNos[i].getPackageName() + "\n" + listNos;
            }
        }
        return listNos;
    }


    private void listCurrentNotification() {
        String result = "";
        if (isEnabledNLS) {
            if (NotificationMonitor.getCurrentNotifications() == null) {
                logNLS("mCurrentNotifications.get(0) is null");
                return;
            }
            int n = NotificationMonitor.mCurrentNotificationsCounts;
            if (n == 0) {
                result = getResources().getString(R.string.active_notification_count_zero);
            }else {
                result = String.format(getResources().getQuantityString(R.plurals.active_notification_count_nonzero, n, n));
            }
            result = result + "\n" + getCurrentNotificationString();
            mTextView.setText(result);
        }else {
            mTextView.setTextColor(Color.RED);
            mTextView.setText("Please Enable Notification Access");
        }
    }

    private void clearLastNotification() {
        if (isEnabledNLS) {
            cancelNotification(this,false);
        }else {
            mTextView.setTextColor(Color.RED);
            mTextView.setText("Please Enable Notification Access");
        }
    }

    private void clearAllNotifications() {
        if (isEnabledNLS) {
            cancelNotification(this,true);
        }else {
            mTextView.setTextColor(Color.RED);
            mTextView.setText("Please Enable Notification Access");
        }
    }

    private void showCreateNotification() {
        if (NotificationMonitor.mPostedNotification != null) {
            String result = NotificationMonitor.mPostedNotification.getPackageName()+"\n"
                    + NotificationMonitor.mPostedNotification.getTag()+"\n"
                    + NotificationMonitor.mPostedNotification.getId()+"\n"+"\n"
                    + mTextView.getText();
            result = "Create notification:"+"\n"+result;
            mTextView.setText(result);
        }
    }


    /**
     * 1.2弹出开启通知使用权对话框
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
     * 1.3跳转到使用权设置页面
     */
    private void openNotificationAccess() {
        startActivity(new Intent(ACTION_NOTIFICATION_LISTENER_SETTINGS));
    }

    private void logNLS(Object object) {
        Log.i(TAG, TAG_PRE+object);
    }
}
