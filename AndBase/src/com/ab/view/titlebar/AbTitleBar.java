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
package com.ab.view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.ab.util.AbStrUtil;
import com.ab.util.AbViewUtil;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbBottomBar.java 
 * 描述：标题栏实现.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-04-24 下午3:46:47
 */
public class AbTitleBar extends LinearLayout {
	
	/** The m context. */
	private Activity mActivity;
	
	/** 标题布局. */
	protected LinearLayout titleTextLayout = null;
	
	/** 显示标题文字的View. */
	protected Button titleTextBtn = null;
	
	/** 显示标题文字的小View. */
	protected Button titleSmallTextBtn = null;
	
	/** 左侧的Logo图标View. */
	protected ImageView logoView = null;
	
	/** 左侧的Logo图标View. */
	protected ImageView logoView2 = null;
	
	/** 左侧的Logo图标右边的分割线View. */
	protected ImageView logoLineView = null;
	
	/** 标题文本的对齐参数. */
	private LinearLayout.LayoutParams titleTextLayoutParams = null;
	
	/** 右边布局的的对齐参数. */
	private LinearLayout.LayoutParams rightViewLayoutParams = null;
	
	/** 右边的View，可以自定义显示什么. */
	protected LinearLayout rightLayout = null;
	
	/** 标题栏布局ID. */
	public int mAbTitleBarID = 1;
	
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	
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
	
	/** 下拉选择. */
	private PopupWindow popupWindow;

	/**
	 * Instantiates a new ab title bar.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbTitleBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		ininTitleBar(context);
	}

	/**
	 * Instantiates a new ab title bar.
	 *
	 * @param context the context
	 */
	public AbTitleBar(Context context) {
		super(context);
		ininTitleBar(context);
		
	}
	
	/**
	 * Inin title bar.
	 *
	 * @param context the context
	 */
	public void ininTitleBar(Context context){
		
		mActivity  = (Activity)context;
		//水平排列
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setId(mAbTitleBarID);
		
		mInflater = LayoutInflater.from(context);
		
		layoutParamsFF = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParamsFW = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWF = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		layoutParamsWW = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		layoutParamsWW.gravity = Gravity.CENTER_VERTICAL;
		
		titleTextLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
		titleTextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		rightViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		rightViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
		
		
		titleTextLayout = new LinearLayout(context);
		titleTextLayout.setOrientation(LinearLayout.VERTICAL);
		titleTextLayout.setGravity(Gravity.CENTER_VERTICAL);
		titleTextLayout.setPadding(0, 0, 0, 0);
		
		titleTextBtn = new Button(context);
		titleTextBtn.setTextColor(Color.rgb(255, 255, 255));
		titleTextBtn.setTextSize(20);
		titleTextBtn.setPadding(5, 0, 5, 0);
		titleTextBtn.setGravity(Gravity.CENTER_VERTICAL);
		titleTextBtn.setBackgroundDrawable(null);
		titleTextBtn.setSingleLine();
		titleTextLayout.addView(titleTextBtn,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1));
		
