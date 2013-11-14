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

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Display;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.view.listener.AbOnChangeListener;


// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbSlidingButton.java 
 * 描述：滑动按钮
 * @author zhaoqp
 * @date：2013-11-14 上午11:31:21
 * @version v1.0
 */
public class AbSlidingButton extends ViewGroup implements OnGestureListener{
	
	/** The m relative layout. */
	private RelativeLayout mRelativeLayout;
	
	/** The Constant ID_BTN1. */
	private static final int ID_BTN1 = 1;
    
    /** The Constant ID_BTN2. */
    private static final int ID_BTN2 = 2;
    
    /** The Constant ID_BTN3. */
    private static final int ID_BTN3 = 3;
    
    /** The Constant ID_BTN4. */
    private static final int ID_BTN4 = 4;
    
	/** The current btn. */
	private ImageButton btnLeft,btnRight,currentBtn;
	
	/** 是否是打开状态. */
	private boolean isChecked = false;
	
	/** The is aimation moving. */
	private boolean isAimationMoving = false;
	
	/** 0关  1开  2关中  3开中  . */
	private int state = 0;
	
	/** The a. */
	private int a = 0;
	
	/** The btn width. */
	int btnWidth = 40;
	
	/** The btn height. */
	int btnHeight = 40;
	
	/** 移动的距离. */
	int moveWidth = 45;
	
	/** 每次移动的距离. */
	int movePDis = 5;
	
	/** 覆盖按钮的宽. */
	int WidthOffset = 5;
	
	/** The detector. */
	private GestureDetector detector;
	
	/** 改变事件. */
	private AbOnChangeListener mSwitcherChangeListener;
	
