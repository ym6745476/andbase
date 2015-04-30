package com.ab.view.expandtabview;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ab.model.AbMenuItem;


public class AbTabView1 extends RelativeLayout{

	private Context mContext;
	private ListView listView;
	private List<AbMenuItem>  menuItems;
	private AbTabListAdapter adapter;
	private int defaultId = -1;
	private String showText;
	private OnItemSelectListener mOnItemSelectListener;
	private int selectResId, selectorResId;
	
	public String getShowText() {
		return showText;
	}

	public AbTabView1(Context context,List<AbMenuItem> menuItems,int defaultId,int selectResId, int selectorResId) {
		super(context);
		this.menuItems = menuItems;
		this.defaultId = defaultId;
		this.selectResId = selectResId;
		this.selectorResId = selectorResId;
		init(context);
	}

	public AbTabView1(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public AbTabView1(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mContext = context;
		listView = new ListView(context);
		listView.setCacheColorHint(Color.parseColor("#00000000"));
		listView.setHorizontalScrollBarEnabled(false);
		listView.setVerticalScrollBarEnabled(false);
		listView.setDivider(new ColorDrawable(Color.parseColor("#D3D3D3")));
		listView.setDividerHeight(1);
		this.addView(listView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT ));
		adapter = new AbTabListAdapter(context, menuItems, selectResId, selectorResId);
		if (defaultId != -1) {
			for (int i = 0; i < menuItems.size(); i++) {
				if (menuItems.get(i).getId()== defaultId) {
					adapter.setSelectedPositionNoNotify(i);
					showText = menuItems.get(i).getText();
					break;
				}
			}
		}
		listView.setAdapter(adapter);
		adapter.setOnItemClickListener(new AbTabListAdapter.OnItemClickListener() {

			@Override
			public void onItemClick(View view, int position) {

				if (mOnItemSelectListener != null) {
					showText = menuItems.get(position).getText();
					mOnItemSelectListener.itemSelected(position);
				}
			}
		});
	}

	public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
		mOnItemSelectListener = onItemSelectListener;
	}

	public interface OnItemSelectListener {
		public void itemSelected(int position);
	}
	
	public void notifyDataSetChanged(){
		adapter.notifyDataSetChanged();
	}
}
