package com.andbase.im.model;



import android.os.Parcel;
import android.os.Parcelable;

/**
 * intent可以携带传递Parcel数据，需要实现三个方法 . 
 * 1、describeContents()返回0就可以.
 * 2、将需要的数据写入Parcel中，框架调用这个方法传递数据. 
 * 3、重写外部类反序列化该类时调用的方法.
 */
public class IMUser implements Parcelable {

	/**
	 * 将user保存在intent中时的key
	 */
	public static final String USERKEY = "im_user";
	
	public static final int AVAILABLE = 1;
	public static final int UNAVAILABLE = 0;
	
	private String jid;
	private String name;
	private boolean available;

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(jid);
		dest.writeString(name);
		dest.writeInt(available ? AVAILABLE : UNAVAILABLE);
	}

	public static final Parcelable.Creator<IMUser> CREATOR = new Parcelable.Creator<IMUser>() {

		@Override
		public IMUser createFromParcel(Parcel source) {
			IMUser u = new IMUser();
			u.jid = source.readString();
			u.name = source.readString();
			u.available = source.readInt() == AVAILABLE ? true : false;
			return u;
		}

		@Override
		public IMUser[] newArray(int size) {
			return new IMUser[size];
		}

	};

	public IMUser clone() {
		IMUser user = new IMUser();
		user.setJid(jid);
		user.setName(name);
		user.setAvailable(available);
		return user;
	}

}
