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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.util.Log;

import com.ab.global.AbAppData;

// TODO: Auto-generated Javadoc
/**
 * 描述： 执行任务线程（按队列执行）.
 * 每个程序只有1个
 * @author zhaoqp
 * @date 2011-11-10
 * @version v1.0
 */
public class AbTaskQueue extends Thread { 
	
	/** The tag. */
	private static String TAG = "AbTaskQueue";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
	/** 等待执行的任务. */
	private static List<AbTaskItem> mAbTaskItemList = null;
    
    /**单例对象 */
  	private static AbTaskQueue mAbTaskQueue = null; 
  	
  	/** 停止的标记. */
	private boolean mQuit = false;
	
	/** 执行完成后的消息句柄. */
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
	 * 单例构造.
	 */
    public static AbTaskQueue getInstance() { 
        if (mAbTaskQueue == null) { 
        	mAbTaskQueue = new AbTaskQueue();
        } 
        return mAbTaskQueue;
    } 
	
	/**
	 * 构造执行线程队列.
	 *
	 * @param context the context
	 */
    public AbTaskQueue() {
    	mQuit = false;
    	mAbTaskItemList = new ArrayList<AbTaskItem>();
    	//设置优先级
    	Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    	//从线程池中获取
    	ExecutorService mExecutorService  = AbTaskPool.getExecutorService();
    	mExecutorService.submit(this); 
    }
    
    /**
     * 开始一个执行任务.
     *
     * @param item 执行单位
     */
    public void execute(AbTaskItem item) { 
         addTaskItem(item); 
    } 
    
    
    /**
     * 开始一个执行任务并清除原来队列.
     * @param item 执行单位
     * @param clean 清空之前的任务
     */
    public void execute(AbTaskItem item,boolean clean) { 
	    if(clean){
	    	if(mAbTaskQueue!=null){
	    		mAbTaskQueue.quit();
	    	}
	    }
    	addTaskItem(item); 
    } 
     
    /**
     * 描述：添加到执行线程队列.
     *
     * @param item 执行单位
     */
    private synchronized void addTaskItem(AbTaskItem item) { 
    	if (mAbTaskQueue == null) { 
        	mAbTaskQueue = new AbTaskQueue();
        	mAbTaskItemList.add(item);
        } else{
        	mAbTaskItemList.add(item);
        }
    	//添加了执行项就激活本线程 
        this.notify();
        
    } 
 
    /**
     * 描述：线程运行.
     *
     * @see java.lang.Thread#run()
     */
    @Override 
    public void run() { 
        while(!mQuit) { 
        	try {
        	    while(mAbTaskItemList.size() > 0){
            
					AbTaskItem item  = mAbTaskItemList.remove(0);
					//定义了回调
				    if (item.listener != null) { 
				    	item.listener.get();
				    	//交由UI线程处理 
				        Message msg = handler.obtainMessage(); 
				        msg.obj = item; 
				        handler.sendMessage(msg); 
				    } 
				    
				    //停止后清空
				    if(mQuit){
				    	mAbTaskItemList.clear();
				    	return;
				    }
        	    }
        	    try {
					//没有执行项时等待 
					synchronized(this) { 
					    this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
					//被中断的是退出就结束，否则继续
					if (mQuit) {
						mAbTaskItemList.clear();
	                    return;
	                }
	                continue;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
        } 
    } 
    
    /**
     * 描述：终止队列释放线程.
     */
    public void quit(){
		mQuit  = true;
		interrupted();
		mAbTaskQueue  = null;
    }

}

