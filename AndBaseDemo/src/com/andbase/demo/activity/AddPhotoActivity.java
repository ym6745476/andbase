package com.andbase.demo.activity;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.protocol.HTTP;

import android.app.DialogFragment;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbRequestParams;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.progress.AbHorizontalProgressBar;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageShowAdapter;
import com.andbase.global.MyApplication;

public class AddPhotoActivity extends AbActivity {
	
	private MyApplication application;
	private GridView mGridView = null;
	private ImageShowAdapter mImagePathAdapter = null;
	private ArrayList<String> mPhotoList = null;
	private int selectIndex = 0;
	private int camIndex = 0;
	private View mAvatarView = null;
	
	/* 用来标识请求照相功能的activity */
	private static final int CAMERA_WITH_DATA = 3023;
	/* 用来标识请求gallery的activity */
	private static final int PHOTO_PICKED_WITH_DATA = 3021;
	/* 用来标识请求裁剪图片后的activity */
	private static final int CAMERA_CROP_DATA = 3022;
	
	/* 拍照的照片存储位置 */
	private  File PHOTO_DIR = null;
	// 照相机拍照得到的图片
	private File mCurrentPhotoFile;
	private String mFileName;
	
	/* ProgressBar进度控制 */
	private AbHorizontalProgressBar mAbProgressBar;
	/* 最大100 */
	private int max = 100;	
	private int progress = 0;
	private TextView numberText, maxText;
	private DialogFragment  mAlertDialog  = null;
	private AbHttpUtil mAbHttpUtil = null;
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.add_photo);
		application = (MyApplication) abApplication;
		   
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.photo_add_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		
		initTitleRightLayout();
		
		mPhotoList = new ArrayList<String>();
		
		//获取Http工具类
        mAbHttpUtil = AbHttpUtil.getInstance(this);
		
        //默认
		mPhotoList.add(String.valueOf(R.drawable.cam_photo));
		
		mGridView = (GridView)findViewById(R.id.myGrid);
		mImagePathAdapter = new ImageShowAdapter(this, mPhotoList,116,116);
		mGridView.setAdapter(mImagePathAdapter);
	   
		//初始化图片保存路径
	    String photo_dir = AbFileUtil.getImageDownloadDir(this);
	    if(AbStrUtil.isEmpty(photo_dir)){
	    	AbToastUtil.showToast(AddPhotoActivity.this,"存储卡不存在");
	    }else{
	    	PHOTO_DIR = new File(photo_dir);
	    }
		
		
		Button addBtn = (Button)findViewById(R.id.addBtn);
		
		mGridView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectIndex = position;
				if(selectIndex == camIndex){
					mAvatarView = mInflater.inflate(R.layout.choose_avatar, null); 
					Button albumButton = (Button)mAvatarView.findViewById(R.id.choose_album);
					Button camButton = (Button)mAvatarView.findViewById(R.id.choose_cam);
					Button cancelButton = (Button)mAvatarView.findViewById(R.id.choose_cancel);
					albumButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							AbDialogUtil.removeDialog(AddPhotoActivity.this);
							// 从相册中去获取
							try {
								Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
								intent.setType("image/*");
								startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
							} catch (ActivityNotFoundException e) {
								AbToastUtil.showToast(AddPhotoActivity.this,"没有找到照片");
							}
						}
						
					});
					
					camButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							AbDialogUtil.removeDialog(AddPhotoActivity.this);
							doPickPhotoAction();
						}
						
					});
					
					cancelButton.setOnClickListener(new OnClickListener(){

						@Override
						public void onClick(View v) {
							AbDialogUtil.removeDialog(AddPhotoActivity.this);
						}
						
					});
					AbDialogUtil.showDialog(mAvatarView,Gravity.BOTTOM);
				}else{
					for(int i=0;i<mImagePathAdapter.getCount();i++){
						ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder)mGridView.getChildAt(i).getTag();
						if(mViewHolder!=null){
							mViewHolder.mImageView2.setBackgroundDrawable(null);
						}
					}
	
					ImageShowAdapter.ViewHolder mViewHolder = (ImageShowAdapter.ViewHolder)view.getTag();
					mViewHolder.mImageView2.setBackgroundResource(R.drawable.photo_select);
				}
			}
			
		});
		
		addBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				uploadFile(mPhotoList);
			}
		});
		
	}
	
	private void initTitleRightLayout() {
		
	}
	
	/**
	 * 从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		//判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			AbToastUtil.showToast(AddPhotoActivity.this,"没有可用的存储卡");
		}
	}

	/**
	 * 拍照获取图片
	 */
	protected void doTakePhoto() {
		try {
			mFileName = System.currentTimeMillis() + ".jpg";
			mCurrentPhotoFile = new File(PHOTO_DIR, mFileName);
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
			startActivityForResult(intent, CAMERA_WITH_DATA);
		} catch (Exception e) {
			AbToastUtil.showToast(AddPhotoActivity.this,"未找到系统相机程序");
		}
	}
	
	/**
	 * 描述：因为调用了Camera和Gally所以要判断他们各自的返回情况,
	 * 他们启动时是这样的startActivityForResult
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent mIntent) {
		if (resultCode != RESULT_OK){
			return;
		}
		switch (requestCode) {
			case PHOTO_PICKED_WITH_DATA:
				Uri uri = mIntent.getData();
				String currentFilePath = getPath(uri);
				if(!AbStrUtil.isEmpty(currentFilePath)){
					Intent intent1 = new Intent(this, CropImageActivity.class);
					intent1.putExtra("PATH", currentFilePath);
					startActivityForResult(intent1, CAMERA_CROP_DATA);
		        }else{
		        	AbToastUtil.showToast(AddPhotoActivity.this,"未在存储卡中找到这个文件");
		        }
				break;
			case CAMERA_WITH_DATA:
				AbLogUtil.d(AddPhotoActivity.class, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
				String currentFilePath2 = mCurrentPhotoFile.getPath();
				Intent intent2 = new Intent(this, CropImageActivity.class);
				intent2.putExtra("PATH",currentFilePath2);
				startActivityForResult(intent2,CAMERA_CROP_DATA);
				break;
			case CAMERA_CROP_DATA:
				String path = mIntent.getStringExtra("PATH");
		    	AbLogUtil.d(AddPhotoActivity.class, "裁剪后得到的图片的路径是 = " + path);
		    	mImagePathAdapter.addItem(mImagePathAdapter.getCount()-1,path);
		     	camIndex++;
				break;
		}
	}

	/**
	 * 从相册得到的url转换为SD卡中图片路径
	 */
	public String getPath(Uri uri) {
		if(AbStrUtil.isEmpty(uri.getAuthority())){
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String path = cursor.getString(column_index);
		return path;
	}
	
	public void uploadFile(List<String> list){
		//已经在后台上传
		if(mAlertDialog!=null){
			mAlertDialog.show(getFragmentManager(), "dialog");
			return;
		}
		String url = "http://192.168.0.104:8080/demo/upload.do";
		
		AbRequestParams params = new AbRequestParams(); 
		
		try {
			//多文件上传添加多个即可
			params.put("data1",URLEncoder.encode("如果包含中文的处理方式",HTTP.UTF_8));
			params.put("data2","100");
			//文件参数，去掉后边那个按钮
			for(int i=0;i<list.size()-1;i++){
				String path = list.get(i);
				File file = new File(path);
				params.put(file.getName(),file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mAbHttpUtil.post(url, params, new AbStringHttpResponseListener() {

			
			@Override
			public void onSuccess(int statusCode, String content) {
				AbToastUtil.showToast(AddPhotoActivity.this,content);
			}

			// 开始执行前
            @Override
			public void onStart() {
            	//打开进度框
            	View v = LayoutInflater.from(AddPhotoActivity.this).inflate(R.layout.progress_bar_horizontal, null, false);
            	mAbProgressBar = (AbHorizontalProgressBar) v.findViewById(R.id.horizontalProgressBar);
            	numberText = (TextView) v.findViewById(R.id.numberText);
        		maxText = (TextView) v.findViewById(R.id.maxText);
        		
        		maxText.setText(progress+"/"+String.valueOf(max));
        		mAbProgressBar.setMax(max);
        		mAbProgressBar.setProgress(progress);
            	
        		mAlertDialog = AbDialogUtil.showAlertDialog("正在上传",v);
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				AbToastUtil.showToast(AddPhotoActivity.this,error.getMessage());
			}

			// 进度
			@Override
			public void onProgress(long bytesWritten, long totalSize) {
				maxText.setText(bytesWritten/(totalSize/max)+"/"+max);
				mAbProgressBar.setProgress((int)(bytesWritten/(totalSize/max)));
			}

			// 完成后调用，失败，成功，都要调用
            public void onFinish() { 
            	//下载完成取消进度框
            	if(mAlertDialog!=null){
            	    mAlertDialog.dismiss();
            	    mAlertDialog  = null;
            	}
            };
			
            
        });
	}

}
