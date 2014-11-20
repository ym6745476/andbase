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
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

// TODO: Auto-generated Javadoc
/**
 * This class requires sorted x values.
 *
 * @param <K> the key type
 * @param <V> the value type
 */
public class IndexXYMap<K, V> extends TreeMap<K, V> {
  
  /** The index list. */
  private final List<K> indexList = new ArrayList<K>();

  /** The max x difference. */
  private double maxXDifference = 0;

  /**
   * Instantiates a new index xy map.
   */
  public IndexXYMap() {
    super();
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @param key the key
   * @param value the value
   * @return the v
   * @see java.util.TreeMap#put(java.lang.Object, java.lang.Object)
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:48
   */
  public V put(K key, V value) {
    indexList.add(key);
    updateMaxXDifference();
    return super.put(key, value);
  }

  /**
   * Update max x difference.
   */
  private void updateMaxXDifference() {
    if (indexList.size() < 2) {
      maxXDifference = 0;
      return;
    }

    if (Math.abs((Double) indexList.get(indexList.size() - 1)
        - (Double) indexList.get(indexList.size() - 2)) > maxXDifference)
      maxXDifference = Math.abs((Double) indexList.get(indexList.size() - 1)
          - (Double) indexList.get(indexList.size() - 2));
  }

  /**
   * Gets the max x difference.
   *
   * @return the max x difference
   */
  public double getMaxXDifference() {
    return maxXDifference;
  }

  /**
   * 描述：TODO.
   *
   * @version v1.0
   * @see java.util.TreeMap#clear()
   * @author: amsoft.cn
   * @date：2013-6-17 上午9:04:48
   */
  public void clear() {
    updateMaxXDifference();
    super.clear();
    indexList.clear();
  }

  /**
   * Returns X-value according to the given index.
   *
   * @param index the index
   * @return the X value
   */
  public K getXByIndex(int index) {
    return indexList.get(index);
  }

  /**
   * Returns Y-value according to the given index.
   *
   * @param index the index
   * @return the Y value
   */
  public V getYByIndex(int index) {
    K key = indexList.get(index);
    return this.get(key);
  }

  /**
   * Returns XY-entry according to the given index.
   *
   * @param index the index
   * @return the X and Y values
   */
  public XYEntry<K, V> getByIndex(int index) {
    K key = indexList.get(index);
    return new XYEntry<K, V>(key, this.get(key));
  }

  /**
   * Removes entry from map by index.
   *
   * @param index the index
   * @return the xY entry
   */
  public XYEntry<K, V> removeByIndex(int index) {
    K key = indexList.remove(index);
    return new XYEntry<K, V>(key, this.remove(key));
  }

  /**
   * Gets the index for key.
   *
   * @param key the key
   * @return the index for key
   */
  public int getIndexForKey(K key) {
    return Collections.binarySearch(indexList, key, null);
  }
}
