package com.ab.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ab.global.AbAppConfig;
import com.ab.global.AbConstant;

//TODO: Auto-generated Javadoc

/**
* © 2012 amsoft.cn
* 名称：AbSharedUtil.java 
* 描述：保存到 SharedPreferences 的数据.    
*
* @author 还如一梦中
* @version v1.0
* @date：2014-10-09 下午11:52:13
*/
public class AbSharedUtil {

	private static final String SHARED_PATH = AbAppConfig.SHARED_PATH;

	public static SharedPreferences getDefaultSharedPreferences(Context context) {
		return context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
	}
	
	public static void putInt(Context context,String key, int value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putInt(key, value);
		edit.commit();
	}

	public static int getInt(Context context,String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getInt(key, 0);
	}
	
	public static void putString(Context context,String key, String value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putString(key, value);
		edit.commit();
	}

	public static String getString(Context context,String key) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getString(key,null);
	}
	
	public static void putBoolean(Context context,String key, boolean value) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		Editor edit = sharedPreferences.edit();
		edit.putBoolean(key, value);
		edit.commit();
	}

	public static boolean getBoolean(Context context,String key,boolean defValue) {
		SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(key,defValue);
	}

}
