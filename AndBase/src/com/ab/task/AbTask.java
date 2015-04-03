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
package com.ab.task;
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
import java.util.List;

import android.os.AsyncTask;
// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbAsyncTask.java 
 * 描述：下载数据的任务实现
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-9-2 下午12:52:13
 */
public class AbTask extends AsyncTask<AbTaskItem, Integer, AbTaskItem> {
	
	/** 监听器. */
	private AbTaskListener listener; 
	
	/** 结果. */
	private Object result;
	
	/**
	 * 初始化Task.
	 */
	public AbTask() {
		super();
	}
	
	/**
	 * 实例化.
	 */
	public static AbTask newInstance() {
		AbTask mAbTask = new AbTask();
		return mAbTask;
	}
	
	/**
	 * 
	 * 执行任务.
	 * @param items
	 * @return
	 */
	@Override
	protected AbTaskItem doInBackground(AbTaskItem... items) {
		AbTaskItem item = items[0];
		this.listener = item.getListener();
		if (this.listener != null) { 
			if(this.listener instanceof AbTaskListListener){
				result = ((AbTaskListListener)this.listener).getList(); 
        	}else if(this.listener instanceof AbTaskObjectListener){
        		result = ((AbTaskObjectListener)this.listener).getObject(); 
        	}else{
        		this.listener.get(); 
        	}
        } 
		return item;
	}

	/**
	 * 
	 * 取消.
	 */
	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	/**
	 * 
	 * 执行完成.
	 * @param item
	 */
	@Override
	protected void onPostExecute(AbTaskItem item) {
		if (this.listener != null) {
			if(this.listener instanceof AbTaskListListener){
        		((AbTaskListListener)this.listener).update((List<?>)result); 
        	}else if(this.listener instanceof AbTaskObjectListener){
        		((AbTaskObjectListener)this.listener).update(result); 
        	}else{
        		this.listener.update(); 
        	}
		}
	}

	/**
	 * 
	 * 执行前.
	 */
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	/**
	 * 
	 * 进度更新.
	 * @param values
	 */
	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		if (this.listener != null) { 
			this.listener.onProgressUpdate(values);
		}
	}

}
