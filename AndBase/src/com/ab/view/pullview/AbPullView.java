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
package com.ab.view.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.ab.view.listener.AbOnRefreshListener;

// TODO: Auto-generated Javadoc
/**
 * The Class AbPullView.
 */
public class AbPullView extends ScrollView{

	
	/** The m last y. */
	private float mLastY = -1; 
	
	/** The m scroller. */
	private Scroller mScroller;
	
	/** The m scroll layout. */
	private LinearLayout mScrollLayout;
	//头部刷新View
	/** The m header view. */
	private AbListViewHeader mHeaderView;
	
	//头部View的高度
	/** The m header view height. */
	private int mHeaderViewHeight; 
	
	/** The m enable pull refresh. */
	private boolean mEnablePullRefresh = true;
	
	/** The m pull refreshing. */
	private boolean mPullRefreshing = false;
	
	/** The m ab on refresh listener. */
	private AbOnRefreshListener  mAbOnRefreshListener = null;
 
	/** The m scroll back. */
	private int mScrollBack;
	
	/** The Constant SCROLLBACK_HEADER. */
	private final static int SCROLLBACK_HEADER = 0;
	
	/** The Constant SCROLL_DURATION. */
	private final static int SCROLL_DURATION = 400;
	
	/** The Constant OFFSET_RADIO. */
	private final static float OFFSET_RADIO = 1.8f;
	
	
	/**
	 * 构造.
	 *
	 * @param context the context
	 */
	public AbPullView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 构造.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbPullView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 初始化View.
	 * @param context the context
	 */
	private void initView(Context context) {
		mScroller = new Scroller(context, new DecelerateInterpolator());
		
		LinearLayout.LayoutParams layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		mScrollLayout = new LinearLayout(context);
		mScrollLayout.setLayoutParams(layoutParamsFW);
		mScrollLayout.setOrientation(LinearLayout.VERTICAL);
		
		// init header view
		mHeaderView = new AbListViewHeader(context);
		
		// init header height
		mHeaderViewHeight = mHeaderView.getHeaderHeight();
		mHeaderView.setGravity(Gravity.BOTTOM);
		mScrollLayout.addView(mHeaderView,layoutParamsFW);
		this.addView(mScrollLayout);
	}

	/**
	 * 打开或者关闭下拉刷新功能.
	 *
	 * @param enable 开关标记
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) { // disable, hide the content
			mHeaderView.setVisibility(View.INVISIBLE);
		} else {
			mHeaderView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 停止刷新并重置header的状态.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
	}

	/**
	 * 更新header的高度.
	 *
	 * @param delta 差的距离
	 */
	private void updateHeaderHeight(float delta) {
		int newHeight = (int) delta + mHeaderView.getVisiableHeight();
		mHeaderView.setVisiableHeight(newHeight);
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
				mHeaderView.setState(AbListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
			}
		}
	}

	/**
	 * 根据状态设置Header的位置.
	 */
	private void resetHeaderHeight() {
		//当前下拉到的高度
		int height = mHeaderView.getVisiableHeight();
		if (height < mHeaderViewHeight || !mPullRefreshing) {
			//距离短  隐藏
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -1*height, SCROLL_DURATION);
		}else if(height > mHeaderViewHeight || !mPullRefreshing){
			//距离多的  弹回到mHeaderViewHeight
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -(height-mHeaderViewHeight), SCROLL_DURATION);
		}
		
		invalidate();
	}
	
	/**
	 * 描述：TODO
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if ( (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				updateHeaderHeight(deltaY / OFFSET_RADIO);
			}
			break;
		case MotionEvent.ACTION_UP:
			mLastY = -1;
			//需要刷新的条件
			if (mEnablePullRefresh && mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
				mPullRefreshing = true;
				mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
				if (mAbOnRefreshListener != null) {
					//刷新
					mAbOnRefreshListener.onRefresh();
				}
			}
			if(mEnablePullRefresh){
				//弹回
				resetHeaderHeight();
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 描述：TODO
	 */
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	/**
	 * 描述：设置ListView的监听器.
	 *
	 * @param abOnRefreshListener
	 */
	public void setAbOnRefreshListener(AbOnRefreshListener abOnRefreshListener) {
		mAbOnRefreshListener = abOnRefreshListener;
	}
	
	/**
	 * 描述：向滚动容器中添加View.
	 *
	 * @param child the child
	 * @param index the index
	 */
	public void addChildView(View child, int index) {
		mScrollLayout.addView(child,index);
	}

	/**
	 * 描述：向滚动容器中添加View.
	 *
	 * @param child the child
	 */
	public void addChildView(View child) {
		mScrollLayout.addView(child);
	}
	
	/**
	 * 
	 * 描述：获取Header View
	 * @return
	 * @throws 
	 */
	public AbListViewHeader getHeaderView() {
		return mHeaderView;
	}

	/**
	 * 
	 * 描述：获取Header ProgressBar，用于设置自定义样式
	 * @return
	 * @throws 
	 */
	public ProgressBar getHeaderProgressBar() {
		return mHeaderView.getHeaderProgressBar();
	}
	

}
