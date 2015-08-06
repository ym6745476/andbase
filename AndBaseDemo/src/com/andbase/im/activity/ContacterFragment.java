
package com.andbase.im.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;

import com.andbase.R;
import com.andbase.global.MyApplication;
import com.andbase.im.adapter.ContacterExpandableListAdapter;
import com.andbase.im.model.IMRosterGroup;
import com.andbase.model.User;

/**
 * 联系人列表界面
 *
 */
public class ContacterFragment extends Fragment {
	
	private MyApplication application;
	private ContacterActivity mActivity = null;
	private ExpandableListView mListView;
	private List<String> mGroupNames;
	private List<IMRosterGroup> mIMRosterGroups;
	private User mUser = null;
	private ContacterExpandableListAdapter mContacterListAdapter = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 mActivity = (ContacterActivity)this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.im_contact_list, null);
		 mListView = (ExpandableListView) view.findViewById(R.id.contact_list);
		 
		 mGroupNames = new ArrayList<String>();
		 mIMRosterGroups = new ArrayList<IMRosterGroup>();
		 
		 mContacterListAdapter = new ContacterExpandableListAdapter(mActivity, mIMRosterGroups);
		 mListView.setAdapter(mContacterListAdapter);
			
		 mListView.setOnChildClickListener(new OnChildClickListener() {

				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
					IMRosterGroup  mIMRosterGroup  = mIMRosterGroups.get(groupPosition);
					List<User> mUsers = mIMRosterGroup.getUsers();
					User user = mUsers.get(childPosition);
					toChat(user.getUserName());
					return true;
				}
		});
		 
		initContacter();
		 
		return view;
	} 
	
	
	@Override
	public void onStart() {
		super.onStart();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void initContacter(){
		mGroupNames.clear();
		mIMRosterGroups.clear();
		//增加分组
		try {
			List<String> mGroupNames = new ArrayList<String>();
			mGroupNames.add("我的好友");
			List<User> users = new ArrayList<User>();
			User user = new User();
			user.setUserName("客服1");
			users.add(user);
			IMRosterGroup group = new IMRosterGroup("我的好友",users);
			mIMRosterGroups.add(group);
		} catch (Exception e) {
		    e.printStackTrace();
			mGroupNames.clear();
			mIMRosterGroups.clear();
		}
		mContacterListAdapter.notifyDataSetChanged();
		if(mGroupNames.size()>0){
			mListView.expandGroup(0);
		}
		
	}
	
	public void toChat(String userName){
	    //进入会话窗口
        Intent chatIntent = new Intent(mActivity,ChatActivity.class);
        chatIntent.putExtra("USERNAME", userName);
        startActivity(chatIntent);
    }
	
	

}

