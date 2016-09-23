package com.ok.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.ok.utils.R;

/**
 * 自定义Button,处理了按钮的重复点击
 */
public class GButton extends Button {
	private static final int CLICK_INTERVAL_DEFAULT=1000; 
	
	private long lastClickTime;
	private int clickInterval=0;

	public GButton(Context context) {
		super(context);
		init(context,null);
	}
	
	public GButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public GButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public void init(Context context,AttributeSet attrs) {
		if(attrs!=null){
			TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.li_GButton);
			clickInterval=a.getInt(R.styleable.li_GButton_li_clickInterval, CLICK_INTERVAL_DEFAULT);
			a.recycle();	
		}else{
			clickInterval=CLICK_INTERVAL_DEFAULT;	
		}
	}

	@Override
	public boolean performClick() {
		if (isFastDoubleClick())
			return true;
		return super.performClick();
	}

	public boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeSpan = time - lastClickTime;
		if (0 < timeSpan && timeSpan < clickInterval) {
			return true;
		}
		lastClickTime = time;
		return false;
	}
}
