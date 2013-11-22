package com.andbase.friend;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInfoListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataOperationListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbDateUtil;
import com.ab.util.AbStrUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.andbase.model.User;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaPush;
import com.baidu.frontia.api.FrontiaPushListener.PushMessageListener;
import com.baidu.frontia.api.FrontiaPushMessageReceiver;
import com.baidu.frontia.api.FrontiaPushUtil;
import com.baidu.frontia.api.FrontiaPushUtil.NotificationContent;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ChatActivity extends AbActivity {
	
	private static final String TAG = "ChartActivity";
	private static final boolean D = Constant.DEBUG;
	
	private MyApplication application;
	private int pageSize = 10;
	private ChatMsgViewAdapter mChatMsgViewAdapter = null;
	private List<ChatMsg> mChatMsgList = null;
	private ListView mMsgListView = null;
	private EditText mContentEdit = null;
	
	private AbTitleBar mAbTitleBar = null;
	
	//发送选项面板
	private LinearLayout chatAppPanel = null;
	
	private User faceUser = null;
	
	private String faceUserPushChannelId = null;
	private String faceUserPushUserlId = null;
	
	//应用数据存储
	private FrontiaStorage mCloudStorage = null;
	
	//推送服务
	private FrontiaPush mPush = null;
	
	//推送内容
	private String mContentStr = null;
	
	//是否可以发送下一条
	private boolean isSendEnable  = true;
	
	private final String action = "com.baidu.android.pushservice.action.MESSAGE";
			
	//数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public ChatMsgDao mChatMsgDao = null;
	public UserDao mUserDao = null;
	
	//创建BroadcastReceiver
	private BroadcastReceiver mDataReceiver = new FrontiaPushMessageReceiver() {

		@Override
		public void onBind(Context context, int errorCode, String appid,
				String userId, String channelId, String requestId) {
			
		}
		
		@Override
		public void onMessage(Context context, String message,String customContentString) {
			Log.d(TAG, "收到了消息:" + message);
			if(message!=null && message.indexOf("content")!=-1){
				GsonBuilder builder = new GsonBuilder();
				Gson gson = builder.create();
				ChatMsg mChatMsg  = gson.fromJson(message, ChatMsg.class);
				mChatMsg.setSendState(2);
				mChatMsg.setCreateTime(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
				
				if(mChatMsg.getFaceUid().equals(application.mUser.getuId())){
					mChatMsgList.add(mChatMsg);
					mChatMsgViewAdapter.notifyDataSetChanged();
				}
				//由外部的接收器保存
				//saveData(mChatMsg);
				//saveUserData(mChatMsg.getUser());
			}
		}

		@Override
		public void onDelTags(Context arg0, int arg1, List<String> arg2,
				List<String> arg3, String arg4) {
		}

		@Override
		public void onListTags(Context arg0, int arg1, List<String> arg2,
				String arg3) {
		}

		@Override
		public void onNotificationClicked(Context arg0, String arg1,
				String arg2, String arg3) {
		}

		@Override
		public void onSetTags(Context arg0, int arg1, List<String> arg2,
				List<String> arg3, String arg4) {
		}

		@Override
		public void onUnbind(Context arg0, int arg1, String arg2) {
		}
		
		
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.chat);
		application = (MyApplication) abApplication;
		
		//数据存储初始化
      	Frontia.init(this.getApplicationContext(), Constant.APIKEY);
		mCloudStorage = Frontia.getStorage();
		mPush = Frontia.getPush();
		
		//初始化AbSqliteStorage
	    mAbSqliteStorage = AbSqliteStorage.getInstance(this);
	    
	    //初始化数据库操作实现类
	    mChatMsgDao = new ChatMsgDao(ChatActivity.this);
	    mUserDao  = new UserDao(ChatActivity.this);
		mAbTitleBar = this.getTitleBar();
		
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setTitleTextBold(false);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER,Gravity.CENTER);
		
		//获取传递的参数
		Intent mIntent = this.getIntent();
		String uId = mIntent.getStringExtra("ID");
		String name = mIntent.getStringExtra("NAME");
		String headUrl = mIntent.getStringExtra("HEADURL");
		
		faceUser = new User();
		faceUser.setuId(uId);
		faceUser.setName(name);
		faceUser.setPhotoUrl(headUrl);
		
		mAbTitleBar.setTitleText("与"+name+"的会话");
		
		
		mContentEdit = (EditText)findViewById(R.id.content);
		Button sendBtn = (Button)findViewById(R.id.sendBtn);
		
		sendBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(application.mUser == null){
					showToast("请先返回登录");
					return;
				}
				
				mContentStr = mContentEdit.getText().toString().trim();
				if(!AbStrUtil.isEmpty(mContentStr)){
					if(!isSendEnable){
						showToast("上一条正在发送，请稍等");
						return;
					}
					
					
					//发送通知
					if(faceUserPushChannelId!=null){
						isSendEnable = false;
						
						//清空文本框
	                    mContentEdit.setText("");
						
						//显示
						ChatMsg mChatMsg  = new ChatMsg();
						mChatMsg.setContent(mContentStr);
						mChatMsg.setSendState(0);
						mChatMsg.setCreateTime(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
						mChatMsg.setuId(application.mUser.getuId());
						mChatMsg.setFaceUid(faceUser.getuId());
						mChatMsg.setUser(application.mUser);
						
						mChatMsgList.add(mChatMsg);
						mChatMsgViewAdapter.notifyDataSetChanged();
						
						saveData(mChatMsg);
						
						GsonBuilder builder = new GsonBuilder();
						Gson gson = builder.create();
						
						if(mPush!=null && mPush.isPushWorking()){
					    	 Log.d("TAG", "发送通知");
					    	 //notificationToUser(mContentStr);
					    	 messageToUser(gson.toJson(mChatMsg));
					    }else{
					    	 Log.d("TAG", "推送服务未启动");
					    	 //启动推送服务
                             PushUtil.startPushService(mPush,ChatActivity.this);
					    }
						
						
					}else{
						showToast(faceUser.getName()+"[不能发送]");
					}
					
				}
			}
			
		});
		
		mMsgListView = (ListView)this.findViewById(R.id.mListView);
		mChatMsgList = new ArrayList<ChatMsg>();
		
		mChatMsgViewAdapter = new ChatMsgViewAdapter(this,mChatMsgList);
		mMsgListView.setAdapter(mChatMsgViewAdapter);
		
		//面板选项
	    chatAppPanel = (LinearLayout)this.findViewById(R.id.chatAppPanel);
	    ImageButton addBtn = (ImageButton)this.findViewById(R.id.addBtn);
	    addBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(chatAppPanel.getVisibility()==View.GONE){
					chatAppPanel.setVisibility(View.VISIBLE);
				}else{
					chatAppPanel.setVisibility(View.GONE);
				}
				
			}
		});
	    
	    
	    if(application.mUser != null){
	    	queryMsgList();
		    
		    //查询这个用户的channelId
		    queryChannelId();
		}
	    
	    
	}
	
	
	private void initTitleRightLayout() {
		
				
	}
	
	@Override
	protected void onStart() {
		// 注册广播接收器
		IntentFilter mIntentFilter = new IntentFilter(action);
		mIntentFilter.addAction("com.baidu.android.pushservice.action.RECEIVE");
		mIntentFilter.addAction("com.baidu.android.pushservice.action.notification.CLICK");
		registerReceiver(mDataReceiver, mIntentFilter);
		super.onStart();
	}

	@Override
	protected void onStop() {
		// 取消注册的广播接收器
		unregisterReceiver(mDataReceiver);
		super.onStop();
	}

	@Override
	public void finish() {
		//必须要释放
		mAbSqliteStorage.release();
		super.finish();
	}
	
	/**
	 * 
	 * 描述：发消息
	 * @param mMessage
	 * @throws 
	 */
	protected void messageToUser(String content) {
		String mMessageId =  "msg_"+mChatMsgList.size();
		FrontiaPushUtil.MessageContent msg = new FrontiaPushUtil.MessageContent(
				mMessageId, FrontiaPushUtil.DeployStatus.PRODUCTION);

		msg.setMessage(content);

		mPush.pushMessage(faceUserPushUserlId, faceUserPushChannelId, msg, new PushMessageListener() {

			@Override
			public void onSuccess(String id) {
				
				//更新显示状态
                ChatMsg mChatMsg = mChatMsgList.get(mChatMsgList.size()-1);
                mChatMsg.setSendState(1);
                mChatMsg.setCreateTime(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
				mChatMsgViewAdapter.notifyDataSetChanged();
				isSendEnable = true;
				updateData(mChatMsg);
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				 showToast(errMsg);
				 isSendEnable = true;
				 //更新显示状态
                 ChatMsg msg = mChatMsgList.get(mChatMsgList.size()-1);
                 msg.setSendState(-1);
			     mChatMsgViewAdapter.notifyDataSetChanged();
			     updateData(msg);
			}

		});

	}
	
	/**
	 * 
	 * 描述：发通知
	 * @param mMessage
	 * @throws 
	 */
	protected void notificationToUser(String mMessage) {
		String mMessageId =  "msg_"+mChatMsgList.size();
		NotificationContent notification = new NotificationContent(application.mUser.getName(), mMessage);
		FrontiaPushUtil.MessageContent msg = new FrontiaPushUtil.MessageContent(
				mMessageId, FrontiaPushUtil.DeployStatus.PRODUCTION);
		
		msg.setNotification(notification);
		mPush.pushMessage(faceUserPushUserlId, faceUserPushChannelId, msg, new PushMessageListener() {

			@Override
			public void onSuccess(String id) {
                    
                    //更新显示状态
                    ChatMsg msg = mChatMsgList.get(mChatMsgList.size()-1);
                    msg.setSendState(1);
					msg.setCreateTime(AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS));
					mChatMsgViewAdapter.notifyDataSetChanged();
					updateData(msg);
					isSendEnable = true;
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				    showToast(errMsg);
				    isSendEnable = true;
				    //更新显示状态
                    ChatMsg msg = mChatMsgList.get(mChatMsgList.size()-1);
                    msg.setSendState(-1);
					mChatMsgViewAdapter.notifyDataSetChanged();
					updateData(msg);
			}

		});

	}

	
	public void queryChannelId(){
		showProgressDialog("正在连线"+faceUser.getName()+"...");
		
		/*//测试
		if(faceUser.getuId().equals("1627666132")){
			//2442547254
			{"push_app_channel_id":"3487968799628127529",
				"push_app_push_user_id":"740175682241184050",
				"push_app_app_id":"1512528",
				"push_app_user_id":"1627666132"}
			removeProgressDialog();
			faceUserPushChannelId = "3487968799628127529";
			faceUserPushUserlId  = "740175682241184050";
			return;
		}else if(faceUser.getuId().equals("1476665044")){
			//396196516
			{"push_app_channel_id":"3924444570987482063"
				,"push_app_push_user_id":"1044216166506584831",
				"push_app_app_id":"1512528",
				"push_app_user_id":"1476665044"}
			
			removeProgressDialog();
			faceUserPushChannelId = "3924444570987482063";
			faceUserPushUserlId  = "1044216166506584831";
			return;
		}*/
		
		//查询
		FrontiaQuery query = new FrontiaQuery();
		query = query.equals("push_local_user_id", faceUser.getuId());
		Log.d(TAG, "查询channel数据push_local_user_id="+faceUser.getuId());
		mCloudStorage.findData(query,new DataInfoListener() {

			@Override
			public void onSuccess(List<FrontiaData> dataList) {
				removeProgressDialog();
		        if(dataList!=null && dataList.size()>0){
		        	FrontiaData fd = dataList.get(0);
		        	try {
						JSONObject data = fd.getData();
						Log.d(TAG, "查询到channel数据"+data.toString());
						faceUserPushChannelId = data.getString("push_channel_id");
						faceUserPushUserlId = data.getString("push_user_id");
					} catch (Exception e) {
						e.printStackTrace();
					}
		        }else{
		        	showToast(faceUser.getName()+"[不能发送]");
		        }
			}

			@Override
			public void onFailure(int errCode, String errMsg) {
				removeProgressDialog();
				Log.d("TAG","queryChannelId错误为"+errCode+errMsg);
				showToast(faceUser.getName()+"[不能发送]");
			}
		});
	}


	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void saveData(final ChatMsg chatMsg){
		mAbSqliteStorage.insertData(chatMsg, mChatMsgDao, new AbDataInsertListener(){

			@Override
			public void onSuccess(long id) {
				//showToast("插入数据成功id="+id);
				chatMsg.set_id((int)id);
			}

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				showToast(errorMessage);
			}
			
		});
	}
	
	public void saveUserData(final User user){
		//查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("u_id",user.getuId());
		
		//无sql存储的查询
		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao, new AbDataInfoListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				showToast(errorMessage);
			}

			@Override
			public void onSuccess(List<?> paramList) {
				if(paramList==null || paramList.size()==0){
					user.setLoginUser(false);
					mAbSqliteStorage.insertData(user, mUserDao, new AbDataInsertListener(){

						@Override
						public void onSuccess(long id) {
							
						}

						@Override
						public void onFailure(int errorCode, String errorMessage) {
							showToast(errorMessage);
						}
						
					});
				}
			}
			
		});
		
	}
	
	public void queryMsgList(){
		//查询数据
		AbStorageQuery mAbStorageQuery1 = new AbStorageQuery();
		
		//第一组条件
		mAbStorageQuery1.equals("u_id",application.mUser.getuId());
		mAbStorageQuery1.equals("face_u_id",faceUser.getuId());
		
		//第二组条件
		AbStorageQuery mAbStorageQuery2 = new AbStorageQuery();
		mAbStorageQuery2.equals("face_u_id",application.mUser.getuId());
		mAbStorageQuery2.equals("u_id",faceUser.getuId());
		
		//组合查询
		AbStorageQuery mAbStorageQuery = mAbStorageQuery1.or(mAbStorageQuery2);
		
		//无sql存储的查询
		mAbSqliteStorage.findData(mAbStorageQuery, mChatMsgDao, new AbDataInfoListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				showToast(errorMessage);
			}

			@Override
			public void onSuccess(List<?> paramList) {
				
				mChatMsgList.addAll((List<ChatMsg>)paramList);
				mChatMsgViewAdapter.notifyDataSetChanged();
			}
			
		});
		
	}
	
	
	public void updateData(ChatMsg mChatMsg){
		
		//无sql存储的查询
		mAbSqliteStorage.updateData(mChatMsg, mChatMsgDao, new AbDataOperationListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				showToast(errorMessage);
			}

			@Override
			public void onSuccess(long row) {
			}
			
		});
		
	}
	
	
	public void queryUserById(String uId,final int position){
		//查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("u_id",uId);
		
		//无sql存储的查询
		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao, new AbDataInfoListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				showToast(errorMessage);
			}

			@Override
			public void onSuccess(List<?> paramList) {
				if(paramList!=null && paramList.size()>0){
					User user = (User)paramList.get(0);
					if(user!=null){
						ChatMsg msg = mChatMsgList.get(position);
						msg.setUser(user);
						mChatMsgViewAdapter.notifyDataSetChanged();
					}
				}
			}
			
		});
		
	}
	
	
	
}
