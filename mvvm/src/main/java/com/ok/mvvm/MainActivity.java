package com.ok.mvvm;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.ok.mvvm.bean.User;
import com.ok.mvvm.databinding.ActivityMainBinding;
import com.ok.mvvm.event.UserEvent;
import com.ok.mvvm.net.NetLogin;

public class MainActivity extends AppCompatActivity {

    private Dialog dialog;
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        user = new User();
        UserEvent event = new UserEvent(user);
        binding.setEvent(event);
        dialog = new ProgressDialog(this);
    }

    /**
     * 按钮点击
     * @param view
     */
    public void login(View view){
        boolean userInfo= checkUserInfo(user);
        if(userInfo==true){
            dialog.show();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    NetLogin netLogin = new NetLogin();
                    boolean isLoginSuceess = netLogin.sendNetLogin(user);
                    if(isLoginSuceess){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //登录成功
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,"欢迎回来："+user.username,Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //登录失败
                                dialog.dismiss();
                                Toast.makeText(MainActivity.this,"用户名密码错误",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
        }else{
            Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 判断用户输入
     * @param user
     * @return
     */
    private boolean checkUserInfo(User user){
        if(TextUtils.isEmpty(user.username)&&TextUtils.isEmpty(user.password)){
            return false;
        }else{
            return true;
        }
    }
}
