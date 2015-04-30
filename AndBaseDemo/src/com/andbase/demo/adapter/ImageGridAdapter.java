package com.andbase.demo.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbImageUtil;
import com.andbase.R;
import com.andbase.model.User;
/**
 * © 2012 amsoft.cn
 * 名称：OverlayGridAdapter
 * 描述：在Adapter中释放Bitmap
 * @author 还如一梦中
 * @date 2011-12-10
 * @version
 */
public class ImageGridAdapter extends BaseAdapter{
  
	private Context mContext;
	//xml转View对象
    private LayoutInflater mInflater;
    //单行的布局
    private int mResource;
    //列表展现的数据
    private List<User> mData;
    //Map中的key
    private String[] mFrom;
    //view的id
    private int[] mTo;
    
    //图片下载器
    private AbImageLoader mAbImageLoader = null;
    
   /**
    * 构造方法
    * @param context
    * @param data 列表展现的数据
    * @param resource 单行的布局
    * @param from Map中的key
    * @param to view的id
    */
    public ImageGridAdapter(Context context, List<User> data,
            int resource, String[] from, int[] to){
    	this.mContext = context;
    	this.mData = data;
    	this.mResource = resource;
    	this.mFrom = from;
    	this.mTo = to;
        //用于将xml转为View
        this.mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //图片下载器
        mAbImageLoader = new AbImageLoader(mContext);
    } 
    
    @Override
    public int getCount() {
        return mData.size();
    }
    
    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position){
      return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
    	  final ViewHolder holder;
          if(convertView == null){
	           convertView = mInflater.inflate(mResource, parent, false);
			   holder = new ViewHolder();
			   holder.itemsIcon = ((ImageView) convertView.findViewById(mTo[0])) ;
			   convertView.setTag(holder);
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          
		  //获取该行的数据
          final User mUser = (User)mData.get(position);
          String imageUrl = mUser.getHeadUrl();
          //设置加载中的View
          View loadingView = convertView.findViewById(R.id.progressBar);
          //图片的下载
          mAbImageLoader.display(holder.itemsIcon,loadingView,imageUrl,100,100);
         
          return convertView;
    }
    
    /**
	 * View元素
	 */
	static class ViewHolder {
		ImageView itemsIcon;
	}
    
}
