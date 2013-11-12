package com.andbase.demo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.ab.view.slidingmenu.AbSlidingLayout;
import com.ab.view.slidingmenu.AbSlidingLayout.OnOpenListener;
import com.ab.view.slidingmenu.AbSlidingLeftView;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class SlidingLeftActivity extends Activity{
	private LayoutInflater mInflater = null;
	private AbSlidingLayout mAbSlidingLayout;
	private MyApplication application = null;
	private OnOpenListener mOnOpenListener;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
		application = (MyApplication) this.getApplication();
		mInflater = LayoutInflater.from(this);
		LinearLayout.LayoutParams layoutParamsFF = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mAbSlidingLayout = new AbSlidingLayout(this);
		mAbSlidingLayout.setLayoutParams(layoutParamsFF);
		
		AbSlidingLeftView mAbSlidingLeftView = new AbSlidingLeftView(this,R.layout.left);
		View mainView = mInflater.inflate(R.layout.right, null);
		mAbSlidingLayout.addView(mAbSlidingLeftView.getView(), layoutParamsFF);
		mAbSlidingLayout.addView(mainView, layoutParamsFF);
		setContentView(mAbSlidingLayout);
		
		Button rightBtn = (Button)mainView.findViewById(R.id.rightBtn);
		rightBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if (mAbSlidingLayout.getScreenState() == AbSlidingLayout.SCREEN_STATE_CLOSE) {
					mAbSlidingLayout.open();
				}
			}
		});
    }

	
    
}


