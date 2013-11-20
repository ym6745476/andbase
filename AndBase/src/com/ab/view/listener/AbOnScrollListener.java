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


// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbOnScrollListener.java 
 * 描述：滚动监听器
 * @author zhaoqp
 * @date：2013-11-20 下午3:11:11
 * @version v1.0
 */
public interface AbOnScrollListener {
    
    /**
     * 滚动.
     * @param arg1 返回参数
     */
    public void onScroll(int arg1); 
    
    /**
	 * 滚动停止.
	 */
    public void onScrollStoped();

	/**
	 * 滚到了最左边.
	 */
    public void onScrollToLeft();

	/**
	 * 滚到了最右边.
	 */
    public void onScrollToRight();

}
