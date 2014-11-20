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
package com.ab.task;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbTaskItem.java 
 * 描述：数据执行单位.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-9-2 下午12:52:13
 */
public class AbTaskItem { 
	
	/** 记录的当前索引. */
	private int position;
	 
 	/** 执行完成的回调接口. */
    private AbTaskListener listener; 
    
	/**
	 * Instantiates a new ab task item.
	 */
	public AbTaskItem() {
		super();
	}

	/**
	 * Instantiates a new ab task item.
	 *
	 * @param listener the listener
	 */
	public AbTaskItem(AbTaskListener listener) {
		super();
		this.listener = listener;
	}

	/**
	 * Gets the position.
	 *
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Sets the position.
	 *
	 * @param position the new position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Gets the listener.
	 *
	 * @return the listener
	 */
	public AbTaskListener getListener() {
		return listener;
	}

	/**
	 * Sets the listener.
	 *
	 * @param listener the new listener
	 */
	public void setListener(AbTaskListener listener) {
		this.listener = listener;
	}

} 

