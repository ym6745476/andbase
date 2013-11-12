package com.ab.view.progress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.ab.view.listener.AbOnProgressListener;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：AbCircleProgressBar.java 
 * 描述：环形的ProgressBar
 * @author zhaoqp
 * @date：2013-9-22 下午2:44:17
 * @version v1.0
 */
public class AbCircleProgressBar extends View {
	
	
	private int progress = 0;
	private int max = 100;
	
	//绘制轨迹
	private Paint pathPaint = null;
	
	//绘制填充
	private Paint fillArcPaint = null;
	
	private RectF oval;
	
	//梯度渐变的填充颜色
	private int[] arcColors = new int[] {0xFF02C016,  0xFF3DF346, 0xFF40F1D5, 0xFF02C016 };
	private int[] shadowsColors = new int[] { 0xFF111111, 0x00AAAAAA, 0x00AAAAAA };
	//灰色轨迹
	private int pathColor = 0xFFF0EEDF;
	private int pathBorderColor = 0xFFD2D1C4;
	
	//环的路径宽度
	private int pathWidth = 35;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height; 
	
	//默认圆的半径
	private int radius = 120;
	
	// 指定了光源的方向和环境光强度来添加浮雕效果
	private EmbossMaskFilter emboss = null;
	// 设置光源的方向  
	float[] direction = new float[]{1,1,1};
	//设置环境光亮度  
	float light = 0.4f;  
	// 选择要应用的反射等级  
	float specular = 6;  
	// 向 mask应用一定级别的模糊  
	float blur = 3.5f;  
	
	//指定了一个模糊的样式和半径来处理 Paint 的边缘
	private BlurMaskFilter mBlur = null;
	
	//监听器
	private AbOnProgressListener mAbOnProgressListener = null;
	
	//view重绘的标记
	private boolean reset = false;
	

	public AbCircleProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		pathPaint  = new Paint();
		// 设置是否抗锯齿
		pathPaint.setAntiAlias(true);
		// 帮助消除锯齿
		pathPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// 设置中空的样式
		pathPaint.setStyle(Paint.Style.STROKE);
		pathPaint.setDither(true);
		pathPaint.setStrokeJoin(Paint.Join.ROUND);
		
		fillArcPaint = new Paint();
		// 设置是否抗锯齿
		fillArcPaint.setAntiAlias(true);
		// 帮助消除锯齿
		fillArcPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		// 设置中空的样式
		fillArcPaint.setStyle(Paint.Style.STROKE);
		fillArcPaint.setDither(true);
		fillArcPaint.setStrokeJoin(Paint.Join.ROUND);
				
		oval = new RectF();
		emboss = new EmbossMaskFilter(direction,light,specular,blur);  
		mBlur = new BlurMaskFilter(20, BlurMaskFilter.Blur.NORMAL);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(reset){
			canvas.drawColor(Color.TRANSPARENT);
			reset = false;
		}
		this.width = getMeasuredWidth();
		this.height = getMeasuredHeight();
		this.radius = getMeasuredWidth()/2 - pathWidth;
		
		// 设置画笔颜色
		pathPaint.setColor(pathColor);
		// 设置画笔宽度
		pathPaint.setStrokeWidth(pathWidth);
		
		//添加浮雕效果
		pathPaint.setMaskFilter(emboss); 
		
		// 在中心的地方画个半径为r的圆
		canvas.drawCircle(this.width/2, this.height/2, radius, pathPaint);
		
		//边线
		pathPaint.setStrokeWidth(0.5f);
		pathPaint.setColor(pathBorderColor);
		canvas.drawCircle(this.width/2, this.height/2, radius+pathWidth/2+0.5f, pathPaint);
		canvas.drawCircle(this.width/2, this.height/2, radius-pathWidth/2-0.5f, pathPaint);
		
		
		/*int[] gradientColors = new int[3];  
		gradientColors[0] = Color.GREEN;  
		gradientColors[1] = Color.YELLOW;  
		gradientColors[2] = Color.RED;  
		float[] gradientPositions = new float[3];  
		gradientPositions[0] = 0.0f;  
		gradientPositions[1] = 0.5f;  
		gradientPositions[2] = 1.0f;  
		
		//按颜色比例圆形填充
		RadialGradient radialGradientShader = new RadialGradient(this.width/2,this.height/2, 
				radius, gradientColors, gradientPositions, TileMode.CLAMP); 
		
		paint1.setShader(radialGradientShader);*/
		
		//环形颜色填充
		SweepGradient sweepGradient = new SweepGradient(this.width/2, this.height/2, arcColors, null);
		fillArcPaint.setShader(sweepGradient);
		// 设置画笔为白色
		
		//模糊效果
		fillArcPaint.setMaskFilter(mBlur);
		
		//设置线的类型,边是圆的
		fillArcPaint.setStrokeCap(Paint.Cap.ROUND);
		
		//fillArcPaint.setColor(Color.BLUE);
		
		fillArcPaint.setStrokeWidth(pathWidth);
		// 设置类似于左上角坐标，右下角坐标
		oval.set(this.width/2 - radius, this.height/2 - radius, this.width/2 + radius, this.height/2 + radius);
		// 画圆弧，第二个参数为：起始角度，第三个为跨的角度，第四个为true的时候是实心，false的时候为空心
		canvas.drawArc(oval, -90, ((float) progress / max) * 360, false, fillArcPaint);
		
	}

	/**
	 * 
	 * 描述：获取圆的半径
	 * @return
	 * @throws 
	 */
	public int getRadius() {
		return radius;
	}
	
	
    /**
     * 
     * 描述：设置圆的半径
     * @param radius
     * @throws 
     */
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
		if(this.mAbOnProgressListener!=null){
			if(this.max <= this.progress){
				this.mAbOnProgressListener.onComplete(progress);
			}else{
				this.mAbOnProgressListener.onProgress(progress);
			}
		}
	}
	
	@Override  
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){   
	    int height = View.MeasureSpec.getSize(heightMeasureSpec);    
	    int width = View.MeasureSpec.getSize(widthMeasureSpec);    
	    setMeasuredDimension(width,height);
	}

	public AbOnProgressListener getAbOnProgressListener() {
		return mAbOnProgressListener;
	}

	public void setAbOnProgressListener(AbOnProgressListener mAbOnProgressListener) {
		this.mAbOnProgressListener = mAbOnProgressListener;
	}  
	
	
	/**
	 * 
	 * 描述：重置进度
	 * @throws 
	 */
	public void reset(){
		reset  = true;
		this.progress = 0;
		this.invalidate();
	}
	
	
}
