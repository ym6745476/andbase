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
package com.ab.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;

import com.ab.view.app.AbMonitorView;


// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbMonitorUtil.java 
 * 描述：应用监控类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-30 下午11:52:13
 */
public final class AbMonitorUtil {
	
	/** 性能测试开关状态. */
	public static boolean mMonitorOpened = false;
	
	/** 帧测试View. */
	private static AbMonitorView mAbMonitorView  = null;
	
	/** 帧测试Handler. */
	private static Handler mMonitorHandler = new Handler();
	
	/** 帧测试线程. */
	private static Runnable mMonitorRunnable = null;
	
	/** Window 管理器. */
	private static WindowManager mWindowManager = null;
	
	/** 帧测试参数. */
	private static WindowManager.LayoutParams mMonitorParams = null;

    
	/**
	 * 描述：打开关闭帧数测试
	 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
	 * lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
	 *
	 */
	public static void openMonitor(Context context){
		
		if(mMonitorOpened){
			return;
		}
        mWindowManager = ((Activity)context).getWindowManager();
		
		DisplayMetrics display = AbAppUtil.getDisplayMetrics(context);
		final int diaplayWidth = display.widthPixels;
		
		mAbMonitorView  = new AbMonitorView(context);
		mMonitorParams = new WindowManager.LayoutParams();
        // 设置window type
		mMonitorParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        /*
         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
         * 那么优先级会降低一些, 即拉下通知栏不可见
         */
		//设置图片格式，效果为背景透明
		mMonitorParams.format = PixelFormat.RGBA_8888; 
        // 设置Window flag
		mMonitorParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        /*
         * 下面的flags属性的效果形同“锁定”。
         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
                               | LayoutParams.FLAG_NOT_FOCUSABLE
                               | LayoutParams.FLAG_NOT_TOUCHABLE;
         */
        // 设置悬浮窗的长得宽
		mMonitorParams.width = AbViewUtil.scaleValue(context, 100);
		mMonitorParams.height = AbViewUtil.scaleValue(context, 50);
        mWindowManager.addView(mAbMonitorView, mMonitorParams);
        mMonitorOpened = true;
		mMonitorRunnable = new Runnable() {

			@Override
			public void run() {
				mAbMonitorView.postInvalidate();
				mMonitorHandler.postDelayed(this,0);
			}
		};
		mMonitorHandler.postDelayed(mMonitorRunnable,0);
		
		mAbMonitorView.setOnTouchListener(new OnTouchListener() {
        	int lastX, lastY;
        	int paramX, paramY;
        	
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					lastX = (int) event.getRawX();
					lastY = (int) event.getRawY();
					paramX = mMonitorParams.x;
					paramY = mMonitorParams.y;
					break;
				case MotionEvent.ACTION_MOVE:
					int dx = (int) event.getRawX() - lastX;
					int dy = (int) event.getRawY() - lastY;
					if ((paramX + dx) > diaplayWidth/2 ) {
						mMonitorParams.x = diaplayWidth;
					}else {
						mMonitorParams.x = 0;
					}
                    mMonitorParams.x = paramX + dx;
					mMonitorParams.y = paramY + dy;
					// 更新悬浮窗位置
					mWindowManager.updateViewLayout(mAbMonitorView, mMonitorParams);
					break;
				}
				return true;
			}
		});
		
	}
	
	/**
	 * 描述：关闭帧数测试.
	 */
	public static void closeMonitor(){
		if(mMonitorOpened) {
			if(mWindowManager != null && mAbMonitorView != null){
				mWindowManager.removeView(mAbMonitorView);
			}
			mMonitorOpened = false;
			if(mMonitorHandler != null  && mMonitorRunnable != null){
			    mMonitorHandler.removeCallbacks(mMonitorRunnable);
			}
		}
		
	}

}
