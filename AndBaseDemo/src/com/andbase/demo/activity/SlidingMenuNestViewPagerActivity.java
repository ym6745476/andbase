package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ab.activity.AbActivity;
import com.ab.view.slidingmenu.SlidingMenu;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
/**
 * 
 * © 2012 amsoft.cn
 * 名称：SlidingMenuNestViewPagerActivity.java 
 * 描述：SlidingMenu+ViewPager冲突解决的演示
 * @author 还如一梦中
 * @date：2013-10-25 上午9:15:58
 * @version v1.0
 */
public class SlidingMenuNestViewPagerActivity extends AbActivity {

	private SlidingMenu menu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.sliding_menu_content);
		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.sliding_menu_pager_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.getLogoView().setBackgroundResource(R.drawable.button_selector_menu);
		
        //主视图的Fragment添加
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, new FragmentLoad())
		.commit();

		//SlidingMenu的配置
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
		
		//slidingmenu的事件模式，如果里面有可以滑动的请用TOUCHMODE_MARGIN
		//可解决事件冲突问题
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
		
		//menu视图的Fragment添加
		menu.setMenu(R.layout.sliding_menu_menu);
		getFragmentManager()
		.beginTransaction()
		.replace(R.id.menu_frame, new FragmentLoad())
		.commit();
		
		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (menu.isMenuShowing()) {
 					menu.showContent();
 				} else {
 					menu.showMenu();
 				}
			}
		});
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
