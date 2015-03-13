package com.andbase.im.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Presence;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.ab.util.AbStrUtil;
import com.andbase.im.IMConfig;
import com.andbase.im.XmppConnectionManager;
import com.andbase.im.global.IMConstant;
import com.andbase.im.model.IMRosterGroup;
import com.andbase.im.model.IMUser;
import com.andbase.im.service.IMChatService;
import com.andbase.im.service.IMContactService;
import com.andbase.im.service.IMSystemMsgService;
import com.andbase.im.service.ReConnectService;

public class IMUtil {
    
    public static Context mContext  = null;

	/** 已经登录了 */
	public static final int LOGGED_CODE = 0;
	/** 登录成功 */
	public static final int SUCCESS_CODE = 1;
	/** 登录失败 */
	public static final int FAIL_CODE = -1;
	
	/**IM连接配置信息*/
	public static IMConfig mIMConfig = null;
	
	/**
     * 保存着所有的联系人信息
     */
    public static Map<String, IMUser> contacters = null;
	
	
	public static IMConfig getIMLoginConfig(){
        return mIMConfig;
    }

    public static void setIMConfig(Context context,IMConfig iMConfig){
        mIMConfig = iMConfig;
        mContext = context;
        // 初始化xmpp配置
        XmppConnectionManager.getInstance().init(mIMConfig);
    }
    
    public static boolean isLogin(){
        XMPPConnection connection = getXMPPConnection();
        if(!connection.isConnected() || !connection.isAuthenticated()){
            return false;
        }
        return true;
    }
    
	public static int loginIM(String userName,String password) {
		try {
		    
			XMPPConnection connection = getXMPPConnection();
            // 连接
            connection.connect();
            // 登录
            connection.login(userName,password);
			
			// 处理离线消息
			// OfflineMsgManager.getInstance(activitySupport).dealOfflineMsg(connection);
			connection.sendPacket(new Presence(Presence.Type.available));
			
			// 隐身登录
			if (mIMConfig.isNovisible()) {
				Presence presence = new Presence(Presence.Type.unavailable);
				Collection<RosterEntry> rosters = connection.getRoster()
						.getEntries();
				for (RosterEntry rosterEntry : rosters) {
					presence.setTo(rosterEntry.getUser());
					connection.sendPacket(presence);
				}
			}
			mIMConfig.setOnline(true);
			
			//保存当前登录的用户信息
			SharedPreferences preference = mContext.getSharedPreferences(IMConstant.IMSHARE,0);
	        // 保存在线连接信息
	        preference.edit().putString(IMConstant.USERNAME, mIMConfig.getUserMame()).commit();
	        
	        mIMConfig.setUserName(userName);
	        mIMConfig.setPassword(password);
	        
	        return SUCCESS_CODE;
		} catch (Exception e) {
			e.printStackTrace();
			if (e instanceof IllegalStateException) {
				// Already logged in to server
				return LOGGED_CODE;
			}

		}
		return FAIL_CODE;
	}

	public static void logoutIM() {
		XmppConnectionManager.getInstance().disconnect();
	}
	
	/**
	 * 和用户建立聊天
	 * @param toUserName
	 * @return
	 */
	public static Chat createChat(String toUserName){
	   return getXMPPConnection().getChatManager().createChat(getJidByName(toUserName), null);
	}

	public static void startIMService(Context context) {
		// 好友联系人服务
		Intent contactServer = new Intent(context, IMContactService.class);
		context.startService(contactServer);

		// 聊天服务
		Intent chatServer = new Intent(context, IMChatService.class);
		context.startService(chatServer);

		// 自动恢复连接服务
		Intent reConnectService = new Intent(context, ReConnectService.class);
		context.startService(reConnectService);

		// 系统消息连接服务
		Intent imSystemMsgService = new Intent(context,
				IMSystemMsgService.class);
		context.startService(imSystemMsgService);
	}

	public static void stopIMService(Context context) {

		// 好友联系人服务
		Intent contactServer = new Intent(context, IMContactService.class);
		context.stopService(contactServer);

		// 聊天服务
		Intent chatServer = new Intent(context, IMChatService.class);
		context.stopService(chatServer);

		// 自动恢复连接服务
		Intent reConnectService = new Intent(context, ReConnectService.class);
		context.stopService(reConnectService);

		// 系统消息连接服务
		Intent systemMsgService = new Intent(context,
				IMSystemMsgService.class);
		context.stopService(systemMsgService);
	}
	
