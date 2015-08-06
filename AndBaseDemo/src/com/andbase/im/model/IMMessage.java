package com.andbase.im.model;

import java.io.Serializable;
import java.util.Date;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Table;

@Table(name = "im_message")
public class IMMessage implements Serializable {

	private static final long serialVersionUID = 4733464888738356503L;

	/**
	 * 消息类型
	 */
	public static final int ADD_FRIEND_MSG = 1; // 好友请求
	public static final int SYS_MSG = 2; // 系统消息
	public static final int CHAT_MSG = 3; // 聊天消息
	
	/**
	 * 发送状态
	 */
	public static final int SEND_FINISH = 1; // 已发送
	public static final int SEND_FAILED = -1; // 发送失败
	public static final int SEND_NONE = 0; // 未发送
	public static final int SENDING = 2; //正在发送
	public static final int RECEIVEING = 3; //已接收
    public static final int RECEIVED = 4; //已接收
	
	/**
	 * 已读状态
	 */
	public static final int STATUS_READ = 1;  //已读
	public static final int STATUS_UNREAD = -1; //未读
	
	/**
     * 动作
     */
    public static final int ACTION_ACCEPT = 1;  //接受
    public static final int ACTION_REJECT = -1; //拒绝
    public static final int ACTION_NONE = 0; //初始
    
	//ID
	@Id
	@Column(name = "_id")
	private int _id;

	@Column(name = "message_id")
	private int messageId;

	@Column(name = "from_user_id")
	private int fromUserId;

	@Column(name = "from_user_name")
	private String fromUserName;

	@Column(name = "to_user_id")
	private int toUserId;

	@Column(name = "to_user_name")
	private String toUserName;

	// 消息类型
	@Column(name = "message_type")
	private int messageType;

	// 消息标题
	@Column(name = "title")
	private String title;

	// 消息内容
	@Column(name = "content")
	private String content;

	// 消息创建时间
	@Column(name = "create_date")
	private Date createDate;

	// 消息发送/接收时间
	@Column(name = "send_date")
	private Date sendDate;

	// 发送状态
	@Column(name = "send_state")
	private int sendState;
	
	// 消息动作     
    @Column(name = "message_action")
    private int messageAction;

	public IMMessage() {
	}
	
	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public int getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(int fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public int getToUserId() {
		return toUserId;
	}

	public void setToUserId(int toUserId) {
		this.toUserId = toUserId;
	}

	public String getToUserName() {
		return toUserName;
	}

	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

	public int getMessageAction() {
		return messageAction;
	}

	public void setMessageAction(int messageAction) {
		this.messageAction = messageAction;
	}

}
