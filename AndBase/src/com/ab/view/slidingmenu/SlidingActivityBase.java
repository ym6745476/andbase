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

import android.view.View;
import android.view.ViewGroup.LayoutParams;

// TODO: Auto-generated Javadoc
/**
 * The Interface SlidingActivityBase.
 */
public interface SlidingActivityBase {
	
	/**
	 * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
	 * It can itself be a complex view hierarchy.
	 *
	 * @param view The desired content to display.
	 * @param layoutParams Layout parameters for the view.
	 */
	public void setBehindContentView(View view, LayoutParams layoutParams);

	/**
	 * Set the behind view content to an explicit view. This view is placed directly into the behind view 's view hierarchy.
	 * It can itself be a complex view hierarchy. When calling this method, the layout parameters of the specified
	 * view are ignored. Both the width and the height of the view are set by default to MATCH_PARENT. To use your
	 * own layout parameters, invoke setContentView(android.view.View, android.view.ViewGroup.LayoutParams) instead.
	 *
	 * @param view The desired content to display.
	 */
	public void setBehindContentView(View view);

	/**
	 * Set the behind view content from a layout resource. The resource will be inflated, adding all top-level views
	 * to the behind view.
	 *
	 * @param layoutResID Resource ID to be inflated.
	 */
	public void setBehindContentView(int layoutResID);

	/**
	 * Gets the SlidingMenu associated with this activity.
	 *
	 * @return the SlidingMenu associated with this activity.
	 */
	public SlidingMenu getSlidingMenu();
		
	/**
	 * Toggle the SlidingMenu. If it is open, it will be closed, and vice versa.
	 */
	public void toggle();
	
	/**
	 * Close the SlidingMenu and show the content view.
	 */
	public void showContent();
	
	/**
	 * Open the SlidingMenu and show the menu view.
	 */
	public void showMenu();

	/**
	 * Open the SlidingMenu and show the secondary (right) menu view. Will default to the regular menu
	 * if there is only one.
	 */
	public void showSecondaryMenu();
	
	/**
	 * Controls whether the ActionBar slides along with the above view when the menu is opened,
	 * or if it stays in place.
	 *
	 * @param slidingActionBarEnabled True if you want the ActionBar to slide along with the SlidingMenu,
	 * false if you want the ActionBar to stay in place
	 */
	public void setSlidingActionBarEnabled(boolean slidingActionBarEnabled);
	
}
