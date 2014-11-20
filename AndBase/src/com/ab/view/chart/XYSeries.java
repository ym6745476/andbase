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

import java.io.Serializable;
import java.util.Iterator;
import java.util.SortedMap;

// TODO: Auto-generated Javadoc
/**
 * An XY series encapsulates values for XY charts like line, time, area,
 * scatter... charts.
 */
public class XYSeries implements Serializable {
  /** The series title. */
  private String mTitle;
  
  /** A map to contain values for X and Y axes and index for each bundle. */
  private final IndexXYMap<Double, Double> mXY = new IndexXYMap<Double, Double>();
  
  /** 每个数据点的颜色值. */
  private final IndexXYMap<Double, Integer> mXC = new IndexXYMap<Double, Integer>();
  
  /** 每个数据点的简要 说明. */
  private final IndexXYMap<Double, String> mXE = new IndexXYMap<Double, String>();
  
  
  /** The minimum value for the X axis. */
  private double mMinX = MathHelper.NULL_VALUE;
  /** The maximum value for the X axis. */
  private double mMaxX = -MathHelper.NULL_VALUE;
  /** The minimum value for the Y axis. */
  private double mMinY = MathHelper.NULL_VALUE;
  /** The maximum value for the Y axis. */
  private double mMaxY = -MathHelper.NULL_VALUE;
  /** The scale number for this series. */
  private final int mScaleNumber;

  /**
   * Builds a new XY series.
   * 
   * @param title the series title.
   */
  public XYSeries(String title) {
    this(title, 0);
  }

  /**
   * Builds a new XY series.
   * 
   * @param title the series title.
   * @param scaleNumber the series scale number
   */
  public XYSeries(String title, int scaleNumber) {
    mTitle = title;
    mScaleNumber = scaleNumber;
    initRange();
  }

  /**
   * Gets the scale number.
   *
   * @return the scale number
   */
  public int getScaleNumber() {
    return mScaleNumber;
  }

  /**
   * Initializes the range for both axes.
   */
  private void initRange() {
    mMinX = MathHelper.NULL_VALUE;
    mMaxX = -MathHelper.NULL_VALUE;
    mMinY = MathHelper.NULL_VALUE;
    mMaxY = -MathHelper.NULL_VALUE;
    int length = getItemCount();
    for (int k = 0; k < length; k++) {
      double x = getX(k);
      double y = getY(k);
      updateRange(x, y);
    }
  }

  /**
   * Updates the range on both axes.
   * 
   * @param x the new x value
   * @param y the new y value
   */
  private void updateRange(double x, double y) {
    mMinX = Math.min(mMinX, x);
    mMaxX = Math.max(mMaxX, x);
    mMinY = Math.min(mMinY, y);
    mMaxY = Math.max(mMaxY, y);
  }

  /**
   * Returns the series title.
   * 
   * @return the series title
   */
  public String getTitle() {
    return mTitle;
  }

  /**
   * Sets the series title.
   * 
   * @param title the series title
   */
  public void setTitle(String title) {
    mTitle = title;
  }

  /**
   * Adds a new value to the series.
   * 
   * @param x the value for the X axis
   * @param y the value for the Y axis
   */
  public synchronized void add(double x, double y) {
    mXY.put(x, y);
    updateRange(x, y);
  }
  
  /**
   * Adds the.
   *
   * @param x the x
   * @param y the y
   * @param c the c
   */
  public synchronized void add(double x, double y,int c) {
    mXY.put(x, y);
    updateRange(x, y);
    mXC.put(x, c);
  }
  
  /**
   * 描述：添加坐标，颜色，描述.
   *
   * @param x the x
   * @param y the y
   * @param c the c
   * @param e the e
   */
  public synchronized void add(double x, double y,int c,String e) {
    mXY.put(x, y);
    updateRange(x, y);
    mXC.put(x, c);
    mXE.put(x, e);
  }

