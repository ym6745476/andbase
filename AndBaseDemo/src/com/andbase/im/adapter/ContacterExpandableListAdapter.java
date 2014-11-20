package com.andbase.im.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andbase.R;
import com.andbase.im.model.IMRosterGroup;
import com.andbase.im.model.IMUser;

public class ContacterExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private List<IMRosterGroup> groups = null;
	private LayoutInflater inflater;

	public ContacterExpandableListAdapter(Context context, List<IMRosterGroup> groups) {
		this.context = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.groups = groups;
	}

	public void setContacter(List<IMRosterGroup> groups) {
		this.groups = groups;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).getUsers().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		
		ChildHolder childHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.im_contacter_child_item, null);
			childHolder = new ChildHolder();
			childHolder.mood = (TextView) convertView.findViewById(R.id.mood);
			childHolder.userName = (TextView) convertView
					.findViewById(R.id.user_name);
			childHolder.image = (ImageView) convertView
					.findViewById(R.id.child_item_head);

			convertView.setTag(childHolder);
		} else {
			childHolder = (ChildHolder) convertView.getTag();
		}
		
		IMUser user = groups.get(groupPosition).getUsers().get(childPosition);
		
		//childHolder.mood.setText("");
		childHolder.userName.setText(user.getName()
				+ (user.isAvailable() ? "(在线)" : "(离线)"));
		if (user.isAvailable()) {
		    childHolder.image.setImageResource(R.drawable.user_online);
			childHolder.userName.setTextColor(context.getResources().getColor(R.color.blue));
			childHolder.mood.setTextColor(Color.BLACK);
		} else {
		    childHolder.image.setImageResource(R.drawable.user_offline);
			childHolder.userName.setTextColor(Color.GRAY);
			childHolder.mood.setTextColor(Color.GRAY);
		}

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).getUsers().size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return groups.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		GroupHolder groupHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.im_contacter_group_item, null);
			groupHolder = new GroupHolder();
			groupHolder.onlineNumber = (TextView) convertView
					.findViewById(R.id.online_number);
			groupHolder.groupName = (TextView) convertView
					.findViewById(R.id.group_name);
			convertView.setTag(groupHolder);
		} else { 
			groupHolder = (GroupHolder) convertView.getTag();
		}

		groupHolder.groupName.setText(groups.get(groupPosition).getName());
		
		StringBuffer buffer = new StringBuffer();
		buffer.append("[在线 ");
		buffer.append(groups.get(groupPosition).getCount());
		buffer.append("]");
		groupHolder.onlineNumber.setText(buffer.toString());
		
		groupHolder.groupName.setTag(groups.get(groupPosition).getName());
		
		return convertView;
	}

	class GroupHolder {
		TextView groupName;
		TextView onlineNumber;
	}

	class ChildHolder {
		TextView mood;
		TextView userName;
		ImageView image;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}