		titleSmallTextBtn = new Button(context);
		titleSmallTextBtn.setTextColor(Color.rgb(255, 255, 255));
		titleSmallTextBtn.setTextSize(15);
		titleSmallTextBtn.setPadding(6, 0, 5, 0);
		titleSmallTextBtn.setGravity(Gravity.CENTER_VERTICAL);
		titleSmallTextBtn.setBackgroundDrawable(null);
		titleSmallTextBtn.setSingleLine();
		titleTextLayout.addView(titleSmallTextBtn,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,0));
		
		logoView = new ImageView(context);
		logoView.setVisibility(View.GONE);
		logoLineView = new ImageView(context);
		logoLineView.setVisibility(View.GONE);
		
		logoView2 = new ImageView(context);
		logoView2.setVisibility(View.GONE);
		
		this.addView(logoView,layoutParamsWW);
		this.addView(logoLineView,layoutParamsWW);
		this.addView(logoView2,layoutParamsWW);
		this.addView(titleTextLayout,titleTextLayoutParams);
		
		
		// 加右边的布局
		rightLayout = new LinearLayout(context);
		rightLayout.setOrientation(LinearLayout.HORIZONTAL);
		rightLayout.setGravity(Gravity.RIGHT);
		rightLayout.setPadding(0, 0, 0, 0);
		rightLayout.setHorizontalGravity(Gravity.RIGHT);
		rightLayout.setGravity(Gravity.CENTER_VERTICAL);
		rightLayout.setVisibility(View.GONE);
		this.addView(rightLayout,rightViewLayoutParams);
		
		logoView.setOnClickListener(new View.OnClickListener() {
				
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}
	

	/**
	 * 描述：标题栏的背景图.
	 * @param res  背景图资源ID
	 */         
	public void setTitleBarBackground(int res) {
		this.setBackgroundResource(res);
	}
	
	/**
	 * 描述：设置标题背景.
	 * @param d  背景图
	 */
	public void setTitleBarBackgroundDrawable(Drawable d) {
		this.setBackgroundDrawable(d);
	}
	
	/**
	 * 描述：标题栏的背景图.
	 * @param color  背景颜色值
	 */
	public void setTitleBarBackgroundColor(int color) {
		this.setBackgroundColor(color);
	}
	
	/**
	 * 描述：标题文字的对齐,需要在setTitleBarGravity之后设置才生效.
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTitleTextMargin(int left,int top,int right,int bottom) {
		titleTextLayoutParams.setMargins(left, top, right, bottom);
	}
	

	/**
	 * 描述：标题文字字号.
	 * @param titleTextSize  文字字号
	 */
	public void setTitleTextSize(int titleTextSize) {
		this.titleTextBtn.setTextSize(titleTextSize);
	}
	
	/**
	 * 描述：设置标题文字对齐方式
	 * 根据右边的具体情况判定方向：
	 * （1）中间靠近 Gravity.CENTER,Gravity.CENTER
	 * （2）左边居左 右边居右Gravity.LEFT,Gravity.RIGHT
	 * （3）左边居中，右边居右Gravity.CENTER,Gravity.RIGHT
	 * （4）左边居右，右边居右Gravity.RIGHT,Gravity.RIGHT
	 * 必须在addRightView(view)方法后设置
	 * @param gravity1  标题对齐方式
	 * @param gravity2  右边布局对齐方式
	 */
	public void setTitleBarGravity(int gravity1,int gravity2) {
		AbViewUtil.measureView(this.rightLayout);
		AbViewUtil.measureView(this.logoView);
		int leftWidth = this.logoView.getMeasuredWidth();
		int rightWidth = this.rightLayout.getMeasuredWidth();
		//if(D)Log.d(TAG, "测量布局的宽度："+leftWidth+","+rightWidth);
		this.titleTextLayoutParams.rightMargin = 0;
		this.titleTextLayoutParams.leftMargin = 0;
		//全部中间靠
		if((gravity1 == Gravity.CENTER_HORIZONTAL || gravity1 == Gravity.CENTER) ){
            if(leftWidth==0 && rightWidth==0){
            	this.titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
			}else{
				if(gravity2 == Gravity.RIGHT){
					if(rightWidth==0){
					}else{
						this.titleTextBtn.setPadding(rightWidth/3*2, 0, 0, 0);
					}
					this.titleTextBtn.setGravity(Gravity.CENTER);
					this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
				}if(gravity2 == Gravity.CENTER || gravity2 == Gravity.CENTER_HORIZONTAL){
					this.titleTextLayout.setGravity(Gravity.CENTER_HORIZONTAL);
					this.rightLayout.setHorizontalGravity(Gravity.LEFT);
					this.titleTextBtn.setGravity(Gravity.CENTER);
					int offset = leftWidth-rightWidth;
					if(offset>0){
						this.titleTextLayoutParams.rightMargin = offset;
					}else{
						this.titleTextLayoutParams.leftMargin = Math.abs(offset);
					}
				}
			}
		//左右
		}else if(gravity1 == Gravity.LEFT && gravity2 == Gravity.RIGHT){
			this.titleTextLayout.setGravity(Gravity.LEFT);
			this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
		//全部右靠
		}else if(gravity1 == Gravity.RIGHT && gravity2 == Gravity.RIGHT){
			this.titleTextLayout.setGravity(Gravity.RIGHT);
			this.rightLayout.setHorizontalGravity(Gravity.RIGHT);
		}else if(gravity1 == Gravity.LEFT && gravity2 == Gravity.LEFT){
			this.titleTextLayout.setGravity(Gravity.LEFT);
			this.rightLayout.setHorizontalGravity(Gravity.LEFT);
		}
		
	}
	
	/**
	 * 描述：获取标题文本的Button.
	 * @return the title Button view
	 */
	public Button getTitleTextButton() {
		return titleTextBtn;
	}
	
	/**
	 * 描述：获取小标题文本的Button.
	 * @return the title Button view
	 */
	public Button getTitleSmallTextButton() {
		return titleSmallTextBtn;
	}
	
	/**
	 * 描述：获取标题Logo的View.
	 * @return the logo view
	 */
	public ImageView getLogoView() {
		return logoView;
	}
	
	/**
	 * 描述：获取标题Logo的View.
	 * @return the logo view
	 */
	public ImageView getLogoView2() {
		return logoView2;
	}
	
	/**
	 * 描述：设置标题字体粗体.
	 *
	 * @param bold the new title text bold
	 */
	public void setTitleTextBold(boolean bold){
		TextPaint paint = titleTextBtn.getPaint();  
		if(bold){
			//粗体
			paint.setFakeBoldText(true); 
		}else{
			paint.setFakeBoldText(false); 
		}
		
	}
	
	/**
	 * 描述：设置标题背景.
	 *
	 * @param resId the new title text background resource
	 */
	public void setTitleTextBackgroundResource(int resId){
		titleTextBtn.setBackgroundResource(resId);
	}
	
	
	/**
	 * 描述：设置标题背景.
	 *
	 * @param drawable the new title text background drawable
	 */
	public void setTitleTextBackgroundDrawable(Drawable drawable){
		titleTextBtn.setBackgroundDrawable(drawable);
	}
	
	/**
     * 描述：设置标题文本.
     * @param text  文本
     */
	public void setTitleText(String text) {
		titleTextBtn.setText(text);
	}
	
	/**
     * 描述：设置标题文本.
     * @param resId  文本的资源ID
     */
	public void setTitleText(int resId) {
		titleTextBtn.setText(resId);
	}
	
	
	/**
     * 描述：设置小标题文本.
     * @param text  文本
     */
	public void setTitleSmallText(String text) {
		if(AbStrUtil.isEmpty(text)){
			LinearLayout.LayoutParams titleSmallTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 0);
			titleSmallTextBtn.setLayoutParams(titleSmallTextViewLayoutParams);
			titleSmallTextBtn.setText("");
		}else{
			LinearLayout.LayoutParams titleSmallTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			titleSmallTextBtn.setLayoutParams(titleSmallTextViewLayoutParams);
			titleSmallTextBtn.setText(text);
		}
	}
	
	/**
     * 描述：设置标题文本.
     * @param resId  文本的资源ID
     */
	public void setTitleSmallText(int resId) {
		LinearLayout.LayoutParams titleSmallTextViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		titleSmallTextBtn.setLayoutParams(titleSmallTextViewLayoutParams);
		titleSmallTextBtn.setText(resId);
	}
	
	/**
     * 描述：设置Logo的背景图.
     * @param drawable  Logo资源Drawable
     */
	public void setLogo(Drawable drawable) {
		logoView.setVisibility(View.VISIBLE);
		logoView.setBackgroundDrawable(drawable);
	}
	
	/**
     * 描述：设置Logo的背景资源.
     * @param resId  Logo资源ID
     */
	public void setLogo(int resId) {
		logoView.setVisibility(View.VISIBLE);
		logoView.setBackgroundResource(resId);
	}
	
	/**
     * 描述：设置Logo的背景图.
     * @param drawable  Logo资源Drawable
     */
	public void setLogo2(Drawable drawable) {
		logoView2.setVisibility(View.VISIBLE);
		logoView2.setBackgroundDrawable(drawable);
	}
	
	/**
     * 描述：设置Logo的背景资源.
     * @param resId  Logo资源ID
     */
	public void setLogo2(int resId) {
		logoView2.setVisibility(View.VISIBLE);
		logoView2.setBackgroundResource(resId);
	}
	
	/**
     * 描述：设置Logo分隔线的背景资源.
     * @param resId  Logo资源ID
     */
	public void setLogoLine(int resId) {
		logoLineView.setVisibility(View.VISIBLE);
		logoLineView.setBackgroundResource(resId);
	}
	
	/**
     * 描述：设置Logo分隔线的背景图.
     * @param drawable  Logo资源Drawable
     */
	public void setLogoLine(Drawable drawable) {
		logoLineView.setVisibility(View.VISIBLE);
		logoLineView.setBackgroundDrawable(drawable);
	}
	
	
	/**
     * 描述：把指定的View填加到标题栏右边.
     * @param rightView  指定的View
     */
	public void addRightView(View rightView) {
		rightLayout.setVisibility(View.VISIBLE);
		rightLayout.addView(rightView,layoutParamsFF);
	}
	
	/**
     * 描述：把指定资源ID表示的View填加到标题栏右边.
     * @param resId  指定的View的资源ID
     */
	public void addRightView(int resId) {
		rightLayout.setVisibility(View.VISIBLE);
		rightLayout.addView(mInflater.inflate(resId, null),layoutParamsFF);
	}
	
	/**
     * 描述：清除标题栏右边的View.
     */
	public void clearRightView() {
		rightLayout.removeAllViews();
	}
	
	/**
	 * 获取这个右边的布局,可用来设置位置.
	 *
	 * @return the right layout
	 */
	public LinearLayout getRightLayout() {
		return rightLayout;
	}

	/**
	 * 描述：设置Logo按钮的点击事件.
	 * @param mOnClickListener  指定的返回事件
	 */
	public void setLogoOnClickListener(View.OnClickListener mOnClickListener) {
		 logoView.setOnClickListener(mOnClickListener);
	}
	
	/**
	 * 描述：设置Logo按钮的点击事件.
	 * @param mOnClickListener  指定的返回事件
	 */
	public void setLogo2OnClickListener(View.OnClickListener mOnClickListener) {
		 logoView2.setOnClickListener(mOnClickListener);
	}
	
	/**
	 * 描述：设置标题的点击事件.
	 * @param mOnClickListener  指定的返回事件
	 */
	public void setTitleTextOnClickListener(View.OnClickListener mOnClickListener) {
		titleTextBtn.setOnClickListener(mOnClickListener);
	}
	
	/**
	 * 描述：下拉菜单的的实现方法.
	 *
	 * @param parent the parent
	 * @param view 要显示的View
	 * @param offsetMode 不填满的模式
	 */
	public void showWindow(View parent,View view,boolean offsetMode) {
		AbViewUtil.measureView(view);
		int popWidth = parent.getMeasuredWidth();
		int popMargin = (this.getMeasuredHeight()-parent.getMeasuredHeight())/2;
		if(view.getMeasuredWidth()>parent.getMeasuredWidth()){
			popWidth = view.getMeasuredWidth();
		}
		if(offsetMode){
			popupWindow = new PopupWindow(view, popWidth+10, LayoutParams.WRAP_CONTENT, true);
		}else{
			popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		}
		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		popupWindow.showAsDropDown(parent,0, popMargin+2);
	}
	
	/**
	 * 描述：隐藏Window.
	 */
	public void hideWindow() {
		if (popupWindow != null) {
			popupWindow.dismiss();
		}

	}
	
	/**
	 * 描述：设置标题下拉的View.
	 *
	 * @param view the new title text drop down
	 */
	public void setTitleTextDropDown(final View view){
		 if(view == null){
			   return;
		 }
		 setTitleTextOnClickListener(new OnClickListener() {
				
			 @Override
			 public void onClick(View v) {
				 showWindow(titleTextBtn,view,true);
			 }
		 });
	}

	/**
	 * 获取标题的全体布局.
	 *
	 * @return the title text layout
	 */
	public LinearLayout getTitleTextLayout() {
		return titleTextLayout;
	}
	
	/**
	 * 获取子布局显示宽度比例
	 * 默认为标题填充，右边靠右.
	 *
	 * @param left the new child view fill parent
	 */
	public void setChildViewFillParent(boolean left) {
		if(left){
			titleTextLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
			titleTextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
			titleTextLayout.setLayoutParams(titleTextLayoutParams);
			
			rightViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			rightViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
			rightLayout.setLayoutParams(rightViewLayoutParams);
			
		}else{
			titleTextLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			titleTextLayoutParams.gravity = Gravity.CENTER_VERTICAL;
			titleTextLayout.setLayoutParams(titleTextLayoutParams);
			
			rightViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,1);
			rightViewLayoutParams.gravity = Gravity.CENTER_VERTICAL;
			rightLayout.setLayoutParams(rightViewLayoutParams);
		}
		
	}
	
}