	/**
     * 获取连接
     * */
    public static XMPPConnection getXMPPConnection() {
        XMPPConnection mConnection = null;
        try {
            mConnection = XmppConnectionManager.getInstance().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mConnection;
    }

	/**
	 * 获取花名册
	 * */
	public static Roster getRoster() {
		Roster mRoster = null;
		try {
			mRoster = XmppConnectionManager.getInstance().getConnection()
					.getRoster();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mRoster;
	}

	/**
	 * 获得花名册包含的分组列表
	 */
	public static List<IMRosterGroup> getRosterGroups(Roster roster) {

		List<IMRosterGroup> groups = new ArrayList<IMRosterGroup>();
		groups.add(new IMRosterGroup(IMConstant.ALL_FRIEND, getContacterList()));
		for (RosterGroup group : roster.getGroups()) {
			List<IMUser> users = new ArrayList<IMUser>();
			for (RosterEntry entry : group.getEntries()) {
				users.add(contacters.get(entry.getUser()));
			}
			groups.add(new IMRosterGroup(group.getName(), users));
		}
		groups.add(new IMRosterGroup(IMConstant.NO_GROUP_FRIEND,
				getNoGroupUserList(roster)));
		return groups;
	}
	
	/**
     * 刷新联系人列表
     */
	public static void updateContacterList() {
        if (contacters == null){
            contacters = new HashMap<String, IMUser>();
        }else{
            contacters.clear();
        }
        
        for (RosterEntry entry : getRoster().getEntries()) {
            contacters.put(entry.getUser(),
                    transEntryToUser(entry,getRoster()));
        }
    }
	
	/**
     * 刷新联系人列表
     */
    public static void destroyContacterList() {
        contacters = null;
    }
	
	/**
	 * 获得所有的联系人列表
	 */
	public static List<IMUser> getContacterList() {
		if (contacters == null){
			contacters = new HashMap<String, IMUser>();
			for (RosterEntry entry : getRoster().getEntries()) {
				contacters.put(entry.getUser(),
						transEntryToUser(entry,getRoster()));
			}
		}

		List<IMUser> userList = new ArrayList<IMUser>();

		for (String key : contacters.keySet())
			userList.add(contacters.get(key));

		return userList;
	}
	
	/**
	 * 获得所有未分组的联系人列表
	 */
	public static List<IMUser> getNoGroupUserList(Roster roster) {
		List<IMUser> userList = new ArrayList<IMUser>();

		// 服务器的用户信息改变后，不会通知到unfiledEntries
		for (RosterEntry entry : roster.getUnfiledEntries()) {
			userList.add(contacters.get(entry.getUser()).clone());
		}

		return userList;
	}

	/**
	 * 根据RosterEntry创建一个User
	 */
	public static IMUser transEntryToUser(RosterEntry entry, Roster roster) {
		IMUser user = new IMUser();
		if (entry.getName() == null) {
			user.setName(getUserNameByJid(entry.getUser()));
		} else {
			user.setName(entry.getName());
		}
		user.setJid(entry.getUser());
		Presence presence = roster.getPresence(entry.getUser());
		user.setAvailable(presence.isAvailable());
		return user;
	}
	
	/**
	 * 获得所有组名
	 * @return
	 */
	public static List<String> getGroupNames(Roster roster) {

		List<String> groupNames = new ArrayList<String>();
		for (RosterGroup group : roster.getGroups()) {
			groupNames.add(group.getName());
		}
		return groupNames;
	}
	
	/**
	 * 给JID返回用户名
	 * @param Jid
	 * @return
	 */
	public static String getUserNameByJid(String Jid) {
		if (AbStrUtil.isEmpty(Jid)) {
			return null;
		}
		if (!Jid.contains("@")) {
			return Jid;
		}
		return Jid.split("@")[0];
	}

	/**
	 * 给用户名返回JID
	 * @param xmppServiceName 域名:如amsoft.cn
	 * @param userName
	 * @return
	 */
	public static String getJidByName(String userName) {
		if (AbStrUtil.isEmpty(userName)) {
			return null;
		}
		return userName + "@" + mIMConfig.getXmppServiceName();
	}
	
	/**
     * 回复一个presence信息给用户(接受好友请求，拒绝好友)
     * @param type
     * @param to
     */
	public static void sendSubscribe(Presence.Type type, String to) {
        Presence presence = new Presence(type);
        presence.setTo(to);
        getXMPPConnection().sendPacket(presence);
    }
}
