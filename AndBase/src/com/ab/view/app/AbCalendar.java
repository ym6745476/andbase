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

import java.util.List;
import java.util.TimeZone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbCalendar.java 
 * 描述：自定义显示日期的view
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-7 下午2:35:05
 */
public class AbCalendar extends View {
	
	/** The m calendar. */
	private Time mCalendar;

	/** The m cale bg. */
	private Drawable mCaleBg;
	
	/** The m cale dot. */
	private Drawable mCaleDot;
	
	/** The m pos year. */
	private Point mPosYear;
	
	/** The d array year. */
	private List<Drawable> dArrayYear;
	
	/** The m pos month. */
	private Point mPosMonth;
	
	/** The d array month. */
	private List<Drawable> dArrayMonth;
	
	/** The m pos date. */
	private Point mPosDate;
	
	/** The d array date. */
	private List<Drawable> dArrayDate;
	
	/** The m pos week. */
	private Point mPosWeek;
	
	/** The d array week. */
	private List<Drawable> dArrayWeek;

	/** The m bg width. */
	private int mBgWidth;
	
	/** The m bg height. */
	private int mBgHeight;

	/** The m date text size. */
	private float mDateTextSize;

	/** The date changed receiver. */
	private final BroadcastReceiver dateChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
				String tz = intent.getStringExtra("time-zone");
				mCalendar = new Time(TimeZone.getTimeZone(tz).getID());
			}
			invalidate();
		}
	};
	
	/** The m attached. */
	private boolean mAttached;

	/**
	 * Instantiates a new ab calendar.
	 *
	 * @param context the context
	 * @param caleBg the cale bg
	 * @param caleDot the cale dot
	 * @param posYear the pos year
	 * @param dYearArray the d year array
	 * @param posMonth the pos month
	 * @param dMonthArray the d month array
	 * @param posDate the pos date
	 * @param dDateArray the d date array
	 * @param posWeek the pos week
	 * @param dWeekArray the d week array
	 */
	public AbCalendar(Context context, Drawable caleBg, Drawable caleDot,
			Point posYear, List<Drawable> dYearArray, Point posMonth,
			List<Drawable> dMonthArray, Point posDate,
			List<Drawable> dDateArray, Point posWeek, List<Drawable> dWeekArray) {

		super(context);

		mCaleBg = caleBg;
		mCaleDot = caleDot;
		mPosYear = posYear;
		dArrayYear = dYearArray;
		mPosMonth = posMonth;
		dArrayMonth = dMonthArray;
		mPosDate = posDate;
		dArrayDate = dDateArray;
		mPosWeek = posWeek;
		dArrayWeek = dWeekArray;

		mBgWidth = mCaleBg.getIntrinsicWidth();
		mBgHeight = mCaleBg.getIntrinsicHeight();

		mCalendar = new Time();
		mDateTextSize = 14;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onAttachedToWindow()
	 */
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (!mAttached) {
			mAttached = true;
			IntentFilter intentFilter = new IntentFilter(
					Intent.ACTION_DATE_CHANGED);
			getContext().registerReceiver(dateChangedReceiver, intentFilter);
		}
		mCalendar = new Time();
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDetachedFromWindow()
	 */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mAttached) {
			mAttached = false;
			getContext().unregisterReceiver(dateChangedReceiver);
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

		if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mBgWidth) {
			hScale = (float) widthSize / (float) mBgWidth;
		}

		if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mBgHeight) {
			vScale = (float) heightSize / (float) mBgHeight;
		}

		float scale = Math.min(hScale, vScale);
		setMeasuredDimension(mBgWidth * (int)scale,mBgHeight * (int)scale);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onSizeChanged(int, int, int, int)
	 */
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int availableWidth = getRight() - getLeft();
		int availableHeight = getBottom() - getTop();

		int x = availableWidth / 2;
		int y = availableHeight / 2;

		final Drawable bg = mCaleBg;
		int w = bg.getIntrinsicWidth();
		int h = bg.getIntrinsicHeight();

		boolean scaled = false;

		if (availableWidth < w || availableHeight < h) {
			scaled = true;
			float scale = Math.min((float) availableWidth / (float) w,
					(float) availableHeight / (float) h);
			canvas.save();
			canvas.scale(scale, scale, x, y);
		}

		bg.setBounds(x - (w / 2), y - (h / 2), x + (w / 2), y + (h / 2));
		bg.draw(canvas);
		canvas.save();

		mCalendar.setToNow();

		if ((!dArrayYear.isEmpty() && dArrayYear.size() > 0)
				|| (!dArrayMonth.isEmpty() && dArrayMonth.size() > 0)
				|| (!dArrayDate.isEmpty() && dArrayDate.size() > 0)
				|| (!dArrayWeek.isEmpty() && dArrayWeek.size() > 0)) {
			if (!dArrayYear.isEmpty() && dArrayYear.size() > 0) {
				String sYear = String.format("%04d", mCalendar.year);
				int pos = Integer.parseInt(sYear.substring(0, 1));
				Drawable dBmpYear = dArrayYear.get(pos);
				int bmpW = dBmpYear.getIntrinsicWidth();
				int bmpH = dBmpYear.getIntrinsicHeight();
				dBmpYear.setBounds(x - (w / 2) + mPosYear.x, y - (h / 2)
						+ mPosYear.y, x - (w / 2) + mPosYear.x + bmpW, y
						- (h / 2) + mPosYear.y + bmpH);
				dBmpYear.draw(canvas);
				pos = Integer.parseInt(sYear.substring(1, 2));
				dBmpYear = dArrayYear.get(pos);
				dBmpYear.setBounds(x - (w / 2) + mPosYear.x + bmpW, y - (h / 2)
						+ mPosYear.y, x - (w / 2) + mPosYear.x + 2 * bmpW, y
						- (h / 2) + mPosYear.y + bmpH);
				dBmpYear.draw(canvas);
				pos = Integer.parseInt(sYear.substring(2, 3));
				dBmpYear = dArrayYear.get(pos);
				dBmpYear.setBounds(x - (w / 2) + mPosYear.x + 2 * bmpW, y
						- (h / 2) + mPosYear.y, x - (w / 2) + mPosYear.x + 3
						* bmpW, y - (h / 2) + mPosYear.y + bmpH);
				dBmpYear.draw(canvas);
				pos = Integer.parseInt(sYear.substring(3, 4));
				dBmpYear = dArrayYear.get(pos);
				dBmpYear.setBounds(x - (w / 2) + mPosYear.x + 3 * bmpW, y
						- (h / 2) + mPosYear.y, x - (w / 2) + mPosYear.x + 4
						* bmpW, y - (h / 2) + mPosYear.y + bmpH);
				dBmpYear.draw(canvas);
				if ((mCaleDot != null) && (mPosYear.y == mPosMonth.y)) {
					int dotW = mCaleDot.getIntrinsicWidth();
					int dotH = mCaleDot.getIntrinsicHeight();
					if (dotH < bmpW) {
						mCaleDot.setBounds(x - (w / 2) + mPosYear.x + 4 * bmpW,
								y - (h / 2) + mPosYear.y + (bmpH - dotH) / 2, x
										- (w / 2) + mPosYear.x + 4 * bmpW
										+ dotW, y - (h / 2) + mPosYear.y
										+ (bmpH - dotH) / 2 + dotH);
					} else {
						mCaleDot.setBounds(x - (w / 2) + mPosYear.x + 4 * bmpW,
								y - (h / 2) + mPosYear.y, x - (w / 2)
										+ mPosYear.x + 4 * bmpW + dotW, y
										- (h / 2) + mPosYear.y + dotH);
					}
					mCaleDot.draw(canvas);
				}
			}
			if (!dArrayMonth.isEmpty() && dArrayMonth.size() > 0) {
				if (dArrayMonth.size() > 10) {
					Drawable dBmpMonth = dArrayMonth.get(mCalendar.month);
					int bmpW = dBmpMonth.getIntrinsicWidth();
					int bmpH = dBmpMonth.getIntrinsicHeight();
					dBmpMonth.setBounds(x - (w / 2) + mPosMonth.x, y - (h / 2)
							+ mPosMonth.y, x - (w / 2) + mPosMonth.x + bmpW, y
							- (h / 2) + mPosMonth.y + bmpH);
					dBmpMonth.draw(canvas);
				} else {
					String sMonth = String.format("%02d", mCalendar.month + 1);
					int pos = Integer.parseInt(sMonth.substring(0, 1));
					Drawable dBmpMonth = dArrayMonth.get(pos);
					int bmpW = dBmpMonth.getIntrinsicWidth();
					int bmpH = dBmpMonth.getIntrinsicHeight();

					dBmpMonth.setBounds(x - (w / 2) + mPosMonth.x, y - (h / 2)
							+ mPosMonth.y, x - (w / 2) + mPosMonth.x + bmpW, y
							- (h / 2) + mPosMonth.y + bmpH);
					dBmpMonth.draw(canvas);
					pos = Integer.parseInt(sMonth.substring(1, 2));
					dBmpMonth = dArrayMonth.get(pos);
					dBmpMonth.setBounds(x - (w / 2) + mPosMonth.x + bmpW, y
							- (h / 2) + mPosMonth.y, x - (w / 2) + mPosMonth.x
							+ 2 * bmpW, y - (h / 2) + mPosMonth.y + bmpH);
					dBmpMonth.draw(canvas);
					if ((mCaleDot != null) && (mPosMonth.y == mPosDate.y)) {
						int dotW = mCaleDot.getIntrinsicWidth();
						int dotH = mCaleDot.getIntrinsicHeight();
						if (dotH < bmpW) {
							mCaleDot.setBounds(x - (w / 2) + mPosMonth.x + 2
									* bmpW, y - (h / 2) + mPosMonth.y
									+ (bmpH - dotH) / 2, x - (w / 2)
									+ mPosMonth.x + 2 * bmpW + dotW, y
									- (h / 2) + mPosMonth.y + (bmpH - dotH) / 2
									+ dotH);
						} else {
							mCaleDot.setBounds(x - (w / 2) + mPosMonth.x + 2
									* bmpW, y - (h / 2) + mPosMonth.y, x
									- (w / 2) + mPosMonth.x + 2 * bmpW + dotW,
									y - (h / 2) + mPosMonth.y + dotH);
						}
						mCaleDot.draw(canvas);
					}
				}
			}
			if (!dArrayDate.isEmpty() && dArrayDate.size() > 0) {
				String sDate = String.format("%02d", mCalendar.monthDay);
				int pos = Integer.parseInt(sDate.substring(0, 1));
				Drawable dBmpDate = dArrayDate.get(pos);
				int bmpW = dBmpDate.getIntrinsicWidth();
				int bmpH = dBmpDate.getIntrinsicHeight();
				dBmpDate.setBounds(x - (w / 2) + mPosDate.x, y - (h / 2)
						+ mPosDate.y, x - (w / 2) + mPosDate.x + bmpW, y
						- (h / 2) + mPosDate.y + bmpH);
				dBmpDate.draw(canvas);
				pos = Integer.parseInt(sDate.substring(1, 2));
				dBmpDate = dArrayDate.get(pos);
				dBmpDate.setBounds(x - (w / 2) + mPosDate.x + bmpW, y - (h / 2)
						+ mPosDate.y, x - (w / 2) + mPosDate.x + 2 * bmpW, y
						- (h / 2) + mPosDate.y + bmpH);
				dBmpDate.draw(canvas);
			}
			if (!dArrayWeek.isEmpty() && dArrayWeek.size() > 0) {
				Drawable dBmpWeek = dArrayWeek.get(mCalendar.weekDay);
				int bmpW = dBmpWeek.getIntrinsicWidth();
				int bmpH = dBmpWeek.getIntrinsicHeight();
				dBmpWeek.setBounds(x - (w / 2) + mPosWeek.x, y - (h / 2)
						+ mPosWeek.y, x - (w / 2) + mPosWeek.x + bmpW, y
						- (h / 2) + mPosWeek.y + bmpH);
				dBmpWeek.draw(canvas);
			}
		} else {
			Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			String day = String.format("%02d", mCalendar.monthDay);

			textPaint.setTextAlign(Align.CENTER);
			textPaint.setTextSize(mDateTextSize);
			textPaint.setColor(Color.BLACK);
			int width = getWidth();
			int height = getHeight();
			FontMetrics fontMetrics = textPaint.getFontMetrics();
			float fontHeight = fontMetrics.bottom - fontMetrics.top;
			float pos_x = width / 2;
			float pos_y = height - (height - fontHeight) / 2
					- fontMetrics.bottom;
			canvas.drawText(day, pos_x, pos_y, textPaint);
		}

		if (scaled) {
			canvas.restore();
		}
	}

	/**
	 * Gets the date text size.
	 *
	 * @return the date text size
	 */
	public float getDateTextSize() {
		return mDateTextSize;
	}

	/**
	 * Sets the date text size.
	 *
	 * @param dateTextSize the new date text size
	 */
	public void setDateTextSize(float dateTextSize) {
		this.mDateTextSize = dateTextSize;
	}
	
	
}
