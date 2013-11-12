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

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskPool;
import com.ab.util.AbStrUtil;
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

	/**实例话对象*/
	private static AsyncHttpClient client = new AsyncHttpClient();
	
	/**超时时间*/
	private static int timeout = 5000;
	
	static{
		client.setTimeout(timeout);
	}
	
	/**
	 * 描述：获取AsyncHttpClient.
	 *
	 * @return the http client
	 */
	public static AsyncHttpClient getHttpClient() {
		return client;
	}
	

	public static int getTimeout() {
		return timeout;
	}

	/**
	 * 
	 * 描述：设置链接超时，如果不设置，默认为5s
	 * @param timeout
	 * @throws 
	 */
	public static void setTimeout(int timeout) {
		AbHttpUtil.timeout = timeout;
		client.setTimeout(AbHttpUtil.timeout);
	}


	/**
	 * 
	 * 描述：无参数的get请求
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, AsyncHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}

	/**
	 * 
	 * 描述：带参数的get请求
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}
	
	/**
	 *  
	 * 描述：下载数据使用，会返回byte数据(下载文件或图片)
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, BinaryHttpResponseHandler responseHandler) {
		client.get(url, responseHandler);
	}
	
	/**
	 * 
	 * 描述：文件下载的get
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void get(String url, RequestParams params,
			FileAsyncHttpResponseHandler responseHandler) {
		client.get(url, params, responseHandler);
	}
	
	
	/**
	 * 
	 * 描述：无参数的post请求
	 * @param url
	 * @param responseHandler
	 * @throws 
	 */
	public static void post(String url, AsyncHttpResponseHandler responseHandler) {
		client.post(url, responseHandler);
	}
	
	
	/**
	 * 
	 * 描述：带参数的post请求
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
	
	
	
	/**
	 * 
	 * 描述：文件下载的post
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public static void post(String url, RequestParams params,
			FileAsyncHttpResponseHandler responseHandler) {
		client.post(url, params, responseHandler);
	}
	
	
	/**
	 * HTTP一个/多个文件上传.
	 *
	 * @param url 要使用的URL
	 * @param params 表单参数
	 * @param files 要上传的文件列表（key:文件名，value：File对象）
	 */
	public static void post(final String url, final HashMap<String, String> params,
			final HashMap<String, File> files,final AsyncHttpResponseHandler responseHandler) {
		
		AbTaskItem item = new AbTaskItem();
		item.callback = new AbTaskCallback() {
			
			@Override
			public void update() {
				//发送完成消息
				if (responseHandler != null) {
					 responseHandler.sendFinishMessage();
				}
			}
			
			@Override
			public void get() {
				//标识每个文件的边界
				String BOUNDARY = java.util.UUID.randomUUID().toString();
				String PREFIX = "--";
				String LINEND = "\r\n";
				String MULTIPART_FROM_DATA = "multipart/form-data";
				String CHARSET = "UTF-8";
				HttpURLConnection conn = null;
				DataOutputStream outStream = null;
				try {
					//发送开始执行
					if (responseHandler != null) {
					   responseHandler.sendStartMessage();
					}
					URL uri = new URL(url);
					conn = (HttpURLConnection) uri.openConnection();
					//允许输入
					conn.setDoInput(true);
					//允许输出
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					// Post方式
					conn.setRequestMethod("POST");
					//设置request header 属性
					conn.setRequestProperty("connection", "keep-alive");
					conn.setRequestProperty("Charsert", "UTF-8");
					conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
							+ ";boundary=" + BOUNDARY);
					//组装表单参数数据
					StringBuilder sb = new StringBuilder();
					for (Map.Entry<String, String> entry : params.entrySet()) {
						sb.append(PREFIX);
						sb.append(BOUNDARY);
						sb.append(LINEND);
						sb.append("Content-Disposition: form-data; name=\""
								+ entry.getKey() + "\"" + LINEND);
						sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
						sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
						sb.append(LINEND);
						sb.append(entry.getValue());
						sb.append(LINEND);
					}
					//获取连接发送参数数据
					outStream = new DataOutputStream(conn.getOutputStream());
					outStream.write(sb.toString().getBytes());
					// 发送文件数据
					if (files != null)
						for (Map.Entry<String, File> file : files.entrySet()) {
							StringBuilder sb1 = new StringBuilder();
							sb1.append(PREFIX);
							sb1.append(BOUNDARY);
							sb1.append(LINEND);
							sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\""
									+ file.getKey() + "\"" + LINEND);
							sb1.append("Content-Type: application/octet-stream; charset="
									+ CHARSET + LINEND);
							sb1.append(LINEND);
							//请求头结束至少有一个空行（即有两对\r\n）表示请求头结束了
							Log.d("TAG", "request start:"+sb1.toString());
							outStream.write(sb1.toString().getBytes());
							InputStream is = new FileInputStream(file.getValue());
							byte[] buffer = new byte[1024];
							int len = 0;
							while ((len = is.read(buffer)) != -1) {
								outStream.write(buffer, 0, len);
							}
							is.close();
							//一个文件结束一个空行
							outStream.write(LINEND.getBytes());
						}
						//请求结束的边界打印
						byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
						Log.d("TAG","request end:"+ new String(end_data).toString());
						outStream.write(end_data);
						outStream.flush();
						outStream.close();
						// 获取响应码
						int statusCode = conn.getResponseCode();
						String result = AbStrUtil.convertStreamToString(conn.getInputStream());
						//发送结果消息
						if (responseHandler != null) {
							if(statusCode == 200){
								responseHandler.sendSuccessMessage(statusCode, null, result.getBytes());
							}else{
								responseHandler.sendFailureMessage(statusCode, null, result.getBytes(), null);
							}
						}
						
				} catch (Exception e) {
					e.printStackTrace();
					//发送失败消息
					if (responseHandler != null) {
		                responseHandler.sendFailureMessage(0, null, null, e);
		            }
				} finally{
					if(conn!=null){
						conn.disconnect();
					}
					
				}
			}
		};

		//负责调度与线程的类
		AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
		mAbTaskPool.execute(item);
		
	}

	
}
