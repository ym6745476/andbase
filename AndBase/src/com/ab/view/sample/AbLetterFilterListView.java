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
package com.ab.view.sample;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SectionIndexer;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn 
 * 名称：AbLetterFilterView.java 
 * 描述：字母索引View
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-10-24 下午1:36:45
 */
public class AbLetterFilterListView extends RelativeLayout {
	
	/** The context. */
	private Context context;
	
	/** The section indexter. */
	private SectionIndexer sectionIndexter = null;
	
	/** The list view. */
	private ListView listView;
	
	/** The letter view. */
	private LetterView letterView;
	
	/**
	 * Instantiates a new ab letter filter list view.
	 *
	 * @param context the context
	 */
	public AbLetterFilterListView(Context context) {
		super(context);
		init(context);
	}

	/**
	 * Instantiates a new ab letter filter list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbLetterFilterListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * Instantiates a new ab letter filter list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AbLetterFilterListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	/**
	 * Inits the.
	 *
	 * @param context the context
	 */
	private void init(Context context) {
		this.context  = context;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onFinishInflate()
	 */
	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		int count = getChildCount();
		if (count < 1) {
			throw new IllegalArgumentException("this layout must contain 1 child views,and AdapterView  must in the first position!");
		}
		View view = this.getChildAt(0);
		AdapterView<?> adapterView = null;
		if (view instanceof AdapterView<?>) {
			adapterView = (AdapterView<?>) view;
			listView = (ListView)adapterView;
			sectionIndexter = (SectionIndexer) listView.getAdapter();
			letterView = new LetterView(context);
			letterView.setListView(listView);
			RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(60, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
			layoutParams.topMargin=10;
			layoutParams.rightMargin=10;
			layoutParams.bottomMargin=10;
			this.addView(letterView,layoutParams);
		}
		if (adapterView == null) {
			throw new IllegalArgumentException("must contain a AdapterView in this layout!");
		}
	}

	/**
	 * The Class LetterView.
	 */
	public class LetterView extends View{
		
		/** The list. */
		private ListView listView;
		
		/** The l. */
		private char[] l;
		
		/** The paint. */
		private Paint paint;
		
		/** The width center. */
		private float widthCenter;

		/** 字母之间的间距. */
		private float singleHeight;
		
		/** The gradient drawable. */
		private GradientDrawable gradientDrawable = null;
		
		/**
		 * Instantiates a new letter view.
		 *
		 * @param context the context
		 * @param attrs the attrs
		 * @param defStyle the def style
		 */
		public LetterView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			init();
		}

		/**
		 * Instantiates a new letter view.
		 *
		 * @param context the context
		 * @param attrs the attrs
		 */
		public LetterView(Context context, AttributeSet attrs) {
			super(context, attrs);
			init();
		}

		/**
		 * Instantiates a new letter view.
		 *
		 * @param context the context
		 */
		public LetterView(Context context) {
			super(context);
			init();
		}
		
		/**
		 * Inits the.
		 */
		private void init() {
			l = new char[] {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K',
					'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
					'X', 'Y', 'Z', '#' };
			paint = new Paint();
			paint.setColor(Color.parseColor("#949494"));
			paint.setTypeface(Typeface.DEFAULT_BOLD);
			paint.setTextSize(22);
			paint.setAntiAlias(true);
			paint.setTextAlign(Paint.Align.CENTER);
			
			gradientDrawable = new GradientDrawable(Orientation.BOTTOM_TOP, new int []{0x99B0B0B0,0x99B0B0B0});
			gradientDrawable.setCornerRadius(30);

		}

		/* (non-Javadoc)
		 * @see android.view.View#onDraw(android.graphics.Canvas)
		 */
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			
			float height = getHeight();
			singleHeight = height / l.length;
			widthCenter = getMeasuredWidth() / (float) 2;
			for (int i = 0; i < l.length; i++) {
				canvas.drawText(String.valueOf(l[i]), widthCenter, singleHeight
						+ (i * singleHeight), paint);
			}
			
		}

		/**
		 * Gets the list view.
		 *
		 * @return the list view
		 */
		public ListView getListView() {
			return listView;
		}

		/**
		 * Sets the list view.
		 *
		 * @param listView the new list view
		 */
		public void setListView(ListView listView) {
			this.listView = listView;
		}

		/* (non-Javadoc)
		 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
		 */
		public boolean onTouchEvent(MotionEvent event) {
			super.onTouchEvent(event);
			int i = (int) event.getY();
			int div = (int) singleHeight;
			int idx = 0;
			if (div != 0) {
				idx = i / div;
			}
			if (idx >= l.length) {
				idx = l.length - 1;
			} else if (idx < 0) {
				idx = 0;
			}
			
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				setBackgroundDrawable(new ColorDrawable(0x00000000));
				break;
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_MOVE:
				
				setBackgroundDrawable(gradientDrawable);
				
				if (listView.getAdapter() != null) {
					HeaderViewListAdapter listAdapter = (HeaderViewListAdapter) listView.getAdapter();
					if (sectionIndexter == null) {
						sectionIndexter = (SectionIndexer) listAdapter.getWrappedAdapter();
					}
					int position = sectionIndexter.getPositionForSection(l[idx]);
					if (position == -1) {
						return true;
					}
					listView.setSelection(position);
				}
			}
			return true;
		}
	}

}
