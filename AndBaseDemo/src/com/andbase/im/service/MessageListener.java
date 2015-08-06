package com.andbase.im.service;



import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

import com.ab.db.storage.AbSqliteStorage;
import com.ab.util.AbJsonUtil;
import com.andbase.R;
import com.andbase.im.activity.MessageActivity;
import com.andbase.im.dao.IMMsgDao;
import com.andbase.im.model.IMMessage;
import com.andbase.im.model.IMMessage;
import com.andbase.main.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MessageListener implements PacketListener {
	
	private Context context;
	private final static String TAG = "MessageListener";
	//通知管理器
	private NotificationManager mNotificationManager;
	//通知的id
	private static int NOTIFICATIONS_ID = R.layout.main;
	
	private  IMMsgDao iMMsgDao;

	public MessageListener(Context mContext) {
		this.context = mContext;
	}

	@Override
	public void processPacket(Packet packet) {
		
		Log.d(TAG, "packet.toXML()=" + packet.toXML());
		if (packet instanceof NotificationIQ) {
			NotificationIQ notificationIQ = (NotificationIQ) packet;
			if (notificationIQ.getChildElementXML().contains("androidpn:iq:notification")) {
				String notificationMessage = notificationIQ.getMessage();
				Log.d(TAG, "notificationMessage--->>"+notificationMessage);
				if (notificationMessage != null) {
					
					IMMessage mIMMessage = (IMMessage)AbJsonUtil.fromJson(notificationMessage, IMMessage.class);
					
					//获取通知管理器
		            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		            //设置通知的图标与提示的内容
		            Notification notification = new Notification(R.drawable.ic_launcher,"新消息",System.currentTimeMillis());
		            //点击跳转
		            Intent mIntent  = new Intent(context, MessageActivity.class);
		            if(mIMMessage.getMessageType() == IMMessage.ADD_FRIEND_MSG){
					    Log.d(TAG, "[消息接收器]收到了好友请求:" + mIMMessage.getContent());
					    mIntent  = new Intent(context, MessageActivity.class);
		            }else if(mIMMessage.getMessageType() == IMMessage.CHAT_MSG){
					    Log.d(TAG, "[消息接收器]收到了会话消息:" + mIMMessage.getContent());
					    mIntent  = new Intent(context, MainActivity.class);
			            mIntent.putExtra("USERNAME", mIMMessage.getToUserName());
			            mIntent.putExtra("TYPE",mIMMessage.getMessageType());
					}else if(mIMMessage.getMessageType() == IMMessage.SYS_MSG){
		                Log.d(TAG, "[消息接收器]收到了系统消息:" + mIMMessage.getContent());
		                mIntent  = new Intent(context, MessageActivity.class);
		            }
		            
		            PendingIntent contentIntent = PendingIntent.getActivity(context, 0,mIntent, 0);
			        
			        //设置通知的标题与显示的内容及点击通知后的跳转
			        notification.setLatestEventInfo(context,mIMMessage.getTitle(), mIMMessage.getContent(), contentIntent);
			        //点击notification之后，该notification自动消失
			        notification.flags = Notification.FLAG_AUTO_CANCEL;
			        //notification被notify的时候，触发默认声音和默认震动
			        notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
			        //显示通知
			        mNotificationManager.notify(NOTIFICATIONS_ID, notification);
			        
			        //保存这条消息到数据库
			        iMMsgDao = new IMMsgDao(context);
			        iMMsgDao.startWritableDatabase(false);
			        iMMsgDao.insert(mIMMessage);
			        iMMsgDao.closeDatabase();
			        
				}
			}
		}

	}
	
	
}
