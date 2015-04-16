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
package com.ab.image;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.ab.cache.AbCacheHeaderParser;
import com.ab.cache.AbCacheResponse;
import com.ab.cache.AbCacheUtil;
import com.ab.cache.AbDiskBaseCache;
import com.ab.cache.AbDiskCache.Entry;
import com.ab.cache.image.AbBitmapResponse;
import com.ab.cache.image.AbImageBaseCache;
import com.ab.global.AbAppConfig;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskObjectListener;
import com.ab.task.thread.AbTaskQueue;
import com.ab.util.AbAppUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbImageLoader.java
 * 描述：下载图片并显示的工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2011-12-10 上午10:10:53
 */
public class AbImageLoader { 
	
    /** 上下文. */
    private Context context = null;
    
    /** 单例对象. */
	private static AbImageLoader imageLoader = null; 
    
    /** 显示的图片的宽. */
    private int desiredWidth;
	
	/** 显示的图片的高. */
    private int desiredHeight;
    
    /** 缓存超时时间设置. */
    private int expiresTime;
    
    /** 显示为下载中的图片. */
    private Drawable loadingImage;
    
    /** 显示下载失败的图片. */
    private Drawable errorImage;
    
    /** 图片未找到的图片. */
    private Drawable emptyImage;
    
    /** 请求队列. */
    private List<AbTaskQueue> taskQueueList;
    
    /** 图片下载监听器. */
    private OnImageListener onImageListener = null;
    
    /** 图片缓存. */
    private AbImageBaseCache memCache;
    
    /** 磁盘缓存. */
    private AbDiskBaseCache diskCache;
    
    /**
     * 构造图片下载器.
     *
     * @param context the context
     */
    public AbImageLoader(Context context) {
    	this.context = context;
    	this.expiresTime = AbAppConfig.IMAGE_CACHE_EXPIRES_TIME;
    	this.taskQueueList = new ArrayList<AbTaskQueue>();
    	PackageInfo info = AbAppUtil.getPackageInfo(context);
    	File cacheDir = null;
    	if(!AbFileUtil.isCanUseSD()){
    		cacheDir = new File(context.getCacheDir(), info.packageName);
		}else{
			cacheDir = new File(AbFileUtil.getCacheDownloadDir(context));
		}
    	this.diskCache = new AbDiskBaseCache(cacheDir);
    	this.memCache = AbImageBaseCache.getInstance();
    } 
    
    
	/**
	 * 
	 * 获得一个实例.
	 * @param context
	 * @return
	 */
	public static AbImageLoader getInstance(Context context) {
		if (imageLoader == null) { 
			imageLoader = new AbImageLoader(context); 
        } 
		return imageLoader;
	}
	
