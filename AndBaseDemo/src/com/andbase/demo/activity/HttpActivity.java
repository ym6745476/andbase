package com.andbase.demo.activity;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

import org.apache.http.protocol.HTTP;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.ab.http.AsyncHttpResponseHandler;
import com.ab.http.BinaryHttpResponseHandler;
import com.ab.http.FileAsyncHttpResponseHandler;
import com.ab.http.RequestParams;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
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
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.http_main);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.http_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleLayoutBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button getBtn  = (Button)this.findViewById(R.id.getBtn);
        Button postBtn  = (Button)this.findViewById(R.id.postBtn);
        Button byteBtn  = (Button)this.findViewById(R.id.byteBtn);
        Button fileDownBtn  = (Button)this.findViewById(R.id.fileBtn);
        Button fileUploadBtn  = (Button)this.findViewById(R.id.fileUploadBtn);
        
        //get请求
        getBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				showProgressDialog();
				// 一個獲取菜谱的url地址
				String urlString = "http://client.azrj.cn/json/cook/cook_list.jsp?type=1&p=2&size=10"; 
				AbHttpUtil.get(urlString, new AsyncHttpResponseHandler() {
		        	
		        	// 获取数据成功会调用这里
		            public void onSuccess(String content) {
		            	
		            	showDialog("返回结果",content,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
		            };
		            
		            // 失败，调用
		            public void onFailure(Throwable arg0) { 
		            	showToast("onFailure");
		            };
		            
		            // 完成后调用，失败，成功，都要掉
		            public void onFinish() { 
		            	removeProgressDialog();
		            };
		            
		        });
				
			}
		});
        
        //post请求
        postBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgressDialog();
				String url = "http://client.azrj.cn/json/cook/cook_list.jsp?";
				// 绑定参数
		        RequestParams params = new RequestParams(); 
		        params.put("type", "1");
		        params.put("p", "2");
		        params.put("size", "10");
		        AbHttpUtil.post(url,params, new AsyncHttpResponseHandler() {
		        	
		        	// 获取数据成功会调用这里
		            public void onSuccess(String content) {
		            	
		            	showDialog("返回结果",content,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
		            };
		            
		            // 失败，调用
		            public void onFailure(Throwable arg0) { 
		            	showToast("onFailure");
		            };
		            
		            // 完成后调用，失败，成功，都要掉
		            public void onFinish() { 
		            	removeProgressDialog();
		            };
		            
		        });
			}
		});
        
        //字节数组下载
        byteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgressDialog();
				String url = "http://f.hiphotos.baidu.com/album/w%3D2048/sign=38c43ff7902397ddd6799f046dbab3b7/9c16fdfaaf51f3dee973bf7495eef01f3b2979d8.jpg";
				AbHttpUtil.get(url, new BinaryHttpResponseHandler() {
		        	
		        	// 获取数据成功会调用这里
		            public void onSuccess(byte[] imgByte) {
		            	
		            	Bitmap bitmap = AbImageUtil.bytes2Bimap(imgByte);
		            	ImageView view = new ImageView(HttpActivity.this);
		            	view.setImageBitmap(bitmap);
		            	
		            	showDialog("返回结果",view,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
		            };
		            
		            // 失败，调用
		            public void onFailure(Throwable arg0) { 
		            	showToast("onFailure");
		            };
		            
		            // 完成后调用，失败，成功，都要掉
		            public void onFinish() { 
		            	removeProgressDialog();
		            };
		            
		        });
			}
		});
        
        //文件下载
        fileDownBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgressDialog();
				String url = "http://f.hiphotos.baidu.com/album/w%3D2048/sign=38c43ff7902397ddd6799f046dbab3b7/9c16fdfaaf51f3dee973bf7495eef01f3b2979d8.jpg";
				
				AbHttpUtil.get(url, new FileAsyncHttpResponseHandler(url) {
		        	
		        	// 获取数据成功会调用这里
		            public void onSuccess(File file) {
		            	
		            	Bitmap bitmap = AbFileUtil.getBitmapFromSD(file);
		            	ImageView view = new ImageView(HttpActivity.this);
		            	view.setImageBitmap(bitmap);
		            	
		            	showDialog("返回结果",view,new OnClickListener(){

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
							}
		            		
		            	});
		            };
		            
		            // 失败，调用
		            public void onFailure(Throwable arg0) { 
		            	showToast("onFailure");
		            };
		            
		            // 完成后调用，失败，成功，都要掉
		            public void onFinish() { 
		            	removeProgressDialog();
		            };
		            
		        });
			}
		});
        
        //文件上传
        fileUploadBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showProgressDialog();
				String url = "http://192.168.19.78:8080/demo/addOverlayMobile.do";
				HashMap<String, String> params = new HashMap<String, String>();
				HashMap<String, File> files = new HashMap<String, File>();
				try {
					File pathRoot = Environment.getExternalStorageDirectory();
					String path = pathRoot.getAbsolutePath();
					params.put("data1",URLEncoder.encode("中文可处理",HTTP.UTF_8));
					params.put("data2","100");
					File file1 = new File(path+"/download/cache_files/1.jpg");
					File file2 = new File(path+"/download/cache_files/2.jpg");
					files.put(file1.getName(),file1);
					files.put(file2.getName(),file2);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				AbHttpUtil.post(url, params, files, new AsyncHttpResponseHandler() {
		        	
		        	// 获取数据成功会调用这里
		            public void onSuccess(String content) {
		            	showToast(content);
		            };
		            
		            // 失败，调用
		            public void onFailure(Throwable e) { 
		            	showToast(e.getMessage());
		            };
		            
		            // 完成后调用，失败，成功，都要掉
		            public void onFinish() { 
		            	removeProgressDialog();
		            };
		            
		        });
			}
		});
        
      } 
    
    
}
