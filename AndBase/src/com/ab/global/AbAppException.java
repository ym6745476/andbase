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

import android.text.TextUtils;

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

	
	/** The msg. */
	private String msg = null;

	/**
	 * Instantiates a new ab app exception.
	 *
	 * @param e the e
	 */
	public AbAppException(Exception e) {
		super();

		try {
			if (e instanceof ConnectException) {
				msg = "无法连接网络，请检查网络配置";
			} 
			else if (e instanceof UnknownHostException) {
				msg = "不能解析的服务地址";
			}else if (e instanceof SocketException) {
				msg = "网络有错误，请重试";
			}else if (e instanceof SocketTimeoutException) {
				msg = "连接超时，请重试";
			} else {
				if (e == null || TextUtils.isEmpty(e.getMessage())) {
					msg = "抱歉，程序出错了，请联系我们";
				}
				msg = " " + e.getMessage();
			}
		} catch (Exception e1) {
		}

	}

	/**
	 * Instantiates a new ab app exception.
	 *
	 * @param detailMessage the detail message
	 */
	public AbAppException(String detailMessage) {
		super(detailMessage);
		msg = detailMessage;
	}

	/**
	 * 描述：获取异常信息.
	 *
	 * @return the message
	 * @see java.lang.Throwable#getMessage()
	 */
	@Override
	public String getMessage() {
		return msg;
	}

}
