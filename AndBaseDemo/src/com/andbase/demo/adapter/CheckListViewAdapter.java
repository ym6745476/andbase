package com.andbase.demo.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.ab.util.AbToastUtil;
import com.ab.view.sliding.AbSlidingButton;
import com.andbase.R;

/**
 * © 2012 amsoft.cn 
 * 名称：MyListViewAdapter 
 * 描述：ListView自定义Adapter例子
 * @author 还如一梦中
 * @date 2011-11-8
 * @version
 */
public class CheckListViewAdapter extends BaseAdapter{
  
	private Context mContext;
    //单行的布局
    private int mResource;
    //列表展现的数据
    private List<? extends Map<String, ?>> mData;
    //Map中的key
    private String[] mFrom;
    //view的id
    private int[] mTo;
    
   /**
    * 构造方法
    * @param context
    * @param data 列表展现的数据
    * @param resource 单行的布局
    * @param from Map中的key
    * @param to view的id
    */
    public CheckListViewAdapter(Context context, List<? extends Map<String, ?>> data,
            int resource, String[] from, int[] to){
    	 mContext = context;
    	 mData = data;
         mResource = resource;
         mFrom = from;
         mTo = to;
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
			  holder.itemsTitle = ((TextView) convertView.findViewById(mTo[0]));
			  holder.itemsText = ((TextView) convertView.findViewById(mTo[1]));
			  holder.itemsCheck = ((AbSlidingButton) convertView.findViewById(mTo[2])) ;
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
          //设置数据到View
          holder.itemsTitle.setText(data0.toString());
          holder.itemsText.setText(data1.toString());
          
          //设置开关显示所用的图片
          holder.itemsCheck.setImageResource(R.drawable.btn_bottom,R.drawable.btn_frame,R.drawable.btn_mask, R.drawable.btn_unpressed,R.drawable.btn_pressed);
          //holder.itemsCheck.setFocusable(false);
          //设置开关的默认状态    true开启状态
          //holder.itemsCheck.setToggleState(true);
          
          //设置开关的监听
          holder.itemsCheck.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				AbToastUtil.showToast(mContext,"开关变化");
			}
		});
          
          return convertView;
    }
    
    /**
	 * ViewHolder类
	 */
	static class ViewHolder {
		TextView itemsTitle;
		TextView itemsText;
		AbSlidingButton itemsCheck;
	}
}
