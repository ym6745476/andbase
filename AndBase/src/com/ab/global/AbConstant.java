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
package com.ab.global;

// TODO: Auto-generated Javadoc
/**
 * 描述：常量.
 *
 * @author zhaoqp
 * @date：2013-1-15 下午3:12:06
 * @version v1.0
 */
public class AbConstant {
	
	/** SharePreferences文件名. */
	public static final String SHAREPATH = "app_share";
    
	/** 数据状态：可用. */
    public static final int STATEYES = 1;
    
    /** 数据状态：不可用. */
    public static final int STATENO = 0;
    
    /** 数据状态：全部. */
    public static final int STATEALL = 9;
    
    /** 数据状态：有. */
    public static final int HAVE = 1;
    
    /** 数据状态：没有. */
    public static final int NOTHAVE = 0;
    
    /** 图片处理：裁剪. */
	public static final int CUTIMG = 0;
	
	/** 图片处理：缩放. */
    public static final int SCALEIMG = 1;
    
    /** 图片处理：不处理. */
    public static final int ORIGINALIMG = 2;
    
    /** 返回码：成功. */
    public static final int RESULRCODE_OK = 0;
    
    /** 返回码：失败. */
    public static final int RESULRCODE_ERROR = -1;
    
    /** 显示Toast. */
    public static final int SHOW_TOAST = 0;
    
    /** 显示进度框. */
    public static final int SHOW_PROGRESS = 1;
    
    /** 删除进度框. */
	public static final int REMOVE_PROGRESS = 2;
	
	/** 删除底部进度框. */
	public static final int REMOVE_DIALOGBOTTOM = 3;
	
	/** 删除中间进度框. */
	public static final int REMOVE_DIALOGCENTER = 4;
	
	/** 删除顶部进度框. */
	public static final int REMOVE_DIALOGTOP = 5;
	
	/** 标题栏透明标记. */
	public static final String TITLE_TRANSPARENT_FLAG = "TITLE_TRANSPARENT_FLAG";
	
	/** 标题栏透明. */
	public static final int TITLE_TRANSPARENT = 0;
	
	/** 标题栏不透明. */
	public static final int TITLE_NOTRANSPARENT = 1;
	
	/** View的类型. */
	public static final int LISTVIEW = 1;
	
	/** The Constant GRIDVIEW. */
	public static final int GRIDVIEW = 1;
	
	/** The Constant GALLERYVIEW. */
	public static final int GALLERYVIEW = 2;
	
	/** The Constant RELATIVELAYOUTVIEW. */
	public static final int RELATIVELAYOUTVIEW = 3;
	
	/** Dialog的类型. */
	public static final int DIALOGPROGRESS = 0;
	
	/** The Constant DIALOGBOTTOM. */
	public static final int DIALOGBOTTOM = 1;
	
	/** The Constant DIALOGCENTER. */
	public static final int DIALOGCENTER = 2;
	
	/** The Constant DIALOGTOP. */
	public static final int DIALOGTOP = 3;
	
	/** 缓存的几张默认的图片. */
	public static final String IMAGEERRORURL = "image/image_error.png";
	
	/** The Constant IMAGELOADINGURL. */
	public static final String IMAGELOADINGURL = "image/image_loading.png";
	
	/** The Constant IMAGENOURL. */
	public static final String IMAGENOURL = "image/image_no.png";
   
}
