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

import android.graphics.Bitmap;

// TODO: Auto-generated Javadoc
/**
 * 描述：图片下载单位.
 *
 * @author zhaoqp
 * @date 2011-12-10
 * @version v1.0
 */
public class AbImageDownloadItem {
	
	/** 需要下载的图片的互联网地址. */
	public String imageUrl;
	
	/** 显示的图片的宽. */
	public int width;
	
	/** 显示的图片的高. */
	public int height;
	
	/** 图片的处理类型（剪切或者缩放到指定大小，参考AbConstant类）. */
	public int type;
	
	/** 下载完成的到的Bitmap对象. */
	public Bitmap bitmap;
	
	/** 下载完成的回调接口. */
	public AbImageDownloadListener listener;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public AbImageDownloadListener getListener() {
		return listener;
	}

	public void setListener(AbImageDownloadListener listener) {
		this.listener = listener;
	}
	

}
