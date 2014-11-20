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
package com.ab.view.pullview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.ab.util.AbLogUtil;


// TODO: Auto-generated Javadoc
/**
 * The Class AbMultiColumnListView.
 */
public class AbMultiColumnListView extends ScrollView{

	/** 每一列的宽度. */
	private int columnWidth;

	/** 当前第一列的高度. */
	private int firstColumnHeight;

	/** 当前第二列的高度. */
	private int secondColumnHeight;

	/** 当前第三列的高度. */
	private int thirdColumnHeight;

	/** 是否已加载过一次layout，这里onLayout中的初始化只需加载一次. */
	private boolean loadOnce;
	
	/**  布局的高度。. */
	private static int scrollViewHeight;

	/** 第一列的布局. */
	private LinearLayout firstColumn;

	/** 第二列的布局. */
	private LinearLayout secondColumn;

	/** 第三列的布局. */
	private LinearLayout thirdColumn;

	/** 直接子布局. */
	private LinearLayout scrollLayout;

	/**  Adapter. */
	private AbMultiColumnListAdapter mAdapter = null;
	
	/**  Adapter改变的监听器. */
	private AdapterDataSetObserver mDataSetObserver;
	
	/**  已加载的View. */
	private List<AbViewInfo> mItems = null;
	
	/**  已加载的View. */
	private OnScrollListener mOnScrollListener = null;
	
	/**  可释放图片资源的id. */
	private int[] mReleaseImageResIds;
	
	/**
	 * Instantiates a new ab multi column list view.
	 *
	 * @param context the context
	 */
	public AbMultiColumnListView(Context context) {
		this(context,null);
	}
	
	/**
	 * Instantiates a new ab multi column list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbMultiColumnListView(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}
	
	/**
	 * Instantiates a new ab multi column list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AbMultiColumnListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		scrollLayout = new LinearLayout(context);
		scrollLayout.setOrientation(LinearLayout.HORIZONTAL);
		firstColumn = new LinearLayout(context);
		firstColumn.setOrientation(LinearLayout.VERTICAL);
		secondColumn = new LinearLayout(context);
		secondColumn.setOrientation(LinearLayout.VERTICAL);
		thirdColumn = new LinearLayout(context);
		thirdColumn.setOrientation(LinearLayout.VERTICAL);
		scrollLayout.addView(firstColumn, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
		scrollLayout.addView(secondColumn, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
		scrollLayout.addView(thirdColumn, new LinearLayout.LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
		this.addView(scrollLayout, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		mItems = new ArrayList<AbViewInfo>();
	}

	/**
	 * Gets the adapter.
	 *
	 * @return the adapter
	 */
	public AbMultiColumnListAdapter getAdapter() {
		return mAdapter;
	}

	/**
	 * Sets the adapter.
	 *
	 * @param adapter the new adapter
	 */
	public void setAdapter(AbMultiColumnListAdapter adapter) {
		this.mAdapter = adapter;
		
		if (mAdapter != null && mDataSetObserver != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
		
		if (mAdapter != null) {
            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);
		}
		
		layoutChildren();
	}
	
	/**
	 * 进行一些关键性的初始化操作，获取AbMultiColumnListView的高度，
	 * 以及得到第一列的宽度值。并在这里开始加载第一页的图片。.
	 *
	 * @param changed the changed
	 * @param l the l
	 * @param t the t
	 * @param r the r
	 * @param b the b
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		//AbLogUtil.d(AbMultiColumnListView.class, "onLayout");
		if (changed && !loadOnce) {
			scrollViewHeight = getHeight();
			scrollLayout = (LinearLayout)getChildAt(0);
			columnWidth = firstColumn.getWidth();
			loadOnce = true;
		}
		
	}
	
    /**
     * Layout children.
     */
    protected void layoutChildren() {
    	AbLogUtil.d(AbMultiColumnListView.class, "layoutChildren");
    	firstColumn.removeAllViews();
		secondColumn.removeAllViews();
		thirdColumn.removeAllViews();
		mItems.clear();
		firstColumnHeight = 0;
        secondColumnHeight = 0;
        thirdColumnHeight = 0;
    	if(mAdapter != null){
    		int mItemCount = mAdapter.getCount();
    		for (int i = 0; i < mItemCount; i++) {
    			AbViewInfo viewInfo = mAdapter.getView(i, null, null);
    			viewInfo.setVisible(View.VISIBLE);
    			findColumnToAdd(viewInfo);
    			mItems.add(viewInfo);
        	}
    	}
    }
    
    /**
     * Adds the children.
     */
    protected void addChildren() {
    	AbLogUtil.d(AbMultiColumnListView.class, "addChildren");
    	if(mAdapter != null){
    		int count = mAdapter.getCount();
    		if(count > mItems.size()){
    			for (int i = mItems.size(); i < count; i++) {
        			AbViewInfo viewInfo = mAdapter.getView(i, null, null);
        			viewInfo.setVisible(View.VISIBLE);
        			findColumnToAdd(viewInfo);
        			mItems.add(viewInfo);
            	}
    		}
    	}
    	
    }

	
	/**
	 * 找到此时应该添加View的一列。原则就是对三列的高度进行判断，
	 * 当前高度最小的一列就是应该添加的一列。.
	 *
	 * @param viewInfo the view info
	 */
	private void findColumnToAdd(AbViewInfo viewInfo) {
		int width = viewInfo.getWidth();
		int height = viewInfo.getHeight();
		int scaledHeight = 0;
		double ratio = width / (columnWidth * 1.0);
	    scaledHeight = (int) (height / ratio);
	    View view = viewInfo.getView();
	    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
	    		columnWidth, scaledHeight);
		
