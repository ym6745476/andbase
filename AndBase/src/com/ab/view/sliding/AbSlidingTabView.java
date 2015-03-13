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
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ab.adapter.AbFragmentPagerAdapter;
import com.ab.util.AbLogUtil;
import com.ab.view.sample.AbViewPager;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbSlidingTabView.java 
 * 描述：滑动的tab,tab不固定超出后可滑动.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
@SuppressLint("NewApi")
public class AbSlidingTabView extends LinearLayout {
	
	/** The context. */
	private Context context;
	
	/** The m tab selector. */
	private Runnable mTabSelector;
    
    /** The m listener. */
    private ViewPager.OnPageChangeListener mListener;
    
    /** The m max tab width. */
    public int mMaxTabWidth;
    
    /** The m selected tab index. */
    private int mSelectedTabIndex;
    
    /** tab的背景. */
    private int tabBackgroundResource = -1;
    
    /** tab的文字大小. */
	private int tabTextSize = 30;
	
	/** tab的文字颜色. */
	private int tabTextColor = Color.BLACK;
	
	/** tab的选中文字颜色. */
	private int tabSelectColor = Color.BLACK;
	
	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	
	/** tab的线性布局父. */
	private HorizontalScrollView mTabScrollView  = null;
	
	/** The m view pager. */
	private AbViewPager mViewPager;
	
	/** tab的文字. */
	private List<String> tabItemTextList = null;
	
	/** tab的图标. */
	private List<Drawable> tabItemDrawableList = null;
	
	/** 内容的View. */
	private ArrayList<Fragment> pagerItemList = null;
	
	/** tab的列表. */
	private ArrayList<TextView> tabItemList = null;
	
	/** 内容区域的适配器. */
	private AbFragmentPagerAdapter mFragmentPagerAdapter = null;
    
