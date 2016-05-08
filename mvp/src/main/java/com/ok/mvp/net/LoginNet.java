package com.ok.mvp.net;

import android.os.SystemClock;

import com.ok.mvp.bean.User;


/**
 * Created by Administrator on 2016/4/30.
 */
public class LoginNet {
    /**
     * 发送网络耗时操作
     */
    public boolean sendNetLogin(User user){
        SystemClock.sleep(2000);
        if("itcast".equals(user.username)&&"123456".equals(user.password)){
            return true;
        }else{
            return false;
        }
    }
}
