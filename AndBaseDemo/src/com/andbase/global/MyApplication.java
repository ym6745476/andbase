package com.andbase.global;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ab.global.AbAppConfig;
import com.ab.global.AbConstant;
import com.andbase.R;
import com.andbase.im.IMConfig;
import com.andbase.im.global.IMConstant;
import com.andbase.im.util.IMUtil;
import com.andbase.model.User;

public class MyApplication extends Application {

	// 登录用户
	public User mUser = null;

	public String cityid = Constant.DEFAULTCITYID;
	public String cityName = Constant.DEFAULTCITYNAME;
	public boolean userPasswordRemember = false;
	public boolean ad = false;
	public boolean isFirstStart = true;
	public SharedPreferences mSharedPreferences = null;

	@Override
	public void onCreate() {
		super.onCreate();
		mSharedPreferences = getSharedPreferences(AbAppConfig.SHARED_PATH,
				Context.MODE_PRIVATE);
		initLoginParams();
		initIMConfig();
	}

	/**
	 * 上次登录参数
	 */
	private void initLoginParams() {
		SharedPreferences preferences = getSharedPreferences(
				AbAppConfig.SHARED_PATH, Context.MODE_PRIVATE);
		String userName = preferences.getString(Constant.USERNAMECOOKIE, null);
		String userPwd = preferences.getString(Constant.USERPASSWORDCOOKIE,
				null);
		Boolean userPwdRemember = preferences.getBoolean(
				Constant.USERPASSWORDREMEMBERCOOKIE, false);
		if (userName != null) {
			mUser = new User();
			mUser.setUserName(userName);
			mUser.setPassword(userPwd);
			userPasswordRemember = userPwdRemember;
		}
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
	}

	/**
	 * 清空上次登录参数
	 */
	public void clearLoginParams() {
		Editor editor = mSharedPreferences.edit();
		editor.clear();
		editor.commit();
		mUser = null;
	}

	/**
	 * IM配置
	 */
	public void initIMConfig() {
	    IMConfig mIMConfig = new IMConfig();

	    mIMConfig.setXmppHost(mSharedPreferences.getString(
				IMConstant.XMPP_HOST,
				getResources().getString(R.string.xmpp_host)));

	    mIMConfig.setXmppPort(mSharedPreferences.getInt(
				IMConstant.XMPP_PORT,
				getResources().getInteger(R.integer.xmpp_port)));

	    mIMConfig.setXmppServiceName(mSharedPreferences.getString(
				IMConstant.XMPP_SEIVICE_NAME,
				getResources().getString(R.string.xmpp_service_name)));

	    mIMConfig.setNovisible(mSharedPreferences.getBoolean(
				IMConstant.IS_NOVISIBLE,
				getResources().getBoolean(R.bool.is_novisible)));
		
		IMUtil.setIMConfig(this.getApplicationContext(),mIMConfig);
		
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
	}

}
