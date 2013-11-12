package com.andbase.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ab.global.AbConstant;
import com.andbase.model.User;
import com.baidu.frontia.FrontiaApplication;

public class MyApplication extends FrontiaApplication {

	//登录用户
    public User mUser = null;
    
    public String cityid = Constant.DEFAULTCITYID;
    public String cityName = Constant.DEFAULTCITYNAME;
    public boolean userPasswordRemember = false;
    public boolean ad = false;
    public boolean firstStart = true;
    
	@Override
	public void onCreate() {
		super.onCreate();
		initLoginParams();
	}
	
	/**
	 * 上次登录参数
	 * @throws 
	 * @date：2012-7-17 下午06:07:29
	 * @version v1.0
	 */
	private void initLoginParams() {
		SharedPreferences  sp = getSharedPreferences(AbConstant.SHAREPATH, Context.MODE_PRIVATE);
		String userName = sp.getString(Constant.USERNAMECOOKIE, null);
		String userPwd = sp.getString(Constant.USERPASSWORDCOOKIE, null);
		Boolean userPwdRemember = sp.getBoolean(Constant.USERPASSWORDREMEMBERCOOKIE, false);
		if(userName!=null){
			mUser = new User();
			mUser.setName(userName);
			mUser.setPassword(userPwd);
			userPasswordRemember = userPwdRemember;
		}
	}
	
	/**
	 * 清空上次登录参数
	 */
	public void clearLoginParams() {
		SharedPreferences  sp = getSharedPreferences(AbConstant.SHAREPATH, Context.MODE_PRIVATE);
		Editor editor = sp.edit();
		editor.clear();
		editor.commit();
		mUser = null;
	}

}
