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
package com.ab.view.sample;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import com.ab.view.listener.AbOnScrollListener;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbHorizontalScrollView1.java 
 * 描述：有滚动事件监听的HorizontalScrollView
 * @author zhaoqp
 * @date：2013-11-20 下午3:00:53
 * @version v1.0
 */
public class AbHorizontalScrollView extends HorizontalScrollView {

	private int intitPosition;
	private int childWidth = 0;
	private AbOnScrollListener onScrollListner;
	
	public AbHorizontalScrollView(Context context) {
		super(context);
	}
	
	public AbHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		
		int newPosition = getScrollX();
		if (intitPosition - newPosition == 0) {
			if (onScrollListner == null) {
				return;
			}
			onScrollListner.onScrollStoped();
			new Handler().postDelayed(new Runnable(){
				@Override
				public void run(){
					Rect outRect = new Rect();
					getDrawingRect(outRect);
					if (getScrollX() == 0) {
						onScrollListner.onScroll(0);
						onScrollListner.onScrollToLeft();
					} else if (childWidth + getPaddingLeft() + getPaddingRight() == outRect.right) {
						onScrollListner.onScroll(getScrollX());
						onScrollListner.onScrollToRight();
					} else {
						onScrollListner.onScroll(getScrollX());
					}
				}
			},200);
			
		} else {
			intitPosition = getScrollX();
			checkTotalWidth();
		}
		super.onScrollChanged(l, t, oldl, oldt);
	}

	
	/**
	 * 
	 * 描述：设置监听器
	 * @param listner
	 * @throws 
	 */
	public void setOnScrollListener(AbOnScrollListener listner) {
		onScrollListner = listner;
	}
	
	/**
	 * 计算总宽.
	 */
	private void checkTotalWidth() {
		if (childWidth > 0) {
			return;
		}
		for (int i = 0; i < getChildCount(); i++) {
			childWidth += getChildAt(i).getWidth();
		}
	}
	
}
