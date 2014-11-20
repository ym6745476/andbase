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
package com.ab.view.level;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;

import com.ab.util.AbGraphicUtil;
import com.ab.util.AbViewUtil;

// TODO: Auto-generated Javadoc
/**
 * The Class AbLevelChart.
 */
public class AbLevelChart extends AbLevelAbstractChart {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The m dataset. */
	protected AbLevelSeriesDataset mDataset;
	
	/** The m renderer. */
	protected AbLevelSeriesRenderer mRenderer;
	
	/** 等级图形的宽. */
	private int measureWidth;
	
	/** 等级图形的高. */
	private int measureHeight;
	
	/** 各等级段的颜色. */
	private int [] color = null;
	
	/** 假如 每段的百分比  2  3  2  1  1  1. */
	private float [] part = null;
	
	/** 各等级段的值. */
	private float [] partValue = null;
	
	/** 当前值. */
	private String textValue = null;
	
	/** 当前值描述. */
	private String textDesc = null;
	
	/** 当前值的等级. */
	private int textlevelIndex = 0;
	
	/** 当前值文字大小. */
	private int textLevelSize = 30;
	
	/** 当前值文字与顶部的距离. */
	private int marginTop = 30;
	
	/** 指示三角形的宽度. */
	private int arrowWidth  = 20;
	
	/** 指示三角形的高度. */
	private int arrowHeight = 10;
	
	/** 等级条的高度. */
	private int levelHeight = 20;
	
	/** 指示三角形与其他间距. */
	private int arrowMarginTop = 10;
	
	/** 等级坐标文字大小. */
	private int partTextSize = 15;
	
	/** 等级说明文字大小. */
	private int textDescSize = 22;

	/**
	 * Instantiates a new ab level chart.
	 */
	protected AbLevelChart() {
	}

	/**
	 * Instantiates a new ab level chart.
	 *
	 * @param mDataset the m dataset
	 * @param mRenderer the m renderer
	 */
	public AbLevelChart(AbLevelSeriesDataset mDataset,AbLevelSeriesRenderer mRenderer) {
		super();
		this.mDataset = mDataset;
		this.mRenderer = mRenderer;
		this.measureWidth = mRenderer.getWidth();
		this.measureHeight = mRenderer.getHeight();
	}

	/**
	 * Sets the dataset renderer.
	 *
	 * @param dataset the dataset
	 * @param renderer the renderer
	 */
	protected void setDatasetRenderer(AbLevelSeriesDataset dataset,AbLevelSeriesRenderer renderer) {
		mDataset = dataset;
		mRenderer = renderer;
	}

