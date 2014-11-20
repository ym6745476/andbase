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
package com.ab.view.sample;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ListView;
import android.widget.ScrollView;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbInnerViewPager.java 
 * 描述：这个ViewPager解决了外部是可滚动View（List或者scrollView）
 * 与内部可滑动View的事件冲突问题
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-10-24 下午1:36:45
 */
public class AbInnerViewPager extends ViewPager {

	/** The parent scroll view. */
	private ScrollView parentScrollView;
	
	/** The parent list view. */
	private ListView parentListView;
	
	/** The m gesture detector. */
	private GestureDetector mGestureDetector;
	
	/**
	 * 初始化这个内部的ViewPager.
	 *
	 * @param context the context
	 */
	public AbInnerViewPager(Context context) {
		super(context);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
	}

	/**
	 * 初始化这个内部的ViewPager.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbInnerViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		mGestureDetector = new GestureDetector(new YScrollDetector());
		setFadingEdgeLength(0);
	}
	
	/**
	 * 描述：拦截事件.
	 *
	 * @param ev the ev
	 * @return true, if successful
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return super.onInterceptTouchEvent(ev)
				&& mGestureDetector.onTouchEvent(ev);
	}

	/**
	 * 设置父级的View.
	 *
	 * @param flag 父是否滚动开关
	 */
	private void setParentScrollAble(boolean flag) {
		if(parentScrollView!=null){
			parentScrollView.requestDisallowInterceptTouchEvent(!flag);
		}
		
		if(parentListView!=null){
			parentListView.requestDisallowInterceptTouchEvent(!flag);
		}
		
	}

	/**
	 * 如果外层有ScrollView需要设置.
	 *
	 * @param parentScrollView the new parent scroll view
	 */
	public void setParentScrollView(ScrollView parentScrollView) {
		this.parentScrollView = parentScrollView;
	}
	
	/**
	 * 如果外层有ListView需要设置.
	 *
	 * @param parentListView the new parent scroll view
	 */
	public void setParentListView(ListView parentListView) {
		this.parentListView = parentListView;
	}
	
	
	/**
	 * The Class YScrollDetector.
	 */
	class YScrollDetector extends SimpleOnGestureListener {
		
		/* (non-Javadoc)
		 * @see android.view.GestureDetector.SimpleOnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
		 */
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			
			if (Math.abs(distanceX) >= Math.abs(distanceY)) {
				//父亲不滑动
				setParentScrollAble(false);
				return true;
			}else{
				setParentScrollAble(true);
			}
			return false;
		}
	}

	

}