	/** The m handler. */
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			//关
			if (msg.what == 0) {
				state = 2;
				int left = btnRight.getLeft();
				int right = btnRight.getRight();
				//0,45
				if(left-movePDis<=0){
					isChecked = false;
					isAimationMoving = false;
					state = 0;
				}else{
				     currentBtn.layout(left-movePDis, 0, right-movePDis, btnHeight);
				}
			}
			// 开
			else if (msg.what == 1) {
				state = 3;
				int left = btnLeft.getLeft();
				int right = btnLeft.getRight();
				//35,80
				if(left+movePDis>=moveWidth){
					isChecked = true;
					isAimationMoving = false;
					state = 1;
				}else{
				    currentBtn.layout(left+movePDis, 0,right+movePDis, btnHeight);
			    }
			//关
			}else if (msg.what == 3) {
				currentBtn = btnLeft;
				btnRight.setVisibility(View.GONE);
				btnLeft.setVisibility(View.VISIBLE);
				isChecked = false;
				isAimationMoving = false;
				state = 0;
			}
			// 开
			else if (msg.what == 4) {
				currentBtn = btnRight;
				btnLeft.setVisibility(View.GONE);
				btnRight.setVisibility(View.VISIBLE);
				isChecked = true;
				isAimationMoving = false;
				state = 1;
			}
		}
	};

	/**
	 * Instantiates a new ab slider button.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AbSlidingButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Instantiates a new ab slider button.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbSlidingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		
		
		btnWidth = 40;
		btnHeight = 40;
		//移动的距离
		moveWidth = 45;
		//每次移动的距离
		movePDis = 5;
		//覆盖按钮的宽
		WidthOffset = 5;
		
		WindowManager wManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);    	
		Display display = wManager.getDefaultDisplay();    	
		int width = display.getWidth();    	
		int height = display.getHeight();
		
		/*
		 2 px = 3 dip if dpi == 80(ldpi), 320x240 screen
		 1 px = 1 dip if dpi == 160(mdpi), 480x320 screen
		 3 px = 2 dip if dpi == 240(hdpi), 840x480 screen
		*/


		if(width<320){
			btnWidth = btnWidth-10;
			btnHeight =  btnHeight-10;
			moveWidth = moveWidth-10;
		}else if(width>320 && width<450){
			btnWidth = btnWidth+10;
			btnHeight =  btnHeight+10;
			moveWidth = moveWidth+10;
		}else if(width>=450){
			btnWidth = btnWidth+20;
			btnHeight =  btnHeight+20;
			moveWidth = moveWidth+20;
		}
		
		
		detector = new GestureDetector(this);
		
		mRelativeLayout = new RelativeLayout(context);
		
		ImageButton btnOn = new ImageButton(context);
		ImageButton btnOff = new ImageButton(context);
		btnLeft = new ImageButton(context);
		btnRight = new ImageButton(context);
		
		btnOn.setFocusable(false);
		btnOff.setFocusable(false);
		btnLeft.setFocusable(false);
		btnRight.setFocusable(false);

		
		Bitmap onImage = AbFileUtil.getBitmapFormSrc("image/button_on_bg.png");
		Bitmap offImage = AbFileUtil.getBitmapFormSrc("image/button_off_bg.png");
		Bitmap blockImage = AbFileUtil.getBitmapFormSrc("image/button_block.png");
		
		btnOn.setId(ID_BTN1);
		//btnOn.setImageBitmap(onImage);
		btnOn.setMinimumWidth(btnWidth);
		btnOn.setMinimumHeight(btnHeight);
		btnOn.setBackgroundDrawable(AbImageUtil.bitmapToDrawable(onImage));
		
		btnOff.setId(ID_BTN2);
		btnOff.setMinimumWidth(btnWidth);
		btnOff.setMinimumHeight(btnHeight);
		//btnOff.setImageBitmap(offImage);
		btnOff.setBackgroundDrawable(AbImageUtil.bitmapToDrawable(offImage));
		
		btnLeft.setId(ID_BTN3);
		btnLeft.setMinimumWidth(btnWidth+WidthOffset);
		btnLeft.setMinimumHeight(btnHeight);
		//btnLeft.setImageBitmap(blockImage);
		btnLeft.setBackgroundDrawable(AbImageUtil.bitmapToDrawable(blockImage));
		
		btnRight.setId(ID_BTN4);
		btnRight.setMinimumWidth(btnWidth+WidthOffset);
		btnRight.setMinimumHeight(btnHeight);
		//btnRight.setImageBitmap(blockImage);
		btnRight.setBackgroundDrawable(AbImageUtil.bitmapToDrawable(blockImage));
		
		mRelativeLayout.addView(btnOn);
		mRelativeLayout.setFocusable(false);
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.RIGHT_OF, ID_BTN1);
		mRelativeLayout.addView(btnOff, lp2);
		
		RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp3.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		mRelativeLayout.addView(btnLeft, lp3);
		
		RelativeLayout.LayoutParams lp4 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp4.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
		mRelativeLayout.addView(btnRight, lp4);
		
		addView(mRelativeLayout);
		
		mSwitcherChangeListener = new AbOnChangeListener(){

			@Override
			public void onChange(int position) {
				
			}
			
		};
		
		
		
	}
 
	/**
	 * Instantiates a new ab slider button.
	 *
	 * @param context the context
	 */
	public AbSlidingButton(Context context) {
		super(context);
	}
	
	
	/**
	 * 描述：TODO.
	 *
	 * @param widthMeasureSpec the width measure spec
	 * @param heightMeasureSpec the height measure spec
	 * @see android.view.View#onMeasure(int, int)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        
        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);
            // measure
            child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


	/**
	 * 描述：TODO.
	 *
	 * @param changed the changed
	 * @param l the l
	 * @param t the t
	 * @param r the r
	 * @param b the b
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int count = getChildCount();
        for(int i=0;i<count;i++){
            final View child = this.getChildAt(i);
            btnWidth = child.getMeasuredWidth();
            btnHeight = child.getMeasuredHeight();
            child.layout(0, 0, btnWidth, btnHeight);
        }
	}
	
	/**
	 * Sets the checked.
	 *
	 * @param checked the checked
	 * @param anim the anim
	 * @param changeEvent the change event
	 * @return true, if successful
	 */
	public boolean setChecked(boolean checked,boolean anim,final boolean changeEvent){
		isChecked = checked;
		if(anim){
			if(checked){
				a = 0;
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if(a<moveWidth){
							isAimationMoving = true;
							mHandler.sendEmptyMessage(1);
							mHandler.postDelayed(this,0);
						}else if(a==moveWidth){
						    isAimationMoving = true;
							mHandler.sendEmptyMessage(1);
							mHandler.removeCallbacks(this);
							if(changeEvent){
								mSwitcherChangeListener.onChange(1);
							}
							
						}else{
							isAimationMoving = false;
							state = 1;
						}
						a+=5;
					}
				},0);
			}else{
				a = 0;
				mHandler.postDelayed(new Runnable() {

					@Override
					public void run() {
						if(a<moveWidth){
							isAimationMoving = true;
							//不能小于move_x   arg1是移动距离，arg2是是否结束了
							mHandler.sendEmptyMessage(0);
							mHandler.postDelayed(this,0);
						}else if(a==moveWidth){
						    isAimationMoving = true;
							mHandler.sendEmptyMessage(0);
							mHandler.removeCallbacks(this);
							if(changeEvent){
								mSwitcherChangeListener.onChange(0);
							}
						}else{
							isAimationMoving = false;
							state = 0;
						}
						a+=movePDis;
						
					}
				}, 0);
			}
		}else{
            if(checked){
            	mHandler.sendEmptyMessage(4);
            	if(changeEvent){
					mSwitcherChangeListener.onChange(1);
				}
			}else{
				mHandler.sendEmptyMessage(3);
				if(changeEvent){
					mSwitcherChangeListener.onChange(0);
				}
			}
		}
		return true;
		
	}
	
	/**
	 * 描述：改变事件.
	 *
	 * @param switcherChangeListener the new switcher change listener
	 * @date：2012-8-8 下午3:53:00
	 * @version v1.0
	 */
	public void setSwitcherChangeListener(AbOnChangeListener switcherChangeListener){
		this.mSwitcherChangeListener = switcherChangeListener;
	}
	
	/**
	 * 描述：TODO.
	 *
	 * @param ev the ev
	 * @return true, if successful
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		detector.onTouchEvent(ev);
		super.dispatchTouchEvent(ev);
		return true;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param e the e
	 * @return true, if successful
	 * @see android.view.GestureDetector.OnGestureListener#onDown(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public boolean onDown(MotionEvent e) {
		return false;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param e the e
	 * @see android.view.GestureDetector.OnGestureListener#onShowPress(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public void onShowPress(MotionEvent e) {
	}

	/**
	 * 描述：TODO.
	 *
	 * @param e the e
	 * @return true, if successful
	 * @see android.view.GestureDetector.OnGestureListener#onSingleTapUp(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		if (isAimationMoving) {
			return true;
		}
		if (isChecked) {
			a = 0;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if(a<moveWidth){
						isAimationMoving = true;
						mHandler.sendEmptyMessage(0);
						mHandler.postDelayed(this,0);
					}else if(a==moveWidth){
					    isAimationMoving = true;
						mHandler.sendEmptyMessage(3);
						mHandler.removeCallbacks(this);
						//关
						mSwitcherChangeListener.onChange(0);
					}else{
						isAimationMoving = false;
						state = 0;
					}
					a+=movePDis;
					
				}
			}, 0);
		} else {
			a = 0;
			mHandler.postDelayed(new Runnable() {

				@Override
				public void run() {
					if(a<moveWidth){
						isAimationMoving = true;
						mHandler.sendEmptyMessage(1);
						mHandler.postDelayed(this,0);
					}else if(a==moveWidth){
					    isAimationMoving = true;
						mHandler.sendEmptyMessage(4);
						mHandler.removeCallbacks(this);
						//开
						mSwitcherChangeListener.onChange(1);
					}else{
						isAimationMoving = false;
						state = 1;
					}
					a+=movePDis;
				}
			}, 0);
		}
		return true;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param e1 the e1
	 * @param e2 the e2
	 * @param distanceX the distance x
	 * @param distanceY the distance y
	 * @return true, if successful
	 * @see android.view.GestureDetector.OnGestureListener#onScroll(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		return false;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param e the e
	 * @see android.view.GestureDetector.OnGestureListener#onLongPress(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public void onLongPress(MotionEvent e) {
	}

	/**
	 * 描述：TODO.
	 *
	 * @param e1 the e1
	 * @param e2 the e2
	 * @param velocityX the velocity x
	 * @param velocityY the velocity y
	 * @return true, if successful
	 * @see android.view.GestureDetector.OnGestureListener#onFling(android.view.MotionEvent, android.view.MotionEvent, float, float)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:46
	 * @version v1.0
	 */
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return true;
	}

	/**
	 * 
	 * 描述：是否当前是选中
	 * @return
	 */
	public boolean isChecked() {
		return isChecked;
	}

	
	
	
}
