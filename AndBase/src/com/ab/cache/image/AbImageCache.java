/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ab.cache.image;

import android.graphics.Bitmap;

// TODO: Auto-generated Javadoc
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbImageCache.java 
 * 描述：图片缓存接口
 * @author 还如一梦中
 * @date 2015年4月3日 上午9:51:36
 * @version v1.0
 */
public interface AbImageCache {
	
	/**
	 * Gets the bitmap.
	 *
	 * @param cacheKey the cache key
	 * @return the bitmap
	 */
	public Bitmap getBitmap(String cacheKey);

	/**
	 * Put bitmap.
	 *
	 * @param cacheKey the cache key
	 * @param bitmap the bitmap
	 */
	public void putBitmap(String cacheKey, Bitmap bitmap);

	/**
	 * Gets the cache key.
	 *
	 * @param requestUrl the request url
	 * @param maxWidth the max width
	 * @param maxHeight the max height
	 * @return the cache key
	 */
	public String getCacheKey(String requestUrl, int maxWidth, int maxHeight);

	/**
	 * Removes the bitmap.
	 *
	 * @param requestUrl the request url
	 * @param maxWidth the max width
	 * @param maxHeight the max height
	 */
	public void removeBitmap(String requestUrl, int maxWidth, int maxHeight);
}
