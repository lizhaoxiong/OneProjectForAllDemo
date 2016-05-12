package com.ok.loginthird;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class CodeActivity extends AppCompatActivity {

    private EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        et_code = (EditText)findViewById(R.id.et_code);
    }
}
