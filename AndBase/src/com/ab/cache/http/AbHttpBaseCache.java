package com.ab.cache.http;


public class AbHttpBaseCache {
	
	/** AbHttpBaseCache 实例. */
	private static AbHttpBaseCache mHttpCache;
	
	/**
	 * 构造方法.
	 */
	public AbHttpBaseCache() {
		super();
	}
	
	/**
	 * 
	 * 获取单例的AbHttpBaseCache.
	 * @return
	 */
	public static AbHttpBaseCache getInstance() {
		if(mHttpCache == null){
			mHttpCache = new AbHttpBaseCache();
		}
		return mHttpCache;
	}
	
	/**
	 * 获取用于缓存的Key.
	 * @param url
	 * @return
	 */
    public String getCacheKey(String url) {
        return new StringBuilder(url.length()).append(url).toString();
    }

}
