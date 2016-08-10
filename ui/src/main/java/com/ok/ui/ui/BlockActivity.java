package com.ok.ui.ui;

import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ok.ui.R;

public class BlockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block);


    }

    public void imitateClick(View v){
        Toast.makeText(this,"ffffff",Toast.LENGTH_SHORT).show();
        SystemClock.sleep(3000);
    }

    public void memoryClick(View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                for(;;){

                }
            }
        }).start();
    }

}
