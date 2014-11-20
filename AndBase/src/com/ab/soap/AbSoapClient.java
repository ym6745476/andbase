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

import java.util.List;
import java.util.concurrent.Executor;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.content.Context;

import com.ab.global.AbAppConfig;
import com.ab.global.AbAppException;
import com.ab.http.AbHttpStatus;
import com.ab.task.AbThreadFactory;
import com.ab.util.AbAppUtil;
import com.ab.util.AbLogUtil;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 
 * 名称：AbSoapClient.java 
 * 描述：Soap客户端
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-10-27 下午15:28:55
 */
public class AbSoapClient {

	/** 上下文. */
	private static Context mContext;

	/** 线程执行器. */
	public static Executor mExecutorService = null;

	/**  WebService dotNet属性. */
	private boolean mDotNet = true;

	/**  soap参数. */
	private AbSoapParams mParams = null;
	
	/** 超时时间. */
    public static final int DEFAULT_SOCKET_TIMEOUT = 10000;
    
    /** 超时时间. */
	private int mTimeout = DEFAULT_SOCKET_TIMEOUT;


	/**
	 * 初始化.
	 * 
	 * @param context
	 *            the context
	 */
	public AbSoapClient(Context context) {
		mContext = context;
		mExecutorService = AbThreadFactory.getExecutorService();
	}

	/**
	 * Call.
	 *
	 * @param url the url
	 * @param nameSpace the name space
	 * @param methodName the method name
	 * @param Params the params
	 * @param listener the listener
	 */
	public void call(final String url,final String nameSpace,final String methodName, AbSoapParams Params,
			final AbSoapListener listener) {
		this.mParams = Params;

		if (!AbAppUtil.isNetworkAvailable(mContext)) {
			listener.sendFailureMessage(AbHttpStatus.CONNECT_FAILURE_CODE,
					AbAppConfig.CONNECT_EXCEPTION, new AbAppException(
							AbAppConfig.CONNECT_EXCEPTION));
			return;
		}

		listener.sendStartMessage();

		mExecutorService.execute(new Runnable() {
			@Override
			public void run() {
				try {
					doCall(url,nameSpace,methodName,mParams,listener);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Do call.
	 *
	 * @param url the url
	 * @param nameSpace the name space
	 * @param methodName the method name
	 * @param params the params
	 * @param listener the listener
	 */
	public void doCall(String url,String nameSpace,String methodName,AbSoapParams params, AbSoapListener listener) {

		String result = null;
		try {
			SoapObject request = new SoapObject(nameSpace, methodName);
			// 传递参数
			List<BasicNameValuePair> paramsList = params.getParamsList();
			for (NameValuePair nameValuePair : paramsList) {
				request.addProperty(nameValuePair.getName(), nameValuePair.getValue());
			}
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.bodyOut = request;
			envelope.dotNet = mDotNet;
			envelope.setOutputSoapObject(request);
			HttpTransportSE httpTransportSE = new HttpTransportSE(url,mTimeout);
			httpTransportSE.debug = true;

			AbLogUtil.d(AbSoapClient.class, "--call--");
			httpTransportSE.call(nameSpace+methodName, envelope);

			SoapObject bodyIn = (SoapObject) envelope.bodyIn;
			result = bodyIn.toString();
			if (result != null) {
				listener.sendSuccessMessage(AbHttpStatus.SUCCESS_CODE, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			listener.sendFailureMessage(AbHttpStatus.UNTREATED_CODE, AbAppConfig.UNTREATED_EXCEPTION, new AbAppException(AbAppConfig.UNTREATED_EXCEPTION));
		}
	}
	
	/**
     * 描述：设置连接超时时间.
     *
     * @param timeout 毫秒
     */
    public void setTimeout(int timeout) {
    	this.mTimeout = timeout;
	}

	/**
	 * Checks if is dot net.
	 *
	 * @return true, if is dot net
	 */
	public boolean isDotNet() {
		return mDotNet;
	}

	/**
	 * Sets the dot net.
	 *
	 * @param dotNet the new dot net
	 */
	public void setDotNet(boolean dotNet) {
		this.mDotNet = dotNet;
	}
    
    

}
