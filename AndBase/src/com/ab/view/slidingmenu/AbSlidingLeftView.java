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
package com.ab.view.slidingmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * ÃèÊö£º²à±ßÀ¸×ó±ßÊµÏÖ.
 *
 * @author zhaoqp
 * @date£º2013-4-24 ÏÂÎç3:46:47
 * @version v1.0
 */
public class AbSlidingLeftView {
	
	/** The m context. */
	private Context mContext;
	
	/** The m left view. */
	private View mLeftView;

	/** The m on change view listener. */
	private OnChangeViewListener mOnChangeViewListener;

	/**
	 * Instantiates a new ab flipper left view.
	 *
	 * @param context the context
	 * @param res the res
	 */
	public AbSlidingLeftView(Context context, int res) {
		mContext = context;
		mLeftView = LayoutInflater.from(context).inflate(res, null);
	}

	/**
	 * Gets the view.
	 *
	 * @return the view
	 */
	public View getView() {
		return mLeftView;
	}

	/**
	 * The listener interface for receiving onChangeView events.
	 * The class that is interested in processing a onChangeView
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnChangeViewListener<code> method. When
	 * the onChangeView event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnChangeViewEvent
	 */
	public interface OnChangeViewListener {
		
		/**
		 * On change view.
		 *
		 * @param arg0 the arg0
		 */
		public abstract void onChangeView(int arg0);
	}

	/**
	 * Sets the on change view listener.
	 *
	 * @param onChangeViewListener the new on change view listener
	 */
	public void setOnChangeViewListener(OnChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}
}
