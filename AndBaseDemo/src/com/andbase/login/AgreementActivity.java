package com.andbase.login;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class AgreementActivity extends AbActivity {
	
    private MyApplication application;
    private AbTitleBar mAbTitleBar = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.agreement);
		application = (MyApplication) abApplication;
	    
	    mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.agreement_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		this.setTitleBarOverlay(true);
		
	}
	
}