	/**
     * 显示这个图片.
     *
     * @param imageView 显得的View
     * @param url the url
     */
    public void display(final ImageView imageView,final String url) { 
    	display(imageView,null,url);
    }
	
     
    /**
     * 显示这个图片.
     *
     * @param imageView 显得的View
     * @param url the url
     */
    public void display(final ImageView imageView,final View loadingView,final String url) { 
    	
    	if(imageView != null){
    		if(AbStrUtil.isEmpty(url)){
        		if(emptyImage != null){
        			if(loadingView != null){
            			loadingView.setVisibility(View.INVISIBLE);
            		}
        			imageView.setVisibility(View.VISIBLE);
        			imageView.setImageDrawable(emptyImage);
        		}
        		return;
        	}
    	}
    	
    	//显示的图片的宽
        final int imageWidth = desiredWidth;
    	
    	//显示的图片的高
        final int imageHeight = desiredHeight;
    	
    	final String cacheKey = memCache.getCacheKey(url, imageWidth, imageHeight);
    	//先看内存
    	Bitmap bitmap = memCache.getBitmap(cacheKey);
    	AbLogUtil.i(AbImageLoader.class, "从LRUCache中获取到的图片"+cacheKey+":"+bitmap);
    	
    	if(bitmap != null){
    		showView(url,imageView,loadingView,bitmap);
    	}else{
    		if(imageView != null){
	    		//显示加载中
	        	if(loadingView!=null){
	    			loadingView.setVisibility(View.VISIBLE);
	    			imageView.setVisibility(View.INVISIBLE);
	    		}else {
	    			if(loadingImage != null){
	    			   imageView.setImageDrawable(loadingImage);
	    			   imageView.setVisibility(View.VISIBLE);
	    			}
	    		}
	        	//设置标记,目的解决闪烁问题
	            imageView.setTag(url);
    		}
    		
    		AbTaskItem item = new AbTaskItem();
            item.setListener(new AbTaskObjectListener(){
            	
                @Override
                public <T> void update(T entity) {
                	AbBitmapResponse response = (AbBitmapResponse)entity;
                	if(response == null){
                		if(imageView != null){
                			if(errorImage != null){
                    			imageView.setImageDrawable(errorImage);
                    		}
                        	imageView.setVisibility(View.VISIBLE);
                        	if(loadingView != null){
                    			loadingView.setVisibility(View.INVISIBLE);
                    		}
                		}
                		if(onImageListener!=null){
                    		onImageListener.onResponse(null);
                    	}
                	}else{
                		Bitmap bitmap = response.getBitmap();
                		//要判断这个imageView的url有变化，如果没有变化才set
                        //有变化就取消，解决列表的重复利用View的问题
                    	if(url.equals(imageView.getTag())){
                    		showView(url,imageView,loadingView,bitmap);
                    	}
                		
                		if(onImageListener!=null){
                    		onImageListener.onResponse(bitmap);
                    	}
                		AbLogUtil.d(AbImageLoader.class, "获取到图片："+bitmap);
                	}
                	
                }

    			@Override
                public AbBitmapResponse getObject() {
                    try {
                    	Bitmap bitmap = null;
                		//看磁盘
                		Entry entry = diskCache.get(cacheKey);
                		if(entry == null || entry.isExpired()){
                			AbLogUtil.i(AbImageLoader.class, "图片磁盘中没有，或者已经过期");
                			
                			AbCacheResponse response = AbCacheUtil.getCacheResponse(url,expiresTime);
                			if(response!=null){
                				bitmap =  AbImageUtil.getBitmap(response.data, imageWidth, imageHeight);
                    			memCache.putBitmap(cacheKey, bitmap);
                    			diskCache.put(cacheKey, AbCacheHeaderParser.parseCacheHeaders(response));
                			}
                		}else{
                			//磁盘中有
                			byte [] bitmapData = entry.data;
                			bitmap =  AbImageUtil.getBitmap(bitmapData, imageWidth, imageHeight);
                			memCache.putBitmap(cacheKey, bitmap);
                		}
                    	
                    	AbBitmapResponse bitmapResponse = new AbBitmapResponse(url);
                    	bitmapResponse.setBitmap(bitmap);
                    	return bitmapResponse;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                
            });
            
            add2Queue(item);
    	}
    	
    } 
    
    /**
     * 下载这个图片.
     *
     * @param url the url
     */
    public void download(final String url) { 
    	
    	if(AbStrUtil.isEmpty(url)){
    		return;
    	}
    	
    	if(Uri.parse(url).getHost() == null){
    		return;
    	}
    	
    	display(null,null,url);
    } 
    
    
    public void showView(String url,ImageView imageView,View loadingView, Bitmap bitmap){
    	
    	if(imageView != null){
        	if (bitmap != null) {
        		imageView.setImageBitmap(bitmap);
            } else {
            	if (emptyImage != null) {
            	   imageView.setImageDrawable(emptyImage);
                }
            }
        	imageView.setVisibility(View.VISIBLE);
        	
        	if(loadingView != null){
    			loadingView.setVisibility(View.INVISIBLE);
    		}
        	
    	}
    	
    	if(onImageListener!=null){
    		onImageListener.onResponse(bitmap);
    	}
    	
    }
   
   
   /**
    * 
    * 设置下载中的图片.
    * @param resID
    */
	public void setLoadingImage(int resID) {
		this.loadingImage = context.getResources().getDrawable(resID);
	}
	
	/**
	 * 描述：设置下载失败的图片.
	 *
	 * @param resID the new error image
	 */
	public void setErrorImage(int resID) {
		this.errorImage = context.getResources().getDrawable(resID);
	}

	/**
	 * 描述：设置未找到的图片.
	 *
	 * @param resID the new empty image
	 */
	public void setEmptyImage(int resID) {
		this.emptyImage = context.getResources().getDrawable(resID);
	}


	/**
	 * 获取失效时间.
	 *
	 * @return the expires time
	 */
	public int getExpiresTime() {
		return expiresTime;
	}


	/**
	 * 设置失效时间.
	 *
	 * @param expiresTime the new expires time
	 */
	public void setExpiresTime(int expiresTime) {
		this.expiresTime = expiresTime;
	}
	
	/**
	 * 监听器
	 */
	public interface OnImageListener {
		
		/**
		 * On response.
		 *
		 * @param bitmap the bitmap
		 */
		public void onResponse(Bitmap bitmap);
	}

	/**
	 * 获得监听器.
	 *
	 * @return the on image listener
	 */
	public OnImageListener getOnImageListener() {
		return onImageListener;
	}


	/**
	 * 设置监听器.
	 *
	 * @param onImageListener the new on image listener
	 */
	public void setOnImageListener(OnImageListener onImageListener) {
		this.onImageListener = onImageListener;
	}

	public int getDesiredWidth() {
		return desiredWidth;
	}


	public void setDesiredWidth(int desiredWidth) {
		this.desiredWidth = desiredWidth;
	}


	public int getDesiredHeight() {
		return desiredHeight;
	}


	public void setDesiredHeight(int desiredHeight) {
		this.desiredHeight = desiredHeight;
	}
	
	/**
	 * 
	 * 增加到最少的队列中.
	 * @param item
	 */
	private void add2Queue(AbTaskItem item){
		int minQueueIndex = 0;
		if(taskQueueList.size() == 0){
			AbTaskQueue queue = AbTaskQueue.newInstance();
			taskQueueList.add(queue);
			queue.execute(item);
		}else{
			int minSize = 0;
			for(int i=0;i<taskQueueList.size();i++){
				AbTaskQueue queue = taskQueueList.get(i);
				int size = queue.getTaskItemListSize();
				if(i==0){
					minSize = size;
					minQueueIndex = i;
				}else{
					if(size < minSize){
						minSize = size;
						minQueueIndex = i;
					}
				}
			}
			if(taskQueueList.size() <5 && minSize > 2){
				AbTaskQueue queue = AbTaskQueue.newInstance();
				taskQueueList.add(queue);
				queue.execute(item);
			}else{
				AbTaskQueue minQueue = taskQueueList.get(minQueueIndex);
				minQueue.execute(item);
			}
			
		}
		
		for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			int size = queue.getTaskItemListSize();
			AbLogUtil.i(AbImageLoader.class, "线程队列["+i+"]的任务数："+size);
		}
		
	}
	
	/**
	 * 
	 * 释放资源.
	 */
	public void cancelAll(){
		for(int i=0;i<taskQueueList.size();i++){
			AbTaskQueue queue = taskQueueList.get(i);
			queue.cancel(true);
		}
		taskQueueList.clear();
	}


	public AbImageBaseCache getMemCache() {
		return memCache;
	}


	public void setMemCache(AbImageBaseCache memCache) {
		this.memCache = memCache;
	}


	public AbDiskBaseCache getDiskCache() {
		return diskCache;
	}


	public void setDiskCache(AbDiskBaseCache diskCache) {
		this.diskCache = diskCache;
	}
	
}

