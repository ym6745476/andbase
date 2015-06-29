package com.andbase.main;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbRequestParams;
import com.ab.util.AbAppUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;
import com.andbase.R;
import com.andbase.global.LocationProvider;
import com.andbase.global.MyApplication;
import com.andbase.global.LocationProvider.LocationListener;
import com.baidu.location.BDLocation;


public class LauncherActivity extends AbActivity {
	
	private MyApplication application;
	private LinearLayout launcherView;
	private Animation mFadeIn;
	private Animation mFadeInScale;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.launcher);
		application = (MyApplication) abApplication;
		launcherView = (LinearLayout) this.findViewById(R.id.launcherView);
		AbViewUtil.scaleContentView(launcherView);
		// Font path
		String fontPath1 = "fonts/jianxiqian.ttf";
		String fontPath2 = "fonts/jianchilun.ttf";
		TextView title1 = (TextView) findViewById(R.id.textView1);
		Typeface tf = Typeface.createFromAsset(getAssets(), fontPath2);
		title1.setTypeface(tf);

		TextView title2 = (TextView) findViewById(R.id.textView2);
		Typeface tf2 = Typeface.createFromAsset(getAssets(), fontPath1);
		title2.setTypeface(tf2);
		
		
		TextView version = (TextView) findViewById(R.id.version);
		version.setTypeface(tf2);
		
		PackageInfo packageInfo = AbAppUtil.getPackageInfo(this);
		version.setText("V"+packageInfo.versionName);

		init();
		setListener();
	}

	private void setListener() {

		mFadeIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				launcherView.startAnimation(mFadeInScale);
			}
		});
		
		mFadeInScale.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				Intent intent = new Intent();
				intent.setClass(LauncherActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	private void init() {
		initAnim();
		launcherView.startAnimation(mFadeIn);
	}

	private void initAnim() {
		mFadeIn = AnimationUtils.loadAnimation(LauncherActivity.this,
				R.anim.welcome_fade_in);
		mFadeIn.setDuration(500);
		mFadeIn.setFillAfter(true);
		
		mFadeInScale = AnimationUtils.loadAnimation(LauncherActivity.this,
				R.anim.welcome_fade_in_scale);
		mFadeInScale.setDuration(800);
		mFadeInScale.setFillAfter(true);
	}
	
	

}
