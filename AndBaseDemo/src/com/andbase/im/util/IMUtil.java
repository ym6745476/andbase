package com.andbase.im.util;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;

import android.content.Context;

import com.andbase.global.Constant;
import com.andbase.im.service.MessageIQProvider;
import com.andbase.im.service.MessageListener;
import com.andbase.im.service.NotificationIQ;

/**
 * © 2012 amsoft.cn
 * 名称：IMUtil.java 
 * 描述：TODO
 * @author Administrator
 * @date 2015年7月25日 上午10:01:00
 * @version v1.0
 */
public class IMUtil {
      
	public static void login(Context context,String userName,String password){
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(Constant.xmppHost, Constant.xmppPort);
			connConfig.setReconnectionAllowed(true);
			connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			connConfig.setSASLAuthenticationEnabled(true);
			connConfig.setTruststorePath("/system/etc/security/cacerts.bks");
			connConfig.setTruststorePassword("changeit");
			connConfig.setTruststoreType("bks");
			XMPPConnection connection = new XMPPConnection(connConfig);
			connection.connect();
				
			// 设置消息接收器
			ProviderManager.getInstance().addIQProvider("notification", "androidpn:iq:notification", new MessageIQProvider());
			PacketFilter packetFilter = new PacketTypeFilter(NotificationIQ.class);
			connection.addPacketListener(new MessageListener(context), packetFilter);

			
			connection.login(userName, password, "AndroidPNClient");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void register(String userName,String password){
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(Constant.xmppHost, Constant.xmppPort);
			connConfig.setReconnectionAllowed(true);
			connConfig.setSecurityMode(ConnectionConfiguration.SecurityMode.enabled);
			connConfig.setSASLAuthenticationEnabled(true);
			connConfig.setTruststorePath("/system/etc/security/cacerts.bks");
			connConfig.setTruststorePassword("changeit");
			connConfig.setTruststoreType("bks");
			XMPPConnection connection = new XMPPConnection(connConfig);
			connection.connect();
			
			// 注册、传递参数过程
			Registration registration = new Registration();
			registration.setType(IQ.Type.SET);
			registration.addAttribute("username", userName);
			registration.addAttribute("password", password);
			connection.sendPacket(registration);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void quit(String userName){
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
