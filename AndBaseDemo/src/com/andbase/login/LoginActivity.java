package com.andbase.login;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
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
import com.ab.global.AbConstant;
import com.ab.util.AbStrUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.friend.UserDao;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;

public class LoginActivity extends AbActivity {
	
	private EditText userName = null;
	private EditText userPwd = null;
	private MyApplication application;
	private String mStr_name = null;
	private String mStr_pwd = null;
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
		mAbTitleBar.setTitleText(R.string.login);
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
				Editor editor = abSharedPreferences.edit();
				editor.putBoolean(Constant.USERPASSWORDREMEMBERCOOKIE, isChecked);
				editor.commit();
				application.userPasswordRemember = isChecked;
			}
		});
        
        String name = abSharedPreferences.getString(Constant.USERNAMECOOKIE, "");
        String password = abSharedPreferences.getString(Constant.USERPASSWORDCOOKIE, "");
		boolean userPwdRemember = abSharedPreferences.getBoolean(Constant.USERPASSWORDREMEMBERCOOKIE, false);
        
		if(userPwdRemember){
			userName.setText(name);
			userPwd.setText(password);
			checkBox.setChecked(true);
		}else{
			userName.setText("");
			userPwd.setText("");
			checkBox.setChecked(false);
		}
		
		//userName.setText("zhaoqp");
        //userPwd.setText("123456");
        
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
						showToast(R.string.error_name_expr);
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
						showToast(R.string.error_pwd_expr);
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
			if(v==loginBtn){
				mStr_name = userName.getText().toString();
				mStr_pwd = userPwd.getText().toString();
				
				if (TextUtils.isEmpty(mStr_name)) {
					showToast(R.string.error_name);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (!AbStrUtil.isNumberLetter(mStr_name)) {
					showToast(R.string.error_name_expr);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStr_name)<3) {
					showToast(R.string.error_name_length1);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStr_name)>20) {
					showToast(R.string.error_name_length2);
					userName.setFocusable(true);
					userName.requestFocus();
					return;
				}
				
				if (TextUtils.isEmpty(mStr_pwd)) {
					showToast(R.string.error_pwd);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStr_pwd)<6) {
					showToast(R.string.error_pwd_length1);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				
				if (AbStrUtil.strLength(mStr_pwd)>20) {
					showToast(R.string.error_pwd_length2);
					userPwd.setFocusable(true);
					userPwd.requestFocus();
					return;
				}
				
				showToast("演示界面,没什么用");
				
				//showProgressDialog();
				
				/*application.mUser = new User();
				if(application.mUser !=null){
					if(application.userPasswordRemember){
						Editor editor = abSharedPreferences.edit();
						editor.putString(Constant.USERNAMECOOKIE, mStr_name);
						editor.putString(Constant.USERPASSWORDCOOKIE,mStr_pwd);
						application.firstStart = false;
						editor.putBoolean(Constant.FIRSTSTART, false);
						editor.commit();
					}else{
						Editor editor = abSharedPreferences.edit();
						application.firstStart = false;
						editor.putBoolean(Constant.FIRSTSTART, false);
						editor.commit();
					}
					
					finish();
				}else{
		        	showToast("用户不存在");
		        }*/

			}
			
		}
	}
   
}


