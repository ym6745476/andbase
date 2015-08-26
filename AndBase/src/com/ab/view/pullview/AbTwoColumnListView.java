package com.ab.view.pullview;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbTwoColumnListView.java 
 * 描述：代替系统的GridView
 * @author 还如一梦中
 * @date 2015年8月12日 下午4:13:51
 * @version v1.0
 */
public class AbTwoColumnListView extends LinearLayout {

	private LinearLayout mainLayout;
	private LinearLayout firstColumn;
	private LinearLayout secondColumn;
	
	/** Adapter. */
	private BaseAdapter adapter = null;
	
	/** Adapter监听器. */
	private AdapterDataSetObserver mDataSetObserver;
	
	private ArrayList<View> items;
	
	private OnItemClickListener onItemClickListener;

	public AbTwoColumnListView(Context context) {
		this(context, null);
	}

	public AbTwoColumnListView(Context context, AttributeSet attrs,int defStyle) {
		super(context, attrs, defStyle);
		mainLayout = new LinearLayout(context);
		mainLayout.setBackgroundColor(Color.WHITE);
		mainLayout.setLayoutParams(new LayoutParams(500, 500));
		mainLayout.setOrientation(LinearLayout.HORIZONTAL);
		firstColumn = new LinearLayout(context);
		firstColumn.setOrientation(LinearLayout.VERTICAL);
		firstColumn.setPadding(10, 10, 5, 10);
		secondColumn = new LinearLayout(context);
		secondColumn.setOrientation(LinearLayout.VERTICAL);
		secondColumn.setPadding(5, 10, 10, 10);
		
		mainLayout.addView(firstColumn, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));
		mainLayout.addView(secondColumn, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT, 1));
		
		this.addView(mainLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		items = new ArrayList<View>();
	}

	public AbTwoColumnListView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	class AdapterDataSetObserver extends DataSetObserver {

		@Override
		public void onChanged() {
			int count = adapter.getCount();
			if (count > items.size()) {
				addChildren();
			} else {
				layoutChildren();
			}
			super.onChanged();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}

	}

	public void setAdapter(BaseAdapter adapter) {
		this.adapter = adapter;

		if (adapter != null && mDataSetObserver != null) {
			adapter.unregisterDataSetObserver(mDataSetObserver);
		}

		if (adapter != null) {
			mDataSetObserver = new AdapterDataSetObserver();
			adapter.registerDataSetObserver(mDataSetObserver);
		}
		layoutChildren();
	}

	protected void layoutChildren() {
		firstColumn.removeAllViews();
		secondColumn.removeAllViews();
		items.clear();
		if (adapter != null) {
			int itemCount = adapter.getCount();
			for (int i = 0; i < itemCount; i++) {
				View viewInfo = adapter.getView(i, null, null);
				viewInfo.setVisibility(View.VISIBLE);
				items.add(viewInfo);
				if ((i + 1) % 2 == 1) {
					firstColumn.addView(viewInfo);
				} else {
					secondColumn.addView(viewInfo);
				}
			}
		}
	}

	/**
	 * Adds the children.
	 */
	protected void addChildren() {
		if (adapter != null) {
			int count = adapter.getCount();
			if (count > items.size()) {
				for (int i = items.size(); i < count; i++) {
					View viewInfo = adapter.getView(i, null, null);
					items.add(viewInfo);
					if ((i + 1) % 2 == 1) {
						firstColumn.addView(viewInfo);
					} else {
						secondColumn.addView(viewInfo);
					}
				}
			}
		}

	}

	public OnItemClickListener getOnItemClickListener() {
		return onItemClickListener;
	}

	public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}

}
