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
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
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
	private int total = 150;
	private int pageSize = 15;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setAbContentView(R.layout.photo_grid);
	    
	    AbTitleBar mAbTitleBar = this.getTitleBar();
	    mAbTitleBar.setTitleText(R.string.pull_list_name);
	    mAbTitleBar.setLogo(R.drawable.button_selector_back);
	    mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
	    mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
	    mAbTitleBar.setLogoLine(R.drawable.line);
        
	    mAbTaskQueue = AbTaskQueue.getInstance();
	    mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/0.jpg");  
	    mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/1.jpg");
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/2.jpg");
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/3.jpg");
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/4.jpg");  
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/5.jpg");  
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/6.jpg");  
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/7.jpg"); 
		mPhotoList.add("http://www.418log.org/content/templates/default/images/rand/8.jpg");   
	    
		application = (MyApplication) this.getApplication();
		mAbPullGridView = (AbPullGridView)findViewById(R.id.mPhotoGridView);
		
		//开关默认打开
        mAbPullGridView.setPullRefreshEnable(true); 
        mAbPullGridView.setPullLoadEnable(true);
        
        //设置进度条的样式
        mAbPullGridView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullGridView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
		
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
		item1.listener = new AbTaskListener() {

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
	   				
	   				for (int i = 0; i < pageSize; i++) {
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
		item2.listener = new AbTaskListener() {

			@Override
			public void update() {
				if(mNewUserList!=null && mNewUserList.size()>0){
					mUserList.addAll(mNewUserList);
					myGridViewAdapter.notifyDataSetChanged();
                	mNewUserList.clear();
                }
				mAbPullGridView.stopLoadMore();
			}

			@Override
			public void get() {
	   		    try {
	   		    	currentPage++;
	   		    	Thread.sleep(1000);
	   		    	mNewUserList =  new ArrayList<User>() ;
	   				for (int i = 0; i < pageSize; i++) {
	   					final User mUser = new User();
	   					mUser.setPhotoUrl(mPhotoList.get(new Random().nextInt(mPhotoList.size())));
	   					if((mUserList.size()+mNewUserList.size()) < total){
	   						mNewUserList.add(mUser);
		   		    	}
	   					
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
