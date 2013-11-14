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
package com.ab.task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppData;
import com.ab.util.AbAppUtil;
// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbTaskPool.java 
 * 描述：线程池,程序中只有1个
 * @author zhaoqp
 * @date：2013-5-23 上午10:10:53
 * @version v1.0
 */

public class AbTaskPool{
	
	/** The tag. */
	private static String TAG = "AbTaskPool";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** 单例对象 The http pool. */
	private static AbTaskPool mAbTaskPool = null; 
	
	/** 固定5个线程来执行任务. */
	private static int nThreads  = 5;
	
	/** 线程执行器. */
	private static ExecutorService executorService = null; 
	
	/** 下载完成后的消息句柄. */
    private static Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
        	AbTaskItem item = (AbTaskItem)msg.obj; 
        	if(item.getListener() instanceof AbTaskListListener){
        		((AbTaskListListener)item.listener).update((List<?>)item.getResult()); 
        	}else if(item.getListener() instanceof AbTaskObjectListener){
        		((AbTaskObjectListener)item.listener).update(item.getResult()); 
        	}else{
        		item.listener.update(); 
        	}
        } 
    }; 
    
    /**
     * 初始化线程池
     */
    static{
    	nThreads = AbAppUtil.getNumCores();
    	mAbTaskPool = new AbTaskPool(nThreads*5); 
    }
	
	/**
	 * 构造线程池.
	 *
	 * @param nThreads 初始的线程数
	 */
    protected AbTaskPool(int nThreads) {
    	executorService = Executors.newFixedThreadPool(nThreads); 
    } 
	
	/**
	 * 单例构造图片下载器.
	 *
	 * @return single instance of AbHttpPool
	 */
    public static AbTaskPool getInstance() { 
        return mAbTaskPool;
    } 
    
    /**
     * 执行任务.
     * @param item the item
     */
    public void execute(final AbTaskItem item) {    
    	executorService.submit(new Runnable() { 
    		public void run() {
    			try {
    				//定义了回调
                    if (item.listener != null) { 
                    	item.listener.get();
                    	//交由UI线程处理 
                        Message msg = handler.obtainMessage(); 
                        msg.obj = item; 
                        handler.sendMessage(msg); 
                    }                              
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}                         
    		}                 
    	});                 
    	
    }
    
    
    /**
     * 
     * 描述：获取线程池的执行器
     * @return executorService
     * @throws 
     */
    public static ExecutorService getExecutorService() {
		return executorService;
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
