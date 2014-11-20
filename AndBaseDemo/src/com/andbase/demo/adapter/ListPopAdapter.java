package com.andbase.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ab.model.AbMenuItem;
import com.andbase.R;


public class ListPopAdapter extends BaseAdapter {

	private Context context;

	private List<AbMenuItem> list;

	private int itemResource;
	
	public ListPopAdapter(Context context, List<AbMenuItem> list,int itemResource) {
		this.context = context;
		this.list = list;
		this.itemResource = itemResource;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {

		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup viewGroup) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(itemResource, null);
			holder = new ViewHolder();
			holder.itemText = (TextView) convertView.findViewById(R.id.pop_item);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AbMenuItem item = list.get(position);
		holder.itemText.setText(item.getText());
		
		return convertView;
	}

	static class ViewHolder {
		TextView itemText;
	}

}
