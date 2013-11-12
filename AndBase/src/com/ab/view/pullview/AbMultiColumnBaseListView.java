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
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbMultiColumnBaseListView.java 
 * 描述：The Class AbMultiColumnBaseListView.
 * @author zhaoqp
 * @date：2013-10-28 上午9:30:50
 * @version v1.0
 */
public class AbMultiColumnBaseListView extends AbMultiColumnAbsListView {

	/** The Constant TAG. */
	@SuppressWarnings("unused")
	private static final String TAG = "MultiColumnListView";

	/** The Constant DEFAULT_COLUMN_NUMBER. */
	private static final int DEFAULT_COLUMN_NUMBER = 2;

	/** The m column number. */
	private int mColumnNumber = 2;
	
	/** The m columns. */
	private Column[] mColumns = null;
	
	/** The m fixed column. */
	private Column mFixedColumn = null; // column for footers & headers.
	
	/** The m items. */
	private SparseIntArray mItems = new SparseIntArray();

	/** The m column padding left. */
	private int mColumnPaddingLeft = 0;
	
	/** The m column padding right. */
	private int mColumnPaddingRight = 0;

	/**
	 * Instantiates a new ab multi column base list view.
	 *
	 * @param context the context
	 */
	public AbMultiColumnBaseListView(Context context) {
		super(context);
		init(null);
	}

	/**
	 * Instantiates a new ab multi column base list view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbMultiColumnBaseListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}


	/** The m frame rect. */
	private Rect mFrameRect = new Rect();

	/**
	 * 初始化.
	 *
	 * @param attrs the attrs
	 */
	private void init(AttributeSet attrs) {
		getWindowVisibleDisplayFrame(mFrameRect);

		if (attrs == null) {
			mColumnNumber = (DEFAULT_COLUMN_NUMBER); // default column number is
														// 2.
		} else {

			int landColNumber = 3;
			int defColNumber = 3;

			if (mFrameRect.width() > mFrameRect.height() && landColNumber != -1) {
				mColumnNumber = (landColNumber);
			} else if (defColNumber != -1) {
				mColumnNumber = (defColNumber);
			} else {
				mColumnNumber = (DEFAULT_COLUMN_NUMBER);
			}

			mColumnPaddingLeft = 0;
			mColumnPaddingRight = 0;
		}

		mColumns = new Column[getColumnNumber()];
		for (int i = 0; i < getColumnNumber(); ++i)
			mColumns[i] = new Column(i);

		mFixedColumn = new FixedColumn();
	}

