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

import org.apache.http.Header;
import org.apache.http.HttpEntity;

import android.os.Environment;

import com.ab.util.AbFileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


// TODO: Auto-generated Javadoc
/**
 * The Class FileAsyncHttpResponseHandler.
 */
public class FileAsyncHttpResponseHandler extends AsyncHttpResponseHandler {

    /** The m file. */
    private File mFile;

    /**
     * 不指定保存目录.
     *
     * @param url the url
     */
    public FileAsyncHttpResponseHandler(String url) {
        super();
        //生成缓存文件
        if(AbFileUtil.isCanUseSD()){
	        File path = Environment.getExternalStorageDirectory();
			String fileName = AbFileUtil.getFileNameFromUrl(url);
	    	File file = new File(path.getAbsolutePath() + AbFileUtil.getDownPathFileDir()+fileName);
	    	this.mFile = file;
        }
    	
    }
    
    /**
     * 指定保存目录.
     *
     * @param file the file
     */
    public FileAsyncHttpResponseHandler(File file) {
        super();
	    this.mFile = file;
    }

    /**
     * On success.
     *
     * @param file the file
     */
    public void onSuccess(File file) {
    }

    /**
     * On success.
     *
     * @param statusCode the status code
     * @param file the file
     */
    public void onSuccess(int statusCode, File file) {
        onSuccess(file);
    }

    /**
     * On failure.
     *
     * @param e the e
     * @param response the response
     */
    public void onFailure(Throwable e, File response) {
        // By default call lower chain method
        onFailure(e);
    }

    /**
     * On failure.
     *
     * @param statusCode the status code
     * @param e the e
     * @param response the response
     */
    public void onFailure(int statusCode, Throwable e, File response) {
        // By default call lower chain method
        onFailure(e, response);
    }

    /**
     * On failure.
     *
     * @param statusCode the status code
     * @param headers the headers
     * @param e the e
     * @param response the response
     */
    public void onFailure(int statusCode, Header[] headers, Throwable e, File response) {
        // By default call lower chain method
        onFailure(statusCode, e, response);
    }

    /**
     * 描述：TODO
     * @see com.ab.http.AsyncHttpResponseHandler#onFailure(int, org.apache.http.Header[], byte[], java.lang.Throwable)
     * @author: zhaoqp
     * @date：2013-10-22 下午4:23:15
     * @version v1.0
     */
    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
        onFailure(statusCode, headers, error, mFile);
    }

    /**
     * 描述：TODO
     * @see com.ab.http.AsyncHttpResponseHandler#onSuccess(int, org.apache.http.Header[], byte[])
     * @author: zhaoqp
     * @date：2013-10-22 下午4:23:15
     * @version v1.0
     */
    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        onSuccess(statusCode, mFile);
    }

    /**
     * 描述：TODO
     * @see com.ab.http.AsyncHttpResponseHandler#getResponseData(org.apache.http.HttpEntity)
     * @author: zhaoqp
     * @date：2013-10-22 下午4:23:15
     * @version v1.0
     */
    @Override
    byte[] getResponseData(HttpEntity entity) throws IOException {
        if(this.mFile == null || entity == null){
        	return null;
        }
    	
        InputStream instream = entity.getContent();
        long contentLength = entity.getContentLength();
        FileOutputStream buffer = new FileOutputStream(this.mFile);
        if (instream != null) {
	          try {
	              byte[] tmp = new byte[BUFFER_SIZE];
	              int l, count = 0;
	              // do not send messages if request has been cancelled
	              while ((l = instream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
	                  count += l;
	                  buffer.write(tmp, 0, l);
	                  sendProgressMessage(count, (int) contentLength);
	              }
	          } finally {
	              instream.close();
	              buffer.flush();
	              buffer.close();
	          }
        }
        return null;
  }
  
}
