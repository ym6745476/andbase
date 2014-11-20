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

// TODO: Auto-generated Javadoc
/**
 * The Class SeriesSelection.
 */
public class SeriesSelection {
  
  /** The m series index. */
  private int mSeriesIndex;

  /** The m point index. */
  private int mPointIndex;

  /** The m x value. */
  private double mXValue;

  /** The m value. */
  private double mValue;

  /**
   * Instantiates a new series selection.
   *
   * @param seriesIndex the series index
   * @param pointIndex the point index
   * @param xValue the x value
   * @param value the value
   */
  public SeriesSelection(int seriesIndex, int pointIndex, double xValue, double value) {
    mSeriesIndex = seriesIndex;
    mPointIndex = pointIndex;
    mXValue = xValue;
    mValue = value;
  }

  /**
   * Gets the series index.
   *
   * @return the series index
   */
  public int getSeriesIndex() {
    return mSeriesIndex;
  }

  /**
   * Gets the point index.
   *
   * @return the point index
   */
  public int getPointIndex() {
    return mPointIndex;
  }

  /**
   * Gets the x value.
   *
   * @return the x value
   */
  public double getXValue() {
    return mXValue;
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  public double getValue() {
    return mValue;
  }
}
