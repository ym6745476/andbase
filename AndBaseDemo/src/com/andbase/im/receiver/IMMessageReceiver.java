package com.andbase.im.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ab.db.storage.AbSqliteStorage;
import com.andbase.R;
import com.andbase.friend.UserDao;
import com.andbase.im.activity.MessageActivity;
import com.andbase.im.model.IMMessage;
import com.andbase.main.MainActivity;

public class IMMessageReceiver extends BroadcastReceiver {

	private final static String TAG = "PushMessageReceiver";
	//通知管理器
	private NotificationManager mNotificationManager;
	//通知的id
	private static int NOTIFICATIONS_ID = R.layout.main;
	//数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public UserDao mUserDao = null;
	
	@Override
	public void onReceive(Context context, Intent intent) {
        
		IMMessage mIMMessage = (IMMessage)intent.getParcelableExtra("MESSAGE");
		if(mIMMessage!=null){
		    
		     //获取通知管理器
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //设置通知的图标与提示的内容
            Notification notification = new Notification(R.drawable.ic_launcher,"新消息",System.currentTimeMillis());
            //点击跳转
            Intent mIntent  = null;
            if(mIMMessage.getType()==IMMessage.ADD_FRIEND_MSG){
			    Log.d(TAG, "[广播接收器]收到了好友请求:" + mIMMessage.getContent());
			    mIntent  = new Intent(context, MessageActivity.class);
            }else if(mIMMessage.getType()==IMMessage.CHAT_MSG){
			    Log.d(TAG, "[广播接收器]收到了会话消息:" + mIMMessage.getContent());
			    mIntent  = new Intent(context, MainActivity.class);
	            mIntent.putExtra("USERNAME", mIMMessage.getUserName());
	            mIntent.putExtra("TYPE",mIMMessage.getType());
			}else if(mIMMessage.getType()==IMMessage.SYS_MSG){
                Log.d(TAG, "[广播接收器]收到了系统消息:" + mIMMessage.getContent());
                mIntent  = new Intent(context, MessageActivity.class);
            }
	        
	        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,mIntent, 0);
	        
	        //设置通知的标题与显示的内容及点击通知后的跳转
	        notification.setLatestEventInfo(context,mIMMessage.getUserName(), mIMMessage.getContent(), contentIntent);
	        //点击notification之后，该notification自动消失
	        notification.flags = Notification.FLAG_AUTO_CANCEL;
	        //notification被notify的时候，触发默认声音和默认震动
	        notification.defaults = Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
	        //显示通知
	        mNotificationManager.notify(NOTIFICATIONS_ID, notification);
			
		}else{
			Log.d(TAG, "收到了消息:空");
		}
		
		
	}


}
