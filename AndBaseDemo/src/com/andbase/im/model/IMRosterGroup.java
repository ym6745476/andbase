package com.andbase.im.model;

import java.util.List;

import com.andbase.model.User;

/**
 * 
 * IM联系人分组
 */
public class IMRosterGroup {

	// 分组名称
	private String name;
	// 好友列表
	private List<User> users;

	public IMRosterGroup(String name, List<User> users) {
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
}
