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

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * The Interface MenuInterface.
 */
public interface MenuInterface {

	/**
	 * Scroll behind to.
	 *
	 * @param x the x
	 * @param y the y
	 * @param cvb the cvb
	 * @param scrollScale the scroll scale
	 */
	public abstract void scrollBehindTo(int x, int y, 
			CustomViewBehind cvb, float scrollScale);
	
	/**
	 * Gets the menu left.
	 *
	 * @param cvb the cvb
	 * @param content the content
	 * @return the menu left
	 */
	public abstract int getMenuLeft(CustomViewBehind cvb, View content);
	
	/**
	 * Gets the abs left bound.
	 *
	 * @param cvb the cvb
	 * @param content the content
	 * @return the abs left bound
	 */
	public abstract int getAbsLeftBound(CustomViewBehind cvb, View content);

	/**
	 * Gets the abs right bound.
	 *
	 * @param cvb the cvb
	 * @param content the content
	 * @return the abs right bound
	 */
	public abstract int getAbsRightBound(CustomViewBehind cvb, View content);

	/**
	 * Margin touch allowed.
	 *
	 * @param content the content
	 * @param x the x
	 * @param threshold the threshold
	 * @return true, if successful
	 */
	public abstract boolean marginTouchAllowed(View content, int x, int threshold);
	
	/**
	 * Menu open touch allowed.
	 *
	 * @param content the content
	 * @param currPage the curr page
	 * @param x the x
	 * @return true, if successful
	 */
	public abstract boolean menuOpenTouchAllowed(View content, int currPage, int x);
	
	/**
	 * Menu touch in quick return.
	 *
	 * @param content the content
	 * @param currPage the curr page
	 * @param x the x
	 * @return true, if successful
	 */
	public abstract boolean menuTouchInQuickReturn(View content, int currPage, int x);
	
	/**
	 * Menu closed slide allowed.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public abstract boolean menuClosedSlideAllowed(int x);
	
	/**
	 * Menu open slide allowed.
	 *
	 * @param x the x
	 * @return true, if successful
	 */
	public abstract boolean menuOpenSlideAllowed(int x);
	
	/**
	 * Draw shadow.
	 *
	 * @param canvas the canvas
	 * @param shadow the shadow
	 * @param width the width
	 */
	public abstract void drawShadow(Canvas canvas, Drawable shadow, int width);
	
	/**
	 * Draw fade.
	 *
	 * @param canvas the canvas
	 * @param alpha the alpha
	 * @param cvb the cvb
	 * @param content the content
	 */
	public abstract void drawFade(Canvas canvas, int alpha, 
			CustomViewBehind cvb, View content);
	
	/**
	 * Draw selector.
	 *
	 * @param content the content
	 * @param canvas the canvas
	 * @param percentOpen the percent open
	 */
	public abstract void drawSelector(View content, Canvas canvas, float percentOpen);
	
}
