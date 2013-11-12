package com.andbase.friend;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.andbase.db.DBInsideHelper;
import com.andbase.model.User;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：UserDao.java 
 * 描述：用户信息
 * @author zhaoqp
 * @date：2013-7-31 下午4:12:36
 * @version v1.0
 */
public class UserDao extends AbDBDaoImpl<User> {
	public UserDao(Context context) {
		super(new DBInsideHelper(context),User.class);
	}
}
