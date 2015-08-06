package com.andbase.im.dao;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.andbase.db.DBInsideHelper;
import com.andbase.im.model.IMMessage;
/**
 * 
 * IMMsgDao的存储实现类
 */
public class IMMsgDao extends AbDBDaoImpl<IMMessage> {
	public IMMsgDao(Context context) {
		super(new DBInsideHelper(context),IMMessage.class);
	}
	
}
