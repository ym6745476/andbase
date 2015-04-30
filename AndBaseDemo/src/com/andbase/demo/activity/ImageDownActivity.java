package com.andbase.demo.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.ab.activity.AbActivity;
import com.ab.image.AbImageLoader;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;

public class ImageDownActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
	private ImageView scaleView = null;
	private ImageView scaleView2 = null;
	private ImageView scaleView3 = null;
	
	
	//图片下载类
	private AbImageLoader mAbImageLoader = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.image_down);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.down_image_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        scaleView = (ImageView)this.findViewById(R.id.scale_img);
        scaleView2 = (ImageView)this.findViewById(R.id.scale_img2);
        scaleView3 = (ImageView)this.findViewById(R.id.scale_img3);
        
        String imageUrl = Constant.BASEURL+"content/templates/amsoft/images/rand/0.jpg";
        String imageUrl2 = Constant.BASEURL+"content/templates/amsoft/images/rand/1.jpg";
        String imageUrl3 = Constant.BASEURL+"content/templates/amsoft/images/rand/2.jpg";
        
        //图片的下载
        mAbImageLoader = AbImageLoader.getInstance(this);
        
        //原图片的下载
        mAbImageLoader.display(scaleView,imageUrl);
        
        //缩放图片的下载（保持宽高比，计算缩放比例，使一个方向缩放后，另一方向不小与显示的大小的最合适比例）
        mAbImageLoader.display(scaleView2,imageUrl2,150,150);
        
        //放大图片的下载
        mAbImageLoader.display(scaleView3,imageUrl3,180,180);
        
    }
    
    
    private void initTitleRightLayout(){
    	mAbTitleBar.clearRightView();
    }

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}
	
	public void onPause() {
		super.onPause();
	}
   
}


