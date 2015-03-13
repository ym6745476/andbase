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
package com.ab.global;




// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbAppConfig.java 
 * 描述：初始设置类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-03 下午1:33:39
 */
public class AbAppConfig {
	
	/**  UI设计的基准宽度. */
	public static int UI_WIDTH = 720;
	
	/**  UI设计的基准高度. */
	public static int UI_HEIGHT = 1280;
	
	/**  UI设计的密度. */
	public static int UI_DENSITY = 2;
	
	/** 默认 SharePreferences文件名. */
	public static String SHARED_PATH = "app_share";
	
	/** 默认下载文件地址. */
	public static  String DOWNLOAD_ROOT_DIR = "download";
	
    /** 默认下载图片文件地址. */
	public static  String DOWNLOAD_IMAGE_DIR = "images";
	
    /** 默认下载文件地址. */
	public static  String DOWNLOAD_FILE_DIR = "files";
	
	/** APP缓存目录. */
	public static  String CACHE_DIR = "cache";
	
	/** DB目录. */
	public static  String DB_DIR = "db";
	
	/** 默认缓存超时时间设置. */
	public static int IMAGE_CACHE_EXPIRES_TIME = 3600*24*3;
	
	/** 缓存大小  单位10M. */
	public static int MAX_CACHE_SIZE_INBYTES = 10*1024*1024;
	
	/** The Constant CONNECTEXCEPTION. */
	public static String CONNECT_EXCEPTION = "无法连接到网络";
	
	/** The Constant UNKNOWNHOSTEXCEPTION. */
	public static String UNKNOWN_HOST_EXCEPTION = "连接远程地址失败";
	
	/** The Constant SOCKETEXCEPTION. */
	public static String SOCKET_EXCEPTION = "网络连接出错，请重试";
	
	/** The Constant SOCKETTIMEOUTEXCEPTION. */
	public static String SOCKET_TIMEOUT_EXCEPTION = "连接超时，请重试";
	
	/** The Constant NULLPOINTEREXCEPTION. */
	public static String NULL_POINTER_EXCEPTION = "抱歉，远程服务出错了";
	
	/** The Constant NULLMESSAGEEXCEPTION. */
	public static String NULL_MESSAGE_EXCEPTION = "抱歉，程序出错了";
	
	/** The Constant CLIENTPROTOCOLEXCEPTION. */
	public static String CLIENT_PROTOCOL_EXCEPTION = "Http请求参数错误";
	
	/** 参数个数不够. */
	public static String MISSING_PARAMETERS = "参数没有包含足够的值";
	
	/** The Constant REMOTESERVICEEXCEPTION. */
	public static String REMOTE_SERVICE_EXCEPTION = "抱歉，远程服务出错了";
	
	/** 页面未找到. */
	public static String NOT_FOUND_EXCEPTION = "页面未找到";
	
	/** 其他异常. */
	public static String UNTREATED_EXCEPTION = "未处理的异常";
	

}
