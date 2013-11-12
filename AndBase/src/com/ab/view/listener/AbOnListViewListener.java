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
package com.ab.view.listener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving abOnListView events.
 * The class that is interested in processing a abOnListView
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAbOnListViewListener<code> method. When
 * the abOnListView event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AbOnListViewEvent
 */
public interface AbOnListViewListener {
	
	/**
	 * On refresh.
	 */
	public void onRefresh();

	/**
	 * On load more.
	 */
	public void onLoadMore();
}
