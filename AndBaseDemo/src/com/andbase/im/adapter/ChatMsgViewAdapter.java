package com.andbase.im.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.db.storage.AbSqliteStorage;
import com.ab.image.AbImageLoader;
import com.ab.util.AbImageUtil;
import com.andbase.R;
import com.andbase.friend.UserDao;
import com.andbase.global.MyApplication;
import com.andbase.im.activity.ChatActivity;
import com.andbase.im.model.IMMessage;
import com.andbase.model.User;

public class ChatMsgViewAdapter extends BaseAdapter {
	private List<IMMessage> mChatMsgList;
	private Context mContext;
	private ChatActivity activity = null;
	private LayoutInflater mInflater;
	private MyApplication application = null;
	private UserDao mUserDao = null;
	private AbSqliteStorage mAbSqliteStorage = null;
	//图片下载器
    private AbImageLoader mAbImageLoader = null;

	public static interface IMsgViewType {
		int IMVT_COM_MSG = 0;
		int IMVT_TO_MSG = 1;
	}

	public ChatMsgViewAdapter() {
	}

	public ChatMsgViewAdapter(Context context, List<IMMessage> list) {
		this.mContext = context;
		this.mChatMsgList = list;
		this.mInflater = LayoutInflater.from(context);
		activity = (ChatActivity)context;
		application = (MyApplication)activity.getApplication();
		mUserDao = activity.mUserDao;
		//初始化AbSqliteStorage
	    mAbSqliteStorage = activity.mAbSqliteStorage;
	    
	    //图片下载器
        mAbImageLoader = new AbImageLoader(mContext);
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
	    IMMessage mChatMsg = mChatMsgList.get(position);
		if(mChatMsg.getToUserName().equals(application.mUser.getUserName())){
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
		final IMMessage mChatMsg = mChatMsgList.get(position);
		final boolean isComMsg = getItemViewType(position)==IMsgViewType.IMVT_COM_MSG;
		final ViewHolder viewHolder;	
	    if (convertView == null){
	    	  if (isComMsg){
				  convertView = mInflater.inflate(R.layout.chatting_item_msg_text_right, null);
			  }else{
				  convertView = mInflater.inflate(R.layout.chatting_item_msg_text_left, null);
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
        if(mChatMsg.getSendState()==IMMessage.SENDING){
        	viewHolder.sendTime.setText(mChatMsg.getCreateDate()+" 正在发送...");
        }else if(mChatMsg.getSendState()==IMMessage.SEND_FAILED){
        	viewHolder.sendTime.setText(mChatMsg.getCreateDate()+" 发送失败");
        }else if(mChatMsg.getSendState()==IMMessage.SEND_FINISH){
        	viewHolder.sendTime.setText(mChatMsg.getCreateDate()+" 已发送");
        }else if(mChatMsg.getSendState()==IMMessage.RECEIVED){
        	viewHolder.sendTime.setText(mChatMsg.getCreateDate()+" 已接收");
        }
	    
	    viewHolder.chatContent.setText(mChatMsg.getContent());
	    //viewHolder.userName.setText(mChatMsg.getUserName());
	    //final User u = mChatMsg.getUser();
	    String headUrl = null;
	    //if(u!=null){
	    //	 headUrl = u.getHeadUrl();
	    //}else{
	    	 //activity.queryUserById(mChatMsg.getuId(),position);
	    //}
	    
	    //图片的下载
        mAbImageLoader.display(viewHolder.userHead,headUrl,100,100);
        
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
