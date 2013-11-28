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
package com.ab.view.carousel;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsSpinner;
import android.widget.SpinnerAdapter;

// TODO: Auto-generated Javadoc
/**
 * The Class CarouselSpinner.
 */
public abstract class CarouselSpinner extends CarouselAdapter<SpinnerAdapter> {

    /** The m adapter. */
    SpinnerAdapter mAdapter;

    /** The m height measure spec. */
    int mHeightMeasureSpec;
    
    /** The m width measure spec. */
    int mWidthMeasureSpec;
    
    /** The m block layout requests. */
    boolean mBlockLayoutRequests;

    /** The m selection left padding. */
    int mSelectionLeftPadding = 0;
    
    /** The m selection top padding. */
    int mSelectionTopPadding = 0;
    
    /** The m selection right padding. */
    int mSelectionRightPadding = 0;
    
    /** The m selection bottom padding. */
    int mSelectionBottomPadding = 0;
    
    /** The m spinner padding. */
    final Rect mSpinnerPadding = new Rect();

    /** The m recycler. */
    final RecycleBin mRecycler = new RecycleBin();
    
    /** The m data set observer. */
    private DataSetObserver mDataSetObserver;
		
    /**
     * Instantiates a new carousel spinner.
     *
     * @param context the context
     */
    public CarouselSpinner(Context context) {
        super(context);
        initCarouselSpinner();
    }

    /**
     * Instantiates a new carousel spinner.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public CarouselSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new carousel spinner.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public CarouselSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initCarouselSpinner();
    }
    
    /**
     * Common code for different constructor flavors.
     */
    private void initCarouselSpinner() {
        setFocusable(true);
        setWillNotDraw(false);
    }    
           
    
	/**
	 * 描述：TODO
	 * @see com.ab.view.carousel.CarouselAdapter#getAdapter()
	 * @author: zhaoqp
	 * @date：2013-11-28 上午11:14:34
	 * @version v1.0
	 */
	public SpinnerAdapter getAdapter() {
        return mAdapter;
    }

	/**
	 * 描述：TODO
	 * @see com.ab.view.carousel.CarouselAdapter#setAdapter(android.widget.Adapter)
	 * @author: zhaoqp
	 * @date：2013-11-28 上午11:14:34
	 * @version v1.0
	 */
	public void setAdapter(SpinnerAdapter adapter) {
        if (null != mAdapter) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
            resetList();
        }
        
        mAdapter = adapter;
        
        mOldSelectedPosition = INVALID_POSITION;
        mOldSelectedRowId = INVALID_ROW_ID;
        
        if (mAdapter != null) {
            mOldItemCount = mItemCount;
            mItemCount = mAdapter.getCount();
            checkFocus();

            mDataSetObserver = new AdapterDataSetObserver();
            mAdapter.registerDataSetObserver(mDataSetObserver);

            int position = mItemCount > 0 ? 0 : INVALID_POSITION;

            setSelectedPositionInt(position);
            setNextSelectedPositionInt(position);
            
            if (mItemCount == 0) {
                // Nothing selected
                checkSelectionChanged();
            }
            
        } else {
            checkFocus();            
            resetList();
            // Nothing selected
            checkSelectionChanged();
        }

