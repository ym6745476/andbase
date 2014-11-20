package com.andbase.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 名称：DBActivity
 * 描述：本地数据库相关
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class DBActivity extends AbActivity {
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.db_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.db_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button insideSample  = (Button)this.findViewById(R.id.insideSample);
        Button sdSample  = (Button)this.findViewById(R.id.sdSample);
        Button one2one  = (Button)this.findViewById(R.id.one2one);
        Button one2many  = (Button)this.findViewById(R.id.one2many);
        Button object  = (Button)this.findViewById(R.id.object);
        
        insideSample.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DBActivity.this,DBInsideSampleActivity.class); 
 				startActivity(intent);
			}
		});
        
        sdSample.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DBActivity.this,DBSDSampleActivity.class); 
 				startActivity(intent);
			}
		});
        
        
        one2one.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DBActivity.this,DBOne2OneActivity.class); 
 				startActivity(intent);
			}
		});
        
        one2many.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DBActivity.this,DBOne2ManyActivity.class); 
 				startActivity(intent);
			}
		});
        
        object.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DBActivity.this,DBObjectActivity.class); 
 				startActivity(intent);
			}
		});
        
       
      } 
    
}
