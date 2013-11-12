package com.andbase.friend;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ab.activity.AbActivity;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;
import com.baidu.frontia.Frontia;
import com.baidu.frontia.FrontiaACL;
import com.baidu.frontia.FrontiaAccount;
import com.baidu.frontia.FrontiaData;
import com.baidu.frontia.FrontiaQuery;
import com.baidu.frontia.api.FrontiaPush;
import com.baidu.frontia.api.FrontiaStorage;
import com.baidu.frontia.api.FrontiaStorageListener.DataInfoListener;
import com.baidu.frontia.api.FrontiaStorageListener.DataInsertListener;

public class PushUtil {
	
	  private static final String TAG = "PushUtil";
	  private static final boolean D = Constant.DEBUG;
	  //应用数据存储
	  private static FrontiaStorage mCloudStorage = Frontia.getStorage();
	  private static MyApplication application = null;
	
	  /**
	    * 
	    * 描述：推送服务
	    * @throws 
	    */
	   public static void startPushService(final FrontiaPush mPush,final AbActivity activity){
		   application = (MyApplication)activity.abApplication;
		   if(application.mUser == null || application.mUser.getAccessToken() == null){
			   return;
		   }
	       if(!mPush.isPushWorking()){
	    	   Log.d(TAG, "推送服务启动");
	    	   mPush.start(application.mUser.getAccessToken());
	       }
	       
	   }   
	   
	   
	   public static void saveChannelId(String appid,String userId,String channelId,String requestId){
		   if(application==null || application.mUser == null || application.mUser.getAccessToken() == null){
			   return;
		   }
		   
		   queryByUserId(appid,userId,channelId,requestId);
	   }
	   
	   /**
	    * 
	    * 描述：保存通道才能被其他用户查询
	    * @param mPushAppId
	    * @param mUserId
	    * @param mPushUserId
	    * @param mPushChannelId
	    * @param handler
	    * @throws 
	    */
	   public static void saveData(final String appid,final String userId,final String channelId,final String requestId){
		   
		   FrontiaAccount user = Frontia.getCurrentAccount();
		   /**FrontiaACL acl = new FrontiaACL();
		   acl.setAccountReadable(user, true);
		   acl.setAccountWritable(user, true);
		   acl.setPublicReadable(true);*/
	        
		   //对所有Frontia账户可读写
	       FrontiaACL acl = new FrontiaACL();
	       acl.setPublicReadable(true);
	       acl.setAccountWritable(user,true);
		   
		   JSONObject data = new JSONObject();
	       try {
	    	    data.put("push_user_id",userId);
	    	    data.put("push_app_id",appid);
	        	data.put("push_local_user_id",application.mUser.getuId());
	        	//在推送服务启动后需要设置进去，用于发消息,自己并不需要其他人才需要
	        	data.put("push_channel_id",channelId);
	        } catch (JSONException e) {
	            e.printStackTrace();
	        }
	   		FrontiaData newData = new FrontiaData(data);
	   		newData.setACL(acl);
	   		Log.d(TAG, "开始保存channel数据："+data.toString());
	   		//存储数据
	   		mCloudStorage.insertData(newData,new DataInsertListener() {
	   		    @Override
	   		    public void onSuccess() {
	   		        Log.d(TAG, "channel数据已保存");
	   		        //queryAll();
	   		        
	   		    }

	   		    @Override
	   		    public void onFailure(int errCode, String errMsg) {
	   		    	Log.d(TAG, "channel数据保存错误为"+errCode+errMsg);
	   		    }
	     	});
	   		
	   }
	   
	   
	   public static void queryAll(){
           FrontiaQuery query = new FrontiaQuery();
           mCloudStorage.findData(query, new DataInfoListener() {
               @Override
               public void onSuccess(List<FrontiaData> dataList) {
               	   Log.d(TAG, "查询云端所有数据："+dataList);
               }

               @Override
               public void onFailure(int errCode, String errMsg) {
               	    Log.d(TAG, "errCode: " + errCode+ ", errMsg: " + errMsg);
               }
           });
	   }
	   
	   public static void queryByUserId(final String appid,final String userId,final String channelId,final String requestId){
		   
		   Log.d(TAG, "开始查询channel数据...");
		   
		   //请大家不要用我的key调用删除方法，谢谢了，千万小心，尽量换成自己的key
		   /*FrontiaQuery query1 = new FrontiaQuery();
		   Log.d(TAG, "删除所有数据");
		   mCloudStorage.deleteData(query1, null);*/
		   
		   FrontiaQuery query = new FrontiaQuery();
           query = query.equals("push_user_id",userId);
           mCloudStorage.findData(query, new DataInfoListener() {
               @Override
               public void onSuccess(List<FrontiaData> dataList) {
	               	Log.d(TAG, "查询channel数据(push_user_id="+userId+")："+dataList);
	               	
	               	if(dataList!=null && dataList.size()>0){
	               		Log.d(TAG, "存在channel数据，不需要保存");
		               	
	               	}else{
	               		saveData(appid,userId,channelId,requestId);
	               	}
               }

               @Override
               public void onFailure(int errCode, String errMsg) {
               	    Log.d(TAG, "查询channel数据errCode: " + errCode+ ", errMsg: " + errMsg);
               }
           });
	   }
	   
	   /**
	    * 
	    * 描述：删除channel数据
	    * @param mUserId
	    * @throws 
	    */
	   public static void deleteByUserId(String mUserId){
		   //先删除以前的
		   FrontiaQuery query = new FrontiaQuery();
		   query = query.equals("push_user_id", mUserId);
		   Log.d(TAG, "删除channel数据：push_user_id＝"+mUserId);
		   mCloudStorage.deleteData(query, null);
	   }
	   
}
