package com.ok.mvp.presenter;

import android.text.TextUtils;

import com.ok.mvp.Interface.ILoginView;
import com.ok.mvp.bean.User;
import com.ok.mvp.net.LoginNet;

/**
 * Created by Administrator on 2016/4/30.
 */
public class LoginPresenter {

    private ILoginView view;
    public LoginPresenter(ILoginView view){
        this.view = view;
    }
    /**
     * 判断用户输入
     * @param user
     * @return
     */
    public boolean checkUserInfo(User user){
        if(TextUtils.isEmpty(user.username)&&TextUtils.isEmpty(user.password)){
            return false;
        }else{
            return true;
        }
    }

    /**
     * 登录
     */
    public void login(final User user){
        new Thread(new Runnable() {
            @Override
            public void run() {
                LoginNet netLogin = new LoginNet();
                boolean isLoginSuceess = netLogin.sendNetLogin(user);
                if (isLoginSuceess){
                    //登录成功
                    view.success();
                }else{
                    //登录失败
                    view.failed();
                }
            }
        }).start();
    }
}
