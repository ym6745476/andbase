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
package com.ab.http;

import android.content.Context;

/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbHttpUtil.java 
 * 描述：Http执行工具类，可处理get，post，以及异步处理文件的上传下载
 * @author zhaoqp
 * @date：2013-10-22 下午4:15:52
 * @version v1.0
 */
public class AbHttpUtil {
	
	/** The m context. */
	private Context mContext;

	/**实例话对象*/
	private AbHttpClient client = null;
	
	/** The m sqlite storage. */
	private static AbHttpUtil mAbHttpUtil = null;
	
	/**
	 * 描述：获取实例.
	 *
	 * @param context the context
	 * @return single instance of AbHttpUtil
	 */
	public static AbHttpUtil getInstance(Context context){
	    if (null == mAbHttpUtil){
	    	mAbHttpUtil = new AbHttpUtil(context);
	    }
	    
	    return mAbHttpUtil;
	}
	
	private AbHttpUtil(Context context) {
		super();
		this.mContext = context;
		this.client = new AbHttpClient(this.mContext);
	}
	

	/**
	 * 
	 * 描述：无参数的get请求
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public void get(String url, AbHttpResponseListener responseListener) {
		client.get(url, responseListener);
	}

	/**
	 * 
	 * 描述：带参数的get请求
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public void get(String url, AbRequestParams params,
			AbHttpResponseListener responseListener) {
		client.get(url, params, responseListener);
	}
	
	/**
	 *  
	 * 描述：下载数据使用，会返回byte数据(下载文件或图片)
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public void get(String url, AbBinaryHttpResponseListener responseListener) {
		client.get(url, responseListener);
	}
	
	/**
	 * 
	 * 描述：文件下载的get
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public void get(String url, AbRequestParams params,
			AbFileHttpResponseListener responseListener) {
		client.get(url, params, responseListener);
	}
	
	
	/**
	 * 
	 * 描述：无参数的post请求
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public void post(String url, AbHttpResponseListener responseListener) {
		client.post(url, responseListener);
	}
	
	
	/**
	 * 
	 * 描述：带参数的post请求
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public void post(String url, AbRequestParams params,
			AbHttpResponseListener responseListener) {
		client.post(url, params, responseListener);
	}
	
	
	/**
	 * 
	 * 描述：文件下载的post
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public void post(String url, AbRequestParams params,
			AbFileHttpResponseListener responseListener) {
		client.post(url, params, responseListener);
	}
	
	/**
	 * 
	 * 描述：设置连接超时时间
	 * @param timeout 毫秒
	 * @throws 
	 */
	public void setTimeout(int timeout) {
		client.setTimeout(timeout);
	}
	
	/**
	 * 
	 * 描述：设置调试模式
	 * @param debug 开关
	 * @throws 
	 */
	public void setDebug(boolean debug) {
		client.setDebug(debug);
	}
	
}
