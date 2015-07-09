package com.ab.activity;

import com.ab.global.AbActivityManager;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

public class BaseActivity extends FragmentActivity{
	
	/** LayoutInflater. */
	public LayoutInflater mInflater;
	
	/**Application. */
	public Application mApplication = null;
	
	/** 主题ID. */
	public int  mThemeId = -1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInflater = LayoutInflater.from(this);
		mApplication = this.getApplication();
		if(mThemeId!=-1){
    		this.setTheme(mThemeId);
    	}else{
    		if (savedInstanceState != null) {  
                if (savedInstanceState.getInt("theme", -1) != -1) {
                    mThemeId = savedInstanceState.getInt("theme");  
                    this.setTheme(mThemeId);
                }
            }
    	}
		
		AbActivityManager.getInstance().addActivity(this);
	}

	/**
	 * 初始化主题ID
	 * @param themeId
	 */
	public void initAppTheme(int themeId){
		this.mThemeId = themeId;
	}
	
	/**
	 * 设置主题ID
	 * @param themeId
	 */
    public void setAppTheme(int themeId){
    	this.mThemeId = themeId;
        this.recreate();  
    }  
    
   /**
    *   
    * 保存主题ID，onCreate 时读取主题.
    * @param outState
    */
    @Override  
    public void onSaveInstanceState(Bundle outState) {  
        super.onSaveInstanceState(outState);  
        outState.putInt("theme", this.mThemeId);  
    }  
    
    /**
	 * 返回默认
	 * @param view
	 */
	public void back(View view){
		finish();
	}
	
	/**
	 * finish.
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		AbActivityManager.getInstance().removeActivity(this);
		super.finish();
	}
}
