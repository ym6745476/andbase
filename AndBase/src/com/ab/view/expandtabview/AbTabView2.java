package com.ab.view.expandtabview;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ab.model.AbMenuItem;

public class AbTabView2 extends LinearLayout{
	
	private ListView listView1;
	private ListView listView2;
	
	private AbTabListAdapter listViewAdapter1;
	private AbTabListAdapter listViewAdapter2;
	
	private List<AbMenuItem> groups;
	private List<ArrayList<AbMenuItem>> childrens;
	
	/**当前显示的分类2*/
	private List<AbMenuItem> childrenItem = new ArrayList<AbMenuItem>();
	
	private OnItemSelectListener mOnItemSelectListener;
	private int selectGroupResId, selectorGroupResId;
	private int selectChildrenResId, selectorChildrenResId;
	
	private int defaultGroupId = -1;
	private int defaultChildrenId = -1;
	
	private int defaultGroupPosition = 0;
	private int defaultChildrenPosition = 0;
	
	private String showString;

	public AbTabView2(Context context,List<AbMenuItem> groups,List<ArrayList<AbMenuItem>> childrens,int defaultGroupId,int defaultChildrenId,int selectGroupResId, int selectorGroupResId,int selectChildrenResId, int selectorChildrenResId) {
		super(context);
		this.groups = groups;
		this.childrens = childrens;
		this.selectGroupResId = selectGroupResId;
		this.selectorGroupResId = selectorGroupResId;
		this.selectChildrenResId = selectChildrenResId;
		this.selectorChildrenResId = selectorChildrenResId;
		init(context);
	}

	public AbTabView2(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(int groupId, int childId) {
		if (groupId == -1 || childId == -1) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).getId() == groupId) {
				listViewAdapter1.setSelectedPosition(i);
				childrenItem.clear();
				if (i < childrens.size()) {
					childrenItem.addAll(childrens.get(i));
				}
				defaultGroupPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).getId()==childId) {
				listViewAdapter2.setSelectedPosition(j);
				defaultChildrenPosition = j;
				break;
			}
		}
		
		if (defaultChildrenPosition < childrenItem.size()){
			showString = childrenItem.get(defaultChildrenPosition).getText();
		}
		
		setDefaultSelect();
	}

	private void init(Context context) {
		this.setOrientation(LinearLayout.HORIZONTAL);
		listView1 = new ListView(context);
		listView1.setCacheColorHint(Color.parseColor("#00000000"));
		listView1.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView1.setDividerHeight(1);
		listView1.setHorizontalScrollBarEnabled(false);
		listView1.setVerticalScrollBarEnabled(false);
		this.addView(listView1,new LayoutParams(0,LayoutParams.MATCH_PARENT,1));
		
		View line = new View(context);
		line.setBackgroundColor(Color.parseColor("#ebebeb"));
		this.addView(line,new LayoutParams(1,LayoutParams.MATCH_PARENT));
		
		listView2 = new ListView(context);
		listView2.setCacheColorHint(Color.parseColor("#00000000"));
		listView2.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView2.setDividerHeight(1);
		listView2.setHorizontalScrollBarEnabled(false);
		listView2.setVerticalScrollBarEnabled(false);
		this.addView(listView2,new LayoutParams(0,LayoutParams.MATCH_PARENT,1 ));

		
		listViewAdapter1 = new AbTabListAdapter(context, groups,selectGroupResId,selectorGroupResId);
		
		if (defaultGroupId != -1) {
			for (int i = 0; i < groups.size(); i++) {
				if (groups.get(i).getId()== defaultGroupId) {
					listViewAdapter1.setSelectedPositionNoNotify(i);
					break;
				}
			}
		}
		
		listView1.setAdapter(listViewAdapter1);
		listViewAdapter1.setOnItemClickListener(new AbTabListAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				if (position < groups.size()) {
					childrenItem.clear();
					if(childrens.size() > position){
						childrenItem.addAll(childrens.get(position));
					}
					listViewAdapter2.notifyDataSetChanged();
				}
			}
		});
		
		if (defaultGroupId != -1) {
			for (int i = 0; i < groups.size(); i++) {
				if (groups.get(i).getId()== defaultGroupId) {
					listViewAdapter1.setSelectedPositionNoNotify(i);
					defaultGroupPosition = i;
					break;
				}
			}
		}
		
		if (defaultChildrenId != -1) {
			if(defaultGroupId==-1){
				
			}else{
				for (int i = 0; i < childrens.get(defaultGroupPosition).size(); i++) {
					if (childrens.get(i).get(defaultGroupPosition).getId()== defaultChildrenId) {
						defaultChildrenPosition = i;
						break;
					}
				}
			}
		}
		
		if (defaultChildrenPosition < childrens.size()){
			childrenItem.addAll(childrens.get(defaultChildrenPosition));
	    }
	    listViewAdapter2 = new AbTabListAdapter(context, childrenItem,selectChildrenResId,selectorChildrenResId);
	    listViewAdapter2.setSelectedPositionNoNotify(defaultChildrenPosition);
	    listView2.setAdapter(listViewAdapter2);
	    listViewAdapter2.setOnItemClickListener(new AbTabListAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, final int position) {
				showString = childrenItem.get(position).getText();
				if (mOnItemSelectListener != null) {
					mOnItemSelectListener.itemSelected(position);
				}

			}
		});
	   
		if (defaultChildrenPosition < childrenItem.size()){
			showString = childrenItem.get(defaultChildrenPosition).getText();
		}
		
		setDefaultSelect();

	}

	public void setDefaultSelect() {
		listView1.setSelection(defaultGroupPosition);
		listView2.setSelection(defaultChildrenPosition);
	}

	public String getShowText() {
		return showString;
	}

	public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
		mOnItemSelectListener = onItemSelectListener;
	}

	public interface OnItemSelectListener {
		public void itemSelected(int position);
	}

	
	public void notifyDataSetChangedGroup(){
		 listViewAdapter1.notifyDataSetChanged();
	}
	
	public void notifyDataSetChangedChildren(){
		 listViewAdapter2.notifyDataSetChanged();
	}
	
}
