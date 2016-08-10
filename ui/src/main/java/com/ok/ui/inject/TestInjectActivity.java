package com.ok.ui.inject;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ok.ui.R;

public class TestInjectActivity extends AppCompatActivity {
    private static final String TAG = "TestInjectActivity";
    @ViewInject(R.id.tv1)
    private TextView tv1;
    @ViewInject(R.id.tv2)
    private TextView tv2;

    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_inject);
        MyViewUtils.inject(this);

        String stringTv1 = tv1.getText().toString();
        String stringTv2 = tv2.getText().toString();

        System.out.println(stringTv1+stringTv2);
    }

    @OnClick(R.id.bt)
    public void click(View view){
        Toast.makeText(this,"点击成功",Toast.LENGTH_SHORT).show();
    }
}
