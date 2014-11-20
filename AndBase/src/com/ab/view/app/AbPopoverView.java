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
package com.ab.view.app;

import java.util.HashMap;
import java.util.Map;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
// TODO: Auto-generated Javadoc

/**
 * © 2012-2015 amsoft.cn
 * 名称：AbPopoverView.java 
 * 描述：提示框
 *
 * @author zhaoqp
 * @version v1.0
 * @date：2013-11-18 下午5:02:16
 */
public class AbPopoverView extends RelativeLayout implements OnTouchListener{
	
	/**
	 * AbPopoverView的监听器.
	 *
	 * @see PopoverViewEvent
	 */
	public static interface PopoverViewListener{
		
		/**
		 * Called when the popover is going to show.
		 *
		 * @param view The whole popover view
		 */
		void popoverViewWillShow(AbPopoverView view);
		
		/**
		 * Called when the popover did show.
		 *
		 * @param view The whole popover view
		 */
		void popoverViewDidShow(AbPopoverView view);
		
		/**
		 * Called when the popover is going to be dismissed.
		 *
		 * @param view The whole popover view
		 */
		void popoverViewWillDismiss(AbPopoverView view);
		
		/**
		 * Called when the popover was dismissed.
		 *
		 * @param view The whole popover view
		 */
		void popoverViewDidDismiss(AbPopoverView view);
	}
	
	
	/**
	 * Popover arrow points up. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionUp    = 0x00000001;
	/**
	 * Popover arrow points down. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionDown  = 0x00000002;
	/**
	 * Popover arrow points left. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionLeft  = 0x00000004;
	/**
	 * Popover arrow points right. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionRight = 0x00000008;
	/**
	 * Popover arrow points any direction. Integer to use with bit operators to tell the popover where the arrow should appear and from where the popover should appear
	 */
	public final static int PopoverArrowDirectionAny = PopoverArrowDirectionUp|PopoverArrowDirectionDown|PopoverArrowDirectionLeft|PopoverArrowDirectionRight;
	
	/** The delegate of the view. */
	private PopoverViewListener popoverViewListener;
	
	/** The main popover containing the view we want to show. */
	private RelativeLayout popoverView;
	/**
	 * The view group storing this popover. We need this so, when we dismiss the popover, we remove it from the view group
	 */
	private ViewGroup superview;
	
	/** The content size for the view in the popover. */
	private Point contentSizeForViewInPopover = new Point(0, 0);
	
	/** The real content size we will use (it considers the padding). */
	private Point realContentSize = new Point(0, 0);
	
	/** A hash containing. */
	private Map<Integer, Rect> possibleRects;
	
	/** Whether the view is animating or not. */
	private boolean isAnimating = false;
	
	/** The fade animation time in milliseconds. */
	private int fadeAnimationTime = 300;
	
	/** The layout Rect, is the same as the superview rect. */
	private Rect popoverLayoutRect;
	
	/** The popover background drawable. */
	private Drawable backgroundDrawable;
	
	/** The popover arrow up drawable. */
	private Drawable arrowUpDrawable;
	
	/** The popover arrow down drawable. */
	private Drawable arrowDownDrawable;
	
	/** The popover arrow left drawable. */
	private Drawable arrowLeftDrawable;
	
	/** The popover arrow down drawable. */
	private Drawable arrowRightDrawable;
	
	/** 当前显示的箭头图标. */
	private ImageView arrowImageView = null;
	
	/** 当前显示的提示的View. */
	private View popoverContentView = null;
	
	
	/**
	 * Constructor to create a popover with a popover view.
	 *
	 * @param context The context where we should create the popover view
	 */
	public AbPopoverView(Context context) {
		super(context);
		initPopoverView();
	}

