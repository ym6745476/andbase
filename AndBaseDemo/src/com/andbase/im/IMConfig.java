package com.andbase.im;

/**
 * 
 * IM登录配置.
 * 
 */
public class IMConfig {

	private String xmppHost;       // 地址
	private Integer xmppPort;      // 端口
	private String xmppServiceName;// 服务器名称
	private String userName;       // 用户名
	private String password;       // 密码
	private String sessionId;      // 会话id
	private boolean isNovisible;   // 是否隐身登录
	private boolean isOnline;      // 用户连接成功connection

	public boolean isOnline() {
		return isOnline;
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public String getXmppHost() {
		return xmppHost;
	}

	public void setXmppHost(String xmppHost) {
		this.xmppHost = xmppHost;
	}

	public Integer getXmppPort() {
		return xmppPort;
	}

	public void setXmppPort(Integer xmppPort) {
		this.xmppPort = xmppPort;
	}

	public String getXmppServiceName() {
		return xmppServiceName;
	}

	public void setXmppServiceName(String xmppServiceName) {
		this.xmppServiceName = xmppServiceName;
	}

	public String getUserMame() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public boolean isNovisible() {
		return isNovisible;
	}

	public void setNovisible(boolean isNovisible) {
		this.isNovisible = isNovisible;
	}


}
