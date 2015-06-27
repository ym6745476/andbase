package com.andbase.web;

import java.util.List;

import android.content.Context;

import com.ab.http.AbHttpListener;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.model.AbResult;
import com.ab.util.AbFileUtil;
import com.ab.util.AbJsonUtil;
import com.andbase.demo.model.Article;
import com.andbase.demo.model.ArticleListResult;

public class NetworkWeb {

	private AbHttpUtil mAbHttpUtil = null;
	private Context mContext = null;
	
	public NetworkWeb(Context context) {
		// 创建Http工具类
		mContext = context;
		mAbHttpUtil = AbHttpUtil.getInstance(context);
	}

	/**
	 * Create a new instance of SettingWeb.
	 */
	public static NetworkWeb newInstance(Context context) {
		NetworkWeb web = new NetworkWeb(context);
		return web;
	}

	
	
	/**
	 * 调用一个列表请求
	 * @param AbRequestParams  参数列表
	 * @param abHttpListener 请求的监听器
	 */
	public void findLogList(AbRequestParams params,final AbHttpListener abHttpListener){
		
		
		
	}

	
	
}
