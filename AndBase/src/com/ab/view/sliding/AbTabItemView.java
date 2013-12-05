package com.ab.view.sliding;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：TabView.java 
 * 描述：表示一个TAB
 * @author zhaoqp
 * @date：2013-11-25 下午6:02:43
 * @version v1.0
 */
public class AbTabItemView extends LinearLayout {
	
	private Context mContext;
	//当前的索引
    private int mIndex;
    //包含的TextView
    private TextView mTextView;
	
    public AbTabItemView(Context context) {
		this(context,null);
	}

	public AbTabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOrientation(LinearLayout.HORIZONTAL);
        this.mContext = context;
        mTextView = new TextView(context);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
        mTextView.setFocusable(true);
        mTextView.setPadding(12, 5, 12, 5);
        mTextView.setSingleLine();
        this.addView(mTextView);
    }

    public void init(int index,String text) {
        mIndex = index;
        mTextView.setText(text);
    }


    public int getIndex() {
        return mIndex;
    }


	public TextView getTextView() {
		return mTextView;
	}

    /**
     * 
     * 描述：设置文字大小
     * @param tabTextSize
     * @throws 
     */
	public void setTabTextSize(int tabTextSize) {
		mTextView.setTextSize(tabTextSize);
	}

	/**
     * 
     * 描述：设置文字颜色
     * @param tabTextSize
     * @throws 
     */
	public void setTabTextColor(int tabColor) {
		mTextView.setTextColor(tabColor);
	}
	
	/**
     * 
     * 描述：设置文字图片
     * @throws 
     */
	public void setTabCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
		if(left!=null){
			left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight()); 
		}
		if(top!=null){
		    top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight()); 
		}
		if(right!=null){
		    right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
		}
		if(bottom!=null){
		    bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight()); 
		}
		mTextView.setCompoundDrawables(left, top, right, bottom);
	}
	
	/**
     * 
     * 描述：设置tab的背景选择
     * @param resid
     * @throws 
     */
	public void setTabBackgroundResource(int resid) {
		this.setBackgroundResource(resid);
	}
    
}