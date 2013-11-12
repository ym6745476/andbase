/*
 * Copyright (C) 2013 www.418log.org
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

// TODO: Auto-generated Javadoc
/**
 * The Class AbSDDBHelper.
 */
public class AbSDDBHelper extends AbSDSQLiteOpenHelper {
	
	/** The model classes. */
	private Class<?>[] modelClasses;

	/**
	 * 初始化一个AbSDDBHelper.
	 *
	 * @param context 应用context
	 * @param path 要放到SDCard下的文件夹路径
	 * @param databaseName 数据库文件名
	 * @param factory 数据库查询的游标工厂
	 * @param databaseVersion 数据库的新版本号
	 * @param modelClasses 要初始化的表的对象
	 */
	public AbSDDBHelper(Context context, String path,String databaseName,
			SQLiteDatabase.CursorFactory factory, int databaseVersion,
			Class<?>[] modelClasses) {
        super(context, path,databaseName, null, databaseVersion);
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
