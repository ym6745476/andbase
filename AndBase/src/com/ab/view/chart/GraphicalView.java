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
package com.ab.view.chart;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;

import com.ab.util.AbFileUtil;
import com.ab.util.AbGraphicUtil;
import com.ab.util.AbViewUtil;

// TODO: Auto-generated Javadoc
/**
 * The view that encapsulates the graphical chart.
 */
public class GraphicalView extends View {
	
  /** The m context. */
  private Context mContext;
  /** The chart to be drawn. */
  private AbstractChart mChart;
  /** The chart renderer. */
  private DefaultRenderer mRenderer;
  /** The view bounds. */
  private Rect mRect = new Rect();
  /** The user interface thread handler. */
  private Handler mHandler;
  /** The zoom buttons rectangle. */
  private RectF mZoomR = new RectF();
  /** The zoom in icon. */
  private Bitmap zoomInImage;
  /** The zoom out icon. */
  private Bitmap zoomOutImage;
  /** The fit zoom icon. */
  private Bitmap fitZoomImage;
  /** The zoom buttons background color. */
  private static final int ZOOM_BUTTONS_COLOR = Color.argb(175, 150, 150, 150);
  /** The zoom in tool. */
  private Zoom mZoomIn;
  /** The zoom out tool. */
  private Zoom mZoomOut;
  /** The fit zoom tool. */
  private FitZoom mFitZoom;
  /** The paint to be used when drawing the chart. */
  private Paint mPaint = new Paint();
  /** The touch handler. */
  private ITouchHandler mTouchHandler;
  /** The old x coordinate. */
  private float oldX;
  /** The old y coordinate. */
  private float oldY;
  
  /**
   * Creates a new graphical view.
   * @param context the context
   * @param chart the chart to be drawn
   */
  public GraphicalView(Context context, AbstractChart chart) {
    super(context);
    mContext = context;
    mChart = chart;
    mHandler = new Handler();
    if (mChart instanceof XYChart) {
      mRenderer = ((XYChart) mChart).getRenderer();
    } else {
      mRenderer = ((RoundChart) mChart).getRenderer();
    }
    if (mRenderer.isZoomButtonsVisible()) {
      zoomInImage = AbFileUtil.getBitmapFromSrc("image/zoom_in.png");
      zoomOutImage = AbFileUtil.getBitmapFromSrc("image/zoom_out.png");
      fitZoomImage = AbFileUtil.getBitmapFromSrc("image/zoom-1.png");
    }

    if (mRenderer instanceof XYMultipleSeriesRenderer
        && ((XYMultipleSeriesRenderer) mRenderer).getMarginsColor() == XYMultipleSeriesRenderer.NO_COLOR) {
      ((XYMultipleSeriesRenderer) mRenderer).setMarginsColor(mPaint.getColor());
    }
    if (mRenderer.isZoomEnabled() && mRenderer.isZoomButtonsVisible()
        || mRenderer.isExternalZoomEnabled()) {
      mZoomIn = new Zoom(mChart, true, mRenderer.getZoomRate());
      mZoomOut = new Zoom(mChart, false, mRenderer.getZoomRate());
      mFitZoom = new FitZoom(mChart);
    }
    mTouchHandler = new TouchHandler(this, mChart);
    
    if (mChart instanceof XYChart) {
    	XYMultipleSeriesRenderer  mXYMultipleSeriesRenderer  = ((XYChart) mChart).getRenderer();
    	
    	//根据屏幕大小重置所有尺寸
        int explainTextSize1 = mXYMultipleSeriesRenderer.getExplainTextSize1();
        int explainTextSize2 = mXYMultipleSeriesRenderer.getExplainTextSize2();
        int scaleCircleRadius = mXYMultipleSeriesRenderer.getScaleCircleRadius();
        int scaleRectWidth = mXYMultipleSeriesRenderer.getScaleRectWidth();
        int scaleRectHeight = mXYMultipleSeriesRenderer.getScaleRectHeight();
        //按分辨率转换
        mXYMultipleSeriesRenderer.setExplainTextSize1(AbViewUtil.scaleTextValue(mContext, explainTextSize1));
        mXYMultipleSeriesRenderer.setExplainTextSize2(AbViewUtil.scaleTextValue(mContext, explainTextSize2));
        mXYMultipleSeriesRenderer.setScaleCircleRadius(AbViewUtil.scaleValue(mContext, scaleCircleRadius));
        mXYMultipleSeriesRenderer.setScaleRectWidth(AbViewUtil.scaleValue(mContext, scaleRectWidth));
        mXYMultipleSeriesRenderer.setScaleRectHeight(AbViewUtil.scaleValue(mContext, scaleRectHeight));
        
        SimpleSeriesRenderer[]  mSimpleSeriesRenderers = mRenderer.getSeriesRenderers();
        if(mSimpleSeriesRenderers!=null && mSimpleSeriesRenderers.length>0){
        	for(int i=0;i<mSimpleSeriesRenderers.length;i++){
        		SimpleSeriesRenderer mSimpleSeriesRenderer = mSimpleSeriesRenderers[i];
        		int mChartValuesTextSize  = (int)mSimpleSeriesRenderer.getChartValuesTextSize();
        		mSimpleSeriesRenderer.setChartValuesTextSize(AbViewUtil.scaleTextValue(mContext, mChartValuesTextSize));
        	}
        }
    }
    
    int chartTitleTextSize = (int)mRenderer.getChartTitleTextSize();
    mRenderer.setChartTitleTextSize(AbViewUtil.scaleTextValue(mContext, chartTitleTextSize));
    //轴线上标签文字大小
    int mLabelsTextSize  = (int)mRenderer.getLabelsTextSize();
    mRenderer.setLabelsTextSize(AbViewUtil.scaleTextValue(mContext, mLabelsTextSize));
  	//说明文字大小
    int mLegendTextSize  =  (int)mRenderer.getLegendTextSize();
    mRenderer.setLegendTextSize(AbViewUtil.scaleTextValue(mContext, mLegendTextSize));
    
  }

