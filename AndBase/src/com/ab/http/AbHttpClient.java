package com.ab.http;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.ab.global.AbAppException;
import com.ab.global.AbConstant;
import com.ab.task.AbTaskPool;
import com.ab.util.AbAppUtil;
import com.ab.util.AbFileUtil;

public class AbHttpClient {
	
	/** The Constant TAG. */
    private static final String TAG = "AbHttpClient";
    
    /** The m context. */
	private Context mContext;
	
	/** 连接池. */
    private static ExecutorService executorService = null; 
    
    /** The Constant DEFAULT_MAX_CONNECTIONS. */
    private static final int DEFAULT_MAX_CONNECTIONS = 10;
    
    /** The Constant DEFAULT_SOCKET_TIMEOUT. */
    private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
    
    /** The Constant DEFAULT_MAX_RETRIES. */
    private static final int DEFAULT_MAX_RETRIES = 5;
    
    /** The Constant DEFAULT_SOCKET_BUFFER_SIZE. */
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    
    /** The Constant BUFFER_SIZE. */
    private static final int BUFFER_SIZE = 4096;
    
    /** The Constant SUCCESS_MESSAGE. */
    protected static final int SUCCESS_MESSAGE = 0;
    
    /** The Constant FAILURE_MESSAGE. */
    protected static final int FAILURE_MESSAGE = 1;
    
    /** The Constant START_MESSAGE. */
    protected static final int START_MESSAGE = 2;
    
    /** The Constant FINISH_MESSAGE. */
    protected static final int FINISH_MESSAGE = 3;
    
    /** The Constant PROGRESS_MESSAGE. */
    protected static final int PROGRESS_MESSAGE = 4;
    
    /** The Constant RETRY_MESSAGE. */
    protected static final int RETRY_MESSAGE = 5;
    
    /**超时时间*/
	private int timeout = DEFAULT_SOCKET_TIMEOUT;
	
	/**debug true表示是内网*/
	private boolean debug = false;
    
	
	public AbHttpClient(Context context) {
		executorService =  AbTaskPool.getExecutorService();
		mContext = context;
	}
	
	
	/**
	 * 
	 * 描述：无参数的get请求
	 * 
	 * @param url
	 * @param responseHandler
	 * @throws
	 */
	public void get(String url, AbHttpResponseListener responseListener) {
		get(url,null,responseListener);          
	}

	/**
	 * 
	 * 描述：带参数的get请求
	 * 
	 * @param url
	 * @param responseHandler
	 * @throws
	 */
	public void get(final String url,final AbRequestParams params,final AbHttpResponseListener responseListener) {
		
		responseListener.setHandler(new ResponderHandler(responseListener));
		executorService.submit(new Runnable() { 
    		public void run() {
    			try {
    				doGet(url,params,responseListener);
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}
    		}                 
    	});                
	}
	
	/**
	 * 
	 * 描述：无参数的post请求
	 * 
	 * @param url
	 * @param responseHandler
	 * @throws
	 */
	public void post(String url, AbHttpResponseListener responseListener) {
		post(url,null,responseListener);          
	}
	
