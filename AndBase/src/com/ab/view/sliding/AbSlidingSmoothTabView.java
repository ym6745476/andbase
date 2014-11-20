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
package com.ab.view.sliding;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.adapter.AbFragmentPagerAdapter;
import com.ab.util.AbLogUtil;
import com.ab.util.AbViewUtil;
import com.ab.view.sample.AbHorizontalScrollView;
import com.ab.view.sample.AbHorizontalScrollView.AbOnScrollListener;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbSlidingSmoothTabView.java 
 * 描述：滑动的tab,tab不固定超出后可滑动.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbSlidingSmoothTabView extends LinearLayout {
	
	/** The context. */
	private Context context;
	
	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	
	/** tab的线性布局父. */
	private AbHorizontalScrollView mTabScrollView  = null;
	
	/** The m view pager. */
	private ViewPager mViewPager;
	
	/** tab的列表. */
	private ArrayList<TextView> tabItemList = null;
	
	/** 内容的View. */
	private ArrayList<Fragment> pagerItemList = null;
	
	/** tab的文字. */
	private List<String> tabItemTextList = null;
	
	/** The layout params ff. */
	public LinearLayout.LayoutParams layoutParamsFF = null;
	
	/** The layout params fw. */
	public LinearLayout.LayoutParams layoutParamsFW = null;
	
	/** The layout params ww. */
	public LinearLayout.LayoutParams layoutParamsWW = null;
	
	/** 滑块动画图片. */
	private ImageView mTabImg;
	
	/** 当前页卡编号. */
	private int mSelectedTabIndex = 0;
	
	/** 内容区域的适配器. */
	private AbFragmentPagerAdapter mFragmentPagerAdapter = null;

	/** tab的文字大小. */
	private int tabTextSize = 16;
	
	/** tab的文字颜色. */
	private int tabColor = Color.BLACK;
	
	/** tab的选中文字颜色. */
	private int tabSelectColor = Color.BLACK;
	
	/** tab滑块的高度. */
	private int tabSlidingHeight = 5;
	
	/** 当前tab的位置. */
	private int startX = 0;
	
	/** 当前移动的距离. */
	private int scrollX  = 0;
	
	/**
	 * Instantiates a new ab sliding smooth tab view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbSlidingSmoothTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		
		mTabScrollView  = new AbHorizontalScrollView(context);
		mTabScrollView.setHorizontalScrollBarEnabled(false);
		mTabLayout = new LinearLayout(context);
		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
		mTabLayout.setGravity(Gravity.CENTER);
		//mTabLayout是内容宽度
		mTabScrollView.addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));
		
		
		//定义Tab栏
		tabItemList = new ArrayList<TextView>();
		tabItemTextList = new ArrayList<String>();
		
		this.addView(mTabScrollView,layoutParamsFW);
		
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
			AbLogUtil.e(AbSlidingSmoothTabView.class, "构造AbSlidingSmoothTabView的参数context,必须是FragmentActivity的实例。");
		}
		
		FragmentManager mFragmentManager = ((FragmentActivity)this.context).getFragmentManager();
		mFragmentPagerAdapter = new AbFragmentPagerAdapter(
				mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
		
		mTabScrollView.setSmoothScrollingEnabled(true);
		
		mTabScrollView.setOnScrollListener(new AbOnScrollListener() {
			
			@Override
			public void onScrollToRight() {
				AbLogUtil.d(AbSlidingSmoothTabView.class, "onScrollToRight");
			}
			
			@Override
			public void onScrollToLeft() {
				AbLogUtil.d(AbSlidingSmoothTabView.class, "onScrollToLeft");
			}
			
			@Override
			public void onScrollStoped() {
				AbLogUtil.d(AbSlidingSmoothTabView.class, "onScrollStoped");
			}
			
			@Override
			public void onScroll(int arg1) {
				scrollX = arg1;
				View view = mTabLayout.getChildAt(mSelectedTabIndex);
				int toX = view.getLeft()-scrollX;
				
				AbLogUtil.d(AbSlidingSmoothTabView.class, "滑动X"+startX+"to"+toX);
				imageSlide(mTabImg,startX,toX,0,0);
				startX = toX;
			}
		});
	}

	
	
	/**
	 * The listener interface for receiving myOnPageChange events.
	 * The class that is interested in processing a myOnPageChange
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addMyOnPageChangeListener<code> method. When
	 * the myOnPageChange event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see MyOnPageChangeEvent
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener{

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			//计算滑块偏移
			computeTabImg(arg0);
		}
		
	}
	
	/**
	 * 描述：滑动动画.
	 *
	 * @param v the v
	 * @param startX the start x
	 * @param toX the to x
	 * @param startY the start y
	 * @param toY the to y
	 */
	public void imageSlide(View v, int startX, int toX, int startY, int toY) {
		TranslateAnimation anim = new TranslateAnimation(startX, toX, startY, toY);
		anim.setDuration(100);
		anim.setFillAfter(true);
		v.startAnimation(anim);
	}
	
	/**
	 * 描述：滑动条.
	 *
	 * @param index the index
	 */
	public void computeTabImg(int index){
		
		
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i);
			tv.setTextColor(tabColor);
			tv.setSelected(false);
			if(index == i){
				tv.setTextColor(tabSelectColor);
				tv.setSelected(true);
			}
		}
		
		//判断下一个
		final View tabView = mTabLayout.getChildAt(index);
		AbViewUtil.measureView(tabView);
		
		LayoutParams mParams  = new LayoutParams(tabView.getMeasuredWidth(),tabSlidingHeight);
		mParams.topMargin = -tabSlidingHeight;
		mTabImg.setLayoutParams(mParams);
		
		AbLogUtil.d(AbSlidingSmoothTabView.class, "old--startX:"+startX);
		//判断当前显示的是否被被屏幕遮挡，如果遮挡就移动（如果是第一个和最后一个移动遮挡的宽度否则移动tab的宽度）
		AbLogUtil.d(AbSlidingSmoothTabView.class, "view宽度"+index+":"+tabView.getMeasuredWidth());
		AbLogUtil.d(AbSlidingSmoothTabView.class, "ScrollView宽度:"+mTabScrollView.getWidth());
		AbLogUtil.d(AbSlidingSmoothTabView.class, "scrollX:"+scrollX);
		AbLogUtil.d(AbSlidingSmoothTabView.class, "tabView right:"+tabView.getRight());
		AbLogUtil.d(AbSlidingSmoothTabView.class, "tabView left:"+tabView.getLeft());
		
		if(mSelectedTabIndex<index && tabView.getRight()-scrollX > mTabScrollView.getWidth()){
			AbLogUtil.d(AbSlidingSmoothTabView.class, "右边被遮挡");
			int offsetX = 0;
			//右边被遮挡
			if(index == mTabLayout.getChildCount()-1){
				offsetX = tabView.getRight()-mTabScrollView.getWidth()-scrollX;
				mTabScrollView.smoothScrollBy(offsetX, 0);
				scrollX = scrollX+offsetX;
				AbLogUtil.d(AbSlidingSmoothTabView.class, "startX:"+startX+",offsetX:"+offsetX);
				imageSlide(mTabImg,startX,mTabScrollView.getWidth()-tabView.getMeasuredWidth(),0,0);
				startX = mTabScrollView.getWidth()-tabView.getMeasuredWidth();
			}else{
				offsetX = tabView.getMeasuredWidth();
				mTabScrollView.smoothScrollBy(offsetX, 0);
				scrollX = scrollX+offsetX;
				AbLogUtil.d(AbSlidingSmoothTabView.class, "startX:"+startX+",offsetX:"+offsetX);
				int toX = tabView.getLeft()-scrollX;
				imageSlide(mTabImg,startX,toX,0,0);
				startX = toX;
			}
			
			
		}else if(mSelectedTabIndex>index && tabView.getLeft() < scrollX){
			AbLogUtil.d(AbSlidingSmoothTabView.class, "左边被遮挡");
			//左边被遮挡  offsetX是负值
			int offsetX = 0;
            if(index == 0){
            	offsetX = -scrollX;
            	mTabScrollView.smoothScrollBy(offsetX, 0);
            	scrollX = scrollX+offsetX;
    			imageSlide(mTabImg,startX,0,0,0);
    			startX = 0;
			}else{
				offsetX = -tabView.getMeasuredWidth();
				mTabScrollView.smoothScrollBy(offsetX, 0);
				scrollX = scrollX+offsetX;
				AbLogUtil.d(AbSlidingSmoothTabView.class, "startX2:"+startX+",offsetX:"+offsetX);
				int toX = tabView.getLeft()-scrollX;
				imageSlide(mTabImg,startX,toX,0,0);
				startX = toX;
			}
            
		}else{
			int toX = tabView.getLeft()-scrollX;
			imageSlide(mTabImg,startX,toX,0,0);
			startX  = toX;
		}
		
		mSelectedTabIndex = index;
	}
	
	
	/**
	 * 描述：增加一组内容与tab.
	 *
	 * @param tabTexts the tab texts
	 * @param fragments the fragments
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
			tv.setLayoutParams(new LayoutParams(0,LayoutParams.FILL_PARENT,1));
			tv.setPadding(12, 5, 12, 5);
			tv.setFocusable(false);
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
		mFragmentPagerAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
		computeTabImg(0);
		
	}
	
	/**
	 * 描述：增加一个内容与tab.
	 *
	 * @param tabText the tab text
	 * @param fragment the fragment
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
			tv.setLayoutParams(new LayoutParams(0,LayoutParams.FILL_PARENT,1));
			tv.setPadding(12, 5, 12, 5);
			tv.setFocusable(false);
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
		AbLogUtil.d(AbSlidingSmoothTabView.class, "addItemView finish");
		mFragmentPagerAdapter.notifyDataSetChanged();
		mViewPager.setCurrentItem(0);
		computeTabImg(0);
	}
	
	
	/**
	 * 描述：删除某一个.
	 *
	 * @param index the index
	 */
	public void removeItemView(int index){
		
		tabItemList.remove(index);
		mTabLayout.removeViewAt(index);
		pagerItemList.remove(index);
		
		mFragmentPagerAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 描述：删除所有.
	 *
	 * @param index the index
	 */
	public void removeAllItemView(int index){
		tabItemList.clear();
		mTabLayout.removeAllViews();
		pagerItemList.clear();
		mFragmentPagerAdapter.notifyDataSetChanged();
	}

	
	/**
	 * 描述：获取这个View的ViewPager.
	 *
	 * @return the view pager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}

	/**
	 * Gets the tab layout.
	 *
	 * @return the tab layout
	 */
	public LinearLayout getTabLayout() {
		return mTabLayout;
	}

	/**
	 * 描述：设置Tab的背景.
	 *
	 * @param res the new tab layout background resource
	 */
	public void setTabLayoutBackgroundResource(int res) {
		this.mTabLayout.setBackgroundResource(res);
	}

	/**
	 * Gets the tab color.
	 *
	 * @return the tab color
	 */
	public int getTabColor() {
		return tabColor;
	}
	
	/**
	 * 描述：设置tab文字和滑块的颜色.
	 *
	 * @param tabColor the new tab color
	 */
	public void setTabColor(int tabColor) {
		this.tabColor = tabColor;
	}
	

	/**
	 * 描述：设置选中和滑块的颜色.
	 *
	 * @param tabColor the new tab select color
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
		this.mTabImg.setBackgroundColor(tabColor);
	}

	/**
	 * Gets the tab text size.
	 *
	 * @return the tab text size
	 */
	public int getTabTextSize() {
		return tabTextSize;
	}

	/**
	 * Sets the tab text size.
	 *
	 * @param tabTextSize the new tab text size
	 */
	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}
	
	/**
	 * 描述：设置每个tab的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i);
			tv.setPadding(left, top, right, bottom);
		}
	}

	/**
	 * Gets the tab sliding height.
	 *
	 * @return the tab sliding height
	 */
	public int getTabSlidingHeight() {
		return tabSlidingHeight;
	}

	/**
	 * 描述：设置滑块的高度.
	 *
	 * @param tabSlidingHeight the new tab sliding height
	 */
	public void setTabSlidingHeight(int tabSlidingHeight) {
		this.tabSlidingHeight = tabSlidingHeight;
	}
	
	
	/* (non-Javadoc)
	 * @see android.widget.LinearLayout#onMeasure(int, int)
	 */
	@Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
	
}
