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
package com.ab.view.slidingmenu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.ab.view.slidingmenu.SlidingMenu.CanvasTransformer;

// TODO: Auto-generated Javadoc
/**
 * The Class CustomViewBehind.
 */
public class CustomViewBehind extends ViewGroup {

	/** The Constant TAG. */
	private static final String TAG = "CustomViewBehind";

	/** The Constant MARGIN_THRESHOLD. */
	private static final int MARGIN_THRESHOLD = 48; // dips
	
	/** The m touch mode. */
	private int mTouchMode = SlidingMenu.TOUCHMODE_MARGIN;

	/** The m view above. */
	private CustomViewAbove mViewAbove;

	/** The m content. */
	private View mContent;
	
	/** The m secondary content. */
	private View mSecondaryContent;
	
	/** The m margin threshold. */
	private int mMarginThreshold;
	
	/** The m width offset. */
	private int mWidthOffset;
	
	/** The m transformer. */
	private CanvasTransformer mTransformer;
	
	/** The m children enabled. */
	private boolean mChildrenEnabled;
	
	/** The selected view id. */
	private int selectedViewId = 2012;

	/**
	 * Instantiates a new custom view behind.
	 *
	 * @param context the context
	 */
	public CustomViewBehind(Context context) {
		this(context, null);
	}

	/**
	 * Instantiates a new custom view behind.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CustomViewBehind(Context context, AttributeSet attrs) {
		super(context, attrs);
		mMarginThreshold = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				MARGIN_THRESHOLD, getResources().getDisplayMetrics());
	}

	/**
	 * Sets the custom view above.
	 *
	 * @param customViewAbove the new custom view above
	 */
	public void setCustomViewAbove(CustomViewAbove customViewAbove) {
		mViewAbove = customViewAbove;
	}

	/**
	 * Sets the canvas transformer.
	 *
	 * @param t the new canvas transformer
	 */
	public void setCanvasTransformer(CanvasTransformer t) {
		mTransformer = t;
	}

	/**
	 * Sets the width offset.
	 *
	 * @param i the new width offset
	 */
	public void setWidthOffset(int i) {
		mWidthOffset = i;
		requestLayout();
	}
	
	/**
	 * Sets the margin threshold.
	 *
	 * @param marginThreshold the new margin threshold
	 */
	public void setMarginThreshold(int marginThreshold) {
		mMarginThreshold = marginThreshold;
	}
	
	/**
	 * Gets the margin threshold.
	 *
	 * @return the margin threshold
	 */
	public int getMarginThreshold() {
		return mMarginThreshold;
	}

	/**
	 * Gets the behind width.
	 *
	 * @return the behind width
	 */
	public int getBehindWidth() {
		return mContent.getWidth();
	}

	/**
	 * Sets the content.
	 *
	 * @param v the new content
	 */
	public void setContent(View v) {
		if (mContent != null)
			removeView(mContent);
		mContent = v;
		addView(mContent);
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public View getContent() {
		return mContent;
	}

	/**
	 * Sets the secondary (right) menu for use when setMode is called with SlidingMenu.LEFT_RIGHT.
	 * @param v the right menu
	 */
	public void setSecondaryContent(View v) {
		if (mSecondaryContent != null)
			removeView(mSecondaryContent);
		mSecondaryContent = v;
		addView(mSecondaryContent);
	}

	/**
	 * Gets the secondary content.
	 *
	 * @return the secondary content
	 */
	public View getSecondaryContent() {
		return mSecondaryContent;
	}

	/**
	 * Sets the children enabled.
	 *
	 * @param enabled the new children enabled
	 */
	public void setChildrenEnabled(boolean enabled) {
		mChildrenEnabled = enabled;
	}

	/* (non-Javadoc)
	 * @see android.view.View#scrollTo(int, int)
	 */
	@Override
	public void scrollTo(int x, int y) {
		super.scrollTo(x, y);
		if (mTransformer != null)
			invalidate();
	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#onInterceptTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		return !mChildrenEnabled;
	}

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		return !mChildrenEnabled;
	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#dispatchDraw(android.graphics.Canvas)
	 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		if (mTransformer != null) {
			canvas.save();
			mTransformer.transformCanvas(canvas, mViewAbove.getPercentOpen());
			super.dispatchDraw(canvas);
			canvas.restore();
		} else
			super.dispatchDraw(canvas);
	}

