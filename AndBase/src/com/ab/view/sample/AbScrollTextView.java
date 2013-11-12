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
package com.ab.view.sample;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

// TODO: Auto-generated Javadoc
/**
 * The Class AbScrollTextView.
 */
public class AbScrollTextView extends TextView {

	/**
	 * Instantiates a new ab scroll text view.
	 *
	 * @param context the context
	 */
	public AbScrollTextView(Context context) {
		super(context);
	}

	/**
	 * Instantiates a new ab scroll text view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbScrollTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Instantiates a new ab scroll text view.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 * @param defStyle the def style
	 */
	public AbScrollTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 描述：TODO.
	 *
	 * @return true, if is focused
	 * @see android.view.View#isFocused()
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:47
	 * @version v1.0
	 */
	@Override
	public boolean isFocused() {
		return true;
	}

}
