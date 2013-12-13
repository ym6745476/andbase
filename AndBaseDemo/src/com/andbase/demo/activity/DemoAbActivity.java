package com.andbase.demo.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

/**
 * 名称：DemoAbActivity 
 * 描述：AbActivity基本用法
 * @author zhaoqp
 * @date 2011-12-13
 * @version
 */
public class DemoAbActivity extends AbActivity {

	private MyApplication application;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.demo_ab);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.ab_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		application = (MyApplication) abApplication;

		Button btn1 = (Button) this.findViewById(R.id.btn1);
		Button btn2 = (Button) this.findViewById(R.id.btn2);
		Button btn3 = (Button) this.findViewById(R.id.btn3);
		Button btn4 = (Button) this.findViewById(R.id.btn4);
		Button btn5 = (Button) this.findViewById(R.id.btn5);
		Button btn6 = (Button) this.findViewById(R.id.btn6);
		Button btn7 = (Button) this.findViewById(R.id.btn7);

		btn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DemoAbActivity.this,
						TitleBarActivity.class);
				startActivity(intent);
			}
		});

		btn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showProgressDialog();
				// removeProgressDialog();
			}
		});

		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showToast("Toast提示框");
			}
		});

		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				View mView = mInflater.inflate(R.layout.demo_text, null);
				showDialog(AbConstant.DIALOGTOP, mView);
			}
		});

		btn5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				View mView = mInflater.inflate(R.layout.demo_text, null);
				showDialog(AbConstant.DIALOGCENTER, mView);
			}
		});

		btn6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				View mView = mInflater.inflate(R.layout.demo_text, null);
				showDialog(AbConstant.DIALOGBOTTOM, mView);
			}
		});

		btn7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog("标题", "描述", new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						showToast("点击了确认");
					}

				});
			}
		});
	}

}
