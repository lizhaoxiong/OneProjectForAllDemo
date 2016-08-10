package com.ok.ui.parcelable;

import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ok.ui.R;

public class DesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_des);

        Parcelable data = getIntent().getParcelableExtra("data");
        User user = (User)data;
        System.out.println(user);
    }
}
