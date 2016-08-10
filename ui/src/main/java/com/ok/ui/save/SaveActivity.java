package com.ok.ui.save;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ok.ui.R;

public class SaveActivity extends AppCompatActivity {
    private static final String TAG = "SaveActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        if(savedInstanceState!=null){
            String data = savedInstanceState.getString("data");
            Toast.makeText(this,data,Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause...");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop...");
    }

    /**
     * 该方法在onStop之前被调用
     * 应用场景：当进程处于后台进程，可能会被杀死(比如内存不足)，这种情况保存用户信息
     * 当用户恢复这个进程时，就可以在onCreate拿到保存的数据
     * 保存到哪儿：序列化到本地
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG,"onSaveInstanceState...");
        outState.putString("data","i love you");

    }
}
