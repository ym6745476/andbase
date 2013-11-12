package com.andbase.demo.activity;
/*package com.andbase.activity;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

import java.io.File;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Window;

import com.ab.util.AbStrUtil;
import com.andbase.R;

public class VideoViewActivity extends Activity {
	
	//在线视频
	private String path = "http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8";
	//不支持优酷 http://player.youku.com/player.php/sid/XMjg1MTcyNDQ0/v.swf
	private VideoView mVideoView;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		// 设置横屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		// 标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (!io.vov.vitamio.LibsChecker.checkVitamioLibs(this))
			return;

		setContentView(R.layout.videoview);
		mVideoView = (VideoView) findViewById(R.id.surface_view);
		String pathTemp = null;
		if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
		   //无sd卡
		}else{
			 //有sd卡
			 File path1 = Environment.getExternalStorageDirectory();
        	 File fileDirectory = new File(path1.getAbsolutePath());
        	 pathTemp  = listFile(fileDirectory);
    	}
		
        if(AbStrUtil.isEmpty(pathTemp)){
        	pathTemp = path;
		}
		
		mVideoView.setVideoPath(pathTemp);
		mVideoView.setVideoQuality(MediaPlayer.VIDEOQUALITY_HIGH);
		mVideoView.setMediaController(new MediaController(this));
	}
	

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if (mVideoView != null)
			mVideoView.setVideoLayout(VideoView.VIDEO_LAYOUT_SCALE, 0);
		super.onConfigurationChanged(newConfig);
	}
	
	public String listFile(File directory){
		if(directory.isDirectory()){
			File[] files = directory.listFiles();
			if(files!=null){
		          for(int i=0;i<files.length;i++){
		            if(files[i].isDirectory()){
		            	String path = listFile(files[i]);
		            	if(!AbStrUtil.isEmpty(path)){
		            		return path;
		            	}
		            }else{
		            	String fName = files[i].getName();
		            	String end = fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase(); 
		            	Log.d("VideoViewActivity","list files:"+fName+","+end);
		            	if(end.equals("3gp")||end.equals("mp4") || end.equals("avi") ||end.equals("rmvb") ||end.equals("flv") ||end.equals("wmv") || end.equals("mkv") || end.equals("mov") || end.equals("m3u8")){
		            		  return files[i].getPath();
		            	}
		            }
		          }
		      }
        }
		return null;
	}
}
*/
