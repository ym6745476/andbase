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

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

// TODO: Auto-generated Javadoc
/**
 * 描述：Table控件适配器.
 *
 * @author zhaoqp
 * @date：2013-1-28 下午3:55:19
 * @version v1.0
 */

public class AbTableArrayAdapter extends BaseAdapter {

	/** The context. */
	private Context context;
	//缓存View
	/** The table view. */
	private ArrayList<View> tableView;
	
	/** 标题内容数组. */
	private String[] titles;
	//表格内容
	/** The contents. */
	private List<String[]>  contents;
	//单元格数
	/** The columns. */
	private int columns;
	//单元格宽度
	/** The cell width. */
	private int[] cellWidth;
	
	/** The cell types. */
	private int[] cellTypes;
	//表格资源 （索引0标题背景，1内容列表背景。2表格背景）
	/** The table resource. */
	private int[] tableResource;
	//行高度
	/** The row height. */
	private int[] rowHeight;
	//行文字大小（索引0标题，1内容列表）
	/** The row text size. */
	private int[] rowTextSize;
	//行文字颜色（索引0标题，1内容列表）
	/** The row text color. */
	private int[] rowTextColor;
	
	/** The table. */
	private AbTable table;

	/**
	 * Table控件适配器.
	 *
	 * @param context the context
	 * @param table  Table对象
	 */
	public AbTableArrayAdapter(Context context,AbTable table) {
		this.context = context;
		tableView = new ArrayList<View>();
		setTable(table);
	}
	
	/**
	 * 描述：更新Table内容.
	 *
	 * @param table the new table
	 */
	public void setTable(AbTable table) {
		this.table = table;
		this.titles = table.getTitles();
		this.contents = table.getContents();
		this.cellTypes = table.getCellTypes();
		this.cellWidth = table.getCellWidth();
		this.rowHeight = table.getRowHeight();
		this.rowTextSize = table.getRowTextSize();
		this.rowTextColor = table.getRowTextColor();
		this.tableResource = table.getTableResource();
		this.columns = this.cellTypes.length;
		tableView.clear();
	}
	

	/**
	 * 描述：获取数量.
	 *
	 * @return the count
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return contents.size()+1;
	}

	/**
	 * 描述：获取位置.
	 *
	 * @param position the position
	 * @return the item id
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 描述：获取索引位置的View.
	 *
	 * @param position the position
	 * @return the item
	 * @see android.widget.Adapter#getItem(int)
	 */
	public AbTableItemView getItem(int position) {
		return (AbTableItemView)tableView.get(position);
	}

	/**
	 * 描述：绘制View.
	 *
	 * @param position the position
	 * @param convertView the convert view
	 * @param parent the parent
	 * @return the view
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView == null){
			//标题
			if(position==0){
				AbTableCell[] tableCells = new AbTableCell[columns];
				for (int j = 0; j < columns; j++) {
					tableCells[j] = new AbTableCell(titles[j],cellWidth[j],cellTypes[j]);
				}
				convertView = new AbTableItemView(context,this,position,new AbTableRow(tableCells,rowHeight[0],rowTextSize[0],rowTextColor[0],tableResource[1]),table);
				convertView.setBackgroundResource(tableResource[0]);
			}else{
				//内容
				AbTableCell[] tableCells = new AbTableCell[columns];
				String []content = contents.get(position-1);
				int size = contents.size();
				if(size>0){
						for (int j = 0; j < columns; j++) {
							tableCells[j] = new AbTableCell(content[j],cellWidth[j],cellTypes[j]);
						}
						convertView = new AbTableItemView(context,this,position,new AbTableRow(tableCells,rowHeight[1],rowTextSize[1],rowTextColor[1],tableResource[3]),table);
				}else{
					//默认显示一行无数据
			    }
				convertView.setBackgroundResource(tableResource[2]);
			}
		}else{
			if(position==0){
				//将值重置
			    AbTableItemView rowView = (AbTableItemView)convertView;
				//内容
				AbTableCell[] tableCells = new AbTableCell[columns];
				for (int j = 0; j < columns; j++) {
					tableCells[j] = new AbTableCell(titles[j],cellWidth[j],cellTypes[j]);
				}
				rowView.setTableRowView(position,new AbTableRow(tableCells,rowHeight[0],rowTextSize[0],rowTextColor[0],tableResource[1]));
				convertView.setBackgroundResource(tableResource[0]);
			}else{
			    //将值重置
			    AbTableItemView rowView = (AbTableItemView)convertView;
				//内容
				AbTableCell[] tableCells = new AbTableCell[columns];
				String []content = contents.get(position-1);
				int size = contents.size();
				if(size>0){
						for (int j = 0; j < columns; j++) {
							tableCells[j] = new AbTableCell(content[j],cellWidth[j],cellTypes[j]);
						}
						rowView.setTableRowView(position,new AbTableRow(tableCells,rowHeight[1],rowTextSize[1],rowTextColor[1],tableResource[3]));
				}else{
					//默认显示一行无数据
			    }
				convertView.setBackgroundResource(tableResource[2]);
			}
		}
		//将新的View维护到tableView
		if(tableView.size()>position){
			tableView.set(position, convertView);
		}else{
			tableView.add(position,convertView);
		}
		return convertView;
	}
	
	/**
	 * 增加一行.
	 *
	 * @param row 行的数据
	 */
	public void addItem(String[] row) {
		contents.add(row);
        this.notifyDataSetChanged();
	}
	
}

