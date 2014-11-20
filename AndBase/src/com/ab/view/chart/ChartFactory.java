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


import android.content.Context;
import android.util.Log;

import com.ab.view.chart.BarChart.Type;

// TODO: Auto-generated Javadoc
/**
 * Utility methods for creating chart views or intents.
 */
public class ChartFactory {
  /** The key for the chart data. */
  public static final String CHART = "chart";

  /** The key for the chart graphical activity title. */
  public static final String TITLE = "title";

  /**
   * Instantiates a new chart factory.
   */
  private ChartFactory() {
    // empty for now
  }

  /**
   * 折线图.
   *
   * @param context the context
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @return a line chart graphical view
   */
  public static final GraphicalView getLineChartView(Context context,
      XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer) {
    checkParameters(dataset, renderer);
    XYChart chart = new LineChart(dataset, renderer);
    return new GraphicalView(context, chart);
  }
  
  /**
   * 柱状图.
   *
   * @param context the context
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @param type the bar chart type  BarChart.Type.DEFAULT BarChart.Type.STACKED
   * @return a bar chart graphical view
   */
  public static final GraphicalView getBarChartView(Context context,
      XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, Type type) {
    checkParameters(dataset, renderer);
    XYChart chart = new BarChart(dataset, renderer, type);
    return new GraphicalView(context, chart);
  }
  
  /**
   * 饼图.
   *
   * @param context the context
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   * @return a pie chart view
   */
  public static final GraphicalView getPieChartView(Context context, CategorySeries dataset,
      DefaultRenderer renderer) {
    checkParameters(dataset, renderer);
    PieChart chart = new PieChart(dataset, renderer);
    return new GraphicalView(context, chart);
  }
  
  /**
   * 时间轴图表.
   *
   * @param context the context
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   * @param format the date format pattern to be used for displaying the X axis
   * date labels. If null, a default appropriate format will be used.
   * @return a time chart graphical view
   */
  public static final GraphicalView getTimeChartView(Context context,
      XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, String format) {
    checkParameters(dataset, renderer);
    TimeChart chart = new TimeChart(dataset, renderer);
    Log.d("ChartFactory", "TimeChart:"+chart.mDataset);
    chart.setDateFormat(format);
    return new GraphicalView(context, chart);
  }
  
  /**
   * 描述：区域图表.
   *
   * @param context the context
   * @param dataset the dataset
   * @param renderer the renderer
   * @param smoothness 平滑度
   * @return the area chart view
   */
  public static final GraphicalView getAreaChartView(Context context,
	      XYMultipleSeriesDataset dataset, XYMultipleSeriesRenderer renderer, float smoothness) {
	    checkParameters(dataset, renderer);
	    XYChart chart = new AreaChart(dataset, renderer, smoothness);
	    return new GraphicalView(context, chart);
	  }

  /**
   * 检查参数.
   *
   * @param dataset the multiple series dataset (cannot be null)
   * @param renderer the multiple series renderer (cannot be null)
   */
  private static void checkParameters(XYMultipleSeriesDataset dataset,
      XYMultipleSeriesRenderer renderer) {
    if (dataset == null || renderer == null
        || dataset.getSeriesCount() != renderer.getSeriesRendererCount()) {
      throw new IllegalArgumentException(
          "Dataset and renderer should be not null and should have the same number of series");
    }
  }

  /**
   * Checks the validity of the dataset and renderer parameters.
   *
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   */
  private static void checkParameters(CategorySeries dataset, DefaultRenderer renderer) {
    if (dataset == null || renderer == null
        || dataset.getItemCount() != renderer.getSeriesRendererCount()) {
      throw new IllegalArgumentException(
          "Dataset and renderer should be not null and the dataset number of items should be equal to the number of series renderers");
    }
  }

  /**
   * Checks the validity of the dataset and renderer parameters.
   *
   * @param dataset the category series dataset (cannot be null)
   * @param renderer the series renderer (cannot be null)
   */
  private static void checkParameters(MultipleCategorySeries dataset, DefaultRenderer renderer) {
    if (dataset == null || renderer == null
        || !checkMultipleSeriesItems(dataset, renderer.getSeriesRendererCount())) {
      throw new IllegalArgumentException(
          "Titles and values should be not null and the dataset number of items should be equal to the number of series renderers");
    }
  }

  /**
   * Check multiple series items.
   *
   * @param dataset the dataset
   * @param value the value
   * @return true, if successful
   */
  private static boolean checkMultipleSeriesItems(MultipleCategorySeries dataset, int value) {
    int count = dataset.getCategoriesCount();
    boolean equal = true;
    for (int k = 0; k < count && equal; k++) {
      equal = dataset.getValues(k).length == dataset.getTitles(k).length;
    }
    return equal;
  }

}
