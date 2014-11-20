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
import android.view.MotionEvent;

// TODO: Auto-generated Javadoc
/**
 * The main handler of the touch events.
 */
public class TouchHandler implements ITouchHandler {
  /** The chart renderer. */
  private DefaultRenderer mRenderer;
  /** The old x coordinate. */
  private float oldX;
  /** The old y coordinate. */
  private float oldY;
  /** The old x2 coordinate. */
  private float oldX2;
  /** The old y2 coordinate. */
  private float oldY2;
  /** The zoom buttons rectangle. */
  private RectF zoomR = new RectF();
  /** The pan tool. */
  private Pan mPan;
  /** The zoom for the pinch gesture. */
  private Zoom mPinchZoom;
  /** The graphical view. */
  private GraphicalView graphicalView;

  /**
   * Creates a new graphical view.
   * 
   * @param view the graphical view
   * @param chart the chart to be drawn
   */
  public TouchHandler(GraphicalView view, AbstractChart chart) {
    graphicalView = view;
    zoomR = graphicalView.getZoomRectangle();
    if (chart instanceof XYChart) {
      mRenderer = ((XYChart) chart).getRenderer();
    } else {
      mRenderer = ((RoundChart) chart).getRenderer();
    }
    if (mRenderer.isPanEnabled()) {
      mPan = new Pan(chart);
    }
    if (mRenderer.isZoomEnabled()) {
      mPinchZoom = new Zoom(chart, true, 1);
    }
  }

