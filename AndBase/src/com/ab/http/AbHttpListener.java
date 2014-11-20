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
package com.ab.http;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbHttpListener.java 
 * 描述：Http响应监听器
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-08-05 上午9:00:52
 */
public abstract class AbHttpListener{
	
    /**
     * 构造.
     */
	public AbHttpListener() {
		super();
	}

	/**
	 * 请求成功.
	 *
	 * @param content the content
	 */
    public void onSuccess(String content){};
    
    /**
	 * 请求成功.
	 *
	 * @param list the list
	 */
    public void onSuccess(List<?> list){};
    
    /**
     * 请求失败.
     * @param content the content
     */
    public abstract void onFailure(String content);
    
    
    /**
	 * 描述：获取数据开始.
	 */
    public void onStart(){};
    
    
    /**
	 * 完成后调用，失败，成功，调用.
	 */
    public void onFinish(){};

}
