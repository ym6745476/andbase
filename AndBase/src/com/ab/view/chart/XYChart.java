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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedMap;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Join;
import android.graphics.Paint.Style;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.Log;

import com.ab.view.chart.XYMultipleSeriesRenderer.Orientation;

// TODO: Auto-generated Javadoc
/**
 * The XY chart rendering class.
 */
public abstract class XYChart extends AbstractChart {
  /** The multiple series dataset. */
  protected XYMultipleSeriesDataset mDataset;
  /** The multiple series renderer. */
  protected XYMultipleSeriesRenderer mRenderer;
  /** The current scale value. */
  private float mScale;
  /** The current translate value. */
  private float mTranslate;
  /** The canvas center point. */
  private Point mCenter;
  /** The visible chart area, in screen coordinates. */
  private Rect mScreenR;
  /** The calculated range. */
  private final Map<Integer, double[]> mCalcRange = new HashMap<Integer, double[]>();

  /**
   * The clickable areas for all points. The array index is the series index,
   * and the RectF list index is the point index in that series.
   */
  private Map<Integer, List<ClickableArea>> clickableAreas = new HashMap<Integer, List<ClickableArea>>();
  
  /** 点的值. */
  private Map<Integer, List<Double>> values = new HashMap<Integer, List<Double>>();;
  
  /** 点的真实坐标. */
  private Map<Integer, List<Float>> points = new HashMap<Integer, List<Float>>();
  
  /** 每个数据点的简要 说明. */
  private final Map<Integer, List<Integer>> colors = new HashMap<Integer, List<Integer>>();
  
  /** 每个数据点的简要 说明. */
  private final Map<Integer, List<String>> explains = new HashMap<Integer, List<String>>();

  
  /**
   * Instantiates a new xY chart.
   */
  protected XYChart() {
  }

