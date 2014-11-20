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

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbBinaryHttpResponseListener.java 
 * 描述：Http二进制响应监听器
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public abstract class AbBinaryHttpResponseListener extends AbHttpResponseListener{
	
    /**
     * 构造.
     */
	public AbBinaryHttpResponseListener() {
		super();
	}
	
	/**
	 * 描述：获取数据成功会调用这里.
	 *
	 * @param statusCode the status code
	 * @param content the content
	 */
    public abstract void onSuccess(int statusCode,byte[] content);
    

	/**
     * 成功消息.
     *
     * @param statusCode the status code
     * @param content the content
     */
    public void sendSuccessMessage(int statusCode,byte[] content){
    	sendMessage(obtainMessage(AbHttpClient.SUCCESS_MESSAGE, new Object[]{statusCode, content}));
    }
    

}
