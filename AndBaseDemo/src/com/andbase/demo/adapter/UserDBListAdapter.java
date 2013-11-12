package com.andbase.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andbase.R;
import com.andbase.demo.activity.DBInsideSampleActivity;
import com.andbase.demo.activity.DBObjectActivity;
import com.andbase.demo.activity.DBSDSampleActivity;
import com.andbase.demo.model.LocalUser;

public class UserDBListAdapter extends BaseAdapter {

	private Context mContext;
	// xml转View对象
	private LayoutInflater mInflater;
	// 列表展现的数据
	private List<LocalUser> mUserlist;

	/**
	 * 构造方法
	 * @param context
	 * @param data 列表展现的数据
	 * @param resource 单行的布局
	 * @param from Map中的key
	 * @param to view的id
	 */
	public UserDBListAdapter(Context context, List<LocalUser> userlist) {
		mContext = context;
		mUserlist = userlist;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mUserlist.size();
	}

	@Override
	public Object getItem(int position) {
		return mUserlist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		// 当前索引
		final int index = position;
		
		if (convertView == null) {
			// 使用自定义的list_items作为Layout
			convertView = mInflater.inflate(R.layout.db_list_items, parent, false);
			// 减少findView的次数
			holder = new ViewHolder();
			// 初始化布局中的元素
			holder.itemsTitle = ((TextView) convertView.findViewById(R.id.itemsTitle));
			holder.itemsText = ((EditText) convertView.findViewById(R.id.itemsText));
			holder.modifyBtn = ((Button) convertView.findViewById(R.id.modBtn));
			holder.delBtn = ((Button) convertView.findViewById(R.id.delBtn));
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// 获取该行的数据
		final LocalUser user = mUserlist.get(position);
		// 设置数据到View
		holder.itemsTitle.setText(String.valueOf(user.get_id()));
		holder.itemsText.setText(user.getName());
		// 修改和删除按钮
		// 释放焦点
		holder.modifyBtn.setFocusable(false);
		holder.delBtn.setFocusable(false);
		// 修改按钮事件
		holder.modifyBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				 //修改数据
				 user.setName(holder.itemsText.getText().toString().trim());
				 
				 if(mContext instanceof DBInsideSampleActivity){
					 ((DBInsideSampleActivity)mContext).updateData(user);
				 }else if(mContext instanceof DBSDSampleActivity){
					 ((DBSDSampleActivity)mContext).updateData(user);
					
				 }else{
					 ((DBObjectActivity)mContext).updateData(user);
				 }
				 
			}
		});
		
		// 删除按钮事件
		holder.delBtn.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mContext instanceof DBInsideSampleActivity){
					 ((DBInsideSampleActivity)mContext).delData(Integer.parseInt(holder.itemsTitle.getText().toString()));
				 }else if(mContext instanceof DBSDSampleActivity){
					 ((DBSDSampleActivity)mContext).delData(Integer.parseInt(holder.itemsTitle.getText().toString()));
				 }else{
					 ((DBObjectActivity)mContext).delData(Integer.parseInt(holder.itemsTitle.getText().toString()));
				 }
			}
		});
		return convertView;
	}

	/**
	 * 增加一条并改变视图
	 * @param item
	 */
	public void addItem(int position,LocalUser user) {
		mUserlist.add(position,user);
		notifyDataSetChanged();
	}
	
	/**
	 * 增加一条并改变视图
	 * @param item
	 */
	public void addItem(LocalUser user) {
		mUserlist.add(0,user);
		notifyDataSetChanged();
	}

	/**
	 * View元素
	 */
	static class ViewHolder {
		TextView itemsTitle;
		EditText itemsText;
		Button modifyBtn;
		Button delBtn;
	}

}
