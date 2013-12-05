package com.ab.view.sliding;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.adapter.AbFragmentPagerAdapter;
import com.ab.global.AbAppData;

/**
 * This widget implements the dynamic action bar tab behavior that can change
 * across different configurations or circumstances.
 */
public class AbSlidingTabView extends LinearLayout {
	
	/** The tag. */
	private static String TAG = "AbSlidingTabView";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
  
	private Context context;
	
	private Runnable mTabSelector;
    
    private ViewPager.OnPageChangeListener mListener;
    
    public int mMaxTabWidth;
    
    private int mSelectedTabIndex;
    
    /**tab的背景*/
    private int tabBackgroundResource = -1;
    
    /**tab的文字大小*/
	private int tabTextSize = 16;
	
	/**tab的文字颜色*/
	private int tabTextColor = Color.BLACK;
	
	/**tab的选中文字颜色*/
	private int tabSelectColor = Color.BLACK;
	
	/** tab的线性布局. */
	private LinearLayout mTabLayout = null;
	
	/** tab的线性布局父. */
	private HorizontalScrollView mTabScrollView  = null;
	
	/** The m view pager. */
	private ViewPager mViewPager;
	
	/**tab的文字*/
	private List<String> tabItemTextList = null;
	
	/**tab的图标*/
	private List<Drawable> tabItemDrawableList = null;
	
	
	/**内容的View*/
	private ArrayList<Fragment> pagerItemList = null;
	
	/**tab的列表*/
	private ArrayList<TextView> tabItemList = null;
	
	/**内容区域的适配器*/
	private AbFragmentPagerAdapter mFragmentPagerAdapter = null;
    
    private OnClickListener mTabClickListener = new OnClickListener() {
        public void onClick(View view) {
        	AbTabItemView tabView = (AbTabItemView)view;
            mViewPager.setCurrentItem(tabView.getIndex());
        }
    };

    public AbSlidingTabView(Context context) {
        this(context, null);
    }

    public AbSlidingTabView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        
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
  		mViewPager = new ViewPager(context);
  		//手动创建的ViewPager,必须调用setId()方法设置一个id
  		mViewPager.setId(1985);
  		pagerItemList = new ArrayList<Fragment>();
  	    //定义Tab栏
  		tabItemList = new ArrayList<TextView>();
  		tabItemTextList = new ArrayList<String>();
  		tabItemDrawableList = new ArrayList<Drawable>();
  		
  	    //要求必须是FragmentActivity的实例
		if(!(this.context instanceof FragmentActivity)){
			Log.e(TAG, "构造AbSlidingTabView的参数context,必须是FragmentActivity的实例。");
		}
  			
  		FragmentManager mFragmentManager = ((FragmentActivity)this.context).getSupportFragmentManager();
		mFragmentPagerAdapter = new AbFragmentPagerAdapter(
				mFragmentManager, pagerItemList);
		mViewPager.setAdapter(mFragmentPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setOffscreenPageLimit(3);
  		
		this.addView(mViewPager,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    
    }
    
    public class MyOnPageChangeListener implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			if (mListener != null) {
	            mListener.onPageScrollStateChanged(arg0);
	        }
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			if (mListener != null) {
	            mListener.onPageScrolled(arg0, arg1, arg2);
	        }
		}

		@Override
		public void onPageSelected(int arg0) {
			setCurrentItem(arg0);
	        if (mListener != null) {
	            mListener.onPageSelected(arg0);
	        }
		}
		
	}

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

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mTabSelector != null) {
            // Re-post the selector we saved
            post(mTabSelector);
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mTabSelector != null) {
            removeCallbacks(mTabSelector);
        }
    }
    
    /**
     * 
     * 描述：创造一个Tab
     * @param text
     * @param index
     * @throws 
     */
    private void addTab(String text, int index) {
    	addTab(text,index,null);
    }
    
    /**
     * 
     * 描述：创造一个Tab
     * @param text
     * @param index
     * @throws 
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
        mTabLayout.addView(tabView, new LayoutParams(0,LayoutParams.FILL_PARENT,1));
    }

    /**
     * 
     * 描述：tab有变化刷新
     * @throws 
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
     * 
     * 描述：设置显示哪一个
     * @param item
     * @throws 
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
     * 
     * 描述：设置一个外部的监听器
     * @param listener
     * @throws 
     */
    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mListener = listener;
    }

	/**
	 * 
	 * 描述：设置tab文字的颜色
	 * @param tabColor
	 * @throws 
	 */
	public void setTabTextColor(int tabColor) {
		this.tabTextColor = tabColor;
	}

	/**
	 * 
	 * 描述：设置选中的颜色
	 * @param tabColor
	 * @throws 
	 */
	public void setTabSelectColor(int tabColor) {
		this.tabSelectColor = tabColor;
	}

	/**
     * 
     * 描述：设置文字大小
     * @param tabTextSize
     * @throws 
     */
	public void setTabTextSize(int tabTextSize) {
		this.tabTextSize = tabTextSize;
	}
	
	/**
     * 描述：设置tab的背景选择
     * @param resid
     * @throws 
     */
	public void setTabBackgroundResource(int resid) {
    	tabBackgroundResource = resid;
    }
	
	/**
	 * 
	 * 描述：设置Tab的背景
	 * @param res
	 * @throws 
	 */
	public void setTabLayoutBackgroundResource(int res) {
		this.mTabLayout.setBackgroundResource(res);
	}
	
	/**
	 * 
	 * 描述：增加一组内容与tab
	 * @throws 
	 */
	public void addItemViews(List<String> tabTexts,List<Fragment> fragments){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 
	 * 描述：增加一组内容与tab
	 * @throws 
	 */
	public void addItemViews(List<String> tabTexts,List<Drawable> drawables,List<Fragment> fragments){
		
		tabItemTextList.addAll(tabTexts);
		pagerItemList.addAll(fragments);
		tabItemDrawableList.addAll(drawables);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 
	 * 描述：增加一个内容与tab
	 * @throws 
	 */
	public void addItemView(String tabText,Fragment fragment){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 
	 * 描述：增加一个内容与tab
	 * @throws 
	 */
	public void addItemView(String tabText,Drawable drawable,Fragment fragment){
		tabItemTextList.add(tabText);
		pagerItemList.add(fragment);
		tabItemDrawableList.add(drawable);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	
	/**
	 * 
	 * 描述：删除某一个
	 * @param index
	 * @throws 
	 */
	public void removeItemView(int index){
		
		mTabLayout.removeViewAt(index);
		pagerItemList.remove(index);
		tabItemList.remove(index);
		tabItemDrawableList.remove(index);
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 
	 * 描述：删除所有
	 * @throws 
	 */
	public void removeAllItemViews(){
		mTabLayout.removeAllViews();
		pagerItemList.clear();
		tabItemList.clear();
		tabItemDrawableList.clear();
		mFragmentPagerAdapter.notifyDataSetChanged();
		notifyTabDataSetChanged();
	}
	
	/**
	 * 
	 * 描述：获取这个View的ViewPager
	 * @return
	 * @throws 
	 */
	public ViewPager getViewPager() {
		return mViewPager;
	}
	
	/**
	 * 
	 * 描述：设置每个tab的边距
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @throws 
	 */
	public void setTabPadding(int left, int top, int right, int bottom) {
		for(int i = 0;i<tabItemList.size();i++){
			TextView tv = tabItemList.get(i);
			tv.setPadding(left, top, right, bottom);
		}
	}
    
}
