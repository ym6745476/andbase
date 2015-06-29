package com.andbase.global;



import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class LocationProvider {

	private Context context;
	private LocationClient locationClient = null;
	private MyBDListener bDListener = new MyBDListener();
	private LocationListener listener;

	public LocationProvider(Context context) {
		super();
		this.context = context;
	}

	public void startLocation() {
		locationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		// 设置定位模式
		option.setLocationMode(LocationMode.Hight_Accuracy);
		// 返回的定位结果是百度经纬度，默认值gcj02
		option.setCoorType("gcj02");
		int span = 1000;
		span = Integer.valueOf(5000);
		// 设置发起定位请求的间隔时间为5000ms
		option.setScanSpan(span);
		option.setIsNeedAddress(true);
		locationClient.setLocOption(option);
		locationClient.registerLocationListener(bDListener);
		// 将开启与获取位置分开，就可以尽量的在后面的使用中获取到位置
		locationClient.start();
	}

	/**
	 * 停止，减少资源消耗
	 */
	public void stopListener() {
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}

	/**
	 * 更新位置并保存到SItude中
	 */
	public void updateListener() {
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.requestLocation();
		}
	}

	public LocationListener getListener() {
		return listener;
	}

	public void setListener(LocationListener listener) {
		this.listener = listener;
	}

	private class MyBDListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			
			if (location.getCity() == null) {
				updateListener();
			} else {
				if (listener != null) {
					listener.onReceiveLocation(location);
					stopListener();
				}
			}
		}
	}

	public interface LocationListener {
		public void onReceiveLocation(BDLocation location);
	}
}
