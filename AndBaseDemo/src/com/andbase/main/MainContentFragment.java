package com.andbase.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.andbase.R;
import com.andbase.demo.activity.AddPhotoActivity;
import com.andbase.demo.activity.CalendarActivity;
import com.andbase.demo.activity.CarouselActivity;
import com.andbase.demo.activity.ChartActivity;
import com.andbase.demo.activity.DBActivity;
import com.andbase.demo.activity.DemoAbActivity;
import com.andbase.demo.activity.DownListActivity;
import com.andbase.demo.activity.HttpActivity;
import com.andbase.demo.activity.ImageDownActivity;
import com.andbase.demo.activity.IocViewActivity;
import com.andbase.demo.activity.NestScrollActivity;
import com.andbase.demo.activity.PHashActivity;
import com.andbase.demo.activity.PopoverActivity;
import com.andbase.demo.activity.ProgressBarActivity;
import com.andbase.demo.activity.PullToRefreshActivity;
import com.andbase.demo.activity.Rotate3DActivity;
import com.andbase.demo.activity.SceneActivity;
import com.andbase.demo.activity.SlidingButtonActivity;
import com.andbase.demo.activity.SlidingMenuActivity;
import com.andbase.demo.activity.SlidingPlayViewActivity;
import com.andbase.demo.activity.SlidingTabActivity;
import com.andbase.demo.activity.TableActivity;
import com.andbase.demo.activity.ThreadControlActivity;
import com.andbase.demo.activity.WelcomeActivity;
import com.andbase.demo.activity.WheelActivity;
import com.andbase.demo.adapter.MyListViewAdapter;
import com.andbase.global.MyApplication;

public class MainContentFragment extends Fragment {
	
	private MyApplication application;
	private Activity mActivity = null;
	private ListView mListView = null;
	private MyListViewAdapter myListViewAdapter = null;
	private List<Map<String, Object>> list = null;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		 
		 mActivity = this.getActivity();
		 application = (MyApplication) mActivity.getApplication();
		 
