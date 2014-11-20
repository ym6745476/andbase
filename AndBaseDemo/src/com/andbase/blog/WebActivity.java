package com.andbase.blog;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
/**
 * © 2012 amsoft.cn 
 * 名称：WebActivity 
 * 描述：网站Wap
 * @author 还如一梦中
 * @date 2011-11-8
 * @version 
 */
public class WebActivity extends AbActivity {
	
	//主界面
	private WebView mWebView = null;
	private ProgressBar mProgressBar = null;
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置布局
        setAbContentView(R.layout.web);
        
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.blog_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		mAbTitleBar.setVisibility(View.GONE);
		
        mWebView = (WebView)findViewById(R.id.webView);
        mProgressBar = (ProgressBar)findViewById(R.id.progress_bar);
        //设置支持JavaScript脚本
		WebSettings webSettings = mWebView.getSettings();  
		webSettings.setJavaScriptEnabled(true);
		//设置可以访问文件
		webSettings.setAllowFileAccess(true);
		//设置可以支持缩放
		webSettings.setSupportZoom(true);
		//设置默认缩放方式尺寸是far
		webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
		//设置出现缩放工具
		webSettings .setBuiltInZoomControls(true);
		webSettings.setDefaultFontSize(20);
        //访问assets目录下的文件
        String url = Constant.BASEURL+"m";
        mWebView.loadUrl(url);
        
		
        // 设置WebViewClient
		mWebView.setWebViewClient(new WebViewClient() {
			// url拦截
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				// 使用自己的WebView组件来响应Url加载事件，而不是使用默认浏览器器加载页面
				view.loadUrl(url);
				// 相应完成返回true
				return true;
				//return super.shouldOverrideUrlLoading(view, url);
			}

			// 页面开始加载
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				mProgressBar.setVisibility(View.VISIBLE);
				super.onPageStarted(view, url, favicon);
			}

			// 页面加载完成
			@Override
			public void onPageFinished(WebView view, String url) {
				mProgressBar.setVisibility(View.GONE);
				super.onPageFinished(view, url);
			}

			// WebView加载的所有资源url
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

		});

		// 设置WebChromeClient
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			// 处理javascript中的alert
			public boolean onJsAlert(WebView view, String url, String message,
					final JsResult result) {
				return super.onJsAlert(view, url, message, result);
			};

			@Override
			// 处理javascript中的confirm
			public boolean onJsConfirm(WebView view, String url,
					String message, final JsResult result) {
				return super.onJsConfirm(view, url, message, result);
			};

			@Override
			// 处理javascript中的prompt
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, final JsPromptResult result) {
				return super.onJsPrompt(view, url, message, defaultValue, result);
			};
			
			//设置网页加载的进度条
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				mProgressBar.setProgress(newProgress);
				super.onProgressChanged(view, newProgress);
			}
			
			//设置程序的Title
			@Override
			public void onReceivedTitle(WebView view, String title) {
				setTitle(title);
				super.onReceivedTitle(view, title);
			}
		});
		
        mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				back();
			}
		});

	}
    
    /**
	 * 拦截返回键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 是否触发按键为back键
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			back();
			return true;
			// 如果不是back键正常响应
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void back(){
		if(mWebView.canGoBack()){
			mWebView.goBack();
		}else{
			finish();
		}
	}
}
