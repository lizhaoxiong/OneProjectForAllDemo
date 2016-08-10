package com.ok.utils.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

import com.ok.utils.utils.log.LogUtils;

import java.util.List;

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
public class PackUtils {

	/**
	 * 当前应用程序是否正在运行
	 * @param context
	 * @return
     */
	public static boolean packIsRunning(Context context) {
		List<RunningTaskInfo> taskInfos = getTaskInfos(context, 1000);
		if (taskInfos == null || taskInfos.isEmpty())
			return false;
		for (RunningTaskInfo info : taskInfos) {
			if (context.getPackageName().equals(info.baseActivity.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断app是否前台运行
	 * @param context
	 * @return
     */
	public static boolean isOnForeground(Context context) {
		List<RunningTaskInfo> taskInfos = getTaskInfos(context, 2);
		if (taskInfos == null || taskInfos.isEmpty())
			return false;
		RunningTaskInfo runningTaskInfo = taskInfos.get(0);
		ComponentName topActivity = runningTaskInfo.topActivity;
		if (context.getPackageName().equals(topActivity.getPackageName())) {
			return true;
		}
		return false;
	}

	/**
	 * 判断app是否后台运行
	 * @param context
	 * @return
     */
	public static boolean isRunningBackground(Context context) {
		if (!isOnForeground(context) && packIsRunning(context)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取正在运行app的集合
	 * @param context
	 * @param num
     * @return
     */
	public static List<RunningTaskInfo> getTaskInfos(Context context, int num) {
		try {//需要catch异常，用户可能会限制权限
			ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> taskInfos = activityManager.getRunningTasks(num);
			if (taskInfos != null && !taskInfos.isEmpty()) {
				return taskInfos;
			}
		}
		catch (Exception e) {
			LogUtils.printStackTrace(e);
		}
		return null;
	}
}
