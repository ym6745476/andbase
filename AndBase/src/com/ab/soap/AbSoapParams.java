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
package com.ab.soap;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.message.BasicNameValuePair;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn 
 * 名称：AbSoapParams.java 
 * 描述：Soap请求参数
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-10-27 下午15:28:55
 */
public class AbSoapParams {

	/** 参数. */
	protected ConcurrentHashMap<String, String> params;

	/**
	 * Instantiates a new ab soap params.
	 */
	public AbSoapParams() {
		init();
	}

	/**
	 * Instantiates a new ab soap params.
	 * 
	 * @param obj
	 *            the obj
	 */
	public AbSoapParams(Object obj) {
		try {
			init();
			Class<?> clazz = obj.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				field.setAccessible(true);
				String fieldValue = (String) field.get(obj);
				params.put(fieldName, fieldValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 用一个map构造请求参数.
	 * 
	 * @param source
	 *            the source
	 */
	public AbSoapParams(Map<String, String> source) {
		init();

		for (Map.Entry<String, String> entry : source.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	/**
	 * 初始化.
	 */
	private void init() {
		params = new ConcurrentHashMap<String, String>();
	}

	/**
	 * 增加一对请求参数.
	 * 
	 * @param key
	 *            the key
	 * @param value
	 *            the value
	 */
	public void put(String key, String value) {
		if (key != null && value != null) {
			params.put(key, value);
		}
	}

	/**
	 * 删除一个请求参数.
	 * 
	 * @param key
	 *            the key
	 */
	public void remove(String key) {
		params.remove(key);
	}

	/**
	 * 获取参数列表.
	 * 
	 * @return the params list
	 */
	public List<BasicNameValuePair> getParamsList() {
		List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
		for (ConcurrentHashMap.Entry<String, String> entry : params.entrySet()) {
			paramsList.add(new BasicNameValuePair(entry.getKey(), entry
					.getValue()));
		}
		return paramsList;
	}
}
