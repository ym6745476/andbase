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
package com.ab.view.app;

import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.view.View;

import com.ab.util.AbLogUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbAnalogClock.java 
 * 描述：自定义模拟时钟
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-6 上午9:13:49
 */
public class AbAnalogClock extends View {
	
	/** The m calendar. */
	private Time mCalendar;

	//时针图标
	/** The m hour hand. */
	private Drawable mHourHand;
	
	//分针图标
	/** The m minute hand. */
	private Drawable mMinuteHand;
	
	//秒针图标
	/** The m second hand. */
	private Drawable mSecondHand;
		
	//表盘图标
	/** The m dial. */
	private Drawable mDial;

	//表盘图片的大小
	/** The m dial width. */
	private int mDialWidth;
	
	/** The m dial height. */
	private int mDialHeight;

	/** The m attached. */
	private boolean mAttached;
	
	//当前时间
	/** The m minutes. */
	private float mMinutes;
	
	/** The m hour. */
	private float mHour;
	
	/** The m second. */
	private float mSecond;
	
	/** The m handler. */
	private final Handler mHandler = new Handler();
	
	/** The m changed. */
	private boolean mChanged;

	/**
	 * Instantiates a new ab analog clock.
	 *
	 * @param context the context
	 * @param dial the dial
	 * @param hourHand the hour hand
	 * @param minuteHand the minute hand
	 * @param secondHand the second hand
	 */
	public AbAnalogClock(Context context, Drawable dial, Drawable hourHand,
			Drawable minuteHand,Drawable secondHand) {
		super(context);
		
		mDial = dial;
		mHourHand = hourHand;
		mMinuteHand = minuteHand;
		mSecondHand = secondHand;

		mCalendar = new Time();

		mDialWidth = mDial.getIntrinsicWidth();
		mDialHeight = mDial.getIntrinsicHeight();
	}

	/* (non-Javadoc)
	 * @see android.view.View#onAttachedToWindow()
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		if (!mAttached) {
			mAttached = true;
			IntentFilter filter = new IntentFilter();

			filter.addAction(Intent.ACTION_TIME_TICK);
			filter.addAction(Intent.ACTION_TIME_CHANGED);
			filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

			getContext().registerReceiver(mIntentReceiver, filter, null,mHandler);
		}

		// NOTE: It's safe to do these after registering the receiver since the
		// receiver always runs
		// in the main thread, therefore the receiver can't run before this
		// method returns.

		// The time zone may have changed while the receiver wasn't registered,
		// so update the Time
		mCalendar = new Time();

		// 更新当前时间每秒调用一次
		onTimeChanged();
		
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
			getContext().unregisterReceiver(mIntentReceiver);
			mAttached = false;
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		float hScale = 1.0f;
		float vScale = 1.0f;

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
			hScale = (float) widthSize / (float) mDialWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
			vScale = (float) heightSize / (float) mDialHeight;
		}

		float scale = Math.min(hScale, vScale);

		setMeasuredDimension(mDialWidth * (int)scale,mDialHeight * (int)scale);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mChanged = true;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		boolean changed = mChanged;
		if (changed) {
			mChanged = false;
		}

		int availableWidth = getRight() - getLeft();
		int availableHeight = getBottom() - getTop();

		int x = availableWidth / 2;
		int y = availableHeight / 2;

		final Drawable dial = mDial;
		int w = dial.getIntrinsicWidth();
		int h = dial.getIntrinsicHeight();

		boolean scaled = false;

		if (availableWidth < w || availableHeight < h) {
			scaled = true;
			float scale = Math.min((float) availableWidth / (float) w,
					(float) availableHeight / (float) h);
			canvas.save();
			canvas.scale(scale, scale, x, y);
		}

		if (changed) {
			dial.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		}
		dial.draw(canvas);

		//时针
		canvas.save();
		canvas.rotate(mHour / 12.0f * 360.0f, x, y);
		final Drawable hourHand = mHourHand;
		if (changed) {
			w = hourHand.getIntrinsicWidth();
			h = hourHand.getIntrinsicHeight();
			hourHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
					+ (h / 2));
		}
		hourHand.draw(canvas);
		canvas.restore();

		//分针
		canvas.save();
		canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);

		final Drawable minuteHand = mMinuteHand;
		if (changed) {
			w = minuteHand.getIntrinsicWidth();
			h = minuteHand.getIntrinsicHeight();
			minuteHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
					+ (h / 2));
		}
		minuteHand.draw(canvas);
		canvas.restore();
		
		//秒针
		canvas.save();
		canvas.rotate(mSecond / 60.0f * 360.0f, x, y);

		final Drawable secondHand = mSecondHand;
		if (changed) {
			w = secondHand.getIntrinsicWidth();
			h = secondHand.getIntrinsicHeight();
			secondHand.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y
					+ (h / 2));
		}
		secondHand.draw(canvas);
		canvas.restore();

		if (scaled) {
			canvas.restore();
		}
	}

	/**
	 * On time changed.
	 */
	private void onTimeChanged() {
		mCalendar.setToNow();

		int hour = mCalendar.hour;
		int minute = mCalendar.minute;
		int second = mCalendar.second;
		mSecond =  second;
		mMinutes = minute + second / 60.0f;
		mHour = hour + mMinutes / 60.0f;
		mChanged = true;
		AbLogUtil.d(AbAnalogClock.class, "时间改变:mHour:"+mHour+",mMinutes:"+mMinutes+",mSecond:"+mSecond);
		updateContentDescription(mCalendar);
        new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				onTimeChanged();
			}
		}, 1000);
        invalidate();
	}

	/** The m intent receiver. */
	private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				String tz = intent.getStringExtra("time-zone");
				mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
			}
		}
	};

	/**
	 * Update content description.
	 *
	 * @param time the time
	 */
	private void updateContentDescription(Time time) {
		final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;
		String contentDescription = DateUtils.formatDateTime(getContext(),
				time.toMillis(false), flags);
		setContentDescription(contentDescription);
	}
}
