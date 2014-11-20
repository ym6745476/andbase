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
 * Holds An PieChart Segment.
 */
public class PieSegment implements Serializable {
  
  /** The m start angle. */
  private float mStartAngle;

  /** The m end angle. */
  private float mEndAngle;

  /** The m data index. */
  private int mDataIndex;

  /** The m value. */
  private float mValue;

  /**
   * Instantiates a new pie segment.
   *
   * @param dataIndex the data index
   * @param value the value
   * @param startAngle the start angle
   * @param angle the angle
   */
  public PieSegment(int dataIndex, float value, float startAngle, float angle) {
    mStartAngle = startAngle;
    mEndAngle = angle + startAngle;
    mDataIndex = dataIndex;
    mValue = value;
  }

  /**
   * Checks if angle falls in segment.
   *
   * @param angle the angle
   * @return true if in segment, false otherwise.
   */
  public boolean isInSegment(double angle) {
    return angle >= mStartAngle && angle <= mEndAngle;
  }

  /**
   * Gets the start angle.
   *
   * @return the start angle
   */
  protected float getStartAngle() {
    return mStartAngle;
  }

  /**
   * Gets the end angle.
   *
   * @return the end angle
   */
  protected float getEndAngle() {
    return mEndAngle;
  }

  /**
   * Gets the data index.
   *
   * @return the data index
   */
  protected int getDataIndex() {
    return mDataIndex;
  }

  /**
   * Gets the value.
   *
   * @return the value
   */
  protected float getValue() {
    return mValue;
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @return the string
   * @see java.lang.Object#toString()
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:50
   */
  public String toString() {
    return "mDataIndex=" + mDataIndex + ",mValue=" + mValue + ",mStartAngle=" + mStartAngle
        + ",mEndAngle=" + mEndAngle;
  }

}
