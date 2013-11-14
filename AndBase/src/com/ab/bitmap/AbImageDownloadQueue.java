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

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbFileUtil;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * 描述：图片下载线程，按队列下载（先检查SD卡是否存在相同文件，不存在则下载，最后再从SD卡中读取）.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageDownloadQueue extends Thread { 
	
	/** The tag. */
	private static String TAG = "AbImageDownloadQueue";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** 下载队列. */
	private List<AbImageDownloadItem> queue;
	
	/** 图片下载线程单例类. */
    private static AbImageDownloadQueue imageDownloadThread = null; 
    
    /** 控制释放. */
    private static boolean stop = false;
    
    /** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	//if(D)Log.d(TAG, "任务callback handleMessage...");
            AbImageDownloadItem item = (AbImageDownloadItem)msg.obj; 
            item.listener.update(item.bitmap, item.imageUrl); 
        } 
    }; 
    
    
    /**
     * 构造图片下载线程队列.
     */
    private AbImageDownloadQueue() {
    	stop = false;
		queue = new ArrayList<AbImageDownloadItem>();
    } 
    
    /**
     * 单例构造图片下载线程.
     *
     * @return single instance of AbImageDownloadQueue
     */
    public static AbImageDownloadQueue getInstance() { 
        if (imageDownloadThread == null) { 
            imageDownloadThread = new AbImageDownloadQueue(); 
            //创建后立刻运行
            imageDownloadThread.start(); 
        } 
        return imageDownloadThread; 
    } 
     
    /**
     * 开始一个下载任务.
     *
     * @param item 图片下载单位
     * @return Bitmap 下载完成后得到的Bitmap
     */
    public void download(AbImageDownloadItem item) { 
    	//检查图片路径
    	String url = item.imageUrl;
    	if(AbStrUtil.isEmpty(url)){
    		if(D)Log.d(TAG, "图片URL为空，请先判断");
    	}else{
    		url = url.trim();
    	}
		//从缓存中获取这个Bitmap.
    	String cacheKey = AbImageCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
		item.bitmap =  AbImageCache.getBitmapFromMemCache(cacheKey);
    	if(item.bitmap == null){
    			addDownloadItem(item); 
		}else{
    		if (item.listener != null) {
                Message msg = handler.obtainMessage(); 
                msg.obj = item; 
                handler.sendMessage(msg); 
            }
	    }
    } 
    
    /**
     * 描述：添加到图片下载线程队列.
     *
     * @param item 图片下载单位
     */
    private synchronized void addDownloadItem(AbImageDownloadItem item) { 
        queue.add(item); 
        //添加了下载项就激活本线程 
        this.notify();
    } 
    
    /**
     * 开始一个下载任务并清除原来队列.
     *
     * @param item 下载单位
     */
    public void downloadBeforeClean(AbImageDownloadItem item) { 
    	queue.clear();
        addDownloadItem(item); 
    } 
 
    /**
     * 描述：线程运行.
     *
     * @see java.lang.Thread#run()
     */
    @Override 
    public void run() { 
        while(!stop) { 
        	//if(D)Log.d(TAG, "任务大小："+queue.size());
            while(queue.size() > 0) { 
                AbImageDownloadItem item = queue.remove(0); 
                //开始下载
                item.bitmap = AbFileUtil.getBitmapFromSDCache(item.imageUrl,item.type,item.width,item.height);
                //缓存图片路径
                String cacheKey = AbImageCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
                AbImageCache.addBitmapToMemoryCache(cacheKey,item.bitmap);                                           
                //需要执行回调来显示图片
                if (item.listener != null) { 
                    //交由UI线程处理 
                    Message msg = handler.obtainMessage(); 
                    msg.obj = item; 
                    handler.sendMessage(msg); 
                } 
                
                //停止
                if(stop){
                	queue.clear();
                	return;
                }
            } 
            try { 
            	//没有下载项时等待 
                synchronized(this) { 
                    this.wait();
                } 
            } catch (InterruptedException e) { 
                e.printStackTrace(); 
            } 
        } 
    } 

    /**
     * 
     * 描述：终止队列释放线程
     * @throws 
     */
    public void stopQueue(){
    	try {
			stop  = true;
			imageDownloadThread = null;
			this.interrupt();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

}

