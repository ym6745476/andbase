package com.ab.http;

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
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

//TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn 
 * 名称：AbRequestParams.java 
 * 描述：Http请求参数
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午10:28:55
 */
public class AbRequestParams {

	/** url参数. */
	protected ConcurrentHashMap<String, String> urlParams;
	/** 文件参数. */
	protected ConcurrentHashMap<String, ContentBody> fileParams;
	private MultipartEntity multiPart = null;
	private final static int boundaryLength = 32;
	private final static String boundaryAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_";
	private String boundary;

	/**
	 * auto generate boundary string
	 * 
	 * @return a boundary string
	 */
	private String getBoundary() {
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < boundaryLength; ++i)
			sb.append(boundaryAlphabet.charAt(random.nextInt(boundaryAlphabet
					.length())));
		return sb.toString();
	}

	/**
	 * @return get MultipartEntity (apache)
	 */
	public MultipartEntity getMultiPart() {
		return multiPart;
	}

	/**
	 * default boundary is auto generate {@link #getBoundary()}
	 */
	public AbRequestParams() {
		super();
		boundary = getBoundary();
		urlParams = new ConcurrentHashMap<String, String>();
		fileParams = new ConcurrentHashMap<String, ContentBody>();
		multiPart = new MultipartEntity(HttpMultipartMode.STRICT, boundary,
				Charset.forName("UTF-8"));
	}

	/**
	 * @return multipart boundary string
	 */
	public String boundaryString() {
		return boundary;
	}

	/**
	 * 添加一个文件参数
	 * 
	 * @param attr
	 *            属性名
	 * @param file
	 *            文件
	 */
	public void put(String attr, File file) {
		if (attr != null && file != null) {
			fileParams.put(attr, new FileBody(file));
		}

	}

	/**
	 * 添加一个byte[]参数
	 * 
	 * @param attr
	 *            属性名
	 * @param fileName
	 *            文件名
	 * @param data
	 *            字节数组
	 */
	public void put(String attr, String fileName, byte[] data) {
		if (attr != null && fileName != null) {
			fileParams.put(attr, new ByteArrayBody(data, fileName));
		}
	}

	/**
	 * 添加一个String参数
	 * 
	 * @param attr
	 * @param str
	 */
	public void put(String attr, String value) {
		try {
			if (attr != null && value != null) {
				urlParams.put(attr, value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取参数字符串.
	 * 
	 * @return the param string
	 */
	public String getParamString() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		return URLEncodedUtils.format(paramsList, HTTP.UTF_8);
	}

	/**
	 * 获取参数列表.
	 * 
	 * @return the params list
	 */
	public List<BasicNameValuePair> getParamsList() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, String> entry : urlParams
				.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		return paramsList;
	}

	/**
	 * 
	 * 获取HttpEntity.
	 */
	public HttpEntity getEntity() {

		if (fileParams.isEmpty()) {
			// 不包含文件的
			return createFormEntity();
		} else {
			// 包含文件和参数的
			return createMultipartEntity();
		}
	}

	/**
	 * 创建HttpEntity.
	 * 
	 * @return the http entity
	 */
	public HttpEntity createFormEntity() {
		try {
			return new UrlEncodedFormEntity(getParamsList(), HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 描述：创建文件域HttpEntity.
	 * 
	 * @return
	 * @throws IOException
	 */
	private HttpEntity createMultipartEntity() {

		try {
			// Add string params
			for (ConcurrentHashMap.Entry<String, String> entry : urlParams
					.entrySet()) {
				multiPart.addPart(
						entry.getKey(),
						new StringBody(entry.getValue(), Charset
								.forName("UTF-8")));
			}

			// Add file params
			for (ConcurrentHashMap.Entry<String, ContentBody> entry : fileParams
					.entrySet()) {
				ContentBody contentBody = entry.getValue();
				multiPart.addPart(entry.getKey(), contentBody);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return multiPart;
	}

	/**
	 * 获取url参数.
	 * 
	 * @return the url params
	 */
	public ConcurrentHashMap<String, String> getUrlParams() {
		return urlParams;
	}

	/**
	 * 获取文件参数.
	 * 
	 * @return the file params
	 */
	public ConcurrentHashMap<String, ContentBody> getFileParams() {
		return fileParams;
	}

}
