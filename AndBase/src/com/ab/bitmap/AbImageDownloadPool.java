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
package com.ab.bitmap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbAppUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;
// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbImageDownload.java 
 * 描述：线程池图片下载
 * @author zhaoqp
 * @date：2013-5-23 上午10:10:53
 * @version v1.0
 */

public class AbImageDownloadPool{
	
	/** The tag. */
	private static String TAG = "AbImageDownloadPool";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	//单例对象
	/** The image download. */
	private static AbImageDownloadPool imageDownload = null; 
	
	/** 固定3个线程来执行任务 . */
	private static int nThreads  = 3;
	
	/** The executor service. */
	private ExecutorService executorService = null; 
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
            AbImageDownloadItem item = (AbImageDownloadItem)msg.obj; 
            item.callback.update(item.bitmap, item.imageUrl); 
        } 
    }; 
	
	/**
	 * 构造图片下载器.
	 *
	 * @param nThreads the n threads
	 */
    protected AbImageDownloadPool(int nThreads) {
    	executorService = Executors.newFixedThreadPool(nThreads); 
    } 
	
	/**
	 * 单例构造图片下载器.
	 *
	 * @return single instance of AbImageDownloadPool
	 */
    public static AbImageDownloadPool getInstance() { 
        if (imageDownload == null) { 
        	nThreads = AbAppUtil.getNumCores();
        	imageDownload = new AbImageDownloadPool(nThreads*3); 
        } 
        return imageDownload;
    } 
    
    /**
     * Download.
     *
     * @param item the item
     */
    public void download(final AbImageDownloadItem item) {    
    	String urlImage = item.imageUrl;
    	if(AbStrUtil.isEmpty(urlImage)){
    		if(D)Log.d(TAG, "图片URL为空，请先判断");
    	}else{
    		urlImage = urlImage.trim();
    	}
    	final String url = urlImage;
		// 如果缓存过就从缓存中取出数据
    	String cacheKey = AbImageCache.getCacheKey(url, item.width, item.height, item.type);
		item.bitmap =  AbImageCache.getBitmapFromMemCache(cacheKey);
		if(D) Log.d(TAG, "缓存中获取的"+cacheKey+":"+item.bitmap);
    	
    	if(item.bitmap == null){
    		// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
	    	executorService.submit(new Runnable() { 
	    		public void run() {
	    			try {
	    				item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
	    				if(D) Log.d(TAG, "下载从SD卡得到的:"+item.bitmap);
	    				String cacheKey = AbImageCache.getCacheKey(url, item.width, item.height, item.type);
	    				AbImageCache.addBitmapToMemoryCache(cacheKey,item.bitmap);                                           
	    			} catch (Exception e) { 
	    				e.printStackTrace();
	    			} finally{ 
		    			if (item.callback != null) { 
	    	                Message msg = handler.obtainMessage(); 
	    	                msg.obj = item; 
	    	                handler.sendMessage(msg); 
	    	            } 
	    			}
	    		}                 
	    	});  
    	}else{
    		if (item.callback != null) { 
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            } 
    	}
    	
    }
    
    /**
     * 描述：立即关闭.
     */
    public void shutdownNow(){
    	if(!executorService.isTerminated()){
    		executorService.shutdownNow();
    		listenShutdown();
    	}
    	
    }
    
    /**
     * 描述：平滑关闭.
     */
    public void shutdown(){
    	if(!executorService.isTerminated()){
    	   executorService.shutdown();
    	   listenShutdown();
    	}
    }
    
    /**
     * 描述：关闭监听.
     */
    public void listenShutdown(){
    	try {
			while(!executorService.awaitTermination(1, TimeUnit.MILLISECONDS)) { 
				if(D) Log.d(TAG, "线程池未关闭");
			}  
			if(D) Log.d(TAG, "线程池已关闭");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}
