package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class SlidingMenuRightActivity extends AbActivity {

	private SlidingMenu menu;
	private AbTitleBar mAbTitleBar = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.sliding_menu_content);
		
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.sliding_menu_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
        
        //主视图的Fragment添加
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new FragmentLoad())
		.commit();

		//SlidingMenu的配置
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.RIGHT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow_right);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		//menu视图的Fragment添加
		menu.setMenu(R.layout.sliding_menu_menu);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new FragmentLoad())
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
	
	private void initTitleRightLayout(){
		mAbTitleBar.clearRightView();
    	View rightViewMenu = mInflater.inflate(R.layout.menu_btn, null);
    	mAbTitleBar.addRightView(rightViewMenu);
    	Button menuBtn = (Button)rightViewMenu.findViewById(R.id.menuBtn);
    	
    	menuBtn.setOnClickListener(new View.OnClickListener(){

 			@Override
 			public void onClick(View v) {
 				if (menu.isMenuShowing()) {
 					menu.showContent();
 				} else {
 					menu.showMenu();
 				}
 			}
         });
    	
    }

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}

}