	/* (non-Javadoc)
	 * @see android.view.ViewGroup#onLayout(boolean, int, int, int, int)
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int width = r - l;
		final int height = b - t;
		mContent.layout(0, 0, width-mWidthOffset, height);
		if (mSecondaryContent != null)
			mSecondaryContent.layout(0, 0, width-mWidthOffset, height);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = getDefaultSize(0, widthMeasureSpec);
		int height = getDefaultSize(0, heightMeasureSpec);
		setMeasuredDimension(width, height);
		final int contentWidth = getChildMeasureSpec(widthMeasureSpec, 0, width-mWidthOffset);
		final int contentHeight = getChildMeasureSpec(heightMeasureSpec, 0, height);
		mContent.measure(contentWidth, contentHeight);
		if (mSecondaryContent != null)
			mSecondaryContent.measure(contentWidth, contentHeight);
	}

	/** The m mode. */
	private int mMode;
	
	/** The m fade enabled. */
	private boolean mFadeEnabled;
	
	/** The m fade paint. */
	private final Paint mFadePaint = new Paint();
	
	/** The m scroll scale. */
	private float mScrollScale;
	
	/** The m shadow drawable. */
	private Drawable mShadowDrawable;
	
	/** The m secondary shadow drawable. */
	private Drawable mSecondaryShadowDrawable;
	
	/** The m shadow width. */
	private int mShadowWidth;
	
	/** The m fade degree. */
	private float mFadeDegree;

	/**
	 * Sets the mode.
	 *
	 * @param mode the new mode
	 */
	public void setMode(int mode) {
		if (mode == SlidingMenu.LEFT || mode == SlidingMenu.RIGHT) {
			if (mContent != null)
				mContent.setVisibility(View.VISIBLE);
			if (mSecondaryContent != null)
				mSecondaryContent.setVisibility(View.INVISIBLE);
		}
		mMode = mode;
	}

	/**
	 * Gets the mode.
	 *
	 * @return the mode
	 */
	public int getMode() {
		return mMode;
	}

	/**
	 * Sets the scroll scale.
	 *
	 * @param scrollScale the new scroll scale
	 */
	public void setScrollScale(float scrollScale) {
		mScrollScale = scrollScale;
	}

	/**
	 * Gets the scroll scale.
	 *
	 * @return the scroll scale
	 */
	public float getScrollScale() {
		return mScrollScale;
	}

	/**
	 * Sets the shadow drawable.
	 *
	 * @param shadow the new shadow drawable
	 */
	public void setShadowDrawable(Drawable shadow) {
		mShadowDrawable = shadow;
		invalidate();
	}

	/**
	 * Sets the secondary shadow drawable.
	 *
	 * @param shadow the new secondary shadow drawable
	 */
	public void setSecondaryShadowDrawable(Drawable shadow) {
		mSecondaryShadowDrawable = shadow;
		invalidate();
	}

	/**
	 * Sets the shadow width.
	 *
	 * @param width the new shadow width
	 */
	public void setShadowWidth(int width) {
		mShadowWidth = width;
		invalidate();
	}

	/**
	 * Sets the fade enabled.
	 *
	 * @param b the new fade enabled
	 */
	public void setFadeEnabled(boolean b) {
		mFadeEnabled = b;
	}

	/**
	 * Sets the fade degree.
	 *
	 * @param degree the new fade degree
	 */
	public void setFadeDegree(float degree) {
		if (degree > 1.0f || degree < 0.0f)
			throw new IllegalStateException("The BehindFadeDegree must be between 0.0f and 1.0f");
		mFadeDegree = degree;
	}

	/**
	 * Gets the menu page.
	 *
	 * @param page the page
	 * @return the menu page
	 */
	public int getMenuPage(int page) {
		page = (page > 1) ? 2 : ((page < 1) ? 0 : page);
		if (mMode == SlidingMenu.LEFT && page > 1) {
			return 0;
		} else if (mMode == SlidingMenu.RIGHT && page < 1) {
			return 2;
		} else {
			return page;
		}
	}

	/**
	 * Scroll behind to.
	 *
	 * @param content the content
	 * @param x the x
	 * @param y the y
	 */
	public void scrollBehindTo(View content, int x, int y) {
		int vis = View.VISIBLE;		
		if (mMode == SlidingMenu.LEFT) {
			if (x >= content.getLeft()) vis = View.INVISIBLE;
			scrollTo((int)((x + getBehindWidth())*mScrollScale), y);
		} else if (mMode == SlidingMenu.RIGHT) {
			if (x <= content.getLeft()) vis = View.INVISIBLE;
			scrollTo((int)(getBehindWidth() - getWidth() + 
					(x-getBehindWidth())*mScrollScale), y);
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			mContent.setVisibility(x >= content.getLeft() ? View.INVISIBLE : View.VISIBLE);
			mSecondaryContent.setVisibility(x <= content.getLeft() ? View.INVISIBLE : View.VISIBLE);
			vis = x == 0 ? View.INVISIBLE : View.VISIBLE;
			if (x <= content.getLeft()) {
				scrollTo((int)((x + getBehindWidth())*mScrollScale), y);				
			} else {
				scrollTo((int)(getBehindWidth() - getWidth() + 
						(x-getBehindWidth())*mScrollScale), y);				
			}
		}
		if (vis == View.INVISIBLE)
			Log.v(TAG, "behind INVISIBLE");
		setVisibility(vis);
	}

