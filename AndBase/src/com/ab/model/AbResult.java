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
package com.ab.model;

import com.ab.util.AbJsonUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbResult.java 
 * 描述：结果
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public class AbResult {
	
	/** 返回码：成功. */
    public static final int RESULT_OK = 0;
    
    /** 返回码：失败. */
    public static final int RESULT_ERROR = -1;

	/** 结果code. */
	private int resultCode;
	
	/** 结果 message. */
	private String resultMessage;
	
    /**
     * 构造
     */
	public AbResult() {
		super();
	}

	/**
	 * 构造
	 * @param resultCode
	 * @param resultMessage
	 */
	public AbResult(int resultCode, String resultMessage) {
		super();
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 用json构造自己
	 * @param json
	 */
	public AbResult(String json) {
		super();
		AbResult result = (AbResult)AbJsonUtil.fromJson(json, this.getClass());
		this.resultCode = result.getResultCode();
		this.resultMessage = result.getResultMessage();
	}

	/**
	 * 获取返回码.
	 *
	 * @return the result code
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * 设置返回码.
	 *
	 * @param resultCode the new result code
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * 获取返回信息.
	 *
	 * @return the result message
	 */
	public String getResultMessage() {
		return resultMessage;
	}

	/**
	 * 设置返回信息.
	 *
	 * @param resultMessage the new result message
	 */
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	
	/**
	 * 
	 * 描述：转换成json.
	 * @return
	 */
	public String toJson(){
		return AbJsonUtil.toJson(this);
	}
	

}
