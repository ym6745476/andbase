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
package com.ab.view.table;

// TODO: Auto-generated Javadoc
/**
 * 描述：表格的一行.
 *
 * @author zhaoqp
 * @date：2013-1-28 下午3:57:44
 * @version v1.0
 */
public class AbTableRow {
	
	/** 行的所有列. */
	public AbTableCell[] cells;
	
	/** 行高. */
	public int height;
	
	/** 该行单元格的背景. */
	public int backgroundResource;
	
	/** 字体大小. */
	public int textSize;
	
	/** 字体颜色. */
	public int textColor;

	/**
	 * 表格的一行构造.
	 *
	 * @param cells   行的所有列
	 * @param height  行高
	 * @param textSize 字体大小
	 * @param textColor 字体颜色
	 * @param backgroundResource 该行单元格的背景
	 */
	public AbTableRow(AbTableCell[] cells,int height,int textSize,int textColor,int backgroundResource) {
	  this.cells = cells;
	  this.height = height;
	  this.textSize = textSize;
	  this.textColor = textColor;
	  this.backgroundResource = backgroundResource;
	}

	/**
	 * 行中的单元格数.
	 *
	 * @return the cell size
	 */
	public int getCellSize() {
	    return cells.length;
	}

	/**
	 * 根据列索引回去列的值.
	 *
	 * @param index 从0开始
	 * @return 单元格对象
	 */
	public AbTableCell getCellValue(int index) {
	   if (index >= cells.length)
	      return null;
	   return cells[index];
	}
}