	// /////////////////////////////////////////////////////////////////////
	// Override Methods...
	// /////////////////////////////////////////////////////////////////////

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#onLayout(boolean, int, int, int, int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		// TODO the adapter status may be changed. what should i do here...
	}

	/** The column width. */
	private int columnWidth;

	/**
	 * Gets the column width.
	 *
	 * @return the column width
	 */
	public int getColumnWidth() {
		return columnWidth;
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#onMeasure(int, int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		columnWidth = (getMeasuredWidth() - mListPadding.left
				- mListPadding.right - mColumnPaddingLeft - mColumnPaddingRight)
				/ getColumnNumber();

		for (int index = 0; index < getColumnNumber(); ++index) {
			mColumns[index].mColumnWidth = columnWidth;
			mColumns[index].mColumnLeft = mListPadding.left
					+ mColumnPaddingLeft + columnWidth * index;
		}

		mFixedColumn.mColumnLeft = mListPadding.left;
		mFixedColumn.mColumnWidth = getMeasuredWidth();
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#onMeasureChild(android.view.View, int, int, int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onMeasureChild(View child, int position,
			int widthMeasureSpec, int heightMeasureSpec) {
		if (isFixedView(child))
			child.measure(widthMeasureSpec, heightMeasureSpec);
		else
			child.measure(MeasureSpec.EXACTLY | getColumnWidth(position),
					heightMeasureSpec);
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#modifyFlingInitialVelocity(int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int modifyFlingInitialVelocity(int initialVelocity) {
		return initialVelocity / getColumnNumber();
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#onItemAddedToList(int, boolean)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onItemAddedToList(int position, boolean flow) {
		super.onItemAddedToList(position, flow);

		if (isHeaderOrFooterPosition(position) == false) {
			Column col = getNextColumn(flow, position);
			mItems.append(position, col.getIndex());
		}
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#onLayoutSync(int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onLayoutSync(int syncPos) {
		for (Column c : mColumns) {
			c.save();
		}
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#onLayoutSyncFinished(int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onLayoutSyncFinished(int syncPos) {
		for (Column c : mColumns) {
			c.clear();
		}
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#onAdjustChildViews(boolean)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected void onAdjustChildViews(boolean down) {

		int firstItem = getFirstVisiblePosition();
		if (down == false && firstItem == 0) {
			final int firstColumnTop = mColumns[0].getTop();
			for (Column c : mColumns) {
				final int top = c.getTop();
				// align all column's top to 0's column.
				c.offsetTopAndBottom(firstColumnTop - top);
			}
		}
		super.onAdjustChildViews(down);
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#getFillChildBottom()
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getFillChildBottom() {
		// return smallest bottom value.
		// in order to determine fill down or not... (calculate below space)
		int result = Integer.MAX_VALUE;
		for (Column c : mColumns) {
			int bottom = c.getBottom();
			result = result > bottom ? bottom : result;
		}
		return result;
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#getFillChildTop()
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getFillChildTop() {
		// find largest column.
		int result = Integer.MIN_VALUE;
		for (Column c : mColumns) {
			int top = c.getTop();
			result = result < top ? top : result;
		}
		return result;
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#getScrollChildBottom()
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getScrollChildBottom() {
		// return largest bottom value.
		// for checking scrolling region...
		int result = Integer.MIN_VALUE;
		for (Column c : mColumns) {
			int bottom = c.getBottom();
			result = result < bottom ? bottom : result;
		}
		return result;
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnBaseAbsListView#getScrollChildTop()
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getScrollChildTop() {
		// find largest column.
		int result = Integer.MAX_VALUE;
		for (Column c : mColumns) {
			int top = c.getTop();
			result = result > top ? top : result;
		}
		return result;
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#getItemLeft(int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getItemLeft(int pos) {

		if (isHeaderOrFooterPosition(pos))
			return mFixedColumn.getColumnLeft();

		return getColumnLeft(pos);
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#getItemTop(int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getItemTop(int pos) {

		if (isHeaderOrFooterPosition(pos))
			return mFixedColumn.getBottom(); // footer view should be placed
												// below the last column.

		int colIndex = mItems.get(pos, -1);
		if (colIndex == -1)
			return getFillChildBottom();

		return mColumns[colIndex].getBottom();
	}

	/**
	 * 描述：TODO
	 * @see com.ab.view.pullview.AbMultiColumnAbsListView#getItemBottom(int)
	 * @author: zhaoqp
	 * @date：2013-9-4 下午4:06:33
	 * @version v1.0
	 */
	@Override
	protected int getItemBottom(int pos) {

		if (isHeaderOrFooterPosition(pos))
			return mFixedColumn.getTop(); // header view should be place above
											// the first column item.

		int colIndex = mItems.get(pos, -1);
		if (colIndex == -1)
			return getFillChildTop();

		return mColumns[colIndex].getTop();
	}

	// ////////////////////////////////////////////////////////////////////////////
	// Private Methods...
	// ////////////////////////////////////////////////////////////////////////////

	// flow If flow is true, align top edge to y. If false, align bottom edge to
	// y.
	/**
	 * Gets the next column.
	 *
	 * @param flow the flow
	 * @param position the position
	 * @return the next column
	 */
	private Column getNextColumn(boolean flow, int position) {

		// we already have this item...
		int colIndex = mItems.get(position, -1);
		if (colIndex != -1) {
			return mColumns[colIndex];
		}

		// adjust position (exclude headers...)
		position = Math.max(0, position - getHeaderViewsCount());

		final int lastVisiblePos = Math.max(0, position);
		if (lastVisiblePos < getColumnNumber())
			return mColumns[lastVisiblePos];

		if (flow) {
			// find column which has the smallest bottom value.
			return gettBottomColumn();
		} else {
			// find column which has the smallest top value.
			return getTopColumn();
		}
	}

	/**
	 * Checks if is header or footer position.
	 *
	 * @param pos the pos
	 * @return true, if is header or footer position
	 */
	private boolean isHeaderOrFooterPosition(int pos) {
		int type = mAdapter.getItemViewType(pos);
		return type == ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
	}

	/**
	 * Gets the top column.
	 *
	 * @return the top column
	 */
	private Column getTopColumn() {
		Column result = mColumns[0];
		for (Column c : mColumns) {
			result = result.getTop() > c.getTop() ? c : result;
		}
		return result;
	}

	/**
	 * Gets the t bottom column.
	 *
	 * @return the t bottom column
	 */
	private Column gettBottomColumn() {
		Column result = mColumns[0];
		for (Column c : mColumns) {
			result = result.getBottom() > c.getBottom() ? c : result;
		}

		if (DEBUG)
			Log.d("Column", "get Shortest Bottom Column: " + result.getIndex());
		return result;
	}

	/**
	 * Gets the column left.
	 *
	 * @param pos the pos
	 * @return the column left
	 */
	private int getColumnLeft(int pos) {
		int colIndex = mItems.get(pos, -1);

		if (colIndex == -1)
			return 0;

		return mColumns[colIndex].getColumnLeft();
	}

	/**
	 * Gets the column width.
	 *
	 * @param pos the pos
	 * @return the column width
	 */
	private int getColumnWidth(int pos) {
		int colIndex = mItems.get(pos, -1);

		if (colIndex == -1)
			return 0;

		return mColumns[colIndex].getColumnWidth();
	}

	// /////////////////////////////////////////////////////////////
	// Inner Class.
	// /////////////////////////////////////////////////////////////

	/**
	 * Gets the column number.
	 *
	 * @return the column number
	 */
	public int getColumnNumber() {
		return mColumnNumber;
	}

	/**
	 * The Class Column.
	 */
	private class Column {

		/** The m index. */
		private int mIndex;
		
		/** The m column width. */
		private int mColumnWidth;
		
		/** The m column left. */
		private int mColumnLeft;
		
		/** The m synched top. */
		private int mSynchedTop = 0;
		
		/** The m synched bottom. */
		private int mSynchedBottom = 0;

		// TODO is it ok to use item position info to identify item??

		/**
		 * Instantiates a new column.
		 *
		 * @param index the index
		 */
		public Column(int index) {
			mIndex = index;
		}

		/**
		 * Gets the column left.
		 *
		 * @return the column left
		 */
		public int getColumnLeft() {
			return mColumnLeft;
		}

		/**
		 * Gets the column width.
		 *
		 * @return the column width
		 */
		public int getColumnWidth() {
			return mColumnWidth;
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
		 * Gets the bottom.
		 *
		 * @return the bottom
		 */
		public int getBottom() {
			// find biggest value.
			int bottom = Integer.MIN_VALUE;
			int childCount = getChildCount();

			for (int index = 0; index < childCount; ++index) {
				View v = getChildAt(index);

				if (v.getLeft() != mColumnLeft && isFixedView(v) == false)
					continue;
				bottom = bottom < v.getBottom() ? v.getBottom() : bottom;
			}

			if (bottom == Integer.MIN_VALUE)
				return mSynchedBottom; // no child for this column..
			return bottom;
		}

		/**
		 * Offset top and bottom.
		 *
		 * @param offset the offset
		 */
		public void offsetTopAndBottom(int offset) {
			if (offset == 0)
				return;

			// find biggest value.
			int childCount = getChildCount();

			for (int index = 0; index < childCount; ++index) {
				View v = getChildAt(index);

				if (v.getLeft() != mColumnLeft && isFixedView(v) == false)
					continue;

				v.offsetTopAndBottom(offset);
			}
		}

		/**
		 * Gets the top.
		 *
		 * @return the top
		 */
		public int getTop() {
			// find smallest value.
			int top = Integer.MAX_VALUE;
			int childCount = getChildCount();
			for (int index = 0; index < childCount; ++index) {
				View v = getChildAt(index);
				if (v.getLeft() != mColumnLeft && isFixedView(v) == false)
					continue;
				top = top > v.getTop() ? v.getTop() : top;
			}

			if (top == Integer.MAX_VALUE)
				return mSynchedTop; // no child for this column. just return
									// saved sync top..
			return top;
		}

		/**
		 * Save.
		 */
		public void save() {
			mSynchedTop = 0;
			mSynchedBottom = getTop(); // getBottom();
		}

		/**
		 * Clear.
		 */
		public void clear() {
			mSynchedTop = 0;
			mSynchedBottom = 0;
		}
	}// end of inner class Column

	/**
	 * The Class FixedColumn.
	 */
	private class FixedColumn extends Column {

		/**
		 * Instantiates a new fixed column.
		 */
		public FixedColumn() {
			super(Integer.MAX_VALUE);
		}

		/**
		 * 描述：TODO
		 * @see com.ab.view.pullview.AbMultiColumnBaseListView.Column#getBottom()
		 * @author: zhaoqp
		 * @date：2013-9-4 下午4:06:34
		 * @version v1.0
		 */
		@Override
		public int getBottom() {
			return getScrollChildBottom();
		}

		/**
		 * 描述：TODO
		 * @see com.ab.view.pullview.AbMultiColumnBaseListView.Column#getTop()
		 * @author: zhaoqp
		 * @date：2013-9-4 下午4:06:34
		 * @version v1.0
		 */
		@Override
		public int getTop() {
			return getScrollChildTop();
		}

	}// end of class

}// end of class
