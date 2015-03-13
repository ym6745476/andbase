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

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

// TODO: Auto-generated Javadoc
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
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    
    /** 流常量. */
    private static final String APPLICATION_OCTET_STREAM = "application/octet-stream";

    /**
     * 构造一个空的请求参数.
     */
    public AbRequestParams() {
        init();
    }

    /**
     * 用一个map构造请求参数.
     *
     * @param source the source
     */
    public AbRequestParams(Map<String, String> source) {
        init();

        for (Map.Entry<String, String> entry : source.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    /**
     * 用一个key和value构造请求参数.
     *
     * @param key the key
     * @param value the value
     */
    public AbRequestParams(String key, String value) {
        init();
        put(key, value);
    }
    
    /**
     * 初始化.
     */
    private void init() {
        urlParams = new ConcurrentHashMap<String, String>();
        fileParams = new ConcurrentHashMap<String, FileWrapper>();
    }


    /**
     * 增加一对请求参数.
     *
     * @param key the key
     * @param value the value
     */
    public void put(String key, String value) {
        if (key != null && value != null) {
        	urlParams.put(encode(key), encode(value));
        }
    }
    
    /**
     * 增加一个文件域.
     *
     * @param key the key
     * @param file the file
     * @param contentType the content type of the file, eg. application/json
     */
    public void put(String key, File file,String contentType) {
        if (key != null && file != null) {
        	fileParams.put(encode(key), new FileWrapper(file,contentType));
        }
    }
    
    /**
     * 增加一个文件域.
     *
     * @param key the key
     * @param file the file
     */
    public void put(String key, File file) {
    	put(encode(key),file,APPLICATION_OCTET_STREAM);
    }
    
    
    /**
     * 删除一个请求参数.
     *
     * @param key the key
     */
    public void remove(String key) {
        urlParams.remove(encode(key));
        fileParams.remove(encode(key));
    }

    /**
     * 描述：转换为参数字符串.
     *
     * @return the string
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            if (result.length() > 0)
                result.append("&");

            result.append(decode(entry.getKey()));
            result.append("=");
            result.append(decode(entry.getValue()));
        }

        return result.toString();
    }

    /**
     * 获取参数列表.
     * @return the params list
     */
    public List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> paramsList = new LinkedList<BasicNameValuePair>();
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
        	paramsList.add(new BasicNameValuePair(decode(entry.getKey()), decode(entry.getValue())));
        }
        return paramsList;
    }

    
    /**
     * 获取参数字符串.
     * @return the param string
     */
    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), HTTP.UTF_8);
    }
    
   /**
    * 获取HttpEntity.
    *
    * @param responseListener the response listener
    * @return the entity
    * @throws IOException Signals that an I/O exception has occurred.
    */
    public HttpEntity getEntity(AbHttpResponseListener responseListener) throws IOException {
        
    	//不包含文件的
    	if (fileParams.isEmpty()) {
            return createFormEntity();
        } else {
        	//包含文件和参数的
            return createMultipartEntity(responseListener);
        }
    }
    
    /**
     * 创建HttpEntity.
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
     * @param responseListener the response listener
     * @return the http entity
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private HttpEntity createMultipartEntity(AbHttpResponseListener responseListener) throws IOException {
    	AbMultipartEntity entity = new AbMultipartEntity(responseListener);

        // Add string params
        for (ConcurrentHashMap.Entry<String, String> entry : urlParams.entrySet()) {
            entity.addPart(entry.getKey(), entry.getValue());
        }

        // Add file params
        for (ConcurrentHashMap.Entry<String, FileWrapper> entry : fileParams.entrySet()) {
            FileWrapper fileWrapper = entry.getValue();
            entity.addPart(entry.getKey(), fileWrapper.file, fileWrapper.contentType);
        }

        return entity;
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
	 * Gets the file params.
	 *
	 * @return the file params
	 */
	public ConcurrentHashMap<String, FileWrapper> getFileParams() {
		return fileParams;
	}

	/**
	 * Sets the file params.
	 *
	 * @param fileParams the file params
	 */
	public void setFileParams(ConcurrentHashMap<String, FileWrapper> fileParams) {
		this.fileParams = fileParams;
	}

	/**
	 * 设置url参数.
	 *
	 * @param urlParams the url params
	 */
	public void setUrlParams(ConcurrentHashMap<String, String> urlParams) {
		this.urlParams = urlParams;
	}
	
	/**
	 * 
	 * 描述：参数转换.
	 * @param s
	 * @return
	 */
	public String encode(String s){
		try {
			return URLEncoder.encode(s,HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 
	 * 描述：参数转换.
	 * @param s
	 * @return
	 */
	public String decode(String s){
		try {
			return URLDecoder.decode(s,HTTP.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}
	
	
	/**
     * 文件类.
     */
    private static class FileWrapper {
        
        /** 文件. */
        public File file;
        
        /** 类型. */
        public String contentType;

        /**
         * 构造.
         * @param file the file
         * @param contentType the content type
         */
        public FileWrapper(File file, String contentType) {
            this.file = file;
            this.contentType = contentType;
        }
    }
    
    
}
