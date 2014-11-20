package com.andbase.demo.activity;


import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.ab.activity.AbActivity;
import com.ab.view.app.AbPopoverView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;

public class PopoverActivity extends AbActivity implements OnClickListener{
   
	RelativeLayout rootView = null;
	AbPopoverView popoverView = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.popover);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.popview_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        rootView = (RelativeLayout)findViewById(R.id.rootLayout);
        popoverView = new AbPopoverView(this);
        popoverView.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.popover_bg));
        popoverView.setArrowLeftDrawable(this.getResources().getDrawable(R.drawable.popover_arrow_left));
        popoverView.setArrowRightDrawable(this.getResources().getDrawable(R.drawable.popover_arrow_right));
        popoverView.setArrowDownDrawable(this.getResources().getDrawable(R.drawable.popover_arrow_down));
        popoverView.setArrowUpDrawable(this.getResources().getDrawable(R.drawable.popover_arrow_up));
        
        popoverView.setContentSizeForViewInPopover(new Point(300, 300));
        popoverView.setPopoverViewListener(new AbPopoverView.PopoverViewListener() {
			
			@Override
			public void popoverViewWillShow(AbPopoverView view) {
			}
			
			@Override
			public void popoverViewWillDismiss(AbPopoverView view) {
			}
			
			@Override
			public void popoverViewDidShow(AbPopoverView view) {
			}
			
			@Override
			public void popoverViewDidDismiss(AbPopoverView view) {
			}
		});
        
        findViewById(R.id.button1).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        findViewById(R.id.button4).setOnClickListener(this);
        findViewById(R.id.button5).setOnClickListener(this);
        findViewById(R.id.button6).setOnClickListener(this);
        findViewById(R.id.button7).setOnClickListener(this);
        findViewById(R.id.button8).setOnClickListener(this);
        findViewById(R.id.button9).setOnClickListener(this);
    }

	@Override
	public void onClick(View v) {
		popoverView.setPopoverContentView(mInflater.inflate(R.layout.popover_showed_view, null));
		popoverView.showPopoverFromRectInViewGroup(rootView, AbPopoverView.getFrameForView(v), AbPopoverView.PopoverArrowDirectionAny, true);
	}

	
}