package com.andbase.im.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.packet.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataUpdateListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.util.AbDateUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.friend.UserDao;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.andbase.im.adapter.ChatMsgViewAdapter;
import com.andbase.im.dao.IMMsgDao;
import com.andbase.im.model.IMMessage;
import com.andbase.im.util.IMRecordListener;
import com.andbase.im.util.IMRecorder;
import com.andbase.model.User;


public class ChatActivity extends AbActivity {
	
	private static final String TAG = "ChartActivity";
	private static final boolean D = Constant.DEBUG;
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
	
	private ChatMsgViewAdapter mChatMsgViewAdapter = null;
	private List<IMMessage> mChatMsgList = null;
	private ListView mMsgListView = null;
	private EditText mContentEdit = null;
	private Button mSendBtn = null;
	private ImageButton mAddBtn = null;
	private ImageButton mVoiceBtn = null;
	private Button mVoiceSendBtn = null;
	
	//发送选项面板
	private LinearLayout chatAppPanel = null;
	
	//推送内容
	private String mContentStr = null;
	
	//是否可以发送下一条
	private boolean isSendEnable  = true;
	
	private final String action = "com.baidu.android.pushservice.action.MESSAGE";
			
	//数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public UserDao mUserDao = null;
	private IMMsgDao mIMMsgDao = null;
	private int pageSize = 10;
	
	// 登录用户
	protected String userName;
	// 和谁聊天
	protected String toUserName;
	
	//我的录音类
	private IMRecorder  mIMRecorder = null;
	
	//创建BroadcastReceiver
	private BroadcastReceiver mDataReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
			IMMessage mIMMessage = (IMMessage)intent.getParcelableExtra("MESSAGE");
			
