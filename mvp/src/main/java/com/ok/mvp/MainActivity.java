 package com.ok.mvp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ok.mvp.Interface.ILoginView;
import com.ok.mvp.bean.User;
import com.ok.mvp.presenter.LoginPresenter;

 public class MainActivity extends AppCompatActivity implements ILoginView{
     private EditText et_username;
     private EditText et_password;
     private Dialog dialog;
     private LoginPresenter presenter;
     private String username;
     private String password;

     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_username = (EditText) findViewById(R.id.et_username);
        et_password = (EditText) findViewById(R.id.et_password);
        dialog = new ProgressDialog(this);
        presenter = new LoginPresenter(this);
    }

     /**
      * 按钮点击
      * @param view
      */
     public void login(View view){
         username = et_username.getText().toString();
         password = et_password.getText().toString();
         User user = new User();
         user.username=username;
         user.password=password;
         boolean checkUser = presenter.checkUserInfo(user);
         if(checkUser){
             dialog.show();
             presenter.login(user);
         }else {
             Toast.makeText(MainActivity.this,"用户名或密码不能为空",Toast.LENGTH_SHORT).show();
         }
     }

     /**
      * 登录成功
      */
     @Override
     public void success(){
         runOnUiThread(new Runnable() {
             @Override
             public void run() {
                 //登录成功
                 dialog.dismiss();
                 Toast.makeText(MainActivity.this,"欢迎回来："+username,Toast.LENGTH_SHORT).show();
             }
         });
     }

     /**
      * 登录失败
      */
     @Override
     public void failed(){
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
