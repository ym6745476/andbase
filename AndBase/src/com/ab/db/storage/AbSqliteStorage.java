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
package com.ab.db.storage;

import java.util.List;

import android.content.Context;

import com.ab.db.orm.dao.AbDBDaoImpl;
import com.ab.db.storage.AbSqliteStorageListener.AbDataDeleteListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataUpdateListener;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.task.AbTaskObjectListener;
import com.ab.task.AbTaskQueue;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbSqliteStorage.java 
 * 描述：数据库对象操作类
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-10-16 下午1:33:39
 */
public class AbSqliteStorage {
	
	/** 单例. */
	private static AbSqliteStorage mSqliteStorage = null;
	
	/** 队列. */
	private static AbTaskQueue mAbTask = null;
	
	
	/**
	 * 描述：获取存储实例.
	 *
	 * @param context the context
	 * @return single instance of AbSqliteStorage
	 */
	public static AbSqliteStorage getInstance(Context context){
	    if (null == mSqliteStorage){
	    	mSqliteStorage = new AbSqliteStorage(context);
	    }
	    if(mAbTask == null){
	        //用队列避免并发访问数据库问题
	        mAbTask = new AbTaskQueue();
	    }
	    
	    return mSqliteStorage;
	}
	
	/**
	 * 初始化.
	 *
	 * @param context the context
	 */
	private AbSqliteStorage(Context context) {
		super();
	}
	
