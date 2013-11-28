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
package com.ab.view.carousel;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ab.util.AbImageUtil;
// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：CarouselImageAdapter.java 
 * 描述：只有图片适配的Carousel
 * @author zhaoqp
 * @date：2013-8-22 下午4:04:42
 * @version v1.0
 */
public class CarouselImageAdapter extends BaseAdapter {

	/** The m context. */
	private Context mContext;
	
	/** The m drawables. */
	private List<Drawable> mDrawables;
	
	/** The m reflected. */
	private boolean mReflected = true;
	
	/** The m carousel image views. */
	private CarouselItemImage[]  mCarouselImageViews = null;

	/**
	 * Instantiates a new carousel image adapter.
	 *
	 * @param c the c
	 * @param drawables the drawables
	 * @param reflected 反射镜面效果
	 */
	public CarouselImageAdapter(Context c,List<Drawable> drawables,boolean reflected) {
		mContext = c;
		mDrawables = drawables;
		mReflected = reflected;
		setImages();
	}

	/**
	 * 描述：TODO.
	 *
	 * @return the count
	 * @see android.widget.Adapter#getCount()
	 * @author: zhaoqp
	 * @date：2013-8-22 下午4:07:39
	 * @version v1.0
	 */
	public int getCount() {
		if (mDrawables == null){
			return 0;
		}else{
			return mDrawables.size();
		}
	}

	/**
	 * 描述：TODO.
	 *
	 * @param position the position
	 * @return the item
	 * @see android.widget.Adapter#getItem(int)
	 * @author: zhaoqp
	 * @date：2013-8-22 下午4:07:39
	 * @version v1.0
	 */
	public Object getItem(int position) {
		return position;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param position the position
	 * @return the item id
	 * @see android.widget.Adapter#getItemId(int)
	 * @author: zhaoqp
	 * @date：2013-8-22 下午4:07:39
	 * @version v1.0
	 */
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 描述：TODO.
	 *
	 * @param position the position
	 * @param convertView the convert view
	 * @param parent the parent
	 * @return the view
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 * @author: zhaoqp
	 * @date：2013-8-22 下午4:07:39
	 * @version v1.0
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return mCarouselImageViews[position];
	}
	
	/**
	 * Sets the images.
	 */
	public void setImages(){
		mCarouselImageViews = new CarouselItemImage[mDrawables.size()];
		for(int i = 0; i< mDrawables.size(); i++){
			Drawable drawable = mDrawables.get(i);
			Bitmap originalImage = ((BitmapDrawable)drawable).getBitmap();
			
			if(mReflected){
				originalImage = AbImageUtil.toReflectionBitmap(originalImage);
			}
			
			CarouselItemImage imageView = new CarouselItemImage(mContext);
			imageView.setImageBitmap(originalImage);
			imageView.setIndex(i);
			
			mCarouselImageViews[i] = imageView;
		}
		
		
	}

}
