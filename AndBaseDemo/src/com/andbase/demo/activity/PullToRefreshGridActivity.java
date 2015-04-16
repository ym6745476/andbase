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
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewUtil;
import com.ab.view.pullview.AbPullToRefreshView;
import com.ab.view.pullview.AbPullToRefreshView.OnFooterLoadListener;
import com.ab.view.pullview.AbPullToRefreshView.OnHeaderRefreshListener;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageGridAdapter;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.andbase.model.User;

public class PullToRefreshGridActivity extends AbActivity implements OnHeaderRefreshListener,OnFooterLoadListener{
	
	private int currentPage = 1;
	private MyApplication application;
	private ArrayList<User> mUserList = null;
	private ArrayList<User> mNewUserList = null;
	private AbPullToRefreshView mAbPullToRefreshView;
	private GridView mGridView = null;
	private ImageGridAdapter myGridViewAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
	private int total = 250;
	private int pageSize = 28;
	private AbLoadDialogFragment  mDialogFragment = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setAbContentView(R.layout.pull_to_refresh_grid);
	    
	    AbTitleBar mAbTitleBar = this.getTitleBar();
	    mAbTitleBar.setTitleText(R.string.pull_list_name);
	    mAbTitleBar.setLogo(R.drawable.button_selector_back);
	    mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
	    mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
	    mAbTitleBar.setLogoLine(R.drawable.line);
        
	    
	    for (int i = 0; i < 23; i++) {
        	mPhotoList.add(Constant.BASEURL+"content/templates/amsoft/images/rand/"+i+".jpg");
		}
	    
		application = (MyApplication) this.getApplication();
		//获取ListView对象
        mAbPullToRefreshView = (AbPullToRefreshView)this.findViewById(R.id.mPullRefreshView);
        mGridView = (GridView)this.findViewById(R.id.mGridView);
        
        //设置监听器
        mAbPullToRefreshView.setOnHeaderRefreshListener(this);
        mAbPullToRefreshView.setOnFooterLoadListener(this);
        
        //设置进度条的样式
        mAbPullToRefreshView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        mAbPullToRefreshView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular));
        //mAbPullListView.getHeaderView().setHeaderProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
        //mAbPullListView.getFooterView().setFooterProgressBarDrawable(this.getResources().getDrawable(R.drawable.progress_circular2));
		
		mGridView.setColumnWidth(AbViewUtil.scaleValue(this, 200));
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
		mGridView.setPadding(0, 0, 0, 0);
		mGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		mGridView.setVerticalSpacing(20);
		// ListView数据
		mUserList = new ArrayList<User>();
		// 使用自定义的Adapter
		myGridViewAdapter = new ImageGridAdapter(this, mUserList,
				R.layout.item_grid, new String[] { "itemsIcon" },
				new int[] { R.id.itemsIcon });
		mGridView.setAdapter(myGridViewAdapter);
		
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
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				AbToastUtil.showToast(PullToRefreshGridActivity.this,""+position);
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
    
    public void refreshTask(){
    	AbTask mAbTask = AbTask.newInstance();
        //定义两种查询的事件
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

            @Override
            public void update() {
            	AbDialogUtil.removeDialog(PullToRefreshGridActivity.this);
                mUserList.clear();
                if(mNewUserList!=null && mNewUserList.size()>0){
                    mUserList.addAll(mNewUserList);
                    myGridViewAdapter.notifyDataSetChanged();
                    mNewUserList.clear();
                }
                mAbPullToRefreshView.onHeaderRefreshFinish();
            }

            @Override
            public void get() {
                try {
                    currentPage = 1;
                    Thread.sleep(1000);
                    mNewUserList =  new ArrayList<User>() ;
                    
                    for (int i = 0; i < pageSize; i++) {
                        final User mUser = new User();
                        if(i>=mPhotoList.size()){
                            mUser.setHeadUrl(mPhotoList.get(mPhotoList.size()-1));
                        }else{
                            mUser.setHeadUrl(mPhotoList.get(i));
                        }
                        
                        mNewUserList.add(mUser);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AbToastUtil.showToastInThread(PullToRefreshGridActivity.this,e.getMessage());
                }
          };
        });
        
        mAbTask.execute(item);
    }
    
    public void loadMoreTask(){
    	AbTask mAbTask = AbTask.newInstance();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskListener() {

            @Override
            public void update() {
                if(mNewUserList!=null && mNewUserList.size()>0){
                    mUserList.addAll(mNewUserList);
                    myGridViewAdapter.notifyDataSetChanged();
                    mNewUserList.clear();
                }
                mAbPullToRefreshView.onFooterLoadFinish();
            }

            @Override
            public void get() {
                try {
                    currentPage++;
                    Thread.sleep(1000);
                    mNewUserList =  new ArrayList<User>() ;
                    for (int i = 0; i < pageSize; i++) {
                        final User mUser = new User();
                        mUser.setHeadUrl(mPhotoList.get(new Random().nextInt(mPhotoList.size())));
                        if((mUserList.size()+mNewUserList.size()) < total){
                            mNewUserList.add(mUser);
                        }
                        
                    }
                } catch (Exception e) {
                    currentPage--;
                    mNewUserList.clear();
                    AbToastUtil.showToastInThread(PullToRefreshGridActivity.this,e.getMessage());
                }
               
          };
        });
        
        mAbTask.execute(item);
    }


}