    /** The m tab click listener. */
    private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
        	AbTabItemView tabView = (AbTabItemView)view;
            mViewPager.setCurrentItem(tabView.getIndex());
        }
    };

    /**
     * Instantiates a new ab sliding tab view.
     *
     * @param context the context
     */
    public AbSlidingTabView(Context context) {
        this(context, null);
    }
    
    /**
     * Instantiates a new ab sliding tab view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbSlidingTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
  		FragmentManager mFragmentManager = ((FragmentActivity)this.context).getFragmentManager();
		mFragmentPagerAdapter = new AbFragmentPagerAdapter(
				mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
		this.addView(mViewPager,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    
    }
    
    /**
     * Instantiates a new ab sliding tab view.
     *
     * @param fragment the fragment
     */
    public AbSlidingTabView(Fragment fragment) {
    	
        super(fragment.getActivity());
       
        this.context = fragment.getActivity();
        initView();
  		if(VERSION.SDK_INT <= 17){	
  			AbLogUtil.e(AbSlidingTabView.class, "AbSlidingTabView(Fragment fragment) 要求最低SDK版本17");
  		    return;
  		}
  		FragmentManager mFragmentManager = fragment.getChildFragmentManager();
		mFragmentPagerAdapter = new AbFragmentPagerAdapter(
				mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
  		
		this.addView(mViewPager,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    
    }
    
    /**
     * Inits the view.
     */
    public void initView(){
    	this.setOrientation(LinearLayout.VERTICAL);
 		this.setBackgroundColor(Color.rgb(255, 255, 255));
 		
 		mTabScrollView  = new HorizontalScrollView(context);
 		mTabScrollView.setHorizontalScrollBarEnabled(false);
 		mTabScrollView.setSmoothScrollingEnabled(true);
 		
 		mTabLayout = new LinearLayout(context);
 		mTabLayout.setOrientation(LinearLayout.HORIZONTAL);
 		mTabLayout.setGravity(Gravity.CENTER);
 		
 		//mTabLayout是内容宽度
 		mTabScrollView.addView(mTabLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT));
 		
 		this.addView(mTabScrollView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
         
 		//内容的View的适配
   		mViewPager = new AbViewPager(context);
   		//手动创建的ViewPager,必须调用setId()方法设置一个id
   		mViewPager.setId(1985);
   		pagerItemList = new ArrayList<Fragment>();
   	    //定义Tab栏
   		tabItemList = new ArrayList<TextView>();
   		tabItemTextList = new ArrayList<String>();
   		tabItemDrawableList = new ArrayList<Drawable>();
   		
   	    //要求必须是FragmentActivity的实例
 		if(!(this.context instanceof FragmentActivity)){
 			AbLogUtil.e(AbSlidingTabView.class, "构造AbSlidingTabView的参数context,必须是FragmentActivity的实例。");
 		}
    }
    
    /**
     * The listener interface for receiving myOnPageChange events.
     * The class that is interested in processing a myOnPageChange
     * event implements this interface, and the object created
     * with that class is registered with a component using the
     * component's <code>addMyOnPageChangeListener<code> method. When
     * the myOnPageChange event occurs, that object's appropriate
     * method is invoked.
     *
     * @see MyOnPageChangeEvent
     */
    public class MyOnPageChangeListener implements OnPageChangeListener{

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrollStateChanged(int)
		 */
		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (mListener != null) {
	            mListener.onPageScrollStateChanged(arg0);
	        }
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageScrolled(int, float, int)
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (mListener != null) {
	            mListener.onPageScrolled(arg0, arg1, arg2);
	        }
		}

		/* (non-Javadoc)
		 * @see android.support.v4.view.ViewPager.OnPageChangeListener#onPageSelected(int)
		 */
		@Override
		public void onPageSelected(int arg0) {
			setCurrentItem(arg0);
	        if (mListener != null) {
	            mListener.onPageSelected(arg0);
	        }
		}
		
	}

    /* (non-Javadoc)
     * @see android.widget.LinearLayout#onMeasure(int, int)
     */
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
        mTabScrollView.setFillViewport(lockedExpanded);

        final int childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
            if (childCount > 2) {
                mMaxTabWidth = (int)(MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
            } else {
                mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
            }
        } else {
            mMaxTabWidth = -1;
        }

        final int oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int newWidth = getMeasuredWidth();

        if (lockedExpanded && oldWidth != newWidth) {
            // Recenter the tab display if we're at a new (scrollable) size.
            setCurrentItem(mSelectedTabIndex);
        }
    }

    /**
     * Animate to tab.
     *
     * @param position the position
     */
    private void animateToTab(final int position) {
        final View tabView = mTabLayout.getChildAt(position);
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
        mTabSelector = new Runnable() {
            public void run() {
                final int scrollPos = tabView.getLeft() - (getWidth() - tabView.getWidth()) / 2;
                mTabScrollView.smoothScrollTo(scrollPos, 0);
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    /* (non-Javadoc)
     * @see android.view.View#onAttachedToWindow()
     */
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    /* (non-Javadoc)
     * @see android.view.View#onDetachedFromWindow()
     */
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }
    
    /**
     * 描述：创造一个Tab.
     *
     * @param text the text
     * @param index the index
     */
    private void addTab(String text, int index) {
    	addTab(text,index,null);
    }
    
    /**
     * 描述：创造一个Tab.
     *
     * @param text the text
     * @param index the index
     * @param top the top
     */
    private void addTab(String text, int index,Drawable top) {
   	
    	AbTabItemView tabView = new AbTabItemView(this.context);
        if(tabBackgroundResource!=-1){
        	tabView.setTabBackgroundResource(tabBackgroundResource);
        }
        if(top!=null){
        	tabView.setTabCompoundDrawables(null, top, null, null);
        }
    	tabView.setTabTextColor(tabTextColor);
    	tabView.setTabTextSize(tabTextSize);
        
        tabView.init(index,text);
        tabItemList.add(tabView.getTextView());
        tabView.setOnClickListener(mTabClickListener);
        mTabLayout.addView(tabView, new LayoutParams(0,LayoutParams.MATCH_PARENT,1));
    }

    /**
     * 描述：tab有变化刷新.
     */
    public void notifyTabDataSetChanged() {
        mTabLayout.removeAllViews();
        tabItemList.clear();
        final int count = mFragmentPagerAdapter.getCount();
        for (int i = 0; i < count; i++) {
        	if(tabItemDrawableList.size()>0){
        		addTab(tabItemTextList.get(i), i,tabItemDrawableList.get(i));
        	}else{
        		addTab(tabItemTextList.get(i), i);
        	}
            
        }
        if (mSelectedTabIndex > count) {
            mSelectedTabIndex = count - 1;
        }
        setCurrentItem(mSelectedTabIndex);
        requestLayout();
    }

    /**
     * 描述：设置显示哪一个.
     *
     * @param item the new current item
     */
    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mSelectedTabIndex = item;
        final int tabCount = mTabLayout.getChildCount();
        for (int i = 0; i < tabCount; i++) {
            final AbTabItemView child = (AbTabItemView)mTabLayout.getChildAt(i);
            final boolean isSelected = (i == item);
            child.setSelected(isSelected);
            if (isSelected) {
            	child.setTabTextColor(tabSelectColor);
                animateToTab(item);
            }else{
            	child.setTabTextColor(tabTextColor);
            }
        }
    }

    /**
     * 描述：设置一个外部的监听器.
     *
     * @param listener the new on page change listener
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

	/**
	 * 描述：设置tab文字的颜色.
	 *
	 * @param tabColor the new tab text color
	 */
	public void setTabTextColor(int tabColor) {
		this.tabTextColor = tabColor;
	}

	/**
	 * 描述：设置选中的颜色.
	 *
	 * @param tabColor the new tab select color
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
	}

	/**
	 * 描述：设置文字大小.
	 *
	 * @param tabTextSize the new tab text size
	 */
	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}
	
	/**
	 * 描述：设置单个tab的背景选择器.
	 *
	 * @param resid the new tab background resource
	 */
	public void setTabBackgroundResource(int resid) {
    	tabBackgroundResource = resid;
    }
	
	/**
	 * 描述：设置Tab的背景.
	 *
	 * @param resid the new tab layout background resource
	 */
	public void setTabLayoutBackgroundResource(int resid) {
		this.mTabLayout.setBackgroundResource(resid);
	}
	
	/**
	 * 描述：增加一组内容与tab.
	 *
	 * @param tabTexts the tab texts
	 * @param fragments the fragments
	 */
	public void addItemViews(List<String> tabTexts,List<Fragment> fragments){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：增加一组内容与tab.
	 *
	 * @param tabTexts the tab texts
	 * @param fragments the fragments
	 * @param drawables the drawables
	 */
	public void addItemViews(List<String> tabTexts,List<Fragment> fragments,List<Drawable> drawables){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		tabItemDrawableList.addAll(drawables);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：增加一个内容与tab.
	 *
	 * @param tabText the tab text
	 * @param fragment the fragment
	 */
	public void addItemView(String tabText,Fragment fragment){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：增加一个内容与tab.
	 *
	 * @param tabText the tab text
	 * @param fragment the fragment
	 * @param drawable the drawable
	 */
	public void addItemView(String tabText,Fragment fragment,Drawable drawable){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		tabItemDrawableList.add(drawable);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	
	/**
	 * 描述：删除某一个.
	 *
	 * @param index the index
	 */
	public void removeItemView(int index){
		
		mTabLayout.removeViewAt(index);
		pagerItemList.remove(index);
		tabItemList.remove(index);
		tabItemDrawableList.remove(index);
		tabItemTextList.remove(index); 
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：删除所有.
	 */
	public void removeAllItemViews(){
		mTabLayout.removeAllViews();
		pagerItemList.clear();
		tabItemList.clear();
		tabItemDrawableList.clear();
		tabItemTextList.clear(); 
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 描述：获取这个View的ViewPager.
	 *
	 * @return the view pager
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}
	
	/**
	 * 描述：设置每个tab的边距.
	 *
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 */
	public void setTabPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i);
			tv.setPadding(left, top, right, bottom);
		}
	}
	
	
	/**
	 * Sets the sliding enabled.
	 *
	 * @param sliding the new sliding enabled
	 */
	public void setSlidingEnabled(boolean sliding) {
		mViewPager.setPagingEnabled(sliding);
	}
    
}