  /**
   * Returns the current series selection object.
   * 
   * @return the series selection
   */
  public SeriesSelection getCurrentSeriesAndPoint() {
    return mChart.getSeriesAndPointForScreenCoordinate(new Point(oldX, oldY));
  }

  /**
   * Transforms the currently selected screen point to a real point.
   * 
   * @param scale the scale
   * @return the currently selected real point
   */
  public double[] toRealPoint(int scale) {
    if (mChart instanceof XYChart) {
      XYChart chart = (XYChart) mChart;
      return chart.toRealPoint(oldX, oldY, scale);
    }
    return null;
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @param canvas the canvas
   * @see android.view.View#onDraw(android.graphics.Canvas)
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:51
   */
  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.getClipBounds(mRect);
    int top = mRect.top;
    int left = mRect.left;
    int width = mRect.width();
    int height = mRect.height();
    if (mRenderer.isInScroll()) {
      top = 0;
      left = 0;
      width = getMeasuredWidth();
      height = getMeasuredHeight();
    }
    mChart.draw(canvas, left, top, width, height, mPaint);
    
    if (mRenderer != null && mRenderer.isZoomEnabled() && mRenderer.isZoomButtonsVisible()) {
      mPaint.setColor(ZOOM_BUTTONS_COLOR);
      
      int bitmapWidth = zoomInImage.getWidth();
      int bitmapHeight = zoomInImage.getHeight();
      
      int rectMargin = 10;
      int topPadding = 15;
      int leftPadding = 20;
      int leftRect = width - bitmapWidth * 3 - rectMargin - 4*leftPadding;
      int topRect = height - bitmapHeight - rectMargin -2*topPadding; 
      int rightRect = width-rectMargin; 
      int bottomRect = height-rectMargin; 
      
      mZoomR.set(leftRect, topRect, rightRect, bottomRect);
      canvas.drawRoundRect(mZoomR, bitmapWidth / 2, bitmapWidth / 2, mPaint);
      //画图标
      float buttonY = height - bitmapHeight - rectMargin-topPadding;
      canvas.drawBitmap(zoomInImage, width - rectMargin-bitmapWidth*3-3*leftPadding, buttonY, null);
      canvas.drawBitmap(zoomOutImage,width - rectMargin-bitmapWidth*2-2*leftPadding, buttonY, null);
      canvas.drawBitmap(fitZoomImage, width - rectMargin-bitmapWidth-leftPadding, buttonY, null);
    }
    
    if (mChart instanceof XYChart) {
    	XYMultipleSeriesRenderer  mXYMultipleSeriesRenderer  = ((XYChart) mChart).getRenderer();
    
	    if(mXYMultipleSeriesRenderer.isScaleLineEnabled()){
	    	oldX = ((TouchHandler) mTouchHandler).getOldX();
	    	int scaleTopPadding = 50;
	    	//从坐标轴往下
	    	int scaleBottomPadding = 20;
	        int explainTextSize1 = mXYMultipleSeriesRenderer.getExplainTextSize1();
	        int explainTextSize2 = mXYMultipleSeriesRenderer.getExplainTextSize2();
	        int scaleCircleRadius = mXYMultipleSeriesRenderer.getScaleCircleRadius();
	        //按分辨率转换
	        scaleTopPadding = AbViewUtil.scaleValue(mContext, scaleTopPadding);
	        scaleBottomPadding = AbViewUtil.scaleValue(mContext, scaleBottomPadding);
	        
	        
	        //Y轴位置
	        int bottomY = 0;
	        int topY = 0;
	        if (mChart instanceof XYChart) {
	      	  Rect mScreenR = ((XYChart) mChart).getScreenR();
	      	  bottomY = mScreenR.bottom;
	      	  topY = mScreenR.top;
	      	  if(oldX == 0){
	      		oldX = mScreenR.right;
	      	  }
	        } else {
	        }
	        
	        //标尺线与拖手
	        mPaint.setColor(mXYMultipleSeriesRenderer.getScaleLineColor());
	        canvas.drawLine(oldX,scaleTopPadding,oldX, bottomY+scaleBottomPadding, mPaint);
	        mPaint.setStyle(Paint.Style.FILL);
	        mPaint.setColor(ZOOM_BUTTONS_COLOR);
	        canvas.drawCircle(oldX, bottomY+scaleBottomPadding+scaleCircleRadius, scaleCircleRadius, mPaint);
	        
	        
	        //判断挂到哪个点上
	        if(mChart instanceof XYChart){
	          List<Float> mPoints = null;
	          List<Double> mValues = null;
	          List<String> mExplains = null;
	          float minValue = -1;
	          int minIndex = -1;
	          //要显示的文本
	          String showValue = "";
	          String showExplain = "";
	          Map<Integer, List<Float>> points = ((XYChart) mChart).getPoints();
	          Map<Integer, List<Double>> values = ((XYChart) mChart).getValues();
	          Map<Integer, List<String>> explains = ((XYChart) mChart).getExplains();
	          for (Entry<Integer, List<Float>> value : points.entrySet()) {
	            int index = value.getKey();
	            mPoints = value.getValue();
	            mValues = values.get(value.getKey());
	            mExplains = explains.get(value.getKey());
	            
	            //判断距离在2范围的点的索引
	            minValue = 1000;
	            minIndex = -1;
	           
	            for(int i=0;i<mPoints.size();i+=2){
	              Float f = mPoints.get(i);
	              if(Math.abs(f-oldX)<minValue){
	                minValue = Math.abs(f-oldX);
	                minIndex = i;
	              }
	            }
	            if(index==0){
	               showValue = String.valueOf((double)mValues.get(minIndex+1)).replace(".0", "");
	               String showExplainT = mExplains.get(minIndex/2);
	               if(showExplainT!=null && !"".equals(showExplainT.trim())){
	                 showExplain = mExplains.get(minIndex/2);
	               }
	            }else{
	               showValue = showValue+"/"+String.valueOf((double)mValues.get(minIndex+1)).replace(".0", "");
	               String showExplainT = mExplains.get(minIndex/2);
	               if(showExplainT!=null && !"".equals(showExplainT.trim())){
	                 if(showExplain!=null && !"".equals(showExplain.trim())){
	                   showExplain = showExplain+"/"+mExplains.get(minIndex/2);
	                 }else{
	                   showExplain = mExplains.get(minIndex/2);
	                 }
	               }
	               
	            }
	            
	          }
	          if(minValue<5){
	              
	              boolean showRect = false;
	              if(mXYMultipleSeriesRenderer.isDisplayValue0()){
	                  showRect = true;
	              }else{
	                  if("0".equals(showValue) || "0/0".equals(showValue)){
	                       showRect = false;
	                  }else{
	                    showRect = true;
	                  }
	              }
	              if(showRect){
	                  //画框框和点
	            	  
	            	  //获取文本的高度
	            	  TextPaint mTextPaint1 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	            	  mTextPaint1.setColor(Color.WHITE);
	                  mTextPaint1.setTypeface(Typeface.DEFAULT);
	                  mTextPaint1.setTextSize(explainTextSize1);
	                  
	                  TextPaint mTextPaint2 = new TextPaint(Paint.ANTI_ALIAS_FLAG);
	                  mTextPaint2.setColor(Color.WHITE);
	                  mTextPaint2.setTypeface(Typeface.DEFAULT);
	                  mTextPaint2.setTextSize(explainTextSize2);
	                  
	                  //行高
	                  float hSize1 = AbGraphicUtil.getDesiredHeight(mTextPaint1);
	                  
	                  //行高
	                  float hSize2 = AbGraphicUtil.getDesiredHeight(mTextPaint2);
	                  
	                  int textPadding = 8;
	                  textPadding = AbViewUtil.scaleValue(mContext, textPadding);
	                  
	                  //设置个新的长方形  
	                  //判断文字是否超出设置的框框
	                  int row1 = AbGraphicUtil.getDrawRowCount(showValue,mXYMultipleSeriesRenderer.getScaleRectWidth()-10,mTextPaint1);
	                  int row2 = AbGraphicUtil.getDrawRowCount(showExplain,mXYMultipleSeriesRenderer.getScaleRectWidth()-10,mTextPaint2);
	                  RectF mRectF = null;
	                  int textHeight = (int)(row1*hSize1+row2*hSize2+textPadding);
	                  
	                  //提示框的宽度和高度
	                  int realScaleRectWidth = mXYMultipleSeriesRenderer.getScaleRectWidth();
	                  int realScaleRectHeight = mXYMultipleSeriesRenderer.getScaleRectHeight();
	                  
	                  //画圆角矩形  //充满
	                  mPaint.setStyle(Paint.Style.FILL);  
	                  mPaint.setColor(mXYMultipleSeriesRenderer.getScaleRectColor()); 
	                  //设置画笔的锯齿效果  
	                  mPaint.setAntiAlias(true);
	                  
	                  int mRectLeft  = (int)(double)mPoints.get(minIndex);
	                  int mRectTop  = (int)(double)mPoints.get(minIndex+1);
	                  int mRectRight  = mRectLeft+realScaleRectWidth;
	                  int mRectBottom  = mRectTop+realScaleRectHeight;
	                  
	                  if(textHeight > realScaleRectHeight){
	                	  realScaleRectHeight = textHeight;
	                	  mRectBottom = mRectTop + realScaleRectHeight;
	                  }
	                  
	                  //判断是否会超出屏幕
	                  if(mRectRight>width){
	                    //超出了
	                    mRectLeft  = (int)(double)mPoints.get(minIndex)-5-mXYMultipleSeriesRenderer.getScaleRectWidth();
	                    mRectRight = (int)(double)mPoints.get(minIndex)-5;
	                  }
	                  if(mRectBottom>bottomY){
	                    //超出了
	                    mRectTop  = (int)(double)mPoints.get(minIndex+1)-5-realScaleRectHeight;
	                    mRectBottom = (int)(double)mPoints.get(minIndex+1)-5;
	                    
	                    //是否超出Y=0的位置的
	                    if(mRectTop<topY){
	                      mRectTop = mRectTop+realScaleRectHeight/2;
	                      mRectBottom = mRectBottom+realScaleRectHeight/2;
	                    }
	                  }
	                 
	                  mRectF = new RectF(mRectLeft,mRectTop, mRectRight,mRectBottom);
	                  //第二个参数是x半径，第三个参数是y半径
	                  canvas.drawRoundRect(mRectF, 5, 5, mPaint);
	                  
	                  AbGraphicUtil.drawText(canvas,showValue,mXYMultipleSeriesRenderer.getScaleRectWidth(),mTextPaint1,mRectLeft+textPadding, mRectTop+textPadding);
	                  AbGraphicUtil.drawText(canvas,showExplain,mXYMultipleSeriesRenderer.getScaleRectWidth(),mTextPaint2,mRectLeft+textPadding, (int)(mRectTop+textPadding+row1*hSize1));
	                  
	              }
	          }
	          
	        }
	     }
    }
    
  }

