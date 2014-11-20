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
package com.ab.model;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbMenuItem.java 
 * 描述：菜单实体
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-13 上午9:00:52
 */
public class AbMenuItem {
	
	/** 菜单的id. */
	private int id;

	/** 菜单的图标id. */
	private int iconId;

	/** 菜单的文本. */
	private String text;
	
	/** 菜单的描述. */
	private String mark;
	

	/**
	 * Instantiates a new ab menu item.
	 *
	 * @param id the id
	 * @param text the text
	 */
	public AbMenuItem(int id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	
	/**
	 * Instantiates a new ab menu item.
	 *
	 * @param text the text
	 */
	public AbMenuItem(String text) {
		super();
		this.text = text;
	}
	
	/**
	 * Instantiates a new ab menu item.
	 *
	 */
	public AbMenuItem() {
		super();
	}
	

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Gets the icon id.
	 *
	 * @return the icon id
	 */
	public int getIconId() {
		return iconId;
	}

	/**
	 * Sets the icon id.
	 *
	 * @param iconId the new icon id
	 */
	public void setIconId(int iconId) {
		this.iconId = iconId;
	}

	/**
	 * Gets the text.
	 *
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets the text.
	 *
	 * @param text the new text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the mark.
	 *
	 * @return the mark
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * Sets the mark.
	 *
	 * @param mark the new mark
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}
	
	

}
