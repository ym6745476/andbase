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
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Scroller;

import com.ab.view.listener.AbOnListViewListener;

// TODO: Auto-generated Javadoc
/**
 * The Class AbPullListView.
 */
public class AbPullListView extends ListView implements OnScrollListener {

	/** The m last y. */
	private float mLastY = -1;
	
	/** The m scroller. */
	private Scroller mScroller; 
	
	/** The m scroll listener. */
	private OnScrollListener mScrollListener; 

	/** The m list view listener. */
	private AbOnListViewListener mListViewListener;

	/** The m header view. */
	private AbListViewHeader mHeaderView;
	
	/** The m footer view. */
	private AbListViewFooter mFooterView;
	
	/** The m header view height. */
	private int mHeaderViewHeight; 
	
	/** The m footer view height. */
	private int mFooterViewHeight; 
	
	/** The m enable pull refresh. */
	private boolean mEnablePullRefresh = true;
	
	/** The m enable pull load. */
	private boolean mEnablePullLoad = true;
	
	/** The m pull refreshing. */
	private boolean mPullRefreshing = false;
	
	/** The m pull loading. */
	private boolean mPullLoading;

	/** The m is footer ready. */
	private boolean mIsFooterReady = false;

	// total list items, used to detect is at the bottom of listview.
	/** The m total item count. */
	private int mTotalItemCount;

	// for mScroller, scroll back from header or footer.
	/** The m scroll back. */
	private int mScrollBack;
	
	/** The Constant SCROLLBACK_HEADER. */
	private final static int SCROLLBACK_HEADER = 0;
	
	/** The Constant SCROLLBACK_FOOTER. */
	private final static int SCROLLBACK_FOOTER = 1;

	/** The Constant SCROLL_DURATION. */
	private final static int SCROLL_DURATION = 200;
	
	/** The Constant OFFSET_RADIO. */
	private final static float OFFSET_RADIO = 1.8f; // support iOS like pull
													// feature.
	/** 数据相关. */
	private ListAdapter mAdapter = null;
	
	/**上一次的数量*/
	private int count = 0;
	
	/**
	 * Instantiates a new ab pull list view.
	 *
	 * @param context the context
	 */
	public AbPullListView(Context context) {
		super(context);
		initWithContext(context);
	}

	/**
	 * Instantiates a new ab pull list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbPullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initWithContext(context);
	}

	/**
	 * Inits the with context.
	 *
	 * @param context the context
	 */
	private void initWithContext(Context context) {
		
		mScroller = new Scroller(context, new DecelerateInterpolator());
		
		super.setOnScrollListener(this);

		// init header view
		mHeaderView = new AbListViewHeader(context);
		
		// init header height
		mHeaderViewHeight = mHeaderView.getHeaderHeight();
		mHeaderView.setGravity(Gravity.BOTTOM);
		addHeaderView(mHeaderView);

		// init footer view
		mFooterView = new AbListViewFooter(context);
		
		mFooterViewHeight= mFooterView.getFooterHeight();
		
		//默认是打开刷新与更多
		setPullRefreshEnable(true);
		setPullLoadEnable(true);
		
		mFooterView.hide();
	}

	/**
	 * 描述：TODO
	 * @see android.widget.ListView#setAdapter(android.widget.ListAdapter)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:32
	 * @version v1.0
	 */
	@Override
	public void setAdapter(ListAdapter adapter) {
		mAdapter = adapter;
		// make sure XListViewFooter is the last footer view, and only add once.
		if (mIsFooterReady == false) {
			mIsFooterReady = true;
			mFooterView.setGravity(Gravity.TOP);
			addFooterView(mFooterView);
		}
		super.setAdapter(adapter);
	}

