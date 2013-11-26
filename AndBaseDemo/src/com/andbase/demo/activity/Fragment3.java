
package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskQueue;
import com.ab.view.listener.AbOnChangeListener;
import com.ab.view.listener.AbOnItemClickListener;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbPullListView;
import com.ab.view.sliding.AbSlidingPlayView;
import com.andbase.R;
import com.andbase.demo.adapter.ImageListAdapter;
import com.andbase.global.MyApplication;


public class Fragment3 extends Fragment {
	
	private MyApplication application;
	private AbActivity mActivity = null;
	private List<Map<String, Object>> list = null;
	private List<Map<String, Object>> newList = null;
	private AbPullListView mAbPullListView = null;
	private int currentPage = 1;
	private AbTaskQueue mAbTaskQueue = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private ImageListAdapter myListViewAdapter = null;
	private AbSlidingPlayView mSlidingPlayView = null;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 mActivity = (AbActivity)this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.pull_list, null);
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215035600700175/T1C2mzXthaXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215025617307680/T1AQqAXqpeXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i1/13215035569460099/T16GuzXs0cXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i2/13215023694438773/T1lImmXElhXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023521330093/T1BWuzXrhcXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i4/13215035563144015/T1Q.eyXsldXXXXXXXX_!!0-item_pic.jpg_230x230.jpg");  
		 mPhotoList.add("http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg"); 
		 mAbTaskQueue = AbTaskQueue.getInstance();
	     //获取ListView对象
         mAbPullListView = (AbPullListView)view.findViewById(R.id.mListView);
         
         //ListView数据
    	 list = new ArrayList<Map<String, Object>>();
    	 //设置进度条的样式
         mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
    	 //使用自定义的Adapter
    	 myListViewAdapter = new ImageListAdapter(mActivity, list,R.layout.list_items,
				new String[] { "itemsIcon", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle,R.id.itemsText });
    	 mAbPullListView.setAdapter(myListViewAdapter);
    	 //item被点击事件
    	 mAbPullListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
    	 });
    	 
    	//组和个AbSlidingPlayView
        mSlidingPlayView = new AbSlidingPlayView(mActivity);
         
 	    final View mPlayView = inflater.inflate(R.layout.play_view_item, null);
 		ImageView mPlayImage = (ImageView) mPlayView.findViewById(R.id.mPlayImage);
 		TextView mPlayText = (TextView) mPlayView.findViewById(R.id.mPlayText);
 		mPlayText.setText("1111111111111");
 		mPlayImage.setBackgroundResource(R.drawable.pic1);
 		
 		final View mPlayView1 = inflater.inflate(R.layout.play_view_item, null);
 		ImageView mPlayImage1 = (ImageView) mPlayView1.findViewById(R.id.mPlayImage);
 		TextView mPlayText1 = (TextView) mPlayView1.findViewById(R.id.mPlayText);
 		mPlayText1.setText("2222222222222");
 		mPlayImage1.setBackgroundResource(R.drawable.pic2);
 		
 		final View mPlayView2 = inflater.inflate(R.layout.play_view_item, null);
 		ImageView mPlayImage2 = (ImageView) mPlayView2.findViewById(R.id.mPlayImage);
 		TextView mPlayText2 = (TextView) mPlayView2.findViewById(R.id.mPlayText);
 		mPlayText2.setText("33333333333333333");
 		mPlayImage2.setBackgroundResource(R.drawable.pic3);

 		mSlidingPlayView.setPageLineHorizontalGravity(Gravity.RIGHT);
 		mSlidingPlayView.addView(mPlayView);
 		mSlidingPlayView.addView(mPlayView1);
 		mSlidingPlayView.addView(mPlayView2);
 		mSlidingPlayView.startPlay();
 		//设置高度
 		mSlidingPlayView.setLayoutParams(new AbsListView.LayoutParams(LayoutParams.FILL_PARENT,300));
 		mAbPullListView.addHeaderView(mSlidingPlayView);
 		//解决冲突问题
 		mSlidingPlayView.setParentListView(mAbPullListView);
 		mSlidingPlayView.setParentListView(mAbPullListView);
 		mSlidingPlayView.setOnItemClickListener(new AbOnItemClickListener() {
 			
 			@Override
 			public void onClick(int position) {
 				mActivity.showToast("点击"+position);
 			}
 		});
 	    
         mSlidingPlayView.setOnPageChangeListener(new AbOnChangeListener() {
 			
 			@Override
 			public void onChange(int position) {
 				//mActivity.showToast("改变"+position);
 			}
 		});
    	 

	    
		 return view;
	} 
	
	

	@Override
	public void onStart() {
		super.onStart();
		//定义两种查询的事件
    	final AbTaskItem item1 = new AbTaskItem();
		item1.listener = new AbTaskListener() {

			@Override
			public void update() {
				list.clear();
				if(newList!=null && newList.size()>0){
	                list.addAll(newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullListView.stopRefresh();
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
	   					map.put("itemsIcon",mPhotoList.get(new Random().nextInt(mPhotoList.size())));
		   		    	map.put("itemsTitle", "[Fragment3]"+i);
		   		    	map.put("itemsText", "[Fragment3]..."+i);
		   		    	newList.add(map);
	   				}
	   		    } catch (Exception e) {
	   		    }
		  };
		};
		
		final AbTaskItem item2 = new AbTaskItem();
		item2.listener = new AbTaskListener() {

			@Override
			public void update() {
				if(newList!=null && newList.size()>0){
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
                }
				mAbPullListView.stopLoadMore();
				
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
	   		    }
		  };
		};
		
		mAbPullListView.setAbOnListViewListener(new AbOnListViewListener(){

			@Override
			public void onRefresh() {
				mAbTaskQueue.execute(item1);
			}

			@Override
			public void onLoadMore() {
				mAbTaskQueue.execute(item2);
			}
			
		});
		
    	//第一次下载数据
		mAbTaskQueue.execute(item1);
	}



	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}



	@Override
	public void onDestroy() {
		mSlidingPlayView.stopPlay();
		super.onDestroy();
	}

	

}

