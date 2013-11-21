package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ab.activity.AbActivity;
import com.ab.global.AbMenuItem;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ListPopAdapter;
import com.andbase.global.MyApplication;

public class TitleBarActivity extends AbActivity {
	
	private MyApplication application;
	
	//标题栏
	private AbTitleBar mAbTitleBar = null;
	
	private PopupWindow popupWindow;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.titlebar_main);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText("多功能标题栏");
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        mAbTitleBar.setLogoOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showToast("点了返回哦");
				finish();
			}
		});
        
        Button btn1 = (Button) this.findViewById(R.id.btn1);
		Button btn2 = (Button) this.findViewById(R.id.btn2);
		Button btn3 = (Button) this.findViewById(R.id.btn3);
		Button btn4 = (Button) this.findViewById(R.id.btn4);
		Button btn5 = (Button) this.findViewById(R.id.btn5);
		Button btn6 = (Button) this.findViewById(R.id.btn6);
		Button btn7 = (Button) this.findViewById(R.id.btn7);
		Button btn8 = (Button) this.findViewById(R.id.btn8);
		Button btn9 = (Button) this.findViewById(R.id.btn9);
		
		//显示标题栏
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mAbTitleBar.setVisibility(View.VISIBLE);
			}
		});
		
		//隐藏标题栏
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mAbTitleBar.setVisibility(View.GONE);
			}
		});
		
		//显示右边布局
		btn3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mAbTitleBar.clearRightView();
		    	View rightViewMore = mInflater.inflate(R.layout.more_btn, null);
		    	View rightViewApp = mInflater.inflate(R.layout.app_btn, null);
		    	mAbTitleBar.addRightView(rightViewApp);
		    	mAbTitleBar.addRightView(rightViewMore);
		    	Button about = (Button)rightViewMore.findViewById(R.id.moreBtn);
		    	Button appBtn = (Button)rightViewApp.findViewById(R.id.appBtn);
		    	
		    	appBtn.setOnClickListener(new View.OnClickListener(){

		 			@Override
		 			public void onClick(View v) {
		 				showToast("别点了");
		 			}
		         });
		    	
		    	about.setOnClickListener(new View.OnClickListener(){

		 			@Override
		 			public void onClick(View v) {
		 				showToast("还点");
		 			}
		         	
		         });
			}
		});
		
		//删除右边布局
		btn4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mAbTitleBar.clearRightView();
			}
		});
		
		btn5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				//关于界面
				Intent intent = new Intent(TitleBarActivity.this,AboutActivity.class); 
				startActivity(intent);
			}
		});
		
		
		btn6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				 mAbTitleBar.setTitleTextBackgroundResource(R.drawable.drop_down_title_btn);
				 View view = mInflater.inflate(R.layout.list_pop, null);
				 ListView popListView = (ListView) view.findViewById(R.id.pop_list);
				 List<AbMenuItem> list = new ArrayList<AbMenuItem>();
				 list.add(new AbMenuItem("蔡文姬"));
				 list.add(new AbMenuItem("貂蝉"));
				 list.add(new AbMenuItem("紫罂粟"));
				 list.add(new AbMenuItem("孙尚香"));
				 ListPopAdapter mListPopAdapter = new ListPopAdapter(TitleBarActivity.this, list);
				 popListView.setAdapter(mListPopAdapter);
				 
				 mAbTitleBar.setTitleTextDropDown(view);
			}
		});
		
		btn7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				 mAbTitleBar.setTitleTextBackgroundDrawable(null);
				 mAbTitleBar.setTitleTextOnClickListener(null);
			}
		});
		
		btn8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bottomLayout.setVisibility(View.VISIBLE);
				
				setBottomView(R.layout.bottom_bar);
			}
		});
		
		btn9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				bottomLayout.setVisibility(View.GONE);
			}
		});
    }   
    
}