	/**
	 * 打开或者关闭下拉刷新功能.
	 *
	 * @param enable 开关标记
	 */
	public void setPullRefreshEnable(boolean enable) {
		mEnablePullRefresh = enable;
		if (!mEnablePullRefresh) {
			mHeaderView.setVisibility(View.INVISIBLE);
		} else {
			mHeaderView.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 打开或者关闭加载更多功能.
	 *
	 * @param enable 开关标记
	 */
	public void setPullLoadEnable(boolean enable) {
		mEnablePullLoad = enable;
		if (!mEnablePullLoad) {
			mFooterView.hide();
			mFooterView.setOnClickListener(null);
		} else {
			mPullLoading = false;
			mFooterView.setState(AbListViewFooter.STATE_READY);
			//load more点击事件.
			mFooterView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startLoadMore();
				}
			});
		}
	}

	/**
	 * stop refresh, reset header view.
	 */
	public void stopRefresh() {
		if (mPullRefreshing == true) {
			mPullRefreshing = false;
			resetHeaderHeight();
		}
		
		count = mAdapter.getCount();
		//判断有没有数据
		if(count>0){
			mFooterView.setState(AbListViewFooter.STATE_READY);
		}else{
			mFooterView.setState(AbListViewFooter.STATE_EMPTY);
		}
	}

	/**
	 * stop load more, reset footer view.
	 *
	 */
	public void stopLoadMore() {
		mFooterView.hide();
		if (mPullLoading == true) {
			mPullLoading = false;
			mFooterView.setState(AbListViewFooter.STATE_READY);
		}
		int countNew = mAdapter.getCount();
		//判断有没有更多数据了
		if(countNew > count){
			mFooterView.setState(AbListViewFooter.STATE_READY);
		}else{
			mFooterView.setState(AbListViewFooter.STATE_NO);
		}
	}



	/**
	 * Update header height.
	 *
	 * @param delta the delta
	 */
	private void updateHeaderHeight(float delta) {
		int newHeight = (int) delta + mHeaderView.getVisiableHeight();
		mHeaderView.setVisiableHeight(newHeight);
		if (mEnablePullRefresh && !mPullRefreshing) {
			if (mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
				mHeaderView.setState(AbListViewHeader.STATE_READY);
			} else {
				mHeaderView.setState(AbListViewHeader.STATE_NORMAL);
			}
		}
		setSelection(0); 
	}

	/**
	 * 根据状态设置Header的位置.
	 */
	private void resetHeaderHeight() {
		//当前下拉到的高度
		int height = mHeaderView.getVisiableHeight();
		if (height < mHeaderViewHeight || !mPullRefreshing) {
			//距离短  隐藏
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -1*height, SCROLL_DURATION);
		}else if(height > mHeaderViewHeight || !mPullRefreshing){
			//距离多的  弹回到mHeaderViewHeight
			mScrollBack = SCROLLBACK_HEADER;
			mScroller.startScroll(0, height, 0, -(height-mHeaderViewHeight), SCROLL_DURATION);
		}
		
		invalidate();
	}


	/**
	 * Start load more.
	 */
	private void startLoadMore() {
		mFooterView.show();
		mPullLoading = true;
		mFooterView.setState(AbListViewFooter.STATE_LOADING);
		if (mListViewListener != null) {
			//开始下载数据
			mListViewListener.onLoadMore();
		}
	}

	/**
	 * 描述：onTouchEvent
	 * @see android.widget.ListView#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (mLastY == -1) {
			mLastY = ev.getRawY();
		}

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastY = ev.getRawY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getRawY() - mLastY;
			mLastY = ev.getRawY();
			if (mEnablePullRefresh && getFirstVisiblePosition() == 0 && (mHeaderView.getVisiableHeight() > 0 || deltaY > 0)) {
				updateHeaderHeight(deltaY / OFFSET_RADIO);
			} else if (mEnablePullLoad && getLastVisiblePosition() == mTotalItemCount - 1 && deltaY<0) {
				startLoadMore();
			}
			break;
		case MotionEvent.ACTION_UP:
			mLastY = -1; 
			if (getFirstVisiblePosition() == 0) {
				//需要刷新的条件
				if (mEnablePullRefresh && mHeaderView.getVisiableHeight() >= mHeaderViewHeight) {
					mPullRefreshing = true;
					mHeaderView.setState(AbListViewHeader.STATE_REFRESHING);
					if (mListViewListener != null) {
						//刷新
						mListViewListener.onRefresh();
					}
				}
				
				if(mEnablePullRefresh){
					//弹回
					resetHeaderHeight();
				}
			}
			break;
		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 描述：TODO
	 * @see android.view.View#computeScroll()
	 */
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			if (mScrollBack == SCROLLBACK_HEADER) {
				mHeaderView.setVisiableHeight(mScroller.getCurrY());
			}
			postInvalidate();
		}
		super.computeScroll();
	}

	/**
	 * 描述：设置ListView的监听器.
	 *
	 * @param listViewListener the new ab on list view listener
	 */
	public void setAbOnListViewListener(AbOnListViewListener listViewListener) {
		mListViewListener = listViewListener;
	}

	/**
	 * 描述：TODO
	 * @see android.widget.AbsListView.OnScrollListener#onScrollStateChanged(android.widget.AbsListView, int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:32
	 * @version v1.0
	 */
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mScrollListener != null) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	/**
	 * 描述：TODO
	 * @see android.widget.AbsListView.OnScrollListener#onScroll(android.widget.AbsListView, int, int, int)
	 */
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// send to user's listener
		mTotalItemCount = totalItemCount;
		if (mScrollListener != null) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}	
	}

	/**
	 * 
	 * 描述：获取Header View
	 * @return
	 * @throws 
	 */
	public AbListViewHeader getHeaderView() {
		return mHeaderView;
	}

	/**
	 * 
	 * 描述：获取Footer View
	 * @return
	 * @throws 
	 */
	public AbListViewFooter getFooterView() {
		return mFooterView;
	}
	
	/**
	 * 
	 * 描述：获取Header ProgressBar，用于设置自定义样式
	 * @return
	 * @throws 
	 */
	public ProgressBar getHeaderProgressBar() {
		return mHeaderView.getHeaderProgressBar();
	}
	
	
	/**
	 * 
	 * 描述：获取Footer ProgressBar，用于设置自定义样式
	 * @return
	 * @throws 
	 */
	public ProgressBar getFooterProgressBar() {
		return mFooterView.getFooterProgressBar();
	}
	
	
}
