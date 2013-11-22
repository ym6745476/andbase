package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.view.app.AbNumberClock;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class NumberClockActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.number_clock);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.number_clock_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        Drawable timeBg = this.getResources().getDrawable(R.drawable.timer_bg);
        Drawable timeColon = this.getResources().getDrawable(R.drawable.timer_colon);
        
		List<Drawable> dTimeArray = new ArrayList<Drawable>();
		List<Drawable> dApmArray = new ArrayList<Drawable>();
		
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time0));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time1));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time2));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time3));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time4));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time5));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time6));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time7));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time8));
		dTimeArray.add(this.getResources().getDrawable(R.drawable.time9));
		
		//AM 和PM的图标
		//dApmArray.add(this.getResources().getDrawable(R.drawable.time0));
		//dApmArray.add(this.getResources().getDrawable(R.drawable.time1));
		
		
		AbNumberClock view = new AbNumberClock(this, timeBg, timeColon, dTimeArray,dApmArray);
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


