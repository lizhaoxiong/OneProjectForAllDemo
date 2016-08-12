package com.seven.notificationlistenerdemo;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.notificationlistenerdemo.R;

import java.util.ArrayList;
import java.util.List;


/**
 * 列出所有的应用
 */
public class SettingActivity extends Activity {

    ListView lv;
    ArrayList<AppInfo> list = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        lv = (ListView) findViewById(R.id.lv_package);
        lv.setAdapter(new myAdapter(list));

        
    }

    private class myAdapter extends BaseAdapter {
        ArrayList<AppInfo> list;
        public myAdapter(ArrayList list){
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return null;
        }
    }
}
