package com.andbase.friend;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.andbase.R;
import com.baidu.frontia.FrontiaUser;

public class FriendAdapter extends BaseAdapter {
	
	private List<FrontiaUser.FrontiaUserDetail> mList;
	private Context mContext;
	public  int pageSize = 16;
	//图片下载器
    private AbImageDownloader mAbImageDownloader = null;
	
	public FriendAdapter(Context context, List<FrontiaUser.FrontiaUserDetail> list, int page) {
		mContext = context;
		//图片下载器
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setWidth(120);
        mAbImageDownloader.setHeight(120);
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
		
		mList = new ArrayList<FrontiaUser.FrontiaUserDetail>();
		int i = page * pageSize;
		int iEnd = i+pageSize;
		while ((i<list.size()) && (i<iEnd)) {
			mList.add(list.get(i));
			i++;
		}
	}
	public int getCount() {
		return mList.size();
	}

	public Object getItem(int position) {
		return mList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.friend_item, null);
			holder = new ViewHolder();
			holder.itemIcon = (ImageView)convertView.findViewById(R.id.itemIcon);
			holder.itemText = (TextView)convertView.findViewById(R.id.itemText);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder)convertView.getTag();
		}
		
		FrontiaUser.FrontiaUserDetail userInfo = mList.get(position);
		
		holder.itemText.setText(userInfo.getName());
		
		String imageUrl = userInfo.getHeadUrl();
		
		//图片的下载
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        mAbImageDownloader.display(holder.itemIcon,imageUrl);
		
		return convertView;
	}

	class ViewHolder {
		ImageView itemIcon;
		TextView itemText;
	}
}
