package com.andbase.demo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbImageUtil;
import com.andbase.R;

/**
 * © 2012 amsoft.cn 
 * 名称：MyListViewAdapter 
 * 描述：ListView自定义Adapter例子
 * @author 还如一梦中
 * @date 2011-11-8
 * @version
 */
public class MyListViewAdapter extends BaseAdapter{
  
	private Context mContext;
    //单行的布局
    private int mResource;
    //列表展现的数据
    private List<? extends Map<String, ?>> mData;
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
    public MyListViewAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to){
    	 mContext = context;
    	 mData = data;
         mResource = resource;
         mFrom = from;
         mTo = to;
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
	          //使用自定义的list_items作为Layout
	          convertView = LayoutInflater.from(mContext).inflate(mResource, parent, false);
	          //使用减少findView的次数
			  holder = new ViewHolder();
			  holder.itemsIcon = ((ImageView) convertView.findViewById(mTo[0])) ;
			  holder.itemsTitle = ((TextView) convertView.findViewById(mTo[1]));
			  holder.itemsText = ((TextView) convertView.findViewById(mTo[2]));
			  //设置标记
			  convertView.setTag(holder);
          }else{
        	  holder = (ViewHolder) convertView.getTag();
          }
	      //设置数据
          final Map<String, ?> dataSet = mData.get(position);
          if (dataSet == null) {
              return null;
          }
          //获取该行数据
          final Object data0 = dataSet.get(mFrom[0]);
          final Object data1 = dataSet.get(mFrom[1]);
          final Object data2 = dataSet.get(mFrom[2]);
          String imageUrl = (String)data0;
          View loadingView = convertView.findViewById(R.id.progressBar);
          //图片的下载
          mAbImageLoader.display(holder.itemsIcon,loadingView,imageUrl,100,100);
          holder.itemsTitle.setText(String.valueOf(position+1)+"."+data1.toString());
          holder.itemsText.setText(data2.toString());
          return convertView; 
    }
    
    /**
	 * ViewHolder类
	 */
	static class ViewHolder {
		ImageView itemsIcon;
		TextView itemsTitle;
		TextView itemsText;
	}
}
