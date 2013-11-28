package com.andbase.demo.activity;

import java.io.File;
import java.net.URLEncoder;

import org.apache.http.protocol.HTTP;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbBinaryHttpResponseListener;
import com.ab.http.AbFileHttpResponseListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 名称：HttpActivity
 * 描述：Http框架
 * @author zhaoqp
 * @date 2011-12-13
 * @version
 */
public class HttpActivity extends AbActivity {
	
	/** The Constant TAG. */
    private static final String TAG = "HttpActivity";
	
	private MyApplication application;
	
	private AbHttpUtil mAbHttpUtil = null;
	
	// ProgressBar进度控制
	private AbHorizontalProgressBar mAbProgressBar;
	// 最大100
	private int max = 100;	
	private int progress = 0;
	private TextView numberText, maxText;
	private AlertDialog  mAlertDialog  = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.http_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.http_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button getBtn  = (Button)this.findViewById(R.id.getBtn);
        Button postBtn  = (Button)this.findViewById(R.id.postBtn);
        Button byteBtn  = (Button)this.findViewById(R.id.byteBtn);
        Button fileDownBtn  = (Button)this.findViewById(R.id.fileBtn);
        Button fileUploadBtn  = (Button)this.findViewById(R.id.fileUploadBtn);
        
        //获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
        mAbHttpUtil.setDebug(true);
        //get请求
        getBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				// 一个菜谱的url地址
				String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; 
				mAbHttpUtil.get(urlString, new AbStringHttpResponseListener() {
					
					//获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, String content) {
		        		Log.d(TAG, "onSuccess");
		        		
		        		showDialog("返回结果",content,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
		        	
		        	}
		        	
		        	
		        	// 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	
		            	Log.d(TAG, "onFailure");
		            	showToast(error.getMessage());
					}

		            // 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//显示进度框
		            	showProgressDialog();
					}


					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//移除进度框
		            	removeProgressDialog();
		            };
		            
		        });
				
			}
		});
        
        //post请求
        postBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = "http://client.azrj.cn/json/cook/cook_list.jsp?";
				// 绑定参数
		        AbRequestParams params = new AbRequestParams(); 
		        params.put("type", "1");
		        params.put("p", "2");
		        params.put("size", "10");
		        mAbHttpUtil.post(url,params, new AbStringHttpResponseListener() {
		        	
		        	// 获取数据成功会调用这里
		        	@Override
		        	public void onSuccess(int statusCode, String content) {
		        		Log.d(TAG, "onSuccess");
		            	showDialog("返回结果",content,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
		            };
		            
		            // 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//显示进度框
		            	showProgressDialog();
					}
		            
		            // 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	showToast(error.getMessage());
					}

					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//移除进度框
		            	removeProgressDialog();
		            };
		            
		        });
			}
		});
        
        //字节数组下载
        byteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = "http://www.418log.org/content/templates/default/images/rand/8.jpg";
				mAbHttpUtil.get(url, new AbBinaryHttpResponseListener() {
		        	
					// 获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, byte[] content) {
		        		Log.d(TAG, "onSuccess");
		        		Bitmap bitmap = AbImageUtil.bytes2Bimap(content);
		            	ImageView view = new ImageView(HttpActivity.this);
		            	view.setImageBitmap(bitmap);
		            	
		            	showDialog("返回结果",view,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
					}
		        	
		        	// 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//显示进度框
		            	showProgressDialog();
					}

		            // 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
		            	showToast(error.getMessage());
					}

					// 完成后调用，失败，成功
		            @Override
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//移除进度框
		            	removeProgressDialog();
		            };
		            
		        });
			}
		});
        
        //文件下载
        fileDownBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String url = "http://www.418log.org/content/uploadfile/201311/38ed1385599018.jpg";
				
				mAbHttpUtil.get(url, new AbFileHttpResponseListener(url) {
		        	
					
					// 获取数据成功会调用这里
		        	@Override
					public void onSuccess(int statusCode, File file) {
		        		Log.d(TAG, "onSuccess");
		        		Bitmap bitmap = AbFileUtil.getBitmapFromSD(file);
		            	ImageView view = new ImageView(HttpActivity.this);
		            	view.setImageBitmap(bitmap);
		            	
		            	showDialog("返回结果",view,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
					}
		        	
		        	// 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//打开进度框
		            	View v = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
		            	mAbProgressBar = (AbHorizontalProgressBar) v.findViewById(R.id.horizontalProgressBar);
		            	numberText = (TextView) v.findViewById(R.id.numberText);
		        		maxText = (TextView) v.findViewById(R.id.maxText);
		        		
		        		maxText.setText(progress+"/"+String.valueOf(max));
		        		mAbProgressBar.setMax(max);
		        		mAbProgressBar.setProgress(progress);
		            	
		        		mAlertDialog = showDialog("正在下载",v);
					}

		        	// 失败，调用
		            @Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						Log.d(TAG, "onFailure");
						showToast(error.getMessage());
					}
					
					// 下载进度
					@Override
					public void onProgress(int bytesWritten, int totalSize) {
						maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
						mAbProgressBar.setProgress(bytesWritten/(totalSize/max));
					}

					// 完成后调用，失败，成功
		            public void onFinish() { 
		            	//下载完成取消进度框
		            	if(mAlertDialog!=null){
		            		mAlertDialog.cancel();
			            	mAlertDialog  = null;
		            	}
		            	
		            	Log.d(TAG, "onFinish");
		            };
		            
		        });
			}
		});
        
        //文件上传
        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//已经在后台上传
				if(mAlertDialog!=null){
					mAlertDialog.show();
					return;
				}
				String url = "http://192.168.19.78:8080/demo/addOverlayMobile.do";
				
				AbRequestParams params = new AbRequestParams(); 
				
				try {
					File pathRoot = Environment.getExternalStorageDirectory();
					String path = pathRoot.getAbsolutePath();
					params.put("data1",URLEncoder.encode("中文可处理",HTTP.UTF_8));
					params.put("data2","100");
					File file1 = new File(path+"/download/cache_files/1.jpg");
					File file2 = new File(path+"/download/cache_files/3.wmv");
					params.put(file1.getName(),file1);
					params.put(file2.getName(),file2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {

					
					@Override
					public void onSuccess(int statusCode, String content) {
						showToast("onSuccess");
					}

					// 开始执行前
		            @Override
					public void onStart() {
		            	Log.d(TAG, "onStart");
		            	//打开进度框
		            	View v = LayoutInflater.from(HttpActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
		            	mAbProgressBar = (AbHorizontalProgressBar) v.findViewById(R.id.horizontalProgressBar);
		            	numberText = (TextView) v.findViewById(R.id.numberText);
		        		maxText = (TextView) v.findViewById(R.id.maxText);
		        		
		        		maxText.setText(progress+"/"+String.valueOf(max));
		        		mAbProgressBar.setMax(max);
		        		mAbProgressBar.setProgress(progress);
		            	
		        		mAlertDialog = showDialog("正在上传",v);
					}

					@Override
					public void onFailure(int statusCode, String content,
							Throwable error) {
						showToast(error.getMessage());
					}

					// 进度
					@Override
					public void onProgress(int bytesWritten, int totalSize) {
						maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
						mAbProgressBar.setProgress(bytesWritten/(totalSize/max));
					}

					// 完成后调用，失败，成功，都要调用
		            public void onFinish() { 
		            	Log.d(TAG, "onFinish");
		            	//下载完成取消进度框
		            	if(mAlertDialog!=null){
		            	    mAlertDialog.cancel();
		            	    mAlertDialog  = null;
		            	}
		            };
					
		            
		        });
			}
		});
        
      } 

    
}
