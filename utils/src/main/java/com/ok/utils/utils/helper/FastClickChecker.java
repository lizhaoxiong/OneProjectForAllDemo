package com.ok.utils.utils.helper;

import android.view.View;

import java.util.HashMap;


/**
 * <p>Copyright: Copyright (c) 2016</p>
 *
 * <p>One hundred thousand</p>
 *
 * @description 快速点击判断帮助类
 *
 * @author Li zhaoxiong
 * @modify 
 * @version 1.0.0 
*/
	
public class FastClickChecker {
	public HashMap<View, Long> viewTimeHashMap = new HashMap<View, Long>();
	public HashMap<Integer, Long> idTimeHashMap = new HashMap<Integer, Long>();
	private int timeSpan = 1000;

	//设置需要监听的id
	public void setView(int... viewIds) {
		for (int id : viewIds) {
			idTimeHashMap.put(id, 0l);
		}
	}

	//设置需要监听的view
	public void setView(View... views) {
		for (View v : views) {
			viewTimeHashMap.put(v, 0l);
		}
	}
	
	//检查是否是快速点击
	public boolean isFastClick(View view) {
		if (viewTimeHashMap.containsKey(view)) {
			Long lastClickTime = viewTimeHashMap.get(view);
			long time = System.currentTimeMillis();
			long span = time - lastClickTime;			
			if (0 < span && span < timeSpan) {				
				return true;
			}
			viewTimeHashMap.put(view, time);
		}
		return false;
	}

	//检查是否是快速点击
	public boolean isFastClick(int viewId) {
		if (idTimeHashMap.containsKey(viewId)) {
			Long lastClickTime = idTimeHashMap.get(viewId);
			long time = System.currentTimeMillis();
			long span = time - lastClickTime;			
			if (0 < span && span < timeSpan) {			
				return true;
			}
			idTimeHashMap.put(viewId, time);
		}
		return false;
	}
	
	//设置认定为快速点击的时间间隔
	public void setTimeSpan(int span){
		this.timeSpan = span;
	}
}
