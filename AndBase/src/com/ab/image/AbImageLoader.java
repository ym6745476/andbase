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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;

import com.ab.global.AbAppConfig;
import com.ab.image.toolbox.ImageLoader;
import com.ab.image.toolbox.ImageLoader.ImageContainer;
import com.ab.image.toolbox.ImageLoader.ImageListener;
import com.ab.network.toolbox.RequestQueue;
import com.ab.network.toolbox.Volley;
import com.ab.network.toolbox.VolleyError;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbImageLoader.java
 * 描述：下载图片并显示的工具类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2011-12-10 上午10:10:53
 */
public class AbImageLoader { 
	
    /** Context. */
    private Context mContext = null;
    
    /** 显示的图片的宽. */
    private int maxWidth;
	
	/** 显示的图片的高. */
    private int maxHeight;
    
    /** 缓存超时时间设置. */
    private int expiresTime;
    
    /** 显示为下载中的图片. */
    private Drawable loadingImage;
    
    /** 显示为下载中的View. */
    private View loadingView;
    
    /** 显示下载失败的图片. */
    private Drawable errorImage;
    
    /** 图片未找到的图片. */
    private Drawable emptyImage;
    
    /** The m queue. */
    private RequestQueue mQueue;
    
    /** The m image loader. */
    private ImageLoader mImageLoader = null;
    
    /** The m on image listener. */
    private OnImageListener mOnImageListener = null;
    
    /**
     * 构造图片下载器.
     *
     * @param context the context
     */
    public AbImageLoader(Context context) {
    	this.mContext = context;
    	this.expiresTime = AbAppConfig.IMAGE_CACHE_EXPIRES_TIME;
    	mQueue = Volley.newRequestQueue(context);
    	mImageLoader = new ImageLoader(mQueue, AbImageCache.getInstance());
    	mImageLoader.setExpiresTime(expiresTime);
    } 
    
    
	/**
	 * New instance.
	 *
	 * @param context the context
	 * @return the ab image downloader
	 */
	public static AbImageLoader newInstance(Context context) {
		AbImageLoader imageDownloader = new AbImageLoader(context);
		return imageDownloader;
	}
	
     
    /**
     * 显示这个图片.
     *
     * @param imageView 显得的View
     * @param url the url
     */
    public void display(final ImageView imageView,String url) { 
    	
    	if(AbStrUtil.isEmpty(url)){
    		if(emptyImage != null){
    			if(loadingView != null){
        			loadingView.setVisibility(View.INVISIBLE);
        		}
    			imageView.setVisibility(View.VISIBLE);
    			imageView.setImageDrawable(emptyImage);
    		}
    		return;
    	}
    	
    	//显示加载中
    	if(loadingView!=null){
			loadingView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
		}else {
			if(loadingImage != null){
			   imageView.setImageDrawable(loadingImage);
			   imageView.setVisibility(View.VISIBLE);
			}
		}
    	
    	//设置标记,目的解决闪烁问题
        imageView.setTag(url);
    	
        mImageLoader.get(url,new ImageListener() {
        	
            @Override
            public void onErrorResponse(VolleyError error) {
            	if(errorImage != null){
        			imageView.setImageDrawable(errorImage);
        		}
            	imageView.setVisibility(View.VISIBLE);
            	if(loadingView != null){
        			loadingView.setVisibility(View.INVISIBLE);
        		}
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
            	
            	Bitmap bitmap = response.getBitmap();
            	if(mOnImageListener!=null){
            		mOnImageListener.onResponse(bitmap);
            	}
            	AbLogUtil.d(AbImageLoader.class, "获取到图片："+bitmap);
            	//要判断这个imageView的url有变化，如果没有变化才set
                //有变化就取消，解决列表的重复利用View的问题
            	if(!response.getRequestUrl().equals(imageView.getTag())){
            		return;
            	}
            	if (bitmap != null) {
            		imageView.setImageBitmap(bitmap);
                } else {
                	if (emptyImage != null) {
                	   imageView.setImageDrawable(emptyImage);
                    }
                }
            	
            	if(loadingView != null){
        			loadingView.setVisibility(View.INVISIBLE);
        		}
            	
            	imageView.setVisibility(View.VISIBLE);
            }
        },maxWidth,maxHeight);
    	
    } 
    
