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
package com.ab.view.ioc;

import java.lang.reflect.Method;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.ab.global.AbAppException;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving abIocEvent events.
 * The class that is interested in processing a abIocEvent
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addAbIocEventListener<code> method. When
 * the abIocEvent event occurs, that object's appropriate
 * method is invoked.
 *
 * @see AbIocEventEvent
 */
public class AbIocEventListener implements OnClickListener, OnLongClickListener, OnItemClickListener, OnItemSelectedListener,OnItemLongClickListener {

	/** The handler. */
	private Object handler;
	
	/** The click method. */
	private String clickMethod;
	
	/** The long click method. */
	private String longClickMethod;
	
	/** The item click method. */
	private String itemClickMethod;
	
	/** The item select method. */
	private String itemSelectMethod;
	
	/** The nothing selected method. */
	private String nothingSelectedMethod;
	
	/** The item long click mehtod. */
	private String itemLongClickMehtod;
	
	/** The Constant CLICK. */
	public static final int CLICK = 0;
	
	/** The Constant LONGCLICK. */
	public static final int LONGCLICK = 1;
	
	/** The Constant ITEMCLICK. */
	public static final int ITEMCLICK = 2;
	
	/** The Constant ITEMLONGCLICK. */
	public static final int ITEMLONGCLICK = 3;
	
	/**
	 * Instantiates a new ab ioc event listener.
	 *
	 * @param handler the handler
	 */
	public AbIocEventListener(Object handler) {
		this.handler = handler;
	}
	
	/**
	 * Click.
	 *
	 * @param method the method
	 * @return the ab ioc event listener
	 */
	public AbIocEventListener click(String method){
		this.clickMethod = method;
		return this;
	}
	
	/**
	 * Long click.
	 *
	 * @param method the method
	 * @return the ab ioc event listener
	 */
	public AbIocEventListener longClick(String method){
		this.longClickMethod = method;
		return this;
	}
	
	/**
	 * Item long click.
	 *
	 * @param method the method
	 * @return the ab ioc event listener
	 */
	public AbIocEventListener itemLongClick(String method){
		this.itemLongClickMehtod = method;
		return this;
	}
	
	/**
	 * Item click.
	 *
	 * @param method the method
	 * @return the ab ioc event listener
	 */
	public AbIocEventListener itemClick(String method){
		this.itemClickMethod = method;
		return this;
	}
	
	/**
	 * Select.
	 *
	 * @param method the method
	 * @return the ab ioc event listener
	 */
	public AbIocEventListener select(String method){
		this.itemSelectMethod = method;
		return this;
	}
	
	/**
	 * No select.
	 *
	 * @param method the method
	 * @return the ab ioc event listener
	 */
	public AbIocEventListener noSelect(String method){
		this.nothingSelectedMethod = method;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnLongClickListener#onLongClick(android.view.View)
	 */
	public boolean onLongClick(View v) {
		return invokeLongClickMethod(handler,longClickMethod,v);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemLongClickListener#onItemLongClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		return invokeItemLongClickMethod(handler,itemLongClickMehtod,arg0,arg1,arg2,arg3);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
		
		invokeItemSelectMethod(handler,itemSelectMethod,arg0,arg1,arg2,arg3);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android.widget.AdapterView)
	 */
	public void onNothingSelected(AdapterView<?> arg0) {
		invokeNoSelectMethod(handler,nothingSelectedMethod,arg0);
	}
	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		invokeItemClickMethod(handler,itemClickMethod,arg0,arg1,arg2,arg3);
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	public void onClick(View v) {
		
		invokeClickMethod(handler, clickMethod, v);
	}
	
	
	/**
	 * Invoke click method.
	 *
	 * @param handler the handler
	 * @param methodName the method name
	 * @param params the params
	 * @return the object
	 */
	private Object invokeClickMethod(Object handler, String methodName,  Object... params){
		if(handler == null) return null;
		Method method = null;
		try{   
			method = handler.getClass().getDeclaredMethod(methodName,View.class);
			if(method!=null)
				return method.invoke(handler, params);	
			else
				throw new AbAppException("no such method:"+methodName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	
	/**
	 * Invoke long click method.
	 *
	 * @param handler the handler
	 * @param methodName the method name
	 * @param params the params
	 * @return true, if successful
	 */
	private boolean invokeLongClickMethod(Object handler, String methodName,  Object... params){
		if(handler == null) return false;
		Method method = null;
		try{   
			//public boolean onLongClick(View v)
			method = handler.getClass().getDeclaredMethod(methodName,View.class);
			if(method!=null){
				Object obj = method.invoke(handler, params);
				return obj==null?false:Boolean.valueOf(obj.toString());	
			}
			else
				throw new AbAppException("no such method:"+methodName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
		
	}
	
	/**
	 * Invoke item click method.
	 *
	 * @param handler the handler
	 * @param methodName the method name
	 * @param params the params
	 * @return the object
	 */
	private Object invokeItemClickMethod(Object handler, String methodName,  Object... params){
		if(handler == null) return null;
		Method method = null;
		try{   
			///onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			method = handler.getClass().getDeclaredMethod(methodName,AdapterView.class,View.class,int.class,long.class);
			if(method!=null)
				return method.invoke(handler, params);	
			else
				throw new AbAppException("no such method:"+methodName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	/**
	 * Invoke item long click method.
	 *
	 * @param handler the handler
	 * @param methodName the method name
	 * @param params the params
	 * @return true, if successful
	 */
	private boolean invokeItemLongClickMethod(Object handler, String methodName,  Object... params){
		
		Method method = null;
		try{   
			if(handler == null){
				throw new AbAppException("invokeItemLongClickMethod: handler is null :");
			}
			///onItemLongClick(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			method = handler.getClass().getDeclaredMethod(methodName,AdapterView.class,View.class,int.class,long.class);
			if(method!=null){
				Object obj = method.invoke(handler, params);
				return Boolean.valueOf(obj==null?false:Boolean.valueOf(obj.toString()));	
			}
			else
				throw new AbAppException("no such method:"+methodName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return false;
	}
	
	
	/**
	 * Invoke item select method.
	 *
	 * @param handler the handler
	 * @param methodName the method name
	 * @param params the params
	 * @return the object
	 */
	private Object invokeItemSelectMethod(Object handler, String methodName,  Object... params){
		if(handler == null) return null;
		Method method = null;
		try{   
			///onItemSelected(AdapterView<?> arg0, View arg1, int arg2,long arg3)
			method = handler.getClass().getDeclaredMethod(methodName,AdapterView.class,View.class,int.class,long.class);
			if(method!=null)
				return method.invoke(handler, params);	
			else
				throw new AbAppException("no such method:"+methodName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Invoke no select method.
	 *
	 * @param handler the handler
	 * @param methodName the method name
	 * @param params the params
	 * @return the object
	 */
	private Object invokeNoSelectMethod(Object handler, String methodName,  Object... params){
		if(handler == null) return null;
		Method method = null;
		try{   
			//onNothingSelected(AdapterView<?> arg0)
			method = handler.getClass().getDeclaredMethod(methodName,AdapterView.class);
			if(method!=null)
				return method.invoke(handler, params);	
			else
				throw new AbAppException("no such method:"+methodName);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
}