	/**
	 * Gets the menu left.
	 *
	 * @param content the content
	 * @param page the page
	 * @return the menu left
	 */
	public int getMenuLeft(View content, int page) {
		if (mMode == SlidingMenu.LEFT) {
			switch (page) {
			case 0:
				return content.getLeft() - getBehindWidth();
			case 2:
				return content.getLeft();
			}
		} else if (mMode == SlidingMenu.RIGHT) {
			switch (page) {
			case 0:
				return content.getLeft();
			case 2:
				return content.getLeft() + getBehindWidth();	
			}
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			switch (page) {
			case 0:
				return content.getLeft() - getBehindWidth();
			case 2:
				return content.getLeft() + getBehindWidth();
			}
		}
		return content.getLeft();
	}

	/**
	 * Gets the abs left bound.
	 *
	 * @param content the content
	 * @return the abs left bound
	 */
	public int getAbsLeftBound(View content) {
		if (mMode == SlidingMenu.LEFT || mMode == SlidingMenu.LEFT_RIGHT) {
			return content.getLeft() - getBehindWidth();
		} else if (mMode == SlidingMenu.RIGHT) {
			return content.getLeft();
		}
		return 0;
	}

	/**
	 * Gets the abs right bound.
	 *
	 * @param content the content
	 * @return the abs right bound
	 */
	public int getAbsRightBound(View content) {
		if (mMode == SlidingMenu.LEFT) {
			return content.getLeft();
		} else if (mMode == SlidingMenu.RIGHT || mMode == SlidingMenu.LEFT_RIGHT) {
			return content.getLeft() + getBehindWidth();
		}
		return 0;
	}

	/**
	 * Margin touch allowed.
	 *
	 * @param content the content
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean marginTouchAllowed(View content, int x) {
		int left = content.getLeft();
		int right = content.getRight();
		if (mMode == SlidingMenu.LEFT) {
			return (x >= left && x <= mMarginThreshold + left);
		} else if (mMode == SlidingMenu.RIGHT) {
			return (x <= right && x >= right - mMarginThreshold);
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			return (x >= left && x <= mMarginThreshold + left) || 
					(x <= right && x >= right - mMarginThreshold);
		}
		return false;
	}

	/**
	 * Sets the touch mode.
	 *
	 * @param i the new touch mode
	 */
	public void setTouchMode(int i) {
		mTouchMode = i;
	}

	/**
	 * Menu open touch allowed.
	 *
	 * @param content the content
	 * @param currPage the curr page
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean menuOpenTouchAllowed(View content, int currPage, float x) {
		switch (mTouchMode) {
		case SlidingMenu.TOUCHMODE_FULLSCREEN:
			return true;
		case SlidingMenu.TOUCHMODE_MARGIN:
			return menuTouchInQuickReturn(content, currPage, x);
		}
		return false;
	}

	/**
	 * Menu touch in quick return.
	 *
	 * @param content the content
	 * @param currPage the curr page
	 * @param x the x
	 * @return true, if successful
	 */
	public boolean menuTouchInQuickReturn(View content, int currPage, float x) {
		if (mMode == SlidingMenu.LEFT || (mMode == SlidingMenu.LEFT_RIGHT && currPage == 0)) {
			return x >= content.getLeft();
		} else if (mMode == SlidingMenu.RIGHT || (mMode == SlidingMenu.LEFT_RIGHT && currPage == 2)) {
			return x <= content.getRight();
		}
		return false;
	}

	/**
	 * Menu closed slide allowed.
	 *
	 * @param dx the dx
	 * @return true, if successful
	 */
	public boolean menuClosedSlideAllowed(float dx) {
		if (mMode == SlidingMenu.LEFT) {
			return dx > 0;
		} else if (mMode == SlidingMenu.RIGHT) {
			return dx < 0;
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			return true;
		}
		return false;
	}

	/**
	 * Menu open slide allowed.
	 *
	 * @param dx the dx
	 * @return true, if successful
	 */
	public boolean menuOpenSlideAllowed(float dx) {
		if (mMode == SlidingMenu.LEFT) {
			return dx < 0;
		} else if (mMode == SlidingMenu.RIGHT) {
			return dx > 0;
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			return true;
		}
		return false;
	}

