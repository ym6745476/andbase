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

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.ab.view.listener.AbOnItemClickListener;
// TODO: Auto-generated Javadoc
/**
 * 描述：表格控件实体类
 *  <p>(1)标题配置
	<p>titles = new String[] { "标题1", "标题2", "标题3", "标题4","标题5"};
	<p>(2)内容列表配置(初始为空的)
	<p>contents = new ArrayList<String[]>();
	<p>(3)列类型配置（参照AbCellType）
	<p>cellTypes = new int[] { AbCellType.STRING, AbCellType.STRING, AbCellType.STRING, AbCellType.STRING,AbCellType.STRING};
	<p>(4)列宽配置(%) 超过100% 可以横向滑动
	<p>cellWidth = new int[] {20,50,10,20,50};
	<p>(5)行高（索引0：标题高，1：内容列表高）
	<p>rowHeight = new int[] { 35, 35 };
	<p>(6)行文字大小（索引0标题，1内容列表）
	<p>rowTextSize = new int[] { 15, 12};
	<p>(7)行文字颜色（索引0标题，1内容列表）
	<p>rowTextColor = new int[] {Color.rgb(255, 255, 255),Color.rgb(113, 113, 113) };
	<p>(8)背景资源（索引0标题行背景，1标题单元格背景，2内容列表行背景。3表格内容单元格背景）
	<p>tableResource = new int[] {android.R.color.transparent,R.drawable.title_cell,android.R.color.transparent,R.drawable.content_cell};
	<p> (9)表格实体（通过newAbTable实例化并初始化列数，可自动完成AbTable的配置）
	<p>table = AbTable.newAbTable(this,5); 
	<p>table.setTitles(titles);
	<p>table.setContents(contents);
	<p>table.setCellTypes(cellTypes);
	<p>table.setCellWidth(cellWidth);
	<p>table.setRowHeight(rowHeight);
	<p>table.setRowTextSize(rowTextSize);
	<p>table.setTableResource(tableResource);
	<p>table.setRowTextColor(rowTextColor);
	<p>(10)AbTableArrayAdapter对象
	<p>mAbTableArrayAdapter = new AbTableArrayAdapter(this, table);
	<p>(12)ListView（布局参照）：
	<p> &nbsp;&nbsp; < HorizontalScrollView
    <p> &nbsp;&nbsp;&nbsp;&nbsp; android:id="@+id/horView"
    <p> &nbsp;&nbsp;&nbsp;&nbsp; android:layout_width="fill_parent"
    <p> &nbsp;&nbsp;&nbsp;&nbsp; android:layout_height="fill_parent" >
    <p>
    <p> &nbsp;&nbsp;&nbsp;&nbsp; <ListView
    <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  android:id="@+id/mListView"
    <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:layout_width="wrap_content"
    <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:layout_height="wrap_content"
    <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:cacheColorHint="#00000000"
    <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:divider="@android:color/transparent"
    <p> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; android:dividerHeight="0dip" >
    <p> &nbsp;&nbsp;&nbsp;&nbsp;  < /ListView>
    <p> &nbsp;&nbsp;< /HorizontalScrollView>
	<p>mListView = (ListView)findViewById(R.id.mListView);
	<p>(11)设置Adapter
	<p>mListView.setAdapter(tableAdapter);
 * @author zhaoqp
 * @date：2013-1-28 下午3:54:41
 * @version v1.0
 */
public class AbTable {
	
	/** AbTable实例. */
	private static AbTable mAbTable = null;
	
	/** 屏幕的宽. */
	private static int mScreenWidth = 0;
	
	/** 屏幕的高. */
	private static int mScreenHeight = 0;
	
	/** 标题数组. */
	private String[] titles;
	
	/** 内容列表数组. */
	private List<String[]> contents;
	
	/** 单元格类型数组. */
	private int[] cellTypes;
	
    /** 单元格宽度数组. */
	private int[] cellWidth;
	
	/** 行高度数组（索引0标题，1内容列表）. */
	private int[]  rowHeight;
	
	/** 行文字大小数组（索引0标题，1内容列表）. */
	private int[] rowTextSize;
	
	/** 行文字颜色数组（索引0标题，1内容列表）. */
	private int[] rowTextColor;
	
	/** 表格资源数组（索引0标题行背景，1标题单元格背景，2内容列表行背景。3表格内容单元格背景）. */
	private int[] tableResource;
	
	/** The item cell touch listener. */
	private AbOnItemClickListener itemCellTouchListener;
	
	/** The item cell check listener. */
	private AbOnItemClickListener itemCellCheckListener;
	
	
	/**
	 * 描述：表格对象构造.
	 *
	 * @param context context对象
	 * @param columnSize 列数
	 * @return the ab table
	 */
	public static AbTable newAbTable(Context context,int columnSize) {
		if(columnSize<=0){
			Toast.makeText(context,"请设置表格的列数>0!", Toast.LENGTH_SHORT).show();
		    return null;
		}
		WindowManager manager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		mScreenWidth = display.getWidth();
		mScreenHeight = display.getHeight();
		
		mAbTable = new AbTable();
		//标题配置
		mAbTable.titles = new String[columnSize];
		for(int i=0;i<columnSize;i++){
			mAbTable.titles[i] = "标题"+i;
		}
		//列类型配置
		mAbTable.cellTypes = new int[columnSize];
		for(int i=0;i<columnSize;i++){
			mAbTable.cellTypes[i] = AbCellType.STRING;
		}
		
		//列宽配置(%)
		mAbTable.cellWidth = new int[columnSize];
		for(int i=0;i<columnSize;i++){
			mAbTable.cellWidth[i] = mScreenWidth/columnSize;
		}
		
		//行高配置（索引0标题，1内容列表）
		mAbTable.rowHeight = new int[] { 30, 30 };
		//行文字大小（索引0标题，1内容列表）
		mAbTable.rowTextSize = new int[] { 18, 16};
		//行文字颜色（索引0标题，1内容列表）
		mAbTable.rowTextColor = new int[] { Color.rgb(113, 113, 113), Color.rgb(113, 113, 113) };
		//背景资源（索引0标题行背景，1标题单元格背景，2内容列表行背景。3表格内容单元格背景）
		mAbTable.tableResource = new int[] {android.R.color.transparent,android.R.color.transparent,android.R.color.transparent,android.R.color.transparent};
		
		return mAbTable;
	}


