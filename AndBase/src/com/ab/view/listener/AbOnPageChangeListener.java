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
package com.ab.view.listener;

import android.support.v4.view.ViewPager;


// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbOnPageChangeListener.java 
 * 描述：页面改变的监听器
 * @author zhaoqp
 * @date：2013-11-25 下午3:47:30
 * @version v1.0
 */
public interface AbOnPageChangeListener extends ViewPager.OnPageChangeListener {
    
    /**
     * Sets the view pager.
     *
     * @param view the new view pager
     */
    public void setViewPager(ViewPager view);

    
    /**
     * Sets the view pager.
     *
     * @param view the view
     * @param initialPosition the initial position
     */
    public void setViewPager(ViewPager view, int initialPosition);

    
    /**
     * Sets the current item.
     *
     * @param item the new current item
     */
    public void setCurrentItem(int item);

   
    /**
     * Sets the on page change listener.
     *
     * @param listener the new on page change listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener);

   
    /**
     * Notify data set changed.
     */
    public void notifyDataSetChanged();
}
