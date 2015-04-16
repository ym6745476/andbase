package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageListAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;

public class ListNestViewPagerActivity extends AbActivity implements OnHeaderRefreshListener,OnFooterLoadListener{
	
	private MyApplication application;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView mListView = null;
	private int currentPage = 1;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private AbTitleBar mAbTitleBar = null;
	private ImageListAdapter myListViewAdapter = null;
	private AbSlidingPlayView mSlidingPlayView = null;
    
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.pull_to_refresh_list);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.list_pager_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        for (int i = 0; i < 23; i++) {
        	mPhotoList.add(Constant.BASEURL+"content/templates/amsoft/images/rand/"+i+".jpg");
		}
		//获取ListView对象
        mAbPullToRefreshView = (AbPullToRefreshView)this.findViewById(R.id.mPullRefreshView);
        mListView = (ListView)this.findViewById(R.id.mListView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);
        
        //设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        //mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        //mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        
        //ListView数据
    	list = new ArrayList<Map<String, Object>>();
    	
    	//使用自定义的Adapter
    	myListViewAdapter = new ImageListAdapter(this, list,R.layout.item_list,
				new String[] { "itemsIcon", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle,R.id.itemsText });
    	
    	//item被点击事件
    	mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
    	});
    	
		//组和个AbSlidingPlayView
        mSlidingPlayView = new AbSlidingPlayView(this);
        
	    final View mPlayView = mInflater.inflate(R.layout.play_view_item, null);
		ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
		TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
		mPlayText.setText("1111111111111");
		mPlayImage.setBackgroundResource(R.drawable.pic1);
		
		final View mPlayView1 = mInflater.inflate(R.layout.play_view_item, null);
		ImageView mPlayImage1 = (ImageView) mPlayView1.findViewById(R.id.mPlayImage);
		TextView mPlayText1 = (TextView) mPlayView1.findViewById(R.id.mPlayText);
		mPlayText1.setText("2222222222222");
		mPlayImage1.setBackgroundResource(R.drawable.pic2);
		
		final View mPlayView2 = mInflater.inflate(R.layout.play_view_item, null);
		ImageView mPlayImage2 = (ImageView) mPlayView2.findViewById(R.id.mPlayImage);
		TextView mPlayText2 = (TextView) mPlayView2.findViewById(R.id.mPlayText);
		mPlayText2.setText("33333333333333333");
		mPlayImage2.setBackgroundResource(R.drawable.pic3);

		mSlidingPlayView.setNavHorizontalGravity(Gravity.RIGHT);
		mSlidingPlayView.addView(mPlayView);
		mSlidingPlayView.addView(mPlayView1);
		mSlidingPlayView.addView(mPlayView2);
		mSlidingPlayView.startPlay();
		//设置高度
		mSlidingPlayView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,300));
		mListView.addHeaderView(mSlidingPlayView);
		//解决冲突问题
		mSlidingPlayView.setParentListView(mListView);
		mSlidingPlayView.setOnItemClickListener(new AbSlidingPlayView.AbOnItemClickListener() {
			
			@Override
			public void onClick(int position) {
				AbToastUtil.showToast(ListNestViewPagerActivity.this,"点击"+position);
			}
		});
	    
        mSlidingPlayView.setOnPageChangeListener(new AbSlidingPlayView.AbOnChangeListener() {
			
			@Override
			public void onChange(int position) {
				AbToastUtil.showToast(ListNestViewPagerActivity.this,"改变"+position);
			}
		});
        
        mListView.setAdapter(myListViewAdapter);
        
        AbDialogUtil.showProgressDialog(ListNestViewPagerActivity.this,R.drawable.progress_circular,"正在查询...");

    	//第一次下载数据
    	refreshTask();
    }
    

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void finish() {
		mSlidingPlayView.stopPlay();
		super.finish();
	}
   
	public void refreshTask(){
		AbTask mAbTask = AbTask.newInstance();
    	AbTaskItem item = new AbTaskItem();
    	item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				AbDialogUtil.removeDialog(ListNestViewPagerActivity.this);
				list.clear();
				if(newList!=null && newList.size()>0){
	                list.addAll(newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void get() {
	   		    try {
	   		    	Thread.sleep(1000);
	   		    	currentPage = 1;
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	
	   		    	for (int i = 0; i < 10; i++) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsIcon",mPhotoList.get(i));
		   		    	map.put("itemsTitle", "item"+i);
		   		    	map.put("itemsText", "item..."+i);
		   		    	newList.add(map);
	   				}
	   		    } catch (Exception e) {
	   		    }
		  };
		});
        
        mAbTask.execute(item);
    }
    
    public void loadMoreTask(){
    	
    	AbTask mAbTask = AbTask.newInstance();
        AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				if(newList!=null && newList.size()>0){
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
                }
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void get() {
	   		    try {
	   		    	currentPage++;
	   		    	Thread.sleep(1000);
	   		    	newList = new ArrayList<Map<String, Object>>();
	   		    	Map<String, Object> map = null;
	   		    	
	   		    	for (int i = 0; i < 10; i++) {
	   		    		map = new HashMap<String, Object>();
	   					map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", "item上拉"+i);
		   		    	map.put("itemsText", "item上拉..."+i);
		   		    	newList.add(map);
	   				}
	   		    	
	   		    } catch (Exception e) {
	   		    	currentPage--;
	   		    	newList.clear();
	   		    	AbToastUtil.showToastInThread(ListNestViewPagerActivity.this,e.getMessage());
	   		    }
		  };
		});
        
        mAbTask.execute(item);
    }
    
    @Override
    public void onFooterLoad(AbPullToRefreshView view) {
	    loadMoreTask();
    }
	
    @Override
    public void onHeaderRefresh(AbPullToRefreshView view) {
        refreshTask();
        
    }
}


