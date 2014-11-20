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
import com.ab.util.AbDialogUtil;
import com.ab.view.sliding.AbSlidingPlayView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.im.activity.ChatActivity;

public class FriendActivity extends AbActivity {
	
	private AbSlidingPlayView mSlidingPlayView = null;
	private static final int pageSize = 48;
	private FriendDao mFriendDao = null;
   
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
        this.setTitleBarOverlay(true);
        mSlidingPlayView = (AbSlidingPlayView)findViewById(R.id.mAbSlidingPlayView);
        
		mSlidingPlayView.setNavHorizontalGravity(Gravity.CENTER_HORIZONTAL);
		mSlidingPlayView.setNavLayoutBackground(R.drawable.page_layout_bg);
		
		mFriendDao = new FriendDao(this);
		
  	    AbDialogUtil.showProgressDialog(this,R.drawable.progress_circular,"正在查询好友...");
  	    
  	    showFriend();
  	    
  	  /*List<Friend> friends = new ArrayList<Friend>();
       for(int i=0;i<userList.size();i++){
    	   FrontiaUser.FrontiaUserDetail remoteUser = userList.get(i) ;
    	   //保存到本地
    	   Friend friend = new Friend();
    	   friend.setuId(remoteUser.getId());
    	   friend.setName(remoteUser.getName());
    	   friend.setPhotoUrl(remoteUser.getHeadUrl());
    	   friend.setSex(remoteUser.getSex().name());
    	   friend.setAccessToken(remoteUser.getAccessToken());
	       friends.add(friend);
       }
       
       mFriendDao.startWritableDatabase(true);
       mFriendDao.deleteAll();
      mFriendDao.insertList(friends);
      mFriendDao.closeDatabase(true);
      
      showFriend();*/
        
        
    }
    
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void onPause() {
		super.onPause();

	}
   
	public void showFriend(){
		   //显示本地
	  	   mFriendDao.startReadableDatabase();
	  	   final List<Friend> friends = mFriendDao.queryList();
	  	   mFriendDao.closeDatabase();
	  	   if(friends==null || friends.size()==0){
	  	    	return;
	  	   }
	       int pageCount = 1;
	       if(friends.size()<=pageSize){
	        	pageCount = 1;
	       }else{
	    	   pageCount = friends.size()/pageSize;
	 	       if(friends.size()%pageSize>0){
	 	    	   pageCount = pageCount+1;
	 	       }
	       }
	       mSlidingPlayView.removeAllViews();
	       for (int i=0; i<pageCount; i++) {
	    	    final int p = i;
	        	GridView appPage = new GridView(FriendActivity.this);
	        	appPage.setAdapter(new FriendAdapter(FriendActivity.this, friends, i));
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
						
						Friend  mFriend = friends.get(p*pageSize+arg2);
						//跳转到聊天界面
						//朋友圈
						Intent intent = new Intent(FriendActivity.this,ChatActivity.class);
						intent.putExtra("ID", mFriend.getuId());
						intent.putExtra("NAME", mFriend.getName());
						intent.putExtra("HEADURL", mFriend.getPhotoUrl());
						startActivity(intent);
					}
	        		
				});
	       }
	}

}