	/**
	 * Draw shadow.
	 *
	 * @param content the content
	 * @param canvas the canvas
	 */
	public void drawShadow(View content, Canvas canvas) {
		if (mShadowDrawable == null || mShadowWidth <= 0) return;
		int left = 0;
		if (mMode == SlidingMenu.LEFT) {
			left = content.getLeft() - mShadowWidth;
		} else if (mMode == SlidingMenu.RIGHT) {
			left = content.getRight();
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			if (mSecondaryShadowDrawable != null) {
				left = content.getRight();
				mSecondaryShadowDrawable.setBounds(left, 0, left + mShadowWidth, getHeight());
				mSecondaryShadowDrawable.draw(canvas);
			}
			left = content.getLeft() - mShadowWidth;
		}
		mShadowDrawable.setBounds(left, 0, left + mShadowWidth, getHeight());
		mShadowDrawable.draw(canvas);
	}

	/**
	 * Draw fade.
	 *
	 * @param content the content
	 * @param canvas the canvas
	 * @param openPercent the open percent
	 */
	public void drawFade(View content, Canvas canvas, float openPercent) {
		if (!mFadeEnabled) return;
		final int alpha = (int) (mFadeDegree * 255 * Math.abs(1-openPercent));
		mFadePaint.setColor(Color.argb(alpha, 0, 0, 0));
		int left = 0;
		int right = 0;
		if (mMode == SlidingMenu.LEFT) {
			left = content.getLeft() - getBehindWidth();
			right = content.getLeft();
		} else if (mMode == SlidingMenu.RIGHT) {
			left = content.getRight();
			right = content.getRight() + getBehindWidth();			
		} else if (mMode == SlidingMenu.LEFT_RIGHT) {
			left = content.getLeft() - getBehindWidth();
			right = content.getLeft();
			canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
			left = content.getRight();
			right = content.getRight() + getBehindWidth();			
		}
		canvas.drawRect(left, 0, right, getHeight(), mFadePaint);
	}
	
	/** The m selector enabled. */
	private boolean mSelectorEnabled = true;
	
	/** The m selector drawable. */
	private Bitmap mSelectorDrawable;
	
	/** The m selected view. */
	private View mSelectedView;
	
	/**
	 * Draw selector.
	 *
	 * @param content the content
	 * @param canvas the canvas
	 * @param openPercent the open percent
	 */
	public void drawSelector(View content, Canvas canvas, float openPercent) {
		if (!mSelectorEnabled) return;
		if (mSelectorDrawable != null && mSelectedView != null) {
			String tag = (String) mSelectedView.getTag(selectedViewId);
			if (tag.equals(TAG+"SelectedView")) {
				canvas.save();
				int left, right, offset;
				offset = (int) (mSelectorDrawable.getWidth() * openPercent);
				if (mMode == SlidingMenu.LEFT) {
					right = content.getLeft();
					left = right - offset;
					canvas.clipRect(left, 0, right, getHeight());
					canvas.drawBitmap(mSelectorDrawable, left, getSelectorTop(), null);		
				} else if (mMode == SlidingMenu.RIGHT) {
					left = content.getRight();
					right = left + offset;
					canvas.clipRect(left, 0, right, getHeight());
					canvas.drawBitmap(mSelectorDrawable, right - mSelectorDrawable.getWidth(), getSelectorTop(), null);
				}
				canvas.restore();
			}
		}
	}
	
	/**
	 * Sets the selector enabled.
	 *
	 * @param b the new selector enabled
	 */
	public void setSelectorEnabled(boolean b) {
		mSelectorEnabled = b;
	}

	/**
	 * Sets the selected view.
	 *
	 * @param v the new selected view
	 */
	public void setSelectedView(View v) {
		if (mSelectedView != null) {
			mSelectedView.setTag(selectedViewId, null);
			mSelectedView = null;
		}
		if (v != null && v.getParent() != null) {
			mSelectedView = v;
			mSelectedView.setTag(selectedViewId, TAG+"SelectedView");
			invalidate();
		}
	}

	/**
	 * Gets the selector top.
	 *
	 * @return the selector top
	 */
	private int getSelectorTop() {
		int y = mSelectedView.getTop();
		y += (mSelectedView.getHeight() - mSelectorDrawable.getHeight()) / 2;
		return y;
	}

	/**
	 * Sets the selector bitmap.
	 *
	 * @param b the new selector bitmap
	 */
	public void setSelectorBitmap(Bitmap b) {
		mSelectorDrawable = b;
		refreshDrawableState();
	}

}
