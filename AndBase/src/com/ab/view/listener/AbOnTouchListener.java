package com.ab.view.listener;

import android.view.MotionEvent;

/**
 * 
 * 触摸屏幕接口
 *
 */
public interface AbOnTouchListener {
	/**
	 * 描述：Touch事件.
	 *
	 * @param event 触摸手势
	 */
    public void onTouch(MotionEvent event); 
}
