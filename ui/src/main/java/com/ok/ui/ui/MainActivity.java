package com.ok.ui.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ok.ui.R;
import com.ok.utils.bean.LiveUserInfo;
import com.ok.utils.eventbus.EventBusManager;
import com.ok.utils.net.http.HomeRequest;
import com.ok.utils.utils.log.LogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {
    private HomeRequest mHomeRequest ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBusManager.getInstance().register(this);
        setContentView(R.layout.activity_main);
        //--检测是否是最新版本
        mHomeRequest = new HomeRequest();
        mHomeRequest.reqUserInfo(48922554);



    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LiveUserInfo userInfo){
        if(userInfo.isSuccess()){
            LogUtils.d("userInfo.follow",userInfo.follow+"");
        }else {
            LogUtils.d("userInfo.follow","failed");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBusManager.getInstance().unregister(this);
    }
}