	/**
	 * 描述：插入数据.
	 *
	 * @param <T> the generic type
	 * @param entity  实体类 设置了对象关系映射
	 * @param dao     实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void insertData(final T entity,final AbDBDaoImpl<T> dao, final AbDataInsertListener paramDataInsertListener){
		 
		 if (entity != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.setListener(new AbTaskObjectListener() {
	    		
	    		@SuppressWarnings({ "hiding", "unchecked" })
				@Override
				public <T> T getObject() {
					long rowId = -1;
					try {
						//(1)获取数据库 
						dao.startWritableDatabase(false);
						//(2)执行
						rowId = dao.insert(entity);
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataInsertListener != null){
			    		    paramDataInsertListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, e.getMessage());
				        }
					}finally{
						//(3)关闭数据库
						dao.closeDatabase();
					}
				  	return (T)Long.valueOf(rowId);
				}
				
				@SuppressWarnings("hiding")
				@Override
				public <T> void update(T obj) {
				    long rowId = (Long)obj;
				    if (paramDataInsertListener != null){
				    	if(rowId > -1){
					    	paramDataInsertListener.onSuccess(rowId);
						}else{
				    		paramDataInsertListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, "");
						}
				    }
					
				}
				
			});
	    	mAbTask.execute(item);
	    	
	    }else{
	    	if (paramDataInsertListener != null){
	    		paramDataInsertListener.onFailure(AbDBStatus.BAD_PARAMS_CODE, "");
		    }
	    }
	    
	}
	
	/**
	 * 描述：插入数据.
	 *
	 * @param <T> the generic type
	 * @param entityList  实体类 设置了对象关系映射
	 * @param dao     实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void insertData(final List<T> entityList,final AbDBDaoImpl<T> dao, final AbDataInsertListListener paramDataInsertListListener){
		 if (entityList != null){
			
	    	AbTaskItem item = new AbTaskItem();
	    	item.setListener(new AbTaskObjectListener() {
	    		
	    		@SuppressWarnings({ "hiding", "unchecked" })
				@Override
				public <T> T getObject() {
	    			long[] rowIds = null;
					try {
						//(1)获取数据库 
						dao.startWritableDatabase(false);
						//(2)执行
						rowIds = dao.insertList(entityList);
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataInsertListListener != null){
							paramDataInsertListListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, e.getMessage());
				        }
					}finally{
						//(3)关闭数据库
						dao.closeDatabase();
					}
					return (T)rowIds;
			    	
				}
				
				@SuppressWarnings("hiding")
				@Override
				public <T> void update(T obj) {
					long sum = -1;
					long[] rowIdsBase = null;
				    if(obj!=null){
				    	Long[] rowIds = (Long[])obj;
				    	rowIdsBase = new long[rowIds.length];
					    for(int i=0;i<rowIds.length;i++){
					    	long rowId = rowIds[i];
					    	sum += rowId;
					    	rowIdsBase[i] = rowId;
					    }
				    }
				    
				    if (paramDataInsertListListener != null){
				    	if(sum > -1){
				    		paramDataInsertListListener.onSuccess(rowIdsBase);
						}else{
							paramDataInsertListListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, "");
						}
				    }
					
				}
				
				
			});
	    	mAbTask.execute(item);
	    	
	    }else{
	    	if (paramDataInsertListListener != null){
	    		paramDataInsertListListener.onFailure(AbDBStatus.BAD_PARAMS_CODE, "");
		    }
	    }
	    
	  }
	
	
	/**
	 * 查找数据.
	 *
	 * @param <T> 描述：查询数据
	 * @param storageQuery the storage query
	 * @param dao     实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void findData(final AbStorageQuery storageQuery,final AbDBDaoImpl<T> dao, final AbDataSelectListener paramDataSelectListener){
		     
	    	final AbTaskItem item = new AbTaskItem();
	    	item.setListener(new AbTaskListListener() {
				
				@Override
				public List<?> getList() {
					List<?> list = null;
					try {
						//(1)获取数据库 
						dao.startReadableDatabase();
						//(2)执行
						if(storageQuery.getLimit()!=-1 && storageQuery.getOffset()!=-1){
							list = dao.queryList(null, storageQuery.getWhereClause(),storageQuery.getWhereArgs(), storageQuery.getGroupBy(), storageQuery.getHaving(), storageQuery.getOrderBy()+" limit "+storageQuery.getLimit()+ " offset " +storageQuery.getOffset(), null);
						}else{
							list = dao.queryList(null, storageQuery.getWhereClause(),storageQuery.getWhereArgs(), storageQuery.getGroupBy(), storageQuery.getHaving(), storageQuery.getOrderBy(), null);
						}
						
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataSelectListener != null){
							paramDataSelectListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, e.getMessage());
				        }
					}finally{
						//(3)关闭数据库
						dao.closeDatabase();
					}
				  	return list;
				}
				
				@Override
				public void update(List<?> paramList) {
					if (paramDataSelectListener != null){
						paramDataSelectListener.onSuccess(paramList);
				    }
				}
			});
	    	mAbTask.execute(item);
	    
	  }
	
	/**
	 * 描述：修改数据.
	 *
	 * @param <T> the generic type
	 * @param entity  实体类 设置了对象关系映射
	 * @param dao     实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void updateData(final T entity,final AbDBDaoImpl<T> dao, final AbDataUpdateListener paramDataUpdateListener){
		 
		 if (entity != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.setListener(new AbTaskObjectListener() {
	    		
				@SuppressWarnings({ "hiding", "unchecked" })
				@Override
				public <T> T getObject() {
					int rows = 0;
				    try {
						//(1)获取数据库 
						dao.startWritableDatabase(false);
						//(2)执行
						rows = dao.update(entity);
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataUpdateListener != null){
							paramDataUpdateListener.onFailure(AbDBStatus.EXEC_ERROR_CODE,e.getMessage());
                        }
					}finally{
						//(3)关闭数据库
						dao.closeDatabase();
					}
				    return (T)Integer.valueOf(rows);
				}
				
                @SuppressWarnings("hiding")
				@Override
				public <T> void update(T obj) {
                	 int rows = (Integer)obj;
                	 if (paramDataUpdateListener != null){
                		 paramDataUpdateListener.onSuccess(rows);
                     }
                     
					
				}
               
			});
	    	mAbTask.execute(item);
	    	
	    }else{
	    	if (paramDataUpdateListener != null){
	    		paramDataUpdateListener.onFailure(AbDBStatus.BAD_PARAMS_CODE, "");
		    }
	    }
	    
	  }
	
	/**
	 * 描述：修改数据.
	 *
	 * @param <T> the generic type
	 * @param entityList  实体类 设置了对象关系映射
	 * @param dao     实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void updateData(final List<T> entityList,final AbDBDaoImpl<T> dao, final AbDataUpdateListener paramDataUpdateListener){
		 
		 if (entityList != null){
	    	
	    	AbTaskItem item = new AbTaskItem();
	    	item.setListener(new AbTaskObjectListener() {
				
				@SuppressWarnings({ "unchecked", "hiding" })
				@Override
				public <T> T getObject() {
					int rows = 0;
					try {
						//(1)获取数据库 
						dao.startWritableDatabase(false);
						//(2)执行
						rows = dao.updateList(entityList);
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataUpdateListener != null){
							paramDataUpdateListener.onFailure(AbDBStatus.EXEC_ERROR_CODE,e.getMessage());
                        }
					}finally{
						//(3)关闭数据库
						dao.closeDatabase();
					}
					return (T)Integer.valueOf(rows);
			    	
				}
				
				@SuppressWarnings("hiding")
				@Override
				public <T> void update(T obj) {
				    try {
						int ret = (Integer)obj;
						if (paramDataUpdateListener != null){
							if(ret >= 0){
								paramDataUpdateListener.onSuccess(ret);
							}else{
								paramDataUpdateListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, "");
							}
					    }
						
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataUpdateListener != null){
							paramDataUpdateListener.onFailure(AbDBStatus.EXEC_ERROR_CODE,e.getMessage());
                        }
					}
				}
			});
	    	mAbTask.execute(item);
	    	
	    }else{
	    	if (paramDataUpdateListener != null){
	    		paramDataUpdateListener.onFailure(AbDBStatus.BAD_PARAMS_CODE, "");
		    }
	    }
	    
	  }
	
	/**
	 * 描述：修改数据.
	 *
	 * @param <T> the generic type
	 * @param storageQuery  条件实体
	 * @param dao     实现AbDBDaoImpl的Dao
	 * @param paramDataInsertListener 返回监听器
	 */
	public <T> void deleteData(final AbStorageQuery storageQuery,final AbDBDaoImpl<T> dao, final AbDataDeleteListener paramDataDeleteListener){
		 
	    	AbTaskItem item = new AbTaskItem();
	    	item.setListener(new AbTaskObjectListener() {
	    		
	    		@SuppressWarnings({ "unchecked", "hiding" })
				@Override
				public <T> T getObject() {
	    			int rows = 0;
					try {
						//(1)获取数据库 
						dao.startWritableDatabase(false);
						//(2)执行
						rows = dao.delete(storageQuery.getWhereClause(),storageQuery.getWhereArgs());
					} catch (Exception e) {
						e.printStackTrace();
						if (paramDataDeleteListener != null){
							paramDataDeleteListener.onFailure(AbDBStatus.EXEC_ERROR_CODE,e.getMessage());
                        }
					}finally{
						//(3)关闭数据库
						dao.closeDatabase();
					}
					return (T)Integer.valueOf(rows);
				}
	    		
				@SuppressWarnings("hiding")
				@Override
				public <T> void update(T obj) {
				    int rows = (Integer)obj;
				    if (paramDataDeleteListener != null){
				    	if(rows >= 0){
							paramDataDeleteListener.onSuccess(rows);
						}else{
							paramDataDeleteListener.onFailure(AbDBStatus.EXEC_ERROR_CODE, "");
						}
				    }
					
				}
				
			});
	    	mAbTask.execute(item);
	}
	
	/**
	 * 描述：释放存储实例.
	 */
	public void release(){
		if(mAbTask!=null){
			mAbTask.cancel(true);
			mAbTask = null;
		}
	}
	
}
