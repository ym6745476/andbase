package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.view.listener.AbOnListViewListener;
import com.ab.view.pullview.AbMultiColumnListView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.MultiColumnImageListAdapter;
import com.andbase.demo.model.ImageInfo;
import com.andbase.global.MyApplication;

public class PullToRefreshMultiColumnListActivity extends AbActivity implements AbOnListViewListener{
	
	private MyApplication application;
	private List<ImageInfo> mNewImageList = null;
	private AbMultiColumnListView mListView = null;
	private MultiColumnImageListAdapter myListViewAdapter = null;
	private int currentPage = 1;
	private AbTitleBar mAbTitleBar = null;
	private AbHttpUtil mAbHttpUtil = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.pull_multi_list);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.multi_column_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        //获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        
	    //获取ListView对象
        mListView = (AbMultiColumnListView)this.findViewById(R.id.mListView);
        
        //设置进度条的样式
        mListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        
        
        //ListView数据
        LinkedList<ImageInfo> list = new LinkedList<ImageInfo>();
        mNewImageList = new ArrayList<ImageInfo>();
    	
    	//使用自定义的Adapter
    	myListViewAdapter = new MultiColumnImageListAdapter(this, list);
    	mListView.setAdapter(myListViewAdapter);
    	
    	mListView.setPullLoadEnable(true);
    	mListView.setPullRefreshEnable(true); 
    	mListView.setAbOnListViewListener(this);
		
		showProgressDialog();
		onRefresh();
    }
    

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();
	}
   

	@Override
	public void onRefresh() {
		currentPage = 1;
	    String url = "http://www.duitang.com/album/1733789/masn/p/"
					+ currentPage + "/24/";
	    mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
	    	
        	
        	@Override
			public void onSuccess(int statusCode, String content) {
        		mNewImageList = parseJSON(content);
            	if(mNewImageList!=null && mNewImageList.size()>0){
					myListViewAdapter.addItemTop(mNewImageList);
	                myListViewAdapter.notifyDataSetChanged();
	                mListView.stopRefresh();
   		    	}
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
			}
            
            public void onFinish() { 
            	removeProgressDialog();
            };
            
        });
    	
	}

	@Override
	public void onLoadMore() {
		currentPage++;
		String url = "http://www.duitang.com/album/1733789/masn/p/"
						+ currentPage + "/24/";
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
            
            @Override
			public void onSuccess(int statusCode, String content) {
                mNewImageList = parseJSON(content);
            	
            	if(mNewImageList!=null && mNewImageList.size()>0){
					myListViewAdapter.addItemLast(mNewImageList);
	                myListViewAdapter.notifyDataSetChanged();
   		    	}
            	mListView.stopLoadMore();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
			}
            
            public void onFinish() { 
            	removeProgressDialog();
            };
            
        });
	}
	
	/**
	 * 
	 * 描述：数据来源
	 * @param json
	 * @return
	 * @throws 
	 */
	public List<ImageInfo> parseJSON(String json){
		List<ImageInfo> mImageList = new ArrayList<ImageInfo>();
		try {
			if (null != json) {
				JSONObject newsObject = new JSONObject(json);
				JSONObject jsonObject = newsObject.getJSONObject("data");
				JSONArray blogsJson = jsonObject.getJSONArray("blogs");
				ImageInfo newsInfo = null;
				for (int i = 0; i < blogsJson.length(); i++) {
					newsInfo = new ImageInfo();
					JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
					newsInfo.setUrl(newsInfoLeftObject.isNull("isrc") ? ""
									: newsInfoLeftObject.getString("isrc"));
					newsInfo.setHeight(newsInfoLeftObject.getInt("iht"));
					mImageList.add(newsInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mImageList;
	}
	
}


