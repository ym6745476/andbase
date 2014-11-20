package com.andbase.im.service;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ab.util.AbDateUtil;
import com.andbase.global.Constant;
import com.andbase.im.XmppConnectionManager;
import com.andbase.im.dao.IMMsgDao;
import com.andbase.im.global.IMConstant;
import com.andbase.im.model.IMMessage;
import com.andbase.im.util.IMUtil;

/**
 * 
 * 聊天服务.
 */
public class IMChatService extends Service {
    
    /** 记录日志的标记. */
    private String TAG = IMChatService.class.getSimpleName();
    
    /** 记录日志的开关. */
    private boolean D = Constant.DEBUG;
    
	private Context context;
	private IMMsgDao mIMMsgDao = null;

	@Override
	public void onCreate() {
		context = this;
		Log.i("TAG", "[服务]会话服务：启动");
		init();
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
	    Log.i("TAG", "[服务]会话服务：关闭");
		super.onDestroy();
	}

	private void init() {
		//数据业务类
		mIMMsgDao = new IMMsgDao(context);
		XMPPConnection conn = XmppConnectionManager.getInstance()
				.getConnection();
		conn.addPacketListener(pListener, new MessageTypeFilter(
				Message.Type.chat));
		
		
	}

	/**监听到消息*/
	PacketListener pListener = new PacketListener() {

		@Override
		public void processPacket(Packet arg0) {
			Message message = (Message) arg0;
			if (message != null && message.getBody() != null
					&& !message.getBody().equals("null")) {
				
				Log.i("TAG", "[服务]收到会话消息："+message.getBody());
				
				// 生成消息历史记录
				IMMessage mIMMessage = new IMMessage();
				mIMMessage.setTitle("会话信息");
				mIMMessage.setContent(message.getBody());
				
				if (Message.Type.error == message.getType()) {
					mIMMessage.setSendState(IMMessage.FAILED);
				} else {
					mIMMessage.setSendState(IMMessage.RECEIVED);
				}
				mIMMessage.setType(IMMessage.CHAT_MSG);
				
				String from = IMUtil.getUserNameByJid(message.getFrom());
				//发送方
				mIMMessage.setUserName(from);
				//接收方
				mIMMessage.setToUserName(IMUtil.getUserNameByJid(message.getTo()));
				
				mIMMessage.setReadState(IMMessage.UNREAD);
				
				String time = AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS);
				mIMMessage.setTime(time);

				//保存本地
				mIMMsgDao.startWritableDatabase(false);
				long messageId = mIMMsgDao.insert(mIMMessage);
				mIMMsgDao.closeDatabase();

				if (messageId != -1) {
					//发出接收到会话的消息
					Intent intent = new Intent();
					intent.setAction(IMConstant.ACTION_NEW_MESSAGE);
					intent.setAction(IMConstant.ACTION_CHAT_MESSAGE);
					intent.putExtra("MESSAGE", mIMMessage);
					sendBroadcast(intent);

				}

			}

		}

	};
	
}
