package com.andbase.demo.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.fragment.AbDialogFragment;
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.fragment.AbRefreshDialogFragment;
import com.ab.http.AbHttpListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
import com.andbase.web.NetworkWeb;

/**
 * 名称：DemoAbActivity 
 * 描述：AbActivity基本用法
 * 
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class DemoAbActivity extends AbActivity {

	private MyApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.ab_activity);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.ab_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		application = (MyApplication) abApplication;

		Button btn1 = (Button) this.findViewById(R.id.btn1);

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DemoAbActivity.this,
						TitleBarActivity.class);
				startActivity(intent);
			}
		});
		
	}

}
