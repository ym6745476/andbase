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


import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;


// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：CalendarView.java 
 * 描述：日历View
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-8-23 下午2:03:29
 */
public class CalendarView extends LinearLayout {
	
	/** The tag. */
	private String TAG = "CalendarView";
	
	/** The context. */
	private Context context;
	
	/** The layout params fw. */
	private LinearLayout.LayoutParams layoutParamsFW = null;
	
	/** The m linear layout header. */
	private LinearLayout mLinearLayoutHeader = null;
	
	/** The m linear layout content. */
	private LinearLayout mLinearLayoutContent = null;
	
	/** The m calendar header. */
	private CalendarHeader  mCalendarHeader = null;
	
	/** The width. */
	private int width = 320;    	
	
	/** The height. */
	private int height = 480;
	
	/** 星期头的行高. */
	private int headerHeight = 45;
	
	//行高
	/** The row height. */
	private int rowHeight = 40;
	//每个单元格的宽度
	/** The cell width. */
	private int cellWidth = 40;
	
	// 日期变量
	/** The cal start date. */
	public static Calendar calStartDate = Calendar.getInstance();
	
	/** The cal today. */
	private Calendar calToday = Calendar.getInstance();
	
	/** The cal selected. */
	private Calendar calSelected = null;
	//累计日期
	/** The cal calendar. */
	private Calendar calCalendar = Calendar.getInstance();
	
	/** The current month. */
	private int currentMonth = 0;
	
	/** The current year. */
	private int currentYear = 0;
	
	//本日历的第一个单元格的星期
	/** The first day of week. */
	private int firstDayOfWeek = Calendar.SUNDAY;

	
	//当前显示的单元格
	/** The m calendar cells. */
	private ArrayList<CalendarCell> mCalendarCells = new ArrayList<CalendarCell>();
    
	/** The m on item click listener. */
	private AbOnItemClickListener mOnItemClickListener;
	
    /**
     * Instantiates a new ab grid view.
     *
     * @param context the context
     */
    public CalendarView(Context context) {
    	this(context, null);
    }

	/**
	 * Instantiates a new calendar view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		
		WindowManager wManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);    	
		Display display = wManager.getDefaultDisplay();    	
		width = display.getWidth();    	
		height = display.getHeight();
		
		mLinearLayoutHeader = new LinearLayout(context);
		mLinearLayoutHeader.setLayoutParams(new LayoutParams(width,headerHeight));
		mLinearLayoutHeader.setOrientation(LinearLayout.VERTICAL);
		mCalendarHeader = new CalendarHeader(context);
		mCalendarHeader.setLayoutParams(new LayoutParams(width,headerHeight));
		mLinearLayoutHeader.addView(mCalendarHeader,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		addView(mLinearLayoutHeader);
		
		
		mLinearLayoutContent = new LinearLayout(context);
		mLinearLayoutContent.setOrientation(LinearLayout.VERTICAL);
		addView(mLinearLayoutContent);
		
		cellWidth = (width-20)/7;
		rowHeight = cellWidth;
		
		//初始化选中今天
		calSelected = Calendar.getInstance();
		initRow();
		initStartDateForMonth();
		initCalendar();
	}
	
	/**
	 * Inits the row.
	 */
	public void initRow(){
		mLinearLayoutContent.removeAllViews();
		mCalendarCells.clear();
		for (int iRow = 0; iRow < 6; iRow++) {
			LinearLayout mLinearLayoutRow = new LinearLayout(context);
			mLinearLayoutRow.setLayoutParams(new LayoutParams(width,rowHeight));
			mLinearLayoutRow.setOrientation(LinearLayout.HORIZONTAL);
			for (int iDay = 0; iDay < 7; iDay++) {
				CalendarCell dayCell = new CalendarCell(context,(iRow*7)+iDay,cellWidth,rowHeight);
				dayCell.setOnItemClickListener(mOnDayCellClick);
				mLinearLayoutRow.addView(dayCell);
				mCalendarCells.add(dayCell);
			}
			mLinearLayoutContent.addView(mLinearLayoutRow);
		}
	}
	
	
	/**
	 * 描述：由于日历上的日期都是从周日开始的，计算第一个单元格的日期.
	 */
	private void initStartDateForMonth() {
		calStartDate.setTimeInMillis(calSelected.getTimeInMillis());
		//获取当前的
		currentMonth = calStartDate.get(Calendar.MONTH);
		currentYear = calStartDate.get(Calendar.YEAR);
		
		calStartDate.set(Calendar.DAY_OF_MONTH, 1);
		calStartDate.set(Calendar.HOUR_OF_DAY, 0);
		calStartDate.set(Calendar.MINUTE, 0);
		calStartDate.set(Calendar.SECOND, 0);
		
		int iDay = 0;
		int iStartDay = firstDayOfWeek;
		
		if (iStartDay == Calendar.MONDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.MONDAY;
			if (iDay < 0)
				iDay = 6;
		}
		
		if (iStartDay == Calendar.SUNDAY) {
			iDay = calStartDate.get(Calendar.DAY_OF_WEEK) - Calendar.SUNDAY;
			if (iDay < 0)
				iDay = 6;
		}
		
		calStartDate.add(Calendar.DAY_OF_WEEK, -iDay);
	}
	
