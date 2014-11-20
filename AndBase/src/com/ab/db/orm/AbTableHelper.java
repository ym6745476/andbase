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

import java.lang.reflect.Field;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ab.db.orm.annotation.Column;
import com.ab.db.orm.annotation.Id;
import com.ab.db.orm.annotation.Relations;
import com.ab.db.orm.annotation.Table;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbTableHelper.java 
 * 描述：数据库辅助类
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-5-23 上午10:10:53
 */
public class AbTableHelper {
	
	/** 日志标记. */
	private static final String TAG = "AbTableHelper";

	/**
	 * 根据映射的对象创建表.
	 *
	 * @param <T> the generic type
	 * @param db 数据库对象
	 * @param clazzs 对象映射
	 */
	public static <T> void createTablesByClasses(SQLiteDatabase db,Class<?>[] clazzs) {
		for (Class<?> clazz : clazzs){
			createTable(db, clazz);
		}
	}

	/**
	 * 根据映射的对象删除表.
	 *
	 * @param <T> the generic type
	 * @param db 数据库对象
	 * @param clazzs 对象映射
	 */
	public static <T> void dropTablesByClasses(SQLiteDatabase db,Class<?>[] clazzs) {
		for (Class<?> clazz : clazzs){
			dropTable(db, clazz);
		}
	}

	/**
	 * 创建表.
	 *
	 * @param <T> the generic type
	 * @param db 根据映射的对象创建表.
	 * @param clazz 对象映射
	 */
	public static <T> void createTable(SQLiteDatabase db, Class<T> clazz) {
		String tableName = "";
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
		}
		if(AbStrUtil.isEmpty(tableName)){
			Log.d(TAG, "想要映射的实体["+clazz.getName()+"],未注解@Table(name=\"?\"),被跳过");
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ").append(tableName).append(" (");

		List<Field> allFields = AbTableHelper.joinFieldsOnlyColumn(clazz.getDeclaredFields(), clazz.getSuperclass().getDeclaredFields());
		for (Field field : allFields) {
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}

			Column column = (Column) field.getAnnotation(Column.class);

			String columnType = "";
			if (column.type().equals(""))
				columnType = getColumnType(field.getType());
			else {
				columnType = column.type();
			}

			sb.append(column.name() + " " + columnType);

			if (column.length() != 0) {
				sb.append("(" + column.length() + ")");
			}
			//实体类定义为Integer类型后不能生成Id异常
			if ((field.isAnnotationPresent(Id.class)) 
					&& ((field.getType() == Integer.TYPE) || (field.getType() == Integer.class)))
				sb.append(" primary key autoincrement");
			else if (field.isAnnotationPresent(Id.class)) {
				sb.append(" primary key");
			}

			sb.append(", ");
		}

		sb.delete(sb.length() - 2, sb.length() - 1);
		sb.append(")");

		String sql = sb.toString();

		Log.d(TAG, "create table [" + tableName + "]: " + sql);

		db.execSQL(sql);
	}

	/**
	 * 删除表.
	 *
	 * @param <T> the generic type
	 * @param db 根据映射的对象创建表.
	 * @param clazz 对象映射
	 */
	public static <T> void dropTable(SQLiteDatabase db, Class<T> clazz) {
		String tableName = "";
		if (clazz.isAnnotationPresent(Table.class)) {
			Table table = (Table) clazz.getAnnotation(Table.class);
			tableName = table.name();
		}
		String sql = "DROP TABLE IF EXISTS " + tableName;
		Log.d(TAG, "dropTable[" + tableName + "]:" + sql);
		db.execSQL(sql);
	}

	/**
	 * 获取列类型.
	 *
	 * @param fieldType the field type
	 * @return 列类型
	 */
	private static String getColumnType(Class<?> fieldType) {
		if (String.class == fieldType) {
			return "TEXT";
		}
		if ((Integer.TYPE == fieldType) || (Integer.class == fieldType)) {
			return "INTEGER";
		}
		if ((Long.TYPE == fieldType) || (Long.class == fieldType)) {
			return "BIGINT";
		}
		if ((Float.TYPE == fieldType) || (Float.class == fieldType)) {
			return "FLOAT";
		}
		if ((Short.TYPE == fieldType) || (Short.class == fieldType)) {
			return "INT";
		}
		if ((Double.TYPE == fieldType) || (Double.class == fieldType)) {
			return "DOUBLE";
		}
		if (Blob.class == fieldType) {
			return "BLOB";
		}

		return "TEXT";
	}

	/**
	 * 合并Field数组并去重,并实现过滤掉非Column字段,和实现Id放在首字段位置功能.
	 *
	 * @param fields1 属性数组1
	 * @param fields2 属性数组2
	 * @return 属性的列表
	 */
	public static List<Field> joinFieldsOnlyColumn(Field[] fields1, Field[] fields2) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		for (Field field : fields1) {
			// 过滤掉非Column定义的字段
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			map.put(column.name(), field);
		}
		for (Field field : fields2) {
			// 过滤掉非Column定义的字段
			if (!field.isAnnotationPresent(Column.class)) {
				continue;
			}
			Column column = (Column) field.getAnnotation(Column.class);
			if (!map.containsKey(column.name())) {
				map.put(column.name(), field);
			}
		}
		List<Field> list = new ArrayList<Field>();
		for (String key : map.keySet()) {
			Field tempField = map.get(key);
			// 如果是Id则放在首位置.
			if (tempField.isAnnotationPresent(Id.class)) {
				list.add(0, tempField);
			} else {
				list.add(tempField);
			}
		}
		return list;
	}
	
	/**
	 * 合并Field数组并去重.
	 *
	 * @param fields1 属性数组1
	 * @param fields2 属性数组2
	 * @return 属性的列表
	 */
	public static List<Field> joinFields(Field[] fields1, Field[] fields2) {
		Map<String, Field> map = new LinkedHashMap<String, Field>();
		for (Field field : fields1) {
			// 过滤掉非Column和Relations定义的字段
			if (field.isAnnotationPresent(Column.class)) {
				Column column = (Column) field.getAnnotation(Column.class);
				map.put(column.name(), field);
			}else if(field.isAnnotationPresent(Relations.class)){
				Relations relations = (Relations) field.getAnnotation(Relations.class);
				map.put(relations.name(), field);
			}
			
		}
		for (Field field : fields2) {
			// 过滤掉非Column和Relations定义的字段
			if (field.isAnnotationPresent(Column.class)) {
				Column column = (Column) field.getAnnotation(Column.class);
				if (!map.containsKey(column.name())) {
				   map.put(column.name(), field);
				}
			}else if(field.isAnnotationPresent(Relations.class)){
				Relations relations = (Relations) field.getAnnotation(Relations.class);
				if (!map.containsKey(relations.name())) {
				   map.put(relations.name(), field);
				}
			}
		}
		List<Field> list = new ArrayList<Field>();
		for (String key : map.keySet()) {
			Field tempField = map.get(key);
			// 如果是Id则放在首位置.
			if (tempField.isAnnotationPresent(Id.class)) {
				list.add(0, tempField);
			} else {
				list.add(tempField);
			}
		}
		return list;
	}
}
