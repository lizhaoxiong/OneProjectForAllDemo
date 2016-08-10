package com.ok.ui.parcelable;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ok.ui.R;

public class SrcActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src2);

        startActivity(getIntent().putExtra("data",new User("小红",22)).setClass(this,DesActivity.class));
    }
}
