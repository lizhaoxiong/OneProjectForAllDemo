package com.itheima.animatortest.drawview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author li zhaoxiong
 * @version 1.0.0
 * @description
 * @modify
 */
public class CircleView extends View {
    private Context context;
    private Paint paint;

    public CircleView(Context context) {
        this(context,null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {

        this.paint = new Paint();

    }

    @Override
    protected void onDraw(Canvas canvas) {

        int centerX = getWidth()/2;//原点
        int centerY = getWidth()/2;
        int radius = dip2px(context, 83); //设置内圆半径
        int ringWidth = dip2px(context, 5); //设置圆环宽度
//
//        //绘制内圆
//        this.paint.setARGB(255, 255, 255, 255);//颜色
//        this.paint.setStrokeWidth(2);//宽度
//        canvas.drawCircle(centerX,centerY, radius, this.paint);

        //绘制圆环
        this.paint.setARGB(255, 212 ,225, 233);
        this.paint.setStrokeWidth(ringWidth);
        canvas.drawCircle(centerX,centerY,radius+1+ringWidth/2,this.paint);

        //绘制外圆
        this.paint.setARGB(155, 167, 190, 206);
        this.paint.setStrokeWidth(2);
        canvas.drawCircle(centerX,centerY, radius+2*ringWidth, this.paint);

        super.onDraw(canvas);
        //http://www.cnblogs.com/linlf03/p/3577828.html
        //http://blog.csdn.net/joker_ya/article/details/38589677
        //http://52xianmengyu.iteye.com/blog/1716773
        //Android  翻牌效果动画
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
