package com.andbase.im.model;

import java.util.List;

/**
 * 
 * IM联系人分组
 */
public class IMRosterGroup {

	// 分组名称
	private String name;
	// 好友列表
	private List<IMUser> users;

	public IMRosterGroup(String name, List<IMUser> users) {
		this.name = name;
		this.users = users;
	}

	public int getCount() {
		if (users != null)
			return users.size();
		return 0;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<IMUser> getUsers() {
		return users;
	}

	public void setUsers(List<IMUser> users) {
		this.users = users;
	}
}
