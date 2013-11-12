package com.andbase.push;

import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbDateUtil;
import com.andbase.R;
import com.andbase.friend.ChatMsg;
import com.andbase.friend.ChatMsgDao;
import com.andbase.friend.PushUtil;
import com.andbase.friend.UserDao;
import com.andbase.main.MainActivity;
import com.andbase.model.User;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PushMessageReceiver extends FrontiaPushMessageReceiver {

	private final static String TAG = "PushMessageReceiver";
	//通知管理器
	private NotificationManager mNotificationManager;
	//通知的id
	private static int NOTIFICATIONS_ID = R.layout.main;
	//数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public ChatMsgDao mChatMsgDao = null;
	public UserDao mUserDao = null;
	
	@Override
	public void onBind(Context context, int errorCode, String appid,
			String userId, String channelId, String requestId) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("绑定成功\n");
		sb.append("errCode:"+errorCode);
		sb.append("appid:"+appid+"\n");
		sb.append("userId:"+userId+"\n");
		sb.append("channelId:"+channelId+"\n");
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
		
		//保存起来供其他用户使用
        PushUtil.saveChannelId(appid,userId,channelId,requestId);
	}

	@Override
	public void onUnbind(Context context, int errorCode, String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("解绑成功\n");
		sb.append("errCode:"+errorCode);
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}

	@Override
	public void onSetTags(Context context, int errorCode,
			List<String> successTags, List<String> failTags,
			String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("设置tag成功\n");
		sb.append("errCode:"+errorCode);
		sb.append("success tags:");
		for(String tag:successTags){
			sb.append(tag+"\n");
		}
		sb.append("fail tags:");
		for(String tag:failTags){
			sb.append(tag+"\n");
		}
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}

	@Override
	public void onDelTags(Context context, int errorCode,
			List<String> successTags, List<String> failTags,
			String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("删除tag成功\n");
		sb.append("errCode:"+errorCode);
		sb.append("success tags:");
		for(String tag:successTags){
			sb.append(tag+"\n");
		}
		sb.append("fail tags:");
		for(String tag:failTags){
			sb.append(tag+"\n");
		}
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}

	@Override
	public void onListTags(Context context, int errorCode,
			List<String> tags, String requestId) {
		StringBuffer sb = new StringBuffer();
		sb.append("list tag成功\n");
		sb.append("errCode:"+errorCode);
		sb.append("tags:");
		for(String tag:tags){
			sb.append(tag+"\n");
		}
		sb.append("requestId"+requestId+"\n");
		Log.d(TAG,sb.toString());
	}
	
	@Override
	public void onMessage(Context context, String message,String customContentString) {
		Log.d(TAG, "收到了消息:" + message);
		if(mAbSqliteStorage == null){
			//初始化AbSqliteStorage
		    mAbSqliteStorage = AbSqliteStorage.getInstance(context);
		    
		    //初始化数据库操作实现类
		    mChatMsgDao = new ChatMsgDao(context);
		    mUserDao  = new UserDao(context);
		}
		
		if(message!=null && message.indexOf("content")!=-1){
			GsonBuilder builder = new GsonBuilder();
			Gson gson = builder.create();
			ChatMsg mChatMsg  = gson.fromJson(message, ChatMsg.class);
			mChatMsg.setSendState(2);
			mChatMsg.setCreateTime(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
			
			saveData(mChatMsg);
			saveUserData(mChatMsg.getUser());
			
			//获取通知管理器
	        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	        //设置通知的图标与提示的内容
	        Notification notification = new Notification(R.drawable.ic_launcher,"新消息",System.currentTimeMillis());
	        //点击通知后将挑转到指定Activity
	        Intent mIntent  = new Intent(context, MainActivity.class);
	        User u = mChatMsg.getUser();
	        mIntent.putExtra("ID",u.getuId());
	        mIntent.putExtra("NAME", u.getName());
	        mIntent.putExtra("HEADURL",u.getPhotoUrl());
	        mIntent.putExtra("TO","chat");
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,mIntent, 0);
	        
	        //设置通知的标题与显示的内容及点击通知后的跳转
	        notification.setLatestEventInfo(context,u.getName(), mChatMsg.getContent(), contentIntent);
	        //点击notification之后，该notification自动消失
	        notification.flags = Notification.FLAG_AUTO_CANCEL;
	        //notification被notify的时候，触发默认声音和默认震动
	        notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
	        //显示通知
	        mNotificationManager.notify(NOTIFICATIONS_ID, notification);
		}else{
			
		}
	}

	@Override
	public void onNotificationClicked(Context context, String title,
			String description, String customContentString) {
		StringBuffer sb = new StringBuffer();
		sb.append("通知被点击\n");
		sb.append("title:"+title+"\n");
		sb.append("description:"+description);
		sb.append("customContentString:"+customContentString+"\n");
		Log.d(TAG,sb.toString());
	}

	public void saveData(final ChatMsg chatMsg){
		if(mAbSqliteStorage == null){
			return;
		}
		mAbSqliteStorage.insertData(chatMsg, mChatMsgDao, new AbDataInsertListener(){

			@Override
			public void onSuccess(long id) {
				Log.d(TAG,"消息保存成功");
				chatMsg.set_id((int)id);
			}

			@Override
			public void onFailure(int errorCode, String errorMessage) {
			}
			
		});
	}
	
	public void saveUserData(final User user){
		if(mAbSqliteStorage == null){
			return;
		}
		//查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("u_id",user.getuId());
		
		//无sql存储的查询
		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao, new AbDataInfoListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
			}

			@Override
			public void onSuccess(List<?> paramList) {
				if(paramList==null || paramList.size()==0){
					mAbSqliteStorage.insertData(user, mUserDao, new AbDataInsertListener(){

						@Override
						public void onSuccess(long id) {
							Log.d(TAG,"插入一条用户信息成功");
						}

						@Override
						public void onFailure(int errorCode, String errorMessage) {
							Log.d(TAG,"插入一条用户信息失败");
						}
						
					});
				}else{
					Log.d(TAG,"用户信息已经存在");
				}
			}
			
		});
		
	}
	
	/*<!-- push service client -->
    <receiver android:name="com.andbase.push.PushMessageReceiver">
        <intent-filter>
            <!-- 接收push消息 -->
            <action android:name="com.baidu.android.pushservice.action.MESSAGE" />
            <!-- 接收bind,unbind,fetch,delete等反馈消息 -->
            <action android:name="com.baidu.android.pushservice.action.RECEIVE" />
            <action android:name="com.baidu.android.pushservice.action.notification.CLICK" />
        </intent-filter>
    </receiver>*/

}
