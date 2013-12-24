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
            item.listener.update(item.bitmap, item.imageUrl); 
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
    	String imageUrl = item.imageUrl;
    	if(AbStrUtil.isEmpty(imageUrl)){
    		if(D)Log.d(TAG, "图片URL为空，请先判断");
    	}else{
    		imageUrl = imageUrl.trim();
    	}
		//从缓存中获取图片
    	final String cacheKey = AbImageCache.getCacheKey(imageUrl, item.width, item.height, item.type);
		item.bitmap =  AbImageCache.getBitmapFromCache(cacheKey);
    	
    	if(item.bitmap == null){
			// 缓存中没有图像，则从网络上取出数据，并将取出的数据缓存到内存中
	    	executorService.submit(new Runnable() { 
	    		public void run() {
	    			try {
	    				//逻辑：判断这个任务是否有其他线程再执行，如果有等待，直到下载完成唤醒显示
	    	    		Runnable runnable = AbImageCache.getRunRunnableFromCache(cacheKey);
	    				if(runnable != null){
	    					
	    	            	//线程等待通知后显示
    	                	if(D) Log.d(TAG, "等待:"+cacheKey+","+item.imageUrl);
    	                	AbImageCache.addToWaitRunnableCache(cacheKey, this);
    	                	synchronized(this){
    	                		this.wait();
    	                	}
    	                	if(D) Log.d(TAG, "我醒了:"+item.imageUrl);
    	    				//直接获取
    	    				item.bitmap =  AbImageCache.getBitmapFromCache(cacheKey);
	    				}else{
	    					//增加下载中的线程记录
	    					if(D) Log.d(TAG, "增加图片下载中:"+cacheKey+","+item.imageUrl);
    	    				AbImageCache.addToRunRunnableCache(cacheKey, this);
    	    				item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
    	    				//增加到下载完成的缓存，删除下载中的记录和等待的记录，同时唤醒所有等待列表中key与其key相同的线程
        	    			AbImageCache.addBitmapToCache(cacheKey,item.bitmap);     
    	    				
	    				}
	    				
	    			} catch (Exception e) { 
	    				if(D) Log.d(TAG, "error:"+item.imageUrl);
	    				e.printStackTrace();
	    			} finally{ 
		    			if (item.listener != null) { 
	    	                Message msg = handler.obtainMessage(); 
	    	                msg.obj = item; 
	    	                handler.sendMessage(msg); 
	    	            } 
	    			}
	    		}                 
	    	});  
    		
    	}else{
    		if(D) Log.d(TAG, "从内存缓存中得到图片:"+cacheKey+","+item.bitmap);
    		if (item.listener != null) { 
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
