package com.andbase.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.view.ioc.AbIocView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * © 2012 amsoft.cn
 * 名称：UIElementActivity.java 
 * 描述：常用控件汇总.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-11-03 下午3:27:05
 */

public class UIElementActivity extends AbActivity {
	
	private MyApplication application;
	
	@AbIocView(id = R.id.button1,click="btnClick")Button button1;
    @AbIocView(id = R.id.button2,click="btnClick")Button button2;
    @AbIocView(id = R.id.button3,click="btnClick")Button button3;
    @AbIocView(id = R.id.button4,click="btnClick")Button button4;
    @AbIocView(id = R.id.button5,click="btnClick")Button button5;
    @AbIocView(id = R.id.button6,click="btnClick")Button button6;
    @AbIocView(id = R.id.button7,click="btnClick")Button button7;
    @AbIocView(id = R.id.button8,click="btnClick")Button button8;
    @AbIocView(id = R.id.button9,click="btnClick")Button button9;
    @AbIocView(id = R.id.button10,click="btnClick")Button button10;
    @AbIocView(id = R.id.button11,click="btnClick")Button button11;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.ui_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.ui_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        
    } 
	
	public void btnClick(View v){
		Intent intent = null;
		switch (v.getId()) {
		case R.id.button1:
			//滑动按钮
			intent = new Intent(UIElementActivity.this, SlidingButtonActivity.class);
			startActivity(intent);
			break;
		case R.id.button2:
			//图片轮播
			intent = new Intent(UIElementActivity.this, SlidingPlayViewActivity.class);
			startActivity(intent);
			break;
		case R.id.button3:
			//日历选择器
			intent = new Intent(UIElementActivity.this, CalendarActivity.class);
			startActivity(intent);
			break;
		case R.id.button4:
			//POP提示框
			intent = new Intent(UIElementActivity.this, PopoverActivity.class);
			startActivity(intent);
			break;
		case R.id.button5:
			//轮子选择控件
			intent = new Intent(UIElementActivity.this, WheelActivity.class);
			startActivity(intent);
			break;
		case R.id.button6:
			//欢迎页面
			intent = new Intent(UIElementActivity.this, WelcomeActivity.class);
			startActivity(intent);
			break;
		case R.id.button7:
			//拍照和相册选取图片
			intent = new Intent(UIElementActivity.this, AddPhotoActivity.class);
			startActivity(intent);
			break;
		case R.id.button8:
			//城市选择
			intent = new Intent(UIElementActivity.this, CityListActivity.class);
			startActivity(intent);
			break;
		case R.id.button9:
			intent = new Intent(UIElementActivity.this,AnalogClockActivity.class); 
			startActivity(intent);
			break;
		case R.id.button10:
			intent = new Intent(UIElementActivity.this,NumberClockActivity.class); 
			startActivity(intent);
			break;
		case R.id.button11:
			intent = new Intent(UIElementActivity.this,DeskCalendarActivity.class); 
			startActivity(intent);
			break;
		default:
			break;
		}
	}
    
}
