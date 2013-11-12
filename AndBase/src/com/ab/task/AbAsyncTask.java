package com.ab.task;

import android.os.AsyncTask;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbAsyncTask.java 
 * 描述：下载数据的任务实现，单次下载
 * @author zhaoqp
 * @date：2013-9-2 下午12:52:13
 * @version v1.0
 */
public class AbAsyncTask extends AsyncTask<AbTaskItem, Integer, AbTaskItem> {
	
	public AbAsyncTask() {
		super();
	}

	@Override
	protected AbTaskItem doInBackground(AbTaskItem... items) {
		AbTaskItem item = items[0];
		if (item.callback != null) { 
			item.callback.get();
        } 
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(AbTaskItem item) {
		if (item.callback != null) { 
		    item.callback.update(); 
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