        requestLayout();
		
	}

    /**
     * 描述：TODO
     * @see com.ab.view.carousel.CarouselAdapter#getSelectedView()
     * @author: zhaoqp
     * @date：2013-11-28 上午11:14:34
     * @version v1.0
     */
    public View getSelectedView() {
        if (mItemCount > 0 && mSelectedPosition >= 0) {
            return getChildAt(mSelectedPosition - mFirstPosition);
        } else {
            return null;
        }
    }
	
    /**
     * Jump directly to a specific item in the adapter data.
     *
     * @param position the position
     * @param animate the animate
     */
    public void setSelection(int position, boolean animate) {
        // Animate only if requested position is already on screen somewhere
        boolean shouldAnimate = animate && mFirstPosition <= position &&
                position <= mFirstPosition + getChildCount() - 1;
        setSelectionInt(position, shouldAnimate);
    }
    

    /**
     * Makes the item at the supplied position selected.
     * 
     * @param position Position to select
     * @param animate Should the transition be animated
     * 
     */
    void setSelectionInt(int position, boolean animate) {
        if (position != mOldSelectedPosition) {
            mBlockLayoutRequests = true;
            int delta  = position - mSelectedPosition;
            setNextSelectedPositionInt(position);
            layout(delta, animate);
            mBlockLayoutRequests = false;
        }
    }
    
    /**
     * Layout.
     *
     * @param delta the delta
     * @param animate the animate
     */
    abstract void layout(int delta, boolean animate);    

	/**
	 * 描述：TODO
	 * @see com.ab.view.carousel.CarouselAdapter#setSelection(int)
	 * @author: zhaoqp
	 * @date：2013-11-28 上午11:14:34
	 * @version v1.0
	 */
	public void setSelection(int position) {
        setSelectionInt(position, false);
	}

    /**
     * Clear out all children from the list.
     */
    void resetList() {
        mDataChanged = false;
        mNeedSync = false;
        
        removeAllViewsInLayout();
        mOldSelectedPosition = INVALID_POSITION;
        mOldSelectedRowId = INVALID_ROW_ID;
        
        setSelectedPositionInt(INVALID_POSITION);
        setNextSelectedPositionInt(INVALID_POSITION);
        invalidate();
    }
	
    /**
     * On measure.
     *
     * @param widthMeasureSpec the width measure spec
     * @param heightMeasureSpec the height measure spec
     * @see android.view.View#measure(int, int)
     * 
     * Figure out the dimensions of this Spinner. The width comes from
     * the widthMeasureSpec as Spinnners can't have their width set to
     * UNSPECIFIED. The height is based on the height of the selected item
     * plus padding.
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize;
        int heightSize;

        mSpinnerPadding.left = getPaddingLeft() > mSelectionLeftPadding ? getPaddingLeft()
                : mSelectionLeftPadding;
        mSpinnerPadding.top = getPaddingTop() > mSelectionTopPadding ? getPaddingTop()
                : mSelectionTopPadding;
        mSpinnerPadding.right = getPaddingRight() > mSelectionRightPadding ? getPaddingRight()
                : mSelectionRightPadding;
        mSpinnerPadding.bottom = getPaddingBottom() > mSelectionBottomPadding ? getPaddingBottom()
                : mSelectionBottomPadding;

        if (mDataChanged) {
            handleDataChanged();
        }
        
        int preferredHeight = 0;
        int preferredWidth = 0;
        boolean needsMeasuring = true;
        
        int selectedPosition = getSelectedItemPosition();
        if (selectedPosition >= 0 && mAdapter != null && selectedPosition < mAdapter.getCount()) {
            // Try looking in the recycler. (Maybe we were measured once already)
            View view = mRecycler.get(selectedPosition);
            if (view == null) {
                // Make a new one
                view = mAdapter.getView(selectedPosition, null, this);
            }

            if (view != null) {
                // Put in recycler for re-measuring and/or layout
                mRecycler.put(selectedPosition, view);
            }

            if (view != null) {
                if (view.getLayoutParams() == null) {
                    mBlockLayoutRequests = true;
                    view.setLayoutParams(generateDefaultLayoutParams());
                    mBlockLayoutRequests = false;
                }
                measureChild(view, widthMeasureSpec, heightMeasureSpec);
                
                preferredHeight = getChildHeight(view) + mSpinnerPadding.top + mSpinnerPadding.bottom;
                preferredWidth = getChildWidth(view) + mSpinnerPadding.left + mSpinnerPadding.right;
                
                needsMeasuring = false;
            }
        }
        
        if (needsMeasuring) {
            // No views -- just use padding
            preferredHeight = mSpinnerPadding.top + mSpinnerPadding.bottom;
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                preferredWidth = mSpinnerPadding.left + mSpinnerPadding.right;
            }
        }

        preferredHeight = Math.max(preferredHeight, getSuggestedMinimumHeight());
        preferredWidth = Math.max(preferredWidth, getSuggestedMinimumWidth());

        heightSize = resolveSize(preferredHeight, heightMeasureSpec);
        widthSize = resolveSize(preferredWidth, widthMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);
        mHeightMeasureSpec = heightMeasureSpec;
        mWidthMeasureSpec = widthMeasureSpec;
    }
    
    /**
     * Gets the child height.
     *
     * @param child the child
     * @return the child height
     */
    int getChildHeight(View child) {
        return child.getMeasuredHeight();
    }
    
    /**
     * Gets the child width.
     *
     * @param child the child
     * @return the child width
     */
    int getChildWidth(View child) {
        return child.getMeasuredWidth();
    }
    
    /**
     * 描述：TODO
     * @see android.view.ViewGroup#generateDefaultLayoutParams()
     * @author: zhaoqp
     * @date：2013-11-28 上午11:14:34
     * @version v1.0
     */
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        /*
         * Carousel expects Carousel.LayoutParams.
         */
        return new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    	
    }
    
    /**
     * Recycle all views.
     */
    void recycleAllViews() {
        final int childCount = getChildCount();
        final CarouselSpinner.RecycleBin recycleBin = mRecycler;
        final int position = mFirstPosition;

        // All views go in recycler
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            int index = position + i;
            recycleBin.put(index, v);
        }  
    }    
    
    /**
     * Override to prevent spamming ourselves with layout requests
     * as we place views.
     *
     * @see android.view.View#requestLayout()
     */
    @Override
    public void requestLayout() {
        if (!mBlockLayoutRequests) {
            super.requestLayout();
        }
    }
    

    /**
     * 描述：TODO
     * @see com.ab.view.carousel.CarouselAdapter#getCount()
     * @author: zhaoqp
     * @date：2013-11-28 上午11:14:34
     * @version v1.0
     */
    @Override
    public int getCount() {
        return mItemCount;
    }    
	
    /**
     * Maps a point to a position in the list.
     * 
     * @param x X in local coordinate
     * @param y Y in local coordinate
     * @return The position of the item which contains the specified point, or
     *         {@link #INVALID_POSITION} if the point does not intersect an item.
     */
    public int pointToPositionView(int x, int y) {    	

    	ArrayList<CarouselItemView> fitting = new ArrayList<CarouselItemView>();
    	
    	for(int i = 0; i < mAdapter.getCount(); i++){

    		CarouselItemView item = (CarouselItemView)getChildAt(i);

    		Matrix mm = item.getCIMatrix();
    		float[] pts = new float[3];
    		
    		pts[0] = item.getLeft();
    		pts[1] = item.getTop();
    		pts[2] = 0;
    		
    		mm.mapPoints(pts);
    		
    		int mappedLeft = (int)pts[0];
    		int mappedTop =  (int)pts[1];
    		    		
    		pts[0] = item.getRight();
    		pts[1] = item.getBottom();
    		pts[2] = 0;
    		
    		mm.mapPoints(pts);

    		int mappedRight = (int)pts[0];
    		int mappedBottom = (int)pts[1];
    		
    		if(mappedLeft < x && mappedRight > x & mappedTop < y && mappedBottom > y)
    			fitting.add(item);
    		
    	}
    	
    	Collections.sort(fitting);
    	
    	if(fitting.size() != 0)
    		return fitting.get(0).getIndex();
    	else
    		return mSelectedPosition;
    }
    
    /**
     * Maps a point to a position in the list.
     * 
     * @param x X in local coordinate
     * @param y Y in local coordinate
     * @return The position of the item which contains the specified point, or
     *         {@link #INVALID_POSITION} if the point does not intersect an item.
     */
    public int pointToPositionImage(int x, int y) {    	

    	ArrayList<CarouselItemImage> fitting = new ArrayList<CarouselItemImage>();
    	
    	for(int i = 0; i < mAdapter.getCount(); i++){

    		CarouselItemImage item = (CarouselItemImage)getChildAt(i);

    		Matrix mm = item.getCIMatrix();
    		float[] pts = new float[3];
    		
    		pts[0] = item.getLeft();
    		pts[1] = item.getTop();
    		pts[2] = 0;
    		
    		mm.mapPoints(pts);
    		
    		int mappedLeft = (int)pts[0];
    		int mappedTop =  (int)pts[1];
    		    		
    		pts[0] = item.getRight();
    		pts[1] = item.getBottom();
    		pts[2] = 0;
    		
    		mm.mapPoints(pts);

    		int mappedRight = (int)pts[0];
    		int mappedBottom = (int)pts[1];
    		
    		if(mappedLeft < x && mappedRight > x & mappedTop < y && mappedBottom > y)
    			fitting.add(item);
    		
    	}
    	
    	Collections.sort(fitting);
    	
    	if(fitting.size() != 0)
    		return fitting.get(0).getIndex();
    	else
    		return mSelectedPosition;
    }
    
    /**
     * The Class SavedState.
     */
    static class SavedState extends BaseSavedState {
        
        /** The selected id. */
        long selectedId;
        
        /** The position. */
        int position;

        /**
         * Constructor called from {@link AbsSpinner#onSaveInstanceState()}.
         *
         * @param superState the super state
         */
        SavedState(Parcelable superState) {
            super(superState);
        }
        
        /**
         * Constructor called from {@link #CREATOR}.
         *
         * @param in the in
         */
        private SavedState(Parcel in) {
            super(in);
            selectedId = in.readLong();
            position = in.readInt();
        }

        /**
         * 描述：TODO
         * @see android.view.AbsSavedState#writeToParcel(android.os.Parcel, int)
         * @author: zhaoqp
         * @date：2013-11-28 上午11:14:34
         * @version v1.0
         */
        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeLong(selectedId);
            out.writeInt(position);
        }

        /**
         * 描述：TODO
         * @see java.lang.Object#toString()
         * @author: zhaoqp
         * @date：2013-11-28 上午11:14:34
         * @version v1.0
         */
        @Override
        public String toString() {
            return "AbsSpinner.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " selectedId=" + selectedId
                    + " position=" + position + "}";
        }

        /** The Constant CREATOR. */
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    /**
     * 描述：TODO
     * @see android.view.View#onSaveInstanceState()
     * @author: zhaoqp
     * @date：2013-11-28 上午11:14:34
     * @version v1.0
     */
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState ss = new SavedState(superState);
        ss.selectedId = getSelectedItemId();
        if (ss.selectedId >= 0) {
            ss.position = getSelectedItemPosition();
        } else {
            ss.position = INVALID_POSITION;
        }
        return ss;
    }    

    /**
     * 描述：TODO
     * @see android.view.View#onRestoreInstanceState(android.os.Parcelable)
     * @author: zhaoqp
     * @date：2013-11-28 上午11:14:34
     * @version v1.0
     */
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState ss = (SavedState) state;
  
        super.onRestoreInstanceState(ss.getSuperState());

        if (ss.selectedId >= 0) {
            mDataChanged = true;
            mNeedSync = true;
            mSyncRowId = ss.selectedId;
            mSyncPosition = ss.position;
            mSyncMode = SYNC_SELECTED_POSITION;
            requestLayout();
        }
    }
    
    /**
     * The Class RecycleBin.
     */
    class RecycleBin {
        
        /** The m scrap heap. */
        private final SparseArray<View> mScrapHeap = new SparseArray<View>();

        /**
         * Put.
         *
         * @param position the position
         * @param v the v
         */
        public void put(int position, View v) {
            mScrapHeap.put(position, v);
        }
        
        /**
         * Gets the.
         *
         * @param position the position
         * @return the view
         */
        View get(int position) {
            // System.out.print("Looking for " + position);
            View result = mScrapHeap.get(position);
            if (result != null) {
                // System.out.println(" HIT");
                mScrapHeap.delete(position);
            } else {
                // System.out.println(" MISS");
            }
            return result;
        }

        /**
         * Clear.
         */
        void clear() {
            final SparseArray<View> scrapHeap = mScrapHeap;
            final int count = scrapHeap.size();
            for (int i = 0; i < count; i++) {
                final View view = scrapHeap.valueAt(i);
                if (view != null) {
                    removeDetachedView(view, true);
                }
            }
            scrapHeap.clear();
        }
    }	
}
