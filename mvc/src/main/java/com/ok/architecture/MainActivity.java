package com.ok.architecture;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ok.architecture.bean.User;
import com.ok.architecture.net.NetLogin;

public class MainActivity extends AppCompatActivity {

    private EditText et_username;
    private EditText et_password;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        dialog = new ProgressDialog(this);

    }

    /**
     * 按钮点击
     * @param view
     */
    public void login(View view){
        final String username = et_username.getText().toString();
        final String password = et_password.getText().toString();

        final User user = new User();
        user.username=username;
        user.password=password;
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
                                Toast.makeText(MainActivity.this,"欢迎回来："+username,Toast.LENGTH_SHORT).show();
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
