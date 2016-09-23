package com.ok.utils.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.ok.utils.R;
import com.ok.utils.utils.log.LogUtils;


public class AnimLoadingDialog extends Dialog {

	boolean isCenter = false;
	boolean canWatchOutsideTouch = true;
	boolean dimBehindEnabled = false;
	
	/**
	 * 设置dialog是否可以响应下面activity的事件
	 * @method: setWatchOutsideTouch
	 * @description: TODO
	 * @author: DongFuhai
	 * @param canWatchOutsideTouch == true 可以响应，false 不能响应，默认==true
	 * @return: void
	 * @date: 2013-9-18 下午3:46:09
	 */
	public AnimLoadingDialog setWatchOutsideTouch(boolean canWatchOutsideTouch) {
		this.canWatchOutsideTouch = canWatchOutsideTouch;
		return this;
	}

	/**
	 * 设置dialog背景是不是变灰。默认是不变灰的
	 * @method: setDimBehindEnabled
	 * @description:
	 * @author: DongFuhai
	 * @param dimBehindEnabled
	 * @return: void
	 * @date: 2013-9-18 下午4:09:11
	 */
	public AnimLoadingDialog setDimBehindEnabled(boolean dimBehindEnabled) {
		this.dimBehindEnabled = dimBehindEnabled;
		return this;
	}
	
	public AnimLoadingDialog setCentered(boolean isCenter){
		this.isCenter = isCenter;
		return this;
	}
	
	public AnimLoadingDialog(Context context) {
		super(context, R.style.li_AnimDialogLoading);
	}

	private AnimLoadingDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public AnimLoadingDialog(Context context, int theme) {
		super(context, R.style.li_AnimDialogLoading);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (canWatchOutsideTouch) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);
		}

		setContentView(R.layout.li_animloading);
		setCanceledOnTouchOutside(false);
	}

	@Override
	public void show() {
		initXY();
		try {
			super.show();
		}
		catch (Exception ex) {
			LogUtils.printStackTrace(ex);
		}		
	}

	private void initXY() {
		//100 - (getWindow().getWindowManager().getDefaultDisplay().getHeight() / 2)
		//Log.i("tag", "height==>"+getWindow().getWindowManager().getDefaultDisplay().getHeight());
		//LayoutParams params = new LayoutParams();
//		if (dimBehindEnabled) {
//			params.flags = LayoutParams.FLAG_DIM_BEHIND;
//			params.dimAmount = 0.5f;
//		} // 默认全部透明
//		if(!isCenter)
//			params.y = -200;
		//getWindow().setAttributes(params);
	}

}
