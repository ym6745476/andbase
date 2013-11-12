package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.Random;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTaskCallback;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullGridView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageGridAdapter;
import com.andbase.global.MyApplication;
import com.andbase.model.User;

public class PullToRefreshGridActivity extends AbActivity {
	
	private int currentPage = 1;
	private MyApplication application;
	private ArrayList<User> mUserList = null;
	private ArrayList<User> mNewUserList = null;
	private AbPullGridView mAbPullGridView = null;
	private GridView mGridView = null;
	private ImageGridAdapter myGridViewAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private AbTaskQueue mAbTaskQueue = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setAbContentView(R.layout.photo_grid);
	    
	    AbTitleBar mAbTitleBar = this.getTitleBar();
	    mAbTitleBar.setTitleText(R.string.pull_list_name);
	    mAbTitleBar.setLogo(R.drawable.button_selector_back);
	    mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
	    mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
	    mAbTitleBar.setLogoLine(R.drawable.line);
        
	    mAbTaskQueue = AbTaskQueue.getInstance();
	    mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
	    
		application = (MyApplication) this.getApplication();
		mAbPullGridView = (AbPullGridView)findViewById(R.id.mPhotoGridView);
		
		//开关默认打开
        mAbPullGridView.setPullRefreshEnable(true); 
        mAbPullGridView.setPullLoadEnable(true);
		
		mGridView = mAbPullGridView.getGridView();
		mGridView.setColumnWidth(150);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setHorizontalSpacing(5);
		
		//Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade);
	    //得到一个LayoutAnimationController对象;
	    //LayoutAnimationController lac = new LayoutAnimationController(animation);
		//mGridView.setLayoutAnimation(lac);
		/*AlphaAnimation animationAlpha = new AlphaAnimation(0.0f,1.0f);  
	    //得到一个LayoutAnimationController对象;
	    LayoutAnimationController lac = new LayoutAnimationController(animationAlpha);
	    //设置控件显示的顺序;
	    lac.setOrder(LayoutAnimationController.ORDER_RANDOM);
	    //设置控件显示间隔时间;
	    lac.setDelay(0.5f);
	    //为View设置LayoutAnimationController属性;
		mGridView.setLayoutAnimation(lac);*/

		mGridView.setNumColumns(GridView.AUTO_FIT);
		mGridView.setPadding(5, 5, 5, 5);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(5);
		// ListView数据
		mUserList = new ArrayList<User>();
		// 使用自定义的Adapter
		myGridViewAdapter = new ImageGridAdapter(this, mUserList,
				R.layout.photo_grid_items, new String[] { "itemsIcon" },
				new int[] { R.id.itemsIcon });
		mAbPullGridView.setAdapter(myGridViewAdapter);
		
		showProgressDialog();
		
    	//定义两种查询的事件
    	final AbTaskItem item1 = new AbTaskItem();
		item1.callback = new AbTaskCallback() {

			@Override
			public void update() {
				removeProgressDialog();
				mUserList.clear();
				if(mNewUserList!=null && mNewUserList.size()>0){
					mUserList.addAll(mNewUserList);
					myGridViewAdapter.notifyDataSetChanged();
					mNewUserList.clear();
   		    	}
				mAbPullGridView.stopRefresh();
			}

			@Override
			public void get() {
	   		    try {
	   		    	currentPage = 1;
	   		    	Thread.sleep(1000);
	   		    	mNewUserList =  new ArrayList<User>() ;
	   				
	   				for (int i = 0; i < 40; i++) {
	   					final User mUser = new User();
	   					mUser.setPhotoUrl(mPhotoList.get(new Random().nextInt(mPhotoList.size())));
	   					mNewUserList.add(mUser);
	   				}
	   		    } catch (Exception e) {
	   		    	showToastInThread(e.getMessage());
	   		    }
		  };
		};
		
		final AbTaskItem item2 = new AbTaskItem();
		item2.callback = new AbTaskCallback() {

			@Override
			public void update() {
				if(mNewUserList!=null && mNewUserList.size()>0){
					mUserList.addAll(mNewUserList);
					myGridViewAdapter.notifyDataSetChanged();
                	mNewUserList.clear();
                	mAbPullGridView.stopLoadMore(true);
                }else{
                	//没有新数据了
                	mAbPullGridView.stopLoadMore(false);
                }
				
			}

			@Override
			public void get() {
	   		    try {
	   		    	currentPage++;
	   		    	Thread.sleep(1000);
	   		    	mNewUserList =  new ArrayList<User>() ;
	   				for (int i = 0; i < 8; i++) {
	   					final User mUser = new User();
	   					mUser.setPhotoUrl(mPhotoList.get(new Random().nextInt(mPhotoList.size())));
	   					mNewUserList.add(mUser);
	   				}
	   		    } catch (Exception e) {
	   		    	currentPage--;
	   		    	mNewUserList.clear();
	   		    	showToastInThread(e.getMessage());
	   		    }
	   		   
		  };
		};
		//设置两种查询的事件
		mAbPullGridView.setAbOnListViewListener(new AbOnListViewListener() {
			
			@Override
			public void onRefresh() {
				//第一次下载数据
				mAbTaskQueue.execute(item1);
			}
			
			@Override
			public void onLoadMore() {
				mAbTaskQueue.execute(item2);
			}
		});
		
    	//第一次下载数据
		mAbTaskQueue.execute(item1);
		
		mAbPullGridView.getGridView().setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				showToast(""+position);
			}
    		
    	});
		

	}


}