		if (firstColumnHeight <= secondColumnHeight) {
			if (firstColumnHeight <= thirdColumnHeight) {
				viewInfo.setTop(firstColumnHeight);
				firstColumnHeight += scaledHeight;
				viewInfo.setBottom(firstColumnHeight);
				firstColumn.addView(view,params);
			}else{
				viewInfo.setTop(thirdColumnHeight);
				thirdColumnHeight += scaledHeight;
				viewInfo.setBottom(thirdColumnHeight);
				thirdColumn.addView(view,params);
			}
			
		} else {
			if (secondColumnHeight <= thirdColumnHeight) {
				viewInfo.setTop(secondColumnHeight);
				secondColumnHeight += scaledHeight;
				viewInfo.setBottom(secondColumnHeight);
				secondColumn.addView(view,params);
			}else{
				viewInfo.setTop(thirdColumnHeight);
				thirdColumnHeight += scaledHeight;
				viewInfo.setBottom(thirdColumnHeight);
				thirdColumn.addView(view,params);
			}
			
		}
		
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onScrollChanged(int, int, int, int)
	 */
	@Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        //将不可见区域的资源释放
        AbViewInfo viewInfo = null;
        for(int i=0;i<mItems.size();i++){
        	viewInfo = mItems.get(i);
        	if(!checkVisibility(i)){
        		if(viewInfo.getVisible()==View.VISIBLE){
        			viewInfo.setVisible(View.INVISIBLE);
        			ImageView imageView = null;
    				for(int id:mReleaseImageResIds){
    				    imageView = (ImageView) viewInfo.getView().findViewById(id);
    				    imageView.setImageBitmap(null);
    				}
        		}
        		
        	}else{
        		try {
					if(viewInfo.getVisible()==View.INVISIBLE){
						//重新getView
						viewInfo = mAdapter.getView(i, viewInfo, null);
						viewInfo.setVisible(View.VISIBLE);
					}
				} catch (Exception e) {
				}
        		
        	}
        	
        }
        
        mOnScrollListener.onScrollChanged(x, y, oldx, oldy);
    }
	
	/**
	 * 遍历List中的每个View，对可见性进行检查.
	 *
	 * @param position the position
	 * @return true, if successful
	 */
	public boolean checkVisibility(int position) {
		AbViewInfo viewInfo = mItems.get(position);
		int borderTop = viewInfo.getTop();
		int borderBottom = viewInfo.getBottom();
		if (borderBottom > getScrollY()
				&& borderTop < getScrollY() + scrollViewHeight) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * An asynchronous update interface for receiving notifications
	 * about AdapterDataSet information as the AdapterDataSet is constructed.
	 */
	class AdapterDataSetObserver extends DataSetObserver {

		/* (non-Javadoc)
		 * @see android.database.DataSetObserver#onChanged()
		 */
		@Override
		public void onChanged() {
			AbLogUtil.d(AbMultiColumnListView.class, "onChanged");
			//判断是刷新还是添加
    		int count = mAdapter.getCount();
    		if(count > mItems.size()){
    			//添加
    			addChildren();
    		}else{
    			//刷新
    			layoutChildren();
    		}
			
			super.onChanged();
		}

		/* (non-Javadoc)
		 * @see android.database.DataSetObserver#onInvalidated()
		 */
		@Override
		public void onInvalidated() {
			
			super.onInvalidated();
		}
		
   }
	
	
	
   
   /**
    * Gets the on scroll listener.
    *
    * @return the on scroll listener
    */
   public OnScrollListener getOnScrollListener() {
		return mOnScrollListener;
	}

	/**
	 * Sets the on scroll listener.
	 *
	 * @param onScrollListener the new on scroll listener
	 */
	public void setOnScrollListener(OnScrollListener onScrollListener) {
		this.mOnScrollListener = onScrollListener;
	}
	
    /**
     * Gets the release image res ids.
     *
     * @return the release image res ids
     */
    public int[] getReleaseImageResIds() {
		return mReleaseImageResIds;
	}

	/**
	 * Sets the release image res ids.
	 *
	 * @param releaseImageResIds the new release image res ids
	 */
	public void setReleaseImageResIds(int[] releaseImageResIds) {
		this.mReleaseImageResIds = releaseImageResIds;
	}

/**
    * The listener interface for receiving onScroll events.
    * The class that is interested in processing a onScroll
    * event implements this interface, and the object created
    * with that class is registered with a component using the
    * component's <code>addOnScrollListener<code> method. When
    * the onScroll event occurs, that object's appropriate
    * method is invoked.
    *
    * @see OnScrollEvent
    */
   public interface OnScrollListener {
		 
	    /**
    	 * On scroll changed.
    	 *
    	 * @param x the x
    	 * @param y the y
    	 * @param oldx the oldx
    	 * @param oldy the oldy
    	 */
    	void onScrollChanged(int x, int y, int oldx, int oldy);
	 
   }

}