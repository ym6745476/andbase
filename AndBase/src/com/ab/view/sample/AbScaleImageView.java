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
package com.ab.view.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbScaleImageView.java 
 * 描述：根据宽度或高度设置图像尺寸，如果未设置取决于图像尺寸
 * @author zhaoqp
 * @date：2013-9-3 下午4:09:16
 * @version v1.0
 */
public class AbScaleImageView extends ImageView {
    
    /** 当前的bitmap. */
    private Bitmap currentBitmap;
    
    /** The image width. */
    private int imageWidth;
    
    /** The image height. */
    private int imageHeight;

    /**
     * 构造方法.
     *
     * @param context the context
     */
   public AbScaleImageView(Context context) {
        super(context);
        init();
    }

    /**
     * 构造方法.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public AbScaleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 构造方法.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbScaleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * 初始化图片缩放类型.
     */
    private void init() {
        this.setScaleType(ScaleType.CENTER_INSIDE);
    }

    /**
     * 回收.
     */
    public void recycle() {
        setImageBitmap(null);
        if ((this.currentBitmap == null) || (this.currentBitmap.isRecycled()))
            return;
        this.currentBitmap.recycle();
        this.currentBitmap = null;
    }

    /**
     * 描述：设置图片Bitmap
     */
    @Override
    public void setImageBitmap(Bitmap bm) {
        currentBitmap = bm;
        super.setImageBitmap(currentBitmap);
    }

    /**
     * 描述：设置图片Drawable
     */
    @Override
    public void setImageDrawable(Drawable d) {
        super.setImageDrawable(d);
    }

    /**
     * 描述：设置图片资源
     */
    @Override
    public void setImageResource(int id) {
        super.setImageResource(id);
    }

    
    /**
     * 描述：onMeasure
     * @see android.widget.ImageView#onMeasure(int, int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (imageWidth == 0) {
            // 按图片大小
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        } else {
        	this.setScaleType(ScaleType.CENTER_CROP);
            setMeasuredDimension(imageWidth, imageHeight);
        }
    }
    
    /**
     * 设置View的宽度.
     *
     * @param w the new image width
     */
    public void setImageWidth(int w) {
        imageWidth = w;
    }

    /**
     * 设置View的高度.
     *
     * @param h the new image height
     */
    public void setImageHeight(int h) {
        imageHeight = h;
    }


}
