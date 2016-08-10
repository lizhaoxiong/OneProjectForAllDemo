package com.ok.utils.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageButton;

import com.ok.utils.R;


/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description 自定义的ImageButton，处理了按钮的重复点击
 * @author 薛文超
 * @modify
 * @version 1.0.0
 */
public class GImageButton extends ImageButton {
	private static final int CLICK_INTERVAL_DEFAULT=1000; 
	private long lastClickTime;
	private int clickInterval=0;

	public GImageButton(Context context) {
		super(context);
		init(context,null);
	}
	
	public GImageButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public GImageButton(Context context, AttributeSet attrs, int defStyle) {
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
