package com.andbase.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ab.global.AbAppConfig;
import com.andbase.model.User;

public class MyApplication extends Application {

	// 登录用户
	public User mUser = null;

	public String cityid = Constant.DEFAULTCITYID;
	public String cityName = Constant.DEFAULTCITYNAME;
	public boolean userPasswordRemember = false;
	public boolean ad = false;
	public boolean isFirstStart = true;
	public boolean isLogin = false;
	public SharedPreferences mSharedPreferences = null;
	
	/** 默认城市 */
	public String province;
	public String city;
	public double longitude;
	public double latitude;
	public String address;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPreferences = getSharedPreferences(AbAppConfig.SHARED_PATH,Context.MODE_PRIVATE);
		initLoginParams();
	}

	/**
	 * 上次登录参数
	 */
	private void initLoginParams() {
		String userName = mSharedPreferences.getString(Constant.USERNAMECOOKIE, null);
		String userPwd = mSharedPreferences.getString(Constant.USERPASSWORDCOOKIE,
				null);
		Boolean userPwdRemember = mSharedPreferences.getBoolean(
				Constant.USERPASSWORDREMEMBERCOOKIE, false);
		mUser = new User();
		mUser.setUserName(userName);
		mUser.setPassword(userPwd);
		userPasswordRemember = userPwdRemember;
	}

	public void updateLoginParams(User user) {
		mUser = user;
		if (userPasswordRemember) {
			Editor editor = mSharedPreferences.edit();
			editor.putString(Constant.USERNAMECOOKIE, user.getUserName());
			editor.putString(Constant.USERPASSWORDCOOKIE, user.getPassword());
			editor.putBoolean(Constant.ISFIRSTSTART, false);
			editor.commit();
		} else {
			Editor editor = mSharedPreferences.edit();
			editor.putBoolean(Constant.ISFIRSTSTART, false);
			editor.commit();
		}
		isFirstStart = false;
		isLogin = true;
	}

	/**
	 * 清空上次登录参数
	 */
	public void clearLoginParams() {
		Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.commit();
		mUser = null;
		isLogin = false;
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
