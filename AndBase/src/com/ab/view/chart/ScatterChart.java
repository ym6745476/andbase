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

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.Log;

// TODO: Auto-generated Javadoc
/**
 * The scatter chart rendering class.
 */
public class ScatterChart extends XYChart {
  /** The constant to identify this chart type. */
  public static final String TYPE = "Scatter";
  /** The default point shape size. */
  private static final float SIZE = 3;
  /** The legend shape width. */
  private static final int SHAPE_WIDTH = 10;
  /** The point shape size. */
  private float size = SIZE;

  /**
   * Instantiates a new scatter chart.
   */
  ScatterChart() {
  }

  /**
   * Builds a new scatter chart instance.
   * 
   * @param dataset the multiple series dataset
   * @param renderer the multiple series renderer
   */
  public ScatterChart(XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    super(dataset, renderer);
    Log.d("ScatterChart", "调用父类XYChart的XYMultipleSeriesDataset 设置2");
    size = renderer.getPointSize();
  }

  // TODO: javadoc
  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @param dataset the dataset
   * @param renderer the renderer
   * @see com.ab.view.chart.XYChart#setDatasetRenderer(com.ab.view.chart.XYMultipleSeriesDataset, com.ab.view.chart.XYMultipleSeriesRenderer)
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:49
   */
  protected void setDatasetRenderer(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    super.setDatasetRenderer(dataset, renderer);
    size = renderer.getPointSize();
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
  public void drawSeries(Canvas canvas, Paint paint, float[] points,int[] colors,
      SimpleSeriesRenderer seriesRenderer, float yAxisValue, int seriesIndex, int startIndex) {
    XYSeriesRenderer renderer = (XYSeriesRenderer) seriesRenderer;
    paint.setColor(renderer.getColor());
    if (renderer.isFillPoints()) {
      paint.setStyle(Style.FILL);
    } else {
      paint.setStyle(Style.STROKE);
    }
    int length = points.length;
    switch (renderer.getPointStyle()) {
    case X:
      for (int i = 0; i < length; i += 2) {
        drawX(canvas, paint, points[i], points[i + 1]);
      }
      break;
    //绘制圆点
    case CIRCLE:
      for (int i = 0; i < length; i += 2) {
        if(colors!=null && colors[i/2]!=0){
          paint.setColor(colors[i/2]);
        }
        drawCircle(canvas, paint, points[i], points[i + 1]);
        paint.setColor(renderer.getColor());
      }
      break;
    case TRIANGLE:
      float[] path = new float[6];
      for (int i = 0; i < length; i += 2) {
        drawTriangle(canvas, paint, path, points[i], points[i + 1]);
      }
      break;
    case SQUARE:
      for (int i = 0; i < length; i += 2) {
        drawSquare(canvas, paint, points[i], points[i + 1]);
      }
      break;
    case DIAMOND:
      path = new float[8];
      for (int i = 0; i < length; i += 2) {
        drawDiamond(canvas, paint, path, points[i], points[i + 1]);
      }
      break;
    case POINT:
      canvas.drawPoints(points, paint);
      break;
    }
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @param points the points
   * @param values the values
   * @param yAxisValue the y axis value
   * @param seriesIndex the series index
   * @param startIndex the start index
   * @return the clickable area[]
   * @see com.ab.view.chart.XYChart#clickableAreasForPoints(float[], double[], float, int, int)
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:49
   */
  @Override
  protected ClickableArea[] clickableAreasForPoints(float[] points, double[] values,
      float yAxisValue, int seriesIndex, int startIndex) {
    int length = points.length;
    ClickableArea[] ret = new ClickableArea[length / 2];
    for (int i = 0; i < length; i += 2) {
      int selectableBuffer = mRenderer.getSelectableBuffer();
      ret[i / 2] = new ClickableArea(new RectF(points[i] - selectableBuffer, points[i + 1]
          - selectableBuffer, points[i] + selectableBuffer, points[i + 1] + selectableBuffer),
          values[i], values[i + 1]);
    }
    return ret;
  }

  /**
   * Returns the legend shape width.
   * 
   * @param seriesIndex the series index
   * @return the legend shape width
   */
  public int getLegendShapeWidth(int seriesIndex) {
    return SHAPE_WIDTH;
  }

  /**
   * The graphical representation of the legend shape.
   * 
   * @param canvas the canvas to paint to
   * @param renderer the series renderer
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   * @param seriesIndex the series index
   * @param paint the paint to be used for drawing
   */
  public void drawLegendShape(Canvas canvas, SimpleSeriesRenderer renderer, float x, float y,
      int seriesIndex, Paint paint) {
    if (((XYSeriesRenderer) renderer).isFillPoints()) {
      paint.setStyle(Style.FILL);
    } else {
      paint.setStyle(Style.STROKE);
    }
    switch (((XYSeriesRenderer) renderer).getPointStyle()) {
    case X:
      drawX(canvas, paint, x + SHAPE_WIDTH, y);
      break;
    case CIRCLE:
      drawCircle(canvas, paint, x + SHAPE_WIDTH, y);
      break;
    case TRIANGLE:
      drawTriangle(canvas, paint, new float[6], x + SHAPE_WIDTH, y);
      break;
    case SQUARE:
      drawSquare(canvas, paint, x + SHAPE_WIDTH, y);
      break;
    case DIAMOND:
      drawDiamond(canvas, paint, new float[8], x + SHAPE_WIDTH, y);
      break;
    case POINT:
      canvas.drawPoint(x + SHAPE_WIDTH, y, paint);
      break;
    }
  }

  /**
   * The graphical representation of an X point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawX(Canvas canvas, Paint paint, float x, float y) {
    canvas.drawLine(x - size, y - size, x + size, y + size, paint);
    canvas.drawLine(x + size, y - size, x - size, y + size, paint);
  }

  /**
   * The graphical representation of a circle point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawCircle(Canvas canvas, Paint paint, float x, float y) {
    canvas.drawCircle(x, y, size, paint);
  }

  /**
   * The graphical representation of a triangle point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param path the triangle path
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawTriangle(Canvas canvas, Paint paint, float[] path, float x, float y) {
    path[0] = x;
    path[1] = y - size - size / 2;
    path[2] = x - size;
    path[3] = y + size;
    path[4] = x + size;
    path[5] = path[3];
    drawPath(canvas, path, paint, true);
  }

  /**
   * The graphical representation of a square point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawSquare(Canvas canvas, Paint paint, float x, float y) {
    canvas.drawRect(x - size, y - size, x + size, y + size, paint);
  }

  /**
   * The graphical representation of a diamond point shape.
   * 
   * @param canvas the canvas to paint to
   * @param paint the paint to be used for drawing
   * @param path the diamond path
   * @param x the x value of the point the shape should be drawn at
   * @param y the y value of the point the shape should be drawn at
   */
  private void drawDiamond(Canvas canvas, Paint paint, float[] path, float x, float y) {
    path[0] = x;
    path[1] = y - size;
    path[2] = x - size;
    path[3] = y;
    path[4] = x;
    path[5] = y + size;
    path[6] = x + size;
    path[7] = y;
    drawPath(canvas, path, paint, true);
  }

  /**
   * Returns the chart type identifier.
   * 
   * @return the chart type
   */
  public String getChartType() {
    return TYPE;
  }

}
