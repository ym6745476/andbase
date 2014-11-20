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
package com.ab.view.calendar;

import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import com.ab.util.AbGraphicUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：CalendarHeader.java 
 * 描述：日历控件头部绘制类
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-7-9 下午2:07:52
 */
public class CalendarHeader extends View {
	
	/** The tag. */
	private String TAG = "CalendarHeader";
	
	/** The m paint. */
	private final Paint mPaint; 
	
	/** The rect. */
	private RectF rect = new RectF();
	
	//星期几
	/** The week day. */
	private int weekDay = Calendar.SUNDAY;
	
	//星期的数据
	/** The day name. */
	private String[] dayName = new String[10];

	/** The width. */
	private int width = 320;    	
	
	/** The height. */
	private int height = 480;
	
	/** 每个单元格的宽度. */
	private int cellWidth = 40;
	
	/** 文字颜色. */
	private int defaultTextColor = Color.rgb(86, 86, 86);
	
	/** 特别文字颜色. */
	private int specialTextColor = Color.rgb(240, 140, 26);
	
	/** 字体大小. */
	private int defaultTextSize = 25;
	
	/** 字体是否加粗. */
	private boolean defaultTextBold = false;
	
	/** 是否有设置头部背景. */
	private boolean hasBg = false;
	
	/**
	 * 日历头.
	 *
	 * @param context the context
	 */
	public CalendarHeader(Context context) {
		this(context, null);
	}
	
	/**
	 * Instantiates a new calendar header.
	 *
	 * @param context the context
	 * @param attributeset the attributeset
	 */
	public CalendarHeader(Context context, AttributeSet attributeset) {
		super(context);
		dayName[Calendar.SUNDAY] = "周日";
		dayName[Calendar.MONDAY] = "周一";
		dayName[Calendar.TUESDAY] = "周二";
		dayName[Calendar.WEDNESDAY] = "周三";
		dayName[Calendar.THURSDAY] = "周四";
		dayName[Calendar.FRIDAY] = "周五";
		dayName[Calendar.SATURDAY] = "周六";
		mPaint = new Paint(); 
        mPaint.setColor(defaultTextColor);
        mPaint.setAntiAlias(true); 
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(defaultTextSize);
        
        WindowManager wManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);    	
		Display display = wManager.getDefaultDisplay();    	
		width = display.getWidth();    	
		height = display.getHeight();
		cellWidth = (width-20)/7;
	}
	
	/**
	 * 描述：设置背景.
	 *
	 * @param resid the new header background resource
	 */
 	public void setHeaderBackgroundResource(int resid){
 		setBackgroundResource(resid);
 		hasBg = true;
 	}
	
	/**
	 * 描述：文字大小.
	 *
	 * @return the text size
	 */
	public int getTextSize() {
		return defaultTextSize;
	}

	/**
	 * 描述：设置文字大小.
	 *
	 * @param mTextSize the new text size
	 */
	public void setTextSize(int mTextSize) {
		this.defaultTextSize = mTextSize;
		mPaint.setTextSize(defaultTextSize);
		this.invalidate();
	}  

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param canvas the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * @author: amsoft.cn
	 * @date：2013-7-19 下午4:30:45
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		if(!hasBg){
		   canvas.drawColor(Color.WHITE);
		   //设置矩形大小
		   rect.set(0, 0, this.getWidth(),this.getHeight());
		   rect.inset(0.5f,0.5f);
		}
		// 绘制日历头部
		drawDayHeader(canvas);
		
	}

	/**
	 * Draw day header.
	 *
	 * @param canvas the canvas
	 */
	private void drawDayHeader(Canvas canvas) {
		// 写入日历头部，设置画笔参数
		if(!hasBg){
			// 画矩形，并设置矩形画笔的颜色
			mPaint.setColor(Color.rgb(150, 195, 70));
			canvas.drawRect(rect, mPaint);
		}
		
		if(defaultTextBold){
			mPaint.setFakeBoldText(true);
		}
		mPaint.setColor(defaultTextColor);
		
		for (int iDay = 1; iDay < 8; iDay++) {
			if(iDay==1 || iDay==7){
				mPaint.setColor(specialTextColor);
			}
			// draw day name
			final String sDayName = getWeekDayName(iDay);
			
			TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
	        mTextPaint.setTextSize(defaultTextSize);
	        FontMetrics fm  = mTextPaint.getFontMetrics();
	        //得到行高
	        int textHeight = (int)Math.ceil(fm.descent - fm.ascent);
	        int textWidth = (int)AbGraphicUtil.getStringWidth(sDayName,mTextPaint);
			
			final int iPosX = (int) rect.left +cellWidth*(iDay-1)+(cellWidth-textWidth)/2;
			final int iPosY = (int) (this.getHeight()
					- (this.getHeight() - textHeight) / 2 - mPaint
					.getFontMetrics().bottom);
			canvas.drawText(sDayName, iPosX, iPosY, mPaint);
			mPaint.setColor(defaultTextColor);
		}
		
	}
	
	/**
	 * 描述：获取星期的文字描述.
	 *
	 * @param calendarDay the calendar day
	 * @return the week day name
	 */
	public String getWeekDayName(int calendarDay) {
		return dayName[calendarDay];
	}
	
}
