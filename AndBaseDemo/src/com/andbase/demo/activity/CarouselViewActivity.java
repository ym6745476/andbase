package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.util.AbToastUtil;
import com.andbase.R;
import com.andbase.view.carousel.CarouselAdapter;
import com.andbase.view.carousel.CarouselAdapter.OnItemClickListener;
import com.andbase.view.carousel.CarouselAdapter.OnItemSelectedListener;
import com.andbase.view.carousel.CarouselView;
import com.andbase.view.carousel.CarouselViewAdapter;
/**
 * 
 * © 2012 amsoft.cn
 * 名称：CarouselViewActivity.java 
 * 描述：View适配的旋转木马
 * @author 还如一梦中
 * @date：2013-8-23 下午2:07:13
 * @version v1.0
 */
public class CarouselViewActivity extends AbActivity {
	
	private CarouselView carousel = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.carousel_view);
		
		carousel = (CarouselView) findViewById(R.id.carousel);
		int [] drawables = new int[]{
				R.drawable.icon1,
				R.drawable.icon2,
				R.drawable.icon3,
				R.drawable.icon4,
				R.drawable.icon5,
				R.drawable.icon6,
		};
		
		//不支持的动态添加adapter.notifyDataSetChanged()增强滑动的流畅
		
		List<View> mViews = new ArrayList<View>();
		for(int i=0;i<6;i++){
			View convertView = mInflater.inflate(R.layout.carousel_item_view, null, false);
			TextView textView = (TextView)convertView.findViewById(R.id.itemsText);
		    textView.setText("Item"+i);
		    ImageView imageView = (ImageView)convertView.findViewById(R.id.itemsIcon);
		    imageView.setBackgroundResource(drawables[i]);
		    mViews.add(convertView);
		}
		
		
		CarouselViewAdapter adapter = new CarouselViewAdapter(this,mViews,true);
		carousel.setAdapter(adapter);
		
		
		carousel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(CarouselAdapter<?> parent, View view,
					int position, long id) {
				AbToastUtil.showToast(CarouselViewActivity.this,"Click Position=" + position);
				
			}

		});
		
		carousel.setOnItemSelectedListener(new OnItemSelectedListener(){

			@Override
			public void onItemSelected(CarouselAdapter<?> parent, View view,
					int position, long id) {
				AbToastUtil.showToast(CarouselViewActivity.this,"Selected Position=" + position);
			}

			@Override
			public void onNothingSelected(CarouselAdapter<?> parent) {
			}
			
		});
		
	}

}
