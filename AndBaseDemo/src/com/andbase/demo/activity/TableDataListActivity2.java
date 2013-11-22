package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskPool;
import com.ab.view.listener.AbOnItemClickListener;
import com.ab.view.table.AbCellType;
import com.ab.view.table.AbTable;
import com.ab.view.table.AbTableArrayAdapter;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.model.Stock;
import com.andbase.global.MyApplication;


public class TableDataListActivity2 extends AbActivity {
	
	private MyApplication application;
	/////////////////////////////////////////
	//表格内容数据源
	private List<String[]> contents;
	//表格标题数据源
	private String[] titles = null;
	private AbTable table = null;
	private ListView mListView = null;
	private int[] cellTypes = null;
	private int[] cellWidth = null;
	private int[] rowHeight = null;
	// (6)行文字大小（索引0标题，1内容列表）
	private int[] rowTextSize = null;
	// (7)行文字颜色（索引0标题，1内容列表）
	private int[] rowTextColor = null;
	// (8)背景资源
	private int[] tableResource = null;
	
	// 表格的Adapter
	private AbTableArrayAdapter tableAdapter;
	
	///////////////////////////////////////////////
	
	private View noView = null;
	private ArrayList<Stock> mStockList = null;
	private com.ab.task.AbTaskPool mAbTaskPool = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		application = (MyApplication)abApplication;
		setAbContentView(mInflater.inflate(R.layout.table_data_list, null));
        
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.table_list_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
        
		noView = LayoutInflater.from(this).inflate(R.layout.no_data, null);
		
		mAbTaskPool = AbTaskPool.getInstance();
		
		// (1)标题配置  如果checkbox，值为0表示默认不选中，1表示选中
		titles = new String[] { "标题1", "标题2", "标题3","图标","0"};
		// (2)内容列表配置
		contents = new ArrayList<String[]>();
		// (3)列类型配置
		cellTypes = new int[] { AbCellType.STRING, AbCellType.STRING, AbCellType.STRING, AbCellType.IMAGE,AbCellType.CHECKBOX};
		// (4)列宽配置(%) 超过100% 可以横向滑动
		cellWidth = new int[] {25,25,25,25,25};
		// (5)行高（索引0：标题高，1：内容列表高）
		rowHeight = new int[] { 90, 80 };
		// (6)行文字大小（索引0标题，1内容列表）
		rowTextSize = new int[] { 18, 15};
		// (7)行文字颜色（索引0标题，1内容列表）
		rowTextColor = new int[] {Color.rgb(255, 255, 255),Color.rgb(113, 113, 113) };
		// (8)背景资源
		tableResource = new int[] {android.R.color.transparent,R.drawable.title_cell,android.R.color.transparent,R.drawable.content_cell};
		// (9)表格实体
		table = AbTable.newAbTable(this,5); 
		table.setTitles(titles);
		table.setContents(contents);
		table.setCellTypes(cellTypes);
		table.setCellWidth(cellWidth);
		table.setRowHeight(rowHeight);
		table.setRowTextSize(rowTextSize);
		table.setTableResource(tableResource);
		table.setRowTextColor(rowTextColor);
		//点击事件
		AbOnItemClickListener mAbOnItemClickListener = new AbOnItemClickListener(){

			@Override
			public void onClick(int position) {
				showToast("点击了第"+position+"行的图标");
			}
			
		};
		
		//选择事件
		AbOnItemClickListener mAbOnItemCheckListener = new AbOnItemClickListener(){

			@Override
			public void onClick(int position) {
				showToast("点击了第"+position+"行的复选框");
				// 计算是有多少被选中的
				String stateStr = "";
				for (int j = 0; j < tableAdapter.getCount()-1; j++) {
					String oldState1 = table.getContents().get(j)[4];
					if(j==0){
						stateStr = oldState1;
					}else{
						stateStr += ","+oldState1;
					}
				}
				showToast("选择情况："+stateStr);
			}
			
		};
		table.setItemCellTouchListener(mAbOnItemClickListener);
		table.setItemCellCheckListener(mAbOnItemCheckListener);
		
		// (10)TableAdapter对象
		tableAdapter = new AbTableArrayAdapter(TableDataListActivity2.this, table);
		// (12)ListView
		mListView = (ListView)findViewById(R.id.mListView);
		// (11)设置Adapter
		mListView.setAdapter(tableAdapter);
		// -------------- 表格控件-------end------------------
		// 点击事件
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				showToast("点击了list的第"+arg2+"行");
			}
		});
		
		loadData();
	}

	
	public void loadData(){
		
		//查询数据
		showProgressDialog();
		final AbTaskItem item = new AbTaskItem();
		item.listener = new AbTaskListener() {

			@Override
			public void update() {
				removeProgressDialog();
				
				if (mStockList != null && mStockList.size() > 0) {
					contents.clear();
					Stock mStock = null;
					for (int i = 0; i < mStockList.size(); i++) {
						mStock = mStockList.get(i);
						String[] data1 = new String[] {mStock.id,mStock.text1,mStock.text2,mStock.text3,mStock.text4};
						contents.add(data1);
					}
					tableAdapter.notifyDataSetChanged();
				} else {
					contentLayout.removeAllViews();
					contentLayout.addView(noView,layoutParamsFF);
				}
			}

			@Override
			public void get() {
				try {
					mStockList = new ArrayList<Stock> ();
					Stock mStock1 = null;
					for(int i=0;i<20;i++){
						mStock1 = new Stock();
						mStock1.setId(String.valueOf(i));
						mStock1.setText1("Text1");
						mStock1.setText2("Text2");
						mStock1.setText3(String.valueOf(R.drawable.logo_418));
						mStock1.setText4("0");
						mStock1.setText5("Text5");
						mStockList.add(mStock1);
					}
					
					
				} catch (Exception e) {
					e.printStackTrace();
					showToastInThread(e.getMessage());
				}
		  };
		};
		mAbTaskPool.execute(item);
		
	}
	
	
	
}