    /**
     * 下载这个图片.
     *
     * @param url the url
     */
    public void download(String url) { 
    	
    	if(AbStrUtil.isEmpty(url)){
    		return;
    	}
    	
    	if(Uri.parse(url).getHost() == null){
    		return;
    	}
    	
        mImageLoader.get(url,new ImageListener() {
        	
            @Override
            public void onErrorResponse(VolleyError error) {
            }

            @Override
            public void onResponse(ImageContainer response, boolean isImmediate) {
            	
            	Bitmap bitmap = response.getBitmap();
            	if(mOnImageListener!=null){
            		mOnImageListener.onResponse(bitmap);
            	}
            	AbLogUtil.d(AbImageLoader.class, "获取到图片："+bitmap);
            }
        },maxWidth,maxHeight);
    	
    } 
   
   
   /**
    * 描述：设置下载中的图片.
    *
    * @param resID the new loading image
    */
	public void setLoadingImage(int resID) {
		this.loadingImage = mContext.getResources().getDrawable(resID);
	}
	
	/**
	 * 描述：设置下载中的View，优先级高于setLoadingImage.
	 *
	 * @param view 放在ImageView的上边或者下边的View
	 */
	public void setLoadingView(View view) {
		this.loadingView = view;
	}

	/**
	 * 描述：设置下载失败的图片.
	 *
	 * @param resID the new error image
	 */
	public void setErrorImage(int resID) {
		this.errorImage = mContext.getResources().getDrawable(resID);
	}

	/**
	 * 描述：设置未找到的图片.
	 *
	 * @param resID the new empty image
	 */
	public void setEmptyImage(int resID) {
		this.emptyImage = mContext.getResources().getDrawable(resID);
	}


	/**
	 * Gets the max width.
	 *
	 * @return the max width
	 */
	public int getMaxWidth() {
		return maxWidth;
	}

	/**
	 * Sets the max width.
	 *
	 * @param maxWidth the new max width
	 */
	public void setMaxWidth(int maxWidth) {
		this.maxWidth = maxWidth;
	}

	/**
	 * Gets the max height.
	 *
	 * @return the max height
	 */
	public int getMaxHeight() {
		return maxHeight;
	}

	/**
	 * Sets the max height.
	 *
	 * @param maxHeight the new max height
	 */
	public void setMaxHeight(int maxHeight) {
		this.maxHeight = maxHeight;
	}


	/**
	 * Gets the expires time.
	 *
	 * @return the expires time
	 */
	public int getExpiresTime() {
		return expiresTime;
	}


	/**
	 * Sets the expires time.
	 *
	 * @param expiresTime the new expires time
	 */
	public void setExpiresTime(int expiresTime) {
		this.expiresTime = expiresTime;
	}
	
	/**
	 * The listener interface for receiving onImage events.
	 * The class that is interested in processing a onImage
	 * event implements this interface, and the object created
	 * with that class is registered with a component using the
	 * component's <code>addOnImageListener<code> method. When
	 * the onImage event occurs, that object's appropriate
	 * method is invoked.
	 *
	 * @see OnImageEvent
	 */
	public interface OnImageListener {
		
		/**
		 * On response.
		 *
		 * @param bitmap the bitmap
		 */
		public void onResponse(Bitmap bitmap);
	}

	/**
	 * Gets the on image listener.
	 *
	 * @return the on image listener
	 */
	public OnImageListener getOnImageListener() {
		return mOnImageListener;
	}


	/**
	 * Sets the on image listener.
	 *
	 * @param onImageListener the new on image listener
	 */
	public void setOnImageListener(OnImageListener onImageListener) {
		this.mOnImageListener = onImageListener;
	}
	

}

