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
import android.content.Context;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbSlidingPageView.java 
 * 描述：页面滚动切换,实现焦点触发和Touch，适用于电视开发.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbSlidingPageView extends ViewGroup {
	
	/** 记录日志的标记. */
	private String TAG = AbSlidingPageView.class.getSimpleName();
	
	/** 记录日志的开关. */
	private boolean D = true;

	/** 滚动器. */
	private Scroller mScroller;
	
	/** 速度. */
	private VelocityTracker mVelocityTracker;
	
	/** 显示的下一个View. */
	public static final int SCREEN_STATE_NEXT = 0;
	
	/** 显示的前一个View. */
	public static final int SCREEN_STATE_PROVIOUS = 1;
	
	/** 当前屏幕状态. */
	private int mScreenState = SCREEN_STATE_PROVIOUS;
	
	/** 下一个View的偏移. */
	private int nextViewOffset = 50;
	
	/** 页面切换监听器. */
	private  OnPageChangeListener onPageChangeListener = null;
	
	/** 目标滚动是否完成. */
	private boolean finish = true;

	/**
	 * 构造.
	 * @param context the context
	 */
	public AbSlidingPageView(Context context) {
		super(context);
		mScroller = new Scroller(context);

	}

	/**
	 * 构造.
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbSlidingPageView(Context context, AttributeSet attrs) {
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
        //初始布局		
		for (int i = 0; i < getChildCount(); i++) {
			View child = getChildAt(i);
			int height = child.getMeasuredHeight();
			int width = child.getMeasuredWidth();
			child.setFocusable(true);
			if(D) Log.d(TAG, "--onLayout--:"+width);
			if(i==0){
				child.layout(-nextViewOffset,0 ,nextViewOffset+width , height);
			}else{
				child.layout(width-nextViewOffset,0,2*width-nextViewOffset, height);
			}
		}
		
	}

	/**
	 * 描述：测量View的宽高.
	 * @param widthMeasureSpec the width measure spec
	 * @param heightMeasureSpec the height measure spec
	 * @see android.view.View#onMeasure(int, int)
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		if(D) Log.d(TAG, "--onMeasure--");
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);
		for (int i = 0; i < getChildCount(); i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * 打开下一个View.
	 */
	public void showNext() {
        if(!finish ||  mScreenState==SCREEN_STATE_NEXT){
			return;
		}
        finish = false;
        mScreenState = SCREEN_STATE_NEXT;
		if(onPageChangeListener!=null){
			onPageChangeListener.onPageSelected(1);
		}
		if(D) Log.d(TAG, "--showNext--:"+getScrollX()+" dx "+(getChildAt(1).getWidth()-2*nextViewOffset));
		//始终要以onlayout的位置作为最初始位置计算
		mScroller.startScroll(getScrollX(), 0, getChildAt(1).getWidth()-2*nextViewOffset, 0, 800);
		invalidate();
	}

	/**
	 * 返回上一个View.
	 */
	public void showPrevious() {
		if(!finish || mScreenState==SCREEN_STATE_PROVIOUS){
			return;
		}
		mScreenState = SCREEN_STATE_PROVIOUS;
		if(onPageChangeListener!=null){
			onPageChangeListener.onPageSelected(0);
		}
		if(D) Log.d(TAG, "--showPrevious--:"+getScrollX()+" dx -"+getScrollX());
		//滚动回onlayout的0的位置
		mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 800);
		invalidate();
	}

	/**
	 * 描述：滚动.
	 * @see android.view.View#computeScroll()
	 */
	public void computeScroll() {
		super.computeScroll();
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
		//这次滚动结束
		if(mScroller.getFinalX()==mScroller.getCurrX()){
			finish = true;
		}
	}

	/**
	 * 初始化速度检测.
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
	 * @param view the view
	 */
	public void addContentView(View view) {
		addView(view,0, getLayoutParams());
		view.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					 showPrevious();
				}
				
			}
	    });
		
		view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_DOWN == event.getAction()){
					showPrevious();
				}
				return false;
			}
		});
	}
	
	/**
	 * 设置下一个View.
	 *
	 * @param view the view
	 */
	public void addNextView(View view) {
		addView(view,1, getLayoutParams());
        view.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					showNext();
				}
				
			}
	    });
        
        view.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(MotionEvent.ACTION_DOWN == event.getAction()){
					showNext();
				}
				return false;
			}
		});
	}

	/**
	 * Gets the next view offset.
	 *
	 * @return the next view offset
	 */
	public int getNextViewOffset() {
		return nextViewOffset;
	}

	/**
	 * 设置遮挡距离.
	 *
	 * @param nextViewOffset the new next view offset
	 */
	public void setNextViewOffset(int nextViewOffset) {
		this.nextViewOffset = nextViewOffset;
	}

	
	/**
	 * Gets the on page change listener.
	 *
	 * @return the on page change listener
	 */
	public OnPageChangeListener getOnPageChangeListener() {
		return onPageChangeListener;
	}

    /**
     * 设置页面改变监听器.
     *
     * @param onPageChangeListener the new on page change listener
     */
	public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
		this.onPageChangeListener = onPageChangeListener;
	}
	
}
