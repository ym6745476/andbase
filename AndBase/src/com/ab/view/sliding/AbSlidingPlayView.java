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
package com.ab.view.sliding;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.ab.adapter.AbViewPagerAdapter;
import com.ab.util.AbFileUtil;
import com.ab.view.sample.AbInnerViewPager;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbPlayView
 * 描述：可播放显示的View.
 *
 * @author 还如一梦中
 * @version 
 * @date 2011-11-28
 */

public class AbSlidingPlayView extends LinearLayout {
	
	/** 上下文. */
	private Context context;

	/** 内部的ViewPager. */
	private AbInnerViewPager mViewPager;

	/** 导航的布局. */
	private LinearLayout navLinearLayout;
	
	/** 导航布局参数. */
	public LinearLayout.LayoutParams navLayoutParams = null;

	/** 计数. */
	private int count, position;

	/** 导航图片. */
	private Bitmap displayImage, hideImage;
	
	/** 点击. */
	private AbOnItemClickListener mOnItemClickListener;
	
	/** 改变. */
	private AbOnChangeListener mAbChangeListener;
	
	/** 滚动. */
	private AbOnScrollListener mAbScrolledListener;
	
	/** 触摸. */
	private AbOnTouchListener mAbOnTouchListener;
	
	/** List views. */
	private ArrayList<View> mListViews = null;
	
	/** 适配器. */
	private AbViewPagerAdapter mAbViewPagerAdapter = null;
	
	/** 导航的点父View. */
	private LinearLayout mNavLayoutParent = null;
	
	/** 导航内容的对齐方式. */
	private int navHorizontalGravity = Gravity.RIGHT;
	
	/** 播放的方向. */
	private int playingDirection = 0;
	
	/** 播放的开关. */
	private boolean play = false;
	
	/**
	 * 创建一个AbSlidingPlayView.
	 *
	 * @param context the context
	 */
	public AbSlidingPlayView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 从xml初始化的AbSlidingPlayView.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public AbSlidingPlayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	/**
	 * 描述：初始化这个View.
	 *
	 * @param context the context
	 */
	public void initView(Context context){
		this.context = context;
		navLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		this.setOrientation(LinearLayout.VERTICAL);
		this.setBackgroundColor(Color.rgb(255, 255, 255));
		
		RelativeLayout mRelativeLayout = new RelativeLayout(context);

		mViewPager = new AbInnerViewPager(context);
		//手动创建的ViewPager,如果用fragment必须调用setId()方法设置一个id
		mViewPager.setId(1985);
		//导航的点
		mNavLayoutParent = new LinearLayout(context);
		mNavLayoutParent.setPadding(0,5, 0, 5);
		navLinearLayout = new LinearLayout(context);
		navLinearLayout.setPadding(15, 1, 15, 1);
		navLinearLayout.setVisibility(View.INVISIBLE);
		mNavLayoutParent.addView(navLinearLayout,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		
		RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
        lp1.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        lp1.addRule(RelativeLayout.CENTER_VERTICAL,RelativeLayout.TRUE);
        mRelativeLayout.addView(mViewPager,lp1);
		
		RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		mRelativeLayout.addView(mNavLayoutParent,lp2);
		addView(mRelativeLayout,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		
		displayImage = AbFileUtil.getBitmapFromSrc("image/play_display.png");
		hideImage = AbFileUtil.getBitmapFromSrc("image/play_hide.png");
		
		mListViews = new ArrayList<View>();
		mAbViewPagerAdapter = new AbViewPagerAdapter(context,mListViews);
		mViewPager.setAdapter(mAbViewPagerAdapter);
		mViewPager.setFadingEdgeLength(0);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				makesurePosition();
				onPageSelectedCallBack(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				onPageScrolledCallBack(position);
			}

		});
		
	}

