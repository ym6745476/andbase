package com.andbase.login;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class AboutActivity extends AbActivity {
	
	private MyApplication application;
    String version = null;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.about);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.about);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        //mAbTitleBar.setVisibility(View.GONE);
        //设置AbTitleBar在最上
        this.setTitleBarOverlay(true);
        application = (MyApplication)abApplication;
        mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        TextView version_val = ((TextView)findViewById(R.id.version_val));
        
        try {
			PackageInfo pinfo = getPackageManager().getPackageInfo("com.andbase", PackageManager.GET_CONFIGURATIONS);
			version = pinfo.versionName;
			version_val.setText("V"+version);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}


