package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.view.app.AbCalendar;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class DeskCalendarActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.desk_calendar);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.desk_calendar_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        Drawable caleBg = this.getResources().getDrawable(R.drawable.desk_calendar);
        Drawable caleDot = null;
		Point posYear = new Point(60,80);
		List<Drawable> dYearArray = new ArrayList<Drawable>();
		dYearArray.add(this.getResources().getDrawable(R.drawable.year0));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year1));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year2));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year3));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year4));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year5));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year6));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year7));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year8));
		dYearArray.add(this.getResources().getDrawable(R.drawable.year9));
		
		
		Point posMonth = new Point(300,80);
		List<Drawable> dMonthArray = new ArrayList<Drawable>();
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month1));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month2));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month3));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month4));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month5));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month6));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month7));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month8));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month9));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month10));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month11));
		dMonthArray.add(this.getResources().getDrawable(R.drawable.month12));
		
		Point posDate = new Point(90,180);
		List<Drawable> dDateArray = new ArrayList<Drawable>();
		dDateArray.add(this.getResources().getDrawable(R.drawable.date0));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date1));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date2));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date3));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date4));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date5));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date6));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date7));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date8));
		dDateArray.add(this.getResources().getDrawable(R.drawable.date9));
		
		Point posWeek = new Point(322,235);
		List<Drawable> dWeekArray = new ArrayList<Drawable>();
        
		AbCalendar view = new AbCalendar(this, caleBg, caleDot, posYear, dYearArray,
				posMonth, dMonthArray, posDate, dDateArray, posWeek,
				dWeekArray);
        
		LinearLayout contentLayout = (LinearLayout)this.findViewById(R.id.contentLayout);
		contentLayout.addView(view);
    }
    
    
    private void initTitleRightLayout(){
    	mAbTitleBar.clearRightView();
    }

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}
	
	public void onPause() {
		super.onPause();
	}
   
}


