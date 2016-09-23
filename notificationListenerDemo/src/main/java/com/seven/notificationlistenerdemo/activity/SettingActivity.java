package com.seven.notificationlistenerdemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;

import com.example.notificationlistenerdemo.R;
import com.seven.notificationlistenerdemo.service.NotificationMonitor;
import com.seven.notificationlistenerdemo.utils.AppUtils;
import com.seven.notificationlistenerdemo.utils.PreferencesUtils;
import com.seven.notificationlistenerdemo.view.recycleview.adapter.PackageAdapter;
import com.seven.notificationlistenerdemo.view.recycleview.style.DividerItemDecoration;
import com.seven.notificationlistenerdemo.view.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 列出所有的应用
 */
public class SettingActivity extends Activity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "SettingActivity";
    private RecyclerView mRecyclerView;
    private SwitchButton sb_setting_open;
    private PackageAdapter mAdapter;
    private ArrayList<AppUtils.AppInfo> mlistAppInfo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initData();
        sb_setting_open = (SwitchButton)findViewById(R.id.bt_setting_open);
        sb_setting_open.setOnCheckedChangeListener(this);//添加监听事件
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_setting_notification);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
        mRecyclerView.setAdapter(mAdapter = new PackageAdapter(this, mlistAppInfo));

        //item的点击事件改变CheckBox
        mAdapter.setOnItemClickLitener(new PackageAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                /**
                 * 1.获取被点击的isBlock的值改为！，tb的选中状态！
                 * 2.保存目前的状态到sp中
                 */
                PackageAdapter.PackageViewHolder viewHolder = (PackageAdapter.PackageViewHolder) view.getTag();
                AppUtils.AppInfo appInfo = mlistAppInfo.get(position);
                appInfo.setIsWhite(!appInfo.getIsWhite());
                viewHolder.tb.setChecked(!viewHolder.tb.isChecked());
                PreferencesUtils.putSharePref(SettingActivity.this,"white_table",appInfo.getPackagName(),appInfo.getIsWhite()?"0":"");
            }
            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private void initData() {
        if(mlistAppInfo==null){
            mlistAppInfo = new ArrayList<>();
        }
        mlistAppInfo = (ArrayList<AppUtils.AppInfo>) AppUtils.getAppInfos(this);
        //进入页面时，遍历集合，判断包是否在白名单中，在的话更新集合
        for(int i=0;i<mlistAppInfo.size();i++){
            String packagName = mlistAppInfo.get(i).getPackagName();
            String white_table = PreferencesUtils.getSharePrefStr(this, "white_table", packagName);
            if(white_table.equals("0")){
                //在白名单中
                mlistAppInfo.get(i).setIsWhite(true);
            }else{
                mlistAppInfo.get(i).setIsWhite(false);
            }
        }
        Collections.sort(mlistAppInfo, new Comparator<AppUtils.AppInfo>() {
            @Override
            public int compare(AppUtils.AppInfo lhs, AppUtils.AppInfo rhs) {
                int r ,l;
                if(rhs.getIsWhite()){
                    r = 1;
                }else{
                    r = 0;
                }
                if(lhs.getIsWhite()){
                    l = 1;
                }else{
                    l = 0;
                }
                return r-l;
            }
        });

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            openIncerption(this,true);
        }else{
            //关闭事件
            openIncerption(this,false);
        }
    }



    //开启事件，发送广播通知拦截开启
    private void openIncerption(Context context, boolean open) {
        Intent intent = new Intent();
        intent.setAction(NotificationMonitor.ACTION_NLS_CONTROL);
        intent.putExtra("Intercept",open);
        context.sendBroadcast(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
