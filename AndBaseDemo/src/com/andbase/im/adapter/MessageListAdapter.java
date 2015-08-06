package com.andbase.im.adapter;




import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ab.image.AbImageLoader;
import com.ab.util.AbDateUtil;
import com.andbase.R;
import com.andbase.im.model.IMMessage;
/**
 * © 2012 amsoft.cn
 * 名称：MessageListAdapter
 * 描述：消息列表
 * @author 还如一梦中
 * @date 2011-12-10
 * @version
 */
public class MessageListAdapter extends BaseAdapter{
	
	private static String TAG = "MessageListAdapter";
	private Context mContext;
	//xml转View对象
    private LayoutInflater mInflater;
    private List<IMMessage> mData;
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
    public MessageListAdapter(Context context, List<IMMessage> data){
    	this.mContext = context;
    	this.mData = data;
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
	           //使用自定义的list_items作为Layout
	           convertView = mInflater.inflate(R.layout.im_message_item, parent, false);
	           //减少findView的次数
			   holder = new ViewHolder();
	           //初始化布局中的元素
			   holder.itemsIcon = (ImageView) convertView.findViewById(R.id.item_icon) ;
			   holder.itemsTitle = (TextView) convertView.findViewById(R.id.item_title);
			   holder.itemsText = (TextView) convertView.findViewById(R.id.item_text);
			   holder.itemsTime = (TextView) convertView.findViewById(R.id.item_time);
			   convertView.setTag(holder);
          }else{
        	   holder = (ViewHolder) convertView.getTag();
          }
          
		  //获取该行的数据
          final IMMessage mMessage = (IMMessage)mData.get(position);
          holder.itemsTitle.setText(mMessage.getFromUserName());
          holder.itemsText.setText(mMessage.getContent());
          holder.itemsTime.setText(AbDateUtil.getStringByFormat(mMessage.getSendDate(), AbDateUtil.dateFormatYMDHM));
          //图片的下载
          holder.itemsIcon.setBackgroundResource(R.drawable.ic_launcher);
          //mAbImageLoader.display(holder.itemsIcon,mMessage.g,100,100);
          
          return convertView;
    }
    
    /**
	 * View元素
	 */
	static class ViewHolder {
		ImageView itemsIcon;
		TextView itemsTitle;
		TextView itemsText;
		TextView itemsTime;
	}
    
}
