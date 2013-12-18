
package com.andbase.main;

import java.util.ArrayList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.bitmap.AbImageCache;
import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.global.AbMenuItem;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbFileUtil;
import com.andbase.R;
import com.andbase.demo.activity.AboutActivity;
import com.andbase.demo.activity.DemoMainActivity;
import com.andbase.friend.FriendActivity;
import com.andbase.global.MyApplication;
import com.andbase.login.LoginActivity;
import com.andbase.model.User;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.api.FrontiaAuthorization.MediaType;
import com.baidu.frontia.api.FrontiaSocialShare;
import com.baidu.frontia.api.FrontiaSocialShare.FrontiaTheme;
import com.baidu.frontia.api.FrontiaSocialShareContent;
import com.baidu.frontia.api.FrontiaSocialShareListener;


public class MainMenuFragment extends Fragment {
	
	private MyApplication application;
	private MainActivity mActivity = null;
	private ExpandableListView mMenuListView;
	private ArrayList<String> mGroupName = null;
	private ArrayList<ArrayList<AbMenuItem>> mChilds = null;
	private ArrayList<AbMenuItem> mChild1 = null;
	private ArrayList<AbMenuItem> mChild2 = null;
	private LeftMenuAdapter mAdapter;
	private OnChangeViewListener mOnChangeViewListener;
	private TextView mNameText;
	private TextView mUserPoint;
	private ImageView mUserPhoto;
	private ImageView sunshineView;
	private AbImageDownloader mAbImageDownloader = null; 
	private RelativeLayout loginLayout = null; 
	private User mUser = null;
	private FrontiaSocialShareContent mImageContent = new FrontiaSocialShareContent();
	private FrontiaSocialShare mSocialShare;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 mActivity = (MainActivity)this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.main_menu, null);
		 mMenuListView = (ExpandableListView) view.findViewById(R.id.menu_list);

		 mNameText = (TextView) view.findViewById(R.id.user_name);
		 mUserPhoto = (ImageView) view.findViewById(R.id.user_photo);
		 mUserPoint = (TextView) view.findViewById(R.id.user_point);
		 sunshineView = (ImageView)view.findViewById(R.id.sunshineView);
		 loginLayout = (RelativeLayout)view.findViewById(R.id.login_layout);
		 Button cacheClearBtn = (Button)view.findViewById(R.id.cacheClearBtn);
		 
		 cacheClearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				mActivity.showProgressDialog("正在清空缓存...");
				AbTask task = new AbTask();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.listener = new AbTaskListener() {

					@Override
					public void update() {
						mActivity.removeProgressDialog();
						mActivity.showToast("缓存已清空完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	AbFileUtil.removeAllFileCache();
			   		    	AbImageCache.removeAllBitmapFromCache();
			   		    } catch (Exception e) {
			   		    	mActivity.showToastInThread(e.getMessage());
			   		    }
				  };
				};
		        task.execute(item);
				
				
			}
		});
		 
		 mGroupName = new ArrayList<String>();
		 mChild1 = new ArrayList<AbMenuItem>();
		 mChild2 = new ArrayList<AbMenuItem>();
			
		 ArrayList<ArrayList<AbMenuItem>> mChilds = new ArrayList<ArrayList<AbMenuItem>>();
		 mChilds.add(mChild1);
		 mChilds.add(mChild2);
		 
		 
		 mAdapter = new LeftMenuAdapter(mActivity, mGroupName, mChilds);
		 mMenuListView.setAdapter(mAdapter);
		 for (int i = 0; i < mGroupName.size(); i++) {
			mMenuListView.expandGroup(i);
		 }
		 
		 mMenuListView.setOnGroupClickListener(new OnGroupClickListener() {

				public boolean onGroupClick(ExpandableListView parent, View v,
						int groupPosition, long id) {
					return true;
				}
		 });
			
		 mMenuListView.setOnChildClickListener(new OnChildClickListener() {

				public boolean onChildClick(ExpandableListView parent, View v,
						int groupPosition, int childPosition, long id) {
						if (mOnChangeViewListener != null) {
							mOnChangeViewListener.onChangeView(groupPosition,childPosition);
						}
					return true;
				}
		});
		 
		initMenu();
		
        startAnimation(sunshineView);
		
        //图片的下载
        mAbImageDownloader = new AbImageDownloader(mActivity);
        mAbImageDownloader.setWidth(150);
        mAbImageDownloader.setHeight(150);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
		 
        mSocialShare = Frontia.getSocialShare();
		mSocialShare.setContext(mActivity);
		mSocialShare.setClientId(MediaType.WEIXIN.toString(), "wx329c742cb69b41b8");
		mImageContent.setTitle(mActivity.getResources().getString(R.string.app_name));
		mImageContent.setContent("Android开发宝，简单你的工作，改变编程方式，独一无二最全框架。相关问题请到www.418log.org留言");
		mImageContent.setLinkUrl("http://www.418log.org/");
		mImageContent.setImageUri(Uri.parse("http://apps.bdimg.com/developer/static/04171450/developer/images/icon/terminal_adapter.png"));
        
		return view;
	} 
	
	public interface OnChangeViewListener {
		public abstract void onChangeView(int groupPosition, int childPosition);
	}

	public void setOnChangeViewListener(OnChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}
	
	

	@Override
	public void onStart() {
		super.onStart();
	}



	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	public void initMenu(){
		mGroupName.clear();
		mChild1.clear();
		mChild2.clear();
		
		
		mGroupName.add("常用");
		mGroupName.add("操作");
		
		AbMenuItem m0 = new AbMenuItem();
		m0.setIconId(R.drawable.square);
		m0.setText("朋友圈");
		mChild1.add(m0);
		
		AbMenuItem m1 = new AbMenuItem();
		m1.setIconId(R.drawable.share);
		m1.setText("程序案例");
		mChild1.add(m1);
		
		AbMenuItem m2 = new AbMenuItem();
		m2.setIconId(R.drawable.app);
		m2.setText("应用游戏");
		mChild1.add(m2);
		
		AbMenuItem m3 = new AbMenuItem();
		m3.setIconId(R.drawable.set);
		m3.setText("精神赞助我");
		mChild2.add(m3);
		
		AbMenuItem m4 = new AbMenuItem();
		m4.setIconId(R.drawable.recommend);
		m4.setText("推荐给好友");
		mChild2.add(m4);
		
		mUser = application.mUser;
		if(mUser!=null){
			AbMenuItem m5 = new AbMenuItem();
			m5.setIconId(R.drawable.quit);
			m5.setText("注销");
			mChild2.add(m5);
		}
		
		AbMenuItem m6 = new AbMenuItem();
		m6.setIconId(R.drawable.about);
		m6.setText("关于");
		mChild2.add(m6);
		mAdapter.notifyDataSetChanged();
		for (int i = 0; i < mGroupName.size(); i++) {
			mMenuListView.expandGroup(i);
		}
		
		
		if(mUser==null){
			setNameText("登录");
			setUserPhoto(R.drawable.photo01);
			setUserPoint("0");
			mNameText.setCompoundDrawables(null, null,null, null);
			loginLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(application.mUser == null){
						((MainActivity)mActivity).loginAuthorization(0);
					}else{
						Intent intent = new Intent(mActivity,LoginActivity.class);
    					startActivity(intent);
					}
					
				}
			});
		}else{
			setNameText(mUser.getName());
			downSetPhoto(mUser.getPhotoUrl());
			if("MAN".equals(mUser.getSex())){
				Drawable d = mActivity.getResources().getDrawable(R.drawable.user_info_male);
				d.setBounds(0, 0, 26, 26);
				mNameText.setCompoundDrawables(null, null,d , null);
			}else if("WOMAN".equals(mUser.getSex())){
				Drawable d = mActivity.getResources().getDrawable(R.drawable.user_info_female);
				d.setBounds(0, 0, 26, 26);
				mNameText.setCompoundDrawables(null, null, d, null);
			}else{
				mNameText.setCompoundDrawables(null, null,null, null);
			}
			
			setUserPoint(String.valueOf(mUser.getPoint()));
            loginLayout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					
				}
			});
		}
		final String shareStr = this.getResources().getString(R.string.share_desc);
		setOnChangeViewListener(new OnChangeViewListener(){

			@Override
			public void onChangeView(int groupPosition, int childPosition) {
				if(groupPosition==0){
					if(childPosition==0){
						//朋友圈
						if(application.mUser == null){
							((MainActivity)mActivity).loginAuthorization(1);
						}else{
							Intent intent = new Intent(mActivity,FriendActivity.class);
    						startActivity(intent);
						}
					}else if(childPosition==1){
						//程序案例
						Intent intent = new Intent(mActivity,DemoMainActivity.class);
						startActivity(intent); 
					}else if(childPosition==2){
						//应用游戏
						mActivity.showApp();
					}
				}else if(groupPosition==1){
                    if(childPosition==0){
						//选项、赞助作者
                    	mActivity.showApp();
					}else if(childPosition==1){
						//推荐
						mSocialShare.show(mActivity.getWindow().getDecorView(), mImageContent, FrontiaTheme.LIGHT,  new ShareListener());
					}else if(childPosition==2){
						if(mUser!=null){
							mActivity.showDialog("注销", "确定要注销该用户吗?", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									//注销
									application.clearLoginParams();
									initMenu();
								}
							});
							
						}else{
							//关于
							Intent intent = new Intent(mActivity,AboutActivity.class); 
							startActivity(intent);
						}
					}else if(childPosition==3){
						if(application.mUser!=null){
							//关于
							Intent intent = new Intent(mActivity,AboutActivity.class); 
							startActivity(intent);
						}else{
							//无
						}
					}
				}
			}
			
		});
		
	}
	
	/**
	 * 描述：用户名的设置
	 * @param mNameText
	 */
	public void setNameText(String mNameText) {
		this.mNameText.setText(mNameText);
	}
	
	/**
	 * 描述：设置用户阳光
	 * @param mPoint
	 */
	public void setUserPoint(String mPoint) {
		this.mUserPoint.setText(mPoint);
		startAnimation(sunshineView);
	}
	
	public void downSetPhoto(String mPhotoUrl) {
		//缩放图片的下载
		mAbImageDownloader.setNoImage(R.drawable.photo01);
		mAbImageDownloader.setErrorImage(R.drawable.photo01_error);
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        mAbImageDownloader.display(mUserPhoto,mPhotoUrl);
	}
	
    public void startAnimation(ImageView v) {
    	
        //创建AnimationSet对象
        AnimationSet animationSet = new AnimationSet(true);
        //创建RotateAnimation对象
        RotateAnimation rotateAnimation = new RotateAnimation(0f,+360f, 
					Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画持续
        rotateAnimation.setDuration(2000);
        rotateAnimation.setRepeatCount(5);
        //rotateAnimation.setRepeatMode(Animation.RESTART);
        //动画插入器
        rotateAnimation.setInterpolator(mActivity, android.R.anim.decelerate_interpolator);
        //添加到AnimationSet
        animationSet.addAnimation(rotateAnimation);
        
        //开始动画 
        v.startAnimation(animationSet);
	}

    /**
     * 描述：设置头像
     * @param drawable
     */
	public void setUserPhoto(int resId) {
		this.mUserPhoto.setImageResource(resId);
	}
	
	private class ShareListener implements FrontiaSocialShareListener {

		@Override
		public void onSuccess() {
			Log.d("TAG","share success");
			mActivity.showToast("分享成功");
		}

		@Override
		public void onFailure(int errCode, String errMsg) {
			Log.d("TAG","share errCode "+errCode);
		}

		@Override
		public void onCancel() {
			Log.d("TAG","cancel ");
		}
		
	}


}