			if(mIMMessage!=null){
			    if(D) Log.d(TAG, "收到了消息:" + mIMMessage.getContent());
                //mIMMessage.setSendState(IMMessage.READ);
                if(application.mUser!=null){
                    if(mIMMessage.getToUserName().equals(application.mUser.getUserName())){
                        mChatMsgList.add(mIMMessage);
                        mChatMsgViewAdapter.notifyDataSetChanged();
                    }
                }
				
			}
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.chat);
		application = (MyApplication) abApplication;
		
		//初始化AbSqliteStorage
	    mAbSqliteStorage = AbSqliteStorage.getInstance(this);
	    
	    //初始化数据库操作实现类
	    mUserDao  = new UserDao(this);
        mIMMsgDao = new IMMsgDao(this);
        
		mAbTitleBar = this.getTitleBar();
		
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setTitleTextBold(false);
		mAbTitleBar.setTitleBarGravity(Gravity.CENTER,Gravity.CENTER);
		//设置AbTitleBar在最上
		this.setTitleBarOverlay(true);
        
		//获取传递的参数
		Intent mIntent = this.getIntent();
        //聊天对象
        toUserName = mIntent.getStringExtra("USERNAME");
        if(application.mUser!=null){
            userName  = application.mUser.getUserName();
        }
       
		mAbTitleBar.setTitleText("与"+toUserName+"的会话");
		
		mContentEdit = (EditText)findViewById(R.id.content);
		mSendBtn = (Button)findViewById(R.id.sendBtn);
		mAddBtn = (ImageButton)findViewById(R.id.addBtn);
		mVoiceBtn = (ImageButton)findViewById(R.id.voiceBtn);
		mVoiceSendBtn= (Button)findViewById(R.id.voiceSendBtn);
		mContentEdit.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before,
                    int count) {
                String str = mContentEdit.getText().toString().trim();
                int length = str.length();
                if (length > 0) {
                    mSendBtn.setVisibility(View.VISIBLE);
                    mAddBtn.setVisibility(View.GONE);
                } else {
                    mSendBtn.setVisibility(View.GONE);
                    mAddBtn.setVisibility(View.VISIBLE);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                    int after) {
                
            }

            public void afterTextChanged(Editable s) {

            }
        });
		
		mVoiceBtn.setOnClickListener(new OnClickListener(){
            
            @Override
            public void onClick(View v){
                if(mVoiceSendBtn.getVisibility() == View.VISIBLE){
                    mVoiceSendBtn.setVisibility(View.GONE);
                    mContentEdit.setVisibility(View.VISIBLE);
                    mVoiceBtn.setBackgroundResource(R.drawable.button_selector_chat_voice);
                }else{
                    mVoiceSendBtn.setVisibility(View.VISIBLE);
                    mContentEdit.setVisibility(View.GONE);
                    mVoiceBtn.setBackgroundResource(R.drawable.button_selector_chat_key);
                }
                
            }
        });
		
		//按住录音
		mVoiceSendBtn.setOnTouchListener(new OnTouchListener(){
            
            @Override
            public boolean onTouch(View v, MotionEvent event){
    		    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //开始录音
                        startRecording();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        
                        break;
                    case MotionEvent.ACTION_UP:
                        //结束录音
                        stopRecording(false);
                        break;
                    }
                    return false;
            }
        });
		
		mSendBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(application.mUser == null){
					AbToastUtil.showToast(ChatActivity.this,"请先返回登录");
					return;
				}
				
				mContentStr = mContentEdit.getText().toString().trim();
				if(!AbStrUtil.isEmpty(mContentStr)){
					if(!isSendEnable){
						AbToastUtil.showToast(ChatActivity.this,"上一条正在发送，请稍等");
						return;
					}
					
					
					//发送通知
					isSendEnable = false;
						
					//清空文本框
	                mContentEdit.setText("");
	                
	                String time = AbDateUtil.getCurrentDate(AbDateUtil.dateFormatYMDHMS);
	                IMMessage mIMMessage = new IMMessage();
	                //mIMMessage.setType(IMMessage.CHAT_MSG);
	                //mIMMessage.setSendState(IMMessage.UNSEND);
	                //mIMMessage.setContent(mContentStr);
	                //mIMMessage.setTime(time);
	                //mIMMessage.setUserName(userName);
	                mIMMessage.setToUserName(toUserName);
	                //mIMMessage.setTime(time);
	                
	                saveMessageData(mIMMessage);
					
	                mChatMsgList.add(mIMMessage);
	                mChatMsgViewAdapter.notifyDataSetChanged();
	                
	                sendMessage(mIMMessage);
					
				}
			}
			
		});
		
		mMsgListView = (ListView)this.findViewById(R.id.mListView);
		mChatMsgList = new ArrayList<IMMessage>();
		
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
	        //查询历史消息
	    	queryMsgList();
		}
	    
	}
	
	
	private void initTitleRightLayout() {
		
				
	}
	
	@Override
	protected void onStart() {
		// 注册广播接收器
		IntentFilter mIntentFilter = new IntentFilter(action);
		//mIntentFilter.addAction(IMConstant.ACTION_NEW_MESSAGE);
		//mIntentFilter.addAction(IMConstant.ACTION_CHAT_MESSAGE);
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
		mAbSqliteStorage.release();
		super.finish();
	}
	
	/**
     * 
     * 描述：发消息
     * @param message
     * @throws 
     */
	protected void sendMessage(IMMessage mIMMessage){
	    //mIMMessage.setSendState(IMMessage.SENDING);
        mChatMsgViewAdapter.notifyDataSetChanged();
	    try{
            Message mMessage = new Message();
            mMessage.setSubject("会话消息");
           // mMessage.setProperty(IMMessage.KEY_TIME, mIMMessage.getTime());
            mMessage.setBody(mIMMessage.getContent());
            //chat.sendMessage(mMessage);
           // mIMMessage.setSendState(IMMessage.SENDED);
            mChatMsgViewAdapter.notifyDataSetChanged();
            updateData(mIMMessage);
        }catch (Exception e){
            e.printStackTrace();
           // mIMMessage.setSendState(IMMessage.FAILED);
            mChatMsgViewAdapter.notifyDataSetChanged();
        }
	    isSendEnable = true;
    }


	@Override
	protected void onResume() {
		super.onResume();
	}
	
	/**
	 * 保存消息数据
	 * @param chatMsg
	 */
	public void saveMessageData(final IMMessage chatMsg){
		mAbSqliteStorage.insertData(chatMsg, mIMMsgDao, new AbDataInsertListener(){

			@Override
			public void onSuccess(long id) {
				//showToast("插入数据成功id="+id);
				chatMsg.set_id((int)id);
			}

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				AbToastUtil.showToast(ChatActivity.this,errorMessage);
			}
			
		});
	}
	
	/**
     * 查询消息数据
     */
	public void queryMsgList(){
		//查询数据
		AbStorageQuery mAbStorageQuery1 = new AbStorageQuery();
		
		//第一组条件
		mAbStorageQuery1.equals("user_name",application.mUser.getUserName());
		mAbStorageQuery1.equals("to_user_name",toUserName);
		
		//第二组条件
		AbStorageQuery mAbStorageQuery2 = new AbStorageQuery();
		mAbStorageQuery2.equals("to_user_name",application.mUser.getUserName());
		mAbStorageQuery2.equals("user_name",toUserName);
		
		//组合查询
		AbStorageQuery mAbStorageQuery = mAbStorageQuery1.or(mAbStorageQuery2);
		
		//查询
		mAbSqliteStorage.findData(mAbStorageQuery, mIMMsgDao, new AbDataSelectListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				AbToastUtil.showToast(ChatActivity.this,errorMessage);
			}

			@Override
			public void onSuccess(List<?> paramList) {
				
				mChatMsgList.addAll((List<IMMessage>)paramList);
				mChatMsgViewAdapter.notifyDataSetChanged();
			}
			
		});
		
	}
	
	/**
     * 更新消息数据
     * @param chatMsg
     */
	public void updateData(IMMessage mChatMsg){
		
		mAbSqliteStorage.updateData(mChatMsg, mIMMsgDao, new AbDataUpdateListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				AbToastUtil.showToast(ChatActivity.this,errorMessage);
			}

			@Override
			public void onSuccess(int rows) {
			}
			
		});
		
	}
	
	
	public void queryUserByName(String userName,final int position){
		//查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("user_name",userName);
		
		//查询
		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao, new AbDataSelectListener(){

			@Override
			public void onFailure(int errorCode, String errorMessage) {
				AbToastUtil.showToast(ChatActivity.this,errorMessage);
			}

			@Override
			public void onSuccess(List<?> paramList) {
				if(paramList!=null && paramList.size()>0){
					User user = (User)paramList.get(0);
					if(user!=null){
					    IMMessage msg = mChatMsgList.get(position);
						//msg.setUser(user);
						mChatMsgViewAdapter.notifyDataSetChanged();
					}
				}
			}
			
		});
		
	}
	
	
	public void startRecording(){
	    if(mIMRecorder == null){
	        
	        mIMRecorder = new IMRecorder(this, new IMRecordListener(){
                
                @Override
                public void onRecording(){
                	AbToastUtil.showToast(ChatActivity.this,"正在录音");
                }
                
                @Override
                public void onPreRecording(){
                	AbToastUtil.showToast(ChatActivity.this,"准备录音");
                }
                
                @Override
                public void onFinish(File file, long time){
                    AbDialogUtil.showAlertDialog(ChatActivity.this,"录音完成", "录音完成,文件在"+file.getPath()+",长度为："+AbDateUtil.getTimeDescription(time));
                }
                
                @Override
                public void onError(int errorCode, String errorMessage){
                    AbToastUtil.showToast(ChatActivity.this,"提示："+errorMessage);
                }
                
                @Override
                public void onCancel(){
                    // TODO Auto-generated method stub
                    
                }
            });
	        
	    }
	    
	    //开始录音
        mIMRecorder.startRecording();
	}
	
    public void stopRecording(boolean isCancel){
        mIMRecorder.stopRecording(isCancel);
    }
	
	
	
}
