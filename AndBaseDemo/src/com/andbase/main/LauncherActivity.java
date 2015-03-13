package com.andbase.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.andbase.R;


public class LauncherActivity extends Activity {
	
	private RelativeLayout mLaunchLayout;
	private Animation mFadeIn;
	private Animation mFadeInScale;
	private Animation mFadeOut;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.launcher);
		mLaunchLayout = (RelativeLayout) findViewById(R.id.launch);
		init();
		setListener();
	}

	private void setListener() {
		
		/**
		 * 动画切换原理:开始时是用第一个渐现动画,当第一个动画结束时开始第二个放大动画,当第二个动画结束时调用第三个渐隐动画,
		 * 第三个动画结束时修改显示的内容并且重新调用第一个动画,从而达到循环效果
		 */
		mFadeIn.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				mLaunchLayout.startAnimation(mFadeInScale);
			}
		});
		mFadeInScale.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {

			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				mLaunchLayout.startAnimation(mFadeOut);
			}
		});
		
		mFadeOut.setAnimationListener(new AnimationListener() {

			public void onAnimationStart(Animation animation) {
				Intent intent = new Intent();
				intent.setClass(LauncherActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}

			public void onAnimationRepeat(Animation animation) {

			}

			public void onAnimationEnd(Animation animation) {
				
			}
		});
		

	}

	private void init() {
		initAnim();
		mLaunchLayout.startAnimation(mFadeIn);
	}

	private void initAnim() {
		mFadeIn = AnimationUtils.loadAnimation(LauncherActivity.this,R.anim.welcome_fade_in);
		mFadeIn.setDuration(500);
		mFadeInScale = AnimationUtils.loadAnimation(LauncherActivity.this,R.anim.welcome_fade_in_scale);
		mFadeInScale.setDuration(1000);
		mFadeOut = AnimationUtils.loadAnimation(LauncherActivity.this,R.anim.welcome_fade_out);
		mFadeOut.setDuration(500);
	}

}