	/**
	 * 描述：更新日历.
	 */
	private void updateCalendar() {
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		
		boolean isThisMonth  = false;
		//今天在当前月，则去掉默认选中的1号
		if (calToday.get(Calendar.YEAR) == iSelectedYear) {
			if (calToday.get(Calendar.MONTH) == iSelectedMonth) {
				isThisMonth = true;
			}
		}
		
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
		for (int i = 0; i < mCalendarCells.size(); i++) {
			CalendarCell dayCell = mCalendarCells.get(i);
			//
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);
			

			// 判断是否当天
			boolean bToday = false;
			// 是否被选中
			boolean bSelected = false;
			// check holiday
			boolean bHoliday = false;
			// 是否有记录
			boolean hasRecord = false;
			
			if (calToday.get(Calendar.YEAR) == iYear) {
				if (calToday.get(Calendar.MONTH) == iMonth) {
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
						bToday = true;
					}
				}
			}
			
			if ((iDayOfWeek == Calendar.SATURDAY) || (iDayOfWeek == Calendar.SUNDAY)){
				bHoliday = true;
			}
			if ((iMonth == Calendar.JANUARY) && (iDay == 1)){
				bHoliday = true;
			}

			if (bIsSelection){
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth) && (iSelectedYear == iYear)) {
					bSelected = true;
				}else{
					bSelected = false;
				}
			}
			
			if(iDay==1 && isThisMonth){
				bSelected = false;
			}

			dayCell.setThisCellDate(iYear, iMonth, iDay, bToday,bSelected, bHoliday,currentMonth, hasRecord);

			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		invalidate();
	}
	
	/**
	 * 描述：更新日历.
	 */
	private void initCalendar() {
		final boolean bIsSelection = (calSelected.getTimeInMillis() != 0);
		final int iSelectedYear = calSelected.get(Calendar.YEAR);
		final int iSelectedMonth = calSelected.get(Calendar.MONTH);
		final int iSelectedDay = calSelected.get(Calendar.DAY_OF_MONTH);
		
		calCalendar.setTimeInMillis(calStartDate.getTimeInMillis());
		for (int i = 0; i < mCalendarCells.size(); i++) {
			CalendarCell dayCell = mCalendarCells.get(i);
			//
			final int iYear = calCalendar.get(Calendar.YEAR);
			final int iMonth = calCalendar.get(Calendar.MONTH);
			final int iDay = calCalendar.get(Calendar.DAY_OF_MONTH);
			final int iDayOfWeek = calCalendar.get(Calendar.DAY_OF_WEEK);

			// 判断是否当天
			boolean bToday = false;
			// 是否被选中
			boolean bSelected = false;
			// check holiday
			boolean bHoliday = false;
			
			if (calToday.get(Calendar.YEAR) == iYear) {
				if (calToday.get(Calendar.MONTH) == iMonth) {
					if (calToday.get(Calendar.DAY_OF_MONTH) == iDay) {
						bToday = true;
					}
				}
			}
			
			if ((iDayOfWeek == Calendar.SATURDAY) || (iDayOfWeek == Calendar.SUNDAY)){
				bHoliday = true;
			}
			if ((iMonth == Calendar.JANUARY) && (iDay == 1)){
				bHoliday = true;
			}
			
			if (bIsSelection){
				if ((iSelectedDay == iDay) && (iSelectedMonth == iMonth) && (iSelectedYear == iYear)) {
					bSelected = true;
				}else{
					bSelected = false;
				}
			}
			

			dayCell.setThisCellDate(iYear, iMonth, iDay, bToday,bSelected, bHoliday,currentMonth, false);

			calCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		invalidate();
	}
	

	/**
	 * Sets the on item click listener.
	 *
	 * @param mAbOnItemClickListener the new on item click listener
	 */
	public void setOnItemClickListener(
			AbOnItemClickListener mAbOnItemClickListener) {
		this.mOnItemClickListener = mAbOnItemClickListener;
	}

	/**
	 * Sets the header height.
	 *
	 * @param height the new header height
	 */
	public void setHeaderHeight(int height) {
		headerHeight = height;
		mLinearLayoutHeader.removeAllViews();
		mCalendarHeader.setLayoutParams(new LayoutParams(width,headerHeight));
		mLinearLayoutHeader.addView(mCalendarHeader,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		this.invalidate();
		
	}
	
	/**
	 * Sets the header text size.
	 *
	 * @param size the new header text size
	 */
	public void setHeaderTextSize(int size) {
		mCalendarHeader.setTextSize(size);
		this.invalidate();
	}
	
	/**
	 * Rebuild calendar.
	 *
	 * @param calendar the calendar
	 */
	public void rebuildCalendar(Calendar calendar) {
		//初始化选中1号
		calSelected.setTimeInMillis(calendar.getTimeInMillis());
		initRow();
		initStartDateForMonth();
		updateCalendar();
	}
    
    /** 点击日历，触发事件. */
 	private AbOnItemClickListener mOnDayCellClick = new AbOnItemClickListener(){

		@Override
		public void onClick(int position) {
			CalendarCell mCalendarCell = mCalendarCells.get(position);
			if(mCalendarCell.isActiveMonth()){
				calSelected.setTimeInMillis(mCalendarCell.getThisCellDate().getTimeInMillis());
				for(int i=0;i<mCalendarCells.size();i++){
					CalendarCell mCalendarCellOther = mCalendarCells.get(i);
					mCalendarCellOther.setSelected(false);
				}
				mCalendarCell.setSelected(true);
	 			if(mOnItemClickListener!=null){
	 				mOnItemClickListener.onClick(position);
	 			}
			}
		}
 		
 	};
 	
 	/**
	  * 描述：设置标题背景.
	  *
	  * @param resid the new header background resource
	  */
 	public void setHeaderBackgroundResource(int resid){
 		mCalendarHeader.setHeaderBackgroundResource(resid);
 	}
 	
 	/**
	  * 描述：根据索引获取选择的日期.
	  *
	  * @param position the position
	  * @return the str date at position
	  */
 	public String getStrDateAtPosition(int position){
 		CalendarCell mCalendarCell = mCalendarCells.get(position);
 		Calendar calendar = mCalendarCell.getThisCellDate();
 		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
 		return year+"-"+month+"-"+day;
 	}
 	
 	/**
	  * 描述：获取这个日历的总日期数.
	  *
	  * @return the calendar cell size
	  */
 	public int getCalendarCellSize(){
 		return mCalendarCells.size();
 	}

	/**
	 * 描述：获取当前日历的所有条目.
	 *
	 * @return the calendar cells
	 */
	public ArrayList<CalendarCell> getCalendarCells() {
		return mCalendarCells;
	}

	/**
	 * 描述：获取选中的日期，默认为今天.
	 *
	 * @return the cal selected
	 */
	public String getCalSelected() {
		final int iYear = calSelected.get(Calendar.YEAR);
		final int iMonth = calSelected.get(Calendar.MONTH)+1;
		final int iDay = calSelected.get(Calendar.DAY_OF_MONTH);
		return iYear+"-"+iMonth+"-"+iDay;
	}
	
	/**
	 * 条目点击接口.
	 *
	 * @see AbOnItemClickEvent
	 */
	public interface AbOnItemClickListener {
	    
	    /**
	     * 描述：点击事件.
	     * @param position 索引
	     */
	    public void onClick(int position); 
	}

 	
}

