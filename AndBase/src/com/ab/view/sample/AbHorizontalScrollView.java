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
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

// TODO: Auto-generated Javadoc
/**
 * The Class AbHorizontalScrollView.
 */
public class AbHorizontalScrollView extends HorizontalScrollView {
	
	/** The scroller task. */
	private Runnable scrollerTask;
	
	/** The intit position. */
	private int intitPosition;
	
	/** The new check. */
	private int newCheck = 100;
	
	/** The child width. */
	private int childWidth = 0;

	/**
	 * The Interface OnScrollStopListner.
	 */
	public interface OnScrollStopListner {
		
		/**
		 * scroll have stoped.
		 */
		void onScrollStoped();

		/**
		 * scroll have stoped, and is at left edge.
		 */
		void onScrollToLeftEdge();

		/**
		 * scroll have stoped, and is at right edge.
		 */
		void onScrollToRightEdge();

		/**
		 * scroll have stoped, and is at middle.
		 */
		void onScrollToMiddle();
	}

	/** The on scrollstop listner. */
	private OnScrollStopListner onScrollstopListner;

	/**
	 * Instantiates a new ab horizontal scroll view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		scrollerTask = new Runnable() {
			@Override
			public void run() {
				int newPosition = getScrollX();
				if (intitPosition - newPosition == 0) {
					if (onScrollstopListner == null) {
						return;
					}
					onScrollstopListner.onScrollStoped();
					Rect outRect = new Rect();
					getDrawingRect(outRect);
					if (getScrollX() == 0) {
						onScrollstopListner.onScrollToLeftEdge();
					} else if (childWidth + getPaddingLeft() + getPaddingRight() == outRect.right) {
						onScrollstopListner.onScrollToRightEdge();
					} else {
						onScrollstopListner.onScrollToMiddle();
					}
				} else {
					intitPosition = getScrollX();
					postDelayed(scrollerTask, newCheck);
				}
			}
		};
	}

	/**
	 * Sets the on scroll stop listner.
	 *
	 * @param listner the new on scroll stop listner
	 */
	public void setOnScrollStopListner(OnScrollStopListner listner) {
		onScrollstopListner = listner;
	}

	/**
	 * Start scroller task.
	 */
	public void startScrollerTask() {
		intitPosition = getScrollX();
		postDelayed(scrollerTask, newCheck);
		checkTotalWidth();
	}

	/**
	 * Check total width.
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