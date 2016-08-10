package com.ok.image;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ok.fresco.FrescoActivity;
import com.ok.fresco.R;
import com.ok.utils.utils.GoActivityUtil;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button bt_bigimg;
    private Button bt_fresco;
    private Button bt_glide;
    private Button bt_imgLoader;
    private Button bt_img1;
    private Button bt_img2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt_bigimg = (Button)findViewById(R.id.bt_bigimg);
        bt_fresco = (Button)findViewById(R.id.bt_fresco);
        bt_glide = (Button)findViewById(R.id.bt_glide);
        bt_imgLoader = (Button)findViewById(R.id.bt_imgLoader);
        bt_img1 = (Button)findViewById(R.id.bt_img1);
        bt_img2 = (Button)findViewById(R.id.bt_img2);

        bt_bigimg.setOnClickListener(this);
        bt_fresco.setOnClickListener(this);
        bt_glide.setOnClickListener(this);
        bt_imgLoader.setOnClickListener(this);
        bt_img1.setOnClickListener(this);
        bt_img2.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_bigimg:
                //跳转到加载具图界面
                GoActivityUtil.goNormalActivity(this,ImageViewerActivity.class);
                break;
            case R.id.bt_fresco:
                GoActivityUtil.goNormalActivity(this,FrescoActivity.class);
                break;
            case R.id.bt_glide:


                break;
            case R.id.bt_imgLoader:


                break;
            case R.id.bt_img1:


                break;
            case R.id.bt_img2:

                break;
        }
    }
}
