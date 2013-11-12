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

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListAdapter;


/**
 * A view that shows items in a vertically scrolling list. The items come from
 */
public class AbMultiColumnAbsListView extends AbMultiColumnBaseAbsListView {

    /**
     * Used to indicate a no preference for a position type.
     */
    static final int NO_POSITION = -1;

    /**
     * When arrow scrolling, ListView will never scroll more than this factor
     * times the height of the list.
     */
    private static final float MAX_SCROLL_FACTOR = 0.33f;

    /**
     * A class that represents a fixed view in a list, for example a header at
     * the top or a footer at the bottom.
     */
    public class FixedViewInfo {
        /** The view to add to the list */
        public View view;
        /**
         * The data backing the view. This is returned from
         */
        public Object data;
        /** <code>true</code> if the fixed view should be selectable in the list */
        public boolean isSelectable;
    }

    private ArrayList<FixedViewInfo> mHeaderViewInfos = new ArrayList<AbMultiColumnAbsListView.FixedViewInfo>();
    private ArrayList<FixedViewInfo> mFooterViewInfos = new ArrayList<AbMultiColumnAbsListView.FixedViewInfo>();

    Drawable mOverScrollHeader;
    Drawable mOverScrollFooter;

    private boolean mIsCacheColorOpaque;
    private boolean mDividerIsOpaque;
    private boolean mAreAllItemsSelectable = true;

    private boolean mItemsCanFocus = false;

    // used for temporary calculations.
    private final Rect mTempRect = new Rect();


	public AbMultiColumnAbsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		final Drawable osHeader = null;
        if (osHeader != null) {
            setOverscrollHeader(osHeader);
        }

