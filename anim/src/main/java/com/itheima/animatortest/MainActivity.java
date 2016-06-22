package com.itheima.animatortest;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewAnimator;

public class MainActivity extends Activity {
    JufanBoatView boatView;
    RelativeLayout rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rl = (RelativeLayout)findViewById(R.id.rl);

        new ViewGroup(this){

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {

            }
        };


        View view = new View(this);

        ViewGroup group =(ViewGroup) view.getParent();
        ViewParent parent = (ViewParent) view.getParent();


        Toast.makeText(this,"你好",Toast.LENGTH_SHORT).show();


    }
    public void click(View v) {
        boatView = new JufanBoatView(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rl.addView(boatView,params);
        mHandler.sendEmptyMessageDelayed(2000,500);
    }

    /** 控制动画执行 */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 2000:
                    boatView.shipAnimatorSet();
                    break;
            }
        }
    };
}
