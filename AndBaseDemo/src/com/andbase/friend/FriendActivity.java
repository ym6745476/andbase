package com.andbase.friend;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ab.activity.AbActivity;
import com.ab.global.AbConstant;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.baidu.frontia.FrontiaUser;
import com.baidu.frontia.FrontiaUser.FrontiaUserDetail;
import com.baidu.frontia.FrontiaUserQuery;

public class FriendActivity extends AbActivity {
	
	private AbSlidingPlayView mSlidingPlayView = null;
	private List<FrontiaUser.FrontiaUserDetail> mUserList = null;
	private static final int pageSize = 16;
   
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.friend);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.friend_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        this.setTitleBarAbove(true);
        mSlidingPlayView = (AbSlidingPlayView)findViewById(R.id.mAbSlidingPlayView);
        
		mSlidingPlayView.setPageLineHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		mSlidingPlayView.setPageLineLayoutBackground(R.drawable.page_layout_bg);
		showProgressDialog();
		
		//对用户的查询使用FrontiaUserQuery，是FrontiaQuery的子类。
        FrontiaUserQuery query = new FrontiaUserQuery();
        FrontiaUser.findUsers(query, new FrontiaUser.FetchUserListener() {
            @Override
            public void onSuccess(List<FrontiaUser.FrontiaUserDetail> userList) {
         	       removeProgressDialog();
         	       mUserList = userList;
         	       if(mUserList == null || mUserList.size()<0){
         	    	   return;
         	       }
         	       //showToast(""+mUserList.size());
         	       // the total pages
         	       int pageCount = 1;
         	       if(mUserList.size()<=pageSize){
         	        	pageCount = 1;
         	       }else{
         	    	   pageCount = mUserList.size()/pageSize;
            	       if(mUserList.size()%pageSize>0){
            	    	  pageCount = pageCount+1;
            	       }
         	       }
         	       for (int i=0; i<pageCount; i++) {
         	    	    final int p = i;
         	        	GridView appPage = new GridView(FriendActivity.this);
         	        	appPage.setAdapter(new FriendAdapter(FriendActivity.this, mUserList, i));
         	        	appPage.setVerticalSpacing(10);
         	        	appPage.setNumColumns(4);
         	        	appPage.setPadding(5, 5, 5, 5);
         	        	appPage.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
         	        	appPage.setVerticalSpacing(20);
         	        	
         	        	mSlidingPlayView.addView(appPage);
         	        	
         	        	appPage.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								
								FrontiaUserDetail  mFrontiaUserDetail = mUserList.get(p*pageSize+arg2);
								//跳转到聊天界面
								//朋友圈
								Intent intent = new Intent(FriendActivity.this,ChatActivity.class);
								intent.putExtra("ID", mFrontiaUserDetail.getId());
								intent.putExtra("NAME", mFrontiaUserDetail.getName());
								intent.putExtra("HEADURL", mFrontiaUserDetail.getHeadUrl());
								startActivity(intent);
							}
         	        		
						});
         	       }
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
         	   removeProgressDialog();
         	   showToast("errCode: " + errCode+ ", errMsg: " + errMsg);
            }
        });
        
        
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();

	}
   

}


