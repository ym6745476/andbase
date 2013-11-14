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
package com.ab.global;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.HttpHostConnectException;

import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * 描述： 公共异常类.
 *
 * @author zhaoqp
 * @date 2012-2-10
 * @version v1.0
 */
public class AbAppException extends Exception {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1;

	
	/** 异常消息. */
	private String msg = null;

	/**
	 * 构造异常类.
	 *
	 * @param e 异常
	 */
	public AbAppException(Exception e) {
		super();

		try {
			if( e instanceof HttpHostConnectException) {  
				msg = AbConstant.UNKNOWNHOSTEXCEPTION;
			}else if (e instanceof ConnectException) {
				msg = AbConstant.CONNECTEXCEPTION;
			}else if (e instanceof UnknownHostException) {
				msg = AbConstant.UNKNOWNHOSTEXCEPTION;
			}else if (e instanceof SocketException) {
				msg = AbConstant.SOCKETEXCEPTION;
			}else if (e instanceof SocketTimeoutException) {
				msg = AbConstant.SOCKETTIMEOUTEXCEPTION;
			}else if( e instanceof NullPointerException) {  
				msg = AbConstant.NULLPOINTEREXCEPTION;
			}else if( e instanceof ClientProtocolException) {  
				msg = AbConstant.CLIENTPROTOCOLEXCEPTION;
			}else {
				if (e == null || AbStrUtil.isEmpty(e.getMessage())) {
					msg = AbConstant.NULLMESSAGEEXCEPTION;
				}else{
				    msg = e.getMessage();
				}
			}
		} catch (Exception e1) {
		}
		
	}

	/**
	 * 用一个消息构造异常类.
	 *
	 * @param message 异常的消息
	 */
	public AbAppException(String message) {
		super(message);
		msg = message;
	}

	/**
	 * 描述：获取异常信息.
	 *
	 * @return the message
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
