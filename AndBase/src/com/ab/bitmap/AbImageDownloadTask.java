package com.ab.bitmap;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbFileUtil;
import com.ab.util.AbStrUtil;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbImageDownloadTask.java 
 * 描述：AsyncTask实现的下载，单次下载
 * @author zhaoqp
 * @date：2013-9-2 下午12:47:51
 * @version v1.0
 */
public class AbImageDownloadTask extends AsyncTask<AbImageDownloadItem, Integer, AbImageDownloadItem> {
	
	/** The tag. */
	private static String TAG = "AbImageDownloadTask";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	//if(D)Log.d(TAG, "任务callback handleMessage...");
            AbImageDownloadItem item = (AbImageDownloadItem)msg.obj; 
            item.listener.update(item.bitmap, item.imageUrl); 
        } 
    }; 
	
	public AbImageDownloadTask() {
		super();
	}

	/**  
     * 这里的第一个参数对应AsyncTask中的第一个参数   
     * 这里的String返回值对应AsyncTask的第三个参数  
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改  
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作  
     */  
	@Override
	protected AbImageDownloadItem doInBackground(AbImageDownloadItem... items) {
		AbImageDownloadItem item = items[0];
		//检查图片路径
    	String url = item.imageUrl;
    	if(AbStrUtil.isEmpty(url)){
    		if(D)Log.d(TAG, "图片URL为空，请先判断");
    	}else{
    		url = url.trim();
    	}
    	
    	String cacheKey = AbImageCache.getCacheKey(url, item.width, item.height, item.type);
		item.bitmap =  AbImageCache.getBitmapFromCache(cacheKey);
    	if(item.bitmap == null){
    		//开始下载
            item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
            //缓存图片路径
            AbImageCache.addBitmapToCache(cacheKey,item.bitmap);                                           
            //需要执行回调来显示图片
            if (item.listener != null) {
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            }
		}else{
			if(D) Log.d(TAG, "从内存缓存中得到图片:"+cacheKey+","+item.bitmap);
			if (item.listener != null) {
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            }
	    }
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(AbImageDownloadItem item) {
		if (item.listener != null) { 
        	item.listener.update(item.bitmap, item.imageUrl); 
        }
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
	}

}
