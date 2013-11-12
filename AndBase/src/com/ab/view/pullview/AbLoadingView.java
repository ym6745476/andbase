/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.view.pullview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

// TODO: Auto-generated Javadoc
/**
 * The Class AbLoadingView.
 */
public class AbLoadingView extends LinearLayout {
	
	/** The m context. */
	private Context mContext;
	
	/** The loading view. */
	private LinearLayout loadingView;
	
	/** The loading progress bar. */
	private ProgressBar loadingProgressBar;
	
	/**
	 * Instantiates a new ab list view footer.
	 *
	 * @param context the context
	 */
	public AbLoadingView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * Instantiates a new ab list view footer.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbLoadingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * Inits the view.
	 *
	 * @param context the context
	 */
	private void initView(Context context) {
		mContext = context;
		
		loadingView  = new LinearLayout(context);  
		//设置布局 水平方向  
		loadingView.setOrientation(LinearLayout.HORIZONTAL);
		loadingView.setGravity(Gravity.CENTER); 
		
		loadingProgressBar = new ProgressBar(context,null,android.R.attr.progressBarStyle);
		
		LinearLayout.LayoutParams layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW.gravity = Gravity.CENTER;
		loadingView.addView(loadingProgressBar,layoutParamsWW);
		
		this.addView(loadingView,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));

	}
	

	/**
	 * 
	 * 描述：获取ProgressBar，用于设置自定义样式
	 * @return
	 * @throws 
	 */
	public ProgressBar getFooterProgressBar() {
		return loadingProgressBar;
	}

	public void setLoadingProgressBar(ProgressBar loadingProgressBar) {
		this.loadingProgressBar = loadingProgressBar;
	}

}
