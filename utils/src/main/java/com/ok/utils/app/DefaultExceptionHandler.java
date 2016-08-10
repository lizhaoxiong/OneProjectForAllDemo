package com.ok.utils.app;


import com.ok.utils.utils.log.LogUtils;

import java.lang.Thread.UncaughtExceptionHandler;

/**
 *
 * @description 崩溃异常捕获的实现类,捕获崩溃异常
 *
 * @author Li zhaoxiong
 * @modify
 * @version 1.0.0
 */
public class DefaultExceptionHandler implements UncaughtExceptionHandler {
	private static final String TAG = "DefaultExceptionHandler";
	private OnReceiveErrorListener mOnReceiveErrorListener;
	
	public DefaultExceptionHandler(OnReceiveErrorListener errorListener) {
		mOnReceiveErrorListener = errorListener;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		LogUtils.e(TAG, ex.toString());
		LogUtils.printStackTrace(ex);

		// 收集异常信息 并且发送到服务器
		sendCrashReport(ex);
		// 等待两秒
		try {
			Thread.sleep(2000);//让程序继续运行2秒再退出，保证文件保存并上传到服务器       
		} catch (InterruptedException e) {
			LogUtils.printStackTrace(e);
		} catch (Exception e) {
			LogUtils.printStackTrace(e);
		}
		// 处理异常
		handleException(thread, ex);
	}

	private void sendCrashReport(Throwable ex) {

	}

	private void handleException(Thread thread, Throwable ex) {
		if (thread.toString().equals(BaseApplication.getInstance().mainThreadId)) {
			if(mOnReceiveErrorListener != null)
				mOnReceiveErrorListener.onReceiveError(ex);
			BaseApplication.getInstance().exitApp();
		}
	}
	
	public static interface OnReceiveErrorListener{
		void onReceiveError(Throwable ex);
	}

}
