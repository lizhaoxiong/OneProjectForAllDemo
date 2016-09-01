package com.ok.ui.ui;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.ok.ui.R;
import com.ok.utils.utils.Colors;
import com.ok.utils.utils.DrawableUtils;

public class DrawActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);
        GradientDrawable gradientDrawable = DrawableUtils.generateDrawable(Colors.BLUE, 20);

        ImageView iv_drawable = (ImageView)findViewById(R.id.iv_drawable);
        if (iv_drawable != null) {
            iv_drawable.setImageDrawable(gradientDrawable);
        }
    }
}
