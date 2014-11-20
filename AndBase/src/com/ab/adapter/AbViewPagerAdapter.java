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
package com.ab.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbViewPagerAdapter.java 
 * 描述：一个通用的ViewPager适配器
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-28 上午10:58:26
 */
public class AbViewPagerAdapter extends PagerAdapter{
	
	/** The m context. */
	private Context mContext;
	
	/** The m list views. */
	private ArrayList<View> mListViews = null;
	
	/** The m views. */
	private HashMap <Integer,View> mViews = null;

	/**
	 * Instantiates a new ab view pager adapter.
	 * @param context the context
	 * @param mListViews the m list views
	 */
	public AbViewPagerAdapter(Context context,ArrayList<View> mListViews) {
		this.mContext = context;
		this.mListViews = mListViews;
		this.mViews = new HashMap <Integer,View>();
	}

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see android.support.v4.view.PagerAdapter#getCount()
	 */
	@Override
	public int getCount() {
		return mListViews.size();
	}

	/**
	 * 描述：Object是否对应这个View.
	 *
	 * @param arg0 the arg0
	 * @param arg1 the arg1
	 * @return true, if is view from object
	 * @see android.support.v4.view.PagerAdapter#isViewFromObject(android.view.View, java.lang.Object)
	 */
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	/**
	 * 描述：显示View.
	 *
	 * @param container the container
	 * @param position the position
	 * @return the object
	 * @see android.support.v4.view.PagerAdapter#instantiateItem(android.view.View, int)
	 */
	@Override
	public Object instantiateItem(View container, int position) {
		View v = mListViews.get(position);
		((ViewPager) container).addView(v);
		return v;
	}

	/**
	 * 描述：移除View.
	 *
	 * @param container the container
	 * @param position the position
	 * @param object the object
	 * @see android.support.v4.view.PagerAdapter#destroyItem(android.view.View, int, java.lang.Object)
	 */
	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager) container).removeView((View)object);
	}
	
	/**
	 * 描述：很重要，否则不能notifyDataSetChanged.
	 *
	 * @param object the object
	 * @return the item position
	 * @see android.support.v4.view.PagerAdapter#getItemPosition(java.lang.Object)
	 */
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	

}
