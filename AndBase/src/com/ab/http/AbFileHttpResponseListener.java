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

import android.content.Context;

import com.ab.util.AbFileUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbBinaryHttpResponseListener.java 
 * 描述：Http文件响应监听器
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public abstract class AbFileHttpResponseListener extends AbHttpResponseListener{
	
    /** 当前缓存文件. */
    private File mFile;
    
    /**
     * 下载文件的构造,用默认的缓存方式.
     *
     * @param url the url
     */
	public AbFileHttpResponseListener(String url) {
		super();
	}
	
	/**
	 * 默认的构造.
	 */
	public AbFileHttpResponseListener() {
		super();
	}
	
	/**
     * 下载文件的构造,指定缓存文件名称.
     * @param file 缓存文件名称
     */
    public AbFileHttpResponseListener(File file) {
        super();
	    this.mFile = file;
    }
	
	/**
	 * 描述：下载文件成功会调用这里.
	 *
	 * @param statusCode the status code
	 * @param file the file
	 */
    public void onSuccess(int statusCode,File file){};
    
    /**
     * 描述：多文件上传成功调用.
     *
     * @param statusCode the status code
     */
    public void onSuccess(int statusCode){};
    
   
   /**
    * 成功消息.
    *
    * @param statusCode the status code
    */
    public void sendSuccessMessage(int statusCode){
    	sendMessage(obtainMessage(AbHttpClient.SUCCESS_MESSAGE, new Object[]{statusCode}));
    }
    
    /**
     * 失败消息.
     *
     * @param statusCode the status code
     * @param error the error
     */
    public void sendFailureMessage(int statusCode,Throwable error){
    	sendMessage(obtainMessage(AbHttpClient.FAILURE_MESSAGE, new Object[]{statusCode, error}));
    }
    

	/**
	 * Gets the file.
	 *
	 * @return the file
	 */
	public File getFile() {
		return mFile;
	}

	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 */
	public void setFile(File file) {
		this.mFile = file;
		try {
			if(!file.getParentFile().exists()){
			      file.getParentFile().mkdirs();
			}
			if(!file.exists()){
			      file.createNewFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Sets the file.
	 *
	 * @param context the context
	 * @param name the name
	 */
	public void setFile(Context context,String name) {
		//生成缓存文件
        if(AbFileUtil.isCanUseSD()){
	    	File file = new File(AbFileUtil.getFileDownloadDir(context) + name);
	    	setFile(file);
        }
	}
    
}