  /**
   * Sets the zoom rate.
   * 
   * @param rate the zoom rate
   */
  public void setZoomRate(float rate) {
    if (mZoomIn != null && mZoomOut != null) {
      mZoomIn.setZoomRate(rate);
      mZoomOut.setZoomRate(rate);
    }
  }

  /**
   * Do a chart zoom in.
   */
  public void zoomIn() {
    if (mZoomIn != null) {
      mZoomIn.apply(Zoom.ZOOM_AXIS_XY);
      repaint();
    }
  }

  /**
   * Do a chart zoom out.
   */
  public void zoomOut() {
    if (mZoomOut != null) {
      mZoomOut.apply(Zoom.ZOOM_AXIS_XY);
      repaint();
    }
  }
  


  /**
   * Do a chart zoom reset / fit zoom.
   */
  public void zoomReset() {
    if (mFitZoom != null) {
      mFitZoom.apply();
      mZoomIn.notifyZoomResetListeners();
      repaint();
    }
  }

  /**
   * Adds a new zoom listener.
   *
   * @param listener zoom listener
   * @param onButtons the on buttons
   * @param onPinch the on pinch
   */
  public void addZoomListener(ZoomListener listener, boolean onButtons, boolean onPinch) {
    if (onButtons) {
      if (mZoomIn != null) {
        mZoomIn.addZoomListener(listener);
        mZoomOut.addZoomListener(listener);
      }
      if (onPinch) {
        mTouchHandler.addZoomListener(listener);
      }
    }
  }

