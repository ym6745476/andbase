package com.andbase.im.service;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;
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

/**
 * 
 * 系统消息服务.
 * 
 */
public class IMSystemMsgService extends Service {
    
    /** 记录日志的标记. */
    private String TAG = IMSystemMsgService.class.getSimpleName();
    
    /** 记录日志的开关. */
    private boolean D = Constant.DEBUG;
    
	private Context context;
	
	private IMMsgDao mIMMsgDao = null;

	@Override
	public void onCreate() {
		context = this;
		Log.i("TAG", "[服务]系统消息服务：启动");
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
	    Log.i("TAG", "[服务]系统消息服务：关闭");
		XmppConnectionManager.getInstance().getConnection()
				.removePacketListener(pListener);
		super.onDestroy();
	}

	private void init() {
	    //数据业务类
        mIMMsgDao = new IMMsgDao(context);
		XMPPConnection con = XmppConnectionManager.getInstance()
				.getConnection();
		con.addPacketListener(pListener, new MessageTypeFilter(
				Message.Type.normal));
	}

	// 来消息监听
	PacketListener pListener = new PacketListener() {

		@Override
		public void processPacket(Packet packetz) {
			Message message = (Message) packetz;
            if (message != null && message.getBody() != null
                    && !message.getBody().equals("null")) {
                if (message.getType() == Type.normal) {
                    
                    Log.i("TAG", "[服务]收到系统消息："+message.getBody());
                    
                    // 生成消息历史记录
                    IMMessage mIMMessage = new IMMessage();
                    mIMMessage.setTitle("系统信息");
                    mIMMessage.setContent(message.getBody());
                    
                    if (Message.Type.error == message.getType()) {
                        mIMMessage.setSendState(IMMessage.FAILED);
                    } else {
                        mIMMessage.setSendState(IMMessage.RECEIVED);
                    }
                    mIMMessage.setType(IMMessage.SYS_MSG);
                    
                    String from = message.getFrom().split("@")[0];
                    //发送方
                    mIMMessage.setUserName(from);
                    //接收方
                    mIMMessage.setToUserName(message.getTo().split("@")[0]);
                    
                    mIMMessage.setReadState(IMMessage.UNREAD);
                    
                    String time = AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS);
                    mIMMessage.setTime(time);
    
                    //保存本地
                    mIMMsgDao.startWritableDatabase(false);
                    long messageId = mIMMsgDao.insert(mIMMessage);
                    mIMMsgDao.closeDatabase();
    
                    if (messageId != -1) {
                        //发出接收到的系统消息
                        Intent intent = new Intent();
                        intent.setAction(IMConstant.ACTION_NEW_MESSAGE);
                        intent.setAction(IMConstant.ACTION_SYS_MESSAGE);
                        intent.putExtra("MESSAGE", mIMMessage);
                        sendBroadcast(intent);
    
                    }
    
                }
			}
		}
	};
	

}
