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

// TODO: Auto-generated Javadoc
/**
 * A class to encapsulate the definition of a point.
 */
public final class Point implements Serializable {
  /** The X axis coordinate value. */
  private float mX;
  /** The Y axis coordinate value. */
  private float mY;
  
  /**
   * Instantiates a new point.
   */
  public Point() {
  }
  
  /**
   * Instantiates a new point.
   *
   * @param x the x
   * @param y the y
   */
  public Point(float x, float y) {
    mX = x;
    mY = y;
  }
  
  /**
   * Gets the x.
   *
   * @return the x
   */
  public float getX() {
    return mX;
  }

  /**
   * Gets the y.
   *
   * @return the y
   */
  public float getY() {
    return mY;
  }
  
  /**
   * Sets the x.
   *
   * @param x the new x
   */
  public void setX(float x) {
    mX = x;
  }
  
  /**
   * Sets the y.
   *
   * @param y the new y
   */
  public void setY(float y) {
    mY = y;
  }
}
