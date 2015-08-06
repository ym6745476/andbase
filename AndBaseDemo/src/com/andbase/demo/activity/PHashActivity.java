package com.andbase.demo.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.model.AbResult;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.thread.AbTaskPool;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;
import com.ab.util.AbLogUtil;
import com.ab.util.AbMathUtil;
import com.ab.util.AbStrUtil;
import com.ab.util.AbToastUtil;
import com.ab.util.AbViewUtil;
import com.ab.view.chart.CategorySeries;
import com.ab.view.chart.ChartFactory;
import com.ab.view.chart.PointStyle;
import com.ab.view.chart.XYMultipleSeriesDataset;
import com.ab.view.chart.XYMultipleSeriesRenderer;
import com.ab.view.chart.XYSeriesRenderer;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.adapter.ImageShowAdapter;
import com.andbase.global.MyApplication;

public class PHashActivity extends AbActivity {

	private MyApplication application;
	private GridView mGridView = null;
	private ImageShowAdapter mImagePathAdapter = null;
	private ArrayList<String> mPhotoList = new ArrayList<String>();
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
	
	private ImageView myImage = null;
	
	private AbResult mAbResult = null;
	
	private StringBuffer filePathAll = null;
	
	private List<String> hashCodes = new ArrayList<String>();
	private List<File> files = new ArrayList<File>();
	private HashMap<Integer, Integer> hashCodesAndDis = new HashMap<Integer, Integer>();
	private AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
	