	/**
	 * Constructor to create a popover with a popover view.
	 *
	 * @param context The context where we should create the popover view
	 * @param attrs Attribute set to init the view
	 */
	public AbPopoverView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initPopoverView();
	}
	
	/**
	 * Constructor to create a popover with a popover view.
	 *
	 * @param context The context where we should create the popover view
	 * @param attrs Attribute set to init the view
	 * @param defStyle The default style for this view
	 */
	public AbPopoverView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initPopoverView();
	}
	
	/**
	 * Init the popover view.
	 */
	private void initPopoverView(){
		
		//Init the relative layout
		popoverView = new RelativeLayout(getContext());
		setBackgroundColor(Color.WHITE);
		setOnTouchListener(this);
	}
	
	/**
	 * Get the Rect frame for a view (relative to the Window of the application).
	 *
	 * @param v The view to get the rect from
	 * @return The rect of the view, relative to the application window
	 */
	public static Rect getFrameForView(View v){
		int location [] = new int [2];
		v.getLocationOnScreen(location);
		Rect viewRect = new Rect(location[0], location[1], location[0]+v.getWidth(), location[1]+v.getHeight());
		return viewRect;
	}
	
	
	/**
	 * Add the popover to the view with a defined rect inside the popover.
	 *
	 * @param insertRect The rect we want to insert the view
	 */
	private void addPopoverInRect(Rect insertRect){
		//Set layout params
		LayoutParams insertParams = new LayoutParams(insertRect.width(), insertRect.height());
		insertParams.leftMargin = insertRect.left;
		insertParams.topMargin = insertRect.top;
		//Add the view
		addView(popoverView, insertParams);
		
	}
	
	
	/**
	 * Inits the arrow.
	 *
	 * @param originRect the origin rect
	 * @param arrowDirection the arrow direction
	 */
	private void initArrow(Rect originRect, Integer arrowDirection){
		
		//重新定位
		if(arrowImageView != null){
			removeView(arrowImageView);
		}
		
		//Add arrow drawable
		arrowImageView = new ImageView(getContext());
		Drawable arrowDrawable = null;
		int xPos = 0;
		int arrowWidth = 0;
		int yPos = 0;
		int arrowHeight = 0;
		//Get correct drawable, and get Width, Height, Xpos and yPos depending on the selected arrow direction
		if (arrowDirection == AbPopoverView.PopoverArrowDirectionUp){
			arrowDrawable = arrowUpDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.centerX() - (arrowWidth/2) - popoverLayoutRect.left;
			yPos = originRect.bottom - popoverLayoutRect.top;
		}
		else if (arrowDirection == AbPopoverView.PopoverArrowDirectionDown){
			arrowDrawable = arrowDownDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.centerX() - (arrowWidth/2) - popoverLayoutRect.left;
			yPos = originRect.top - arrowHeight - popoverLayoutRect.top;
		}
		else if (arrowDirection == AbPopoverView.PopoverArrowDirectionLeft){
			arrowDrawable = arrowLeftDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.right - popoverLayoutRect.left;
			yPos = originRect.centerY() - (arrowHeight/2) - popoverLayoutRect.top;
		}
		else if (arrowDirection == AbPopoverView.PopoverArrowDirectionRight){
			arrowDrawable = arrowRightDrawable;
			arrowWidth = arrowDrawable.getIntrinsicWidth();
			arrowHeight = arrowDrawable.getIntrinsicHeight();
			xPos = originRect.left - arrowWidth - popoverLayoutRect.left;
			yPos = originRect.centerY() - (arrowHeight/2) - popoverLayoutRect.top;
		}
		//Set drawable
		arrowImageView.setImageDrawable(arrowDrawable);
		//Init layout params
		LayoutParams arrowParams = new LayoutParams(arrowWidth, arrowHeight);
		arrowParams.leftMargin = xPos;
		arrowParams.topMargin = yPos;
		
		//add view
		addView(arrowImageView, arrowParams);
	}
	
	
	/**
	 * Calculates the rect for showing the view with Arrow Up.
	 *
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowUp(Rect originRect){
		
		//Get available space		
		int xAvailable = popoverLayoutRect.width();
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = popoverLayoutRect.height() - (originRect.bottom - popoverLayoutRect.top);
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.centerX()-popoverLayoutRect.left) - (finalX/2) ;
		if (originX < 0)
			originX = 0;
		else if (originX+finalX > popoverLayoutRect.width())
			originX = popoverLayoutRect.width() - finalX;
		int originY = (originRect.bottom - popoverLayoutRect.top);
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
		
	}
	
	/**
	 * Calculates the rect for showing the view with Arrow Down.
	 *
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowDown(Rect originRect){
		
		//Get available space		
		int xAvailable = popoverLayoutRect.width();
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = (originRect.top - popoverLayoutRect.top);
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.centerX()-popoverLayoutRect.left) - (finalX/2) ;
		if (originX < 0)
			originX = 0;
		else if (originX+finalX > popoverLayoutRect.width())
			originX = popoverLayoutRect.width() - finalX;
		int originY = (originRect.top - popoverLayoutRect.top) - finalY;
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
		
	}
	
	
	/**
	 * Calculates the rect for showing the view with Arrow Right.
	 *
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowRight(Rect originRect){
		//Get available space		
		int xAvailable = (originRect.left - popoverLayoutRect.left);
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = popoverLayoutRect.height();
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.left - popoverLayoutRect.left) - finalX;
		int originY = (originRect.centerY()-popoverLayoutRect.top) - (finalY/2) ;
		if (originY < 0)
			originY = 0;
		else if (originY+finalY > popoverLayoutRect.height())
			originY = popoverLayoutRect.height() - finalY;
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
	}
	
	/**
	 * Calculates the rect for showing the view with Arrow Left.
	 *
	 * @param originRect The origin rect
	 * @return The calculated rect to show the view
	 */
	private Rect getRectForArrowLeft(Rect originRect){
		//Get available space		
		int xAvailable = popoverLayoutRect.width() - (originRect.right - popoverLayoutRect.left);
		if (xAvailable < 0)
			xAvailable = 0;
		int yAvailable = popoverLayoutRect.height();
		if (yAvailable < 0)
			yAvailable = 0;
		
		//Get final width and height
		int finalX = xAvailable;
		if ((realContentSize.x > 0) && (realContentSize.x < finalX))
			finalX = realContentSize.x;
		int finalY = yAvailable;
		if ((realContentSize.y > 0) && (realContentSize.y < finalY))
			finalY = realContentSize.y;
		
		//Get final origin X and Y
		int originX = (originRect.right - popoverLayoutRect.left);
		int originY = (originRect.centerY()-popoverLayoutRect.top) - (finalY/2) ;
		if (originY < 0)
			originY = 0;
		else if (originY+finalY > popoverLayoutRect.height())
			originY = popoverLayoutRect.height() - finalY;
		
		//Create rect
		Rect finalRect = new Rect(originX, originY, originX+finalX, originY+finalY);
		//And return
		return finalRect;
	}
	
	
	/**
	 * Add available rects for each selected arrow direction.
	 *
	 * @param originRect The rect where the popover will appear from
	 * @param arrowDirections The bit mask for the possible arrow directions
	 */
	private void addAvailableRects(Rect originRect, int arrowDirections){
		//Get popover rects for the available directions
		possibleRects = new HashMap<Integer, Rect>();
		if ((arrowDirections & AbPopoverView.PopoverArrowDirectionUp) != 0){
			possibleRects.put(AbPopoverView.PopoverArrowDirectionUp, getRectForArrowUp(originRect));
		}
		if ((arrowDirections & AbPopoverView.PopoverArrowDirectionDown) != 0){
			possibleRects.put(AbPopoverView.PopoverArrowDirectionDown, getRectForArrowDown(originRect));
		}
		if ((arrowDirections & AbPopoverView.PopoverArrowDirectionRight) != 0){
			possibleRects.put(AbPopoverView.PopoverArrowDirectionRight, getRectForArrowRight(originRect));
		}
		if ((arrowDirections & AbPopoverView.PopoverArrowDirectionLeft) != 0){
			possibleRects.put(AbPopoverView.PopoverArrowDirectionLeft, getRectForArrowLeft(originRect));
		}
		
	}
	
	/**
	 * Get the best available rect (bigger area).
	 *
	 * @return The Integer key to get the Rect from posibleRects (PopoverArrowDirectionUp,PopoverArrowDirectionDown,PopoverArrowDirectionRight or PopoverArrowDirectionLeft)
	 */
	private Integer getBestRect(){
		//Get the best one (bigger area)
		Integer best = null;
		for (Integer arrowDir : possibleRects.keySet()) {
			if (best == null){
				best = arrowDir;	
			}
			else{
				Rect bestRect = possibleRects.get(best);
				Rect checkRect = possibleRects.get(arrowDir);
				if ((bestRect.width()*bestRect.height()) < (checkRect.width()*checkRect.height()))
					best = arrowDir;
			}
		}
		return best;
	}
	
	
	/**
	 * Gets the current fade animation time.
	 *
	 * @return The fade animation time, in milliseconds
	 */
	public int getFadeAnimationTime() {
		return fadeAnimationTime;
	}

	/**
	 * Sets the fade animation time.
	 *
	 * @param fadeAnimationTime The time in milliseconds
	 */
	public void setFadeAnimationTime(int fadeAnimationTime) {
		this.fadeAnimationTime = fadeAnimationTime;
	}
	
	/**
	 * Get the content size for view in popover.
	 *
	 * @return The point with the content size
	 */
	public Point getContentSizeForViewInPopover() {
		return contentSizeForViewInPopover;
	}

	/**
	 * Sets the content size for the view in a popover, if point is (0,0) the popover will full the screen.
	 *
	 * @param contentSizeForViewInPopover the new content size for view in popover
	 */
	public void setContentSizeForViewInPopover(Point contentSizeForViewInPopover) {
		this.contentSizeForViewInPopover = contentSizeForViewInPopover;
		//Save the real content size
		realContentSize = new Point(contentSizeForViewInPopover);
		realContentSize.x += popoverView.getPaddingLeft()+popoverView.getPaddingRight();
		realContentSize.y += popoverView.getPaddingTop()+popoverView.getPaddingBottom();
		
	}


	/**
	 * Gets the popover view listener.
	 *
	 * @return the popover view listener
	 */
	public PopoverViewListener getPopoverViewListener() {
		return popoverViewListener;
	}

	/**
	 * Sets the popover view listener.
	 *
	 * @param popoverViewListener the new popover view listener
	 */
	public void setPopoverViewListener(PopoverViewListener popoverViewListener) {
		this.popoverViewListener = popoverViewListener;
	}

	/**
	 * This method shows a popover in a ViewGroup, from an origin rect (relative to the Application Window).
	 *
	 * @param group The group we want to insert the popup. Normally a Relative Layout so it can stand on top of everything
	 * @param originRect The rect we want the popup to appear from (relative to the Application Window!)
	 * @param arrowDirections The mask of bits to tell in which directions we want the popover to be shown
	 * @param animated Whether is animated, or not
	 */
	public void showPopoverFromRectInViewGroup(ViewGroup group, Rect originRect, int arrowDirections, boolean animated){
		
		//First, tell delegate we will show
		if (popoverViewListener != null)
			popoverViewListener.popoverViewWillShow(this);
		
		//Save superview
		superview = group;
		
		//First, add the view to the view group. The popover will cover the whole area
		android.view.ViewGroup.LayoutParams insertParams =  new  android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		group.addView(this, insertParams);
		
		//Now, save rect for the layout (is the same as the superview)
		popoverLayoutRect = AbPopoverView.getFrameForView(superview);
		
		//Add available rects
		addAvailableRects(originRect, arrowDirections);
		//Get best rect
		Integer best = getBestRect();
		
		//Add popover
		Rect bestRect = possibleRects.get(best);
		addPopoverInRect(bestRect);
		
		//箭头图标
		initArrow(originRect, best);
		
		
		//If we don't want animation, just tell the delegate
		if (!animated){
			//Tell delegate we did show
			if (popoverViewListener != null)
				popoverViewListener.popoverViewDidShow(this);
		}
		//If we want animation, animate it!
		else{
			//Continue only if we are not animating
			if (!isAnimating){
				
				//Create alpha animation, with its listener
				AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
				animation.setDuration(fadeAnimationTime);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						//End animation
						isAnimating = false;
						//Tell delegate we did show
						if (popoverViewListener != null)
							popoverViewListener.popoverViewDidShow(AbPopoverView.this);
					}
				});
				
				//Start animation
				isAnimating = true;
				startAnimation(animation);
				
			}
		}
		
	}
	
	/**
	 * Dismiss the current shown popover.
	 *
	 * @param animated Whether it should be dismissed animated or not
	 */
	public void dissmissPopover(boolean animated){
		
		//Tell delegate we will dismiss
		if (popoverViewListener != null)
			popoverViewListener.popoverViewWillDismiss(AbPopoverView.this);
		
		//If we don't want animation
		if (!animated){
			//Just remove views
			popoverView.removeAllViews();
			removeAllViews();
			superview.removeView(this);
			//Tell delegate we did dismiss
			if (popoverViewListener != null)
				popoverViewListener.popoverViewDidDismiss(AbPopoverView.this);
		}
		else{
			//Continue only if there is not an animation in progress
			if (!isAnimating){
				//Create alpha animation, with its listener
				AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
				animation.setDuration(fadeAnimationTime);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						//Nothing to do here
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						//Remove the view
						popoverView.removeAllViews();
						removeAllViews();
						AbPopoverView.this.superview.removeView(AbPopoverView.this);
						//End animation
						isAnimating = false;
						//Tell delegate we did dismiss
						if (popoverViewListener != null)
							popoverViewListener.popoverViewDidDismiss(AbPopoverView.this);
					}
				});
				
				//Start animation
				isAnimating = true;
				startAnimation(animation);
			}
			
		}
		
	}
	
	
	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		//If we touched over the background popover view (this)
		if ((!isAnimating) && (v  == this)){
			dissmissPopover(true);
		}
		return true;
	}

	/**
	 * Gets the background drawable.
	 *
	 * @return the background drawable
	 */
	public Drawable getBackgroundDrawable() {
		return backgroundDrawable;
	}

	/* (non-Javadoc)
	 * @see android.view.View#setBackgroundDrawable(android.graphics.drawable.Drawable)
	 */
	public void setBackgroundDrawable(Drawable backgroundDrawable) {
		this.backgroundDrawable = backgroundDrawable;
		popoverView.setBackgroundDrawable(backgroundDrawable);
	}

	/**
	 * Gets the arrow up drawable.
	 *
	 * @return the arrow up drawable
	 */
	public Drawable getArrowUpDrawable() {
		return arrowUpDrawable;
	}

	/**
	 * Sets the arrow up drawable.
	 *
	 * @param arrowUpDrawable the new arrow up drawable
	 */
	public void setArrowUpDrawable(Drawable arrowUpDrawable) {
		this.arrowUpDrawable = arrowUpDrawable;
	}

	/**
	 * Gets the arrow down drawable.
	 *
	 * @return the arrow down drawable
	 */
	public Drawable getArrowDownDrawable() {
		return arrowDownDrawable;
	}

	/**
	 * Sets the arrow down drawable.
	 *
	 * @param arrowDownDrawable the new arrow down drawable
	 */
	public void setArrowDownDrawable(Drawable arrowDownDrawable) {
		this.arrowDownDrawable = arrowDownDrawable;
	}

	/**
	 * Gets the arrow left drawable.
	 *
	 * @return the arrow left drawable
	 */
	public Drawable getArrowLeftDrawable() {
		return arrowLeftDrawable;
	}

	/**
	 * Sets the arrow left drawable.
	 *
	 * @param arrowLeftDrawable the new arrow left drawable
	 */
	public void setArrowLeftDrawable(Drawable arrowLeftDrawable) {
		this.arrowLeftDrawable = arrowLeftDrawable;
	}

	/**
	 * Gets the arrow right drawable.
	 *
	 * @return the arrow right drawable
	 */
	public Drawable getArrowRightDrawable() {
		return arrowRightDrawable;
	}

	/**
	 * Sets the arrow right drawable.
	 *
	 * @param arrowRightDrawable the new arrow right drawable
	 */
	public void setArrowRightDrawable(Drawable arrowRightDrawable) {
		this.arrowRightDrawable = arrowRightDrawable;
	}

	/**
	 * Gets the popover content view.
	 *
	 * @return the popover content view
	 */
	public View getPopoverContentView() {
		return popoverContentView;
	}

	/**
	 * 描述：设置显示的View.
	 *
	 * @param popoverContentView the new popover content view
	 */
	public void setPopoverContentView(View popoverContentView) {
		this.popoverContentView = popoverContentView;
		popoverView.removeAllViews();
		popoverView.addView(popoverContentView,LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}
	
	
	
}
