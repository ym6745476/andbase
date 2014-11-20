package com.ab.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.util.AbAnimationUtil;
import com.ab.util.AbViewUtil;
/**
 * © 2012 amsoft.cn
 * 名称：AbLoadDialogFragment.java 
 * 描述：弹出加载框
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-30 下午16:00:52
 */
public class AbFragment extends Fragment {

	private int mLoadDrawable;
	private int mRefreshDrawable;
	public String mLoadMessage  = "正在查询,请稍候";
	public String mRefreshMessage = "请求出错，请重试";
	private int mTextSize = 15;
	private int mTextColor = Color.WHITE;
	private RelativeLayout rootView = null;
	private View mContentView;
	private LinearLayout mLoadView = null;
	private LinearLayout mRefreshView = null;
	private TextView mLoadTextView = null;
	private ImageView mLoadImageView = null;
	private TextView mRefreshTextView = null;
	private ImageView mRefreshImageView = null;
	private View mIndeterminateView = null;
	private int mBackgroundColor = Color.parseColor("#88838B8B");
	private AbFragmentOnLoadListener mAbFragmentOnLoadListener = null;
	
	/**
	 * 创建
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		
		rootView = new RelativeLayout(this.getActivity());
		rootView.setBackgroundColor(mBackgroundColor);
		mContentView = onCreateContentView(inflater,container,savedInstanceState);
		//设置默认资源
		setResource();
		//先显示load
		showLoadView();
		return rootView;
	} 
	
	/**
	 * 显示View的方法（需要实现）
	 * @return
	 */
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return null;
	} 
	
	/**
	 * 设置用到的资源（需要实现）
	 */
	public void setResource() {
		
	}
	
	/**
	 * 初始化加载View
	 */
	public void initLoadView() {
		
		mLoadView = new LinearLayout(this.getActivity());
		mLoadView.setGravity(Gravity.CENTER);
		mLoadView.setOrientation(LinearLayout.VERTICAL);
		mLoadView.setPadding(20, 20, 20, 20);
		mLoadView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		mLoadImageView = new ImageView(this.getActivity());
		mLoadImageView.setImageResource(mLoadDrawable);
		mLoadImageView.setScaleType(ScaleType.MATRIX);

		mLoadTextView = new TextView(this.getActivity());
		mLoadTextView.setText(mLoadMessage);
		mLoadTextView.setTextColor(mTextColor);
		mLoadTextView.setTextSize(mTextSize);
		mLoadTextView.setPadding(5, 5, 5, 5);

		mLoadView.addView(mLoadImageView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mLoadView.addView(mLoadTextView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		mLoadImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					// 执行刷新
					load(v);
			}

		});
		
	}
	
	/**
	 * 初始化刷新View
	 */
	public void initRefreshView() {

		mRefreshView = new LinearLayout(this.getActivity());
		mRefreshView.setGravity(Gravity.CENTER);
		mRefreshView.setOrientation(LinearLayout.VERTICAL);
		mRefreshView.setPadding(20, 20, 20, 20);
		mRefreshView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
		mRefreshImageView = new ImageView(this.getActivity());
		mRefreshImageView.setImageResource(mRefreshDrawable);
		mRefreshImageView.setScaleType(ScaleType.MATRIX);

		mRefreshTextView = new TextView(this.getActivity());
		mRefreshTextView.setText(mRefreshMessage);
		mRefreshTextView.setTextColor(mTextColor);
		mRefreshTextView.setTextSize(mTextSize);
		mRefreshTextView.setPadding(5, 5, 5, 5);

		mRefreshView.addView(mRefreshImageView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRefreshView.addView(mRefreshTextView, new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		mRefreshImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					// 执行刷新
					load(v);
			}

		});

	}
	
	/**
	 * 显示加载View
	 */
	public void showLoadView() {
		if(rootView.getChildCount() > 0){
			if(mLoadView == rootView.getChildAt(0)){
				return;
			}
		}
	
		rootView.removeAllViews();
		if(mLoadView == null){
			initLoadView();
		}
		AbViewUtil.removeSelfFromParent(mLoadView);
		rootView.addView(mLoadView);
		// 执行加载
	    load(mLoadImageView);
	}

	/**
	 * 显示刷新View
	 */
	public void showRefreshView() {
		if(rootView.getChildCount() > 0){
			if(mRefreshView == rootView.getChildAt(0)){
				loadStop(mRefreshImageView);
				return;
			}
		}
		
		rootView.removeAllViews();
		if(mRefreshView == null){
			initRefreshView();
		}
		AbViewUtil.removeSelfFromParent(mRefreshView);
		rootView.addView(mRefreshView);
	}
	
	/**
	 * 显示内容View
	 */
	public void showContentView() {
		if(rootView.getChildCount() > 0){
			if(mContentView == rootView.getChildAt(0)){
				return;
			}
		}
		
		rootView.removeAllViews();
		AbViewUtil.removeSelfFromParent(mContentView);
		rootView.addView(mContentView,new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}
	
	/**
	 * 加载完成调用
	 */
	public void loadFinish(){
		//停止动画
		loadStop(mIndeterminateView);
	}
	
	/**
	 * 加载结束
	 */
	public void loadStop(final View view){
		if(view == null){
			return;
		}
		//停止动画
		view.postDelayed(new Runnable(){

			@Override
			public void run() {
				view.clearAnimation();
			}
			
		}, 200);
	}
	
	/**
	 * 加载调用
	 */
	public void load(View v){
		if(mAbFragmentOnLoadListener!=null){
			mAbFragmentOnLoadListener.onLoad();
		}
		mIndeterminateView = v;
		AbAnimationUtil.playRotateAnimation(mIndeterminateView, 300, Animation.INFINITE,
				Animation.RESTART);
	}
	
    /**
     * 获取内容View
     * @return
     */
	public View getContentView() {
		return mContentView;
	}
	
	/**
	 * 获取加载View文字的尺寸
	 * @return
	 */
	public int getTextSize() {
		return mTextSize;
	}

	/**
	 * 设置加载View文字的尺寸
	 * @return
	 */
	public void setTextSize(int textSize) {
		this.mTextSize = textSize;
	}

	public int getTextColor() {
		return mTextColor;
	}

	public void setTextColor(int textColor) {
		this.mTextColor = textColor;
	}
	
	public void setLoadMessage(String message) {
		this.mLoadMessage = message;
		if(mLoadTextView!=null){
			mLoadTextView.setText(mLoadMessage);
		}
	}
	
	public void setRefreshMessage(String message) {
		this.mRefreshMessage = message;
		if(mRefreshTextView!=null){
			mRefreshTextView.setText(mRefreshMessage);
		}
	}

	public int getLoadDrawable() {
		return mLoadDrawable;
	}

	public void setLoadDrawable(int resid) {
		this.mLoadDrawable = resid;
		if(mLoadImageView != null){
			mLoadImageView.setBackgroundResource(resid);
		}
	}
	
	public int getRefreshDrawable() {
		return mRefreshDrawable;
	}

	public void setRefreshDrawable(int resid) {
		this.mRefreshDrawable = resid;
		if(mRefreshImageView != null){
			mRefreshImageView.setBackgroundResource(resid);
		}
	}

	public int getBackgroundColor() {
		return mBackgroundColor;
	}

	public void setBackgroundColor(int backgroundColor) {
		this.mBackgroundColor = backgroundColor;
	}
	
	public AbFragmentOnLoadListener getAbFragmentOnLoadListener() {
		return mAbFragmentOnLoadListener;
	}

	public void setAbFragmentOnLoadListener(
			AbFragmentOnLoadListener abFragmentOnLoadListener) {
		this.mAbFragmentOnLoadListener = abFragmentOnLoadListener;
	}
	
	/**
	 * 加载事件的接口.
	 */
	public interface AbFragmentOnLoadListener {

		/**
		 * 加载
		 */
		public void onLoad();
		
	}
	
}
