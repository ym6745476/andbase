package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbMultiColumnListView;
import com.ab.view.pullview.AbMultiColumnListView.OnScrollListener;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.MultiColumnImageListAdapter;
import com.andbase.demo.model.ImageInfo;
import com.andbase.global.MyApplication;

public class PullToRefreshMultiColumnListActivity extends AbActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener {

	private MyApplication application;
	private List<ImageInfo> mImageList = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private AbMultiColumnListView mListView = null;
	private MultiColumnImageListAdapter myListViewAdapter = null;
	private int currentPage = 1;
	private AbTitleBar mAbTitleBar = null;
	private AbHttpUtil mAbHttpUtil = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.pull_to_refresh_multi_list);
		application = (MyApplication) abApplication;

		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.multi_column_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		
		// 获取Http工具类
		mAbHttpUtil = AbHttpUtil.getInstance(this);

		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
		mListView = (AbMultiColumnListView) this.findViewById(R.id.mListView);

		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		// 获取ListView对象
		mListView = (AbMultiColumnListView) this.findViewById(R.id.mListView);

		// ListView数据
		mImageList = new ArrayList<ImageInfo>();

		// 使用自定义的Adapter
		myListViewAdapter = new MultiColumnImageListAdapter(this, mImageList);
		mListView.setAdapter(myListViewAdapter);
		
		//如果里面有图片是动态加载的，请在这配置!!!,系统要处理释放
		mListView.setReleaseImageResIds(new int[]{R.id.itemsIcon});
		
		refreshTask();
		
		mListView.setOnScrollListener(new OnScrollListener(){

			@Override
			public void onScrollChanged(int x, int y, int oldx, int oldy) {
				// TODO Auto-generated method stub
			}
		});

	}
	
	@Override
	public void onFooterLoad(AbPullToRefreshView view) {
		loadMoreTask();
	}

	@Override
	public void onHeaderRefresh(AbPullToRefreshView view) {
		refreshTask();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	public void refreshTask() {
		currentPage = 1;
		String url = "http://www.duitang.com/album/1733789/masn/p/"
				+ currentPage + "/24/";
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {
			

			@Override
			public void onStart() {
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				List<ImageInfo> mNewImageList = parseJSON(content);
				mImageList.clear();
				if (mNewImageList != null && mNewImageList.size() > 0) {
					mImageList.addAll(mNewImageList);
					myListViewAdapter.notifyDataSetChanged();
				}
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				AbToastUtil.showToast(PullToRefreshMultiColumnListActivity.this,error.getMessage());
			}

			public void onFinish() {
			};

		});

	}

	public void loadMoreTask() {
		currentPage++;
		String url = "http://www.duitang.com/album/1733789/masn/p/"
				+ currentPage + "/24/";
		mAbHttpUtil.get(url, new AbStringHttpResponseListener() {

			@Override
			public void onStart() {
			}

			@Override
			public void onSuccess(int statusCode, String content) {
				List<ImageInfo> mNewImageList = parseJSON(content);

				if (mNewImageList != null && mNewImageList.size() > 0) {
					mImageList.addAll(mNewImageList);
					myListViewAdapter.notifyDataSetChanged();
				}
				mAbPullToRefreshView.onFooterLoadFinish();
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				AbToastUtil.showToast(PullToRefreshMultiColumnListActivity.this,error.getMessage());
			}

			public void onFinish() {
			};

		});
	}
	

	/**
	 * 
	 * 描述：数据来源
	 * 
	 * @param json
	 * @return
	 * @throws
	 */
	public List<ImageInfo> parseJSON(String json) {
		List<ImageInfo> mImageList = new ArrayList<ImageInfo>();
		try {
			if (null != json) {
				JSONObject newsObject = new JSONObject(json);
				JSONObject jsonObject = newsObject.getJSONObject("data");
				JSONArray blogsJson = jsonObject.getJSONArray("blogs");
				ImageInfo imageInfo = null;
				for (int i = 0; i < blogsJson.length(); i++) {
					imageInfo = new ImageInfo();
					JSONObject newsInfoLeftObject = blogsJson.getJSONObject(i);
					imageInfo.setUrl(newsInfoLeftObject.isNull("isrc") ? ""
							: newsInfoLeftObject.getString("isrc"));
					imageInfo.setWidth(newsInfoLeftObject.getInt("iwd"));
					imageInfo.setHeight(newsInfoLeftObject.getInt("iht"));
					mImageList.add(imageInfo);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mImageList;
	}

}
