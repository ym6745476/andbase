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

import android.content.Context;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating AbLevelChart objects.
 */
public class AbLevelChartFactory {


  /**
   * Instantiates a new ab level chart factory.
   */
  private AbLevelChartFactory() {
  }


  /**
   * Gets the level chart view.
   *
   * @param context the context
   * @param dataset the dataset
   * @param renderer the renderer
   * @return the level chart view
   */
  public static final AbLevelView getLevelChartView(Context context,
      AbLevelSeriesDataset dataset, AbLevelSeriesRenderer renderer) {
    checkParameters(dataset, renderer); 
    AbLevelChart chart = new AbLevelChart(dataset, renderer);
    return new AbLevelView(context, chart);
  }

  
  /**
   * Check parameters.
   *
   * @param dataset the dataset
   * @param renderer the renderer
   */
  private static void checkParameters(AbLevelSeriesDataset dataset,AbLevelSeriesRenderer renderer) {
    if (dataset == null || renderer == null) {
      throw new IllegalArgumentException(
          "Dataset and renderer should be not null");
    }
  }

}
