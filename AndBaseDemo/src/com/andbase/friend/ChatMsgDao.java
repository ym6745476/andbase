package com.andbase.friend;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.andbase.db.DBInsideHelper;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：ChatMsgDao.java 
 * 描述：ChatMsg的存储实现类
 * @author zhaoqp
 * @date：2013-7-31 下午4:12:36
 * @version v1.0
 */
public class ChatMsgDao extends AbDBDaoImpl<ChatMsg> {
	public ChatMsgDao(Context context) {
		super(new DBInsideHelper(context),ChatMsg.class);
	}
	
}
