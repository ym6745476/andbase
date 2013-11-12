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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

import com.ab.bitmap.AbImageDownloader;

// TODO: Auto-generated Javadoc
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbNetworkImageView.java 
 * 描述：根据url下载图片的网络ImageView
 * @author zhaoqp
 * @date：2013-11-12 上午10:09:55
 * @version v1.0
 */
public class AbNetworkImageView extends ImageView {
	
    /** 图片的url. */
    private String mUrl;

    /** 图片下载器. */
    private AbImageDownloader mAbImageDownloader = null;


    /**
     * Instantiates a new ab network image view.
     *
     * @param context the context
     */
    public AbNetworkImageView(Context context) {
        this(context, null);
    }

    /**
     * Instantiates a new ab network image view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbNetworkImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Instantiates a new ab network image view.
     *
     * @param context the context
     * @param attrs the attrs
     * @param defStyle the def style
     */
    public AbNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 描述：设置图片的URL.
     *
     * @param url the url
     * @param abImageDownloader the ab image downloader
     */
    public void setImageUrl(String url, AbImageDownloader abImageDownloader) {
        mUrl = url;
        mAbImageDownloader = abImageDownloader;
        loadImageIfNecessary(false);
    }

    /**
     * 描述：如果未加载就加载.
     *
     * @param isInLayoutPass the is in layout pass
     */
    private void loadImageIfNecessary(final boolean isInLayoutPass) {
        int width = getWidth();
        int height = getHeight();

        boolean isFullyWrapContent = getLayoutParams() != null
                && getLayoutParams().height == LayoutParams.WRAP_CONTENT
                && getLayoutParams().width == LayoutParams.WRAP_CONTENT;
        
        if (width == 0 && height == 0 && !isFullyWrapContent) {
            return;
        }

        if (TextUtils.isEmpty(mUrl)) {
            setImageBitmap(null);
            return;
        }
        
        //图片的下载
        mAbImageDownloader.display(this,mUrl);
        
    }

    /**
     * 描述：TODO
     * @see android.view.View#onLayout(boolean, int, int, int, int)
     * @author: zhaoqp
     * @date：2013-11-12 下午3:12:06
     * @version v1.0
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        loadImageIfNecessary(true);
    }

    /**
     * 描述：TODO
     * @see android.view.View#onDetachedFromWindow()
     * @author: zhaoqp
     * @date：2013-11-12 下午3:12:06
     * @version v1.0
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    /**
     * 描述：TODO
     * @see android.widget.ImageView#drawableStateChanged()
     * @author: zhaoqp
     * @date：2013-11-12 下午3:12:06
     * @version v1.0
     */
    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }
}