	/**
	 * 描述：获取表格的标题.
	 *
	 * @return the titles
	 */
	public String[] getTitles() {
		return titles;
	}

	/**
	 * 描述：设置表格的标题.
	 *
	 * @param titles 表格的标题数组
	 */
	public void setTitles(String[] titles) {
		for(int i=0;i<titles.length;i++){
			mAbTable.titles[i] = titles[i];
		}
	}

	/**
	 * Gets the contents.
	 *
	 * @return the contents
	 */
	public List<String[]> getContents() {
		return contents;
	}
	
	/**
	 * 描述：设置列表内容的数据.
	 *
	 * @param contents 列表内容的数据
	 */
	public void setContents(List<String[]> contents) {
		this.contents = contents;
	}

	/**
	 * Gets the cell types.
	 *
	 * @return the cell types
	 */
	public int[] getCellTypes() {
		return cellTypes;
	}

	/**
	 * 描述：列类型配置（参照AbCellType）.
	 *
	 * @param cellTypes 列类型
	 */
	public void setCellTypes(int[] cellTypes) {
		this.cellTypes = cellTypes;
	}

	/**
	 * Gets the cell width.
	 *
	 * @return the cell width
	 */
	public int[] getCellWidth() {
		return cellWidth;
	}

    /**
     * 描述：列宽配置(%) 超过100% 可以横向滑动.
     *
     * @param cellWidth 列宽的百分比
     */
	public void setCellWidth(int[] cellWidth) {
		for(int i=0;i<cellWidth.length;i++){
			mAbTable.cellWidth[i] = mScreenWidth*cellWidth[i]/100;
		}
	}

	/**
	 * Gets the row height.
	 *
	 * @return the row height
	 */
	public int[] getRowHeight() {
		return rowHeight;
	}

	/**
	 * 描述：设置行高（索引0：标题高，1：内容列表高）.
	 *
	 * @param rowHeight 行高
	 */
	public void setRowHeight(int[] rowHeight) {
		for(int i=0;i<rowHeight.length;i++){
			mAbTable.rowHeight[i] = rowHeight[i];
		}
	}

	/**
	 * Gets the row text size.
	 *
	 * @return the row text size
	 */
	public int[] getRowTextSize() {
		return rowTextSize;
	}

	/**
	 * 描述：设置行文字的大小（索引0标题，1内容列表）.
	 *
	 * @param rowTextSize  行文字的大小
	 */
	public void setRowTextSize(int[] rowTextSize) {
		for(int i=0;i<rowTextSize.length;i++){
			mAbTable.rowTextSize[i] = rowTextSize[i];
		}
	}


	/**
	 * Gets the row text color.
	 *
	 * @return the row text color
	 */
	public int[] getRowTextColor() {
		return rowTextColor;
	}

	/**
	 * 描述：设置行文字的颜色.
	 *
	 * @param rowTextColor  行文字的颜色
	 */
	public void setRowTextColor(int[] rowTextColor) {
		for(int i=0;i<rowTextColor.length;i++){
			mAbTable.rowTextColor[i] = rowTextColor[i];
		}
	}


	/**
	 * Gets the table resource.
	 *
	 * @return the table resource
	 */
	public int[] getTableResource() {
		return tableResource;
	}

	/**
	 * 描述：表格的背景资源（索引0标题行背景，1标题单元格背景，2内容列表行背景。3表格内容单元格背景）.
	 *
	 * @param tableResource the new table resource
	 */
	public void setTableResource(int[] tableResource) {
		for(int i=0;i<tableResource.length;i++){
			this.tableResource[i] = tableResource[i];
		}
	}


	/**
	 * Gets the item cell touch listener.
	 *
	 * @return the item cell touch listener
	 */
	public AbOnItemClickListener getItemCellTouchListener() {
		return itemCellTouchListener;
	}

	/**
	 * 描述：图片cell，一行中的某一个cell被点击.
	 *
	 * @param itemCellTouchListener the new item cell touch listener
	 */
	public void setItemCellTouchListener(AbOnItemClickListener itemCellTouchListener) {
		this.itemCellTouchListener = itemCellTouchListener;
	}


	/**
	 * Gets the item cell check listener.
	 *
	 * @return the item cell check listener
	 */
	public AbOnItemClickListener getItemCellCheckListener() {
		return itemCellCheckListener;
	}

	/**
	 * 描述：复选框cell，一行中的某一个cell被点击.
	 *
	 * @param itemCellCheckListener the new item cell check listener
	 */
	public void setItemCellCheckListener(AbOnItemClickListener itemCellCheckListener) {
		this.itemCellCheckListener = itemCellCheckListener;
	}
	
	
	
	 
	
}

