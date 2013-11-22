package com.andbase.demo.activity;

import java.util.Random;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnRefreshListener;
import com.ab.view.pullview.AbPullView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class PullToRefreshViewActivity extends AbActivity {
	
	private MyApplication application;
	private AbPullView mAbPullView = null;
	private AbTaskQueue mAbTaskQueue = null;
	private TextView textView = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.pull_view);
        application = (MyApplication)abApplication;
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.pull_list_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        mAbTaskQueue = AbTaskQueue.getInstance();
	    //获取ListView对象
        mAbPullView = (AbPullView)this.findViewById(R.id.mPullView);
        textView = new TextView(this);
        textView.setText("下拉看看吧");
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(layoutParamsFF);
        textView.setPadding(0, 100, 0, 100);
        mAbPullView.addChildView(textView);
        
        //设置进度条的样式
        mAbPullView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        
    	showProgressDialog();

    	//定义查询的事件
    	final AbTaskItem item = new AbTaskItem();
		item.listener = new AbTaskListener() {

			@Override
			public void update() {
				removeProgressDialog();
				mAbPullView.stopRefresh();
				textView.setText("别人都说我变了"+new Random().nextInt(100));
			}

			@Override
			public void get() {
	   		    try {
	   		    	Thread.sleep(1000);
	   		    	
	   		    } catch (Exception e) {
	   		    }
		  };
		};
		
		mAbPullView.setAbOnRefreshListener(new AbOnRefreshListener(){

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(item);
			}
			
		});
		
    	//第一次下载数据
		mAbTaskQueue.execute(item);
	    
    }

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
   
}


