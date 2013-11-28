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
package com.ab.activity;

import java.lang.reflect.Field;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Application;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ab.global.AbAppData;
import com.ab.global.AbConstant;
import com.ab.util.AbStrUtil;
import com.ab.view.app.AbMonitorView;
import com.ab.view.ioc.AbIocSelect;
import com.ab.view.ioc.AbIocView;
import com.ab.view.listener.AbIocEventListener;
import com.ab.view.titlebar.AbBottomBar;
import com.ab.view.titlebar.AbTitleBar;

// TODO: Auto-generated Javadoc
/**
 * 描述：继承这个Activity使你的Activity拥有一个程序框架.
 * @author zhaoqp
 * @date：2013-1-15 下午3:27:05
 * @version v1.0
 */

public abstract class AbActivity extends FragmentActivity {
	
	/** 记录日志的标记. */
	private String TAG = AbActivity.class.getSimpleName();
	
	/** 记录日志的开关. */
	private boolean D = AbAppData.DEBUG;

	/** 加载框的文字说明. */
	private String mProgressMessage = "请稍候...";
	
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	
	/** 全局的加载框对象，已经完成初始化. */
	public ProgressDialog mProgressDialog;
	
	/** 底部弹出的Dialog. */
	private Dialog mBottomDialog;
	
	/** 居中弹出的Dialog. */
	private Dialog mCenterDialog;
	
	/** 顶部弹出的Dialog. */
	private Dialog mTopDialog;
	
	/** 底部弹出的Dialog的View. */
	private View mBottomDialogView = null;
	
	/** 居中弹出的Dialog的View. */
	private View mCenterDialogView = null;
	
	/** 顶部弹出的Dialog的View. */
	private View mTopDialogView = null;
	
	/** 弹出的Dialog的左右边距. */
	private int dialogPadding = 40;
	
	/** 全局的Application对象，已经完成初始化. */
	public Application abApplication = null;
	
	/** 全局的SharedPreferences对象，已经完成初始化. */
	public SharedPreferences  abSharedPreferences = null;
	
	/**
	 * LinearLayout.LayoutParams，已经初始化为FILL_PARENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsFF = null;
	
	/**
	 * LinearLayout.LayoutParams，已经初始化为FILL_PARENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsFW = null;
	
	/**
	 * LinearLayout.LayoutParams，已经初始化为WRAP_CONTENT, FILL_PARENT
	 */
	public LinearLayout.LayoutParams layoutParamsWF = null;
	
	/**
	 * LinearLayout.LayoutParams，已经初始化为WRAP_CONTENT, WRAP_CONTENT
	 */
	public LinearLayout.LayoutParams layoutParamsWW = null;
	
	/** 总布局. */
	public RelativeLayout ab_base = null;
	
	/** 标题栏布局. */
	protected AbTitleBar mAbTitleBar = null;
	
	/** 副标题栏布局. */
	protected AbBottomBar mAbBottomBar = null;
	
	/** 主内容布局. */
	protected RelativeLayout contentLayout = null;
	
	/** 屏幕宽度. */
	public int diaplayWidth  = 320;
	
	/** 屏幕高度. */
	public int diaplayHeight  = 480;
	
	/** 帧测试View. */
	private AbMonitorView mAbMonitorView  = null;
	
	/** 帧测试Handler. */
	private Handler mMonitorHandler = new Handler();
	
	/** 帧测试线程. */
	private Runnable mMonitorRunnable = null;
	
	/** Window 管理器. */
	private WindowManager mWindowManager = null;
	
