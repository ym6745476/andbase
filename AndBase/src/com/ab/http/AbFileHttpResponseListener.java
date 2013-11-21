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

import java.io.File;
import java.io.IOException;

import android.os.Environment;

import com.ab.util.AbFileUtil;



// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbBinaryHttpResponseListener.java 
 * 描述：Http文件响应监听器
 * @author zhaoqp
 * @date：2013-11-13 上午9:00:52
 * @version v1.0
 */
public class AbFileHttpResponseListener extends AbHttpResponseListener{
	
	/** The Constant TAG. */
    private static final String TAG = "AbFileHttpResponseListener";
    
    /** 当前缓存文件. */
    private File mFile;
    
    /**
     * 下载文件的构造,用默认的缓存方式
     * @param url
     */
	public AbFileHttpResponseListener(String url) {
		super();
	}
	
	/**
     * 默认的构造
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
	 */
    public void onSuccess(int statusCode,File file) {};
    
    /**
	 * 描述：多文件上传成功调用.
	 */
    public void onSuccess(int statusCode) {};
    
    
    /**
	 * 描述：文件上传下载失败调用.
	 */
    public void onFailure(int statusCode, String content,Throwable error) {}
   
   /**
     * 成功消息.
     */
    public void sendSuccessMessage(int statusCode){
    	sendMessage(obtainMessage(SUCCESS_MESSAGE, new Object[]{statusCode}));
    }
    
    /**
     * 失败消息.
     */
    public void sendFailureMessage(int statusCode,Throwable error){
    	sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{statusCode, error}));
    }
    

	public File getFile() {
		return mFile;
	}

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
	
	public void setFile(String name) {
		//生成缓存文件
        if(AbFileUtil.isCanUseSD()){
	        File path = Environment.getExternalStorageDirectory();
	    	File file = new File(path.getAbsolutePath() + AbFileUtil.getDownPathFileDir()+name);
	    	setFile(file);
        }
	}
    
}
