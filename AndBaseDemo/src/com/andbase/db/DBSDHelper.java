package com.andbase.db;

import android.content.Context;

import com.ab.db.orm.AbSDDBHelper;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Stock;
import com.andbase.friend.ChatMsg;
import com.andbase.model.User;

public class DBSDHelper extends AbSDDBHelper {
	// 数据库名
	private static final String DBNAME = "andbasedemo.db";
	// 数据库 存放路径
    private static final String DBPATH = "AndBaseDemoDB";
    
    // 当前数据库的版本
	private static final int DBVERSION = 1;
	// 要初始化的表
	private static final Class<?>[] clazz = { User.class,LocalUser.class,Stock.class,ChatMsg.class};

	public DBSDHelper(Context context) {
		super(context,DBPATH, DBNAME, null, DBVERSION, clazz);
	}

}



