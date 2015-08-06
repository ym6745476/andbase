package com.andbase.login;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.ab.activity.AbActivity;
import com.ab.db.storage.AbSqliteStorage;
import com.ab.db.storage.AbSqliteStorageListener.AbDataInsertListener;
import com.ab.db.storage.AbSqliteStorageListener.AbDataSelectListener;
import com.ab.db.storage.AbStorageQuery;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbSharedUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.friend.UserDao;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.andbase.im.util.IMUtil;
import com.andbase.model.User;

public class LoginActivity extends AbActivity {
	
	private EditText userName = null;
	private EditText userPwd = null;
	private MyApplication application;
	private String mStrName = null;
	private String mStrPwd = null;
	private ImageButton mClear1;
	private ImageButton mClear2;
	private AbTitleBar mAbTitleBar = null;
	
	private Button loginBtn = null;
	
	//数据库操作类
	public AbSqliteStorage mAbSqliteStorage = null;
	public UserDao mUserDao = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.login);
        application = (MyApplication)abApplication;
        
        //初始化AbSqliteStorage
	    mAbSqliteStorage = AbSqliteStorage.getInstance(this);
	    
	    //初始化数据库操作实现类
	    mUserDao  = new UserDao(LoginActivity.this);
        
        mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.im_login);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
        
        userName = (EditText)this.findViewById(R.id.userName);
		userPwd = (EditText)this.findViewById(R.id.userPwd);
		CheckBox checkBox = (CheckBox) findViewById(R.id.login_check);
		mClear1 = (ImageButton)findViewById(R.id.clearName);
        mClear2 = (ImageButton)findViewById(R.id.clearPwd);
		   
        loginBtn = (Button)this.findViewById(R.id.loginBtn);
	    Button register = (Button)this.findViewById(R.id.registerBtn);
	    loginBtn.setOnClickListener(new LoginOnClickListener());
		
		Button pwdBtn = (Button) findViewById(R.id.pwdBtn);
		pwdBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, FindPwdActivity.class);
				startActivity(intent);
			}
		});
		
		mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
				startActivity(intent);
				finish();
			}
		});
        
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				AbSharedUtil.putBoolean(LoginActivity.this, Constant.USERPASSWORDREMEMBERCOOKIE, isChecked);
				application.userPasswordRemember = isChecked;
			}
		});
        
        String name = AbSharedUtil.getString(LoginActivity.this, Constant.USERNAMECOOKIE);
        String password = AbSharedUtil.getString(LoginActivity.this, Constant.USERPASSWORDCOOKIE);
		boolean userPwdRemember = AbSharedUtil.getBoolean(LoginActivity.this,Constant.USERPASSWORDREMEMBERCOOKIE, false);
        
		if(userPwdRemember){
			userName.setText(name);
			userPwd.setText(password);
			checkBox.setChecked(true);
		}else{
			userName.setText("");
			userPwd.setText("");
			checkBox.setChecked(false);
		}
		
		userName.setText("15150509589");
		userPwd.setText("123456");

        initTitleRightLayout();
        
        userName.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = userName.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear1.setVisibility(View.VISIBLE);
					if(!AbStrUtil.isNumberLetter(str)){
						str = str.substring(0, length-1);
						userName.setText(str);
						String str1 = userName.getText().toString().trim();
						userName.setSelection(str1.length());
						AbToastUtil.showToast(LoginActivity.this,R.string.error_name_expr);
					}
					mClear1.postDelayed(new Runnable(){

						@Override
						public void run() {
							mClear1.setVisibility(View.INVISIBLE);
						}
						
					}, 5000);
					
					
				} else {
					mClear1.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void afterTextChanged(Editable s) {

			}
		});
        
        userPwd.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = userPwd.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear2.setVisibility(View.VISIBLE);
					if(!AbStrUtil.isNumberLetter(str)){
						str = str.substring(0, length-1);
						userPwd.setText(str);
						String str1 = userPwd.getText().toString().trim();
						userPwd.setSelection(str1.length());
						AbToastUtil.showToast(LoginActivity.this,R.string.error_pwd_expr);
					}
					mClear2.postDelayed(new Runnable(){

						@Override
						public void run() {
							mClear2.setVisibility(View.INVISIBLE);
						}
						
					}, 5000);
				} else {
					mClear2.setVisibility(View.INVISIBLE);
				}
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void afterTextChanged(Editable s) {

			}
		});
        
        mClear1.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userName.setText("");
			}
		});
        
        mClear2.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userPwd.setText("");
			}
		});
        
    }
    
   private void initTitleRightLayout(){
    	
   }
    
   public class  LoginOnClickListener implements View.OnClickListener{
		
		@Override
		public void onClick(View v) {
			if(v == loginBtn){
				mStrName = userName.getText().toString();
				mStrPwd = userPwd.getText().toString();
				
				if (TextUtils.isEmpty(mStrName)) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_name);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (!AbStrUtil.isNumberLetter(mStrName)) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_name_expr);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStrName)<3) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_name_length1);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStrName)>20) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_name_length2);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (AbStrUtil.isEmpty(mStrPwd)) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_pwd);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStrPwd)<6) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_pwd_length1);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStrPwd)>20) {
					AbToastUtil.showToast(LoginActivity.this,R.string.error_pwd_length2);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				
				login(mStrName,mStrPwd);
			}
			
		}
	}
   
   
   public void  login(final String userName, final String password){
	    AbDialogUtil.showProgressDialog(LoginActivity.this, 0, "正在登录...");
	    AbTask task = AbTask.newInstance();
		//定义异步执行的对象
  	    final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				AbDialogUtil.removeDialog(LoginActivity.this);
				AbToastUtil.showToast(LoginActivity.this,"登录成功！");
         	    User user = new User();
         	    user.setUserName(userName);
         	    user.setPassword(password);
         	    user.setLoginUser(true);
         	    application.updateLoginParams(user); 
         	    saveUserData(user);
                setResult(RESULT_OK);
                finish();
			}

			@Override
			public void get() {
	   		    try {
	   				IMUtil.login(LoginActivity.this,userName, password);
	   		    } catch (Exception e) {
	   		    	e.printStackTrace();
	   		    }
		  };
		});
        task.execute(item);
   }
   
	/**
	 * 保存登录数据
	 * 
	 */ 
	public void saveUserData(final User user) {
		// 查询数据
		AbStorageQuery mAbStorageQuery = new AbStorageQuery();
		mAbStorageQuery.equals("user_name", user.getUserName());
		mAbStorageQuery.equals("is_login_user", true);

		mAbSqliteStorage.findData(mAbStorageQuery, mUserDao,
			new AbDataSelectListener() {
	
				@Override
				public void onFailure(int errorCode, String errorMessage) {
					AbToastUtil.showToast(LoginActivity.this,errorMessage);
				}
	
				@Override
				public void onSuccess(List<?> paramList) {
					if (paramList == null || paramList.size() == 0) {
						mAbSqliteStorage.insertData(user, mUserDao,
							new AbDataInsertListener() {

								@Override
								public void onSuccess(long id) {

								}

								@Override
								public void onFailure(int errorCode,
										String errorMessage) {
									AbToastUtil.showToast(LoginActivity.this,errorMessage);
								}

							});
					   }
				   }
	
		    });
	}
}


