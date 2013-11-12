/*
 * Copyright (C) 2013 www.418log.org
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
package com.ab.view.sliding;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.adapter.AbFragmentPagerAdapter;
import com.ab.global.AbAppData;

// TODO: Auto-generated Javadoc
/**
 * 名称：AbSlidingTabView
 * 描述：滑动的tab.
 * @author zhaoqp
 * @date 2011-11-28
 * @version
 */
public class AbSlidingTabView extends LinearLayout {
	
	/** The tag. */
	private static String TAG = "AbSlidingTabView";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;

	/** The context. */
	private Context context;
	
	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	
	/** The m view pager. */
	private ViewPager mViewPager;
	
	/**tab的列表*/
	private ArrayList<TextView> tabItemList = null;
	
	/**内容的View*/
	private ArrayList<Fragment> pagerItemList = null;
	
	/**tab的文字*/
	private List<String> tabItemTextList = null;
	
	/** The layout params ff. */
	public LinearLayout.LayoutParams layoutParamsFF = null;
	
	/** The layout params fw. */
	public LinearLayout.LayoutParams layoutParamsFW = null;
	
	/** The layout params ww. */
	public LinearLayout.LayoutParams layoutParamsWW = null;
	
	/**滑块动画图片*/
	private ImageView mTabImg;
	
	/**当前页卡编号*/
	private int currIndex = 0;
	
	/**屏幕宽度*/
	private int displayWidth = 0;
	
	/**内容区域的适配器*/
	private AbFragmentPagerAdapter mFragmentPagerAdapter = null;

	/**tab的文字大小*/
	private int tabTextSize = 16;
	
	/**tab的文字颜色*/
	private int tabColor = Color.BLACK;
	
	/**tab的选中文字颜色*/
	private int tabSelectColor = Color.BLACK;
	
	/**tab滑块的高度*/
	private int tabSlidingHeight = 5;
	
	
	/**当前tab的位置*/
	private int startX = 0;
	
	public AbSlidingTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		//获取屏幕的分辨率，以计算偏移量
		Display display = ((Activity)this.context).getWindowManager().getDefaultDisplay();
		displayWidth = display.getWidth();
		
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		
		mTabLayout = new LinearLayout(context);
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabLayout.setGravity(Gravity.CENTER);
		//定义Tab栏
		tabItemList = new ArrayList<TextView>();
		tabItemTextList = new ArrayList<String>();
		this.addView(mTabLayout,layoutParamsFW);
		
		//页卡滑动图片
		mTabImg  = new ImageView(context);
		this.addView(mTabImg,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,tabSlidingHeight));
		
		//内容的View的适配
		mViewPager = new ViewPager(context);
		//手动创建的ViewPager,必须调用setId()方法设置一个id
		mViewPager.setId(1985);
		pagerItemList = new ArrayList<Fragment>();
		
		this.addView(mViewPager,layoutParamsFF);
		
		//要求必须是FragmentActivity的实例
		if(!(this.context instanceof FragmentActivity)){
			Log.e(TAG, "构造AbSlidingTabView的参数context,必须是FragmentActivity的实例。");
		}
		
		FragmentManager mFragmentManager = ((FragmentActivity)this.context).getSupportFragmentManager();
		mFragmentPagerAdapter = new AbFragmentPagerAdapter(
				mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
		mViewPager.setCurrentItem(0);
		
	}

	
	
	public class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		@Override
		public void onPageSelected(int arg0) {
			//计算滑块偏移
			computeTabImg(arg0);
			
		}
		
	}
	
	public void imageSlide(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(100);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
	
	public void computeTabImg(int index){
		
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i);
			tv.setTextColor(tabColor);
			if(index == i){
				tv.setTextColor(tabSelectColor);
			}
		}
		
		//计算滑块偏移
		int count = mFragmentPagerAdapter.getCount();
		int width = displayWidth/count;
		int toX = width * index;
		currIndex = index;
		mTabImg.setLayoutParams(new LayoutParams(width,tabSlidingHeight));
		imageSlide(mTabImg,startX,toX,0,0);
		startX  = toX;
	}
	
	
	/**
	 * 
	 * 描述：增加一组内容与tab
	 * @throws 
	 */
	public void addItemViews(List<String> tabTexts,List<Fragment> fragments){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		
		tabItemList.clear();
		mTabLayout.removeAllViews();
		
		for(int i=0;i<tabItemTextList.size();i++){
			final int index = i;
			String text = tabItemTextList.get(i);
			TextView tv = new TextView(this.context);
			tv.setTextColor(tabColor);
			tv.setTextSize(tabTextSize);
			tv.setText(text);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.FILL_PARENT,1));
			tabItemList.add(tv);
			mTabLayout.addView(tv);
            tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mViewPager.setCurrentItem(index);
				}
			});
		}
		
		//重新
		computeTabImg(0);
		
		mFragmentPagerAdapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 
	 * 描述：增加一个内容与tab
	 * @throws 
	 */
	public void addItemView(String tabText,Fragment fragment){
		
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		
		tabItemList.clear();
		mTabLayout.removeAllViews();
		
		for(int i=0;i<tabItemTextList.size();i++){
			final int index = i;
			String text = tabItemTextList.get(i);
			TextView tv = new TextView(this.context);
			tv.setTextColor(tabColor);
			tv.setTextSize(tabTextSize);
			tv.setText(text);
			tv.setGravity(Gravity.CENTER);
			tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1));
			tabItemList.add(tv);
			mTabLayout.addView(tv);
			tv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					mViewPager.setCurrentItem(index);
				}
			});
		}
		
		//重新
		computeTabImg(0);
	
		mFragmentPagerAdapter.notifyDataSetChanged();
		
	}
	
	
	/**
	 * 
	 * 描述：删除某一个
	 * @param index
	 * @throws 
	 */
	public void removeItemView(int index){
		
		tabItemList.remove(index);
		mTabLayout.removeViewAt(index);
		pagerItemList.remove(index);
		
		mFragmentPagerAdapter.notifyDataSetChanged();
		
	}
	
	/**
	 * 
	 * 描述：删除所有
	 * @throws 
	 */
	public void removeAllItemView(int index){
		tabItemList.clear();
		mTabLayout.removeAllViews();
		pagerItemList.clear();
		mFragmentPagerAdapter.notifyDataSetChanged();
		
	}

	
	/**
	 * 
	 * 描述：获取这个View的ViewPager
	 * @return
	 * @throws 
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}

	public LinearLayout getTabLayout() {
		return mTabLayout;
	}

	/**
	 * 
	 * 描述：设置Tab的背景
	 * @param res
	 * @throws 
	 */
	public void setTabLayoutBackground(int res) {
		this.mTabLayout.setBackgroundResource(res);
	}

	public int getTabColor() {
		return tabColor;
	}
	
	/**
	 * 
	 * 描述：设置tab文字和滑块的颜色
	 * @param tabColor
	 * @throws 
	 */
	public void setTabColor(int tabColor) {
		this.tabColor = tabColor;
	}
	

	/**
	 * 
	 * 描述：设置选中和滑块的颜色
	 * @param tabColor
	 * @throws 
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
		this.mTabImg.setBackgroundColor(tabColor);
	}

	public int getTabTextSize() {
		return tabTextSize;
	}

	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}

	public int getTabSlidingHeight() {
		return tabSlidingHeight;
	}

	/**
	 * 
	 * 描述：设置滑块的高度
	 * @param tabSlidingHeight
	 * @throws 
	 */
	public void setTabSlidingHeight(int tabSlidingHeight) {
		this.tabSlidingHeight = tabSlidingHeight;
	}
	
	
}