	/**
	 * 描述：绘制.
	 *
	 * @param canvas the canvas
	 * @param x the x
	 * @param y the y
	 * @param measureWidth the measure width
	 * @param measureHeight the measure height
	 * @param screenWidth the screen width
	 * @param screenHeight the screen height
	 * @param paint the paint
	 * @see com.ab.view.level.AbLevelAbstractChart#draw(android.graphics.Canvas, int, int, int, int, int, int, android.graphics.Paint)
	 */
	public void draw(Canvas canvas, int x, int y,int measureWidth,int measureHeight,int screenWidth, int screenHeight,
			Paint paint) {
		
		//各等级段的颜色
		color = mRenderer.getColor();
		//假如 每段的百分比  2  3  2  1  1  1
		part = mRenderer.getPart();
		//各等级段的值
		partValue = mRenderer.getPartValue();
		//当前值
		textValue = mRenderer.getTextValue();
		//当前值描述
		textDesc = mRenderer.getTextDesc();
		//当前值的等级
		textlevelIndex = mRenderer.getTextlevelIndex();
		//当前值文字大小
		textLevelSize = mRenderer.getTextLevelSize();
		textLevelSize = AbViewUtil.scale(screenWidth,screenHeight, textLevelSize);
		//当前值文字与顶部的距离
		marginTop = mRenderer.getMarginTop();
		//指示三角形的宽度
		arrowWidth  = mRenderer.getArrowWidth();
		//指示三角形的高度
		arrowHeight = mRenderer.getArrowHeight();
		//等级条的高度
		levelHeight = mRenderer.getLevelHeight();
		//指示三角形与其他间距
		arrowMarginTop = mRenderer.getArrowMarginTop();
		//等级条坐标文字大小
		partTextSize = mRenderer.getPartTextSize();
		textDescSize = mRenderer.getTextDescSize();
		//获取值的文本的高度
        TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mTextPaint.setTextSize(textLevelSize);
        FontMetrics fm  = mTextPaint.getFontMetrics();
        //得到行高
        int textHeight = (int)Math.ceil(fm.descent - fm.ascent)+2-20;
        int textWidth = (int)AbGraphicUtil.getStringWidth(textValue,mTextPaint);
        
        int left = (screenWidth-measureWidth)/2;
		
        //绘制level条
		int topLevel = marginTop+textHeight+arrowHeight+arrowMarginTop;
		RectF mLevelRect = new RectF(left,topLevel ,left+measureWidth,topLevel+levelHeight);
		paint.setStyle(Paint.Style.FILL);  
        //设置画笔的锯齿效果  
		paint.setAntiAlias(true);
		paint.setStrokeWidth(2);
		paint.setColor(Color.rgb(228, 228, 228));
		canvas.drawRoundRect(mLevelRect, 1, 1, paint);
		
		int partWidth = measureWidth/10;
		RectF mLevelRectPart = null;
		float sumLeft = 0;
		float sumRight = 0;
		for(int i=0;i<color.length;i++){
			if(i==0){
				sumLeft = left;
				sumRight = sumLeft + part[i]*partWidth;
				mLevelRectPart = new RectF(sumLeft,topLevel ,sumRight,topLevel+levelHeight);
			}else{
				sumLeft  += part[i-1]*partWidth;
				sumRight += part[i]*partWidth;
				mLevelRectPart = new RectF(sumLeft+1,topLevel ,sumRight,topLevel+levelHeight);
			}
			paint.setColor(color[i]);
			//当前的值
			if(textlevelIndex == i){
				
				paint.setFlags(Paint.ANTI_ALIAS_FLAG);
				paint.setTextSize(textLevelSize);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				float textLeftOffset = (part[i]*partWidth-textWidth)/2;
				canvas.drawText(textValue,sumLeft+textLeftOffset,marginTop, paint);
				float arrowLeftOffset = (part[i]*partWidth-arrowWidth)/2;
				float center = sumLeft+arrowLeftOffset+arrowWidth/2;
				paint.setStyle(Paint.Style.FILL);  
				paint.setColor(Color.rgb(153, 234, 71));  
				Path path1 = new Path();  
				path1.moveTo(center,marginTop+textHeight+arrowHeight);  
				path1.lineTo(sumLeft+arrowLeftOffset,marginTop+textHeight);  
				path1.lineTo(sumLeft+arrowLeftOffset+arrowWidth,marginTop+textHeight);  
				path1.close();
				canvas.drawPath(path1, paint);
				
				//绘制等级条下方的倒三角
				paint.setColor(Color.rgb(227, 227, 227));  
				paint.setStyle(Paint.Style.FILL);  
				Path path2 = new Path();  
				path2.moveTo(center,marginTop+textHeight+arrowHeight+levelHeight+2*arrowMarginTop);  
				path2.lineTo(sumLeft+arrowLeftOffset,marginTop+textHeight+levelHeight+2*arrowHeight+2*arrowMarginTop);  
				path2.lineTo(sumLeft+arrowLeftOffset+arrowWidth,marginTop+textHeight+levelHeight+2*arrowHeight+2*arrowMarginTop);  
				path2.close();
				canvas.drawPath(path2, paint);
				
				//绘制等级条下方的文字描述
				int topDesc = marginTop+textHeight+2*arrowHeight+2*arrowMarginTop+levelHeight;
				RectF mLevelDescRect = new RectF(center-mRenderer.getTextRectWidth()/2,topDesc ,center+mRenderer.getTextRectWidth()/2,topDesc+mRenderer.getTextRectHeight());
				canvas.drawRoundRect(mLevelDescRect,5, 5, paint);
				paint.setTypeface(Typeface.DEFAULT_BOLD);
				paint.setTextSize(textDescSize);
				paint.setColor(Color.rgb(157, 157, 157));
				mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
		        mTextPaint.setTextSize(textDescSize);
		        FontMetrics textDescFm  = mTextPaint.getFontMetrics();
		        //得到行高
		        int textDescHeight = (int)Math.ceil(textDescFm.descent - textDescFm.ascent)+2;
		        int textDescWidth = (int)AbGraphicUtil.getStringWidth(textDesc,mTextPaint);
				canvas.drawText(textDesc,center-textDescWidth/2,topDesc+20+((mRenderer.getTextRectHeight()-textDescHeight)/2),paint);
				paint.setColor(color[i]);
				
			}
			//绘制等级段
			canvas.drawRoundRect(mLevelRectPart, 1, 1, paint);
			
			//绘制段坐标
			if(partValue!=null && partValue.length == color.length){
				paint.setTextSize(partTextSize);
		        mTextPaint.setTextSize(partTextSize);
		        int partValueWidth = (int)AbGraphicUtil.getStringWidth(String.valueOf(partValue[i]),mTextPaint);
				canvas.drawText(String.valueOf(partValue[i]),mLevelRectPart.left-partValueWidth/2,mLevelRectPart.top+levelHeight+15,paint);
			}
			
		}
		
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return measureWidth;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return measureHeight;
	}

}
