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
package com.ab.task;

// TODO: Auto-generated Javadoc
/**
 * 描述：数据执行单位.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbTaskItem { 
	
	/** 记录的当前索引. */
	public int position;
	 
 	/** 执行完成的回调接口. */
    public AbTaskListener listener; 
    
    /** 执行完成的结果. */
    private Object result;

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public AbTaskListener getListener() {
		return listener;
	}

	public void setListener(AbTaskListener listener) {
		this.listener = listener;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	} 
    
} 

