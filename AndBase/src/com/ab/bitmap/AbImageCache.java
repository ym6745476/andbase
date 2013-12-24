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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbMd5;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * 描述：图片缓存.
 *
 * @author zhaoqp
 * @date：2013-5-23 上午10:10:53
 * @version v1.0
 */

public class AbImageCache {
	
	/** The tag. */
	private static String TAG = "AbImageCache";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** 缓存空间大小8MB. */
	public static int cacheSize = 8 * 1024 * 1024; 
	
	/** 为了加快速度，在内存中开启缓存,最新的LruCache. */
	private static final LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(cacheSize) {      
		protected int sizeOf(String key, Bitmap bitmap) { 
			return bitmap.getRowBytes() * bitmap.getHeight();        
	    }

		@Override
		protected void entryRemoved(boolean evicted, String key,
				Bitmap oldValue, Bitmap newValue) {
			if(D) Log.d(TAG, "LruCache:移除了"+key);
		}
		
		
	};  
	
	/**正在下载中的线程*/
    private static final HashMap<String, Runnable> runRunnableCache = new HashMap<String, Runnable>();
    
    /**等待中的线程*/
    private static final List<HashMap<String, Runnable>> waitRunnableList = new ArrayList<HashMap<String, Runnable>>();
	
    /**锁对象*/
    public static final ReentrantLock lock = new ReentrantLock();
	
	/**
	 * 描述：从缓存中获取这个Bitmap.
	 *
	 * @param key the key
	 * @return the bitmap from mem cache
	 */
	public  static Bitmap getBitmapFromCache(String key) {  
		  return bitmapCache.get(key);  
	} 
	
	/**
	 * 描述：增加一个图片到缓存.
	 *
	 * @param key  通过url计算的缓存key
	 * @param bitmap the bitmap
	 */
	public  static void addBitmapToCache(String key,Bitmap bitmap){
		try {
			if(D) Log.d(TAG, "图片下载完成:"+key);
			lock.lock();
			if(AbStrUtil.isEmpty(key)){
				return;
			}
			
			if (getBitmapFromCache(key) == null && bitmap!=null) {
				bitmapCache.put(key, bitmap);
				if(D) Log.d(TAG, "存入缓存:"+key+","+bitmap);
				if(D) Log.d(TAG, "测试存入缓存是否成功:"+key+","+getBitmapFromCache(key));
			}
			//表示下载中的缓存清除
			removeRunRunnableFromCache(key);
			if(D) Log.d(TAG, "检查挂起线程:"+waitRunnableList.size());
			//唤醒等待线程并移除列表
			removeWaitRunnableFromCache(key);
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			lock.unlock();
		}
	}
	
	/**
	 * 描述：从缓存删除.
	 *
	 * @param key  通过url计算的缓存key
	 */
	public  static void removeBitmapFromCache(String key){
		try {
			lock.lock();
			if (getBitmapFromCache(key) != null) {  
				bitmapCache.remove(key);
			}  
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			lock.unlock();
		}
	}
	
	/**
	 * 描述：清空缓存的Bitmap.
	 */
	public  static void removeAllBitmapFromCache() { 
		  bitmapCache.evictAll();  
	}
	
	/**
     * 根据url计算缓存key,这个key+后缀就是文件名.
     * @param url 图片地址.
     * @param width 图片宽度.
     * @param height 图片高度.
     * @param type 处理类型.
     */
    public  static String getCacheKey(String url, int width, int height,int type) {
        return AbMd5.MD5(new StringBuilder(url.length() + 12).append("#W").append(width)
        .append("#H").append(height).append("#T").append(type).append(url).toString());
    }
    
    /**
	 * 描述：从缓存中获取这个正在执行线程.
	 *
	 * @param key the key
	 * @return the runnable
	 */
	public  static Runnable getRunRunnableFromCache(String key) {  
		  return runRunnableCache.get(key);  
	} 
    
    /**
	 * 描述：增加一个正在执行线程的记录.
	 *
	 * @param key  通过url计算的缓存key
	 * @param runnable the runnable
	 */
	public  static void addToRunRunnableCache(String key,Runnable runnable){
		try {
			lock.lock();
			if(AbStrUtil.isEmpty(key) || runnable == null){
				return;
			}
			if (getRunRunnableFromCache(key) == null) {  
				runRunnableCache.put(key, runnable);
			}  
		}catch (Exception e) {
			e.printStackTrace();
		}  finally{
			lock.unlock();
		}
	}
	
	/**
	 * 描述：从缓存一个正在执行的线程.
	 *
	 * @param key  通过url计算的缓存key
	 */
	public  static void removeRunRunnableFromCache(String key){
		if (getRunRunnableFromCache(key) != null) {  
			runRunnableCache.remove(key);
		}  
	}
	
	/**
	 * 描述：从缓存中获取这个正在等待线程.
	 *
	 * @param key the key
	 * @return the runnable
	 */
	public  static Runnable getWaitRunnableFromCache(String key) {  
		  return runRunnableCache.get(key);  
	} 
    
    /**
	 * 描述：增加一个等待线程的记录.
	 *
	 * @param key  通过url计算的缓存key
	 * @param runnable the runnable
	 */
	public  static void addToWaitRunnableCache(String key,Runnable runnable){
		try {
			lock.lock();
			if(AbStrUtil.isEmpty(key) || runnable == null){
				return;
			}
			HashMap<String, Runnable> runnableMap = new HashMap<String, Runnable>();
			runnableMap.put(key, runnable);
			waitRunnableList.add(runnableMap);
		} catch (Exception e) {
			e.printStackTrace();
		}  finally{
			lock.unlock();
		}
	}
	
	/**
	 * 描述：从缓存删除一个等待线程.
	 *
	 * @param key  通过url计算的缓存key
	 */
	public  static void removeWaitRunnableFromCache(String key){
		try {
			lock.lock();
			for(int i=0;i<waitRunnableList.size();i++){
				HashMap<String, Runnable> runnableMap = waitRunnableList.get(i);
				Runnable runnable = runnableMap.get(key);
				if (runnable != null) {  
					if(D) Log.d(TAG, "从缓存删除并唤醒:"+runnable);
					synchronized(runnable){
					   runnable.notify();
					}
					waitRunnableList.remove(runnableMap);
					i--;
				}  
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			lock.unlock();
		}
	}
	
		
}
