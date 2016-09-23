package com.ok.utils.utils.cache;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>One hundred thousand</p>
 *
 * @author Li zhaoxiong
 * @description 对SharedPreferences的简单封装
 * @modify
 * @version 1.0.0
 */
public class PreferencesUtils {
	/** 
	 *  取出whichSp中field字段对应的string类型的值   
	 *  
	 * @param context 上下文，来区别哪一个activity调用的 
	 * @param spName 使用的SharedPreferences的名字 
	 * @param field SharedPreferences的哪一个字段 
	 * @return 如果该字段没对应值，则取出字符串“”
	 */
	public static String getSharePrefStr(Context context, String spName, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.getString(field,"");
		String s = sp.getString(field, "");//如果该字段没对应值，则取出字符串“”
		return s;
	}
	
	/**
	 * 取出whichSp中field字段对应的int类型的值   
	 * @param context
	 * @param spName
	 * @param userId User ID字段
	 * @param field
	 * @return 如果该字段没对应值，则取出0 
	 */
	public static int getSharePrefInt(Context context, String spName, String userId, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName + userId, Context.MODE_PRIVATE);
		int i = sp.getInt(field, 0);
		return i;
	}

	/**
	 * 取出whichSp中field字段对应的boolean类型的值
	 * @param context
	 * @param spName
	 * @param userId User ID字段
	 * @param field
	 * @return 如果该字段没对应值，则取出0
	 */
	public static boolean getSharePrefBoolean(Context context, String spName, String userId, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName + userId, Context.MODE_PRIVATE);
		boolean bool = sp.getBoolean(field,false);
		return bool;
	}
	/**
	 * 取出whichSp中field字段对应的boolean类型的值
	 * @param context
	 * @param spName
	 * @param userId User ID字段
	 * @param field
	 * @return 如果该字段没对应值，则取出0
	 */
	public static Map<String,Object> getSharePref(Context context, String spName, String userId, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName + userId, Context.MODE_PRIVATE);
		return null;
	}
	/**
	 * 取出whichSp中field字段对应的boolean类型的值
	 * @param context
	 * @param spName
	 * @param field
	 * @return 如果该字段没对应值，则取出0
	 */
	public static boolean getSharePrefBoolean(Context context, String spName, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		boolean bool = sp.getBoolean(field,false);
		return bool;
	}
	/**
	 * 取出whichSp中field字段对应的int类型的值   
	 * @param context
	 * @param spName
	 * @param field
	 * @return 如果该字段没对应值，则取出0 
	 */
	public static int getSharePrefInt(Context context, String spName, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		int i = sp.getInt(field, 0);
		return i;
	}

	/**
	 * 取出whichSp中field字段对应的long类型的值   
	 * @param context
	 * @param spName
	 * @param field
	 * @return 如果该字段没对应值，则取出0 
	 */
	public static long getSharePrefLong(Context context, String spName, String field) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		long i = sp.getLong(field, 0l);
		return i;
	}
	
	/**
	 * 取出whichSp中field字段对应的long类型的值   
	 * @param context
	 * @param spName
	 * @param field
	 * @param defValue 默认值
	 * @return 如果没有该字段则返回默认值 
	 */
	public static long getSharePrefLong(Context context, String spName, String field, long defValue) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		long i = sp.getLong(field, defValue);
		return i;
	}
	
	/**
	 * 取出whichSp中field字段对应的int类型的值
	 * @param context
	 * @param spName
	 * @param field
	 * @return
	 */
	public static int getSharePrefInt(Context context, String spName, String field, int defValue) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		int i = sp.getInt(field, defValue);
		return i;
	}

	/**
	 * 保存string类型的value到whichSp中的field字段
	 * @param context
	 * @param spName
	 * @param field
	 * @param value
	 */
	public static void putSharePref(Context context, String spName, String field, String value) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putString(field, value).commit();
	}

	/**
	 * 保存int类型的value到whichSp中的field字段   
	 * @param context
	 * @param spName
	 * @param field
	 * @param value
	 */
	public static void putSharePref(Context context, String spName, String field, int value) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putInt(field, value).commit();
	}
	
	/**
	 * 保存int类型的value到whichSp中的field字段   
	 * @param context
	 * @param spName
	 * @param userId
	 * @param field
	 * @param value
	 */
	public static void putSharePref(Context context, String spName, String userId, String field, int value) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName + userId, Context.MODE_PRIVATE);
		sp.edit().putInt(field, value).commit();
	}
	
	/**
	 * 保存long类型的value到whichSp中的field字段   
	 * @param context
	 * @param spName
	 * @param field
	 * @param value
	 */
	public static void putSharePref(Context context, String spName, String field, long value) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putLong(field, value).commit();
	}

	/**
	 * 保存boolean类型的value到whichSp中的field字段
	 * @param context
	 * @param spName
	 * @param field
	 * @param value
	 */
	public static void putSharePref(Context context, String spName, String field, boolean value) {
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().putBoolean(field, value).commit();
	}


	/**
	 * 清空指定的xml缓存
	 * @method: clearPreferences
	 * @description:
	 * @author: DongFuhai
	 * @param context
	 * @param spName
	 * @return: void
	 * @date: 2013-9-16 上午10:45:50
	 */
	public static void clearPreferences(Context context, String spName){
		SharedPreferences sp = (SharedPreferences) context.getSharedPreferences(spName, Context.MODE_PRIVATE);
		sp.edit().clear().commit();
	}

}
