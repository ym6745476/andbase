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
package com.ab.util;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.View;

import com.ab.fragment.AbAlertDialogFragment;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.fragment.AbProgressDialogFragment;
import com.ab.fragment.AbRefreshDialogFragment;
import com.ab.fragment.AbSampleDialogFragment;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbDialogUtil.java 
 * 描述：Dialog工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-02 下午11:52:13
 */

public class AbDialogUtil {
	
	/** dialog tag*/
	private static String mDialogTag = "dialog";
	
	/**
	 * 全屏显示一个对话框不影响下面的View的点击
	 * @param view
	 * @return
	 */
	public static AbSampleDialogFragment showTipsDialog(View view) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light);
        newFragment.setContentView(view);
        
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
        // 作为全屏显示,使用“content”作为fragment容器的基本视图,这始终是Activity的基本视图  
        ft.add(android.R.id.content, newFragment, mDialogTag).addToBackStack(null).commit(); 
        
        return newFragment;
    }
	
	/**
	 * 全屏显示一个对话框
	 * @param view
	 * @return
	 */
	public static AbSampleDialogFragment showFullScreenDialog(View view) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NORMAL,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画 
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }
	
	/**
	 * 显示一个自定义的对话框(有背景层)
	 * @param view
	 */
	public static AbSampleDialogFragment showDialog(View view) {
        return showDialog(view,Gravity.CENTER);
    }
	
	/**
	 * 
	 * 描述：显示一个自定义的对话框(有背景层).
	 * @param view
	 * @param gravity 位置
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,int gravity) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog,gravity);
        newFragment.setContentView(view);
        
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
        newFragment.show(ft, mDialogTag);
        
        return newFragment;
    }
	
	/**
	 * 显示一个自定义的对话框(有背景层)
	 * @param view
	 * @param animEnter
	 * @param animExit
	 * @param animPopEnter
	 * @param animPopExit
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,int animEnter, int animExit, int animPopEnter, int animPopExit) {
        return showDialog(view,animEnter,animExit,animPopEnter,animPopExit,Gravity.CENTER);
    }
	
	/**
	 * 
	 * 描述：显示一个自定义的对话框(有背景层).
	 * @param view
	 * @param animEnter
	 * @param animExit
	 * @param animPopEnter
	 * @param animPopExit
	 * @param gravity 位置
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,int animEnter, int animExit, int animPopEnter, int animPopExit,int gravity) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog,gravity);
        newFragment.setContentView(view);
        //自定义转场动画
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setCustomAnimations(animEnter,animExit,animPopEnter,animPopExit);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }
	
	/**
	 * 显示一个自定义的对话框(有背景层)
	 * @param view
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,DialogInterface.OnCancelListener onCancelListener) {
        return showDialog(view,Gravity.CENTER,onCancelListener);
    }
	
	/**
	 * 
	 * 描述：显示一个自定义的对话框(有背景层).
	 * @param view
	 * @param gravity 位置
	 * @param onCancelListener　取消事件
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,int gravity,DialogInterface.OnCancelListener onCancelListener) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog,gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画  
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
        newFragment.setOnCancelListener(onCancelListener);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }
	
	/**
	 * 
	 * 描述：显示一个自定义的对话框(有背景层).
	 * @param view
	 * @param animEnter
	 * @param animExit
	 * @param animPopEnter
	 * @param animPopExit
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,int animEnter, int animExit, int animPopEnter, int animPopExit,DialogInterface.OnCancelListener onCancelListener) {
        return showDialog(view,animEnter,animExit,animPopEnter,animPopExit,Gravity.CENTER,onCancelListener);
    }
	
	/**
	 * 
	 * 描述：显示一个自定义的对话框(有背景层).
	 * @param view
	 * @param animEnter
	 * @param animExit
	 * @param animPopEnter
	 * @param animPopExit
	 * @param gravity
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showDialog(View view,int animEnter, int animExit, int animPopEnter, int animPopExit,int gravity,DialogInterface.OnCancelListener onCancelListener) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog,gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        ft.setCustomAnimations(animEnter,animExit,animPopEnter,animPopExit);
        newFragment.setOnCancelListener(onCancelListener);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }
	
	/**
	 * 显示一个自定义的对话框(无背景层)
	 * @param view
	 */
	public static AbSampleDialogFragment showPanel(View view) {
        return showPanel(view,Gravity.CENTER);
    }
	
	/**
	 * 
	 * 描述：显示一个自定义的对话框(无背景层).
	 * @param view
	 * @param gravity
	 * @return
	 */
	public static AbSampleDialogFragment showPanel(View view,int gravity) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_Panel,gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }
	
	/**
	 * 显示一个自定义的对话框(无背景层)
	 * @param view
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showPanel(View view,DialogInterface.OnCancelListener onCancelListener) {
        return showPanel(view,Gravity.CENTER,onCancelListener);
    }
	
	/**
	 * 显示一个自定义的对话框(无背景层)
	 * @param view
	 * @param onCancelListener
	 * @return
	 */
	public static AbSampleDialogFragment showPanel(View view,int gravity,DialogInterface.OnCancelListener onCancelListener) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
        // Create and show the dialog.
        AbSampleDialogFragment newFragment = AbSampleDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_Panel,gravity);
        newFragment.setContentView(view);
        FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
        newFragment.setOnCancelListener(onCancelListener);
        newFragment.show(ft, mDialogTag);
        return newFragment;
    }
	
	
	/**
	 * 描述：对话框dialog （图标，标题，String内容）.
	 * @param context
	 * @param icon
	 * @param title 对话框标题内容
	 * @param view  对话框提示内容
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,int icon,String title,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon,title,message,null,null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
	    // 指定一个系统转场动画   
	    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 显示一个一般的对话框（图标，标题，string内容，确认，取消）.
	 * @param context
	 * @param icon
	 * @param title 对话框标题内容
	 * @param message 对话框提示内容
	 * @param onClickListener 点击确认按钮的事件监听
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,int icon,String title,String message,AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon,title,message,null,onClickListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	
	/**
	 * 显示一个一般的对话框（标题，String内容，确认，取消）.
	 * @param context
	 * @param title 对话框标题内容
	 * @param message 对话框提示内容
	 * @param onClickListener 点击确认按钮的事件监听
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,String title,String message,AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity)context;
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0,title,message,null,onClickListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 显示一个一般的对话框（View内容）.
	 * @param view 对话框标题内容
	 */
	public static AbAlertDialogFragment showAlertDialog(View view) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0,null,null,view,null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 显示一个一般的对话框（String内容）.
	 * @param context
	 * @param title 对话框标题内容
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0,null,message,null,null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 描述：对话框dialog （图标，标题，View内容）.
	 * @param icon
	 * @param title 对话框标题内容
	 * @param view  对话框提示内容
	 */
	public static AbAlertDialogFragment showAlertDialog(int icon,String title,View view) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon,title,null,view,null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 显示一个一般的对话框（图标，标题，View内容，确认，取消）.
	 * @param icon
	 * @param title 对话框标题内容
	 * @param view 对话框提示内容
	 * @param onClickListener 点击确认按钮的事件监听
	 */
	public static AbAlertDialogFragment showAlertDialog(int icon,String title,View view,AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(icon,title,null,view,onClickListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 描述：对话框dialog （标题，View内容）.
	 * @param title 对话框标题内容
	 * @param view  对话框提示内容
	 */
	public static AbAlertDialogFragment showAlertDialog(String title,View view) {
		FragmentActivity activity = (FragmentActivity)view.getContext();
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0,title,null,view,null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 显示一个一般的对话框（标题，View内容，确认，取消）.
	 * @param title 对话框标题内容
	 * @param view 对话框提示内容
	 * @param onClickListener 点击确认按钮的事件监听
	 */
	public static AbAlertDialogFragment showAlertDialog(String title,View view,AbDialogOnClickListener onClickListener) {
		FragmentActivity activity = (FragmentActivity)view.getContext(); 
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0,title,null,view,onClickListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	/**
	 * 描述：对话框dialog （标题，String内容）.
	 * @param context
	 * @param title 对话框标题内容
	 * @param view  对话框提示内容
	 */
	public static AbAlertDialogFragment showAlertDialog(Context context,String title,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbAlertDialogFragment newFragment = AbAlertDialogFragment.newInstance(0,title,message,null,null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
	}
	
	
	/**
	 * 描述：显示进度框.
	 * @param context the context
	 * @param indeterminateDrawable 用默认请写0
	 * @param message the message
	 */
	public static AbProgressDialogFragment showProgressDialog(Context context,int indeterminateDrawable,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbProgressDialogFragment newFragment = AbProgressDialogFragment.newInstance(indeterminateDrawable,message);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示加载框.
	 * @param context the context
	 * @param indeterminateDrawable
	 * @param message the message
	 */
	public static AbLoadDialogFragment showLoadDialog(Context context,int indeterminateDrawable,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
	    newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示加载框.
	 * @param context the context
	 * @param indeterminateDrawable
	 * @param message the message
	 */
	public static AbLoadDialogFragment showLoadDialog(Context context,int indeterminateDrawable,String message,AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
	    newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示加载框.
	 * @param context the context
	 * @param indeterminateDrawable
	 * @param message the message
	 */
	public static AbLoadDialogFragment showLoadPanel(Context context,int indeterminateDrawable,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
	    newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示加载框.
	 * @param context the context
	 * @param indeterminateDrawable
	 * @param message the message
	 * @param abDialogOnRefreshListener
	 */
	public static AbLoadDialogFragment showLoadPanel(Context context,int indeterminateDrawable,String message,AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbLoadDialogFragment newFragment = AbLoadDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
	    newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示刷新框.
	 * @param context the context
	 * @param indeterminateDrawable
	 * @param message the message
	 * @param abDialogOnRefreshListener
	 */
	public static AbRefreshDialogFragment showRefreshDialog(Context context,int indeterminateDrawable,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
	    newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示刷新框.
	 * @param context
	 * @param indeterminateDrawable
	 * @param message
	 * @param abDialogOnRefreshListener
	 * @return
	 */
	public static AbRefreshDialogFragment showRefreshDialog(Context context,int indeterminateDrawable,String message,AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Holo_Light_Dialog);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示刷新框.
	 * @param context the context
	 * @param indeterminateDrawable
	 * @param message the message
	 */
	public static AbRefreshDialogFragment showRefreshPanel(Context context,int indeterminateDrawable,String message) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(null);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	/**
	 * 描述：显示刷新框.
	 * @param context
	 * @param indeterminateDrawable
	 * @param message
	 * @param abDialogOnRefreshListener
	 * @return
	 */
	public static AbRefreshDialogFragment showRefreshPanel(Context context,int indeterminateDrawable,String message,AbDialogOnLoadListener abDialogOnLoadListener) {
		FragmentActivity activity = (FragmentActivity)context; 
		AbRefreshDialogFragment newFragment = AbRefreshDialogFragment.newInstance(DialogFragment.STYLE_NO_TITLE,android.R.style.Theme_Light_Panel);
		newFragment.setIndeterminateDrawable(indeterminateDrawable);
		newFragment.setMessage(message);
		newFragment.setAbDialogOnLoadListener(abDialogOnLoadListener);
		FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
        // 指定一个系统转场动画   
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);  
		newFragment.show(ft, mDialogTag);
	    return newFragment;
    }
	
	
	/**
	 * 描述：移除Fragment.
	 * @param context the context
	 */
	public static void removeDialog(Context context){
		try {
			FragmentActivity activity = (FragmentActivity)context; 
			FragmentTransaction ft = activity.getFragmentManager().beginTransaction();
	        // 指定一个系统转场动画   
	        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);  
			Fragment prev = activity.getFragmentManager().findFragmentByTag(mDialogTag);
			if (prev != null) {
			    ft.remove(prev);
			}
			ft.addToBackStack(null);
		    ft.commit();
		} catch (Exception e) {
			//可能有Activity已经被销毁的异常
			e.printStackTrace();
		}
	}
	
	/**
	 * 描述：移除Fragment和View
	 * @param view
	 */
	public static void removeDialog(View view){
		removeDialog(view.getContext());
		AbViewUtil.removeSelfFromParent(view);
	}
	

}
