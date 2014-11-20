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

import android.graphics.RectF;

// TODO: Auto-generated Javadoc
/**
 * The Class ClickableArea.
 */
public class ClickableArea {
  
  /** The rect. */
  private RectF rect;
  
  /** The x. */
  private double x;
  
  /** The y. */
  private double y;

  /**
   * Instantiates a new clickable area.
   *
   * @param rect the rect
   * @param x the x
   * @param y the y
   */
  public ClickableArea(RectF rect, double x, double y) {
    super();
    this.rect = rect;
    this.x = x;
    this.y = y;
  }

  /**
   * Gets the rect.
   *
   * @return the rect
   */
  public RectF getRect() {
    return rect;
  }

  /**
   * Gets the x.
   *
   * @return the x
   */
  public double getX() {
    return x;
  }

  /**
   * Gets the y.
   *
   * @return the y
   */
  public double getY() {
    return y;
  }

}
