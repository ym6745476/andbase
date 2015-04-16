package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListListener;
import com.ab.util.AbCharacterParser;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.sample.AbLetterFilterListView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.CityListAdapter;
import com.andbase.demo.model.City;
import com.andbase.global.MyApplication;

public class CityListActivity extends AbActivity{

	private MyApplication application;
	private List<City> list = null;
	private ListView mListView = null;
	private EditText mSearchEditText = null;
	private AbTitleBar mAbTitleBar = null;
	private CityListAdapter mCityListAdapter = null;
	private AbLoadDialogFragment  mDialogFragment = null;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.city_list);
		application = (MyApplication) abApplication;

		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.city_list_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		// 获取ListView对象
		View headerView = LayoutInflater.from(this).inflate(R.layout.city_header, null);
		mListView = (ListView) this.findViewById(R.id.listView);
		mListView.addHeaderView(headerView);
		
		AbLetterFilterListView letterView = (AbLetterFilterListView)this.findViewById(R.id.letterView);
		
		mSearchEditText = (EditText) this.findViewById(R.id.editText);
		
		// ListView数据
		list = new ArrayList<City>();

		// 使用自定义的Adapter
		mCityListAdapter = new CityListAdapter(this, list);
		mListView.setAdapter(mCityListAdapter);

		// item被点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				AbToastUtil.showToast(CityListActivity.this, "点击"+position);
			}
		});

		//显示进度框
		mDialogFragment = AbDialogUtil.showLoadDialog(this, R.drawable.ic_load, "查询中,请等一小会");
		mDialogFragment
		.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 下载网络数据
				downTask();
			}

		});
		
		mSearchEditText.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = mSearchEditText.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					filterData(str);
				} else {
					downTask();
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {

			}
		});

	}

	
	public void downTask() {
		AbLogUtil.prepareLog(CityListActivity.class);
		AbTask mAbTask = AbTask.newInstance();
		final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListListener() {
			@Override
			public List<?> getList() {
				List<City> newList = null;
				try {
					newList = filledData(getResources().getStringArray(R.array.list));
				} catch (Exception e) {
				}
				return newList;
			}

			@Override
			public void update(List<?> paramList) {
				list.clear();
				list.addAll((List<City>)paramList);
				//通知Dialog
				mDialogFragment.loadFinish();
				mCityListAdapter.notifyDataSetChanged();
			}

		});

		mAbTask.execute(item);
	}
	
	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<City> filledData(String [] array){
		List<City> newList = new ArrayList<City>();
		//实例化汉字转拼音类
		AbCharacterParser	characterParser = AbCharacterParser.getInstance();
		
		for(int i=0; i<array.length; i++){
			City city = new City();
			city.setName(array[i]);
			//汉字转换成拼音
			String pinyin = characterParser.getSelling(array[i]);
			String sortString = pinyin.substring(0, 1).toUpperCase();
			
			// 正则表达式，判断首字母是否是英文字母
			if(sortString.matches("[A-Z]")){
				city.setFirstLetter(sortString.toUpperCase());
			}else{
				city.setFirstLetter("#");
			}
			newList.add(city);
		}
		Collections.sort(newList);
		return newList;
		
	}
	
	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * @param filterStr
	 */
	private void filterData(String filterStr){
		//实例化汉字转拼音类
		AbCharacterParser characterParser = AbCharacterParser.getInstance();
		List<City> filterDateList = new ArrayList<City>();
		if(!TextUtils.isEmpty(filterStr)){
			for(City city : list){
				String name = city.getName();
				if(name.indexOf(filterStr) != -1 || characterParser.getSelling(name).startsWith(filterStr)){
					filterDateList.add(city);
				}
			}
		}
		
		// 根据a-z进行排序
		Collections.sort(filterDateList);
		mCityListAdapter.updateListView(filterDateList);
	}

	

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}


}
