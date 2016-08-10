package com.ok.ui;

import android.app.Application;

import com.github.moduth.blockcanary.BlockCanary;
import com.ok.ui.http.HeadHttpConfig;
import com.ok.utils.app.BaseApplication;
import com.ok.utils.eventbus.EventBusManager;
import com.ok.utils.http.VolleyManager;
import com.ok.utils.net.http.NetManager;
import com.ok.utils.utils.PhoneUtils;
import com.ok.utils.utils.log.LogUtils;
import com.squareup.leakcanary.LeakCanary;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description app层Application类
 * @modify
 */
public class MyApplication extends BaseApplication{
    private static final String TAG = "MyApplication";
    private static MyApplication mInstance;

    public MyApplication() {
        super();
    }

    @Override
    public void onCreate() {
        mInstance = this;
        super.onCreate();
        //初始化配置信息
        BlockCanary.install(this, new AppBlockCanaryContext()).start();
        LeakCanary.install(this);
        deviceType = PhoneUtils.checkPhoneOrTablet(this);
        initialize(this);
        NetManager.getInstance().setHttpConfig(new HeadHttpConfig());
        LogUtils.d(TAG, "onCreate(),getDeviceInfo():" + PhoneUtils.getDeviceInfo(this));
    }

    public synchronized void initialize(Application context) {
        VolleyManager.initialize(context);
        EventBusManager.getInstance().registerDefault();
        //Fresco.initialize(this);
    }


}
