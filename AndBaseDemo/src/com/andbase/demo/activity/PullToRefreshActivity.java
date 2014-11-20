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
 * 名称：PullToRefreshActivity 描述：下拉刷新分页
 * 
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class PullToRefreshActivity extends AbActivity {

	private MyApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.pull_to_refresh_main);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.pull_list_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		application = (MyApplication) abApplication;
		Button mListView = (Button) this.findViewById(R.id.mListView);
		Button mSampleView = (Button) this.findViewById(R.id.mSampleView);
		Button mGridView = (Button) this.findViewById(R.id.mGridView);
		Button mMultiListView = (Button) this.findViewById(R.id.mMultiListView);

		mSampleView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshViewActivity.class);
				startActivity(intent);
			}
		});

		mListView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshListActivity.class);
				startActivity(intent);
			}
		});

		mGridView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshGridActivity.class);
				startActivity(intent);
			}
		});

		mMultiListView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(PullToRefreshActivity.this,
						PullToRefreshMultiColumnListActivity.class);
				startActivity(intent);
			}
		});

	}

}
