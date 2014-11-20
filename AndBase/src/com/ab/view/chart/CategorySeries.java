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
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * A series for the category charts like the pie ones.
 */
public class CategorySeries implements Serializable {
  /** The series title. */
  private String mTitle;
  /** The series categories. */
  private List<String> mCategories = new ArrayList<String>();
  /** The series values. */
  private List<Double> mValues = new ArrayList<Double>();
  /** 每个点的颜色. */
  private List<Integer> mColors = new ArrayList<Integer>();
  
  /** 每个数据点的简要 说明. */
  List<String> mExplains = new ArrayList<String>();
  /**
   * Builds a new category series.
   * 
   * @param title the series title
   */
  public CategorySeries(String title) {
    mTitle = title;
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
   * Adds a new value to the series.
   *
   * @param value the new value
   */
  public synchronized void add(double value) {
    add(mCategories.size() + "", value);
  }
  
  /**
   * 增加一个新的点以及颜色值.
   *
   * @param value the new value
   * @param color the new color
   */
  public synchronized void add(double value,int color) {
    add(mCategories.size() + "", value,color);
  }
  
  /**
   * 增加一个新的点以及颜色值,以及颜色,文字说明.
   *
   * @param value the new value
   * @param color the new color
   * @param explain the explain
   */
  public synchronized void add(double value,int color,String explain) {
    add(mCategories.size() + "", value,color,explain);
  }

  /**
   * Adds a new value to the series.
   * @param category the category
   * @param value the new value
   */
  public synchronized void add(String category, double value) {
    mCategories.add(category);
    mValues.add(value);
  }
  
  /**
   * Adds a new value to the series. 以及颜色
   * @param category the category
   * @param value the new value
   * @param color the new color
   */
  public synchronized void add(String category, double value,int color) {
    mCategories.add(category);
    mValues.add(value);
    mColors.add(color);
  }
  
  /**
   * Adds a new value to the series. 以及颜色,文字说明
   *
   * @param category the category
   * @param value the new value
   * @param color the new color
   * @param explain the explain
   */
  public synchronized void add(String category, double value,int color,String explain) {
    mCategories.add(category);
    mValues.add(value);
    mColors.add(color);
    mExplains.add(explain);
  }

  /**
   * Replaces the value at the specific index in the series.
   * 
   * @param index the index in the series
   * @param category the category
   * @param value the new value
   */
  public synchronized void set(int index, String category, double value) {
    mCategories.set(index, category);
    mValues.set(index, value);
  }

  /**
   * Removes an existing value from the series.
   * 
   * @param index the index in the series of the value to remove
   */
  public synchronized void remove(int index) {
    mCategories.remove(index);
    mValues.remove(index);
  }

  /**
   * Removes all the existing values from the series.
   */
  public synchronized void clear() {
    mCategories.clear();
    mValues.clear();
  }

  /**
   * Returns the value at the specified index.
   * 
   * @param index the index
   * @return the value at the index
   */
  public synchronized double getValue(int index) {
    return mValues.get(index);
  }

  /**
   * Returns the category name at the specified index.
   * 
   * @param index the index
   * @return the category name at the index
   */
  public synchronized String getCategory(int index) {
    return mCategories.get(index);
  }
  
  /**
   * 描述：获取点颜色数组.
   *
   * @return the colors
   */
  public List<Integer> getColors() {
    return mColors;
  }
  
  /**
   * 描述：获取点说明.
   *
   * @return the explains
   */
  public List<String> getExplains() {
    return mExplains;
  }

  /**
   * Returns the series item count.
   * 
   * @return the series item count
   */
  public synchronized int getItemCount() {
    return mCategories.size();
  }

  /**
   * Transforms the category series to an XY series.
   * @return the XY series
   */
  public XYSeries toXYSeries() {
    XYSeries xySeries = new XYSeries(mTitle);
    for (int i=0;i<mValues.size();i++) {
      double value = mValues.get(i);
      //坐标，颜色，文字
      xySeries.add(i+1, value,mColors.get(i),mExplains.get(i));
    }
    return xySeries;
  }
}
