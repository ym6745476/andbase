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
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.CheckBox;

import com.ab.global.AbAppData;


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
public class AbSlidingButton extends CheckBox  {
	
	/** 记录日志的标记. */
	private String TAG = AbSlidingButton.class.getSimpleName();
	
	/** 记录日志的开关. */
	private boolean D = AbAppData.DEBUG;

	private Context context;
	//图片资源
	private Bitmap btnFrame;
	private Bitmap btnMask;
	private Bitmap btnBottom;
	private Bitmap mBtnNormal;
	private Bitmap mBtnPressed;
	private OnCheckedChangeListener onCheckedChangeListener;
	//记录开关当前的状态
	private boolean isChecked;
	
	//背景的宽高
	private float mMaskHeight;
	private float mMaskWidth;
	
	private float mBtnOffPos;
	private float mBtnOnPos;
	private float mBtnPos;
	private float mLastBtnPos;
	private float mRealPos;
	private float mBtnWidth;
	
	//Y方向的延伸
	private float mExtendOffsetY;
	private int mAlpha;
	private Paint mPaint;
	private PorterDuffXfermode mXfermode;
	private RectF mSaveLayerRectF;
	private Bitmap mCurBtnPic;
	private float mFirstDownX;
	private boolean mMoveEvent;
	private boolean mAnimating;
	private float mAnimationPosition;
	private float mAnimatedVelocity;
	
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 0:
					mRealPos = getRealPos((Float)msg.obj);
					mLastBtnPos = (Float)msg.obj;
					invalidate();
					break;
				default:
					break;
			}
		}
	};
	
	public AbSlidingButton(Context context) {
		super(context);
		init(context);
	}
	
	public AbSlidingButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		this.context = context;
	    this.mAlpha = 255;
	    this.isChecked = false;
	    
	    this.mPaint = new Paint();
	    this.mPaint.setColor(Color.WHITE);
	    
	    float density = getResources().getDisplayMetrics().density;
		this.mAnimatedVelocity = (int)(0.5F + 350.0F * density);
	}
	
	protected void onDraw(Canvas canvas){
		canvas.saveLayerAlpha(this.mSaveLayerRectF, this.mAlpha, 31);
		canvas.drawBitmap(this.btnMask, 0F, this.mExtendOffsetY, this.mPaint);
	    this.mPaint.setXfermode(this.mXfermode);
	    canvas.drawBitmap(this.btnBottom, this.mRealPos, this.mExtendOffsetY, this.mPaint);
	    this.mPaint.setXfermode(null);
	    canvas.drawBitmap(this.btnFrame, 0F, this.mExtendOffsetY, this.mPaint);
	    canvas.drawBitmap(this.mCurBtnPic, this.mRealPos, 0.40000000596046448F + this.mExtendOffsetY, this.mPaint);
	    canvas.restore();
	}

	

	/**
	 * 描述：测量View的宽高.
	 *
	 * @param widthMeasureSpec the width measure spec
	 * @param heightMeasureSpec the height measure spec
	 * @see android.view.View#onMeasure(int, int)
	 */
	 protected void onMeasure(int paramInt1, int paramInt2){
	    setMeasuredDimension((int)this.mMaskWidth, (int)(this.mMaskHeight + 2F * this.mExtendOffsetY));
	 }
	
	
	/**
	 * 
	 * 描述：设置图片资源
	 * @param btnBg  背景
	 * @param btnBlock 滑块
	 * @throws 
	 * @date：2013-11-29 下午3:26:42
	 * @version v1.0
	 */
	public void setImageResource(int btnBottomResource,int btnFrameResource,int btnMaskResource, int btnNormalResource,int btnPressedResource) {
		btnBottom = BitmapFactory.decodeResource(context.getResources(),btnBottomResource);
		btnFrame = BitmapFactory.decodeResource(context.getResources(),btnFrameResource);
		btnMask = BitmapFactory.decodeResource(context.getResources(),btnMaskResource);
		mBtnNormal = BitmapFactory.decodeResource(context.getResources(),btnNormalResource);
		mBtnPressed = BitmapFactory.decodeResource(context.getResources(),btnPressedResource);
		this.mMaskWidth = this.btnMask.getWidth();
	    this.mMaskHeight = this.btnMask.getHeight();
	    
	    float density = getResources().getDisplayMetrics().density;
	    this.mExtendOffsetY = (int)(0.5F + density * 0F);
	    
	    this.mSaveLayerRectF = new RectF(-20.0F, this.mExtendOffsetY, 20 + this.mMaskWidth, this.mMaskHeight + this.mExtendOffsetY);
	    this.mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
	    this.mCurBtnPic = this.mBtnNormal;
	    this.mBtnWidth = this.mBtnPressed.getWidth();
	    this.mBtnOnPos = (this.mBtnWidth / 2F);
	    this.mBtnOffPos = (this.mMaskWidth - this.mBtnWidth / 2F);
	    if (isChecked){
	    	this.mBtnPos = mBtnOnPos;
	    }else{
	    	this.mBtnPos = mBtnOffPos;
	    }
	    
	    this.mRealPos = getRealPos(this.mBtnPos);
	    
	}
	
	private float getRealPos(float paramFloat){
	    return (paramFloat - this.mBtnWidth / 2F);
	}
	

	/**
	 * 
	 * 描述：获得当前
	 * @see android.widget.CompoundButton#isChecked()
	 * @author: zhaoqp
	 * @date：2013-11-29 下午3:54:47
	 * @version v1.0
	 */
	public boolean isChecked() {
		return isChecked;
	}
	
	/**
	 * 
	 * 描述：设置选中
	 * @see android.widget.CompoundButton#setChecked(boolean)
	 * @author: zhaoqp
	 * @date：2013-11-29 下午3:54:33
	 * @version v1.0
	 */
	public void setChecked(boolean checked) {
		setChecked(checked,false);
	}

	/**
	 * 
	 * 描述：设置选中
	 * @see android.widget.CompoundButton#setChecked(boolean)
	 * @author: zhaoqp
	 * @date：2013-11-29 下午3:54:33
	 * @version v1.0
	 */
	public void setChecked(boolean checked,boolean anim) {
		if(checked){
			this.mBtnPos = this.mBtnOnPos;
		}else{
		    this.mBtnPos = this.mBtnOffPos;
		}
		if(anim){
			startAnimation();
		}else{
			moveViewToTarget();
		}
			
	}


	/**
	 * 
	 * 描述：设置监听器
	 * @see android.widget.CompoundButton#setOnCheckedChangeListener(android.widget.CompoundButton.OnCheckedChangeListener)
	 * @author: zhaoqp
	 * @date：2013-11-29 下午3:54:20
	 * @version v1.0
	 */
	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		onCheckedChangeListener = listener;
	}
	
	/**
	 * 
	 * 描述：滑动事件
	 * @see android.widget.TextView#onTouchEvent(android.view.MotionEvent)
	 * @author: zhaoqp
	 * @date：2013-12-5 上午11:22:12
	 * @version v1.0
	 */
	public boolean onTouchEvent(MotionEvent event){
		if(this.mAnimating){
			return true;
		}
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			 mMoveEvent = false;
			 //当前点击位置
			 this.mFirstDownX = event.getX();
			 //点击效果
		     this.mCurBtnPic = this.mBtnPressed;
		     //当前滑块再的位置
		     if (this.isChecked){
		    	 this.mBtnPos = this.mBtnOnPos;
		     }else{
		    	 this.mBtnPos = this.mBtnOffPos;
		     }
		     mLastBtnPos = mBtnPos;
		     if(D)Log.d(TAG, "原来的X位置："+this.mBtnPos);
		     break;
		case MotionEvent.ACTION_MOVE:
			 if(D)Log.d(TAG, "－－－－移动－－－－");
			 //当前点击位置
			 float x = event.getX();
			 //差
			 float offsetX = x - this.mFirstDownX;
			 if(D)Log.d(TAG, "X需要移动："+offsetX);
		     
		     //转换为点击事件
		     if(Math.abs(offsetX) < 5){
		    	 break;
		     }else{
		    	 mMoveEvent = true;
		     }
		     
		     this.mFirstDownX = event.getX();
			 
		     //移动后的应该在的位置
		     this.mBtnPos = this.mBtnPos + offsetX;
		     if(D)Log.d(TAG, "现在的X位置："+this.mBtnPos);
		     //超出控件的设置
		     if (this.mBtnPos < this.mBtnOffPos){
		          this.mBtnPos = this.mBtnOffPos;
		     }
		     
		     if (this.mBtnPos > this.mBtnOnPos){
		          this.mBtnPos = this.mBtnOnPos;
		     }
		     
		     moveViewToTarget();
			 break;
		default:
			if(mMoveEvent){
				 if(D)Log.d(TAG, "－－－－弹起－－－－");
				 //弹起
			     this.mCurBtnPic = this.mBtnNormal;
			     //本次移动最后结果
			     if (this.mBtnPos < (this.mBtnOnPos-this.mBtnOffPos) / 2F + this.mBtnOffPos){
			    	 this.mBtnPos = this.mBtnOffPos;
			     }else{
			    	 this.mBtnPos = this.mBtnOnPos;
			     }
			     startAnimation();
			     offsetX = 0;
			}else{
				//点击事件
				setChecked(!isChecked,true);
			}
			
			 break;
		}
		
		return true;
	}
	
	/**
	 * 
	 * 描述：滑块移动
	 * @param pos
	 * @throws 
	 * @date：2013-12-5 上午11:46:56
	 * @version v1.0
	 */
	private void moveView(float pos){
		moveView(pos,false);
	}
	
	/**
	 * 
	 * 描述：滑块移动
	 * @param pos
	 * @param delay
	 * @throws 
	 * @date：2013-12-5 上午11:46:56
	 * @version v1.0
	 */
	private void moveView(final float pos,boolean delay){
		if(handler!=null){
			handler.obtainMessage(0, pos).sendToTarget();
		}
	}

	/**
	 * 
	 * 描述：用位移加速度实现动画
	 * @throws 
	 * @date：2013-12-9 上午9:47:59
	 * @version v1.0
	 */
	private void startAnimation(){
		//已经在目标位置
		if(mLastBtnPos == this.mBtnPos){
			return;
 	    }
		this.mAnimating = true;
		if(D)Log.d(TAG, "目标移动X到："+this.mBtnPos+",当前在:"+mLastBtnPos);
    	float mVelocity = this.mAnimatedVelocity;
 	    if(mLastBtnPos > this.mBtnPos){
 	    	 mVelocity = -this.mAnimatedVelocity;
 	    }
 	    this.mAnimationPosition = mLastBtnPos;
 	    int i  = 0;
    	while(true){
    		this.mAnimationPosition = (this.mAnimationPosition + 16.0F * mVelocity / 1000.0F);
    		if(D)Log.d(TAG, i+"次移动X到："+this.mAnimationPosition);
    		if(this.mAnimationPosition >= this.mBtnOnPos){
    			this.mAnimationPosition = this.mBtnOnPos;
    			moveView(this.mAnimationPosition,true);
    			if(!isChecked){
	   		    	 isChecked = true;
	   		    	 if(onCheckedChangeListener!=null){
	   		 			onCheckedChangeListener.onCheckedChanged(this, isChecked);
	   		 		 }
   	    	    }
    			break;
    		}else if(this.mAnimationPosition <= this.mBtnOffPos){
    			this.mAnimationPosition = this.mBtnOffPos;
    			moveView(this.mAnimationPosition,true);
    			if(isChecked){
	   		    	 isChecked = false;
	   		    	 if(onCheckedChangeListener!=null){
	   		 			onCheckedChangeListener.onCheckedChanged(this, isChecked);
	   		 		 }
  	    	    }
    			break;
    		}else{
    			moveView(this.mAnimationPosition,true);
    		}
    		i++;
    	}
    	this.mAnimating = false;
    	
	}
	
	/**
	 * 
	 * 描述：直接移动到位置
	 * @throws 
	 * @date：2013-12-9 上午9:20:38
	 * @version v1.0
	 */
	private void moveViewToTarget(){
		moveView(this.mBtnPos);
	    if (this.mBtnPos == this.mBtnOnPos){
	    	 if(!isChecked){
		    	 isChecked = true;
		    	 if(onCheckedChangeListener!=null){
		 			onCheckedChangeListener.onCheckedChanged(this, isChecked);
		 		 }
	    	 }
		     return;
		}else if(this.mBtnPos == this.mBtnOffPos){
			if(isChecked){
				 isChecked = false;
				 if(onCheckedChangeListener!=null){
						onCheckedChangeListener.onCheckedChanged(this, isChecked);
				 }
			 }
		     return;
		}
	}
	

	@Override
	public boolean performClick() {
		setChecked(!isChecked);
		return true;
	}

	@Override
	public void toggle() {
		this.setChecked(!isChecked);
	}
	
	
}