	/**
	 * 
	 * 描述：带参数的post请求
	 * @param url
	 * @param params
	 * @param responseHandler
	 * @throws 
	 */
	public void post(final String url,final AbRequestParams params,
			final AbHttpResponseListener responseListener) {
		responseListener.setHandler(new ResponderHandler(responseListener));
		executorService.submit(new Runnable() { 
    		public void run() {
    			try {
    				doPost(url,params,responseListener);
    			} catch (Exception e) { 
    				e.printStackTrace();
    			}
    		}                 
    	});      
	}
	
	
	/**
	 * 
	 * 描述：执行get请求
	 * @param url
	 * @param params
	 * @param responseListener
	 * @throws 
	 */
	private void doGet(String url,AbRequestParams params,AbHttpResponseListener responseListener){
		  try {
			  
			  responseListener.sendStartMessage();
			  
			  if(!debug && !AbAppUtil.isNetworkAvailable(mContext)){
					responseListener.sendFailureMessage(AbConstant.CONNECT_FAILURE_CODE,AbConstant.CONNECTEXCEPTION, new AbAppException(AbConstant.CONNECTEXCEPTION));
			        return;
			  }
			  
			  //HttpGet连接对象  
			  if(params!=null){
				  url += params.getParamString();
			  }
			  HttpGet httpRequest = new HttpGet(url);  
			  
			  BasicHttpParams httpParams = new BasicHttpParams();
			  
			  // 从连接池中取连接的超时时间，设置为1秒
		      ConnManagerParams.setTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		      ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		      ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
		      // 读响应数据的超时时间
		      HttpConnectionParams.setSoTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		      HttpConnectionParams.setConnectionTimeout(httpParams, DEFAULT_SOCKET_TIMEOUT);
		      HttpConnectionParams.setTcpNoDelay(httpParams, true);
		      HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

		      HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		      HttpProtocolParams.setUserAgent(httpParams, String.format("andbase-http/%s (http://www.418log.org/)", 1.0));
		      // 设置请求参数
			  httpRequest.setParams(httpParams);
			  
			  //取得HttpClient对象  
			  HttpClient httpClient = new DefaultHttpClient();  
			  //请求HttpClient，取得HttpResponse  
			  HttpResponse httpResponse = httpClient.execute(httpRequest);  
			  //请求成功  
			  int statusCode = httpResponse.getStatusLine().getStatusCode();
			  
			  //取得返回的字符串  
			  HttpEntity  mHttpEntity = httpResponse.getEntity();
			  if (statusCode == HttpStatus.SC_OK){  
				  if(responseListener instanceof AbStringHttpResponseListener){
					  String content = EntityUtils.toString(mHttpEntity);
					  ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(statusCode, content);
				  }else if(responseListener instanceof AbBinaryHttpResponseListener){
					  readResponseData(mHttpEntity,((AbBinaryHttpResponseListener)responseListener));
				  }else if(responseListener instanceof AbFileHttpResponseListener){
					  //获取文件名
					  String fileName = AbFileUtil.getFileNameFromUrl(url, httpResponse);
					  writeResponseData(mHttpEntity,fileName,((AbFileHttpResponseListener)responseListener));
				  }
			  }else{
				  String content = EntityUtils.toString(mHttpEntity);
				  responseListener.sendFailureMessage(statusCode, content, new AbAppException(AbConstant.UNKNOWNHOSTEXCEPTION));
			  }
		} catch (Exception e) {
			e.printStackTrace();
			//发送失败消息
			responseListener.sendFailureMessage(AbConstant.UNTREATED_CODE,e.getMessage(),new AbAppException(e));
		}finally{
			responseListener.sendFinishMessage();
		}
	}
	
