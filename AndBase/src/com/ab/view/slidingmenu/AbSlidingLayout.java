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
package com.ab.view.slidingmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

// TODO: Auto-generated Javadoc
/**
 * 描述：侧边栏实现.
 *
 * @author zhaoqp
 * @date：2013-4-24 下午3:46:47
 * @version v1.0
 */
public class AbSlidingLayout extends ViewGroup {

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
	 * Instantiates a new ab flipper layout.
	 *
	 * @param context the context
	 */
	public AbSlidingLayout(Context context) {
		super(context);
		mScroller = new Scroller(context);
		mWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,54, getResources().getDisplayMetrics());

	}

	/**
	 * Instantiates a new ab flipper layout.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AbSlidingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Instantiates a new ab flipper layout.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbSlidingLayout(Context context, AttributeSet attrs) {
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
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:51
	 * @version v1.0
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
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:51
	 * @version v1.0
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
				return false;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			mVelocityTracker.computeCurrentVelocity(1000,ViewConfiguration.getMaximumFlingVelocity());
			if (mScrollState == SCROLL_STATE_ALLOW && getWidth() - (int) ev.getX() < mWidth) {
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
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
			if (mTouchState==TOUCH_STATE_SCROLLING) {
				return false;
			}
			break;

		case MotionEvent.ACTION_MOVE:
			mOnClick = false;
			mVelocityTracker.computeCurrentVelocity(1000,ViewConfiguration.getMaximumFlingVelocity());
			if (mScrollState == SCROLL_STATE_ALLOW && Math.abs(mVelocityTracker.getXVelocity()) > 200) {
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
			mTouchState = mScroller.isFinished() ? TOUCH_STATE_RESTART : TOUCH_STATE_SCROLLING;
			if (mTouchState==TOUCH_STATE_SCROLLING) {
				return false;
			}
			break;

		case MotionEvent.ACTION_MOVE:
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
	 * Open.
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
	 * Close.
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
	 * Obtain velocity tracker.
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
	 * Release velocity tracker.
	 */
	private void releaseVelocityTracker() {
		if (mVelocityTracker != null) {
			mVelocityTracker.recycle();
			mVelocityTracker = null;
		}
	}

	/**
	 * Gets the screen state.
	 *
	 * @return the screen state
	 */
	public int getScreenState() {
		return mScreenState;
	}

	/**
	 * Sets the content view.
	 *
	 * @param view the new content view
	 */
	public void setContentView(View view) {
		removeViewAt(1);
		addView(view, 1, getLayoutParams());
	}

	/**
	 * The listener interface for receiving onOpen events.
	 * The class that is interested in processing a onOpen
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnOpenListener<code> method. When
	 * the onOpen event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnOpenEvent
	 */
	public interface OnOpenListener {
		
		/**
		 * Open.
		 */
		public abstract void open();
	}

	/**
	 * The listener interface for receiving onClose events.
	 * The class that is interested in processing a onClose
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnCloseListener<code> method. When
	 * the onClose event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnCloseEvent
	 */
	public interface OnCloseListener {
		
		/**
		 * Close.
		 */
		public abstract void close();
	}
}
