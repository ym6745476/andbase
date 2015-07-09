package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class SlidingPlayViewActivity extends AbActivity {
	
	AbSlidingPlayView mSlidingPlayView = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.sliding_play);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.view_play_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        mSlidingPlayView = (AbSlidingPlayView)findViewById(R.id.mAbSlidingPlayView);
        
	    final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
		ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
		TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
		mPlayText.setText("1111111111111");
		mPlayImage.setBackgroundResource(R.drawable.pic1);
		
		final View mPlayView1 = mInflater.inflate(R.layout.play_view_item, null);
		ImageView mPlayImage1 = (ImageView) mPlayView1.findViewById(R.id.mPlayImage);
		TextView mPlayText1 = (TextView) mPlayView1.findViewById(R.id.mPlayText);
		mPlayText1.setText("2222222222222");
		mPlayImage1.setBackgroundResource(R.drawable.pic2);
		
		final View mPlayView2 = mInflater.inflate(R.layout.play_view_item, null);
		ImageView mPlayImage2 = (ImageView) mPlayView2.findViewById(R.id.mPlayImage);
		TextView mPlayText2 = (TextView) mPlayView2.findViewById(R.id.mPlayText);
		mPlayText2.setText("33333333333333333");
		mPlayImage2.setBackgroundResource(R.drawable.pic3);

		mSlidingPlayView.setNavPageResources(R.drawable.play_display,R.drawable.play_hide);
		mSlidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
		mSlidingPlayView.addView(mPlayView);
		mSlidingPlayView.addView(mPlayView1);
        
        Button addButton = (Button)findViewById(R.id.addBtn);
        Button removeButton = (Button)findViewById(R.id.removeBtn);
        Button addAllButton = (Button)findViewById(R.id.addAllBtn);
        Button startButton = (Button)findViewById(R.id.startBtn);
        Button stopButton = (Button)findViewById(R.id.stopBtn);
        
        
        addButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				
				View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
				ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
				TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
				mPlayText.setText("这是第"+mSlidingPlayView.getCount()+"个");
				mPlayImage.setBackgroundResource(R.drawable.pic2);
				mSlidingPlayView.addView(mPlayView);
			}
        	
        });
        
        addAllButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mSlidingPlayView.removeAllViews();
				mSlidingPlayView.addView(mPlayView);
				mSlidingPlayView.addView(mPlayView1);
				mSlidingPlayView.addView(mPlayView2);
			}
        	
        });
        
        removeButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mSlidingPlayView.removeAllViews();
			}
        	
        });
        
        startButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mSlidingPlayView.startPlay();
			}
        	
        });
        
        stopButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				mSlidingPlayView.stopPlay();
			}
        	
        });
        
        mSlidingPlayView.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {
			
			@Override
			public void onClick(int position) {
				AbToastUtil.showToast(SlidingPlayViewActivity.this,"点击"+position);
			}
		});
        
        mSlidingPlayView.setOnPageChangeListener(new AbSlidingPlayView.AbOnChangeListener() {
			
			@Override
			public void onChange(int position) {
				AbToastUtil.showToast(SlidingPlayViewActivity.this,"改变"+position);
			}
		});
        
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}

	@Override
	public void finish() {
		mSlidingPlayView.stopPlay();
		super.finish();
	}

}


