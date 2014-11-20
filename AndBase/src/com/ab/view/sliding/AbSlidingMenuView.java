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

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.ab.util.AbLogUtil;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbSlidingMenuView.java 
 * 描述：左右简单的侧边栏实现
 * 滑动事件只在一个android:clickable="true"的View上.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbSlidingMenuView extends ViewGroup {
	
	/** The m scroller. */
	private Scroller mScroller;
	
	/** The m velocity tracker. */
	private VelocityTracker mVelocityTracker;
	
	/** The m width. */
	private int mWidth;

	/** The Constant SCREEN_STATE_CLOSE. */
	public static final int SCREEN_STATE_CLOSE = 0;
	
	/** The Constant SCREEN_STATE_OPEN. */
	public static final int SCREEN_STATE_OPEN = 1;
	
	/** The Constant TOUCH_STATE_RESTART. */
	public static final int TOUCH_STATE_RESTART = 0;
	
	/** The Constant TOUCH_STATE_SCROLLING. */
	public static final int TOUCH_STATE_SCROLLING = 1;
	
	/** The Constant SCROLL_STATE_NO_ALLOW. */
	public static final int SCROLL_STATE_NO_ALLOW = 0;
	
	/** The Constant SCROLL_STATE_ALLOW. */
	public static final int SCROLL_STATE_ALLOW = 1;
	
	/** The m screen state. */
	private int mScreenState = 0;
	
	/** The m touch state. */
	private int mTouchState = 0;
	
	/** The m scroll state. */
	private int mScrollState = 0;
	
	/** The m velocity value. */
	private int mVelocityValue = 0;
	
	/** The m on click. */
	private boolean mOnClick = false;

	/**
	 * 构造.
	 * @param context the context
	 */
	public AbSlidingMenuView(Context context) {
		super(context);
		mScroller = new Scroller(context);
		mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,60, getResources().getDisplayMetrics());

	}

	/**
	 * 构造.
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbSlidingMenuView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 描述：View的位置设定.
	 *
	 * @param changed the changed
	 * @param l the l
	 * @param t the t
	 * @param r the r
	 * @param b the b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int height = child.getMeasuredHeight();
			int width = child.getMeasuredWidth();
			child.layout(0, 0, width, height);
		}
	}

	/**
	 * 描述：测量View的宽高.
	 *
	 * @param widthMeasureSpec the width measure spec
	 * @param heightMeasureSpec the height measure spec
	 * @see android.view.View#onMeasure(int, int)
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * 描述：执行触摸事件.
	 *
	 * @param ev the ev
	 * @return true, if successful
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	public boolean dispatchTouchEvent(MotionEvent ev) {
		
		obtainVelocityTracker(ev);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			AbLogUtil.d(AbSlidingMenuView.class, "--dispatchTouchEvent ACTION_DOWN--");
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART: TOUCH_STATE_SCROLLING;
			if (mTouchState == TOUCH_STATE_RESTART) {
				int x = (int) ev.getX();
				int screenWidth = getWidth();
				if (x <= mWidth && mScreenState == SCREEN_STATE_CLOSE && mTouchState == TOUCH_STATE_RESTART
						|| x >= screenWidth - mWidth && mScreenState == SCREEN_STATE_OPEN && mTouchState == TOUCH_STATE_RESTART) {
					if (mScreenState == SCREEN_STATE_OPEN) {
						mOnClick = true;
					}
					mScrollState = SCROLL_STATE_ALLOW;
				} else {
					mOnClick = false;
					mScrollState = SCROLL_STATE_NO_ALLOW;
				}
			} else {
				AbLogUtil.d(AbSlidingMenuView.class, "--dispatchTouchEvent ACTION_DOWN return false--");
				return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			AbLogUtil.d(AbSlidingMenuView.class, "--dispatchTouchEvent ACTION_MOVE--");
			mVelocityTracker.computeCurrentVelocity(1000,ViewConfiguration.getMaximumFlingVelocity());
			if (mScrollState == SCROLL_STATE_ALLOW && getWidth() - (int) ev.getX() < mWidth) {
				AbLogUtil.d(AbSlidingMenuView.class, "--dispatchTouchEvent ACTION_MOVE return true--");
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
			releaseVelocityTracker();
			if (mOnClick) {
				mOnClick = false;
				mScreenState = SCREEN_STATE_CLOSE;
				mScroller.startScroll(getChildAt(1).getScrollX(), 0,-getChildAt(1).getScrollX(), 0, 800);
				invalidate();
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	/**
	 * 描述：拦截的触摸事件.
	 *
	 * @param ev the ev
	 * @return true, if successful
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		obtainVelocityTracker(ev);
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			AbLogUtil.d(AbSlidingMenuView.class, "--onInterceptTouchEvent ACTION_DOWN--");
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
			if (mTouchState==TOUCH_STATE_SCROLLING) {
				AbLogUtil.d(AbSlidingMenuView.class, "--onInterceptTouchEvent ACTION_DOWN return false--");
				return false;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			AbLogUtil.d(AbSlidingMenuView.class, "--onInterceptTouchEvent ACTION_MOVE--");
			mOnClick = false;
			mVelocityTracker.computeCurrentVelocity(1000,ViewConfiguration.getMaximumFlingVelocity());
			if (mScrollState == SCROLL_STATE_ALLOW && Math.abs(mVelocityTracker.getXVelocity()) > 200) {
				AbLogUtil.d(AbSlidingMenuView.class, "--onInterceptTouchEvent ACTION_MOVE return true--");
				return true;
			}
			break;

		case MotionEvent.ACTION_UP:
			releaseVelocityTracker();
			if (mScrollState == SCROLL_STATE_ALLOW && mScreenState == SCREEN_STATE_OPEN) {
				return true;
			}
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 描述：触摸事件.
	 *
	 * @param event the event
	 * @return true, if successful
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	public boolean onTouchEvent(MotionEvent event) {
		obtainVelocityTracker(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			AbLogUtil.d(AbSlidingMenuView.class, "--onTouchEvent ACTION_DOWN--");
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
			if (mTouchState==TOUCH_STATE_SCROLLING) {
				AbLogUtil.d(AbSlidingMenuView.class, "--onTouchEvent ACTION_DOWN return false--");
				return false;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			AbLogUtil.d(AbSlidingMenuView.class, "--onTouchEvent ACTION_MOVE--");
			mVelocityTracker.computeCurrentVelocity(1000,ViewConfiguration.getMaximumFlingVelocity());
			mVelocityValue = (int) mVelocityTracker.getXVelocity();
			getChildAt(1).scrollTo(-(int) event.getX(), 0);
			break;

		case MotionEvent.ACTION_UP:
			if (mScrollState == SCROLL_STATE_ALLOW) {
				if (mVelocityValue > 2000) {
					mScreenState = SCREEN_STATE_OPEN;
					mScroller.startScroll(getChildAt(1).getScrollX(),0,-(getWidth()- Math.abs(getChildAt(1).getScrollX()) -mWidth), 0, 250);
					invalidate();

				} else if (mVelocityValue < -2000) {
					mScreenState = SCREEN_STATE_CLOSE;
					mScroller.startScroll(getChildAt(1).getScrollX(), 0,-getChildAt(1).getScrollX(), 0, 250);
					invalidate();
				} else if (event.getX() < getWidth() / 2) {
					mScreenState = SCREEN_STATE_CLOSE;
					mScroller.startScroll(getChildAt(1).getScrollX(), 0,-getChildAt(1).getScrollX(), 0, 800);
					invalidate();
				} else {
					mScreenState = SCREEN_STATE_OPEN;
					mScroller.startScroll(getChildAt(1).getScrollX(),0,-(getWidth()- Math.abs(getChildAt(1).getScrollX()) -mWidth), 0, 800);
					invalidate();
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 打开menu.
	 */
	public void open() {
		mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART: TOUCH_STATE_SCROLLING;
		if (mTouchState == TOUCH_STATE_RESTART) {
			mScreenState = SCREEN_STATE_OPEN;
			mScroller.startScroll(getChildAt(1).getScrollX(), 0, -(getWidth()- Math.abs(getChildAt(1).getScrollX()) -mWidth), 0, 800);
			invalidate();
		}
	}

	/**
	 * 关闭menu.
	 */
	public void close() {
		mScreenState = SCREEN_STATE_CLOSE;
		mScroller.startScroll(getChildAt(1).getScrollX(), 0, -getChildAt(1).getScrollX(), 0, 800);
		invalidate();
	}

	/**
	 * 描述：滚动.
	 *
	 * @see android.view.View#computeScroll()
	 */
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			getChildAt(1).scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	/**
	 * 初始化速度检测.
	 *
	 * @param event the event
	 */
	private void obtainVelocityTracker(MotionEvent event) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(event);
	}

	/**
	 * 释放速度检测.
	 */
	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	/**
	 * 获取当前状态.
	 * @return the screen state
	 */
	public int getScreenState() {
		return mScreenState;
	}

	/**
	 * 设置主View.
	 *
	 * @param view the new content view
	 */
	public void setContentView(View view) {
		removeViewAt(1);
		addView(view, 1, getLayoutParams());
	}

	
}
