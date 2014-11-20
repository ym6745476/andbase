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

package com.ab.view.wheel;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbNumericWheelAdapter.java 
 * 描述：轮子适配器（数字）
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */

public class AbNumericWheelAdapter implements AbWheelAdapter {

	/** The default min value. */
	public static final int DEFAULT_MAX_VALUE = 9;

	/** The default max value. */
	private static final int DEFAULT_MIN_VALUE = 0;

	// Values
	/** The min value. */
	private int minValue;

	/** The max value. */
	private int maxValue;

	// format
	/** The format. */
	private String format;

	/**
	 * Default constructor.
	 */
	public AbNumericWheelAdapter() {
		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
	}

	/**
	 * Constructor.
	 *
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public AbNumericWheelAdapter(int minValue, int maxValue) {
		this(minValue, maxValue, null);
	}

	/**
	 * Constructor.
	 *
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format the format string
	 */
	public AbNumericWheelAdapter(int minValue, int maxValue, String format) {
		this.minValue = minValue;
		this.maxValue = maxValue;
		this.format = format;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param index the index
	 * @return the item
	 * @see com.ab.view.wheel.AbWheelAdapter#getItem(int)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:51
	 */
	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return format != null ? String.format(format, value) : Integer.toString(value);
		}
		return null;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @return the items count
	 * @see com.ab.view.wheel.AbWheelAdapter#getItemsCount()
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:51
	 */
	@Override
	public int getItemsCount() {
		return maxValue - minValue + 1;
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @return the maximum length
	 * @see com.ab.view.wheel.AbWheelAdapter#getMaximumLength()
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:51
	 */
	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(maxValue), Math.abs(minValue));
		int maxLen = Integer.toString(max).length();
		if (minValue < 0) {
			maxLen++;
		}
		return maxLen;
	}
}
