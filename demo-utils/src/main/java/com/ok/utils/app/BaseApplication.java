package com.ok.utils.app;

import android.app.Application;
import android.os.Handler;

import com.ok.utils.utils.AppUtils;
import com.ok.utils.utils.PhoneUtils;
import com.ok.utils.utils.log.LogUtils;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */
public class BaseApplication extends Application implements DefaultExceptionHandler.OnReceiveErrorListener{
    private static final String TAG = "BaseApplication";

    //联网类型
    public static final String NET_TYPE_WIFI = "wifi";
    public static final String NET_TYPE_3G = "3g";
    public static final String NET_TYPE_NONE="none";

    private static BaseApplication mInstance = null;

    public static String channel;
    public static String version;
    public static String IMEI;
    public static String netType;
    public static String language;
    public static int OEMID = 25;
    //1.手机 2.平板
    public static int deviceType;
    public String mainThreadId;
    //是否执行完初始化
    private boolean finishedInit = false;
    private Handler mHandler;

    public static BaseApplication getInstance() {
        return mInstance;
    }

    protected BaseApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mHandler = new Handler();
        finishedInit = false;
        mainThreadId = Thread.currentThread().toString();
        new InitThread().start();

        setUncaughtExceptionHandler(new DefaultExceptionHandler(this));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 子类可以重载次方法
     */
    protected void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }
    private class InitThread extends Thread {
        @Override
        public void run() {
            init();

            finishedInit = true;
        }
    }

    /**
     * 子类可以重载，进行额外的初始化
     */
    protected void init() {
        channel = AppUtils.getChannel(getApplicationContext(), "channel") + "";
        version = AppUtils.getVersionName(getApplicationContext());
        IMEI = PhoneUtils.getIMEI(BaseApplication.getInstance());// IMEI码
        //initNetType();
        language = getResources().getConfiguration().locale.getCountry(); // 返回值是语言的代码，比如中文就“zh”
    }

    public boolean finishedInit() {
        return this.finishedInit;
    }

    @Override
    public void onReceiveError(Throwable ex) {
        //MobclickAgent.reportError(this, ex);
        exitApp();
    }

    /**
     * 退出程序
     */
    public void exitApp() {
        LogUtils.stopFileLogger();
        ActivityStack.finishProgram();
        //MobclickAgent.onKillProcess(this);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(0);
            }
        }, 200);
    }

}
