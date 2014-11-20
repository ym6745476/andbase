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
package com.ab.download;


// TODO: Auto-generated Javadoc
/**
 * 描述：下载文件.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2012-6-25 下午4:20:55
 */
public class DownFile {
	
	/** 唯一标识. */
	private int _ID;
	
	/** 图标. */
	private String icon;
	
	/** 文件名称. */
	private String name;
	
	/** 文件简介. */
	private String description;
	
	/** 如果是apk文件表示包名. */
	private String pakageName;
	
	/** 1表示已下载完成 0表示未开始下载 2表示已开始下载. */
	private int state;
	
	/** 文件下载路径. */
	private String downUrl;
	
	/** 文件保存路径. */
	private String downPath;
	
	/** 文件当前下载大小. */
	private long downLength;
	
	/** 文件总大小. */
	private long totalLength;
	
	/** 文件类型后缀.tmp. */
	private String suffix;

	/**
	 * Gets the  id.
	 *
	 * @return the  id
	 */
	public int get_ID() {
		return _ID;
	}

	/**
	 * Sets the  id.
	 *
	 * @param _ID the new  id
	 */
	public void set_ID(int _ID) {
		this._ID = _ID;
	}

	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the pakage name.
	 *
	 * @return the pakage name
	 */
	public String getPakageName() {
		return pakageName;
	}

	/**
	 * Sets the pakage name.
	 *
	 * @param pakageName the new pakage name
	 */
	public void setPakageName(String pakageName) {
		this.pakageName = pakageName;
	}

	/**
	 * Gets the state.
	 *
	 * @return the state
	 */
	public int getState() {
		return state;
	}

	/**
	 * Sets the state.
	 *
	 * @param state the new state
	 */
	public void setState(int state) {
		this.state = state;
	}

	/**
	 * Gets the down url.
	 *
	 * @return the down url
	 */
	public String getDownUrl() {
		return downUrl;
	}

	/**
	 * Sets the down url.
	 *
	 * @param downUrl the new down url
	 */
	public void setDownUrl(String downUrl) {
		this.downUrl = downUrl;
	}

	/**
	 * Gets the down path.
	 *
	 * @return the down path
	 */
	public String getDownPath() {
		return downPath;
	}

	/**
	 * Sets the down path.
	 *
	 * @param downPath the new down path
	 */
	public void setDownPath(String downPath) {
		this.downPath = downPath;
	}

	/**
	 * Gets the down length.
	 *
	 * @return the down length
	 */
	public long getDownLength() {
		return downLength;
	}

	/**
	 * Sets the down length.
	 *
	 * @param downLength the new down length
	 */
	public void setDownLength(long downLength) {
		this.downLength = downLength;
	}

	/**
	 * Gets the total length.
	 *
	 * @return the total length
	 */
	public long getTotalLength() {
		return totalLength;
	}

	/**
	 * Sets the total length.
	 *
	 * @param totalLength the new total length
	 */
	public void setTotalLength(long totalLength) {
		this.totalLength = totalLength;
	}

	/**
	 * Gets the suffix.
	 *
	 * @return the suffix
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the suffix.
	 *
	 * @param suffix the new suffix
	 */
	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}
	
	
	
	
}
