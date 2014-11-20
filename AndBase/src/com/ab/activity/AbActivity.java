/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.activity;

import java.lang.reflect.Field;

import android.app.Application;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.ab.view.ioc.AbIocEventListener;
import com.ab.view.ioc.AbIocSelect;
import com.ab.view.ioc.AbIocView;
import com.ab.view.titlebar.AbBottomBar;
import com.ab.view.titlebar.AbTitleBar;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbActivity.java 
 * 描述：继承这个Activity使你的Activity拥有一个程序框架.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-1-15 下午3:27:05
 */

public abstract class AbActivity extends FragmentActivity {

	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	
	/** 全局的Application对象，已经完成初始化. */
	public Application abApplication = null;
	
	/** 总布局. */
	public RelativeLayout ab_base = null;
	
	/** 标题栏布局. */
	private AbTitleBar mAbTitleBar = null;
	
	/** 副标题栏布局. */
	private AbBottomBar mAbBottomBar = null;
	
	/** 主内容布局. */
	protected RelativeLayout contentLayout = null;
	
	/**
	 * 描述：创建.
	 *
	 * @param savedInstanceState the saved instance state
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInflater = LayoutInflater.from(this);
		
		//主标题栏
		mAbTitleBar = new AbTitleBar(this);
		
		//最外层布局
		ab_base = new RelativeLayout(this);
		ab_base.setBackgroundColor(Color.rgb(255, 255, 255));
		
		//内容布局
		contentLayout = new RelativeLayout(this);
		contentLayout.setPadding(0, 0, 0, 0);
		
		//副标题栏
		mAbBottomBar = new AbBottomBar(this);
		
        //填入View
		ab_base.addView(mAbTitleBar,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		mAbTitleBar.setVisibility(View.GONE);
		
		RelativeLayout.LayoutParams layoutParamsBottomBar = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsBottomBar.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		ab_base.addView(mAbBottomBar, layoutParamsBottomBar);
		
		RelativeLayout.LayoutParams layoutParamsContent = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsContent.addRule(RelativeLayout.BELOW, mAbTitleBar.getId());
		layoutParamsContent.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
		ab_base.addView(contentLayout, layoutParamsContent);
		
		//Application初始化
		abApplication = getApplication();
        
		//设置ContentView
        setContentView(ab_base,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	/**
	 * 描述：用指定的View填充主界面.
	 * @param contentView  指定的View
	 */
	public void setAbContentView(View contentView) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		//ioc
		initIocView();
	}
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.
	 * @param resId  指定的View的资源ID
	 */
	public void setAbContentView(int resId) {
		setAbContentView(mInflater.inflate(resId, null));
	}
	
	/**
	 * 获取主标题栏布局.
	 * @return the title layout
	 */
	public AbTitleBar getTitleBar() {
		mAbTitleBar.setVisibility(View.VISIBLE);
		return mAbTitleBar;
	}
	
	/**
	 * 获取副标题栏布局.
	 * @return the bottom layout
	 */
	public AbBottomBar getBottomBar() {
		return mAbBottomBar;
	}

	/**
	 * 描述：Activity结束.
	 *
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		super.finish();
	}

	/**
	 * 描述：设置绝对定位的主标题栏覆盖到内容的上边.
	 *
	 * @param overlay the new title bar overlay
	 */
	public void setTitleBarOverlay(boolean overlay) {
		ab_base.removeAllViews();
		if(overlay){
			RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
			ab_base.addView(contentLayout,layoutParamsFW1);
			RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
			ab_base.addView(mAbTitleBar,layoutParamsFW2);
			
			RelativeLayout.LayoutParams layoutParamsFW3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			ab_base.addView(mAbBottomBar, layoutParamsFW3);
			
		}else{
			ab_base.addView(mAbTitleBar,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
			RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			ab_base.addView(mAbBottomBar, layoutParamsFW2);
			
			RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW1.addRule(RelativeLayout.BELOW, mAbTitleBar.getId());
			layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
			ab_base.addView(contentLayout, layoutParamsFW1);
		}
	}
	
	/**
	 * 描述：设置界面显示（忽略标题栏）.
	 *
	 * @param layoutResID the new content view
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initIocView();
	}

	/**
	 * 描述：设置界面显示（忽略标题栏）.
	 *
	 * @param view the view
	 * @param params the params
	 * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View view,
			android.view.ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		initIocView();
	}

	/**
	 * 描述：设置界面显示（忽略标题栏）.
	 *
	 * @param view the new content view
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	public void setContentView(View view) {
		super.setContentView(view);
		initIocView();
	}
	
	/**
	 * 初始化为IOC控制的View.
	 */
	private void initIocView(){
		Field[] fields = getClass().getDeclaredFields();
		if(fields!=null && fields.length>0){
			for(Field field : fields){
				try {
					field.setAccessible(true);
					
					if(field.get(this)!= null )
						continue;
				
					AbIocView viewInject = field.getAnnotation(AbIocView.class);
					if(viewInject!=null){
						
						int viewId = viewInject.id();
					    field.set(this,findViewById(viewId));
					
						setListener(field,viewInject.click(),AbIocEventListener.CLICK);
						setListener(field,viewInject.longClick(),AbIocEventListener.LONGCLICK);
						setListener(field,viewInject.itemClick(),AbIocEventListener.ITEMCLICK);
						setListener(field,viewInject.itemLongClick(),AbIocEventListener.ITEMLONGCLICK);
						
						AbIocSelect select = viewInject.select();
						if(!TextUtils.isEmpty(select.selected())){
							setViewSelectListener(field,select.selected(),select.noSelected());
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 设置view的监听器.
	 *
	 * @param field the field
	 * @param select the select
	 * @param noSelect the no select
	 * @throws Exception the exception
	 */
	private void setViewSelectListener(Field field,String select,String noSelect)throws Exception{
		Object obj = field.get(this);
		if(obj instanceof View){
			((AbsListView)obj).setOnItemSelectedListener(new AbIocEventListener(this).select(select).noSelect(noSelect));
		}
	}
	
	/**
	 * 设置view的监听器.
	 *
	 * @param field the field
	 * @param methodName the method name
	 * @param method the method
	 * @throws Exception the exception
	 */
	private void setListener(Field field,String methodName,int method)throws Exception{
		if(methodName == null || methodName.trim().length() == 0)
			return;
		
		Object obj = field.get(this);
		
		switch (method) {
			case AbIocEventListener.CLICK:
				if(obj instanceof View){
					((View)obj).setOnClickListener(new AbIocEventListener(this).click(methodName));
				}
				break;
			case AbIocEventListener.ITEMCLICK:
				if(obj instanceof AbsListView){
					((AbsListView)obj).setOnItemClickListener(new AbIocEventListener(this).itemClick(methodName));
				}
				break;
			case AbIocEventListener.LONGCLICK:
				if(obj instanceof View){
					((View)obj).setOnLongClickListener(new AbIocEventListener(this).longClick(methodName));
				}
				break;
			case AbIocEventListener.ITEMLONGCLICK:
				if(obj instanceof AbsListView){
					((AbsListView)obj).setOnItemLongClickListener(new AbIocEventListener(this).itemLongClick(methodName));
				}
				break;
			default:
				break;
		}
	}
	
}
