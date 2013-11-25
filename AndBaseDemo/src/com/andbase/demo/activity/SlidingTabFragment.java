package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ab.view.sliding.AbSlidingTabView;
import com.andbase.R;


public class SlidingTabFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.sliding_tab, null);
		AbSlidingTabView mAbSlidingTabView = (AbSlidingTabView)view.findViewById(R.id.mAbSlidingTabView);
		
		//如果里面的页面列表不能下载原因：
		//Fragment里面用的AbTaskQueue,由于有多个tab，顺序下载有延迟，还没下载好就被缓存了。改成用AbTaskPool，就ok了。
		//或者setOffscreenPageLimit(0)
		
		//缓存数量
		mAbSlidingTabView.getViewPager().setOffscreenPageLimit(5);
		
		//禁止滑动
		/*mAbSlidingTabView.getViewPager().setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				return true;
			}
			
		});*/

		Fragment1 page1 = new Fragment1();
		Fragment2 page2 = new Fragment2();
		Fragment4 page3 = new Fragment4();
		Fragment5 page4 = new Fragment5();
		Fragment1 page5 = new Fragment1();
		Fragment2 page6 = new Fragment2();
		Fragment4 page7 = new Fragment4();
		Fragment5 page8 = new Fragment5();
		
		List<Fragment> mFragments = new ArrayList<Fragment>();
		mFragments.add(page1);
		mFragments.add(page2);
		mFragments.add(page3);
		mFragments.add(page4);
		
		List<String> tabTexts = new ArrayList<String>();
		tabTexts.add("推荐");
		tabTexts.add("排行");
		tabTexts.add("游戏中心");
		tabTexts.add("专题栏目");
		//设置样式
		mAbSlidingTabView.setTabTextColor(Color.BLACK);
		mAbSlidingTabView.setTabSelectColor(Color.rgb(30, 168, 131));
		mAbSlidingTabView.setTabBackgroundResource(R.drawable.tab_bg);
		mAbSlidingTabView.setTabLayoutBackgroundResource(R.drawable.slide_top);
		//演示增加一组
		mAbSlidingTabView.addItemViews(tabTexts, mFragments);
		
		//演示增加一个
		mAbSlidingTabView.addItemView("咖啡屋", page5);
		mAbSlidingTabView.addItemView("英雄三国", page6);
		mAbSlidingTabView.addItemView("今日新闻", page7);
		mAbSlidingTabView.addItemView("朋友圈", page8);
		
		mAbSlidingTabView.setTabPadding(20, 8, 20, 8);
		return view;
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

}

