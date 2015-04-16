package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageListAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;

public class PullToRefreshListActivity extends AbActivity implements
		OnHeaderRefreshListener, OnFooterLoadListener {

	private MyApplication application;
	private List<Map<String, Object>> list = null;
	private AbPullToRefreshView mAbPullToRefreshView = null;
	private ListView mListView = null;
	private int currentPage = 1;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private AbTitleBar mAbTitleBar = null;
	private ImageListAdapter myListViewAdapter = null;
	private int total = 50;
	private int pageSize = 15;
	private AbLoadDialogFragment  mDialogFragment = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.pull_to_refresh_list);
		application = (MyApplication) abApplication;

		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.pull_list_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		for (int i = 0; i < 23; i++) {
			mPhotoList.add(Constant.BASEURL
					+ "content/templates/amsoft/images/rand/" + i + ".jpg");
		}

		// 获取ListView对象
		mAbPullToRefreshView = (AbPullToRefreshView) this
				.findViewById(R.id.mPullRefreshView);
		mListView = (ListView) this.findViewById(R.id.mListView);

		// 设置监听器
		mAbPullToRefreshView.setOnHeaderRefreshListener(this);
		mAbPullToRefreshView.setOnFooterLoadListener(this);

		// 设置进度条的样式
		mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));
		mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(
				this.getResources().getDrawable(R.drawable.progress_circular));

		// ListView数据
		list = new ArrayList<Map<String, Object>>();

		// 使用自定义的Adapter
		myListViewAdapter = new ImageListAdapter(this, list,
				R.layout.item_list, new String[] { "itemsIcon", "itemsTitle",
						"itemsText" }, new int[] { R.id.itemsIcon,
						R.id.itemsTitle, R.id.itemsText });
		mListView.setAdapter(myListViewAdapter);

		// item被点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
			}
		});

		//显示进度框
		mDialogFragment = AbDialogUtil.showLoadDialog(this, R.drawable.ic_load, "查询中,请等一小会");
		mDialogFragment
		.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 下载网络数据
				refreshTask();
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
	
	public void refreshTask() {
		AbLogUtil.prepareLog(PullToRefreshListActivity.class);
		AbTask mAbTask = AbTask.newInstance();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			@Override
			public List<?> getList() {
				List<Map<String, Object>> newList = null;
				try {
					Thread.sleep(1000);
					currentPage = 1;
					newList = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;

					for (int i = 0; i < pageSize; i++) {
						map = new HashMap<String, Object>();
						map.put("itemsIcon", mPhotoList.get(i));
						map.put("itemsTitle", "item" + (i + 1));
						map.put("itemsText", "item..." + (i + 1));
						newList.add(map);

					}
				} catch (Exception e) {
				}
				return newList;
			}

			@Override
			public void update(List<?> paramList) {
				
				//通知Dialog
				mDialogFragment.loadFinish();
				AbLogUtil.d(PullToRefreshListActivity.class, "返回", true);
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
				list.clear();
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullToRefreshView.onHeaderRefreshFinish();
			}

		});

		mAbTask.execute(item);
	}

	public void loadMoreTask() {
		AbTask mAbTask = AbTask.newInstance();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {

			@Override
			public void update(List<?> paramList) {
				List<Map<String, Object>> newList = (List<Map<String, Object>>) paramList;
				if (newList != null && newList.size() > 0) {
					list.addAll(newList);
					myListViewAdapter.notifyDataSetChanged();
					newList.clear();
				}
				mAbPullToRefreshView.onFooterLoadFinish();

			}

			@Override
			public List<?> getList() {
				List<Map<String, Object>> newList = null;
				try {
					currentPage++;
					Thread.sleep(1000);
					newList = new ArrayList<Map<String, Object>>();
					Map<String, Object> map = null;

					for (int i = 0; i < pageSize; i++) {
						map = new HashMap<String, Object>();
						map.put("itemsIcon", mPhotoList.get(i));
						map.put("itemsTitle", "item上拉"
								+ ((currentPage - 1) * pageSize + (i + 1)));
						map.put("itemsText", "item上拉..."
								+ ((currentPage - 1) * pageSize + (i + 1)));
						if ((list.size() + newList.size()) < total) {
							newList.add(map);
						}
					}

				} catch (Exception e) {
					currentPage--;
					newList.clear();
					AbToastUtil.showToastInThread(
							PullToRefreshListActivity.this, e.getMessage());
				}
				return newList;
			};
		});

		mAbTask.execute(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}


}
