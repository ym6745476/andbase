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
package com.ab.view.wheel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbDateUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.wheel.AbWheelView.AbOnWheelChangedListener;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbWheelUtil.java 
 * 描述：轮子工具类
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */

public class AbWheelUtil {
	
	/**
	 * 描述：默认的年月日的日期选择器.
	 *
	 * @param activity     AbActivity对象
	 * @param mText the m text
	 * @param mWheelViewY  选择年的轮子
	 * @param mWheelViewM  选择月的轮子
	 * @param mWheelViewD  选择日的轮子
	 * @param okBtn 确定按钮
	 * @param cancelBtn 取消按钮
	 * @param defaultYear  默认显示的年
	 * @param defaultMonth the default month
	 * @param defaultDay the default day
	 * @param startYear    开始的年
	 * @param endYearOffset 结束的年与开始的年的偏移
	 * @param initStart  轮子是否初始化默认时间为当前时间
	 */
	public static void initWheelDatePicker(final AbActivity activity,final TextView mText,final AbWheelView mWheelViewY,final AbWheelView mWheelViewM,final AbWheelView mWheelViewD,
			 Button okBtn,Button cancelBtn,
			 int defaultYear,int defaultMonth,int defaultDay,final int startYear,int endYearOffset,boolean initStart){
		
		int endYear = startYear+endYearOffset;
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		//时间选择可以这样实现
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		
		if(initStart){
			 defaultYear = year;
			 defaultMonth = month;
			 defaultDay = day;
		}

		mText.setText(AbStrUtil.dateTimeFormat(defaultYear+"-"+defaultMonth+"-"+defaultDay));
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		
		//设置"年"的显示数据
		mWheelViewY.setAdapter(new AbNumericWheelAdapter(startYear, endYear));
		mWheelViewY.setCyclic(true);// 可循环滚动
		mWheelViewY.setLabel("年");  // 添加文字
		mWheelViewY.setCurrentItem(defaultYear - startYear);// 初始化时显示的数据
		mWheelViewY.setValueTextSize(35);
		mWheelViewY.setLabelTextSize(35);
		mWheelViewY.setLabelTextColor(0x80000000);
		//mWheelViewY.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 月
		mWheelViewM.setAdapter(new AbNumericWheelAdapter(1, 12));
		mWheelViewM.setCyclic(true);
		mWheelViewM.setLabel("月");
		mWheelViewM.setCurrentItem(defaultMonth-1);
		mWheelViewM.setValueTextSize(35);
		mWheelViewM.setLabelTextSize(35);
		mWheelViewM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 日
		// 判断大小月及是否闰年,用来确定"日"的数据
		if (list_big.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
		} else if (list_little.contains(String.valueOf(month + 1))) {
			mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
		} else {
			// 闰年
			if (AbDateUtil.isLeapYear(year)){
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
			}else{
				mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
			}
		}
		mWheelViewD.setCyclic(true);
		mWheelViewD.setLabel("日");
		mWheelViewD.setCurrentItem(defaultDay - 1);
		mWheelViewD.setValueTextSize(35);
		mWheelViewD.setLabelTextSize(35);
		mWheelViewD.setLabelTextColor(0x80000000);
		//mWheelViewD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 添加"年"监听
		AbOnWheelChangedListener wheelListener_year = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int year_num = newValue + startYear;
				int mDIndex = mWheelViewM.getCurrentItem();
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(mWheelViewM.getCurrentItem() + 1))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					if (AbDateUtil.isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewM.setCurrentItem(mDIndex);
				
			}
		};
		// 添加"月"监听
		AbOnWheelChangedListener wheelListener_month = new AbOnWheelChangedListener() {

			public void onChanged(AbWheelView wheel, int oldValue, int newValue) {
				int month_num = newValue + 1;
				// 判断大小月及是否闰年,用来确定"日"的数据
				if (list_big.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 31));
				} else if (list_little.contains(String.valueOf(month_num))) {
					mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 30));
				} else {
					int year_num = mWheelViewY.getCurrentItem() + startYear;
					if (AbDateUtil.isLeapYear(year_num))
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 29));
					else
						mWheelViewD.setAdapter(new AbNumericWheelAdapter(1, 28));
				}
				mWheelViewD.setCurrentItem(0);
			}
		};
		mWheelViewY.addChangingListener(wheelListener_year);
		mWheelViewM.addChangingListener(wheelListener_month);
		
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(v.getContext());
				int indexYear = mWheelViewY.getCurrentItem();
				String year = mWheelViewY.getAdapter().getItem(indexYear);
				
				int indexMonth = mWheelViewM.getCurrentItem();
				String month = mWheelViewM.getAdapter().getItem(indexMonth);
				
				int indexDay = mWheelViewD.getCurrentItem();
				String day = mWheelViewD.getAdapter().getItem(indexDay);
				
				mText.setText(AbStrUtil.dateTimeFormat(year+"-"+month+"-"+day));
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(v.getContext());
			}
			
		});
		
    }
	
	/**
	 * 描述：默认的月日时分的时间选择器.
	 *
	 * @version v1.0
	 * @param activity     AbActivity对象
	 * @param mText the m text
	 * @param mWheelViewMD  选择月日的轮子
	 * @param mWheelViewHH the m wheel view hh
	 * @param mWheelViewMM  选择分的轮子
	 * @param okBtn 确定按钮
	 * @param cancelBtn 取消按钮
	 * @param defaultYear the default year
	 * @param defaultMonth the default month
	 * @param defaultDay the default day
	 * @param defaultHour the default hour
	 * @param defaultMinute the default minute
	 * @param initStart the init start
	 * @date：2013-7-16 上午10:19:01
	 */
	public static void initWheelTimePicker(final AbActivity activity,final TextView mText,final AbWheelView mWheelViewMD,final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 Button okBtn,Button cancelBtn,
			 int defaultYear,int defaultMonth,int defaultDay,int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DATE);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		
		if(initStart){
			defaultYear = year;
			defaultMonth = month;
			defaultDay = day;
			defaultHour = hour;
			defaultMinute = minute;
		}
		
		String val = AbStrUtil.dateTimeFormat(defaultYear+"-"+defaultMonth+"-"+defaultDay+" "+defaultHour+":"+defaultMinute+":"+second) ;
		mText.setText(val);
		// 添加大小月月份并将其转换为list,方便之后的判断
		String[] months_big = { "1", "3", "5", "7", "8", "10", "12" };
		String[] months_little = { "4", "6", "9", "11" };
		final List<String> list_big = Arrays.asList(months_big);
		final List<String> list_little = Arrays.asList(months_little);
		//
		final List<String> textDMList = new ArrayList<String>();
		final List<String> textDMDateList = new ArrayList<String>();
		for(int i=1;i<13;i++){
			if(list_big.contains(String.valueOf(i))){
				for(int j=1;j<32;j++){
					textDMList.add(i+"月"+" "+j+"日");
					textDMDateList.add(defaultYear+"-"+i+"-"+j);
				}
			}else{
				if(i==2){
					if(AbDateUtil.isLeapYear(defaultYear)){
						for(int j=1;j<28;j++){
							textDMList.add(i+"月"+" "+j+"日");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}else{
						for(int j=1;j<29;j++){
							textDMList.add(i+"月"+" "+j+"日");
							textDMDateList.add(defaultYear+"-"+i+"-"+j);
						}
					}
				}else{
					for(int j=1;j<29;j++){
						textDMList.add(i+"月"+" "+j+"日");
						textDMDateList.add(defaultYear+"-"+i+"-"+j);
					}
				}
			}
			
		}
		String currentDay = defaultMonth+"月"+" "+defaultDay+"日";
		int currentDayIndex = textDMList.indexOf(currentDay);
		
		// 月日
		mWheelViewMD.setAdapter(new AbStringWheelAdapter(textDMList,AbStrUtil.strLength("12月"+" "+"12日")));
		mWheelViewMD.setCyclic(true);
		mWheelViewMD.setLabel(""); 
		mWheelViewMD.setCurrentItem(currentDayIndex);
		mWheelViewMD.setValueTextSize(35);
		mWheelViewMD.setLabelTextSize(35);
		mWheelViewMD.setLabelTextColor(0x80000000);
		//mWheelViewMD.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("点");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(35);
		mWheelViewHH.setLabelTextSize(35);
		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(35);
		mWheelViewMM.setLabelTextSize(35);
		mWheelViewMM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(v.getContext());
				int index1 = mWheelViewMD.getCurrentItem();
				int index2 = mWheelViewHH.getCurrentItem();
				int index3 = mWheelViewMM.getCurrentItem();
				
				String dmStr =  textDMDateList.get(index1);
				Calendar calendar = Calendar.getInstance();
				int second = calendar.get(Calendar.SECOND);
				String val = AbStrUtil.dateTimeFormat(dmStr+" "+index2+":"+index3+":"+second) ;
				mText.setText(val);
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(v.getContext());
			}
			
		});
		
    }
	
	
	/**
	 * 描述：默认的时分的时间选择器.
	 *
	 * @param activity     AbActivity对象
	 * @param mText the m text
	 * @param mWheelViewHH the m wheel view hh
	 * @param mWheelViewMM  选择分的轮子
	 * @param okBtn 确定按钮
	 * @param cancelBtn 取消按钮
	 * @param defaultHour the default hour
	 * @param defaultMinute the default minute
	 * @param initStart the init start
	 */
	public static void initWheelTimePicker2(final AbActivity activity,final TextView mText,final AbWheelView mWheelViewHH,final AbWheelView mWheelViewMM,
			 Button okBtn,Button cancelBtn,
			 int defaultHour,int defaultMinute,boolean initStart){
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		
		if(initStart){
			defaultHour = hour;
			defaultMinute = minute;
		}
		
		String val = AbStrUtil.dateTimeFormat(defaultHour+":"+defaultMinute) ;
		mText.setText(val);
		
		// 时
		mWheelViewHH.setAdapter(new AbNumericWheelAdapter(0, 23));
		mWheelViewHH.setCyclic(true);
		mWheelViewHH.setLabel("点");
		mWheelViewHH.setCurrentItem(defaultHour);
		mWheelViewHH.setValueTextSize(35);
		mWheelViewHH.setLabelTextSize(35);
		mWheelViewHH.setLabelTextColor(0x80000000);
		//mWheelViewH.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		// 分
		mWheelViewMM.setAdapter(new AbNumericWheelAdapter(0, 59));
		mWheelViewMM.setCyclic(true);
		mWheelViewMM.setLabel("分");
		mWheelViewMM.setCurrentItem(defaultMinute);
		mWheelViewMM.setValueTextSize(35);
		mWheelViewMM.setLabelTextSize(35);
		mWheelViewMM.setLabelTextColor(0x80000000);
		//mWheelViewM.setCenterSelectDrawable(this.getResources().getDrawable(R.drawable.wheel_select));
		
		okBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(v.getContext());
				int index2 = mWheelViewHH.getCurrentItem();
				int index3 = mWheelViewMM.getCurrentItem();
				String val = AbStrUtil.dateTimeFormat(index2+":"+index3) ;
				mText.setText(val);
			}
			
		});
		
		cancelBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(v.getContext());
			}
			
		});
		
    }
	
	
}
