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

import android.os.Handler;
import android.os.Message;



// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbHttpResponseListener.java 
 * 描述：Http响应监听器
 * @author zhaoqp
 * @date：2013-11-13 上午9:00:52
 * @version v1.0
 */
public class AbHttpResponseListener {
	
	/** The Constant TAG. */
    private static final String TAG = "AbHttpResponseListener";
	
	/** The Constant SUCCESS_MESSAGE. */
    protected static final int SUCCESS_MESSAGE = 0;
    
    /** The Constant FAILURE_MESSAGE. */
    protected static final int FAILURE_MESSAGE = 1;
    
    /** The Constant START_MESSAGE. */
    protected static final int START_MESSAGE = 2;
    
    /** The Constant FINISH_MESSAGE. */
    protected static final int FINISH_MESSAGE = 3;
    
    /** The Constant PROGRESS_MESSAGE. */
    protected static final int PROGRESS_MESSAGE = 4;
    
    /** The Constant RETRY_MESSAGE. */
    protected static final int RETRY_MESSAGE = 5;
    
    /** The handler. */
    private Handler mHandler;
    
    public AbHttpResponseListener() {
		super();
	}

	/**
	 * 描述：获取数据开始.
	 */
    public void onStart() {};
    
    
    /**
	 * 完成后调用，失败，成功，调用.
	 */
    public void onFinish() {};
    
    /**
	 * 重试.
	 */
    public void onRetry() {}
    
    /**
	 * 描述：失败，调用.
	 */
    public void onFailure(int statusCode, String content,Throwable error) {}
    
    /**
	 * 进度.
	 */
    public void onProgress(int bytesWritten, int totalSize) {}
    
    /**
     * 开始消息.
     */
    public void sendStartMessage(){
    	sendMessage(obtainMessage(START_MESSAGE, null));
    }
    
    /**
     * 完成消息.
     */
    public void sendFinishMessage(){
    	sendMessage(obtainMessage(FINISH_MESSAGE,null));
    }
    
    /**
     * 进度消息.
     */
    public void sendProgressMessage(int bytesWritten, int totalSize) {
        sendMessage(obtainMessage(PROGRESS_MESSAGE, new Object[]{bytesWritten, totalSize}));
    }
    
    /**
     * 失败消息.
     */
    public void sendFailureMessage(int statusCode,String content,Throwable error){
    	sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{statusCode,content, error}));
    }
    
    /**
     * 重试消息.
     */
    public void sendRetryMessage() {
        sendMessage(obtainMessage(RETRY_MESSAGE, null));
    }
    
    /**
     * 发送消息.
     * @param msg the msg
     */
    public void sendMessage(Message msg) {
        if (msg != null) {
        	msg.sendToTarget();
        }
    }
    
    /**
     * 构造Message.
     * @param responseMessage the response message
     * @param response the response
     * @return the message
     */
    protected Message obtainMessage(int responseMessage, Object response) {
        Message msg;
        if (mHandler != null) {
            msg = mHandler.obtainMessage(responseMessage, response);
        } else {
            msg = Message.obtain();
            if (msg != null) {
                msg.what = responseMessage;
                msg.obj = response;
            }
        }
        return msg;
    }

	public Handler getHandler() {
		return mHandler;
	}

	/**
     * 
     * 描述：设置Handler
     * @param handler
     * @throws 
     */
	public void setHandler(Handler handler) {
		this.mHandler = handler;
	}

}
