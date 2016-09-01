package com.ok.utils.utils;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

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
public class DrawableUtils {
    /**
     * 生成drawable资源，对应的xml中的shape标签
     * @param argb
     * @param radius
     * @return
     */
    public static GradientDrawable generateDrawable(int argb, float radius){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);//设置为矩形，默认就是矩形
        drawable.setCornerRadius(radius);//设置圆角的半径
        float[] f = {20,2,0,5};
        drawable.setCornerRadii(f);
        drawable.setColor(argb);
        return drawable;
    }

    /**
     * 动态创建Selector
     * @param pressed
     * @param normal
     * @return
     */
    public static StateListDrawable generateSelector(Drawable pressed, Drawable normal){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, pressed);//设置按下的图片
        drawable.addState(new int[]{}, normal);//设置默认的图片
        return drawable;
    }
}
