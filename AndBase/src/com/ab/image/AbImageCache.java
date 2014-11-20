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
package com.ab.image;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.ab.image.toolbox.ImageLoader.ImageCache;

// TODO: Auto-generated Javadoc
/**
 * The Class AbImageCache.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class AbImageCache implements ImageCache {
	
	/** The m cache. */
	private static LruCache<String, Bitmap> mCache;
	
	/** The m image cache. */
	private static AbImageCache mImageCache;
	

	/**
	 * Instantiates a new ab image cache.
	 */
	public AbImageCache() {
		super();
		int maxSize = 10 * 1024 * 1024;
		mCache = new LruCache<String, Bitmap>(maxSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes() * value.getHeight();
			}
		};
	}

	/**
	 * Gets the single instance of AbImageCache.
	 *
	 * @return single instance of AbImageCache
	 */
	public static AbImageCache getInstance() {
		if(mImageCache == null){
			mImageCache = new AbImageCache();
		}
		return mImageCache;
	}

	/* (non-Javadoc)
	 * @see com.android.volley.toolbox.ImageLoader.ImageCache#getBitmap(java.lang.String)
	 */
	@Override
	public Bitmap getBitmap(String url) {
		return mCache.get(url);
	}

	/* (non-Javadoc)
	 * @see com.android.volley.toolbox.ImageLoader.ImageCache#putBitmap(java.lang.String, android.graphics.Bitmap)
	 */
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		mCache.put(url, bitmap);
	}
	
	/**
	 * Clear bitmap.
	 */
	public void clearBitmap() {
		mCache.evictAll();
	}

}
