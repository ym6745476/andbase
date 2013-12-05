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
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;


// TODO: Auto-generated Javadoc
/**
 * The Class AbGridView.
 */
public class AbBaseGridView extends LinearLayout {
	
	/** The context. */
	private Context context;
	
	/** The m grid view. */
	private GridView mGridView = null;
	
	/** The layout params fw. */
	private LinearLayout.LayoutParams layoutParamsFW = null;
	
	/** The m linear layout header. */
	private LinearLayout mLinearLayoutHeader = null;
	
	/** The m linear layout footer. */
	private LinearLayout mLinearLayoutFooter = null;
    
    /**
     * Instantiates a new ab grid view.
     *
     * @param context the context
     */
    public AbBaseGridView(Context context) {
        super(context);
    }
    

	/**
	 * Instantiates a new ab grid view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbBaseGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		mLinearLayoutHeader = new LinearLayout(context);
		mLinearLayoutFooter = new LinearLayout(context);
		mGridView = new GridView(context);
		addView(mLinearLayoutHeader,layoutParamsFW);
		LinearLayout.LayoutParams layoutParamsFW1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		layoutParamsFW1.weight = 1;
		mGridView.setLayoutParams(layoutParamsFW1);
		addView(mGridView);
		
		addView(mLinearLayoutFooter,layoutParamsFW);
	}
  
    /**
     * Adds the header view.
     *
     * @param v the v
     */
    public void addHeaderView(View v) {
    	mLinearLayoutHeader.addView(v,layoutParamsFW);
    }
    
    /**
     * Adds the footer view.
     *
     * @param v the v
     */
    public void addFooterView(View v) {
    	LinearLayout.LayoutParams layoutParamsFW1 = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
    	layoutParamsFW1.topMargin = 2;
    	mLinearLayoutFooter.addView(v,layoutParamsFW1);
    }

	/**
	 * Gets the grid view.
	 *
	 * @return the grid view
	 */
	public GridView getGridView() {
		return mGridView;
	}


	/**
	 * Sets the grid view.
	 *
	 * @param mGridView the new grid view
	 */
	public void setGridView(GridView mGridView) {
		this.mGridView = mGridView;
	}

}