	/**
	 * 
	 * 描述：执行post请求
	 * @param url
	 * @param params
	 * @param responseListener
	 * @throws 
	 */
	private void doPost(String url,AbRequestParams params,AbHttpResponseListener responseListener){
		  try {
			  responseListener.sendStartMessage();
			  
			  if(!debug && !AbAppUtil.isNetworkAvailable(mContext)){
					responseListener.sendFailureMessage(AbConstant.CONNECT_FAILURE_CODE,AbConstant.CONNECTEXCEPTION, new AbAppException(AbConstant.CONNECTEXCEPTION));
			        return;
			  }
			  
			  //HttpPost连接对象  
		      HttpPost httpRequest = new HttpPost(url);  
		      if(params != null){
		    	  //使用NameValuePair来保存要传递的Post参数设置字符集 
			      HttpEntity httpentity = params.getEntity(responseListener);
			      //请求httpRequest  
			      httpRequest.setEntity(httpentity); 
			  }
		      
              BasicHttpParams httpParams = new BasicHttpParams();
			  
			  // 从连接池中取连接的超时时间，设置为1秒
		      ConnManagerParams.setTimeout(httpParams, timeout);
		      ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new ConnPerRouteBean(DEFAULT_MAX_CONNECTIONS));
		      ConnManagerParams.setMaxTotalConnections(httpParams, DEFAULT_MAX_CONNECTIONS);
		      // 读响应数据的超时时间
		      HttpConnectionParams.setSoTimeout(httpParams, timeout);
		      HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
		      HttpConnectionParams.setTcpNoDelay(httpParams, true);
		      HttpConnectionParams.setSocketBufferSize(httpParams, DEFAULT_SOCKET_BUFFER_SIZE);

		      HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
		      HttpProtocolParams.setUserAgent(httpParams, String.format("andbase-http/%s (http://www.418log.org/)", 1.0));
		      // 设置请求参数
			  httpRequest.setParams(httpParams);
	    	  
		      //取得默认的HttpClient
		      HttpClient httpclient = new DefaultHttpClient();  
		      //取得HttpResponse
		      HttpResponse httpResponse = httpclient.execute(httpRequest);  
			  //请求成功  
			  int statusCode = httpResponse.getStatusLine().getStatusCode();
			  //取得返回的字符串  
			  HttpEntity  mHttpEntity = httpResponse.getEntity();
			  if (statusCode == HttpStatus.SC_OK){  
				  if(responseListener instanceof AbStringHttpResponseListener){
					  String content = EntityUtils.toString(mHttpEntity);
					  ((AbStringHttpResponseListener)responseListener).sendSuccessMessage(statusCode, content);
				  }else if(responseListener instanceof AbBinaryHttpResponseListener){
					  readResponseData(mHttpEntity,((AbBinaryHttpResponseListener)responseListener));
				  }else if(responseListener instanceof AbFileHttpResponseListener){
					  //获取文件名
					  String fileName = AbFileUtil.getFileNameFromUrl(url, httpResponse);
					  writeResponseData(mHttpEntity,fileName,((AbFileHttpResponseListener)responseListener));
				  }
			  }else{
				  String content = EntityUtils.toString(mHttpEntity);
				  responseListener.sendFailureMessage(statusCode, content, new AbAppException(AbConstant.UNKNOWNHOSTEXCEPTION));
			  }
			  
		} catch (Exception e) {
			e.printStackTrace();
			//发送失败消息
			responseListener.sendFailureMessage(AbConstant.UNTREATED_CODE,e.getMessage(),new AbAppException(e));
		}finally{
			responseListener.sendFinishMessage();
		}
	}
	
	
	/**
     * 描述：写入文件并回调进度
     */
    public void writeResponseData(HttpEntity entity,String name,AbFileHttpResponseListener responseListener){
        
    	if(entity == null){
        	return;
        }
    	
    	if(responseListener.getFile() == null){
    		//创建缓存文件
    		responseListener.setFile(name);
        }
    	
    	InputStream inStream = null;
    	FileOutputStream outStream = null;
        try {
	        inStream = entity.getContent();
	        long contentLength = entity.getContentLength();
	        outStream = new FileOutputStream(responseListener.getFile());
	        if (inStream != null) {
	          
	              byte[] tmp = new byte[BUFFER_SIZE];
	              int l, count = 0;
	              while ((l = inStream.read(tmp)) != -1 && !Thread.currentThread().isInterrupted()) {
	                  count += l;
	                  outStream.write(tmp, 0, l);
	                  responseListener.sendProgressMessage(count, (int) contentLength);
	              }
	        }
	        responseListener.sendSuccessMessage(200);
	    }catch(Exception e){
	        e.printStackTrace();
	        //发送失败消息
			responseListener.sendFailureMessage(AbConstant.RESPONSE_TIMEOUT_CODE,AbConstant.SOCKETTIMEOUTEXCEPTION,e);
	    } finally {
        	try {
        		if(inStream!=null){
        			inStream.close();
        		}
        		if(outStream!=null){
        			outStream.flush();
    				outStream.close();
        		}
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        
        
    }
    
    /**
     * 描述：转换为二进制并回调进度
     */
    public void readResponseData(HttpEntity entity,AbBinaryHttpResponseListener responseListener){
        
    	if(entity == null){
        	return;
        }
    	
        InputStream inStream = null;
        ByteArrayOutputStream outSteam = null; 
       
    	try {
    		inStream = entity.getContent();
			outSteam = new ByteArrayOutputStream(); 
			long contentLength = entity.getContentLength();
			if (inStream != null) {
				  int l, count = 0;
				  byte[] tmp = new byte[BUFFER_SIZE];
				  while((l = inStream.read(tmp))!=-1){ 
					  count += l;
			          outSteam.write(tmp,0,l); 
			          responseListener.sendProgressMessage(count, (int) contentLength);

			     } 
			  }
			 responseListener.sendSuccessMessage(200,outSteam.toByteArray());
		} catch (Exception e) {
			e.printStackTrace();
			//发送失败消息
			responseListener.sendFailureMessage(AbConstant.RESPONSE_TIMEOUT_CODE,AbConstant.SOCKETTIMEOUTEXCEPTION,e);
		}finally{
			try {
				if(inStream!=null){
					inStream.close();
				}
				if(outSteam!=null){
					outSteam.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
    	
        
    }


    
    /**
	 * 
	 * 描述：设置连接超时时间
	 * @param timeout 毫秒
	 * @throws 
	 */
    public void setTimeout(int timeout) {
    	this.timeout = timeout;
	}
    
    
    /**
	 * 
	 * 描述：调试模式
	 */
    public boolean isDebug() {
		return debug;
	}

    /**
	 * 
	 * 描述：是否为调试模式
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	/**
     * 
     * Copyright (c) 2012 All rights reserved
     * 名称：ResponderHandler.java 
     * 描述：请求返回
     * @author zhaoqp
     * @date：2013-11-13 下午3:22:30
     * @version v1.0
     */
    private static class ResponderHandler extends Handler {
		
		private Object[] response;
		private AbHttpResponseListener responseListener;
		

		public ResponderHandler(AbHttpResponseListener responseListener) {
			this.responseListener = responseListener;
		}

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			
			case SUCCESS_MESSAGE:
				response = (Object[]) msg.obj;
				
				if (response != null){
					if(responseListener instanceof AbStringHttpResponseListener){
						if(response.length >= 2){
							((AbStringHttpResponseListener)responseListener).onSuccess((Integer) response[0],(String)response[1]);
						}else{
							Log.e(TAG, "SUCCESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
						}
						
					}else if(responseListener instanceof AbBinaryHttpResponseListener){
						if(response.length >= 2){ 
						    ((AbBinaryHttpResponseListener)responseListener).onSuccess((Integer) response[0],(byte[])response[1]);
						}else{
							Log.e(TAG, "SUCCESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
						}
					}else if(responseListener instanceof AbFileHttpResponseListener){
						
						if(response.length >= 1){ 
							AbFileHttpResponseListener mAbFileHttpResponseListener = ((AbFileHttpResponseListener)responseListener);
							mAbFileHttpResponseListener.onSuccess((Integer) response[0],mAbFileHttpResponseListener.getFile());
						}else{
							Log.e(TAG, "SUCCESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
						}
						
					}
				}
                break;
			case FAILURE_MESSAGE:
				 response = (Object[]) msg.obj;
                 if (response != null && response.length >= 3){
                	 //异常转换为可提示的
                	 AbAppException exception = new AbAppException((Exception) response[2]);
					 responseListener.onFailure((Integer) response[0], (String) response[1], exception);
                 }else{
                    Log.e(TAG, "FAILURE_MESSAGE "+AbConstant.MISSINGPARAMETERS);
                 }
	             break;
			case START_MESSAGE:
				responseListener.onStart();
				break;
			case FINISH_MESSAGE:
				responseListener.onFinish();
				break;
			case PROGRESS_MESSAGE:
				 response = (Object[]) msg.obj;
	             if (response != null && response.length >= 2){
	            	 responseListener.onProgress((Integer) response[0], (Integer) response[1]);
			     }else{
	                 Log.e(TAG, "PROGRESS_MESSAGE "+AbConstant.MISSINGPARAMETERS);
			     }
	             break;
			case RETRY_MESSAGE:
				responseListener.onRetry();
				break;
			default:
				break;
		   }
		}
		
	}
	
}
