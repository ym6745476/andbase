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
public class AbTask extends AsyncTask<AbTaskItem, Integer, AbTaskItem> {
	
	public AbTask() {
		super();
	}

	@Override
	protected AbTaskItem doInBackground(AbTaskItem... items) {
		AbTaskItem item = items[0];
		if (item.listener != null) { 
			item.listener.get();
        } 
		return item;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(AbTaskItem item) {
		if (item.listener != null) { 
		    item.listener.update(); 
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
