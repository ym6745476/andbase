package com.andbase.demo.activity;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.soap.AbSoapListener;
import com.ab.soap.AbSoapParams;
import com.ab.soap.AbSoapUtil;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 名称：SoapActivity
 * 描述：soap框架
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class SoapActivity extends AbActivity {
	
	private MyApplication application;
	
	private AbSoapUtil mAbSoapUtil = null;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.soap_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.soap_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button callBtn  = (Button)this.findViewById(R.id.callBtn);
        
        //获取Http工具类
        mAbSoapUtil = AbSoapUtil.getInstance(this);
        mAbSoapUtil.setTimeout(10000);
        //get请求
        callBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// 一个url地址
				String urlString = "http://webservice.webxml.com.cn/WebServices/MobileCodeWS.asmx?op=getMobileCodeInfo"; 
                String nameSpace = "http://WebXml.com.cn/";
                String methodName = "getMobileCodeInfo";
                AbSoapParams params = new AbSoapParams();
                params.put("mobileCode", "15150509589");
                params.put("userID", "");
				
				mAbSoapUtil.call(urlString,nameSpace,methodName,params, new AbSoapListener() {
					
					//获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, SoapObject object) {
		        		
		        		AbDialogUtil.showAlertDialog(SoapActivity.this,"返回结果",object.toString(),new AbDialogOnClickListener(){

							@Override
							public void onNegativeClick() {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onPositiveClick() {
								// TODO Auto-generated method stub
								
							}
							
		            		
		            	});
		        	}
		        	
		        	// 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	
		            	AbToastUtil.showToast(SoapActivity.this,error.getMessage());
					}
		            
		            // 失败，调用
		            @Override
					public void onFailure(int statusCode, SoapFault fault) {
		            	AbToastUtil.showToast(SoapActivity.this,fault.faultstring);
					}

					// 开始执行前
		            @Override
					public void onStart() {
		            	//显示进度框
		            	AbDialogUtil.showProgressDialog(SoapActivity.this,0,"正在查询...");
					}


					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	//移除进度框
		            	AbDialogUtil.removeDialog(SoapActivity.this);
		            };
		            
		        });
				
			}
		});
    }
    
}
