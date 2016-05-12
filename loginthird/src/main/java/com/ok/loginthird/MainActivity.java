package com.ok.loginthird;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button qq;
    private Button wx;
    private Button wb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        qq = (Button)findViewById(R.id.qq);
        wx = (Button)findViewById(R.id.wx);
        wb = (Button)findViewById(R.id.wb);

        qq.setOnClickListener(this);
        wx.setOnClickListener(this);
        wb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.qq:
                //跳转到QQ授权界面

                break;
            case R.id.wx:
                //跳转到WX授权界面

                break;
            case R.id.wb:
                //跳转到WB授权界面

                break;
        }
    }
}
