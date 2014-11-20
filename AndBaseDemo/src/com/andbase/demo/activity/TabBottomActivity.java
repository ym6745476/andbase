package com.andbase.demo.activity;


import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.ab.view.sliding.AbBottomTabView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class TabBottomActivity extends AbActivity {
	
	private AbBottomTabView mBottomTabView;
	private List<Drawable> tabDrawables = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.tab_bottom);
		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.tab_bottom_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		initTitleRightLayout();
		
		mBottomTabView = (AbBottomTabView) findViewById(R.id.mBottomTabView);
		
		//如果里面的页面列表不能下载原因：
		//Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		//或者setOffscreenPageLimit(0)
		
		//缓存数量
		mBottomTabView.getViewPager().setOffscreenPageLimit(5);
		
		//禁止滑动
		/*mBottomTabView.getViewPager().setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
			
		});*/
		
		//mBottomTabView.setOnPageChangeListener(listener);

		FragmentLoad page1 = new FragmentLoad();
		FragmentLoad page2 = new FragmentLoad();
		FragmentRefresh page3 = new FragmentRefresh();
		FragmentLoad page4 = new FragmentLoad();
		
		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page1);
		mFragments.add(page2);
		mFragments.add(page3);
		mFragments.add(page4);
		
		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("我的音乐");
		tabTexts.add("音乐馆");
		tabTexts.add("发现");
		tabTexts.add("更多");
		
		//设置样式
		mBottomTabView.setTabTextColor(Color.BLACK);
		mBottomTabView.setTabSelectColor(Color.rgb(255, 255,255));
		mBottomTabView.setTabBackgroundResource(R.drawable.tab_bg2);
		mBottomTabView.setTabLayoutBackgroundResource(R.drawable.tablayout_bg2);
		
		//注意图片的顺序
		tabDrawables = new ArrayList<Drawable>();
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu1_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu1_f));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu2_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu2_f));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu3_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu3_f));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu4_n));
		tabDrawables.add(this.getResources().getDrawable(R.drawable.menu4_f));
		
		//演示增加一组
		mBottomTabView.addItemViews(tabTexts,mFragments,tabDrawables);
		
		mBottomTabView.setTabPadding(2,10, 2, 2);
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		
	}

	private void initTitleRightLayout() {

	}
}