  /**
   * Removes an existing value from the series.
   * 
   * @param index the index in the series of the value to remove
   */
  public synchronized void remove(int index) {
    XYEntry<Double, Double> removedEntry = mXY.removeByIndex(index);
    double removedX = removedEntry.getKey();
    double removedY = removedEntry.getValue();
    if (removedX == mMinX || removedX == mMaxX || removedY == mMinY || removedY == mMaxY) {
      initRange();
    }
  }

  /**
   * Removes all the existing values from the series.
   */
  public synchronized void clear() {
    mXY.clear();
    initRange();
  }

  /**
   * Returns the X axis value at the specified index.
   * 
   * @param index the index
   * @return the X value
   */
  public synchronized double getX(int index) {
    return mXY.getXByIndex(index);
  }

  /**
   * Returns the Y axis value at the specified index.
   * 
   * @param index the index
   * @return the Y value
   */
  public synchronized double getY(int index) {
    return mXY.getYByIndex(index);
  }

  /**
   * Returns submap of x and y values according to the given start and end.
   *
   * @param start start x value
   * @param stop stop x value
   * @param beforeAfterPoints the before after points
   * @return a submap of x and y values
   */
  public synchronized SortedMap<Double, Double> getRange(double start, double stop,
      int beforeAfterPoints) {
    // we need to add one point before the start and one point after the end (if
    // there are any)
    // to ensure that line doesn't end before the end of the screen

    // this would be simply: start = mXY.lowerKey(start) but NavigableMap is
    // available since API 9
    SortedMap<Double, Double> headMap = mXY.headMap(start);
    if (!headMap.isEmpty()) {
      start = headMap.lastKey();
    }

    // this would be simply: end = mXY.higherKey(end) but NavigableMap is
    // available since API 9
    // so we have to do this hack in order to support older versions
    SortedMap<Double, Double> tailMap = mXY.tailMap(stop);
    if (!tailMap.isEmpty()) {
      Iterator<Double> tailIterator = tailMap.keySet().iterator();
      Double next = tailIterator.next();
      if (tailIterator.hasNext()) {
        stop = tailIterator.next();
      } else {
        stop += next;
      }
    }
    return mXY.subMap(start, stop);
  }

  /**
   * Gets the index for key.
   *
   * @param key the key
   * @return the index for key
   */
  public int getIndexForKey(double key) {
    return mXY.getIndexForKey(key);
  }

  /**
   * Returns the series item count.
   * 
   * @return the series item count
   */
  public synchronized int getItemCount() {
    return mXY.size();
  }

  /**
   * Returns the minimum value on the X axis.
   * 
   * @return the X axis minimum value
   */
  public double getMinX() {
    return mMinX;
  }

  /**
   * Returns the minimum value on the Y axis.
   * 
   * @return the Y axis minimum value
   */
  public double getMinY() {
    return mMinY;
  }

  /**
   * Returns the maximum value on the X axis.
   * 
   * @return the X axis maximum value
   */
  public double getMaxX() {
    return mMaxX;
  }

  /**
   * Returns the maximum value on the Y axis.
   * 
   * @return the Y axis maximum value
   */
  public double getMaxY() {
    return mMaxY;
  }

  /**
   * 描述：获取颜色值.
   *
   * @return the m xc
   */
  public IndexXYMap<Double, Integer> getmXC() {
    return mXC;
  }
  
  /**
   * 描述：获取这个点的颜色值.
   *
   * @param key the key
   * @return the m xc value
   */
  public int getmXCValue(double key) {
    Object c  = mXC.get(key);
    if(c == null){
      return 0;
    }
    return mXC.get(key);
  }
  
  /**
   * 描述：获取这个点的说明.
   *
   * @return the m xe
   */
  public IndexXYMap<Double, String> getmXE() {
    return mXE;
  }
  
  /**
   * 描述：获取值的说明.
   *
   * @param key the key
   * @return the m xe value
   */
  public String getmXEValue(double key) {
    Object e  = mXC.get(key);
    if(e == null){
      return "";
    }
    return mXE.get(key);
  }
  
  
}
