package com.andbase.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
import com.andbase.im.util.IMUtil;

public class RegisterActivity extends AbActivity {
	
	private MyApplication application;
	private EditText userName = null;
	private EditText userPwd = null;
	private EditText userPwd2 = null;
	private EditText email = null;
	private CheckBox checkBox = null;
	
	private ImageButton mClear1;
	private ImageButton mClear2;
	private ImageButton mClear3;
	private ImageButton mClear4;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.register);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.register_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		//设置AbTitleBar在最上
		this.setTitleBarOverlay(true);
        userName = (EditText)this.findViewById(R.id.userName);
		userPwd = (EditText)this.findViewById(R.id.userPwd);
		userPwd2 = (EditText)this.findViewById(R.id.userPwd2);
		email = (EditText)this.findViewById(R.id.email);
		checkBox = (CheckBox) findViewById(R.id.register_check);
		mClear1 = (ImageButton)findViewById(R.id.clearName);
        mClear2 = (ImageButton)findViewById(R.id.clearPwd);
        mClear3 = (ImageButton)findViewById(R.id.clearPwd2);
        mClear4 = (ImageButton)findViewById(R.id.clearEmail);
        Button agreementBtn = (Button) findViewById(R.id.agreementBtn);
        
        userName.setText("15150509589");
        userPwd.setText("123456");
        userPwd2.setText("123456");
        email.setText("amsoft.cn2010@163.com");
        
		agreementBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RegisterActivity.this, AgreementActivity.class);
				startActivity(intent);
			}
		});
		
	    Button registerBtn = (Button)this.findViewById(R.id.registerBtn);
	    registerBtn.setOnClickListener(new RegisterOnClickListener());
		
	    mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
        
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
			}
		});
        
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
						AbToastUtil.showToast(RegisterActivity.this,R.string.error_name_expr);
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
						AbToastUtil.showToast(RegisterActivity.this,R.string.error_name_expr);
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
        
        userPwd2.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = userPwd2.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear3.setVisibility(View.VISIBLE);
					if(!AbStrUtil.isNumberLetter(str)){
						str = str.substring(0, length-1);
						userPwd2.setText(str);
						String str1 = userPwd2.getText().toString().trim();
						userPwd2.setSelection(str1.length());
						AbToastUtil.showToast(RegisterActivity.this,R.string.error_name_expr);
					}
					mClear3.postDelayed(new Runnable(){

						@Override
						public void run() {
							mClear3.setVisibility(View.INVISIBLE);
						}
						
					}, 5000);
				} else {
					mClear3.setVisibility(View.INVISIBLE);
				}
					
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}

			public void afterTextChanged(Editable s) {

			}
		});
        
        
        email.addTextChangedListener(new TextWatcher() {

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String str = email.getText().toString().trim();
				int length = str.length();
				if (length > 0) {
					mClear4.setVisibility(View.VISIBLE);
					if(AbStrUtil.isContainChinese(str)){
						str = str.substring(0, length-1);
						email.setText(str);
						String str1 = email.getText().toString().trim();
						email.setSelection(str1.length());
						AbToastUtil.showToast(RegisterActivity.this,R.string.error_email_expr2);
					}
					mClear4.postDelayed(new Runnable(){

						@Override
						public void run() {
							mClear4.setVisibility(View.INVISIBLE);
						}
						
					}, 5000);
				} else {
					mClear4.setVisibility(View.INVISIBLE);
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
        
        mClear3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				userPwd2.setText("");
			}
		});
        
        mClear4.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				email.setText("");
			}
		});
        
        initTitleRightLayout();
    }
    
    private void initTitleRightLayout(){
    	
    }
    
    
   public class  RegisterOnClickListener implements View.OnClickListener{
		
		@Override
		public void onClick(View v) {
			final String mStr_name = userName.getText().toString().trim();
			final String mStr_pwd = userPwd.getText().toString().trim();
			final String mStr_pwd2 = userPwd2.getText().toString().trim();
			final String mStr_email = email.getText().toString().trim();
			
			if (AbStrUtil.isEmpty(mStr_name)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_name);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (!AbStrUtil.isNumberLetter(mStr_name)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_name_expr);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_name)<3) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_name_length1);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_name)>20) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_name_length2);
				userName.setFocusable(true);
				userName.requestFocus();
				return;
			}
			
			if (AbStrUtil.isEmpty(mStr_pwd)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd);
				userPwd.setFocusable(true);
				userPwd.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_pwd)<6) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd_length1);
				userPwd.setFocusable(true);
				userPwd.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_pwd)>20) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd_length2);
				userPwd.setFocusable(true);
				userPwd.requestFocus();
				return;
			}
			
			if (AbStrUtil.isEmpty(mStr_pwd2)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd);
				userPwd2.setFocusable(true);
				userPwd2.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_pwd2)<6) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd_length1);
				userPwd2.setFocusable(true);
				userPwd2.requestFocus();
				return;
			}
			
			if (AbStrUtil.strLength(mStr_pwd2)>20) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd_length2);
				userPwd2.setFocusable(true);
				userPwd2.requestFocus();
				return;
			}
			
			if (!mStr_pwd2.equals(mStr_pwd)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_pwd_match);
				userPwd2.setFocusable(true);
				userPwd2.requestFocus();
				return;
			}
			
			if (AbStrUtil.isEmpty(mStr_email)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_email);
				email.setFocusable(true);
				email.requestFocus();
				return;
			}
			
			if (!AbStrUtil.isEmail(mStr_email)) {
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_email_expr);
				email.setFocusable(true);
				email.requestFocus();
				return;
			}
			
			if(!checkBox.isChecked()){
				AbToastUtil.showToast(RegisterActivity.this,R.string.error_agreement);
				return;
			}
			
			//注册
			register(mStr_name,mStr_pwd);
		}
	}
   
   
   public void  register(final String userName,final String password){
	    AbDialogUtil.showProgressDialog(RegisterActivity.this, 0, "正在注册...");
	    AbTask task = AbTask.newInstance();
		//定义异步执行的对象
   	    final AbTaskItem item = new AbTaskItem();
		item.setListener(new AbTaskListener() {

			@Override
			public void update() {
				AbDialogUtil.removeDialog(RegisterActivity.this);
				AbToastUtil.showToast(RegisterActivity.this,"注册成功！");
			}

			@Override
			public void get() {
	   		    try {
	   				IMUtil.register(userName, password);
	   		    } catch (Exception e) {
	   		    	e.printStackTrace();
	   		    }
		  };
		});
       task.execute(item);
   }
}


