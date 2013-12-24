/*
 * Copyright (C) 2013 www.418log.org
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
package com.ab.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.view.View;
import android.widget.ImageView;

import com.ab.global.AbAppData;
import com.ab.global.AbConstant;
import com.ab.util.AbImageUtil;
import com.ab.util.AbStrUtil;

// TODO: Auto-generated Javadoc
/**
 * 描述：下载图片并显示的工具类.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageDownloader { 
	
	/** The tag. */
	private static String TAG = "AbImageDownloader";
	
	/** The Constant D. */
	private static final boolean D = AbAppData.DEBUG;
	
    /** Context. */
    private static Context context = null;
    
    /** 显示的图片的宽. */
    private int width;
	
	/** 显示的图片的高. */
    private int height;
	
	/** 图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）. */
    private int type  = AbConstant.ORIGINALIMG;
    
    /** 显示为下载中的图片. */
    private Drawable loadingImage;
    
    /** 显示为下载中的View. */
    private View loadingView;
    
    /** 显示下载失败的图片. */
    private Drawable errorImage;
    
    /** 图片未找到的图片. */
    private Drawable noImage;
    
    /** 线程池. */
    private AbImageDownloadPool mAbImageDownloadPool = null;
    
    /** 动画控制. */
    private boolean animation;
    
    /**
     * 构造图片下载器.
     */
    public AbImageDownloader(Context context) {
    	this.context = context;
    	this.mAbImageDownloadPool = AbImageDownloadPool.getInstance();
    } 
     
    /**
     * 显示这个图片.
     * 加入动画效果后加载下一页后图片会全部闪一下，因为设置了不同的图片
     * @param imageView 显得的View
     * @return url 网络url
     */
    public void display(final ImageView imageView,String url) { 
    	
    	if(AbStrUtil.isEmpty(url)){
    		if(noImage != null){
    			if(loadingView != null){
        			loadingView.setVisibility(View.INVISIBLE);
					imageView.setVisibility(View.VISIBLE);
        		}
    			imageView.setImageDrawable(noImage);
    		}
    		return;
    	}
    	
    	//设置下载项
        AbImageDownloadItem item = new AbImageDownloadItem(); 
        //设置显示的大小
        item.width = width;
        item.height = height;
        //设置为缩放
        item.type = type;
        item.imageUrl = url;
        String cacheKey = AbImageCache.getCacheKey(item.imageUrl, item.width, item.height, item.type);
        item.bitmap =  AbImageCache.getBitmapFromCache(cacheKey);
		//if(D) Log.d(TAG, "缓存中获取的"+cacheKey+":"+item.bitmap);
		
    	if(item.bitmap == null){
    		
    		//先显示加载中
        	if(loadingView!=null){
    			loadingView.setVisibility(View.VISIBLE);
    			imageView.setVisibility(View.INVISIBLE);
    		}else if(loadingImage != null){
    			if(animation){
    				TransitionDrawable td = AbImageUtil.drawableToTransitionDrawable(loadingImage);
	        		imageView.setImageDrawable(td);
	    			td.startTransition(200);
    			}else{
    				imageView.setImageDrawable(loadingImage);
    			}
    		}
    		
    		//下载完成后更新界面
            item.listener = new AbImageDownloadListener() { 
                @Override 
                public void update(Bitmap bitmap, String imageUrl) { 
                	//未设置加载中的图片，并且设置了隐藏的View
            		if(loadingView != null){
            			loadingView.setVisibility(View.INVISIBLE);
						imageView.setVisibility(View.VISIBLE);
            		}
                	if(bitmap!=null){
                		if(animation){
	                		TransitionDrawable td = AbImageUtil.bitmapToTransitionDrawable(bitmap);
	                		imageView.setImageDrawable(td);
	            			td.startTransition(200);
                		}else{
                			imageView.setImageBitmap(bitmap);
                		}
                	}else{
                		if(errorImage != null){
                			if(animation){
	                			TransitionDrawable td = AbImageUtil.drawableToTransitionDrawable(errorImage);
	                    		imageView.setImageDrawable(td);
	                			td.startTransition(200);
                			}else{
                				imageView.setImageDrawable(errorImage);
                			}
                		}
                		
                	}
                } 
            }; 
            
            mAbImageDownloadPool.download(item);
    	}else{
    		if(loadingView != null){
    		    loadingView.setVisibility(View.INVISIBLE);
    		    imageView.setVisibility(View.VISIBLE);
    		}
    		imageView.setImageBitmap(item.bitmap);
    	}
        
    } 
    
    /**
	 * 
	 * 描述：设置下载中的图片
	 * @param resID
	 * @throws 
	 */
	public void setLoadingImage(int resID) {
		this.loadingImage = context.getResources().getDrawable(resID);
	}
	
	/**
	 * 
	 * 描述：设置下载中的View，优先级高于setLoadingImage
	 * @param view 放在ImageView的上边或者下边的View
	 * @throws 
	 */
	public void setLoadingView(View view) {
		this.loadingView = view;
	}

	/**
	 * 
	 * 描述：设置下载失败的图片
	 * @param resID
	 * @throws 
	 */
	public void setErrorImage(int resID) {
		this.errorImage = context.getResources().getDrawable(resID);
	}

	/**
	 * 
	 * 描述：设置未找到的图片
	 * @param resID
	 * @throws 
	 */
	public void setNoImage(int resID) {
		this.noImage = context.getResources().getDrawable(resID);
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 
	 * 描述：设置图片的宽度
	 * @param height
	 * @throws 
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 
	 * 描述：设置图片的高度
	 * @param height
	 * @throws 
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	
	
	public int getType() {
		return type;
	}

	/**
	 * 
	 * 描述：图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）.
	 * @param type
	 * @throws 
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 
	 * 描述：是否开启动画.
	 * @param animation
	 * @throws 
	 */
	public void setAnimation(boolean animation) {
		this.animation = animation;
	}
	
	
    
}

