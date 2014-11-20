package com.andbase.demo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.util.AbDialogUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 名称：NestScrollActivity
 * 描述：各种嵌套
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class NestScrollActivity extends AbActivity {
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.nest_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.nest_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button listPager  = (Button)this.findViewById(R.id.listPager);
        Button slidingMenuPager  = (Button)this.findViewById(R.id.slidingMenuPager);
        Button slidingMenuTab  = (Button)this.findViewById(R.id.slidingMenuTab);
        Button scrollPager  = (Button)this.findViewById(R.id.scrollPager);
        
        
        listPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NestScrollActivity.this,ListNestViewPagerActivity.class); 
 				startActivity(intent);
			}
		});
        
        slidingMenuPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NestScrollActivity.this,SlidingMenuNestViewPagerActivity.class); 
 				startActivity(intent);
			}
		});
        
        slidingMenuTab.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(NestScrollActivity.this,SlidingMenuNestTabActivity.class); 
 				startActivity(intent);
			}
		});
        
        scrollPager.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AbDialogUtil.showAlertDialog(NestScrollActivity.this,"提示", "把你的ScrollView换成AbOuterScrollView就可以了", new AbDialogOnClickListener() {

					@Override
					public void onNegativeClick() {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void onPositiveClick() {
						// TODO Auto-generated method stub
						
					}
					
				});
			}
		});
        
      } 
    
}
