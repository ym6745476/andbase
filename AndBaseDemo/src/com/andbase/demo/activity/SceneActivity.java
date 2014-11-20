package com.andbase.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 名称：SceneActivity
 * 描述：场景化UI
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class SceneActivity extends AbActivity {
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.scene_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.scene_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button analogClock  = (Button)this.findViewById(R.id.analogClock);
        Button numberClock  = (Button)this.findViewById(R.id.numberClock);
        Button calendar  = (Button)this.findViewById(R.id.calendar);
        
        analogClock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SceneActivity.this,AnalogClockActivity.class); 
 				startActivity(intent);
			}
		});
        
        
        numberClock.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SceneActivity.this,NumberClockActivity.class); 
 				startActivity(intent);
			}
		});
        
        calendar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SceneActivity.this,DeskCalendarActivity.class); 
 				startActivity(intent);
			}
		});
      } 
    
}
