package com.andbase.main;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ab.cache.image.AbImageBaseCache;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.image.AbImageLoader;
import com.ab.model.AbMenuItem;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbAnimationUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbToastUtil;
import com.andbase.R;
import com.andbase.demo.activity.DemoMainActivity;
import com.andbase.global.MyApplication;
import com.andbase.im.activity.ContacterActivity;
import com.andbase.im.activity.MessageActivity;
import com.andbase.login.AboutActivity;
import com.andbase.model.User;

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
	private AbImageLoader mAbImageLoader = null;
	private RelativeLayout loginLayout = null;
	private User mUser = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mActivity = (MainActivity) this.getActivity();
		application = (MyApplication) mActivity.getApplication();

		View view = inflater.inflate(R.layout.main_menu, null);
		mMenuListView = (ExpandableListView) view.findViewById(R.id.menu_list);

		mNameText = (TextView) view.findViewById(R.id.user_name);
		mUserPhoto = (ImageView) view.findViewById(R.id.user_photo);
		mUserPoint = (TextView) view.findViewById(R.id.user_point);
		sunshineView = (ImageView) view.findViewById(R.id.sunshineView);
		loginLayout = (RelativeLayout) view.findViewById(R.id.login_layout);
		Button cacheClearBtn = (Button) view.findViewById(R.id.cacheClearBtn);

		cacheClearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AbDialogUtil.showProgressDialog(mActivity,0, "正在清空缓存...");
				AbTask task = AbTask.newInstance();
				// 定义异步执行的对象
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(mActivity);
						AbToastUtil.showToast(mActivity, "缓存已清空完成");
					}

					@Override
					public void get() {
						try {
							AbFileUtil.clearDownloadFile();
							AbImageBaseCache.getInstance().clearBitmap();
						} catch (Exception e) {
							AbToastUtil.showToastInThread(mActivity,
									e.getMessage());
						}
					};
				});
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
					mOnChangeViewListener.onChangeView(groupPosition,
							childPosition);
				}
				return true;
			}
		});

		// 图片的下载
		mAbImageLoader = new AbImageLoader(mActivity);

		initMenu();

		AbAnimationUtil.playRotateAnimation(sunshineView, 2000, 5,
				Animation.RESTART);

		return view;
	}

	public interface OnChangeViewListener {
		public abstract void onChangeView(int groupPosition, int childPosition);
	}

	public void setOnChangeViewListener(
			OnChangeViewListener onChangeViewListener) {
		mOnChangeViewListener = onChangeViewListener;
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void initMenu() {
		mGroupName.clear();
		mChild1.clear();
		mChild2.clear();

		mGroupName.add("常用");
		mGroupName.add("操作");

		AbMenuItem m0 = new AbMenuItem();
		m0.setIconId(R.drawable.square);
		m0.setText("联系人");
		mChild1.add(m0);

		AbMenuItem m1 = new AbMenuItem();
		m1.setIconId(R.drawable.square);
		m1.setText("我的消息");
		mChild1.add(m1);

		AbMenuItem m3 = new AbMenuItem();
		m3.setIconId(R.drawable.share);
		m3.setText("程序案例");
		mChild1.add(m3);

		AbMenuItem m4 = new AbMenuItem();
		m4.setIconId(R.drawable.app);
		m4.setText("应用游戏");
		mChild1.add(m4);

		AbMenuItem m5 = new AbMenuItem();
		m5.setIconId(R.drawable.set);
		m5.setText("支持我");
		mChild2.add(m5);

		AbMenuItem m6 = new AbMenuItem();
		m6.setIconId(R.drawable.recommend);
		m6.setText("推荐给好友");
		mChild2.add(m6);

		mUser = application.mUser;
		if (application.isLogin) {
			AbMenuItem m7 = new AbMenuItem();
			m7.setIconId(R.drawable.quit);
			m7.setText("注销");
			mChild2.add(m7);
		}

		AbMenuItem m8 = new AbMenuItem();
		m8.setIconId(R.drawable.about);
		m8.setText("关于");
		mChild2.add(m8);
		mAdapter.notifyDataSetChanged();
		for (int i = 0; i < mGroupName.size(); i++) {
			mMenuListView.expandGroup(i);
		}

		if (!application.isLogin) {
			setNameText("登录");
			setUserPhoto(R.drawable.photo01);
			setUserPoint("0");
			mNameText.setCompoundDrawables(null, null, null, null);
			loginLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mActivity.toLogin(mActivity.LOGIN_CODE);
				}
			});
		} else {
			setNameText(mUser.getUserName());
			downSetPhoto(mUser.getHeadUrl());
			if ("MAN".equals(mUser.getSex())) {
				Drawable d = mActivity.getResources().getDrawable(
						R.drawable.user_info_male);
				d.setBounds(0, 0, 26, 26);
				mNameText.setCompoundDrawables(null, null, d, null);
			} else if ("WOMAN".equals(mUser.getSex())) {
				Drawable d = mActivity.getResources().getDrawable(
						R.drawable.user_info_female);
				d.setBounds(0, 0, 26, 26);
				mNameText.setCompoundDrawables(null, null, d, null);
			} else {
				mNameText.setCompoundDrawables(null, null, null, null);
			}

			setUserPoint(String.valueOf(mUser.getPoint()));
			loginLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

				}
			});
		}
		final String shareStr = this.getResources().getString(
				R.string.share_desc);
		setOnChangeViewListener(new OnChangeViewListener() {

			@Override
			public void onChangeView(int groupPosition, int childPosition) {
				if (groupPosition == 0) {
					if (childPosition == 0) {
						// 联系人
						if (!application.isLogin) {
							mActivity.toLogin(mActivity.FRIEND_CODE);
						} else {
							Intent intent = new Intent(mActivity,
									ContacterActivity.class);
							mActivity.startActivity(intent);
						}
					} else if (childPosition == 1) {
						// 我的消息
						Intent intent = new Intent(mActivity,
								MessageActivity.class);
						startActivity(intent);
					} else if (childPosition == 2) {
						// 程序案例
						Intent intent = new Intent(mActivity,
								DemoMainActivity.class);
						startActivity(intent);
					} else if (childPosition == 3) {
						// 应用游戏
						mActivity.showApp();
					}
				} else if (groupPosition == 1) {
					if (childPosition == 0) {
						// 选项、赞助作者
						mActivity.showApp();
					} else if (childPosition == 1) {
						// 推荐

					} else if (childPosition == 2) {
						if (application.isLogin) {
							AbDialogUtil.showAlertDialog(mActivity, "注销",
									"确定要注销该用户吗?",
									new AbDialogOnClickListener() {

										@Override
										public void onPositiveClick() {
											// 注销
											application.clearLoginParams();
											initMenu();
											mActivity.stopIMService();
										}

										@Override
										public void onNegativeClick() {
											// TODO Auto-generated method stub

										}

									});

						} else {
							// 关于
							Intent intent = new Intent(mActivity,
									AboutActivity.class);
							startActivity(intent);
						}
					} else if (childPosition == 3) {
						if (application.isLogin) {
							// 关于
							Intent intent = new Intent(mActivity,
									AboutActivity.class);
							startActivity(intent);
						} else {
							// 无
						}
					}
				}
			}

		});

	}

	/**
	 * 描述：用户名的设置
	 * 
	 * @param mNameText
	 */
	public void setNameText(String mNameText) {
		this.mNameText.setText(mNameText);
	}

	/**
	 * 描述：设置用户阳光
	 * 
	 * @param mPoint
	 */
	public void setUserPoint(String mPoint) {
		this.mUserPoint.setText(mPoint);
		AbAnimationUtil.playRotateAnimation(sunshineView, 2000, 5,
				Animation.RESTART);
	}

	public void downSetPhoto(String mPhotoUrl) {
		// 缩放图片的下载
		mAbImageLoader.display(mUserPhoto, mPhotoUrl,150,150);
	}

	/**
	 * 描述：设置头像
	 * 
	 * @param drawable
	 */
	public void setUserPhoto(int resId) {
		this.mUserPhoto.setImageResource(resId);
	}

}
