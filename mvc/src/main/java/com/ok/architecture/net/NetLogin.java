package com.ok.architecture.net;

import android.os.SystemClock;
import android.util.Log;

import com.ok.architecture.bean.User;

/**
 * Created by Administrator on 2016/4/30.
 */
public class NetLogin {
    private static final String TAG = "NetLogin";
    /**
     * 发送网络耗时操作
     */
    public boolean sendNetLogin(User user){
        Log.d(TAG,"sendNetLogin");
        SystemClock.sleep(2000);
        if("itcast".equals(user.username)&&"123456".equals(user.password)){
            return true;
        }else{
            return false;
        }

    }
}