        final Drawable osFooter = null;
        if (osFooter != null) {
            setOverscrollFooter(osFooter);
        }
	}

	public AbMultiColumnAbsListView(Context context) {
		super(context);
	}


    public int getMaxScrollAmount() {
        // return (int) (MAX_SCROLL_FACTOR * (mBottom - mTop));
        return (int) (MAX_SCROLL_FACTOR * (getBottom() - getTop()));
    }

   
    private void adjustViewsUpOrDown() {
        final int childCount = getChildCount();
        int delta;

        if (childCount > 0) {
            // View child;
            if (!mStackFromBottom) {
                // Uh-oh -- we came up short. Slide all views up to make them
                // align with the top
                final int firstTop = getScrollChildTop();
                // child = getChildAt(0);
                // delta = child.getTop() - mListPadding.top;
                delta = firstTop - mListPadding.top;
                if (delta < 0) {
                    // We only are looking to see if we are too low, not too
                    // high
                    delta = 0;
                }
            } else {
                // we are too high, slide all views down to align with bottom
                // child = getChildAt(childCount - 1);
                // delta = child.getBottom() - (getHeight() -
                // mListPadding.bottom);
                final int lastBottom = getScrollChildBottom();
                delta = lastBottom - (getHeight() - mListPadding.bottom);

                if (delta > 0) {
                    delta = 0;
                }
            }

            if (delta != 0) {
                // offsetChildrenTopAndBottom(-delta);
                tryOffsetChildrenTopAndBottom(-delta);
            }
        }
    }

    
    public void addHeaderView(View v, Object data, boolean isSelectable) {

        if (mAdapter != null) {
            throw new IllegalStateException("Cannot add header view to list -- setAdapter has already been called.");
        }

        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        mHeaderViewInfos.add(info);
    }

   
    public void addHeaderView(View v) {
        addHeaderView(v, null, true);
    }

    @Override
    public int getHeaderViewsCount() {
        return mHeaderViewInfos.size();
    }

   
    public boolean isFixedView(View v) {

        {
            // check header view.
            ArrayList<FixedViewInfo> where = mHeaderViewInfos;
            int len = where.size();
            for (int i = 0; i < len; ++i) {
                FixedViewInfo info = where.get(i);
                if (info.view == v) {
                    return true;
                }
            }
        }

        {
            // check footer view.
            ArrayList<FixedViewInfo> where = mFooterViewInfos;
            int len = where.size();
            for (int i = 0; i < len; ++i) {
                FixedViewInfo info = where.get(i);
                if (info.view == v) {
                    return true;
                }
            }
        }

        return false;
    }

    
    public boolean removeHeaderView(View v) {
        if (mHeaderViewInfos.size() > 0) {
            boolean result = false;
            if (((AbMultiColumnHeaderViewListAdapter) mAdapter).removeHeader(v)) {
                mDataSetObserver.onChanged();
                result = true;
            }
            removeFixedViewInfo(v, mHeaderViewInfos);
            return result;
        }
        return false;
    }

    private void removeFixedViewInfo(View v, ArrayList<FixedViewInfo> where) {
        int len = where.size();
        for (int i = 0; i < len; ++i) {
            FixedViewInfo info = where.get(i);
            if (info.view == v) {
                where.remove(i);
                break;
            }
        }
    }

   
    public void addFooterView(View v, Object data, boolean isSelectable) {
        FixedViewInfo info = new FixedViewInfo();
        info.view = v;
        info.data = data;
        info.isSelectable = isSelectable;
        mFooterViewInfos.add(info);

        // in the case of re-adding a footer view, or adding one later on,
        // we need to notify the observer
        if (mDataSetObserver != null) {
            mDataSetObserver.onChanged();
        }
    }

   
    public void addFooterView(View v) {
        addFooterView(v, null, true);
    }

    @Override
    public int getFooterViewsCount() {
        return mFooterViewInfos.size();
    }

   
    public boolean removeFooterView(View v) {
        if (mFooterViewInfos.size() > 0) {
            boolean result = false;
            if (((AbMultiColumnHeaderViewListAdapter) mAdapter).removeFooter(v)) {
                mDataSetObserver.onChanged();
                result = true;
            }
            removeFixedViewInfo(v, mFooterViewInfos);
            return result;
        }
        return false;
    }

    
    @Override
    public ListAdapter getAdapter() {
        return mAdapter;
    }

    
    @Override
    public void setAdapter(ListAdapter adapter) {
        if (null != mAdapter) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }

        resetList();
        mRecycler.clear();

        if (mHeaderViewInfos.size() > 0 || mFooterViewInfos.size() > 0) {
            mAdapter = new AbMultiColumnHeaderViewListAdapter(mHeaderViewInfos, mFooterViewInfos, adapter);
        } else {
            mAdapter = adapter;
        }

        mOldSelectedPosition = INVALID_POSITION;
        mOldSelectedRowId = INVALID_ROW_ID;
        if (mAdapter != null) {
            mAreAllItemsSelectable = mAdapter.areAllItemsEnabled();
            mOldItemCount = mItemCount;
            mItemCount = mAdapter.getCount();
            checkFocus();

            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);

            mRecycler.setViewTypeCount(mAdapter.getViewTypeCount());

        } else {
            mAreAllItemsSelectable = true;
            checkFocus();
            // Nothing selected
        }

        requestLayout();
    }

    @Override
    public int getFirstVisiblePosition() {
        return Math.max(0, mFirstPosition - getHeaderViewsCount());
    }

    @Override
    public int getLastVisiblePosition() {
        return Math.min(mFirstPosition + getChildCount() - 1, mAdapter.getCount() - 1);
    }

    
    @Override
    void resetList() {
        // The parent's resetList() will remove all views from the layout so we
        // need to
        // cleanup the state of our footers and headers
        clearRecycledState(mHeaderViewInfos);
        clearRecycledState(mFooterViewInfos);

        super.resetList();

        mLayoutMode = LAYOUT_NORMAL;
    }

    private void clearRecycledState(ArrayList<FixedViewInfo> infos) {
        if (infos != null) {
            final int count = infos.size();

            for (int i = 0; i < count; i++) {
                final View child = infos.get(i).view;
                final LayoutParams p = (LayoutParams) child.getLayoutParams();
                if (p != null) {
                    p.recycledHeaderFooter = false;
                }
            }
        }
    }

   
    private boolean showingTopFadingEdge() {
        // final int listTop = mScrollY + mListPadding.top;
        final int listTop = getScrollY() + mListPadding.top;
        return (mFirstPosition > 0) || (getChildAt(0).getTop() > listTop);
    }

    private boolean showingBottomFadingEdge() {
        final int childCount = getChildCount();
        final int bottomOfBottomChild = getChildAt(childCount - 1).getBottom();
        final int lastVisiblePosition = mFirstPosition + childCount - 1;

        // final int listBottom = mScrollY + getHeight() - mListPadding.bottom;
        final int listBottom = getScrollY() + getHeight() - mListPadding.bottom;

        return (lastVisiblePosition < mItemCount - 1) || (bottomOfBottomChild < listBottom);
    }

    @Override
    public boolean requestChildRectangleOnScreen(View child, Rect rect, boolean immediate) {

        int rectTopWithinChild = rect.top;

        // offset so rect is in coordinates of the this view
        rect.offset(child.getLeft(), child.getTop());
        rect.offset(-child.getScrollX(), -child.getScrollY());

        final int height = getHeight();
        int listUnfadedTop = getScrollY();
        int listUnfadedBottom = listUnfadedTop + height;
        final int fadingEdge = getVerticalFadingEdgeLength();

        if (showingTopFadingEdge()) {
            // leave room for top fading edge as long as rect isn't at very top
            if (rectTopWithinChild > fadingEdge) {
                listUnfadedTop += fadingEdge;
            }
        }

        int childCount = getChildCount();
        int bottomOfBottomChild = getChildAt(childCount - 1).getBottom();

        if (showingBottomFadingEdge()) {
            // leave room for bottom fading edge as long as rect isn't at very
            // bottom
            if (rect.bottom < (bottomOfBottomChild - fadingEdge)) {
                listUnfadedBottom -= fadingEdge;
            }
        }

        int scrollYDelta = 0;

        if (rect.bottom > listUnfadedBottom && rect.top > listUnfadedTop) {
            // need to MOVE DOWN to get it in view: move down just enough so
            // that the entire rectangle is in view (or at least the first
            // screen size chunk).

            if (rect.height() > height) {
                // just enough to get screen size chunk on
                scrollYDelta += (rect.top - listUnfadedTop);
            } else {
                // get entire rect at bottom of screen
                scrollYDelta += (rect.bottom - listUnfadedBottom);
            }

            // make sure we aren't scrolling beyond the end of our children
            int distanceToBottom = bottomOfBottomChild - listUnfadedBottom;
            scrollYDelta = Math.min(scrollYDelta, distanceToBottom);
        } else if (rect.top < listUnfadedTop && rect.bottom < listUnfadedBottom) {
            // need to MOVE UP to get it in view: move up just enough so that
            // entire rectangle is in view (or at least the first screen
            // size chunk of it).

            if (rect.height() > height) {
                // screen size chunk
                scrollYDelta -= (listUnfadedBottom - rect.bottom);
            } else {
                // entire rect at top
                scrollYDelta -= (listUnfadedTop - rect.top);
            }

            // make sure we aren't scrolling any further than the top our
            // children
            int top = getChildAt(0).getTop();
            int deltaToTop = top - listUnfadedTop;
            scrollYDelta = Math.max(scrollYDelta, deltaToTop);
        }

        final boolean scroll = scrollYDelta != 0;
        if (scroll) {
            scrollListItemsBy(-scrollYDelta);
            positionSelector(child);
            mSelectedTop = child.getTop();
            invalidate();
        }
        return scroll;
    }

   
    protected int getItemLeft(int pos) {
        return mListPadding.left;
    }

  
    protected int getItemTop(int pos) {
        // just return the last itme's bottom position..
        int count = getChildCount();
        return count > 0 ? getChildAt(count - 1).getBottom() : getListPaddingTop();
    }

    
    protected int getItemBottom(int pos) {
        int count = getChildCount();
        return count > 0 ? getChildAt(0).getTop() : getHeight() - getListPaddingBottom();
    }

    
    @Override
    protected void fillGap(boolean down) {
        final int count = getChildCount();
        if (down) {
            fillDown(mFirstPosition + count, getItemTop(mFirstPosition + count));
            onAdjustChildViews(down);
        } else {
            fillUp(mFirstPosition - 1, getItemBottom(mFirstPosition - 1));
            onAdjustChildViews(down);
        }
    }

    
    private View fillDown(int pos, int top) {
        int end = (getBottom() - getTop()) - mListPadding.bottom;
        int childTop = getFillChildBottom();

        while (childTop < end && pos < mItemCount) {
            // is this the selected item?
            makeAndAddView(pos, getItemTop(pos), true, false);
            pos++;
            childTop = getFillChildBottom();
        }

        return null;
    }

   
    private View fillUp(int pos, int bottom) {

        int end = mListPadding.top;
        int childBottom = getFillChildTop();
        while (childBottom > end && pos >= 0) {
            // is this the selected item?
            makeAndAddView(pos, getItemBottom(pos), false, false);
            // nextBottom = child.getTop() - mDividerHeight;
            pos--;
            childBottom = getItemBottom(pos);
        }

        mFirstPosition = pos + 1;

        return null;
    }

    
    private View fillFromTop(int nextTop) {
        mFirstPosition = Math.min(mFirstPosition, -1);
        mFirstPosition = Math.min(mFirstPosition, mItemCount - 1);
        if (mFirstPosition < 0) {
            mFirstPosition = 0;
        }
        return fillDown(mFirstPosition, nextTop);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Sets up mListPadding
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int childWidth = 0;
        int childHeight = 0;

        mItemCount = mAdapter == null ? 0 : mAdapter.getCount();
        if (mItemCount > 0 && (widthMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.UNSPECIFIED)) {
            final View child = obtainView(0, mIsScrap);

            measureScrapChild(child, 0, widthMeasureSpec);

            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            if (recycleOnMeasure() && mRecycler.shouldRecycleViewType(((LayoutParams) child.getLayoutParams()).viewType)) {
                mRecycler.addScrapView(child);
            }
        }

        if (widthMode == MeasureSpec.UNSPECIFIED) {

            widthSize = mListPadding.left + mListPadding.right + childWidth + getVerticalScrollbarWidth();
        }

        if (heightMode == MeasureSpec.UNSPECIFIED) {

            heightSize = mListPadding.top + mListPadding.bottom + childHeight + getVerticalFadingEdgeLength() * 2;

        }

        if (heightMode == MeasureSpec.AT_MOST) {

            // TODO: after first layout we should maybe start at the first
            // visible position, not 0
            heightSize = measureHeightOfChildren(widthMeasureSpec, 0, NO_POSITION, heightSize, -1);

        }

        setMeasuredDimension(widthSize, heightSize);
        mWidthMeasureSpec = widthMeasureSpec;
    }

    private void measureScrapChild(View child, int position, int widthMeasureSpec) {
        LayoutParams p = (LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
            child.setLayoutParams(p);
        }
        p.viewType = mAdapter.getItemViewType(position);
        p.forceAdd = true;

        int childWidthSpec = ViewGroup.getChildMeasureSpec(widthMeasureSpec, mListPadding.left + mListPadding.right, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    protected boolean recycleOnMeasure() {
        return true;
    }

    
    final int measureHeightOfChildren(int widthMeasureSpec, int startPosition, int endPosition, final int maxHeight,
            int disallowPartialChildPosition) {

        final ListAdapter adapter = mAdapter;
        if (adapter == null) {
            return mListPadding.top + mListPadding.bottom;
        }

        // Include the padding of the list
        int returnedHeight = mListPadding.top + mListPadding.bottom;
        // The previous height value that was less than maxHeight and contained
        // no partial children
        int prevHeightWithoutPartialChild = 0;
        int i;
        View child;

        // mItemCount - 1 since endPosition parameter is inclusive
        endPosition = (endPosition == NO_POSITION) ? adapter.getCount() - 1 : endPosition;
        final AbMultiColumnBaseAbsListView.RecycleBin recycleBin = mRecycler;
        final boolean recyle = recycleOnMeasure();
        final boolean[] isScrap = mIsScrap;

        for (i = startPosition; i <= endPosition; ++i) {
            child = obtainView(i, isScrap);

            measureScrapChild(child, i, widthMeasureSpec);

            // Recycle the view before we possibly return from the method
            if (recyle && recycleBin.shouldRecycleViewType(((LayoutParams) child.getLayoutParams()).viewType)) {
                recycleBin.addScrapView(child);
            }

            returnedHeight += child.getMeasuredHeight();

            if (returnedHeight >= maxHeight) {
                // We went over, figure out which height to return. If
                // returnedHeight > maxHeight,
                // then the i'th position did not fit completely.
                return (disallowPartialChildPosition >= 0) // Disallowing is
                                                           // enabled (> -1)
                        && (i > disallowPartialChildPosition) // We've past the
                                                              // min pos
                        && (prevHeightWithoutPartialChild > 0) // We have a prev
                                                               // height
                        && (returnedHeight != maxHeight) // i'th child did not
                                                         // fit completely
                ? prevHeightWithoutPartialChild : maxHeight;
            }

            if ((disallowPartialChildPosition >= 0) && (i >= disallowPartialChildPosition)) {
                prevHeightWithoutPartialChild = returnedHeight;
            }
        }

        // At this point, we went through the range of children, and they each
        // completely fit, so return the returnedHeight
        return returnedHeight;
    }

    @Override
    int findMotionRow(int y) {
        int childCount = getChildCount();
        if (childCount > 0) {
            if (!mStackFromBottom) {
                for (int i = 0; i < childCount; i++) {
                    View v = getChildAt(i);
                    if (y <= v.getBottom()) {
                        return mFirstPosition + i;
                    }
                }
            } else {
                for (int i = childCount - 1; i >= 0; i--) {
                    View v = getChildAt(i);
                    if (y >= v.getTop()) {
                        return mFirstPosition + i;
                    }
                }
            }
        }
        return INVALID_POSITION;
    }

   
    private View fillSpecific(int position, int top) {

        if (DEBUG)
            Log.d("PLA_ListView", "FillSpecific: " + position + ":" + top);

        View temp = makeAndAddView(position, top, true, false);

        // Possibly changed again in fillUp if we add rows above this one.

        mFirstPosition = position;
        if (!mStackFromBottom) {
            fillUp(position - 1, temp.getTop());
            // This will correct for the top of the first view not touching the
            // top of the list
            adjustViewsUpOrDown();
            fillDown(position + 1, temp.getBottom());
            int childCount = getChildCount();
            if (childCount > 0) {
                correctTooHigh(childCount);
            }
        } else {
            fillDown(position + 1, temp.getBottom());
            // This will correct for the bottom of the last view not touching
            // the bottom of the list
            adjustViewsUpOrDown();
            fillUp(position - 1, temp.getTop());
            int childCount = getChildCount();
            if (childCount > 0) {
                correctTooLow(childCount);
            }
        }

        return null;
    }

   
    private void correctTooHigh(int childCount) {
        // First see if the last item is visible. If it is not, it is OK for the
        // top of the list to be pushed up.
        int lastPosition = mFirstPosition + childCount - 1;
        if (lastPosition == mItemCount - 1 && childCount > 0) {

            // Get the last child ...
            // final View lastChild = getChildAt(childCount - 1);

            // ... and its bottom edge
            // final int lastBottom = lastChild.getBottom();
            final int lastBottom = getScrollChildBottom();

            // This is bottom of our drawable area
            // final int end = (mBottom - mTop) - mListPadding.bottom;
            final int end = (getBottom() - getTop()) - mListPadding.bottom;

            // This is how far the bottom edge of the last view is from the
            // bottom of the drawable area
            int bottomOffset = end - lastBottom;

            // View firstChild = getChildAt(0);
            // final int firstTop = firstChild.getTop();
            final int firstTop = getScrollChildTop();

            // Make sure we are 1) Too high, and 2) Either there are more rows
            // above the
            // first row or the first row is scrolled off the top of the
            // drawable area
            if (bottomOffset > 0 && (mFirstPosition > 0 || firstTop < mListPadding.top)) {
                if (mFirstPosition == 0) {
                    // Don't pull the top too far down
                    bottomOffset = Math.min(bottomOffset, mListPadding.top - firstTop);
                }
                // Move everything down
                // offsetChildrenTopAndBottom(bottomOffset);
                tryOffsetChildrenTopAndBottom(bottomOffset);
                if (mFirstPosition > 0) {
                    // Fill the gap that was opened above mFirstPosition with
                    // more rows, if
                    // possible
                    int newFirstTop = getScrollChildTop();
                    fillUp(mFirstPosition - 1, newFirstTop);
                    // Close up the remaining gap
                    adjustViewsUpOrDown();
                }

            }
        }
    }

    
    private void correctTooLow(int childCount) {
        // First see if the first item is visible. If it is not, it is OK for
        // the
        // bottom of the list to be pushed down.
        if (mFirstPosition == 0 && childCount > 0) {

            // Get the first child and its top edge
            final int firstTop = getScrollChildTop();

            // This is top of our drawable area
            final int start = mListPadding.top;

            // This is bottom of our drawable area
            final int end = (getBottom() - getTop()) - mListPadding.bottom;

            // This is how far the top edge of the first view is from the top of
            // the drawable area
            int topOffset = firstTop - start;
            final int lastBottom = getScrollChildBottom();

            int lastPosition = mFirstPosition + childCount - 1;

            // Make sure we are 1) Too low, and 2) Either there are more rows
            // below the
            // last row or the last row is scrolled off the bottom of the
            // drawable area
            if (topOffset > 0) {
                if (lastPosition < mItemCount - 1 || lastBottom > end) {
                    if (lastPosition == mItemCount - 1) {
                        // Don't pull the bottom too far up
                        topOffset = Math.min(topOffset, lastBottom - end);
                    }
                    // Move everything up
                    tryOffsetChildrenTopAndBottom(-topOffset);
                    if (lastPosition < mItemCount - 1) {
                        // Fill the gap that was opened below the last position
                        // with more rows, if
                        // possible
                        fillDown(lastPosition + 1, getFillChildTop());
                        // Close up the remaining gap
                        adjustViewsUpOrDown();
                    }
                } else if (lastPosition == mItemCount - 1) {
                    adjustViewsUpOrDown();
                }
            }
        }
    }

    @Override
    protected void layoutChildren() {
        final boolean blockLayoutRequests = mBlockLayoutRequests;
        if (!blockLayoutRequests) {
            mBlockLayoutRequests = true;
        } else {
            return;
        }

        try {
            super.layoutChildren();
            invalidate();
            if (mAdapter == null) {
                resetList();
                invokeOnItemScrollListener();
                return;
            }

            int childrenTop = mListPadding.top;
            // int childrenBottom = mBottom - mTop - mListPadding.bottom;
            int childrenBottom = getBottom() - getTop() - mListPadding.bottom;

            int childCount = getChildCount();
            int index = 0;

            View oldFirst = null;
            View focusLayoutRestoreView = null;

            // Remember stuff we will need down below
            switch (mLayoutMode) {
            case LAYOUT_FORCE_TOP:
            case LAYOUT_FORCE_BOTTOM:
            case LAYOUT_SPECIFIC:
            case LAYOUT_SYNC:
                break;
            default:
                // Remember the previous first child
                oldFirst = getChildAt(0);
            }

            boolean dataChanged = mDataChanged;
            if (dataChanged) {
                handleDataChanged();
            }

            // Handle the empty set by removing all views that are visible
            // and calling it a day
            if (mItemCount == 0) {
                resetList();
                invokeOnItemScrollListener();
                return;
            } else if (mItemCount != mAdapter.getCount()) {
                throw new IllegalStateException("The content of the adapter has changed but "
                        + "ListView did not receive a notification. Make sure the content of "
                        + "your adapter is not modified from a background thread, but only " + "from the UI thread. [in ListView("
                        + getId() + ", " + getClass() + ") with Adapter(" + mAdapter.getClass() + ")]");
            }

            // Pull all children into the RecycleBin.
            // These views will be reused if possible
            final int firstPosition = mFirstPosition;
            final RecycleBin recycleBin = mRecycler;

            // reset the focus restoration

            // Don't put header or footer views into the Recycler. Those are
            // already cached in mHeaderViews;
            if (dataChanged) {
                for (int i = 0; i < childCount; i++) {
                    recycleBin.addScrapView(getChildAt(i));
                    if (ViewDebug.TRACE_RECYCLER) {
                        ViewDebug.trace(getChildAt(i), ViewDebug.RecyclerTraceType.MOVE_TO_SCRAP_HEAP, index, i);
                    }
                }
            } else {
                recycleBin.fillActiveViews(childCount, firstPosition);
            }

            // take focus back to us temporarily to avoid the eventual
            // call to clear focus when removing the focused child below
            // from messing things up when ViewRoot assigns focus back
            // to someone else
            final View focusedChild = getFocusedChild();
            if (focusedChild != null) {
                // TODO: in some cases focusedChild.getParent() == null
                // we can remember the focused view to restore after relayout if
                // the
                // data hasn't changed, or if the focused position is a header
                // or footer
                if (!dataChanged || isDirectChildHeaderOrFooter(focusedChild)) {
                    // remember the specific view that had focus
                    focusLayoutRestoreView = findFocus();
                    if (focusLayoutRestoreView != null) {
                        // tell it we are going to mess with it
                        focusLayoutRestoreView.onStartTemporaryDetach();
                    }
                }
                requestFocus();
            }

            switch (mLayoutMode) {
            case LAYOUT_SYNC:
                onLayoutSync(mSyncPosition);
                // Clear out old views
                detachAllViewsFromParent();
                fillSpecific(mSyncPosition, mSpecificTop);
                onLayoutSyncFinished(mSyncPosition);
                break;
            case LAYOUT_FORCE_BOTTOM:
                detachAllViewsFromParent();
                fillUp(mItemCount - 1, childrenBottom);
                adjustViewsUpOrDown();
                break;
            case LAYOUT_FORCE_TOP:
                detachAllViewsFromParent();
                mFirstPosition = 0;
                fillFromTop(childrenTop);
                adjustViewsUpOrDown();
                break;
            default:
                if (childCount == 0) {
                    detachAllViewsFromParent();
                    if (!mStackFromBottom) {
                        fillFromTop(childrenTop);
                    } else {
                        fillUp(mItemCount - 1, childrenBottom);
                    }
                } else {
                    if (mFirstPosition < mItemCount) {
                        onLayoutSync(mFirstPosition);
                        detachAllViewsFromParent();
                        fillSpecific(mFirstPosition, oldFirst == null ? childrenTop : oldFirst.getTop());
                        onLayoutSyncFinished(mFirstPosition);
                    } else {
                        onLayoutSync(0);
                        detachAllViewsFromParent();
                        fillSpecific(0, childrenTop);
                        onLayoutSyncFinished(0);
                    }
                }
                break;
            }

            // Flush any cached views that did not get reused above
            recycleBin.scrapActiveViews();

            if (mTouchMode > TOUCH_MODE_DOWN && mTouchMode < TOUCH_MODE_SCROLL) {
                View child = getChildAt(mMotionPosition - mFirstPosition);
                if (child != null)
                    positionSelector(child);
            } else {
                mSelectedTop = 0;
                mSelectorRect.setEmpty();
            }

            // even if there is not selected position, we may need to restore
            // focus (i.e. something focusable in touch mode)
            if (hasFocus() && focusLayoutRestoreView != null) {
                focusLayoutRestoreView.requestFocus();
            }

            // tell focus view we are done mucking with it, if it is still in
            // our view hierarchy.
            if (focusLayoutRestoreView != null && focusLayoutRestoreView.getWindowToken() != null) {
                focusLayoutRestoreView.onFinishTemporaryDetach();
            }

            mLayoutMode = LAYOUT_NORMAL;
            mDataChanged = false;
            mNeedSync = false;

            invokeOnItemScrollListener();
        } finally {
            if (!blockLayoutRequests) {
                mBlockLayoutRequests = false;
            }
        }
    }

  
    private boolean isDirectChildHeaderOrFooter(View child) {

        final ArrayList<FixedViewInfo> headers = mHeaderViewInfos;
        final int numHeaders = headers.size();
        for (int i = 0; i < numHeaders; i++) {
            if (child == headers.get(i).view) {
                return true;
            }
        }
        final ArrayList<FixedViewInfo> footers = mFooterViewInfos;
        final int numFooters = footers.size();
        for (int i = 0; i < numFooters; i++) {
            if (child == footers.get(i).view) {
                return true;
            }
        }
        return false;
    }

    
    @SuppressWarnings("deprecation")
    private View makeAndAddView(int position, int childrenBottomOrTop, boolean flow, boolean selected) {
        View child;

        int childrenLeft;
        if (!mDataChanged) {
            // Try to use an exsiting view for this position
            child = mRecycler.getActiveView(position);
            if (child != null) {

                if (ViewDebug.TRACE_RECYCLER) {
                    ViewDebug.trace(child, ViewDebug.RecyclerTraceType.RECYCLE_FROM_ACTIVE_HEAP, position, getChildCount());
                }

                // Found it -- we're using an existing child
                // This just needs to be positioned
                childrenLeft = getItemLeft(position);
                setupChild(child, position, childrenBottomOrTop, flow, childrenLeft, selected, true);
                return child;
            }
        }

        // Notify new item is added to view.

        onItemAddedToList(position, flow);
        childrenLeft = getItemLeft(position);

        // Make a new view for this position, or convert an unused view if
        // possible
        child = obtainView(position, mIsScrap);

        // This needs to be positioned and measured
        setupChild(child, position, childrenBottomOrTop, flow, childrenLeft, selected, mIsScrap[0]);

        return child;
    }

    
    protected void onItemAddedToList(int position, boolean flow) {
    }

  
    private void setupChild(View child, int position, int y, boolean flowDown, int childrenLeft, boolean selected, boolean recycled) {

        final boolean isSelected = selected && shouldShowSelector();
        final boolean updateChildSelected = isSelected != child.isSelected();
        final int mode = mTouchMode;
        final boolean isPressed = mode > TOUCH_MODE_DOWN && mode < TOUCH_MODE_SCROLL && mMotionPosition == position;
        final boolean updateChildPressed = isPressed != child.isPressed();
        final boolean needToMeasure = !recycled || updateChildSelected || child.isLayoutRequested();

        // Respect layout params that are already in the view. Otherwise make
        // some up...
        // noinspection unchecked
        AbMultiColumnBaseAbsListView.LayoutParams p = (AbMultiColumnBaseAbsListView.LayoutParams) child.getLayoutParams();
        if (p == null) {
            p = new AbMultiColumnBaseAbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        }
        p.viewType = mAdapter.getItemViewType(position);

        if ((recycled && !p.forceAdd) || (p.recycledHeaderFooter && p.viewType == AbMultiColumnAdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER)) {
            attachViewToParent(child, flowDown ? -1 : 0, p);
        } else {
            p.forceAdd = false;
            if (p.viewType == AbMultiColumnAdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER) {
                p.recycledHeaderFooter = true;
            }
            addViewInLayout(child, flowDown ? -1 : 0, p, true);
        }

        if (updateChildSelected) {
            child.setSelected(isSelected);
        }

        if (updateChildPressed) {
            child.setPressed(isPressed);
        }

        if (needToMeasure) {
            int childWidthSpec = ViewGroup.getChildMeasureSpec(mWidthMeasureSpec, mListPadding.left + mListPadding.right, p.width);
            int lpHeight = p.height;
            int childHeightSpec;
            if (lpHeight > 0) {
                childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
            } else {
                childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
            }

            onMeasureChild(child, position, childWidthSpec, childHeightSpec);
        } else {
            cleanupLayoutState(child);
        }

        final int w = child.getMeasuredWidth();
        final int h = child.getMeasuredHeight();
        final int childTop = flowDown ? y : y - h;

        if (needToMeasure) {
            final int childRight = childrenLeft + w;
            final int childBottom = childTop + h;
            onLayoutChild(child, position, childrenLeft, childTop, childRight, childBottom);
        } else {
            final int offsetLeft = childrenLeft - child.getLeft();
            final int offsetTop = childTop - child.getTop();
            onOffsetChild(child, position, offsetLeft, offsetTop);
        }

        if (mCachingStarted && !child.isDrawingCacheEnabled()) {
            child.setDrawingCacheEnabled(true);
        }
    }

    protected void onOffsetChild(View child, int position, int offsetLeft, int offsetTop) {
        child.offsetLeftAndRight(offsetLeft);
        child.offsetTopAndBottom(offsetTop);
    }

    protected void onLayoutChild(View child, int position, int l, int t, int r, int b) {
        child.layout(l, t, r, b);
    }

    
    protected void onMeasureChild(View child, int position, int widthMeasureSpec, int heightMeasureSpec) {
        child.measure(widthMeasureSpec, heightMeasureSpec);
    }

    
    protected void onAdjustChildViews(boolean down) {
        if (down)
            correctTooHigh(getChildCount());
        else
            correctTooLow(getChildCount());
    }

    @Override
    protected boolean canAnimate() {
        return super.canAnimate() && mItemCount > 0;
    }

    
    @Override
    public void setSelection(int position) {
    }

   
    @Override
    int lookForSelectablePosition(int position, boolean lookDown) {
        final ListAdapter adapter = mAdapter;
        if (adapter == null || isInTouchMode()) {
            return INVALID_POSITION;
        }

        final int count = adapter.getCount();
        if (!mAreAllItemsSelectable) {
            if (lookDown) {
                position = Math.max(0, position);
                while (position < count && !adapter.isEnabled(position)) {
                    position++;
                }
            } else {
                position = Math.min(position, count - 1);
                while (position >= 0 && !adapter.isEnabled(position)) {
                    position--;
                }
            }

            if (position < 0 || position >= count) {
                return INVALID_POSITION;
            }
            return position;
        } else {
            if (position < 0 || position >= count) {
                return INVALID_POSITION;
            }
            return position;
        }
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
        boolean populated = super.dispatchPopulateAccessibilityEvent(event);

        // If the item count is less than 15 then subtract disabled items from
        // the count and
        // position. Otherwise ignore disabled items.
        if (!populated) {
            int itemCount = 0;
            int currentItemIndex = getSelectedItemPosition();

            ListAdapter adapter = getAdapter();
            if (adapter != null) {
                final int count = adapter.getCount();
                if (count < 15) {
                    for (int i = 0; i < count; i++) {
                        if (adapter.isEnabled(i)) {
                            itemCount++;
                        } else if (i <= currentItemIndex) {
                            currentItemIndex--;
                        }
                    }
                } else {
                    itemCount = count;
                }
            }

            event.setItemCount(itemCount);
            event.setCurrentItemIndex(currentItemIndex);
        }

        return populated;
    }

    
    public boolean fullScroll(int direction) {
        boolean moved = false;
        if (direction == FOCUS_UP) {
            int position = lookForSelectablePosition(0, true);
            if (position >= 0) {
                mLayoutMode = LAYOUT_FORCE_TOP;
                invokeOnItemScrollListener();
                moved = true;
            }
        } else if (direction == FOCUS_DOWN) {
            int position = lookForSelectablePosition(mItemCount - 1, true);
            if (position >= 0) {
                mLayoutMode = LAYOUT_FORCE_BOTTOM;
                invokeOnItemScrollListener();
            }
            moved = true;
        }

        if (moved && !awakenScrollBars()) {
            awakenScrollBars();
            invalidate();
        }

        return moved;
    }

   
    private void scrollListItemsBy(int amount) {
        // offsetChildrenTopAndBottom(amount);
        tryOffsetChildrenTopAndBottom(amount);

        final int listBottom = getHeight() - mListPadding.bottom;
        final int listTop = mListPadding.top;
        final AbMultiColumnBaseAbsListView.RecycleBin recycleBin = mRecycler;

        if (amount < 0) {
            // shifted items up

            // may need to pan views into the bottom space
            View last = getLastChild();
            int numChildren = getChildCount();
            // View last = getChildAt(numChildren - 1);

            while (last.getBottom() < listBottom) {
                final int lastVisiblePosition = mFirstPosition + numChildren - 1;
                if (lastVisiblePosition < mItemCount - 1) {
                    addViewBelow(last, lastVisiblePosition);
                    last = getLastChild();
                    numChildren++;
                } else {
                    break;
                }
            }

            // may have brought in the last child of the list that is skinnier
            // than the fading edge, thereby leaving space at the end. need
            // to shift back
            if (last.getBottom() < listBottom) {
                // offsetChildrenTopAndBottom(listBottom - last.getBottom());
                tryOffsetChildrenTopAndBottom(listBottom - last.getBottom());
            }

            // top views may be panned off screen
            View first = getChildAt(0);
            while (first.getBottom() < listTop) {
                AbMultiColumnBaseAbsListView.LayoutParams layoutParams = (LayoutParams) first.getLayoutParams();
                if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    detachViewFromParent(first);
                    recycleBin.addScrapView(first);
                } else {
                    removeViewInLayout(first);
                }
                first = getChildAt(0);
                mFirstPosition++;
            }
        } else {
            // shifted items down
            View first = getChildAt(0);

            // may need to pan views into top
            while ((first.getTop() > listTop) && (mFirstPosition > 0)) {
                first = addViewAbove(first, mFirstPosition);
                mFirstPosition--;
            }

            // may have brought the very first child of the list in too far and
            // need to shift it back
            if (first.getTop() > listTop) {
                // offsetChildrenTopAndBottom(listTop - first.getTop());
                tryOffsetChildrenTopAndBottom(listTop - first.getTop());
            }

            int lastIndex = getChildCount() - 1;
            View last = getChildAt(lastIndex);

            // bottom view may be panned off screen
            while (last.getTop() > listBottom) {
                AbMultiColumnBaseAbsListView.LayoutParams layoutParams = (LayoutParams) last.getLayoutParams();
                if (recycleBin.shouldRecycleViewType(layoutParams.viewType)) {
                    detachViewFromParent(last);
                    recycleBin.addScrapView(last);
                } else {
                    removeViewInLayout(last);
                }
                last = getChildAt(--lastIndex);
            }
        }
    }

    protected View getLastChild() {
        int numChildren = getChildCount();
        return getChildAt(numChildren - 1);
    }

    private View addViewAbove(View theView, int position) {
        int abovePosition = position - 1;
        View view = obtainView(abovePosition, mIsScrap);
        int edgeOfNewChild = theView.getTop();
        setupChild(view, abovePosition, edgeOfNewChild, false, mListPadding.left, false, mIsScrap[0]);
        return view;
    }

    private View addViewBelow(View theView, int position) {
        int belowPosition = position + 1;
        View view = obtainView(belowPosition, mIsScrap);
        int edgeOfNewChild = theView.getBottom();
        setupChild(view, belowPosition, edgeOfNewChild, true, mListPadding.left, false, mIsScrap[0]);
        return view;
    }

  
    public void setItemsCanFocus(boolean itemsCanFocus) {
        mItemsCanFocus = itemsCanFocus;
        if (!itemsCanFocus) {
            setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        }
    }

   
    public boolean getItemsCanFocus() {
        return mItemsCanFocus;
    }

    
    @Override
    public boolean isOpaque() {
        // return (mCachingStarted && mIsCacheColorOpaque && mDividerIsOpaque &&
        // hasOpaqueScrollbars()) || super.isOpaque();
        // we can ignore scrollbar...
        return (mCachingStarted && mIsCacheColorOpaque && mDividerIsOpaque) || super.isOpaque();
    }

    @Override
    public void setCacheColorHint(int color) {
        final boolean opaque = (color >>> 24) == 0xFF;
        mIsCacheColorOpaque = opaque;
        super.setCacheColorHint(color);
    }

    void drawOverscrollHeader(Canvas canvas, Drawable drawable, Rect bounds) {
        final int height = drawable.getMinimumHeight();

        canvas.save();
        canvas.clipRect(bounds);

        final int span = bounds.bottom - bounds.top;
        if (span < height) {
            bounds.top = bounds.bottom - height;
        }

        drawable.setBounds(bounds);
        drawable.draw(canvas);

        canvas.restore();
    }

    void drawOverscrollFooter(Canvas canvas, Drawable drawable, Rect bounds) {
        final int height = drawable.getMinimumHeight();

        canvas.save();
        canvas.clipRect(bounds);

        final int span = bounds.bottom - bounds.top;
        if (span < height) {
            bounds.bottom = bounds.top + height;
        }

        drawable.setBounds(bounds);
        drawable.draw(canvas);

        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // Draw the dividers
        final Drawable overscrollHeader = mOverScrollHeader;
        final Drawable overscrollFooter = mOverScrollFooter;
        final boolean drawOverscrollHeader = overscrollHeader != null;
        final boolean drawOverscrollFooter = overscrollFooter != null;

        if (drawOverscrollHeader || drawOverscrollFooter) {
            // Only modify the top and bottom in the loop, we set the left and
            // right here
            final Rect bounds = mTempRect;
            // bounds.left = mPaddingLeft;
            // bounds.right = mRight - mLeft - mPaddingRight;
            bounds.left = getPaddingLeft();
            bounds.right = getRight() - getLeft() - getPaddingRight();

            final int count = getChildCount();
            final int headerCount = mHeaderViewInfos.size();
            final int itemCount = mItemCount;
            final int footerLimit = itemCount - mFooterViewInfos.size() - 1;
            final int first = mFirstPosition;
            final boolean areAllItemsSelectable = mAreAllItemsSelectable;
            final ListAdapter adapter = mAdapter;
            // If the list is opaque *and* the background is not, we want to
            // fill a rect where the dividers would be for non-selectable items
            // If the list is opaque and the background is also opaque, we don't
            // need to draw anything since the background will do it for us

            // final int listBottom = mBottom - mTop - mListPadding.bottom +
            // mScrollY;
            final int listBottom = getBottom() - getTop() - mListPadding.bottom + getScrollY();
            if (!mStackFromBottom) {
                int bottom = 0;

                // Draw top divider or header for overscroll
                // final int scrollY = mScrollY;
                final int scrollY = getScrollY();
                if (count > 0 && scrollY < 0) {
                    if (drawOverscrollHeader) {
                        bounds.bottom = 0;
                        bounds.top = scrollY;
                        drawOverscrollHeader(canvas, overscrollHeader, bounds);
                    }
                }

                for (int i = 0; i < count; i++) {
                    if ((first + i >= headerCount) && (first + i < footerLimit)) {
                        View child = getChildAt(i);
                        bottom = child.getBottom();
                        // Don't draw dividers next to items that are not
                        // enabled
                        if ((bottom < listBottom && !(drawOverscrollFooter && i == count - 1))) {
                            if ((areAllItemsSelectable || (adapter.isEnabled(first + i) && (i == count - 1 || adapter.isEnabled(first + i
                                    + 1))))) {
                                bounds.top = bottom;
                                bounds.bottom = bottom;
                            }
                        }
                    }
                }

                // final int overFooterBottom = mBottom + mScrollY;
                final int overFooterBottom = getBottom() + getScrollY();
                if (drawOverscrollFooter && first + count == itemCount && overFooterBottom > bottom) {
                    bounds.top = bottom;
                    bounds.bottom = overFooterBottom;
                    drawOverscrollFooter(canvas, overscrollFooter, bounds);
                }
            } else {
                int top;
                int listTop = mListPadding.top;

                // final int scrollY = mScrollY;
                final int scrollY = getScrollY();

                if (count > 0 && drawOverscrollHeader) {
                    bounds.top = scrollY;
                    bounds.bottom = getChildAt(0).getTop();
                    drawOverscrollHeader(canvas, overscrollHeader, bounds);
                }

                final int start = drawOverscrollHeader ? 1 : 0;
                for (int i = start; i < count; i++) {
                    if ((first + i >= headerCount) && (first + i < footerLimit)) {
                        View child = getChildAt(i);
                        top = child.getTop();
                        // Don't draw dividers next to items that are not
                        // enabled
                        if (top > listTop) {
                            if ((areAllItemsSelectable || (adapter.isEnabled(first + i) && (i == count - 1 || adapter.isEnabled(first + i
                                    + 1))))) {
                                bounds.top = top;
                                bounds.bottom = top;

                            }
                        }
                    }
                }

                if (count > 0 && scrollY > 0) {
                    if (drawOverscrollFooter) {
                        // final int absListBottom = mBottom;
                        final int absListBottom = getBottom();
                        bounds.top = absListBottom;
                        bounds.bottom = absListBottom + scrollY;
                        drawOverscrollFooter(canvas, overscrollFooter, bounds);
                    }
                }
            }
        }

        // Draw the indicators (these should be drawn above the dividers) and
        // children
        super.dispatchDraw(canvas);
    }

   
    public void setOverscrollHeader(Drawable header) {
        mOverScrollHeader = header;
        // if (mScrollY < 0) {
        // invalidate();
        // }

        if (getScrollY() < 0) {
            invalidate();
        }
    }

    /**
     * @return The drawable that will be drawn above all other list content
     */
    public Drawable getOverscrollHeader() {
        return mOverScrollHeader;
    }

    
    public void setOverscrollFooter(Drawable footer) {
        mOverScrollFooter = footer;
        invalidate();
    }

    
    public Drawable getOverscrollFooter() {
        return mOverScrollFooter;
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);

        if (DEBUG)
            Log.v("PLA_ListView", "onFocusChanged");

        int closetChildIndex = -1;
        if (gainFocus && previouslyFocusedRect != null) {
            // previouslyFocusedRect.offset(mScrollX, mScrollY);
            previouslyFocusedRect.offset(getScrollX(), getScrollY());

            final ListAdapter adapter = mAdapter;
            // Don't cache the result of getChildCount or mFirstPosition here,
            // it could change in layoutChildren.
            if (adapter.getCount() < getChildCount() + mFirstPosition) {
                mLayoutMode = LAYOUT_NORMAL;
                layoutChildren();
            }

            // figure out which item should be selected based on previously
            // focused rect
            Rect otherRect = mTempRect;
            int minDistance = Integer.MAX_VALUE;
            final int childCount = getChildCount();
            final int firstPosition = mFirstPosition;

            for (int i = 0; i < childCount; i++) {
                // only consider selectable views
                if (!adapter.isEnabled(firstPosition + i)) {
                    continue;
                }

                View other = getChildAt(i);
                other.getDrawingRect(otherRect);
                offsetDescendantRectToMyCoords(other, otherRect);
                int distance = getDistance(previouslyFocusedRect, otherRect, direction);

                if (distance < minDistance) {
                    minDistance = distance;
                    closetChildIndex = i;
                }
            }
        }

        if (closetChildIndex >= 0) {
            setSelection(closetChildIndex + mFirstPosition);
        } else {
            requestLayout();
        }
    }

   
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int count = getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; ++i) {
                addHeaderView(getChildAt(i));
            }
            removeAllViews();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mItemsCanFocus && ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            // Don't handle edge touches immediately -- they may actually belong
            // to one of our
            // descendants.
            return false;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public boolean performItemClick(View view, int position, long id) {
        boolean handled = false;

        handled |= super.performItemClick(view, position, id);

        return handled;
    }

    
    public void setItemChecked(int position, boolean value) {
    }

    
    public boolean isItemChecked(int position) {
        return false;
    }

    
    public int getCheckedItemPosition() {
        return INVALID_POSITION;
    }

   
    public SparseBooleanArray getCheckedItemPositions() {
        return null;
    }

   
    @Deprecated
    public long[] getCheckItemIds() {
        // Use new behavior that correctly handles stable ID mapping.
        if (mAdapter != null && mAdapter.hasStableIds()) {
            return getCheckedItemIds();
        }

        return new long[0];
    }

   
    public long[] getCheckedItemIds() {
        return new long[0];
    }

   
    public void clearChoices() {
    }

}// end of class
