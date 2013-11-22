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
 * 名称：ChartActivity
 * 描述：图表
 * @author zhaoqp
 * @date 2011-12-13
 * @version
 */
public class TableActivity extends AbActivity {
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.table_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.table_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button table1  = (Button)this.findViewById(R.id.table1);
        Button table2  = (Button)this.findViewById(R.id.table2);
        Button table3  = (Button)this.findViewById(R.id.table3);
        
        table1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TableActivity.this,TableDataListActivity.class); 
 				startActivity(intent);
			}
		});
        
        
        table2.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TableActivity.this,TableDataListActivity1.class); 
 				startActivity(intent);
			}
		});
        
        table3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(TableActivity.this,TableDataListActivity2.class); 
 				startActivity(intent);
			}
		});
      
      } 
    
}
