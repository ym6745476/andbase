package com.andbase.demo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
/**
 * 
 * © 2012 amsoft.cn
 * 名称：ProgressBarHorizontalActivity.java 
 * 描述：水平进度条
 * @author 还如一梦中
 * @date：2013-9-22 下午4:52:06
 * @version v1.0
 */
public class ProgressBarHorizontalActivity extends AbActivity {

	private String TAG = "MainActivity";
	private static final boolean D = Constant.DEBUG;
	private MyApplication application;

	// ProgressBar进度控制
	private AbHorizontalProgressBar mAbProgressBar;
	// 最大1000
	private int max = 100;	
	private int progress = 0;
	private TextView numberText, maxText;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.progress_bar_horizontal);

		application = (MyApplication) abApplication;
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.progressbar_horizontal_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		// ProgressBar进度控制
		mAbProgressBar = (AbHorizontalProgressBar) findViewById(R.id.horizontalProgressBar);
		
		numberText = (TextView) findViewById(R.id.numberText);
		maxText = (TextView) findViewById(R.id.maxText);
		
		maxText.setText("/"+String.valueOf(max));
		mAbProgressBar.setMax(max);
		mAbProgressBar.setProgress(progress);
		
		mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		startAddProgress();
		
		mAbProgressBar.setAbOnProgressListener(new AbHorizontalProgressBar.AbOnProgressListener() {
			
			@Override
			public void onProgress(int progress) {
				
			}
			
			@Override
			public void onComplete() {
				progress = 0;
				mAbProgressBar.reset();
			}
		});
		
	}


	public void startAddProgress() {
		progress = progress+10;
		numberText.setText(String.valueOf(progress));
		mAbProgressBar.setProgress(progress);
		mUpdateHandler.sendEmptyMessageDelayed(1, 1000);
	}

	private Handler mUpdateHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				startAddProgress();
				break;
			}
			super.handleMessage(msg);
		}
	};
	
	private Runnable mUpdateRunnable = new Runnable() {
		public void run() {
			while (true) {
				Message message = new Message();
				message.what = 1;
				mUpdateHandler.sendMessage(message);
				try {
					// 更新间隔毫秒数
					Thread.sleep(200);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	};
	
	

}
