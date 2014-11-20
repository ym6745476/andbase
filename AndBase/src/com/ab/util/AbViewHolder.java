package com.ab.util;

import android.util.SparseArray;
import android.view.View;

/**
 * © 2012 amsoft.cn
 * 名称：AbViewHolder.java 
 * 描述：超简洁的ViewHolder.
 * 代码更简单，性能略低，可以忽略
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-06-17 下午20:32:13
 */
public class AbViewHolder {
    
    /**
     * ImageView view = AbViewHolder.get(convertView, R.id.imageView);
     * @param view
     * @param id
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }
        return (T) childView;
    }
}
