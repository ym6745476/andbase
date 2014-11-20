/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.db.orm;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbDBHelper.java 
 * 描述：手机data/data下面的数据库
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-7-23 上午9:47:10
 */
public class AbDBHelper extends SQLiteOpenHelper{
	
	/** The model classes. */
	private Class<?>[] modelClasses;
	
	
	/**
	 * 初始化一个AbSDDBHelper.
	 *
	 * @param context 应用context
	 * @param name 数据库名
	 * @param factory 数据库查询的游标工厂
	 * @param version 数据库的新版本号
	 * @param modelClasses 要初始化的表的对象
	 */
	public AbDBHelper(Context context, String name,
			CursorFactory factory, int version,Class<?>[] modelClasses) {
		super(context, name, factory, version);
		this.modelClasses = modelClasses;
	}
	
	
	/**
     * 描述：表的创建.
     *
     * @param db 数据库对象
     * @see com.ab.db.orm.AbSDSQLiteOpenHelper#onCreate(android.database.sqlite.SQLiteDatabase)
     */
    public void onCreate(SQLiteDatabase db) {
		AbTableHelper.createTablesByClasses(db, this.modelClasses);
	}

	/**
	 * 描述：表的重建.
	 *
	 * @param db 数据库对象
	 * @param oldVersion 旧版本号
	 * @param newVersion 新版本号
	 * @see com.ab.db.orm.AbSDSQLiteOpenHelper#onUpgrade(android.database.sqlite.SQLiteDatabase, int, int)
	 */
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		AbTableHelper.dropTablesByClasses(db, this.modelClasses);
		onCreate(db);
	}
}
