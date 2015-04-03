package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.ab.activity.AbActivity;
import com.ab.task.thread.AbTaskPool;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.MyExpandableListAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.andbase.util.download.DownFile;
import com.andbase.util.download.DownFileDao;

public class DownListActivity extends AbActivity{
	
	private MyApplication application;
	private DownFileDao mDownFileDao = null;
	private ArrayList<DownFile> mDownFileList1 = null;
	private ArrayList<DownFile> mDownFileList2 = null;
	private ArrayList<ArrayList<DownFile>> mGroupDownFileList = null;
	private MyExpandableListAdapter mExpandableListAdapter = null;
	private AbTaskPool mAbTaskPool = null;
    
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.down_list);
		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.down_list);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        mDownFileDao = DownFileDao.getInstance(this);
        mDownFileList1 = new ArrayList<DownFile>();
        mDownFileList2 = new ArrayList<DownFile>();
        mGroupDownFileList = new ArrayList<ArrayList<DownFile>>();
        mGroupDownFileList.add(mDownFileList1);
        mGroupDownFileList.add(mDownFileList2);
        
        mAbTaskPool = AbTaskPool.getInstance();
        
        String[] mDownFileGroupTitle = new String[]{this.getResources().getString(R.string.download_complete_title),this.getResources().getString(R.string.undownLoad_title)};
        
        //创建一个BaseExpandableListAdapter对象
      	mExpandableListAdapter = new MyExpandableListAdapter(this,mGroupDownFileList,mDownFileGroupTitle);
      	ExpandableListView mExpandListView = (ExpandableListView)findViewById(R.id.mExpandableListView);
      	mExpandListView.setAdapter(mExpandableListAdapter);
        //Indicator靠右
        int width = getWindowManager().getDefaultDisplay().getWidth();
        mExpandListView.setIndicatorBounds(width-40, width-25);
        mExpandListView.setChildIndicatorBounds(5, 53);
        
        initDownFileList();
     }

	
	
	/**
	 * 初始化所有文件
	 */
	private void initDownFileList() {
		

		List<DownFile> mDownFileList = new ArrayList<DownFile>();
		
		DownFile mDownFile1 = new DownFile();
		mDownFile1.setName("愤怒的小鸟");
		mDownFile1.setDescription("以星球大战电影前传为背景");
		mDownFile1.setPakageName("");
		mDownFile1.setState(Constant.undownLoad);
		mDownFile1.setIcon(String.valueOf(R.drawable.default_pic));
		mDownFile1.setDownUrl("http://down.apk.hiapk.com/down?aid=1832508&em=13");
		mDownFile1.setSuffix(".apk");
		mDownFileList.add(mDownFile1); 
		
		DownFile mDownFile2 = new DownFile();
		mDownFile2.setName("节奏大师");
		mDownFile2.setPakageName("");
		mDownFile2.setDescription("一款老少皆宜的绿色音乐游戏");
		mDownFile2.setState(Constant.undownLoad);
		mDownFile2.setIcon(String.valueOf(R.drawable.default_pic));
		mDownFile2.setDownUrl("http://down.mumayi.com/292416/mbaidu");
		mDownFile2.setSuffix(".apk");
		mDownFileList.add(mDownFile2); 
		
		DownFile mDownFile3 = new DownFile();
		mDownFile3.setName("天天酷跑");
		mDownFile3.setPakageName("");
		mDownFile3.setDescription("腾讯移动游戏平台首批产品");
		mDownFile3.setState(Constant.undownLoad);
		mDownFile3.setIcon(String.valueOf(R.drawable.default_pic));
		mDownFile3.setDownUrl("http://down.mumayi.com/407098/mbaidu");
		mDownFile3.setSuffix(".apk");
		mDownFileList.add(mDownFile3); 
		
		//测试
		//mDownFileDao.delete("http://down.apk.hiapk.com/down?aid=1832508&em=13");
		//mDownFileDao.delete("http://down.mumayi.com/292416/mbaidu");
		//mDownFileDao.delete("http://down.mumayi.com/407098/mbaidu");
		
		//初始化文件已经下载的长度，计算已下载的进度
		for(DownFile mDownFile:mDownFileList){
			  //本地数据
			  DownFile mDownFileT = mDownFileDao.getDownFile(mDownFile.getDownUrl());
	          if(mDownFileT != null){
	        	  mDownFile = mDownFileT;
	        	  if(mDownFile.getDownLength() == mDownFile.getTotalLength() && mDownFile.getTotalLength()!=0){
	    	    	  mDownFile.setState(Constant.downloadComplete);
	    	    	  mDownFileList1.add(mDownFile);
	    	    	  mExpandableListAdapter.notifyDataSetChanged();
				  }else{
					  //显示为暂停状态
		        	  mDownFile.setState(Constant.downLoadPause);
	        	      mDownFileList2.add(mDownFile);
	        	      mExpandableListAdapter.notifyDataSetChanged();
				  }
	          }else{
	        	    mDownFile.setState(Constant.undownLoad);
	        	    mDownFileList2.add(mDownFile);
	        	    mExpandableListAdapter.notifyDataSetChanged();
	          }
	    }
	}
	
	
	@Override
	public void finish() {
		super.finish();
		
		//释放所有的下载线程
		mExpandableListAdapter.releaseThread();

		
	}
	

}
