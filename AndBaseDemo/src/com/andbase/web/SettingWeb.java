package com.andbase.web;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.ab.global.AbAppException;
import com.andbase.global.Constant;

public class SettingWeb {

	private static final String TAG = "SettingWeb";
	private static final boolean D = Constant.DEBUG;

	public static String adSetting() throws AbAppException {
		String mValue = null;
		try {
			// 使用httppost对象提交数据
			HttpPost httpRequest = new HttpPost("");
			// 超时设置
			HttpParams params = new BasicHttpParams();
			// 从连接池中取连接的超时时间，设置为1秒
			ConnManagerParams.setTimeout(params, Constant.timeOut);
			// 通过网络与服务器建立连接的超时时间
			HttpConnectionParams.setConnectionTimeout(params, Constant.connectOut);
			// 读响应数据的超时时间
			HttpConnectionParams.setSoTimeout(params, Constant.getOut);
			// 设置请求参数
			httpRequest.setParams(params);
			// 传递请求参数
			List<BasicNameValuePair> paramsList = new ArrayList<BasicNameValuePair>();
			paramsList.add(new BasicNameValuePair("key", "android_ad"));
			UrlEncodedFormEntity mUrlEncodedFormEntity = new UrlEncodedFormEntity(paramsList, HTTP.UTF_8);
			httpRequest.setEntity(mUrlEncodedFormEntity);
			HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
			int ret = httpResponse.getStatusLine().getStatusCode();
			if (ret == HttpStatus.SC_OK) {
				mValue = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				if (D) Log.d(TAG, "广告开关返回:" + mValue);
			} else {
				throw new ConnectException();
			}
		} catch (Exception e) {
			AbAppException mAbAppException = new AbAppException(e);
			throw mAbAppException;
		}
		return mValue;
	}

	
	
}