  /**
   * Removes a zoom listener.
   * 
   * @param listener zoom listener
   */
  public synchronized void removeZoomListener(ZoomListener listener) {
    if (mZoomIn != null) {
      mZoomIn.removeZoomListener(listener);
      mZoomOut.removeZoomListener(listener);
    }
    mTouchHandler.removeZoomListener(listener);
  }

  /**
   * Adds a new pan listener.
   * 
   * @param listener pan listener
   */
  public void addPanListener(PanListener listener) {
    mTouchHandler.addPanListener(listener);
  }

  /**
   * Removes a pan listener.
   * 
   * @param listener pan listener
   */
  public void removePanListener(PanListener listener) {
    mTouchHandler.removePanListener(listener);
  }

  /**
   * Gets the zoom rectangle.
   *
   * @return the zoom rectangle
   */
  protected RectF getZoomRectangle() {
    return mZoomR;
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @param event the event
   * @return true, if successful
   * @see android.view.View#onTouchEvent(android.view.MotionEvent)
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:51
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    	//判断事件的位置 是标尺 还是图表
        if(mRenderer != null){
            //分解事件
            float newX = event.getX();
            float newY = event.getY();
            //坐标轴之外事件   
            //int bottom = y + height - margins[2] - legendSize;
            int bottom = 0;
            if (mChart instanceof XYChart) {
          	  Rect mScreenR = ((XYChart) mChart).getScreenR();
          	  bottom = mScreenR.bottom;
            } else {
            }
            if(newY >= bottom-10){
          	//不是图表事件
              if (mTouchHandler.handleTouchControl(event)) {
                return true;
              }
            }else{
          	  //可拖动，缩放打开默认拖动也打开
          	  if ((mRenderer.isPanEnabled() || mRenderer.isZoomEnabled())) {
                    if (mTouchHandler.handleTouch(event)) {
                        return true;
                    }
                }
            }
        }
        return super.onTouchEvent(event);
  }

  /**
   * Schedule a view content repaint.
   */
  public void repaint() {
    mHandler.post(new Runnable() {
      public void run() {
        invalidate();
      }
    });
  }

  /**
   * Schedule a view content repaint, in the specified rectangle area.
   * 
   * @param left the left position of the area to be repainted
   * @param top the top position of the area to be repainted
   * @param right the right position of the area to be repainted
   * @param bottom the bottom position of the area to be repainted
   */
  public void repaint(final int left, final int top, final int right, final int bottom) {
    mHandler.post(new Runnable() {
      public void run() {
        invalidate(left, top, right, bottom);
      }
    });
  }

  /**
   * Saves the content of the graphical view to a bitmap.
   * 
   * @return the bitmap
   */
  public Bitmap toBitmap() {
    setDrawingCacheEnabled(false);
    if (!isDrawingCacheEnabled()) {
      setDrawingCacheEnabled(true);
    }
    if (mRenderer.isApplyBackgroundColor()) {
      setDrawingCacheBackgroundColor(mRenderer.getBackgroundColor());
    }
    setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
    return getDrawingCache(true);
  }

}
