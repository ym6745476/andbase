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
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.ScrollView;
// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbOuterScrollView.java 
 * 描述：这个ScrollView与内部的滑动不冲突
 * @author zhaoqp
 * @date：2013-5-21 下午3:59:38
 * @version v1.0
 */
public class AbOuterScrollView extends ScrollView {

    /** The m gesture detector. */
    private GestureDetector mGestureDetector;

    /**
     * Instantiates a new ab pager scroll view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbOuterScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mGestureDetector = new GestureDetector(new YScrollDetector());
        setFadingEdgeLength(0);
    }

    /**
     * 描述：TODO.
     *
     * @param ev the ev
     * @return true, if successful
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev) && mGestureDetector.onTouchEvent(ev);
    }
    
    /**
     * The Class YScrollDetector.
     */
    class YScrollDetector extends SimpleOnGestureListener {
        
        /**
         * 描述：TODO.
         *
         * @param e1 the e1
         * @param e2 the e2
         * @param distanceX the distance x
         * @param distanceY the distance y
         * @return true, if successful
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(distanceY!=0&&distanceX!=0){
                    
            }
            if(Math.abs(distanceY) >= Math.abs(distanceX)) {
                return true;
            }
            return false;
        }
    }
    
    
}
