package com.itheima.animatortest.boat;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.itheima.animatortest.R;

public class BoatActivity extends Activity {
    JufanBoatView boatView;
    RelativeLayout rl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boat);

        rl = (RelativeLayout)findViewById(R.id.rl);
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
