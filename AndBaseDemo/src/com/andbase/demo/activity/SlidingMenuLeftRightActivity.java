package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ab.view.slidingmenu.SlidingFragmentActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class SlidingMenuLeftRightActivity extends SlidingFragmentActivity {

	private SlidingMenu menu;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//使用系统标题栏位置，为了放入自定义的标题栏
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);  
		setContentView(R.layout.sliding_menu_content);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title_bar);  
		
		//自定义的标题栏
		AbTitleBar mAbTitleBar = new AbTitleBar(this);
		mAbTitleBar.setTitleText(R.string.sliding_menu_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_menu);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		
		//加到系统标题栏位置上
		LinearLayout titleBarLinearLayout = (LinearLayout)this.findViewById(R.id.titleBar);
		LinearLayout.LayoutParams layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		titleBarLinearLayout.addView(mAbTitleBar,layoutParamsFF);
		
		
	    //必须在setContentView()之前设置
	    setBehindContentView(R.layout.sliding_menu_menu);
		
	    //SlidingMenu的配置
 		menu = getSlidingMenu();
 		menu.setMode(SlidingMenu.LEFT_RIGHT);
 		menu.setShadowWidthRes(R.dimen.shadow_width);
 		menu.setShadowDrawable(R.drawable.shadow);
 		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
 		menu.setFadeDegree(0.35f);
 		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
 		//可配置的参数
 		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
 		//menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
 		
 		//menu.setBehindScrollScale(0.2f);
 		
 		//menu.setBehindWidth((int) (0.75 * menu.getWidth()));
 		//menu.requestLayout();
 		
 		//menu.setFadeEnabled(true);
 		
 		
 		//主视图的Fragment添加
	    setContentView(R.layout.sliding_menu_content);
        getFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new FragmentLoad())
		.commit();
 		
 		//menu1视图的Fragment添加
		menu.setMenu(R.layout.sliding_menu_menu);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new FragmentLoad())
		.commit();
 		
 		//menu2视图的Fragment添加
		menu.setSecondaryMenu(R.layout.sliding_menu_menu2);
		menu.setSecondaryShadowDrawable(R.drawable.shadow_right);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame_two, new FragmentLoad())
		.commit();
        
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

}