		 View view = inflater.inflate(R.layout.main, null);
		 //获取ListView对象
	     mListView = (ListView)view.findViewById(R.id.mListView);
	     //ListView数据
	     list = new ArrayList<Map<String, Object>>();
	     Map<String, Object> map = new HashMap<String, Object>();
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "1.AbActivity基本用法");
	    	map.put("itemsText", "AbActivity使用示例");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "2.数据库ORM");
	    	map.put("itemsText", "注解，数据库对象映射");
	    	list.add(map);

	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "3.IOC 适配View");
	    	map.put("itemsText", "像findViewById说no");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "4.Http工具类");
	    	map.put("itemsText", "网络通信首选");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "5.线程池与线程队列");
	    	map.put("itemsText", "适应于Http工具类管理范围外，更灵活的应用");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "6.图片下载与处理");
	    	map.put("itemsText", "图片下载,裁剪,缩放");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "7.下拉刷新与分页查询");
	    	map.put("itemsText", "支持下拉刷新，上拉加载下一页");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "8.常规表格");
	    	map.put("itemsText", "多能适配的表格（支持文本，图片，复选框）");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "9.滑动按钮");
	    	map.put("itemsText", "滑动按钮");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "10.图片联播");
	    	map.put("itemsText", "图片联播,View播放");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "11.下载器");
	    	map.put("itemsText", "多线程，断点续传");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "12.动画欢迎页面");
	    	map.put("itemsText", "从远到近的显示的图片切换");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "13.侧边栏");
	    	map.put("itemsText", "左右侧边栏");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "14.sliding Tab");
	    	map.put("itemsText", "可滑动的tab标签");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "15.仿Iphone轮子选择控件");
	    	map.put("itemsText", "仿Iphone轮子选择控件");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "16.拍照和相册选取图片");
	    	map.put("itemsText", "拍照和相册选取图片");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "17.图表");
	    	map.put("itemsText", "线状图，柱状图，饼状图，等级条图");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "18.日历选择器");
	    	map.put("itemsText", "日历选择器哦");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "19.图片相近搜索");
	    	map.put("itemsText", "phash算法");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "20.旋转木马");
	    	map.put("itemsText", "旋转木马");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "21.水平，环形进度条");
	    	map.put("itemsText", "漂亮的水平，环形进度条控件");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "22.3D翻转效果");
	    	map.put("itemsText", "2013纪念币");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "23.各种滑动嵌套问题");
	    	map.put("itemsText", "各种滑动嵌套问题的解决例子");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "24.场景化UI");
	    	map.put("itemsText", "这玩意很流行");
	    	list.add(map);
	    	
	    	map = new HashMap<String, Object>();
	    	map.put("itemsIcon",R.drawable.image_bg);
	    	map.put("itemsTitle", "25.Pop提示框");
	    	map.put("itemsText", "挺实用，小提示");
	    	list.add(map);
	    	
	    	
	    	
	     //使用自定义的Adapter
	     myListViewAdapter = new MyListViewAdapter(mActivity, list,R.layout.list_items,
					new String[] { "itemsIcon", "itemsTitle","itemsText" }, new int[] { R.id.itemsIcon,
							R.id.itemsTitle,R.id.itemsText });
	     mListView.setAdapter(myListViewAdapter);
	     //item被点击事件
	     mListView.setOnItemClickListener(new OnItemClickListener(){
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = null;
					switch (position) {
					case 0:
						intent = new Intent(mActivity,DemoAbActivity.class);
						startActivity(intent);
						break;
					case 1:
						intent = new Intent(mActivity,DBActivity.class);
						startActivity(intent);
						break;
					case 2:
						intent = new Intent(mActivity,IocViewActivity.class);
						startActivity(intent);
						break;
					case 3:
						intent = new Intent(mActivity,HttpActivity.class);
						startActivity(intent);
						break;
					case 4:
						intent = new Intent(mActivity,ThreadControlActivity.class);
						startActivity(intent);
						break;
					case 5:
						intent = new Intent(mActivity,ImageDownActivity.class);
						startActivity(intent);
						break;
					case 6:
						intent = new Intent(mActivity,PullToRefreshActivity.class);
						startActivity(intent);
						break;
					case 7:
						intent = new Intent(mActivity,TableActivity.class);
						startActivity(intent);
						break;
					case 8:
						intent = new Intent(mActivity,SlidingButtonActivity.class);
						startActivity(intent);
						break;
					case 9:
						intent = new Intent(mActivity,SlidingPlayViewActivity.class);
						startActivity(intent);
						break;
					case 10:
						intent = new Intent(mActivity,DownListActivity.class);
						startActivity(intent);
						break;
					case 11:
						intent = new Intent(mActivity,WelcomeActivity.class);
						startActivity(intent);
						break;
					case 12:
						intent = new Intent(mActivity,SlidingMenuActivity.class);
						startActivity(intent);
						break;
					case 13:
						intent = new Intent(mActivity,SlidingTabActivity.class);
						startActivity(intent);
						break;
					case 14:
						intent = new Intent(mActivity,WheelActivity.class);
						startActivity(intent);
						break;
					case 15:
						intent = new Intent(mActivity,AddPhotoActivity.class);
						startActivity(intent);
						break;
					case 16:
						intent = new Intent(mActivity,ChartActivity.class);
						startActivity(intent);
						break;
					case 17:
						intent = new Intent(mActivity,CalendarActivity.class);
						startActivity(intent);
						break;
					case 18:
						intent = new Intent(mActivity,PHashActivity.class);
						startActivity(intent);
						break;
					case 19:
						intent = new Intent(mActivity,CarouselActivity.class);
						startActivity(intent);
						break;
					case 20:
						intent = new Intent(mActivity,ProgressBarActivity.class);
						startActivity(intent);
						break;
					case 21:
						intent = new Intent(mActivity,Rotate3DActivity.class);
						startActivity(intent);
						break;
					case 22:
						intent = new Intent(mActivity,NestScrollActivity.class);
						startActivity(intent);
						break;
					case 23:
						intent = new Intent(mActivity,SceneActivity.class);
						startActivity(intent);
						break;
					case 24:
						intent = new Intent(mActivity,PopoverActivity.class);
						startActivity(intent);
						break;
					default:
						break;
					}
				}
	     });
	    
		 return view;
	} 
    
    

}