	/**
	 * 创建导航点.
	 */
	public void creatIndex() {
		//显示下面的点
		navLinearLayout.removeAllViews();
		mNavLayoutParent.setHorizontalGravity(navHorizontalGravity);
		navLinearLayout.setGravity(Gravity.CENTER);
		navLinearLayout.setVisibility(View.VISIBLE);
		count = mListViews.size();
		navLayoutParams.setMargins(5, 5, 5, 5);
		for (int j = 0; j < count; j++) {
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(navLayoutParams);
			if (j == 0) {
				imageView.setImageBitmap(displayImage);
			} else {
				imageView.setImageBitmap(hideImage);
			}
			navLinearLayout.addView(imageView, j);
		}
	}


	/**
	 * 定位点的位置.
	 */
	public void makesurePosition() {
		position = mViewPager.getCurrentItem();
		for (int j = 0; j < count; j++) {
			if (position == j) {
				((ImageView)navLinearLayout.getChildAt(position)).setImageBitmap(displayImage);
			} else {
				((ImageView)navLinearLayout.getChildAt(j)).setImageBitmap(hideImage);
			}
		}
	}
	
	/**
	 * 描述：添加可播放视图.
	 *
	 * @param view the view
	 */
	public void addView(View view){
		mListViews.add(view);
		if(view instanceof AbsListView){
		}else{
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mOnItemClickListener!=null){
						mOnItemClickListener.onClick(position);
					}
				}
			});
			view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if(mAbOnTouchListener!=null){
					    mAbOnTouchListener.onTouch(event);
					}
					return false;
				}
			});
		}
		
		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();    
	}
	
	/**
	 * 描述：添加可播放视图列表.
	 *
	 * @param views the views
	 */
	public void addViews(List<View> views){
		mListViews.addAll(views);
		for(View view:views){
			view.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(mOnItemClickListener!=null){
						mOnItemClickListener.onClick(position);
					}
				}
			});
			
			view.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View view, MotionEvent event) {
					if(mAbOnTouchListener!=null){
					   mAbOnTouchListener.onTouch(event);
					}
					return false;
				}
			});
		}
		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}
	
	/**
	 * 描述：删除可播放视图.
	 *
	 */
	@Override
	public void removeAllViews(){
		mListViews.clear();
		mAbViewPagerAdapter.notifyDataSetChanged();
		creatIndex();
	}
	
	
	
	/**
	 * 描述：设置页面切换事件.
	 *
	 * @param position the position
	 */
	private void onPageScrolledCallBack(int position) {
		if(mAbScrolledListener!=null){
			mAbScrolledListener.onScroll(position);
		}
		
	}
	
	/**
	 * 描述：设置页面切换事件.
	 *
	 * @param position the position
	 */
	private void onPageSelectedCallBack(int position) {
		if(mAbChangeListener!=null){
			mAbChangeListener.onChange(position);
		}
		
	}
	
	
	/** 用与轮换的 handler. */
	private Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if (msg.what==0) {
				int count = mListViews.size();
				int i = mViewPager.getCurrentItem();
				if(playingDirection==0){
					if(i == count-1){
						playingDirection = -1;
						i--;  
					}else{
						i++;
					}
				}else{
					if(i == 0){
						playingDirection = 0;
						i++;
					}else{
						i--;
					}
				}
				
				mViewPager.setCurrentItem(i, true);
				if(play){
					handler.postDelayed(runnable, 5000);  
				}
		     }
		}
		
	};  
	
	/** 用于轮播的线程. */
	private Runnable runnable = new Runnable() {  
	    public void run() {  
	    	if(mViewPager!=null){
	    		handler.sendEmptyMessage(0);
			} 
	    }  
	};  

	
	/**
	 * 描述：自动轮播.
	 */
	public void startPlay(){
		if(handler!=null){
		   play  = true;
		   handler.postDelayed(runnable, 5000);  
		}
	}
	
	/**
	 * 描述：自动轮播.
	 */
	public void stopPlay(){
		if(handler!=null){
			play  = false;
			handler.removeCallbacks(runnable);  
		}
	}
	
	/**
	 * 设置点击事件监听.
	 *
	 * @param onItemClickListener the new on item click listener
	 */
	public void setOnItemClickListener(AbOnItemClickListener onItemClickListener) {
		mOnItemClickListener = onItemClickListener;
	}
	
	
	/**
	 * 描述：设置页面切换的监听器.
	 *
	 * @param abChangeListener the new on page change listener
	 */
    public void setOnPageChangeListener(AbOnChangeListener abChangeListener) {
    	mAbChangeListener = abChangeListener;
	}
    
    /**
     * 描述：设置页面滑动的监听器.
     *
     * @param abScrolledListener the new on page scrolled listener
     */
    public void setOnPageScrolledListener(AbOnScrollListener abScrolledListener) {
    	mAbScrolledListener = abScrolledListener;
	}
    
    /**
     * 描述：设置页面Touch的监听器.
     *
     * @param abOnTouchListener the new on touch listener
     */
    public void setOnTouchListener(AbOnTouchListener abOnTouchListener){
    	mAbOnTouchListener = abOnTouchListener;
    }
    
    
	/**
	 * Sets the page line image.
	 *
	 * @param displayImage the display image
	 * @param hideImage the hide image
	 */
	public void setPageLineImage(Bitmap displayImage,Bitmap hideImage) {
		this.displayImage = displayImage;
		this.hideImage = hideImage;
		creatIndex();
		
	}

	/**
	 * 描述：获取这个滑动的ViewPager类.
	 *
	 * @return the view pager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}
	
	/**
	 * 描述：获取当前的View的数量.
	 *
	 * @return the count
	 */
	public int getCount() {
		return mListViews.size();
	}
	
	/**
	 * 描述：设置页显示条的位置,在AddView前设置.
	 *
	 * @param horizontalGravity the nav horizontal gravity
	 */
	public void setNavHorizontalGravity(int horizontalGravity) {
		navHorizontalGravity = horizontalGravity;
	}
	
	/**
	 * 如果外层有ScrollView需要设置.
	 *
	 * @param parentScrollView the new parent scroll view
	 */
	public void setParentScrollView(ScrollView parentScrollView) {
		this.mViewPager.setParentScrollView(parentScrollView);
	}
	
	/**
	 * 如果外层有ListView需要设置.
	 *
	 * @param parentListView the new parent list view
	 */
	public void setParentListView(ListView parentListView) {
		this.mViewPager.setParentListView(parentListView);
	}
	
	/**
	 * 描述：设置导航点的背景.
	 *
	 * @param resid the new nav layout background
	 */
	public void setNavLayoutBackground(int resid){
		navLinearLayout.setBackgroundResource(resid);
	}
	
	/**
	 * 监听器.
	 *
	 * @see AbOnChangeEvent
	 */
	public interface AbOnChangeListener {
	    
	    /**
	     * 改变.
	     * @param position the position
	     */
	    public void onChange(int position); 

	}
	
	/**
	 * 条目点击接口.
	 *
	 * @see AbOnItemClickEvent
	 */
    public interface AbOnItemClickListener {
        
        /**
         * 描述：点击事件.
         * @param position 索引
         */
        public void onClick(int position); 
    }
    
    /**
     * 滚动.
     *
     * @see AbOnScrollEvent
     */
    public interface AbOnScrollListener {
        
        /**
         * 滚动.
         * @param arg1 返回参数
         */
        public void onScroll(int arg1); 
        
        /**
         * 滚动停止.
         */
        public void onScrollStoped();

        /**
         * 滚到了最左边.
         */
        public void onScrollToLeft();

        /**
         * 滚到了最右边.
         */
        public void onScrollToRight();

    }
    
    /**
     * 触摸屏幕接口.
     *
     * @see AbOnTouchEvent
     */
    public interface AbOnTouchListener {
    	/**
    	 * 描述：Touch事件.
    	 *
    	 * @param event 触摸手势
    	 */
        public void onTouch(MotionEvent event); 
    }
	
}