	/** 帧测试参数. */
	private WindowManager.LayoutParams mMonitorParams = null;
	
    
	/**
	 * 主要Handler类，在线程中可用
	 * what：0.提示文本信息,1.等待框   ,2.移除等待框 
	 */
	private Handler baseHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case AbConstant.SHOW_TOAST:
					showToast(msg.getData().getString("Msg"));
					break;
				case AbConstant.SHOW_PROGRESS:
					showProgressDialog(mProgressMessage);
					break;
				case AbConstant.REMOVE_PROGRESS:
					removeProgressDialog();
					break;
				case AbConstant.REMOVE_DIALOGBOTTOM:
					removeDialog(AbConstant.DIALOGBOTTOM);
				case AbConstant.REMOVE_DIALOGCENTER:
					removeDialog(AbConstant.DIALOGCENTER);
				case AbConstant.REMOVE_DIALOGTOP:
					removeDialog(AbConstant.DIALOGTOP);
				default:
					break;
			}
		}
	};

	/**
	 * 描述：创建.
	 *
	 * @param savedInstanceState the saved instance state
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mInflater = LayoutInflater.from(this);
		
		mWindowManager = getWindowManager();
		Display display = mWindowManager.getDefaultDisplay();
		diaplayWidth = display.getWidth();
		diaplayHeight = display.getHeight();
		
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		
		//主标题栏
		mAbTitleBar = new AbTitleBar(this);
		
		//最外层布局
		ab_base = new RelativeLayout(this);
		ab_base.setBackgroundColor(Color.rgb(255, 255, 255));
		
		//内容布局
		contentLayout = new RelativeLayout(this);
		contentLayout.setPadding(0, 0, 0, 0);
		
		//副标题栏
		mAbBottomBar = new AbBottomBar(this);
		
        //填入View
		ab_base.addView(mAbTitleBar,layoutParamsFW);
		
		RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		ab_base.addView(mAbBottomBar, layoutParamsFW2);
		
		RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		layoutParamsFW1.addRule(RelativeLayout.BELOW, mAbTitleBar.getId());
		layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
		ab_base.addView(contentLayout, layoutParamsFW1);
		
		//Application初始化
		abApplication = getApplication();
		
		//SharedPreferences初始化
		abSharedPreferences = getSharedPreferences(AbConstant.SHAREPATH, Context.MODE_PRIVATE);
        
		//设置ContentView
        setContentView(ab_base,layoutParamsFF);
        
        //如果Dialog不是充满屏幕，要设置这个值
        if(diaplayWidth < 400){
        	dialogPadding = 30;
		}else if(diaplayWidth>700){
			dialogPadding = 50;
		}
	}
	
	/**
     * 描述：Toast提示文本.
     * @param text  文本
     */
	public void showToast(String text) {
		Toast.makeText(this,""+text, Toast.LENGTH_SHORT).show();
	}
	
	/**
     * 描述：Toast提示文本.
     * @param resId  文本的资源ID
     */
	public void showToast(int resId) {
		Toast.makeText(this,""+this.getResources().getText(resId), Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * 描述：用指定的View填充主界面.
	 * @param contentView  指定的View
	 */
	public void setAbContentView(View contentView) {
		contentLayout.removeAllViews();
		contentLayout.addView(contentView,layoutParamsFF);
		//ioc
		initIocView();
	}
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.
	 * @param resId  指定的View的资源ID
	 */
	public void setAbContentView(int resId) {
		setAbContentView(mInflater.inflate(resId, null));
	}

	/**
	 * 描述：在线程中提示文本信息.
	 * @param resId 要提示的字符串资源ID，消息what值为0,
	 */
	public void showToastInThread(int resId) {
		Message msg = baseHandler.obtainMessage(0);
		Bundle bundle = new Bundle();
		bundle.putString("Msg", this.getResources().getString(resId));
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
	
	/**
	 * 描述：在线程中提示文本信息.
	 * @param toast 消息what值为0
	 */
	public void showToastInThread(String toast) {
		Message msg = baseHandler.obtainMessage(0);
		Bundle bundle = new Bundle();
		bundle.putString("Msg", toast);
		msg.setData(bundle);
		baseHandler.sendMessage(msg);
	}
	
	/**
	 * 描述：显示进度框.
	 */
	public void showProgressDialog() {
		showProgressDialog(null);
    }
	
	/**
	 * 描述：显示进度框.
	 * @param message the message
	 */
	public void showProgressDialog(String message) {
		// 创建一个显示进度的Dialog
		if(!AbStrUtil.isEmpty(message)){
			mProgressMessage = message;
		}
		if (mProgressDialog == null) {
			mProgressDialog = new ProgressDialog(this);
			// 设置点击屏幕Dialog不消失    
			mProgressDialog.setCanceledOnTouchOutside(false);
		}
		mProgressDialog.setMessage(mProgressMessage);
		showDialog(AbConstant.DIALOGPROGRESS);
    }
	
	
	/**
	 * 描述：在底部显示一个Dialog,id为1,在中间显示id为2.
	 * @param id Dialog的类型
	 * @param view 指定一个新View
	 * @see   AbConstant.DIALOGBOTTOM
	 */
	public void showDialog(int id,View view) {
		
		if(id == AbConstant.DIALOGBOTTOM){
			mBottomDialogView = view;
			if(mBottomDialog == null){
				mBottomDialog = new Dialog(this);
				setDialogLayoutParams(mBottomDialog,dialogPadding,Gravity.BOTTOM);
			}
			mBottomDialog.setContentView(mBottomDialogView,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
			showDialog(id);
		}else if(id == AbConstant.DIALOGCENTER){
			mCenterDialogView = view;
			if(mCenterDialog == null){
				mCenterDialog = new Dialog(this);
				setDialogLayoutParams(mCenterDialog,dialogPadding,Gravity.CENTER);
			}
			mCenterDialog.setContentView(mCenterDialogView,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
			showDialog(id);
		}else if(id == AbConstant.DIALOGTOP){
			mTopDialogView = view;
			if(mTopDialog == null){
				mTopDialog = new Dialog(this);
				setDialogLayoutParams(mTopDialog,dialogPadding,Gravity.TOP);
			}
			mTopDialog.setContentView(mTopDialogView,new LayoutParams(diaplayWidth-dialogPadding,LayoutParams.WRAP_CONTENT));
			showDialog(id);
		}else{
			Log.i(TAG,"Dialog的ID传错了，请参考AbConstant类定义");
		}
	}
	
	/**
	 * 描述：对话框dialog （确认，取消）.
	 * @param title 对话框标题内容
	 * @param msg  对话框提示内容
	 * @param mOkOnClickListener  点击确认按钮的事件监听
	 */
	public void showDialog(String title,String msg,DialogInterface.OnClickListener mOkOnClickListener) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setMessage(msg);
		 builder.setTitle(title);
		 builder.setPositiveButton("确认",mOkOnClickListener);
		 builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.dismiss();
			   }
		 });
		 builder.create().show();
	}
	
	/**
	 * 描述：对话框dialog （确认，取消）.
	 * @param title 对话框标题内容
	 * @param view  对话框提示内容
	 * @param mOkOnClickListener  点击确认按钮的事件监听
	 */
	public AlertDialog showDialog(String title,View view,DialogInterface.OnClickListener mOkOnClickListener) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setTitle(title);
		 builder.setView(view);
		 builder.setPositiveButton("确认",mOkOnClickListener);
		 builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			   @Override
			   public void onClick(DialogInterface dialog, int which) {
				   dialog.dismiss();
			   }
		 });
		 AlertDialog mAlertDialog  = builder.create();
		 mAlertDialog.show();
		 return mAlertDialog;
	}
	
	/**
	 * 描述：对话框dialog （无按钮）.
	 * @param title 对话框标题内容
	 * @param msg  对话框提示内容
	 */
	public AlertDialog showDialog(String title,String msg) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setMessage(msg);
		 builder.setTitle(title);
		 builder.create();
		 AlertDialog mAlertDialog  = builder.create();
		 mAlertDialog.show();
		 return mAlertDialog;
	}
	
	/**
	 * 描述：对话框dialog （无按钮）.
	 * @param title 对话框标题内容
	 * @param view  对话框提示内容
	 */
	public AlertDialog showDialog(String title,View view) {
		 AlertDialog.Builder builder = new Builder(this);
		 builder.setTitle(title);
		 builder.setView(view);
		 builder.create();
		 AlertDialog mAlertDialog  = builder.create();
		 mAlertDialog.show();
		 return mAlertDialog;
	}
	
	/**
	 * 描述：设置弹出Dialog的属性.
	 *
	 * @param dialog  弹出Dialog
	 * @param dialogPadding 如果Dialog不是充满屏幕，要设置这个值
	 * @param gravity the gravity
	 */
	private void setDialogLayoutParams(Dialog dialog,int dialogPadding,int gravity){
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = dialog.getWindow();
		WindowManager.LayoutParams lp = window.getAttributes();
		//此处可以设置dialog显示的位置
		window.setGravity(gravity); 
		//设置宽度
		lp.width = diaplayWidth-dialogPadding; 
		lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG;
		//背景透明
		//lp.screenBrightness = 0.2f;
		lp.alpha = 0.8f;
		lp.dimAmount = 0f;
		window.setAttributes(lp); 
		// 添加动画
		window.setWindowAnimations(android.R.style.Animation_Dialog); 
		// 设置点击屏幕Dialog不消失    
		dialog.setCanceledOnTouchOutside(false);

	}
	
	/**
	 * 描述：对话框初始化.
	 * @param id the id
	 * @return the dialog
	 * @see android.app.Activity#onCreateDialog(int)
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		closeMonitor();
		Dialog dialog = null;
		switch (id) {
			case AbConstant.DIALOGPROGRESS:
				if (mProgressDialog == null) {
					Log.i(TAG,"Dialog方法调用错误,请调用showProgressDialog()!");
				}
				return mProgressDialog;
			case AbConstant.DIALOGBOTTOM:
				if (mBottomDialog == null) {
					Log.i(TAG,"Dialog方法调用错误,请调用showDialog(int id,View view)!");
				}
				return mBottomDialog;
			case AbConstant.DIALOGCENTER:
				if (mCenterDialog == null) {
					Log.i(TAG,"Dialog方法调用错误,请调用showDialog(int id,View view)!");
				}
				return mCenterDialog;
			case AbConstant.DIALOGTOP:
				if (mTopDialog == null) {
					Log.i(TAG,"Dialog方法调用错误,请调用showDialog(int id,View view)!");
				}
				return mTopDialog;
			default:
				break;
		}
		return dialog;
	}
	
	/**
	 * 描述：移除进度框.
	 */
	public void removeProgressDialog() {
		removeDialog(AbConstant.DIALOGPROGRESS);
    }
	
	/**
	 * 描述：移除Dialog.
	 * @param id the id
	 * @see android.app.Activity#removeDialog(int)
	 */
	public void removeDialogInThread(int id){
		baseHandler.sendEmptyMessage(id);
	}
	
	/**
	 * 获取主标题栏布局.
	 * @return the title layout
	 */
	public AbTitleBar getTitleBar() {
		return mAbTitleBar;
	}
	
	
	/**
	 * 获取副标题栏布局.
	 * @return the bottom layout
	 */
	public AbBottomBar getAbBottomBar() {
		return mAbBottomBar;
	}


	/**
	 * 描述：打开关闭帧数测试
	 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
	 * lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
	 *
	 */
	public void openMonitor(){
		if(!AbAppData.mMonitorOpened) {
			if(mAbMonitorView == null){
				mAbMonitorView  = new AbMonitorView(this);
				mMonitorParams = new WindowManager.LayoutParams();
		        // 设置window type
				mMonitorParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
		        /*
		         * 如果设置为params.type = WindowManager.LayoutParams.TYPE_PHONE;
		         * 那么优先级会降低一些, 即拉下通知栏不可见
		         */
				//设置图片格式，效果为背景透明
				mMonitorParams.format = PixelFormat.RGBA_8888; 
		        // 设置Window flag
				mMonitorParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
		                              | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		        /*
		         * 下面的flags属性的效果形同“锁定”。
		         * 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应。
		        wmParams.flags=LayoutParams.FLAG_NOT_TOUCH_MODAL
		                               | LayoutParams.FLAG_NOT_FOCUSABLE
		                               | LayoutParams.FLAG_NOT_TOUCHABLE;
		         */
		        // 设置悬浮窗的长得宽
				mMonitorParams.width = 60;
				mMonitorParams.height = 30;
			}
	        mWindowManager.addView(mAbMonitorView, mMonitorParams);
	        AbAppData.mMonitorOpened = true;
			mMonitorRunnable = new Runnable() {
	
				@Override
				public void run() {
					mAbMonitorView.postInvalidate();
					mMonitorHandler.postDelayed(this,0);
				}
			};
			mMonitorHandler.postDelayed(mMonitorRunnable,0);
			
			mAbMonitorView.setOnTouchListener(new OnTouchListener() {
	        	int lastX, lastY;
	        	int paramX, paramY;
	        	
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						lastX = (int) event.getRawX();
						lastY = (int) event.getRawY();
						paramX = mMonitorParams.x;
						paramY = mMonitorParams.y;
						break;
					case MotionEvent.ACTION_MOVE:
						int dx = (int) event.getRawX() - lastX;
						int dy = (int) event.getRawY() - lastY;
						if ((paramX + dx) > diaplayWidth/2 ) {
							mMonitorParams.x = diaplayWidth;
						}else {
							mMonitorParams.x = 0;
						}
	                    mMonitorParams.x = paramX + dx;
						mMonitorParams.y = paramY + dy;
						// 更新悬浮窗位置
						mWindowManager.updateViewLayout(mAbMonitorView, mMonitorParams);
						break;
					}
					return true;
				}
			});
			
		}
		
	}
	
	/**
	 * 描述：关闭帧数测试.
	 */
	public void closeMonitor(){
		if(AbAppData.mMonitorOpened) {
			if(mAbMonitorView!=null){
				mWindowManager.removeView(mAbMonitorView);
			}
			AbAppData.mMonitorOpened = false;
			if(mMonitorHandler!=null  && mMonitorRunnable!=null){
			    mMonitorHandler.removeCallbacks(mMonitorRunnable);
			}
		}
		
	}
	
    /**
     * 描述：设置弹出的Dialog的左右边距.
     *
     * @param dialogPadding  如果Dialog不是充满屏幕，要设置这个值
     */
	public void setDialogPadding(int dialogPadding) {
		this.dialogPadding = dialogPadding;
	}
	
	
	/**
	 * 描述：获取进度框显示的文字.
	 *
	 * @return the progress message
	 */
	public String getProgressMessage() {
		return mProgressMessage;
	}

	/**
	 * 描述：设置进度框显示的文字.
	 *
	 * @param message the new progress message
	 */
	public void setProgressMessage(String message) {
		this.mProgressMessage = message;
	}

	/**
	 * 描述：返回.
	 *
	 * @see android.support.v4.app.FragmentActivity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	/**
	 * 描述：Activity结束.
	 *
	 * @see android.app.Activity#finish()
	 */
	@Override
	public void finish() {
		closeMonitor();
		super.finish();
	}

	/**
	 * Gets the bottom dialog.
	 *
	 * @return the bottom dialog
	 */
	public Dialog getBottomDialog() {
		return mBottomDialog;
	}


	/**
	 * 获取显示在中间的Dialog.
	 *
	 * @return the center dialog
	 */
	public Dialog getCenterDialog() {
		return mCenterDialog;
	}


	/**
	 * Gets the top dialog.
	 *
	 * @return the top dialog
	 */
	public Dialog getTopDialog() {
		return mTopDialog;
	}
	
	/**
	 * 
	 * 描述：设置绝对定位的主标题栏覆盖到内容的上边
	 * @param above
	 * @throws 
	 */
	public void setTitleBarAbove(boolean above) {
		ab_base.removeAllViews();
		if(above){
			RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
			ab_base.addView(contentLayout,layoutParamsFW1);
			RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_TOP,RelativeLayout.TRUE);
			ab_base.addView(mAbTitleBar,layoutParamsFW2);
			
			RelativeLayout.LayoutParams layoutParamsFW3 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			ab_base.addView(mAbBottomBar, layoutParamsFW3);
			
		}else{
			ab_base.addView(mAbTitleBar,layoutParamsFW);
			
			RelativeLayout.LayoutParams layoutParamsFW2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
			ab_base.addView(mAbBottomBar, layoutParamsFW2);
			
			RelativeLayout.LayoutParams layoutParamsFW1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layoutParamsFW1.addRule(RelativeLayout.BELOW, mAbTitleBar.getId());
			layoutParamsFW1.addRule(RelativeLayout.ABOVE, mAbBottomBar.getId());
			ab_base.addView(contentLayout, layoutParamsFW1);
		}
	}
	
	/**
	 * 描述：设置界面显示（忽略标题栏）
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		initIocView();
	}

	/**
	 * 描述：设置界面显示（忽略标题栏）
	 * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View view,
			android.view.ViewGroup.LayoutParams params) {
		super.setContentView(view, params);
		initIocView();
	}

	/**
	 * 描述：设置界面显示（忽略标题栏）
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	public void setContentView(View view) {
		super.setContentView(view);
		initIocView();
	}
	
	/**
	 * 初始化为IOC控制的View.
	 */
	private void initIocView(){
		Field[] fields = getClass().getDeclaredFields();
		if(fields!=null && fields.length>0){
			for(Field field : fields){
				try {
					field.setAccessible(true);
					
					if(field.get(this)!= null )
						continue;
				
					AbIocView viewInject = field.getAnnotation(AbIocView.class);
					if(viewInject!=null){
						
						int viewId = viewInject.id();
					    field.set(this,findViewById(viewId));
					
						setListener(field,viewInject.click(),Method.Click);
						setListener(field,viewInject.longClick(),Method.LongClick);
						setListener(field,viewInject.itemClick(),Method.ItemClick);
						setListener(field,viewInject.itemLongClick(),Method.itemLongClick);
						
						AbIocSelect select = viewInject.select();
						if(!TextUtils.isEmpty(select.selected())){
							setViewSelectListener(field,select.selected(),select.noSelected());
						}
						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 设置view的监听器.
	 *
	 * @param field the field
	 * @param select the select
	 * @param noSelect the no select
	 * @throws Exception the exception
	 */
	private void setViewSelectListener(Field field,String select,String noSelect)throws Exception{
		Object obj = field.get(this);
		if(obj instanceof View){
			((AbsListView)obj).setOnItemSelectedListener(new AbIocEventListener(this).select(select).noSelect(noSelect));
		}
	}
	
	/**
	 * 设置view的监听器.
	 *
	 * @param field the field
	 * @param methodName the method name
	 * @param method the method
	 * @throws Exception the exception
	 */
	private void setListener(Field field,String methodName,Method method)throws Exception{
		if(methodName == null || methodName.trim().length() == 0)
			return;
		
		Object obj = field.get(this);
		
		switch (method) {
			case Click:
				if(obj instanceof View){
					((View)obj).setOnClickListener(new AbIocEventListener(this).click(methodName));
				}
				break;
			case ItemClick:
				if(obj instanceof AbsListView){
					((AbsListView)obj).setOnItemClickListener(new AbIocEventListener(this).itemClick(methodName));
				}
				break;
			case LongClick:
				if(obj instanceof View){
					((View)obj).setOnLongClickListener(new AbIocEventListener(this).longClick(methodName));
				}
				break;
			case itemLongClick:
				if(obj instanceof AbsListView){
					((AbsListView)obj).setOnItemLongClickListener(new AbIocEventListener(this).itemLongClick(methodName));
				}
				break;
			default:
				break;
		}
	}
	
	/**
	 * The Enum Method.
	 */
	public enum Method{
		
		/** The Click. */
		Click,
		/** The Long click. */
		LongClick,
		/** The Item click. */
		ItemClick,
		/** The item long click. */
		itemLongClick
	}

	
}
