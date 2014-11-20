/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbCircleProgressBar.java 
 * 描述：环形的ProgressBar
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-9-22 下午2:44:17
 */
public class AbCircleProgressBar extends View {
	
	
	/** The progress. */
	private int progress = 0;
	
	/** The max. */
	private int max = 100;
	
	//绘制轨迹
	/** The path paint. */
	private Paint pathPaint = null;
	
	//绘制填充
	/** The fill arc paint. */
	private Paint fillArcPaint = null;
	
	/** The oval. */
	private RectF oval;
	
	//梯度渐变的填充颜色
	/** The arc colors. */
	private int[] arcColors = new int[] {0xFF02C016,  0xFF3DF346, 0xFF40F1D5, 0xFF02C016 };
	
	/** The shadows colors. */
	private int[] shadowsColors = new int[] { 0xFF111111, 0x00AAAAAA, 0x00AAAAAA };
	//灰色轨迹
	/** The path color. */
	private int pathColor = 0xFFF0EEDF;
	
	/** The path border color. */
	private int pathBorderColor = 0xFFD2D1C4;
	
	//环的路径宽度
	/** The path width. */
	private int pathWidth = 35;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height; 
	
	//默认圆的半径
	/** The radius. */
	private int radius = 120;
	
	// 指定了光源的方向和环境光强度来添加浮雕效果
	/** The emboss. */
	private EmbossMaskFilter emboss = null;
	// 设置光源的方向  
	/** The direction. */
	float[] direction = new float[]{1,1,1};
	//设置环境光亮度  
	/** The light. */
	float light = 0.4f;  
	// 选择要应用的反射等级  
	/** The specular. */
	float specular = 6;  
	// 向 mask应用一定级别的模糊  
	/** The blur. */
	float blur = 3.5f;  
	
	//指定了一个模糊的样式和半径来处理 Paint 的边缘
	/** The m blur. */
	private BlurMaskFilter mBlur = null;
	
	//监听器
	/** The m ab on progress listener. */
	private AbOnProgressListener mAbOnProgressListener = null;
	
	//view重绘的标记
	/** The reset. */
	private boolean reset = false;
	

	/**
	 * Instantiates a new ab circle progress bar.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
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

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
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
	 * 描述：获取圆的半径.
	 *
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}
	
	
    /**
     * 描述：设置圆的半径.
     *
     * @param radius the new radius
     */
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	/**
	 * Gets the max.
	 *
	 * @return the max
	 */
	public int getMax() {
		return max;
	}

	/**
	 * Sets the max.
	 *
	 * @param max the new max
	 */
	public void setMax(int max) {
		this.max = max;
	}

	/**
	 * Gets the progress.
	 *
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * Sets the progress.
	 *
	 * @param progress the new progress
	 */
	public void setProgress(int progress) {
		this.progress = progress;
		this.invalidate();
		if(this.mAbOnProgressListener!=null){
			if(this.max <= this.progress){
				this.mAbOnProgressListener.onComplete();
			}else{
				this.mAbOnProgressListener.onProgress(progress);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override  
	protected void onMeasure (int widthMeasureSpec, int heightMeasureSpec){   
	    int height = View.MeasureSpec.getSize(heightMeasureSpec);    
	    int width = View.MeasureSpec.getSize(widthMeasureSpec);    
	    setMeasuredDimension(width,height);
	}

	/**
	 * Gets the ab on progress listener.
	 *
	 * @return the ab on progress listener
	 */
	public AbOnProgressListener getAbOnProgressListener() {
		return mAbOnProgressListener;
	}

	/**
	 * Sets the ab on progress listener.
	 *
	 * @param mAbOnProgressListener the new ab on progress listener
	 */
	public void setAbOnProgressListener(AbOnProgressListener mAbOnProgressListener) {
		this.mAbOnProgressListener = mAbOnProgressListener;
	}  
	
	
	/**
	 * 描述：重置进度.
	 */
	public void reset(){
		reset  = true;
		this.progress = 0;
		this.invalidate();
	}
	
	/**
	 * 进度监听器.
	 *
	 * @see AbOnProgressEvent
	 */
	public interface AbOnProgressListener {
	    
	    /**
    	 * 描述：进度.
    	 *
    	 * @param progress the progress
    	 */
	    public void onProgress(int progress); 
	    
	    /**
    	 * 完成.
    	 */
	    public void onComplete(); 

	}
	
}
