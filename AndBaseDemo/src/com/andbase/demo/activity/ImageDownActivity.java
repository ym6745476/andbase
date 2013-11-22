package com.andbase.demo.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.bitmap.AbImageDownloader;
import com.ab.global.AbConstant;
import com.ab.view.sample.AbNetworkImageView;
import com.ab.view.sample.AbScaleImageView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

public class ImageDownActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	
	private ImageView originalView = null;
	private ImageView scaleView = null;
	private ImageView cutView = null;
	private AbScaleImageView setView = null;
	private AbNetworkImageView netView = null;
	
	//图片下载类
	private AbImageDownloader mAbImageDownloader = null;
	
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
        
        originalView = (ImageView)this.findViewById(R.id.original_img);
        scaleView = (ImageView)this.findViewById(R.id.scale_img);
        cutView = (ImageView)this.findViewById(R.id.cut_img);
        setView = (AbScaleImageView)this.findViewById(R.id.set_img);
        netView = (AbNetworkImageView)this.findViewById(R.id.net_img);
        
        TextView scale_text = (TextView)this.findViewById(R.id.scale_text);
        TextView cut_text = (TextView)this.findViewById(R.id.cut_text);
        TextView set_text = (TextView)this.findViewById(R.id.set_text);
        TextView net_text = (TextView)this.findViewById(R.id.net_text);
        
        originalView.setImageResource(R.drawable.image_loading);
        scaleView.setImageResource(R.drawable.image_loading);
        cutView.setImageResource(R.drawable.image_loading);
        setView.setImageResource(R.drawable.image_loading);
        netView.setImageResource(R.drawable.image_loading);
        
        String imageUrl = "http://img01.taobaocdn.com/bao/uploaded/i3/13215023749568975/T1UKWCXvpXXXXXXXXX_!!0-item_pic.jpg_230x230.jpg";
        
        //图片的下载
        mAbImageDownloader = new AbImageDownloader(this);
        mAbImageDownloader.setWidth(150);
        mAbImageDownloader.setHeight(150);
        mAbImageDownloader.setLoadingImage(R.drawable.image_loading);
        mAbImageDownloader.setErrorImage(R.drawable.image_error);
        mAbImageDownloader.setNoImage(R.drawable.image_no);
        
        
        //裁剪图片的下载
        mAbImageDownloader.setType(AbConstant.CUTIMG);
        mAbImageDownloader.display(cutView,imageUrl);
        
        
        //缩放图片的下载
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        mAbImageDownloader.display(scaleView,imageUrl);
        
        //原图片的下载
        mAbImageDownloader.setType(AbConstant.ORIGINALIMG);
        mAbImageDownloader.display(originalView,imageUrl);
        
        //显示为设置的大小px
        setView.setImageWidth(120);
        setView.setImageHeight(120);
        mAbImageDownloader.setWidth(120);
        mAbImageDownloader.setHeight(120);
        mAbImageDownloader.setType(AbConstant.ORIGINALIMG);
        mAbImageDownloader.display(setView,imageUrl);
        
        //自动获取
        mAbImageDownloader.setWidth(150);
        mAbImageDownloader.setHeight(150);
        mAbImageDownloader.setType(AbConstant.SCALEIMG);
        netView.setImageUrl(imageUrl, mAbImageDownloader);
       
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


