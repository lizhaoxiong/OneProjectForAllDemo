package com.ok.utils.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.Toast;

import com.ok.utils.app.ActivityStack;
import com.ok.utils.utils.Utils;

/**
 *
 * @description 显示文字提示,文字提示的各种静态方法
 *
 * @author Li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class ToastUtils {
	private static Toast mToast;

	/**
	 * 多次弹出信息只显示最新的一次
	 * @param text
	 * @param isCheckTopActivity 是否检测context是topActivity
	 */
	public static void showToast(Context context, CharSequence text, boolean isCheckTopActivity) {
		if (context == null)
			return;

		if (TextUtils.isEmpty(text))
			return;
		
		if (isCheckTopActivity) {
			Activity topActivity = ActivityStack.getTopActivity();
			if (topActivity != context && context instanceof ContextThemeWrapper) {
					Context baseContext = ((ContextThemeWrapper)context).getBaseContext();
					if (baseContext != topActivity) {
						return;
					}
					context = baseContext;
			}
		}

		if (mToast == null) {
			mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		
		mToast.setGravity(Gravity.CENTER_VERTICAL, 0, SizeUtils.dp2px(context, 40));
		mToast.show();
	}

	/**
	 * 多次弹出信息只显示最新的一次
	 */
	public static final void showToast(Context context, int resId, boolean isCheckTopActivity) {
		if (context == null)
			return;

		showToast(context, context.getText(resId), isCheckTopActivity);
	}

	/**
	 * 多次弹出信息只显示最新的一次
	 */
	public static void showToast(Context context, CharSequence text) {
		if (context==null )
			return;

		if (TextUtils.isEmpty(text))
			return;

		Activity topActivity = ActivityStack.getTopActivity();
		if (topActivity != context && context instanceof ContextThemeWrapper) {
				Context baseContext = ((ContextThemeWrapper)context).getBaseContext();
				if (baseContext != topActivity) {
					return;
				}
				context = baseContext;
		}
		
		if (mToast == null) {
			mToast = Toast.makeText(context.getApplicationContext(), text, Toast.LENGTH_SHORT);
		} else {
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		
		mToast.show();
	}
	
	/**
	 * 多次弹出信息只显示最新的一次
	 */
	public static final void showToast(Context context, int resId) {
		if (context==null)
			return;

		showToast(context, context.getText(resId));
	}

	/**
	 * 去掉toast提示
	 */
	public static void cancelToast() {
		if (mToast != null)
			mToast.cancel();
	}

	public static void noNetwork(Context context){
		showToast(context,"NO NETWORK",true);
	}
}
