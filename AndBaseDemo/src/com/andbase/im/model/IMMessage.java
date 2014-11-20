package com.andbase.im.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
import com.andbase.model.User;

@Table(name = "im_message")
public class IMMessage implements Parcelable, Comparable<IMMessage> {
	
    
    /**
     * 将IMMessage 的ID保存在intent中时的key
     */
    public static final String MESSAGE_ID_KEY = "im_message_id";
    
    /**
     * 发送状态
     */
    public static final int SENDED = 0; //已发送
    public static final int FAILED = 1; //发送失败
    public static final int SENDING = 2; //正在发送
    public static final int UNSEND = 3; //已接收
    public static final int RECEIVED = 4; //已接收
    
	
	/**
	 * 消息类型
	 */
	public static final int ADD_FRIEND_MSG = 1; // 好友请求
	public static final int SYS_MSG = 2;  // 系统消息
	public static final int CHAT_MSG = 3; // 聊天消息
	
	/**
	 * 已读状态
	 */
	public static final int READ = 0;  //已读
	public static final int UNREAD = 1; //未读
	
	/**
     * 好友请求状态
     */
    public static final int ACCEPT = 0;  //接受
    public static final int REJECT = 1; //拒绝
    public static final int ALL = 2; //初始
	
	public static final String IMMESSAGE_KEY = "immessage.key";
    public static final String KEY_TIME = "immessage.time";
	
	//ID
	@Id
	@Column(name = "_id")
	private int _id;
	
	//发送方
	@Column(name = "user_name")
	private String userName;
	
	//接收方
	@Column(name = "to_user_name")
	private String toUserName;
	
	//标题
	@Column(name = "title")
	private String title;
	
	//内容
	@Column(name = "content")
	private String content;
	
	// 创建时间
	@Column(name = "time")
	private String time;
	
	//图片或者音频地址
	@Column(name = "media_url")
	private String mediaUrl;
	
	//发送状态
	@Column(name = "send_state")
	private int sendState;
	
	// 消息类型 (好友请求,系统消息)
	@Column(name = "type")
	private int type;
	
	//读状态，如果是请求 为处理状态（接受，拒绝）
	@Column(name = "read_state")
	private int readState;
	
	//请求处理状态（接受，拒绝）
    @Column(name = "request_state")
    private int requestState;
	
	//发送方用户信息
	@Relations(name="user",type="one2one",foreignKey = "user_name",action="query")
	private User user;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

	public String getToUserName(){
        return toUserName;
    }

    public void setToUserName(String toUserName){
        this.toUserName = toUserName;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMediaUrl() {
		return mediaUrl;
	}

	public void setMediaUrl(String mediaUrl) {
		this.mediaUrl = mediaUrl;
	}


	public int getSendState() {
		return sendState;
	}

	public void setSendState(int sendState) {
		this.sendState = sendState;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getReadState() {
		return readState;
	}

	public void setReadState(int readState) {
		this.readState = readState;
	}

	public int getRequestState(){
        return requestState;
    }

    public void setRequestState(int requestState){
        this.requestState = requestState;
    }

    public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
	    dest.writeInt(_id);
        dest.writeString(userName);
        dest.writeString(toUserName);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(time);
        dest.writeString(mediaUrl);
        dest.writeInt(sendState);
        dest.writeInt(type);
        dest.writeInt(readState);
        dest.writeInt(requestState);
	}

	public static final Parcelable.Creator<IMMessage> CREATOR = new Parcelable.Creator<IMMessage>() {

		@Override
		public IMMessage createFromParcel(Parcel source) {
			IMMessage message = new IMMessage();
			message.set_id(source.readInt());
			message.setUserName(source.readString());
			message.setToUserName(source.readString());
			message.setTitle(source.readString());
			message.setContent(source.readString());
			message.setTime(source.readString());
			message.setMediaUrl(source.readString());
			message.setSendState(source.readInt());
			message.setType(source.readInt());
			message.setReadState(source.readInt());
			message.setRequestState(source.readInt());
			return message;
		}

		@Override
		public IMMessage[] newArray(int size) {
			return new IMMessage[size];
		}

	};


	/**
	 * 按时间降序排列
	 */
	@Override
	public int compareTo(IMMessage oth) {
		return 0;
	}
}
