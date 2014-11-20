package com.andbase.demo.activity;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.network.StringRequest;
import com.ab.network.toolbox.AuthFailureError;
import com.ab.network.toolbox.Request;
import com.ab.network.toolbox.Request.Method;
import com.ab.network.toolbox.RequestQueue;
import com.ab.network.toolbox.RequestQueue.RequestFilter;
import com.ab.network.toolbox.Response;
import com.ab.network.toolbox.Volley;
import com.ab.network.toolbox.VolleyError;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

/**
 * 
 * © 2012 amsoft.cn 
 * 名称：MiniRequestActivity.java 
 * 描述：快速响应的请求
 * 
 * @author 还如一梦中
 * @date：2014-11-03 上午11:01:21
 * @version v1.0
 */
public class HttpMiniActivity extends AbActivity {

	private MyApplication mApplication;
	private RequestQueue mQueue = null;
	private Response.Listener<String> mListener = new Response.Listener<String>() {
		@Override
		public void onResponse(String response) {
			AbDialogUtil.showAlertDialog(HttpMiniActivity.this,"返回结果",response.trim(),new AbDialogOnClickListener(){

				@Override
				public void onNegativeClick() {
				}

				@Override
				public void onPositiveClick() {
				}
        	});
		}
	};
	
	private Response.ErrorListener mErrorListener = new Response.ErrorListener() {
		@Override
		public void onErrorResponse(VolleyError error) {
			AbToastUtil.showToast(HttpMiniActivity.this,error.getMessage());
		}
    };
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.http_mini);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.http_mini_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		mApplication = (MyApplication) abApplication;
		Button getBtn = (Button) this.findViewById(R.id.getBtn);

		mQueue = Volley.newRequestQueue(this);  
		
		// get请求
		getBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				// 一个url地址
				String urlGet = "http://www.amsoft.cn";
				StringRequest requestGet = new StringRequest(urlGet,mListener,mErrorListener);
				
				mQueue.add(requestGet);
				
				String urlPost = "http://www.baidu.com";
				
				StringRequest requestPost = new StringRequest(Method.POST, urlPost, mListener,mErrorListener) {
					@Override
					protected Map<String, String> getParams() throws AuthFailureError {
						Map<String, String> map = new HashMap<String, String>();
						map.put("params1", "value1");
						map.put("params2", "value2");
						return map;
					}
				};
				
				mQueue.add(requestPost);

			}
		});
	}

	@Override
	public void finish() {
		mQueue.cancelAll(new RequestFilter(){

			@Override
			public boolean apply(Request<?> request) {
				//所有
				return true;
			}
			
		});
		super.finish();
	}
	

}
