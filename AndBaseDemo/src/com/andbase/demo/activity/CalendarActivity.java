package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.calendar.CalendarCell;
import com.ab.view.calendar.CalendarView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class CalendarActivity extends AbActivity {
	
    private CalendarView mCalendarView =  null;
	private List<String> monthList = null;
	private int currentMonthIndex = 0;
	private TextView monthText = null;
	private String currentMonth = null;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.calendar);
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.calendar_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        LinearLayout mLinearLayout = (LinearLayout)this.findViewById(R.id.layout01);
		mCalendarView = new  CalendarView(this);
		mLinearLayout.addView(mCalendarView);
		initTitleRightLayout(); 
		
		mCalendarView.setHeaderHeight(45);
		mCalendarView.setHeaderTextSize(20);
		
		mCalendarView.setBackgroundResource(R.drawable.calendar_bg);
		mCalendarView.setHeaderBackgroundResource(R.drawable.week_bg);
		mCalendarView.setOnItemClickListener(new CalendarView.AbOnItemClickListener() {
			
			@Override
			public void onClick(int position) {
				String date = mCalendarView.getStrDateAtPosition(position);
				AbToastUtil.showToast(CalendarActivity.this,"点击了"+position+"值："+date);
			}
		});
		
		Calendar calendar = Calendar.getInstance();
		monthList = new ArrayList<String>();
		//
		int curYear = calendar.get(Calendar.YEAR); // 得到系统年份
		int curMonth = calendar.get(Calendar.MONTH) + 1; // 得到系统月份
		
		int preYear = curYear-1;
		for (int i = 1; i < 12; i++) {
			monthList.add(preYear + "-" + AbStrUtil.strFormat2(String.valueOf(i)));
		}
		
		for (int i = 1; i <= curMonth; i++) {
			monthList.add(curYear + "-" + AbStrUtil.strFormat2(String.valueOf(i)));
		}
		
		currentMonthIndex = monthList.size()-1;
		currentMonth = monthList.get(currentMonthIndex);
		
		monthText = (TextView)findViewById(R.id.monthText);
		monthText.setText(currentMonth);
		
		Button leftBtn = (Button)findViewById(R.id.leftBtn);
		Button rightBtn = (Button)findViewById(R.id.rightBtn);
		
        leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentMonthIndex--;
				if(currentMonthIndex>=0){
					currentMonth =  monthList.get(currentMonthIndex);
					monthText.setText(currentMonth);
					String [] yearmonth = currentMonth.split("-");
					Calendar cal_select = Calendar.getInstance();
					cal_select.set(Calendar.YEAR, Integer.parseInt(yearmonth[0]));
					cal_select.set(Calendar.MONTH, Integer.parseInt(yearmonth[1])-1);
					cal_select.set(Calendar.DAY_OF_MONTH, 1);
					mCalendarView.rebuildCalendar(cal_select);
					ArrayList<CalendarCell> mCalendarCell = mCalendarView.getCalendarCells();
					for(int i=0;i<5;i++){
					  CalendarCell cc = mCalendarCell.get(new Random().nextInt(mCalendarCell.size()));
					  //有数据
					  cc.setHasRecord(true);
					}
					//showProgressDialog();
					//netGet.downloadBeforeClean(item1);
				}else{
					currentMonthIndex++;
				}
			}
		});
		
		rightBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				currentMonthIndex++;
				if(currentMonthIndex<monthList.size()){
					currentMonth =  monthList.get(currentMonthIndex);
					monthText.setText(currentMonth);
					String [] yearmonth = currentMonth.split("-");
					Calendar cal_select = Calendar.getInstance();
					cal_select.set(Calendar.YEAR, Integer.parseInt(yearmonth[0]));
					cal_select.set(Calendar.MONTH, Integer.parseInt(yearmonth[1])-1);
					cal_select.set(Calendar.DAY_OF_MONTH, 1);
					mCalendarView.rebuildCalendar(cal_select);
					ArrayList<CalendarCell> mCalendarCell = mCalendarView.getCalendarCells();
					for(int i=0;i<5;i++){
						  CalendarCell cc = mCalendarCell.get(new Random().nextInt(mCalendarCell.size()));
						  //有数据
						  cc.setHasRecord(true);
					}
					//mActivity.showProgressDialog();
					//netGet.downloadBeforeClean(item1);
				}else{
					currentMonthIndex--;
				}
			}
		});
		
		ArrayList<CalendarCell> mCalendarCell = mCalendarView.getCalendarCells();
		for(int i=0;i<5;i++){
			CalendarCell cc = mCalendarCell.get(new Random().nextInt(mCalendarCell.size()));
			//有数据
			cc.setHasRecord(true);
		}
        
    }
    

	private void initTitleRightLayout(){
		mAbTitleBar.clearRightView();
    }

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();

	}
   

}


