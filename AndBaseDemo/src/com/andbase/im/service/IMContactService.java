package com.andbase.im.service;

import java.util.Collection;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.Roster.SubscriptionMode;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ab.util.AbDateUtil;
import com.andbase.global.Constant;
import com.andbase.im.dao.IMMsgDao;
import com.andbase.im.global.IMConstant;
import com.andbase.im.model.IMMessage;
import com.andbase.im.model.IMUser;
import com.andbase.im.util.IMUtil;

/**
 * 
 * 联系人服务.
 * 
 */
public class IMContactService extends Service {

    /** 记录日志的标记. */
    private String TAG = IMContactService.class.getSimpleName();
    
    /** 记录日志的开关. */
    private boolean D = Constant.DEBUG;
    
	private Roster roster = null;
	private Context context;
	private IMMsgDao mIMMsgDao = null;

	@Override
	public void onCreate() {
		context = this;
		Log.i("TAG", "[服务]联系人服务：启动");
		init();
		addSubscriptionListener();
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	private void init() {
		initRoster();
	}

	/**
	 * 添加一个监听，监听好友添加请求。
	 */
	private void addSubscriptionListener() {
		PacketFilter filter = new PacketFilter() {
			@Override
			public boolean accept(Packet packet) {
				if (packet instanceof Presence) {
					Presence presence = (Presence) packet;
					if (presence.getType().equals(Presence.Type.subscribe)) {
						return true;
					}
				}
				return false;
			}
		};
		IMUtil.getXMPPConnection()
				.addPacketListener(subscriptionPacketListener, filter);
	}

	/**
	 * 初始化花名册 服务重启时，更新花名册
	 */
	private void initRoster() {
	    roster = IMUtil.getRoster();
		roster.removeRosterListener(rosterListener);
		roster.addRosterListener(rosterListener);
		IMUtil.updateContacterList();
		//数据业务类
        mIMMsgDao = new IMMsgDao(context);
		
	}

	private PacketListener subscriptionPacketListener = new PacketListener() {

		@Override
		public void processPacket(Packet packet) {
		    
		    Log.i("TAG", "[服务]收到联系人消息："+packet.getFrom());
		    
		    //在登录中保存的
			String user = getSharedPreferences(IMConstant.IMSHARE, 0)
					.getString(IMConstant.USERNAME, null);
			if (packet.getFrom().contains(user))
				return;
			
			
			// 如果是自动接收所有请求，则回复一个添加信息
			if (Roster.getDefaultSubscriptionMode().equals(
					SubscriptionMode.accept_all)) {
				Presence subscription = new Presence(Presence.Type.subscribe);
				subscription.setTo(packet.getFrom());
				IMUtil.getXMPPConnection()
						.sendPacket(subscription);
			} else {
			    // 生成消息历史记录
                IMMessage mIMMessage = new IMMessage();
                mIMMessage.setTitle("好友请求");
                String from = IMUtil.getUserNameByJid(packet.getFrom());
                mIMMessage.setContent(from + "申请加您为好友");
                
                Log.i("TAG", "[服务]收到好友请求："+from + "申请加您为好友");
                
                mIMMessage.setSendState(IMMessage.RECEIVED);
                mIMMessage.setType(IMMessage.ADD_FRIEND_MSG);
                
                //发送方
                mIMMessage.setUserName(from);
                //接收方
                mIMMessage.setToUserName(IMUtil.getUserNameByJid(packet.getTo()));
                
                mIMMessage.setReadState(IMMessage.UNREAD);
                mIMMessage.setRequestState(IMMessage.ALL);
                String time = AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS);
                mIMMessage.setTime(time);

                //保存本地
                mIMMsgDao.startWritableDatabase(false);
                long messageId = mIMMsgDao.insert(mIMMessage);
                mIMMsgDao.closeDatabase();
                if (messageId != -1) {
                    //发出接收到会话的消息
                    Intent intent = new Intent();
                    intent.setAction(IMConstant.ACTION_ROSTER_SUBSCRIPTION);
                    intent.putExtra("MESSAGE", mIMMessage);
                    sendBroadcast(intent);

                }
			}
		}
	};


	@Override
	public void onDestroy() {
	    Log.i("TAG", "[服务]联系人服务：关闭");
		// 释放资源
	    IMUtil.getXMPPConnection()
				.removePacketListener(subscriptionPacketListener);
	    IMUtil.destroyContacterList();
		super.onDestroy();
	}

	private RosterListener rosterListener = new RosterListener() {

		@Override
		public void presenceChanged(Presence presence) {
			Intent intent = new Intent();
			intent.setAction(IMConstant.ACTION_ROSTER_PRESENCE_CHANGED);
			String subscriber = presence.getFrom().substring(0,
					presence.getFrom().indexOf("/"));
			RosterEntry entry = roster.getEntry(subscriber);
			if (IMUtil.contacters.containsKey(subscriber)) {
				// 将状态改变之前的user广播出去
				intent.putExtra(IMUser.USERKEY,
				        IMUtil.contacters.get(subscriber));
				IMUtil.contacters.remove(subscriber);
				IMUtil.contacters.put(subscriber,
				        IMUtil.transEntryToUser(entry, roster));
			}
			sendBroadcast(intent);
		}

		@Override
		public void entriesUpdated(Collection<String> addresses) {
			for (String address : addresses) {
				Intent intent = new Intent();
				intent.setAction(IMConstant.ACTION_ROSTER_UPDATED);
				// 获得状态改变的entry
				RosterEntry userEntry = roster.getEntry(address);
				IMUser user = IMUtil
						.transEntryToUser(userEntry, roster);
				if (IMUtil.contacters.get(address) != null) {
					// 这里发布的是更新前的user
					intent.putExtra(IMUser.USERKEY,
					        IMUtil.contacters.get(address));
					// 将发生改变的用户更新到userManager
					IMUtil.contacters.remove(address);
					IMUtil.contacters.put(address, user);
				}
				sendBroadcast(intent);
				// 用户更新，getEntries会更新
				// roster.getUnfiledEntries中的entry不会更新
			}
		}

		@Override
		public void entriesDeleted(Collection<String> addresses) {
			for (String address : addresses) {
				Intent intent = new Intent();
				intent.setAction(IMConstant.ACTION_ROSTER_DELETED);
				IMUser user = null;
				if (IMUtil.contacters.containsKey(address)) {
					user = IMUtil.contacters.get(address);
					IMUtil.contacters.remove(address);
				}
				intent.putExtra(IMUser.USERKEY, user);
				sendBroadcast(intent);
			}
		}

		@Override
		public void entriesAdded(Collection<String> addresses) {
			for (String address : addresses) {
				Intent intent = new Intent();
				intent.setAction(IMConstant.ACTION_ROSTER_ADDED);
				RosterEntry userEntry = roster.getEntry(address);
				IMUser user = IMUtil
						.transEntryToUser(userEntry, roster);
				IMUtil.contacters.put(address, user);
				intent.putExtra(IMUser.USERKEY, user);
				sendBroadcast(intent);
			}
		}
	};
	
	

}
