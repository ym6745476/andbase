/*
 * Copyright (C) 2012 www.amsoft.cn
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
package com.ab.view.sliding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.util.AbViewUtil;
// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbTabItemView.java 
 * 描述：表示一个TAB
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbTabItemView extends LinearLayout {
	
	/** The m context. */
	private Context mContext;
	
    /** 当前的索引. */
	private int mIndex;
	
    /** 包含的TextView. */
    private TextView mTextView;
    
    /** 图片. */
    private Drawable mLeftDrawable,mTopDrawable,mRightDrawable,mBottomDrawable;
	
    /** Bounds. */
    private int leftBounds,topBounds,rightBounds,bottomBounds;
    /**
     * Instantiates a new ab tab item view.
     *
     * @param context the context
     */
    public AbTabItemView(Context context) {
		this(context,null);
	}

	/**
	 * Instantiates a new ab tab item view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.setGravity(Gravity.CENTER);
        this.setPadding(10, 10, 10, 10);
        this.mContext = context;
        mTextView = new TextView(context);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
        mTextView.setFocusable(true);
        mTextView.setPadding(0 , 0, 0, 0);
        mTextView.setCompoundDrawablePadding(10);
        mTextView.setSingleLine();
        this.addView(mTextView);
    }

    /**
     * Inits the.
     *
     * @param index the index
     * @param text the text
     */
    public void init(int index,String text) {
        mIndex = index;
        mTextView.setText(text);
    }


    /**
     * Gets the index.
     *
     * @return the index
     */
    public int getIndex() {
        return mIndex;
    }


	/**
	 * Gets the text view.
	 *
	 * @return the text view
	 */
	public TextView getTextView() {
		return mTextView;
	}

    /**
     * 描述：设置文字大小.
     *
     * @param tabTextSize the new tab text size
     */
	public void setTabTextSize(int tabTextSize) {
		AbViewUtil.setTextSize(mTextView, tabTextSize);
	}

	/**
	 * 描述：设置文字颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		mTextView.setTextColor(tabColor);
	}
	
	/**
	 * 描述：设置文字的图片.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		mLeftDrawable = left;
		mTopDrawable = top;
		mRightDrawable = right;
		mBottomDrawable = bottom;
		
		if(mLeftDrawable!=null){
			mLeftDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		if(mTopDrawable!=null){
			mTopDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		if(mRightDrawable!=null){
			mRightDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		if(mBottomDrawable!=null){
			mBottomDrawable.setBounds(leftBounds, topBounds, rightBounds, bottomBounds); 
		}
		mTextView.setCompoundDrawables(mLeftDrawable, mTopDrawable, mRightDrawable, mBottomDrawable);
	}
	
	/**
	 * 描述：设置图片尺寸.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabCompoundDrawablesBounds(int left, int top, int right, int bottom) {
		leftBounds = AbViewUtil.scaleValue(mContext, left);
		topBounds = AbViewUtil.scaleValue(mContext, top);
		rightBounds = AbViewUtil.scaleValue(mContext, right);
		bottomBounds = AbViewUtil.scaleValue(mContext, bottom);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabBackgroundResource(int resid) {
		this.setBackgroundResource(resid);
	}
	
	/**
	 * 描述：设置tab的背景选择.
	 *
	 * @param d the new tab background drawable
	 */
	public void setTabBackgroundDrawable(Drawable d) {
		this.setBackgroundDrawable(d);
	}
    
}