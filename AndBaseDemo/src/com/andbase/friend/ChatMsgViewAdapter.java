package com.andbase.friend;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.bitmap.AbImageDownloader;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.global.AbConstant;
import com.andbase.R;
import com.andbase.global.MyApplication;
import com.andbase.model.User;

public class ChatMsgViewAdapter extends BaseAdapter {
	private List<ChatMsg> mChatMsgList;
	private Context mContext;
	private ChatActivity activity = null;
	private LayoutInflater mInflater;
	private MyApplication application = null;
	private UserDao mUserDao = null;
	private AbSqliteStorage mAbSqliteStorage = null;
	//图片下载器
    private AbImageDownloader mAbImageDownloader = null;

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	public ChatMsgViewAdapter() {
	}

	public ChatMsgViewAdapter(Context context, List<ChatMsg> list) {
		this.mContext = context;
		this.mChatMsgList = list;
		this.mInflater = LayoutInflater.from(context);
		activity = (ChatActivity)context;
		application = (MyApplication)activity.getApplication();
		mUserDao = activity.mUserDao;
		//初始化AbSqliteStorage
	    mAbSqliteStorage = activity.mAbSqliteStorage;
	    
	    //图片下载器
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setWidth(100);
        mAbImageDownloader.setHeight(100);
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
	}

	@Override
	public int getCount() {
		return mChatMsgList.size();
	}

	@Override
	public Object getItem(int position) {
		return mChatMsgList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		ChatMsg mChatMsg = mChatMsgList.get(position);
		if(mChatMsg.getuId().equals(application.mUser.getuId())){
			return IMsgViewType.IMVT_COM_MSG;
		}else{
			return IMsgViewType.IMVT_TO_MSG;
		}
		
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ChatMsg mChatMsg = mChatMsgList.get(position);
		final boolean isComMsg = getItemViewType(position)==IMsgViewType.IMVT_COM_MSG;
		final ViewHolder viewHolder;	
	    if (convertView == null){
	    	  if (isComMsg){
				  convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
			  }else{
				  convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
			  }

	    	  viewHolder = new ViewHolder();
			  viewHolder.sendTime = (TextView) convertView.findViewById(R.id.sendTime);
			  viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
			  viewHolder.chatContent = (TextView) convertView.findViewById(R.id.chatContent);
			  viewHolder.userHead = (ImageView) convertView.findViewById(R.id.userHead);
			  viewHolder.chatAttach = (ImageView) convertView.findViewById(R.id.chatAttach);
			  viewHolder.isComMsg = isComMsg;
			  convertView.setTag(viewHolder);
	    }else{
	          viewHolder = (ViewHolder) convertView.getTag();
	    }
	    
	    viewHolder.chatAttach.setVisibility(View.GONE);
	    viewHolder.chatAttach.setOnClickListener(null);
	    viewHolder.userHead.setFocusable(false);
        viewHolder.chatAttach.setFocusable(false);
        if(mChatMsg.getSendState()==0){
        	viewHolder.sendTime.setText(mChatMsg.getCreateTime()+" 正在发送...");
        }else if(mChatMsg.getSendState()==-1){
        	viewHolder.sendTime.setText(mChatMsg.getCreateTime()+" 发送失败");
        }else if(mChatMsg.getSendState()==1){
        	viewHolder.sendTime.setText(mChatMsg.getCreateTime()+" 已发送");
        }else if(mChatMsg.getSendState()==2){
        	viewHolder.sendTime.setText(mChatMsg.getCreateTime()+" 已接收");
        }
	    
	    viewHolder.chatContent.setText(mChatMsg.getContent());
	    
	    final User u = mChatMsg.getUser();
	    String imageUrl = null;
	    if(u!=null){
	    	 viewHolder.userName.setText(u.getName());
	    	 imageUrl = u.getPhotoUrl();
	    }else{
	    	 viewHolder.userName.setText("加载中...");
	    	 //activity.queryUserById(mChatMsg.getuId(),position);
	    }
	    
	    //图片的下载
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        mAbImageDownloader.display(viewHolder.userHead,imageUrl);
        
	    return convertView;
	}
	
	static class ViewHolder { 
        public TextView sendTime;
        public TextView userName;
        public TextView chatContent;
        public ImageView userHead;
        public ImageView chatAttach;
        public boolean isComMsg = true;
    }

	
}