  /**
   * Builds a new XY chart instance.
   * 
   * @param dataset the multiple series dataset
   * @param renderer the multiple series renderer
   */
  public XYChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    mDataset = dataset;
    Log.d("XYChart", "XYMultipleSeriesDataset 设置成功");
    mRenderer = renderer;
  }

  // TODO: javadoc
  /**
   * Sets the dataset renderer.
   *
   * @param dataset the dataset
   * @param renderer the renderer
   */
  protected void setDatasetRenderer(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    mDataset = dataset;
    mRenderer = renderer;
  }

  /**
   * The graphical representation of the XY chart.
   * 
   * @param canvas the canvas to paint to
   * @param x the top left x value of the view to draw to
   * @param y the top left y value of the view to draw to
   * @param width the width of the view to draw to
   * @param height the height of the view to draw to
   * @param paint the paint
   */
  public void draw(Canvas canvas, int x, int y, int width, int height, Paint paint) {
	
	Log.d("XYChart", "draw在这里初始化点的数据的");
    paint.setAntiAlias(mRenderer.isAntialiasing());
    int legendSize = getLegendSize(mRenderer, height / 5, mRenderer.getAxisTitleTextSize());
    int[] margins = mRenderer.getMargins();
    int left = x + margins[1];
    int top = y + margins[0];
    int right = x + width - margins[3];
    int sLength = mDataset.getSeriesCount();
    String[] titles = new String[sLength];
    for (int i = 0; i < sLength; i++) {
      titles[i] = mDataset.getSeriesAt(i).getTitle();
    }
    if (mRenderer.isFitLegend() && mRenderer.isShowLegend()) {
      legendSize = drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize,paint, true);
    }
    int bottom = y + height - margins[2] - legendSize;
    if (mScreenR == null) {
      mScreenR = new Rect();
    }
    mScreenR.set(left, top, right, bottom);
    drawBackground(mRenderer, canvas, x, y, width, height, paint, false, DefaultRenderer.NO_COLOR);

    if (paint.getTypeface() == null
        || !paint.getTypeface().toString().equals(mRenderer.getTextTypefaceName())
        || paint.getTypeface().getStyle() != mRenderer.getTextTypefaceStyle()) {
      paint.setTypeface(Typeface.create(mRenderer.getTextTypefaceName(),
          mRenderer.getTextTypefaceStyle()));
    }
    Orientation or = mRenderer.getOrientation();
    if (or == Orientation.VERTICAL) {
      right -= legendSize;
      bottom += legendSize - 20;
    }
    int angle = or.getAngle();
    boolean rotate = angle == 90;
    mScale = (float) (height) / width;
    mTranslate = Math.abs(width - height) / 2;
    if (mScale < 1) {
      mTranslate *= -1;
    }
    mCenter = new Point((x + width) / 2, (y + height) / 2);
    if (rotate) {
      transform(canvas, angle, false);
    }

    int maxScaleNumber = -Integer.MAX_VALUE;
    for (int i = 0; i < sLength; i++) {
      maxScaleNumber = Math.max(maxScaleNumber, mDataset.getSeriesAt(i).getScaleNumber());
    }
    maxScaleNumber++;
    if (maxScaleNumber < 0) {
      return;
    }
    //坐标轴的值
    double[] minX = new double[maxScaleNumber];
    double[] maxX = new double[maxScaleNumber];
    double[] minY = new double[maxScaleNumber];
    double[] maxY = new double[maxScaleNumber];
    boolean[] isMinXSet = new boolean[maxScaleNumber];
    boolean[] isMaxXSet = new boolean[maxScaleNumber];
    boolean[] isMinYSet = new boolean[maxScaleNumber];
    boolean[] isMaxYSet = new boolean[maxScaleNumber];

    for (int i = 0; i < maxScaleNumber; i++) {
      minX[i] = mRenderer.getXAxisMin(i);
      maxX[i] = mRenderer.getXAxisMax(i);
      minY[i] = mRenderer.getYAxisMin(i);
      maxY[i] = mRenderer.getYAxisMax(i);
      isMinXSet[i] = mRenderer.isMinXSet(i);
      isMaxXSet[i] = mRenderer.isMaxXSet(i);
      isMinYSet[i] = mRenderer.isMinYSet(i);
      isMaxYSet[i] = mRenderer.isMaxYSet(i);
      if (mCalcRange.get(i) == null) {
        mCalcRange.put(i, new double[4]);
      }
    }
    double[] xPixelsPerUnit = new double[maxScaleNumber];
    double[] yPixelsPerUnit = new double[maxScaleNumber];
    for (int i = 0; i < sLength; i++) {
      XYSeries series = mDataset.getSeriesAt(i);
      int scale = series.getScaleNumber();
      if (series.getItemCount() == 0) {
        continue;
      }
      if (!isMinXSet[scale]) {
        double minimumX = series.getMinX();
        minX[scale] = Math.min(minX[scale], minimumX);
        mCalcRange.get(scale)[0] = minX[scale];
      }
      if (!isMaxXSet[scale]) {
        double maximumX = series.getMaxX();
        maxX[scale] = Math.max(maxX[scale], maximumX);
        mCalcRange.get(scale)[1] = maxX[scale];
      }
      if (!isMinYSet[scale]) {
        double minimumY = series.getMinY();
        minY[scale] = Math.min(minY[scale], (float) minimumY);
        mCalcRange.get(scale)[2] = minY[scale];
      }
      if (!isMaxYSet[scale]) {
        double maximumY = series.getMaxY();
        maxY[scale] = Math.max(maxY[scale], (float) maximumY);
        mCalcRange.get(scale)[3] = maxY[scale];
      }
    }
    for (int i = 0; i < maxScaleNumber; i++) {
      if (maxX[i] - minX[i] != 0) {
        xPixelsPerUnit[i] = (right - left) / (maxX[i] - minX[i]);
      }
      if (maxY[i] - minY[i] != 0) {
        yPixelsPerUnit[i] = (float) ((bottom - top) / (maxY[i] - minY[i]));
      }
    }
    
    //绘制临界线
    double[] yLimitArray = mRenderer.getmYLimitsLine();
    //System.out.println("临界线绘制:"+yLimitArray);
    if (yLimitArray!=null) {
      for (int h = 0; h < maxScaleNumber; h++) {
        Align axisAlign = mRenderer.getYAxisAlign(h);
        for(int k=0;k< yLimitArray.length;k++){
          Double yLimit = yLimitArray[k];
          float yLabel = (float) (bottom - yPixelsPerUnit[h]
              * (yLimit.doubleValue() - minY[h]));
          int color = mRenderer.getmYLimitsLineColor()[k];
          paint.setColor(color);
          //System.out.println("draw临界线:"+ (float)(double)yLimit);
          if(k+1<yLimitArray.length){
            Double yLimit2 = yLimitArray[k+1];
            int color2 = mRenderer.getmYLimitsLineColor()[k+1];
            float yLabel2 = (float) (bottom - yPixelsPerUnit[h]
                * (yLimit2.doubleValue() - minY[h]));
            //第一条和第二条同色划矩形
            if(color==color2){
              canvas.drawRect(left, yLabel, right,yLabel2, paint);
            }else{
              canvas.drawLine(left, yLabel, right, yLabel, paint);
              canvas.drawLine(left, yLabel2, right, yLabel2, paint);
            }
            k++;
          }else{
            canvas.drawLine(left, yLabel, right, yLabel, paint);
          }
        }
      }
    }

    boolean hasValues = false;
    // use a linked list for these reasons:
    // 1) Avoid a large contiguous memory allocation
    // 2) We don't need random seeking, only sequential reading/writing, so
    // linked list makes sense
    clickableAreas = new HashMap<Integer, List<ClickableArea>>();
    for (int i = 0; i < sLength; i++) {
      XYSeries series = mDataset.getSeriesAt(i);
      int scale = series.getScaleNumber();
      if (series.getItemCount() == 0) {
        continue;
      }

      hasValues = true;
      SimpleSeriesRenderer seriesRenderer = mRenderer.getSeriesRendererAt(i);

      // int originalValuesLength = series.getItemCount();
      // int valuesLength = originalValuesLength;
      // int length = valuesLength * 2;

      //把值转化为坐标点 形如（1,2，2,3） 标示2个点，x＝1 y＝2是一个点  ，另一个点是x＝2 y＝3
      List<Float> pointsIndex = new ArrayList<Float>();
      //点的值
      List<Double> valuesIndex = new ArrayList<Double>();
      //点颜色的值
      List<Integer> colorsIndex = new ArrayList<Integer>();
      
      //点值的说明
      List<String> explainsIndex = new ArrayList<String>();
     
      
      float yAxisValue = Math.min(bottom, (float) (bottom + yPixelsPerUnit[scale] * minY[scale]));
      LinkedList<ClickableArea> clickableArea = new LinkedList<ClickableArea>();

      clickableAreas.put(i, clickableArea);

      SortedMap<Double, Double> range = series.getRange(minX[scale], maxX[scale], 1);
      int startIndex = -1;

      for (Entry<Double, Double> value : range.entrySet()) {

        double xValue = value.getKey();
        double yValue = value.getValue();
        if (startIndex < 0) {
          startIndex = series.getIndexForKey(xValue);
        }

        // points.add((float) (left + xPixelsPerUnit[scale]
        // * (value.getKey().floatValue() - minX[scale])));
        // points.add((float) (bottom - yPixelsPerUnit[scale]
        // * (value.getValue().floatValue() - minY[scale])));
        valuesIndex.add(value.getKey());
        valuesIndex.add(value.getValue());
        colorsIndex.add(series.getmXCValue(value.getKey()));
        explainsIndex.add(series.getmXEValue(value.getKey()));

        if (!isNullValue(yValue)) {
          pointsIndex.add((float) (left + xPixelsPerUnit[scale] * (xValue - minX[scale])));
          pointsIndex.add((float) (bottom - yPixelsPerUnit[scale] * (yValue - minY[scale])));
        } else if (isRenderNullValues()) {
          pointsIndex.add((float) (left + xPixelsPerUnit[scale] * (xValue - minX[scale])));
          pointsIndex.add((float) (bottom - yPixelsPerUnit[scale] * (-minY[scale])));
        } else {
          if (pointsIndex.size() > 0) {
            drawSeries(series, canvas, paint, pointsIndex,colorsIndex, seriesRenderer, yAxisValue, i, or, startIndex);
            ClickableArea[] clickableAreasForSubSeries = clickableAreasForPoints(
                MathHelper.getFloats(pointsIndex), MathHelper.getDoubles(valuesIndex), yAxisValue, i,
                startIndex);
            clickableArea.addAll(Arrays.asList(clickableAreasForSubSeries));
            pointsIndex.clear();
            valuesIndex.clear();
          }
          clickableArea.add(null);
        }
      }
      
      //设置所有值
      points.put(i, pointsIndex);
      values.put(i, valuesIndex);
      colors.put(i, colorsIndex);
      explains.put(i, explainsIndex);

      if (points.size() > 0) {
        drawSeries(series, canvas, paint, pointsIndex,colorsIndex, seriesRenderer, yAxisValue, i, or, startIndex);
        ClickableArea[] clickableAreasForSubSeries = clickableAreasForPoints(
            MathHelper.getFloats(pointsIndex), MathHelper.getDoubles(valuesIndex), yAxisValue, i, startIndex);
        clickableArea.addAll(Arrays.asList(clickableAreasForSubSeries));
      }
      
    }

    // draw stuff over the margins such as data doesn't render on these areas
    drawBackground(mRenderer, canvas, x, bottom, width, height - bottom, paint, true,
        mRenderer.getMarginsColor());
    drawBackground(mRenderer, canvas, x, y, width, margins[0], paint, true,
        mRenderer.getMarginsColor());
    if (or == Orientation.HORIZONTAL) {
      drawBackground(mRenderer, canvas, x, y, left - x, height - y, paint, true,
          mRenderer.getMarginsColor());
      drawBackground(mRenderer, canvas, right, y, margins[3], height - y, paint, true,
          mRenderer.getMarginsColor());
    } else if (or == Orientation.VERTICAL) {
      drawBackground(mRenderer, canvas, right, y, width - right, height - y, paint, true,
          mRenderer.getMarginsColor());
      drawBackground(mRenderer, canvas, x, y, left - x, height - y, paint, true,
          mRenderer.getMarginsColor());
    }

    boolean showLabels = mRenderer.isShowLabels() && hasValues;
    boolean showGridX = mRenderer.isShowGridX();
    boolean showCustomTextGrid = mRenderer.isShowCustomTextGrid();
    if (showLabels || showGridX) {
      List<Double> xLabels = getValidLabels(getXLabels(minX[0], maxX[0], mRenderer.getXLabels()));
      Map<Integer, List<Double>> allYLabels = getYLabels(minY, maxY, maxScaleNumber);

      int xLabelsLeft = left;
      if (showLabels) {
        paint.setColor(mRenderer.getXLabelsColor());
        paint.setTextSize(mRenderer.getLabelsTextSize());
        paint.setTextAlign(mRenderer.getXLabelsAlign());
        if (mRenderer.getXLabelsAlign() == Align.LEFT) {
          xLabelsLeft += mRenderer.getLabelsTextSize() / 4;
        }
      }
      drawXLabels(xLabels, mRenderer.getXTextLabelLocations(), canvas, paint, xLabelsLeft, top,
          bottom, xPixelsPerUnit[0], minX[0], maxX[0]);
      drawYLabels(allYLabels, canvas, paint, maxScaleNumber, left, right, bottom, yPixelsPerUnit,
          minY);
      
      if (showLabels) {
        
        paint.setColor(mRenderer.getLabelsColor());
        for (int i = 0; i < maxScaleNumber; i++) {
          Align axisAlign = mRenderer.getYAxisAlign(i);
          Double[] yTextLabelLocations = mRenderer.getYTextLabelLocations(i);
          for (Double location : yTextLabelLocations) {
            if (minY[i] <= location && location <= maxY[i]) {
              float yLabel = (float) (bottom - yPixelsPerUnit[i]
                  * (location.doubleValue() - minY[i]));
              String label = mRenderer.getYTextLabel(location, i);
              paint.setColor(mRenderer.getYLabelsColor(i));
              paint.setTextAlign(mRenderer.getYLabelsAlign(i));
              if (or == Orientation.HORIZONTAL) {
                if (axisAlign == Align.LEFT) {
                  canvas.drawLine(left + getLabelLinePos(axisAlign), yLabel, left, yLabel, paint);
                  drawText(canvas, label, left, yLabel - 2, paint, mRenderer.getYLabelsAngle());
                } else {
                  canvas.drawLine(right, yLabel, right + getLabelLinePos(axisAlign), yLabel, paint);
                  drawText(canvas, label, right, yLabel - 2, paint, mRenderer.getYLabelsAngle());
                }
                
                if (showCustomTextGrid) {
                  paint.setColor(mRenderer.getGridColor());
                  canvas.drawLine(left, yLabel, right, yLabel, paint);
                }
              } else {
                canvas.drawLine(right - getLabelLinePos(axisAlign), yLabel, right, yLabel, paint);
                drawText(canvas, label, right + 10, yLabel - 2, paint, mRenderer.getYLabelsAngle());
                if (showCustomTextGrid) {
                  paint.setColor(mRenderer.getGridColor());
                  canvas.drawLine(right, yLabel, left, yLabel, paint);
                }
              }
            }
          }
        }
      }

      if (showLabels) {
        paint.setColor(mRenderer.getLabelsColor());
        float size = mRenderer.getAxisTitleTextSize();
        paint.setTextSize(size);
        paint.setTextAlign(Align.CENTER);
        if (or == Orientation.HORIZONTAL) {
          drawText(canvas, mRenderer.getXTitle(), x + width / 2,
              bottom + mRenderer.getLabelsTextSize() * 4 / 3 + size, paint, 0);
          for (int i = 0; i < maxScaleNumber; i++) {
            Align axisAlign = mRenderer.getYAxisAlign(i);
            if (axisAlign == Align.LEFT) {
              drawText(canvas, mRenderer.getYTitle(i), x + size, y + height / 2, paint, -90);
            } else {
              drawText(canvas, mRenderer.getYTitle(i), x + width, y + height / 2, paint, -90);
            }
          }
          paint.setTextSize(mRenderer.getChartTitleTextSize());
          drawText(canvas, mRenderer.getChartTitle(), x + width / 2,
              y + mRenderer.getChartTitleTextSize(), paint, 0);
        } else if (or == Orientation.VERTICAL) {
          drawText(canvas, mRenderer.getXTitle(), x + width / 2, y + height - size, paint, -90);
          drawText(canvas, mRenderer.getYTitle(), right + 20, y + height / 2, paint, 0);
          paint.setTextSize(mRenderer.getChartTitleTextSize());
          drawText(canvas, mRenderer.getChartTitle(), x + size, top + height / 2, paint, 0);
        }
      }
    }
    if (or == Orientation.HORIZONTAL) {
      drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize, paint, false);
    } else if (or == Orientation.VERTICAL) {
      transform(canvas, angle, true);
      drawLegend(canvas, mRenderer, titles, left, right, y, width, height, legendSize, paint, false);
      transform(canvas, angle, false);
    }
    if (mRenderer.isShowAxes()) {
      paint.setColor(mRenderer.getAxesColor());
      canvas.drawLine(left, bottom, right, bottom, paint);
      boolean rightAxis = false;
      for (int i = 0; i < maxScaleNumber && !rightAxis; i++) {
        rightAxis = mRenderer.getYAxisAlign(i) == Align.RIGHT;
      }
      if (or == Orientation.HORIZONTAL) {
        canvas.drawLine(left, top, left, bottom, paint);
        if (rightAxis) {
          canvas.drawLine(right, top, right, bottom, paint);
        }
      } else if (or == Orientation.VERTICAL) {
        canvas.drawLine(right, top, right, bottom, paint);
      }
    }
    if (rotate) {
      transform(canvas, angle, true);
    }
  }

  /**
   * Gets the x labels.
   *
   * @param min the min
   * @param max the max
   * @param count the count
   * @return the x labels
   */
  protected List<Double> getXLabels(double min, double max, int count) {
    return MathHelper.getLabels(min, max, count);
  }

  /**
   * Gets the y labels.
   *
   * @param minY the min y
   * @param maxY the max y
   * @param maxScaleNumber the max scale number
   * @return the y labels
   */
  protected Map<Integer, List<Double>> getYLabels(double[] minY, double[] maxY, int maxScaleNumber) {
    Map<Integer, List<Double>> allYLabels = new HashMap<Integer, List<Double>>();
    for (int i = 0; i < maxScaleNumber; i++) {
      allYLabels.put(i,
          getValidLabels(MathHelper.getLabels(minY[i], maxY[i], mRenderer.getYLabels())));
    }
    return allYLabels;
  }

  /**
   * Gets the screen r.
   *
   * @return the screen r
   */
  protected Rect getScreenR() {
    return mScreenR;
  }

  /**
   * Sets the screen r.
   *
   * @param screenR the new screen r
   */
  protected void setScreenR(Rect screenR) {
    mScreenR = screenR;
  }

  /**
   * Gets the valid labels.
   *
   * @param labels the labels
   * @return the valid labels
   */
  private List<Double> getValidLabels(List<Double> labels) {
    List<Double> result = new ArrayList<Double>(labels);
    for (Double label : labels) {
      if (label.isNaN()) {
        result.remove(label);
      }
    }
    return result;
  }

  /**
   * Draws the series.
   *
   * @param series the series
   * @param canvas the canvas
   * @param paint the paint object
   * @param pointsList the points to be rendered
   * @param colorsList the colors list
   * @param seriesRenderer the series renderer
   * @param yAxisValue the y axis value in pixels
   * @param seriesIndex the series index
   * @param or the orientation
   * @param startIndex the start index of the rendering points
   */
  protected void drawSeries(XYSeries series, Canvas canvas, Paint paint, List<Float> pointsList,List<Integer> colorsList,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, Orientation or,
      int startIndex) {
    BasicStroke stroke = seriesRenderer.getStroke();
    Cap cap = paint.getStrokeCap();
    Join join = paint.getStrokeJoin();
    float miter = paint.getStrokeMiter();
    PathEffect pathEffect = paint.getPathEffect();
    Style style = paint.getStyle();
    if (stroke != null) {
      PathEffect effect = null;
      if (stroke.getIntervals() != null) {
        effect = new DashPathEffect(stroke.getIntervals(), stroke.getPhase());
      }
      setStroke(stroke.getCap(), stroke.getJoin(), stroke.getMiter(), Style.FILL_AND_STROKE,
          effect, paint);
    }
    float[] points = MathHelper.getFloats(pointsList);
    int[] colors = MathHelper.getInts(colorsList);
    drawSeries(canvas, paint, points,colors ,seriesRenderer, yAxisValue, seriesIndex, startIndex);
    if (isRenderPoints(seriesRenderer)) {
      ScatterChart pointsChart = getPointsChart();
      if (pointsChart != null) {
        pointsChart.drawSeries(canvas, paint, points,colors, seriesRenderer, yAxisValue, seriesIndex,
            startIndex);
      }
    }
    paint.setTextSize(seriesRenderer.getChartValuesTextSize());
    if (or == Orientation.HORIZONTAL) {
      paint.setTextAlign(Align.CENTER);
    } else {
      paint.setTextAlign(Align.LEFT);
    }
    if (seriesRenderer.isDisplayChartValues()) {
      paint.setTextAlign(seriesRenderer.getChartValuesTextAlign());
      drawChartValuesText(canvas, series, seriesRenderer, paint, points, seriesIndex, startIndex);
    }
    if (stroke != null) {
      setStroke(cap, join, miter, style, pathEffect, paint);
    }
  }

  /**
   * Sets the stroke.
   *
   * @param cap the cap
   * @param join the join
   * @param miter the miter
   * @param style the style
   * @param pathEffect the path effect
   * @param paint the paint
   */
  private void setStroke(Cap cap, Join join, float miter, Style style, PathEffect pathEffect,
      Paint paint) {
    paint.setStrokeCap(cap);
    paint.setStrokeJoin(join);
    paint.setStrokeMiter(miter);
    paint.setPathEffect(pathEffect);
    paint.setStyle(style);
  }

  /**
   * The graphical representation of the series values as text.
   * 绘制点上的文本
   * 显示的文本判断两点之间距离100以内不显示中间的点的文本
   * @param canvas the canvas to paint to
   * @param series the series to be painted
   * @param renderer the series renderer
   * @param paint the paint to be used for drawing
   * @param points the array of points to be used for drawing the series
   * @param seriesIndex the index of the series currently being drawn
   * @param startIndex the start index of the rendering points
   */
  protected void drawChartValuesText(Canvas canvas, XYSeries series, SimpleSeriesRenderer renderer,
      Paint paint, float[] points, int seriesIndex, int startIndex) {
    if (points.length > 1) { // there are more than one point
      // record the first point's position
      float previousPointX = points[0];
      float previousPointY = points[1];
      for (int k = 0; k < points.length; k += 2) {
        if (k == 2) { // decide whether to display first two points' values or not
          if (Math.abs(points[2]- points[0]) > 100 || Math.abs(points[3] - points[1]) > 100) {
            
            if(mRenderer.isDisplayValue0()){
               // first point
              drawText(canvas, getLabel(series.getY(startIndex)), points[0], points[1]
                  - renderer.getChartValuesSpacing(), paint, 0);
              // second point
              drawText(canvas, getLabel(series.getY(startIndex + 1)), points[2], points[3]
                  - renderer.getChartValuesSpacing(), paint, 0);
            }else{
              //不是0显示
              if(points[1]!=0){
                // first point
                drawText(canvas, getLabel(series.getY(startIndex)), points[0], points[1]
                    - renderer.getChartValuesSpacing(), paint, 0);
              }
              //不是0显示
              if(points[3]!=0){
                // second point
                drawText(canvas, getLabel(series.getY(startIndex + 1)), points[2], points[3]
                    - renderer.getChartValuesSpacing(), paint, 0);
              }
            }

            previousPointX = points[2];
            previousPointY = points[3];
          }
        } else if (k > 2) {
          // compare current point's position with the previous point's, if they are not too close, display
          if (Math.abs(points[k]- previousPointX) > 100 || Math.abs(points[k+1] - previousPointY) > 100) {
            if(mRenderer.isDisplayValue0()){
              drawText(canvas, getLabel(series.getY(startIndex + k / 2)), points[k], points[k + 1]
                  - renderer.getChartValuesSpacing(), paint, 0);
           }else{
             //不是0显示
             if(points[k + 1]!=0){
               drawText(canvas, getLabel(series.getY(startIndex + k / 2)), points[k], points[k + 1]
                   - renderer.getChartValuesSpacing(), paint, 0);
             }
           }
            
            previousPointX = points[k];
            previousPointY = points[k+1];
          }
        }
      }
    } else { // if only one point, display it
      for (int k = 0; k < points.length; k += 2) {
        
        if(mRenderer.isDisplayValue0()){
          drawText(canvas, getLabel(series.getY(startIndex + k / 2)), points[k], points[k + 1]
              - renderer.getChartValuesSpacing(), paint, 0);
       }else{
         //不是0显示
         if(points[k + 1]!=0){
           drawText(canvas, getLabel(series.getY(startIndex + k / 2)), points[k], points[k + 1]
               - renderer.getChartValuesSpacing(), paint, 0);
         }
       }
      }
    }
  }

  /**
   * The graphical representation of a text, to handle both HORIZONTAL and
   * VERTICAL orientations and extra rotation angles.
   * 
   * @param canvas the canvas to paint to
   * @param text the text to be rendered
   * @param x the X axis location of the text
   * @param y the Y axis location of the text
   * @param paint the paint to be used for drawing
   * @param extraAngle the text angle
   */
  protected void drawText(Canvas canvas, String text, float x, float y, Paint paint,
      float extraAngle) {
    float angle = -mRenderer.getOrientation().getAngle() + extraAngle;
    if (angle != 0) {
      // canvas.scale(1 / mScale, mScale);
      canvas.rotate(angle, x, y);
    }
    drawString(canvas, text, x, y, paint);
    if (angle != 0) {
      canvas.rotate(-angle, x, y);
      // canvas.scale(mScale, 1 / mScale);
    }
  }

  /**
   * Transform the canvas such as it can handle both HORIZONTAL and VERTICAL
   * orientations.
   * 
   * @param canvas the canvas to paint to
   * @param angle the angle of rotation
   * @param inverse if the inverse transform needs to be applied
   */
  private void transform(Canvas canvas, float angle, boolean inverse) {
    if (inverse) {
      canvas.scale(1 / mScale, mScale);
      canvas.translate(mTranslate, -mTranslate);
      canvas.rotate(-angle, mCenter.getX(), mCenter.getY());
    } else {
      canvas.rotate(angle, mCenter.getX(), mCenter.getY());
      canvas.translate(-mTranslate, mTranslate);
      canvas.scale(mScale, 1 / mScale);
    }
  }

  /**
   * The graphical representation of the labels on the X axis.
   * 
   * @param xLabels the X labels values
   * @param xTextLabelLocations the X text label locations
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param left the left value of the labels area
   * @param top the top value of the labels area
   * @param bottom the bottom value of the labels area
   * @param xPixelsPerUnit the amount of pixels per one unit in the chart labels
   * @param minX the minimum value on the X axis in the chart
   * @param maxX the maximum value on the X axis in the chart
   */
  protected void drawXLabels(List<Double> xLabels, Double[] xTextLabelLocations, Canvas canvas,
      Paint paint, int left, int top, int bottom, double xPixelsPerUnit, double minX, double maxX) {
    int length = xLabels.size();
    boolean showLabels = mRenderer.isShowLabels();
    boolean showGridY = mRenderer.isShowGridY();
    for (int i = 0; i < length; i++) {
      double label = xLabels.get(i);
      float xLabel = (float) (left + xPixelsPerUnit * (label - minX));
      if (showLabels) {
        paint.setColor(mRenderer.getXLabelsColor());
        canvas.drawLine(xLabel, bottom, xLabel, bottom + mRenderer.getLabelsTextSize() / 3, paint);
        drawText(canvas, getLabel(label), xLabel, bottom + mRenderer.getLabelsTextSize() * 4 / 3,
            paint, mRenderer.getXLabelsAngle());
      }
      if (showGridY) {
        paint.setColor(mRenderer.getGridColor());
        canvas.drawLine(xLabel, bottom, xLabel, top, paint);
      }
    }
    drawXTextLabels(xTextLabelLocations, canvas, paint, showLabels, left, top, bottom,
        xPixelsPerUnit, minX, maxX);
  }

  /**
   * The graphical representation of the labels on the X axis.
   * 
   * @param allYLabels the Y labels values
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param maxScaleNumber the maximum scale number
   * @param left the left value of the labels area
   * @param right the right value of the labels area
   * @param bottom the bottom value of the labels area
   * @param yPixelsPerUnit the amount of pixels per one unit in the chart labels
   * @param minY the minimum value on the Y axis in the chart
   */
  protected void drawYLabels(Map<Integer, List<Double>> allYLabels, Canvas canvas, Paint paint,
      int maxScaleNumber, int left, int right, int bottom, double[] yPixelsPerUnit, double[] minY) {
    Orientation or = mRenderer.getOrientation();
    boolean showGridX = mRenderer.isShowGridX();
    boolean showLabels = mRenderer.isShowLabels();
    for (int i = 0; i < maxScaleNumber; i++) {
      paint.setTextAlign(mRenderer.getYLabelsAlign(i));
      List<Double> yLabels = allYLabels.get(i);
      int length = yLabels.size();
      for (int j = 0; j < length; j++) {
        double label = yLabels.get(j);
        Align axisAlign = mRenderer.getYAxisAlign(i);
        boolean textLabel = mRenderer.getYTextLabel(label, i) != null;
        float yLabel = (float) (bottom - yPixelsPerUnit[i] * (label - minY[i]));
        if (or == Orientation.HORIZONTAL) {
          if (showLabels && !textLabel) {
            paint.setColor(mRenderer.getYLabelsColor(i));
            if (axisAlign == Align.LEFT) {
              canvas.drawLine(left + getLabelLinePos(axisAlign), yLabel, left, yLabel, paint);
              drawText(canvas, getLabel(label), left, yLabel - 2, paint,
                  mRenderer.getYLabelsAngle());
            } else {
              canvas.drawLine(right, yLabel, right + getLabelLinePos(axisAlign), yLabel, paint);
              drawText(canvas, getLabel(label), right, yLabel - 2, paint,
                  mRenderer.getYLabelsAngle());
            }
          }
          if (showGridX) {
            paint.setColor(mRenderer.getGridColor());
            canvas.drawLine(left, yLabel, right, yLabel, paint);
          }
        } else if (or == Orientation.VERTICAL) {
          if (showLabels && !textLabel) {
            paint.setColor(mRenderer.getYLabelsColor(i));
            canvas.drawLine(right - getLabelLinePos(axisAlign), yLabel, right, yLabel, paint);
            drawText(canvas, getLabel(label), right + 10, yLabel - 2, paint,
                mRenderer.getYLabelsAngle());
          }
          if (showGridX) {
            paint.setColor(mRenderer.getGridColor());
            canvas.drawLine(right, yLabel, left, yLabel, paint);
          }
        }
      }
    }
  }

  /**
   * The graphical representation of the text labels on the X axis.
   *
   * @param xTextLabelLocations the X text label locations
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param showLabels the show labels
   * @param left the left value of the labels area
   * @param top the top value of the labels area
   * @param bottom the bottom value of the labels area
   * @param xPixelsPerUnit the amount of pixels per one unit in the chart labels
   * @param minX the minimum value on the X axis in the chart
   * @param maxX the maximum value on the X axis in the chart
   */
  protected void drawXTextLabels(Double[] xTextLabelLocations, Canvas canvas, Paint paint,
      boolean showLabels, int left, int top, int bottom, double xPixelsPerUnit, double minX,
      double maxX) {
    boolean showCustomTextGrid = mRenderer.isShowCustomTextGrid();
    if (showLabels) {
      paint.setColor(mRenderer.getXLabelsColor());
      for (Double location : xTextLabelLocations) {
        if (minX <= location && location <= maxX) {
          float xLabel = (float) (left + xPixelsPerUnit * (location.doubleValue() - minX));
          paint.setColor(mRenderer.getXLabelsColor());
          canvas
          .drawLine(xLabel, bottom, xLabel, bottom + mRenderer.getLabelsTextSize() / 3, paint);
          drawText(canvas, mRenderer.getXTextLabel(location), xLabel,
              bottom + mRenderer.getLabelsTextSize() * 4 / 3, paint, mRenderer.getXLabelsAngle());
          if (showCustomTextGrid) {
            paint.setColor(mRenderer.getGridColor());
            canvas.drawLine(xLabel, bottom, xLabel, top, paint);
          }
        }
      }
    }
  }

  /**
   * 描述：获取这个XY复合渲染器.
   *
   * @return the renderer
   */
  public XYMultipleSeriesRenderer getRenderer() {
    return mRenderer;
  }

  /**
   * Gets the dataset.
   *
   * @return the dataset
   */
  public XYMultipleSeriesDataset getDataset() {
    return mDataset;
  }

  /**
   * Gets the calc range.
   *
   * @param scale the scale
   * @return the calc range
   */
  public double[] getCalcRange(int scale) {
    return mCalcRange.get(scale);
  }

  /**
   * Sets the calc range.
   *
   * @param range the range
   * @param scale the scale
   */
  public void setCalcRange(double[] range, int scale) {
    mCalcRange.put(scale, range);
  }

  /**
   * To real point.
   *
   * @param screenX the screen x
   * @param screenY the screen y
   * @return the double[]
   */
  public double[] toRealPoint(float screenX, float screenY) {
    return toRealPoint(screenX, screenY, 0);
  }

  /**
   * To screen point.
   *
   * @param realPoint the real point
   * @return the double[]
   */
  public double[] toScreenPoint(double[] realPoint) {
    return toScreenPoint(realPoint, 0);
  }

  /**
   * Gets the label line pos.
   *
   * @param align the align
   * @return the label line pos
   */
  private int getLabelLinePos(Align align) {
    int pos = 4;
    if (align == Align.LEFT) {
      pos = -pos;
    }
    return pos;
  }

  /**
   * Transforms a screen point to a real coordinates point.
   *
   * @param screenX the screen x axis value
   * @param screenY the screen y axis value
   * @param scale the scale
   * @return the real coordinates point
   */
  public double[] toRealPoint(float screenX, float screenY, int scale) {
    double realMinX = mRenderer.getXAxisMin(scale);
    double realMaxX = mRenderer.getXAxisMax(scale);
    double realMinY = mRenderer.getYAxisMin(scale);
    double realMaxY = mRenderer.getYAxisMax(scale);
    return new double[] {
        (screenX - mScreenR.left) * (realMaxX - realMinX) / mScreenR.width() + realMinX,
        (mScreenR.top + mScreenR.height() - screenY) * (realMaxY - realMinY) / mScreenR.height()
            + realMinY };
  }

  /**
   * To screen point.
   *
   * @param realPoint the real point
   * @param scale the scale
   * @return the double[]
   */
  public double[] toScreenPoint(double[] realPoint, int scale) {
    double realMinX = mRenderer.getXAxisMin(scale);
    double realMaxX = mRenderer.getXAxisMax(scale);
    double realMinY = mRenderer.getYAxisMin(scale);
    double realMaxY = mRenderer.getYAxisMax(scale);
    if (!mRenderer.isMinXSet(scale) || !mRenderer.isMaxXSet(scale) || !mRenderer.isMinXSet(scale)
        || !mRenderer.isMaxYSet(scale)) {
      double[] calcRange = getCalcRange(scale);
      realMinX = calcRange[0];
      realMaxX = calcRange[1];
      realMinY = calcRange[2];
      realMaxY = calcRange[3];
    }
    return new double[] {
        (realPoint[0] - realMinX) * mScreenR.width() / (realMaxX - realMinX) + mScreenR.left,
        (realMaxY - realPoint[1]) * mScreenR.height() / (realMaxY - realMinY) + mScreenR.top };
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @param screenPoint the screen point
   * @return the series and point for screen coordinate
   * @see com.ab.view.chart.AbstractChart#getSeriesAndPointForScreenCoordinate(com.ab.view.chart.Point)
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:49
   */
  public SeriesSelection getSeriesAndPointForScreenCoordinate(final Point screenPoint) {
    if (clickableAreas != null)
      for (int seriesIndex = clickableAreas.size() - 1; seriesIndex >= 0; seriesIndex--) {
        // series 0 is drawn first. Then series 1 is drawn on top, and series 2
        // on top of that.
        // we want to know what the user clicked on, so traverse them in the
        // order they appear on the screen.
        int pointIndex = 0;
        if (clickableAreas.get(seriesIndex) != null) {
          RectF rectangle;
          for (ClickableArea area : clickableAreas.get(seriesIndex)) {
            rectangle = area.getRect();
            if (rectangle != null && rectangle.contains(screenPoint.getX(), screenPoint.getY())) {
              return new SeriesSelection(seriesIndex, pointIndex, area.getX(), area.getY());
            }
            pointIndex++;
          }
        }
      }
    return super.getSeriesAndPointForScreenCoordinate(screenPoint);
  }

  /**
   * The graphical representation of a series.
   *
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param points the array of points to be used for drawing the series
   * @param colors the colors
   * @param seriesRenderer the series renderer
   * @param yAxisValue the minimum value of the y axis
   * @param seriesIndex the index of the series currently being drawn
   * @param startIndex the start index of the rendering points
   */
  public abstract void drawSeries(Canvas canvas, Paint paint, float[] points,int[] colors,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, int startIndex);

  /**
   * Returns the clickable areas for all passed points.
   *
   * @param points the array of points
   * @param values the array of values of each point
   * @param yAxisValue the minimum value of the y axis
   * @param seriesIndex the index of the series to which the points belong
   * @param startIndex the start index of the rendering points
   * @return an array of rectangles with the clickable area
   */
  protected abstract ClickableArea[] clickableAreasForPoints(float[] points, double[] values,
      float yAxisValue, int seriesIndex, int startIndex);

  /**
   * Returns if the chart should display the null values.
   * 
   * @return if null values should be rendered
   */
  protected boolean isRenderNullValues() {
    return false;
  }

  /**
   * Returns if the chart should display the points as a certain shape.
   *
   * @param renderer the series renderer
   * @return true, if is render points
   */
  public boolean isRenderPoints(SimpleSeriesRenderer renderer) {
    return false;
  }

  /**
   * Returns the default axis minimum.
   * 
   * @return the default axis minimum
   */
  public double getDefaultMinimum() {
    return MathHelper.NULL_VALUE;
  }

  /**
   * Returns the scatter chart to be used for drawing the data points.
   * 
   * @return the data points scatter chart
   */
  public ScatterChart getPointsChart() {
    return null;
  }

  /**
   * Returns the chart type identifier.
   * 
   * @return the chart type
   */
  public abstract String getChartType();

  /**
   * 描述：获取点的坐标值
   * 形如（1,2，2,3） 标示2个点，x＝1 y＝2是一个点  ，另一个点是x＝2 y＝3.
   *
   * @return the points
   */
  public Map<Integer, List<Float>> getPoints() {
    return points;
  }

  /**
   * 描述：获取点的值.
   *
   * @return the values
   */
  public Map<Integer, List<Double>> getValues() {
    return values;
  }
  
  /**
   * 描述：获取点的说明.
   *
   * @return the explains
   */
  public Map<Integer, List<String>> getExplains() {
    return explains;
  }

}
