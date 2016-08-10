package com.itheima.animatortest;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.itheima.animatortest.boat.BoatActivity;
import com.itheima.animatortest.drawview.ViewActivity;
import com.itheima.animatortest.seat.SeatActivity;
import com.itheima.animatortest.thumb.ThumbActivity;
import com.ok.utils.utils.GoActivityUtil;

public class MainActivity extends Activity implements View.OnClickListener{

    private Button bt_bigimg;
    private Button bt_fresco;
    private Button bt_boat;
    private Button bt_thumb;
    private Button bt_img1;
    private Button bt_img2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_bigimg = (Button)findViewById(R.id.bt_flight_seat);
        bt_fresco = (Button)findViewById(R.id.bt_draw_graph);
        bt_boat = (Button)findViewById(R.id.bt_boat);
        bt_thumb = (Button)findViewById(R.id.bt_thumb);
        bt_img1 = (Button)findViewById(R.id.bt_img1);
        bt_img2 = (Button)findViewById(R.id.bt_img2);

        bt_bigimg.setOnClickListener(this);
        bt_fresco.setOnClickListener(this);
        bt_boat.setOnClickListener(this);
        bt_thumb.setOnClickListener(this);
        bt_img1.setOnClickListener(this);
        bt_img2.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_flight_seat:
                GoActivityUtil.goNormalActivity(this,SeatActivity.class);
                break;
            case R.id.bt_draw_graph:
                GoActivityUtil.goNormalActivity(this,ViewActivity.class);
                break;
            case R.id.bt_boat:
                GoActivityUtil.goNormalActivity(this,BoatActivity.class);
                break;
            case R.id.bt_thumb:
                GoActivityUtil.goNormalActivity(this,ThumbActivity.class);
                break;
            case R.id.bt_img1:

                break;
            case R.id.bt_img2:

                break;
        }
    }
}