	private List<int[]> colorHistogram = new ArrayList<int[]>();
	private LinearLayout mChartLinearLayout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.p_hash);
		application = (MyApplication) abApplication;
		
		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.phash_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);
		
		initTitleRightLayout();
		
		mGridView = (GridView)findViewById(R.id.myGrid);
		mImagePathAdapter = new ImageShowAdapter(this, mPhotoList,116,116);
		mGridView.setAdapter(mImagePathAdapter);
	    mAvatarView = mInflater.inflate(R.layout.choose_avatar, null);
	    //要显示图形的View
	    mChartLinearLayout = (LinearLayout) findViewById(R.id.chart01);
	    //初始化图片保存路径
	    String photo_dir = AbFileUtil.getImageDownloadDir(this);
	    if(AbStrUtil.isEmpty(photo_dir)){
	    	AbToastUtil.showToast(PHashActivity.this,"存储卡不存在");
	    }else{
	    	PHOTO_DIR = new File(photo_dir);
	    }
		
		Button albumButton = (Button)mAvatarView.findViewById(R.id.choose_album);
		Button camButton = (Button)mAvatarView.findViewById(R.id.choose_cam);
		Button cancelButton = (Button)mAvatarView.findViewById(R.id.choose_cancel);
		
		albumButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PHashActivity.this);
				// 从相册中去获取	
				try {
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
				} catch (ActivityNotFoundException e) {
					AbToastUtil.showToast(PHashActivity.this,"没有找到照片");
				}
			}
			
		});
		
		camButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PHashActivity.this);
				doPickPhotoAction();
			}
			
		});
		
		cancelButton.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.removeDialog(PHashActivity.this);
			}
			
		});
		
		Button addBtn = (Button)findViewById(R.id.addBtn);
		Button createBtn = (Button)findViewById(R.id.createBtn);
		myImage = (ImageView)findViewById(R.id.myImage);
		addBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(filePathAll==null){
					AbToastUtil.showToast(PHashActivity.this,"请先创建图片索引!");
				}else{
					AbDialogUtil.showDialog(mAvatarView);
				}
				
			}
			
		});
		
		createBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AbDialogUtil.showProgressDialog(PHashActivity.this,R.drawable.progress_circular,"正在查询...");
				final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(PHashActivity.this);
						AbToastUtil.showToast(PHashActivity.this,"创建图片索引完成!");
					}

					@Override
					public void get() {
			   		    try {
			   		    	filePathAll = new StringBuffer();
			   		    	hashCodes.clear();
			   		    	hashCodesAndDis.clear();
			   		    	files.clear();
			   		    	colorHistogram.clear();
							AbLogUtil.prepareLog(PHashActivity.this);
							//查询手机中所有图片
							if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
							   //无sd卡
							}else{
								 //有sd卡
								 File pathSD = Environment.getExternalStorageDirectory();
					        	 File fileDirectory = new File(pathSD.getAbsolutePath()
					        				+ File.separator + "DCIM" + File.separator);
					        	 listFile(fileDirectory);
					    	}
							//循环所有图片检查相似度
							String fileStrs = filePathAll.toString();
							if(!AbStrUtil.isEmpty(fileStrs)){
								if(fileStrs.indexOf(",")!=-1){
									String [] paths = fileStrs.split(",");
									for(int i=0;i<paths.length;i++){
										String str = paths[i];
										if(!AbStrUtil.isEmpty(str)){
											files.add(new File(str));
										}
									}
								}
							}
							
							//计算图片的hash
							for(int i=0;i<files.size();i++){
								File f = files.get(i);
								AbLogUtil.d(PHashActivity.this, "图片的路径是 = " + f.getPath());
								Bitmap bitmap = AbFileUtil.getBitmapFromSD(f);
								if(bitmap==null){
									//图片有问题
									files.remove(i);
									i--;
									break;
								}else{
									//计算hash
									String hashCode = AbImageUtil.getDCTHashCode(bitmap);
									hashCodes.add(hashCode);
									AbLogUtil.d(PHashActivity.this,"hashCodes add:"+i+":"+hashCode);
									//颜色分布
									Bitmap bitmapT = AbImageUtil.getCutBitmap(bitmap,360,360);
									int [] colors = AbImageUtil.getColorHistogram(bitmapT);
									colorHistogram.add(colors);
									AbImageUtil.releaseBitmap(bitmap);
								}
							}
							AbLogUtil.d(PHashActivity.this, "创建索引",true);
			   		    } catch (Exception e) {
			   		    	AbToastUtil.showToastInThread(PHashActivity.this,e.getMessage());
			   		    }
				  };
				});
				mAbTaskPool.execute(item);
			}
			
		});
		
		
	}
	
	private void initTitleRightLayout() { 
		
	}
	
	/**
	 * 描述：从照相机获取
	 */
	private void doPickPhotoAction() {
		String status = Environment.getExternalStorageState();
		//判断是否有SD卡,如果有sd卡存入sd卡在说，没有sd卡直接转换为图片
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			doTakePhoto();
		} else {
			AbToastUtil.showToast(PHashActivity.this,"没有可用的存储卡");
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
			AbToastUtil.showToast(PHashActivity.this,"未找到系统相机程序");
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
		        	AbToastUtil.showToast(PHashActivity.this,"未在存储卡中找到这个文件");
		        }
				break;
			case CAMERA_WITH_DATA:
				AbLogUtil.d(PHashActivity.this, "将要进行裁剪的图片的路径是 = " + mCurrentPhotoFile.getPath());
				String currentFilePath2 = mCurrentPhotoFile.getPath();
				Intent intent2 = new Intent(this, CropImageActivity.class);
				intent2.putExtra("PATH",currentFilePath2);
				startActivityForResult(intent2,CAMERA_CROP_DATA);
				break;
			case CAMERA_CROP_DATA:
				mChartLinearLayout.removeAllViews();
				String path = mIntent.getStringExtra("PATH");
				AbLogUtil.d(PHashActivity.this, "裁剪后得到的图片的路径是 = " + path);
		    	
		    	Bitmap bitmap = AbFileUtil.getBitmapFromSD(new File(path));
		    	myImage.setImageBitmap(bitmap);
		    	
		    	hashCodesAndDis.clear();
		    	mPhotoList.clear();
		    	mImagePathAdapter.notifyDataSetChanged();
		    	AbViewUtil.setAbsListViewHeight(mGridView,3,25);
		    	
		    	//先过滤颜色相似
		    	//颜色分布
				int [] sourceColors = AbImageUtil.getColorHistogram(bitmap);
		    	
		    	//获取这个图的hashcode
		    	String sourceHashCode = AbImageUtil.getDCTHashCode(bitmap);
		    	AbLogUtil.d(PHashActivity.this,"this image sourceHashCode:"+sourceHashCode);
		    	//计算距离
		    	for(int i = 0;i<hashCodes.size();i++){ 
		    		String hashCode = hashCodes.get(i);
		    		int distanceNew = AbImageUtil.hammingDistance(sourceHashCode, hashCode);
		    		if(distanceNew<5){
		    			hashCodesAndDis.put(i,distanceNew);
		    		}
		    	}
		    	
		    	//排序按距离升序
		    	Set set = hashCodesAndDis.entrySet();
                Map.Entry[] hashCodesAndDisNew = (Map.Entry[]) set.toArray(new Map.Entry[set.size()]);
                Arrays.sort(hashCodesAndDisNew, new Comparator() {
                  public int compare(Object arg0, Object arg1) {
    	             Long key1 = Long.valueOf(((Map.Entry) arg0).getValue().toString());
    	             Long key2 = Long.valueOf(((Map.Entry) arg1).getValue().toString());
    	             return key1.compareTo(key2);
    	           }
                });
		    	
                //显示
                for(int i=0;i<hashCodesAndDisNew.length;i++){
            	   String pathNew = files.get((Integer)hashCodesAndDisNew[i].getKey()).getPath();
            	   AbLogUtil.d(PHashActivity.this,"匹配结果:"+pathNew+":"+hashCodesAndDisNew[i].getValue());
            	   mImagePathAdapter.addItem(mImagePathAdapter.getCount(),pathNew);
            	   
            	   
            	   int [] sourceColors1 = colorHistogram.get(i);
       			   for(int j = 0;j<sourceColors1.length;j++){ 
       		    		sourceColors1[j] = sourceColors[j]-sourceColors1[j];
       		       }
       			   createChart(sourceColors1,"图片"+i);
            	   
            	   AbViewUtil.setAbsListViewHeight(mGridView,3,25);
                }
                
		    
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
	
	public void listFile(File directory){
		if(directory.isDirectory()){
			File[] files = directory.listFiles();
			if(files!=null){
		          for(int i=0;i<files.length;i++){
		            if(files[i].isDirectory()){
		            	listFile(files[i]);
		            }else{
		            	String fName = files[i].getName();
		            	String end = fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase(); 
		            	
		            	if(end.equals("jpg")||end.equals("png")){
		            		filePathAll.append(files[i].getPath()+",");
		            	}
		            }
		          }
		      }
        }
	}
	
	public void createChart(int [] colorsArea,String title){
		
		LinearLayout chartView = (LinearLayout)mInflater.inflate(R.layout.chart_view_item, null);
		
		//说明文字
		String[] titles = new String[] { "颜色差"};
		//数据
	    List<double[]> values = new ArrayList<double[]>();
	    //每个数据点的颜色
	    List<int[]> colors = new ArrayList<int[]>();
	    //每个数据点的简要 说明
	    List<String[]> explains = new ArrayList<String[]>();
	    
	    values.add(AbMathUtil.intToDoubleArray(colorsArea));
	    
	    int [] colorsPoint =  new int[colorsArea.length];
	    //红绿蓝4个区
	    for(int i=0;i<colorsArea.length;i++){
	    	if(i/16>3){
	    		colorsPoint[i] = Color.rgb(255, 63, 63);
	    	}else if(i/16>2){
	    		colorsPoint[i] = Color.rgb(191, 127, 127);
	    	}else if(i/16>1){
	    		colorsPoint[i] = Color.rgb(127, 191, 191);
	    	}else{
	    		colorsPoint[i] = Color.rgb(63, 255, 255);
	    	}
	    	
	    }
	    colors.add(colorsPoint);
	    explains.add(new String[colorsArea.length]);
	    
	    //柱体或者线条颜色
	    int[] mSeriescolors = new int[] { Color.rgb(153, 204, 0)};
	    //创建渲染器
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    int length = mSeriescolors.length;
	    for (int i = 0; i < length; i++) {
	      //创建SimpleSeriesRenderer单一渲染器
	      XYSeriesRenderer r = new XYSeriesRenderer();
	      //SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      //设置渲染器颜色
	      r.setColor(mSeriescolors[i]);
	      r.setFillBelowLine(true);
	      r.setFillBelowLineColor(mSeriescolors[i]);
	      r.setFillPoints(true);
		  r.setPointStyle(PointStyle.CIRCLE);
		  r.setLineWidth(1);
		  r.setChartValuesTextSize(16);
	      //加入到集合中
	      renderer.addSeriesRenderer(r);
	    }
	    //坐标轴标题文字大小
		renderer.setAxisTitleTextSize(16);
		//图形标题文字大小
		renderer.setChartTitleTextSize(25);
		//轴线上标签文字大小
		renderer.setLabelsTextSize(15);
		//说明文字大小
		renderer.setLegendTextSize(15);
		//图表标题
	    renderer.setChartTitle(title);
	    //X轴标题
	    renderer.setXTitle("区间");
	    //Y轴标题
	    renderer.setYTitle("数量");
	    //X轴最小坐标点
	    renderer.setXAxisMin(-2);
	    //X轴最大坐标点
	    renderer.setXAxisMax(64);
	    //Y轴最小坐标点
	    renderer.setYAxisMin(-20000);
	    //Y轴最大坐标点
	    renderer.setYAxisMax(20000);
	    //坐标轴颜色
	    renderer.setAxesColor(Color.rgb(51, 181, 229));
	    renderer.setXLabelsColor(Color.rgb(51, 181, 229));
	    renderer.setYLabelsColor(0,Color.rgb(51, 181, 229));
	    //设置图表上标题与X轴与Y轴的说明文字颜色
	    renderer.setLabelsColor(Color.GRAY);
	    //renderer.setGridColor(Color.GRAY);
	    //设置字体加粗
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);
		//设置在图表上是否显示值标签
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(false);
	    //显示屏幕可见取区的XY分割数
	    renderer.setXLabels(10);
	    renderer.setYLabels(10);
	    //X刻度标签相对X轴位置
	    renderer.setXLabelsAlign(Align.CENTER);
	    //Y刻度标签相对Y轴位置
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(false, false);
	    renderer.setZoomEnabled(false);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);
	    
	    //标尺开启
	    renderer.setScaleLineEnabled(false);
	    //设置标尺提示框高
	    renderer.setScaleRectHeight(10);
	    //设置标尺提示框宽
	    renderer.setScaleRectWidth(150);
	    //设置标尺提示框背景色
	    renderer.setScaleRectColor(Color.argb(150, 52, 182, 232));
	    renderer.setScaleLineColor(Color.argb(175, 150, 150, 150));
	    renderer.setScaleCircleRadius(35);
	    //第一行文字的大小
	    renderer.setExplainTextSize1(20);
	    //第二行文字的大小
	    renderer.setExplainTextSize2(20);
	    
	    //临界线
	    //double[] limit = new double[]{15000,12000,4000,9000};
	    //renderer.setmYLimitsLine(limit);
	    //int[] colorsLimit = new int[] { Color.rgb(100, 255,255),Color.rgb(100, 255,255),Color.rgb(0, 255, 255),Color.rgb(0, 255, 255) };
	    //renderer.setmYLimitsLineColor(colorsLimit);
	    
	    //显示表格线
	    renderer.setShowGrid(true);
	    //如果值是0是否要显示
	    renderer.setDisplayValue0(true);
	    //创建渲染器数据填充器
	    XYMultipleSeriesDataset mXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
	    for (int i = 0; i < length; i++) {
	      CategorySeries series = new CategorySeries(titles[i]);
	      double[] v = values.get(i);
	      int[] c = colors.get(i);
	      String[] e = explains.get(i);
	      int seriesLength = v.length;
	      for (int k = 0; k < seriesLength; k++) {
	    	  //设置每个点的颜色
	          series.add(v[k],c[k],e[k]);
	      }
	      mXYMultipleSeriesDataset.addSeries(series.toXYSeries());
	    }
	    //背景
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.rgb(235, 235, 235));
	    renderer.setMarginsColor(Color.rgb(235, 235, 235));
	    
	    //线图
	    View chart = ChartFactory.getAreaChartView(this,mXYMultipleSeriesDataset,renderer,0.2f);
	    chartView.addView(chart);
	    chartView.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 250));
	    mChartLinearLayout.addView(chartView);
			    
	}
	
	
	

}
