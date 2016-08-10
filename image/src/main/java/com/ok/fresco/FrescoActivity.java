package com.ok.fresco;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class FrescoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);//这一步要在加载布局前做
        setContentView(R.layout.activity_fresco);

        Uri uri = Uri.parse("http://k.sinaimg.cn/n/sports/transform/20160807/p0tn-fxutfpf1431637.jpg/w570a4e.jpg");
        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
        draweeView.setImageURI(uri);

    }
}
