package com.ok.utils.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.ArcShape;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.ok.utils.utils.log.LogUtils;


public class NumberDotView extends TextView {
    public NumberDotView(Context context) {
        super(context);
        init();
    }

    public NumberDotView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setIncludeFontPadding(false);
        setTextColor(Color.WHITE);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
        setPadding(dip2Px(1), dip2Px(1), dip2Px(1), dip2Px(1));
        setBackground(8, Color.parseColor("#d3321b"));
        setGravity(Gravity.CENTER);
        setVisibility(View.GONE);
    }

    public void setBackground(int dipRadius, int badgeColor) {
        int radius = dip2Px(dipRadius);
        float[] radiusArray = new float[] { radius, radius, radius, radius, radius, radius, radius, radius};
//        RoundRectShape roundRect = new RoundRectShape(radiusArray, null, null);
        ArcShape arcShape=new ArcShape(0,360);
        ShapeDrawable bgDrawable = new ShapeDrawable(arcShape);
        bgDrawable.getPaint().setColor(badgeColor);
        setBackgroundDrawable(bgDrawable);
    }




    @Override
    public void setText(CharSequence text, BufferType type) {
        if(null!=text&&text.toString().trim().equalsIgnoreCase("0")){
            setVisibility(View.INVISIBLE);
        }else{
            try{
                int number=Integer.parseInt(text.toString().trim());
                if(number>99){
                    text="..";
                }
                setVisibility(View.VISIBLE);
            }catch(Exception e){
                LogUtils.d("NumberDotView","set number error "+text);
                setVisibility(View.GONE);
            }
        }
        super.setText(text, type);
    }

    private int dip2Px(float dip) {
        return (int) (dip * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
}

