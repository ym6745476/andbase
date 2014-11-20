
package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.fragment.AbFragment;
import com.ab.http.AbHttpListener;
import com.ab.http.AbRequestParams;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.andbase.R;
import com.andbase.demo.adapter.ArticleListAdapter;
import com.andbase.demo.model.Article;
import com.andbase.global.MyApplication;
import com.andbase.web.NetworkWeb;


public class FragmentRefresh extends AbFragment {
	
	private MyApplication application;
	private Activity mActivity = null;
	private List<Article> mList = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView mListView = null;
	private int currentPage = 1;
	private ArticleListAdapter myListViewAdapter = null;
	private int total = 50;
	private int pageSize = 5;

	@Override
	public View onCreateContentView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		
		 mActivity = this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		
		 View view = inflater.inflate(R.layout.pull_to_refresh_list, null);
		 //获取ListView对象
         mAbPullToRefreshView = (AbPullToRefreshView)view.findViewById(R.id.mPullRefreshView);
         mListView = (ListView)view.findViewById(R.id.mListView);
        
         //设置监听器
         mAbPullToRefreshView.setOnHeaderRefreshListener(new OnHeaderRefreshListener() {
			
			@Override
			public void onHeaderRefresh(AbPullToRefreshView view) {
				refreshTask();
			}
		});
         mAbPullToRefreshView.setOnFooterLoadListener(new OnFooterLoadListener() {
			
			@Override
			public void onFooterLoad(AbPullToRefreshView view) {
				loadMoreTask();
				
			}
		});
        
         //设置进度条的样式
         mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
         
         //ListView数据
         mList = new ArrayList<Article>();
    	
    	 //使用自定义的Adapter
    	 myListViewAdapter = new ArticleListAdapter(mActivity, mList);
    	 mListView.setAdapter(myListViewAdapter);
    	 //item被点击事件
    	 mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
    	 });
    	 
    	 //加载数据必须
    	 this.setAbFragmentOnLoadListener(new AbFragmentOnLoadListener(){

			@Override
			public void onLoad() {
				//第一次下载数据
				refreshTask();
			}
    		 
    	 });
    	 
    	 return view;
	} 
	
	@Override
	public void setResource() {
		//设置加载的资源
		this.setLoadDrawable(R.drawable.ic_load);
		this.setLoadMessage("正在查询,请稍候");
		 
		this.setRefreshDrawable(R.drawable.ic_refresh);
		this.setRefreshMessage("请求出错，请重试");
	}

	/**
	 * 下载数据
	 */
	public void refreshTask() {
		currentPage = 1;
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("cityCode", "11");
		params.put("pageSize", String.valueOf(pageSize));
		params.put("toPageNo",String.valueOf(currentPage));
		// 下载网络数据
		NetworkWeb web = NetworkWeb.newInstance(mActivity);
		web.findLogList(params, new AbHttpListener(){

			@Override
			public void onSuccess(List<?> newList) {
				mList.clear();
				if(newList!=null && newList.size()>0){
					mList.addAll((List<Article>)newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullToRefreshView.onHeaderRefreshFinish();
				
				//模拟用，真是开发中需要直接调用run内的内容
				new Handler().postDelayed(new Runnable(){

					@Override
					public void run() {
						//显示内容
						showContentView();
					}
					
				}, 3000);
				
			}

			@Override
			public void onFailure(String content) {
				AbToastUtil.showToast(mActivity, content);
				//显示重试的框
				showRefreshView();
			}
			
		});
	}
    
    public void loadMoreTask(){
    	currentPage++;
		// 绑定参数
		AbRequestParams params = new AbRequestParams();
		params.put("cityCode", "11");
		params.put("pageSize", String.valueOf(pageSize));
		params.put("toPageNo",String.valueOf(currentPage));
    	// 下载网络数据
		NetworkWeb web = NetworkWeb.newInstance(this.getActivity());
		web.findLogList(params, new AbHttpListener(){

			@Override
			public void onSuccess(List<?> newList) {
				if(newList!=null && newList.size()>0){
					mList.addAll((List<Article>)newList);
	                myListViewAdapter.notifyDataSetChanged();
	                newList.clear();
   		    	}
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void onFailure(String content) {
				
			}
			
		});
    }

}