  /**
   * Handles the touch event.
   * 图表的滑动事件
   *
   * @param event the touch event
   * @return true, if successful
   */
  public boolean handleTouch(MotionEvent event) {
    int action = event.getAction();
    if (mRenderer != null && action == MotionEvent.ACTION_MOVE) {
      if (oldX >= 0 || oldY >= 0) {
        float newX = event.getX(0);
        float newY = event.getY(0);
        if (event.getPointerCount() > 1 && (oldX2 >= 0 || oldY2 >= 0) && mRenderer.isZoomEnabled()) {
          float newX2 = event.getX(1);
          float newY2 = event.getY(1);
          float newDeltaX = Math.abs(newX - newX2);
          float newDeltaY = Math.abs(newY - newY2);
          float oldDeltaX = Math.abs(oldX - oldX2);
          float oldDeltaY = Math.abs(oldY - oldY2);
          float zoomRate = 1;

          float tan1 = Math.abs(newY - oldY) / Math.abs(newX - oldX);
          float tan2 = Math.abs(newY2 - oldY2) / Math.abs(newX2 - oldX2);
          if ( tan1 <= 0.577 && tan2 <= 0.577) {
            // horizontal pinch zoom, |deltaY| / |deltaX| is [0 ~ 0.577], 0.577 is the approximate value of tan(Pi/6)
            zoomRate = newDeltaX / oldDeltaX;
            if (zoomRate > 0.909 && zoomRate < 1.1) {
              mPinchZoom.setZoomRate(zoomRate);
              mPinchZoom.apply(Zoom.ZOOM_AXIS_X);
            }
          } else if ( tan1 >= 1.732 && tan2 >= 1.732 ) {
            // pinch zoom vertically, |deltaY| / |deltaX| is [1.732 ~ infinity], 1.732 is the approximate value of tan(Pi/3)
            zoomRate = newDeltaY / oldDeltaY;
            if (zoomRate > 0.909 && zoomRate < 1.1) {
              mPinchZoom.setZoomRate(zoomRate);
              mPinchZoom.apply(Zoom.ZOOM_AXIS_Y);
            }
          } else if ( (tan1 > 0.577 && tan1 < 1.732) && (tan2 > 0.577 && tan2 < 1.732) ){
            // pinch zoom diagonally
            if (Math.abs(newX - oldX) >= Math.abs(newY - oldY)) {
              zoomRate = newDeltaX / oldDeltaX;
            } else {
              zoomRate = newDeltaY / oldDeltaY;
            }
            if (zoomRate > 0.909 && zoomRate < 1.1) {
              mPinchZoom.setZoomRate(zoomRate);
              mPinchZoom.apply(Zoom.ZOOM_AXIS_XY);
            }
          }
          oldX2 = newX2;
          oldY2 = newY2;
        } else if (mRenderer.isPanEnabled()) {
          mPan.apply(oldX, oldY, newX, newY);
          oldX2 = 0;
          oldY2 = 0;
        }
        oldX = newX;
        oldY = newY;
        graphicalView.repaint();
        return true;
      }
    } else if (action == MotionEvent.ACTION_DOWN) {
        oldX = event.getX(0);
        oldY = event.getY(0);
    } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
        oldX2 = 0;
        oldY2 = 0;
        if (action == MotionEvent.ACTION_POINTER_UP) {
          oldX = -1;
          oldY = -1;
        }
    }
    return !mRenderer.isClickEnabled();
  }

  /**
   * 描述：标尺滑动和比例缩放的点击的事件.
   *
   * @version v1.0
   * @param event the event
   * @return true, if successful
   * @see com.ab.view.chart.ITouchHandler#handleTouchControl(android.view.MotionEvent)
   * @author: amsoft.cn
   * @date：2013-6-7 上午9:56:06
   */
  @Override
  public boolean handleTouchControl(MotionEvent event) {
    int action = event.getAction();
    //判断是否为比例缩放
    if (mRenderer != null && action == MotionEvent.ACTION_MOVE) {
      if (oldX >= 0 || oldY >= 0) {
    	  if (oldX >= 0 || oldY >= 0) {
    	        float newX = event.getX();
    	        float newY = event.getY();
    	        oldX = newX;
    	        oldY = newY;
    	        graphicalView.repaint();
    	        return true;
    	  }
      }
    } else if (action == MotionEvent.ACTION_DOWN) {
      oldX = event.getX(0);
      oldY = event.getY(0);
      if (mRenderer != null && mRenderer.isZoomEnabled() && zoomR.contains(oldX, oldY)) {
        if (oldX < zoomR.left + zoomR.width() / 3) {
          graphicalView.zoomIn();
        } else if (oldX < zoomR.left + zoomR.width() * 2 / 3) {
          graphicalView.zoomOut();
        } else {
          graphicalView.zoomReset();
        }
        return true;
      }
    } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_POINTER_UP) {
      oldX2 = 0;
      oldY2 = 0;
      if (action == MotionEvent.ACTION_POINTER_UP) {
        oldX = -1;
        oldY = -1;
      }
    }
    return !mRenderer.isClickEnabled();
  }

  /**
   * Adds a new zoom listener.
   * 
   * @param listener zoom listener
   */
  public void addZoomListener(ZoomListener listener) {
    if (mPinchZoom != null) {
      mPinchZoom.addZoomListener(listener);
    }
  }

  /**
   * Removes a zoom listener.
   * 
   * @param listener zoom listener
   */
  public void removeZoomListener(ZoomListener listener) {
    if (mPinchZoom != null) {
      mPinchZoom.removeZoomListener(listener);
    }
  }

  /**
   * Adds a new pan listener.
   * 
   * @param listener pan listener
   */
  public void addPanListener(PanListener listener) {
    if (mPan != null) {
      mPan.addPanListener(listener);
    }
  }

  /**
   * Removes a pan listener.
   * 
   * @param listener pan listener
   */
  public void removePanListener(PanListener listener) {
    if (mPan != null) {
      mPan.removePanListener(listener);
    }
  }
  
  /**
   * Gets the old x.
   *
   * @return the old x
   */
  public float getOldX() {
    return oldX;
  }


  /**
   * Gets the old y.
   *
   * @return the old y
   */
  public float getOldY() {
    return oldY;
  }
  
}
