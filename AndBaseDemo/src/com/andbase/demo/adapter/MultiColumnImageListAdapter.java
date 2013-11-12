package com.andbase.demo.adapter;




import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.view.sample.AbScaleImageView;
import com.andbase.R;
import com.andbase.demo.model.ImageInfo;
import com.andbase.global.Constant;
/**
 * Copyright (c) 2011 All rights reserved
 * 名称：MyListViewAdapter
 * 描述：在Adapter中释放Bitmap
 * @author zhaoqp
 * @date 2011-12-10
 * @version
 */
public class MultiColumnImageListAdapter extends BaseAdapter{
	
	private static String TAG = "MultiColumnImageListAdapter";
	private static final boolean D = Constant.DEBUG;
  
	private Context mContext;
    //列表展现的数据
    private LinkedList<ImageInfo> mImageList;
    //图片下载器
    private AbImageDownloader mAbImageDownloader = null;
    
    
   /**
    * 构造方法
    * @param context
    * @param data 列表展现的数据
    * @param resource 单行的布局
    * @param from Map中的key
    * @param to view的id
    */
    public MultiColumnImageListAdapter(Context context, LinkedList<ImageInfo> imageList){
    	this.mContext = context;
    	this.mImageList = imageList;
    	//图片下载器
        mAbImageDownloader = new AbImageDownloader(mContext);
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
	    //mAbImageDownloader.setAnimation(true);
    }   
    
    @Override
    public int getCount() {
        return mImageList.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mImageList.get(position);
    }

    @Override
    public long getItemId(int position){
      return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    	  final ViewHolder holder;
          if(convertView == null){
	           //使用自定义的list_items作为Layout
        	   LayoutInflater layoutInflator = LayoutInflater.from(parent.getContext());
	           convertView = layoutInflator.inflate(R.layout.multi_list_items, null);
	           //减少findView的次数
			   holder = new ViewHolder();
	           //初始化布局中的元素
			   holder.itemsIcon = ((AbScaleImageView) convertView.findViewById(R.id.itemsIcon)) ;
			   convertView.setTag(holder);
			   
			      
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          
          //获取该行的数据
          final ImageInfo  mImageInfo = mImageList.get(position);
          holder.itemsIcon.setImageWidth(mImageInfo.getWidth());
		  holder.itemsIcon.setImageHeight(mImageInfo.getHeight());
		  //图片下载的最大宽高
		  mAbImageDownloader.setWidth(mImageInfo.getWidth());
		  mAbImageDownloader.setHeight(mImageInfo.getHeight());
		  String url = mImageInfo.getUrl();
		  
		  //设置加载中的View
          mAbImageDownloader.setLoadingView(convertView.findViewById(R.id.progressBar));
          mAbImageDownloader.display(holder.itemsIcon,url);
          
          return convertView;
    }
    
    /**
     * 
     * 描述：往底部添加
     * @param datas
     * @throws 
     */
    public void addItemLast(List<ImageInfo> datas) {
    	mImageList.addAll(datas);
	}

    /**
     * 
     * 描述：往顶部添加
     * @param datas
     * @throws 
     */
	public void addItemTop(List<ImageInfo> datas) {
		for (ImageInfo imageInfo : datas) {
			mImageList.addFirst(imageInfo);
		}
	}
    
    /**
	 * View元素
	 */
	static class ViewHolder {
		AbScaleImageView itemsIcon;
	}
    
}
