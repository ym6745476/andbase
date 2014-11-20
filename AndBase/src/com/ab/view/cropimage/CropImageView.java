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
package com.ab.view.cropimage;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;


// TODO: Auto-generated Javadoc
/**
 * The Class CropImageView.
 */
public class CropImageView extends CropViewBase {
    
    /** The m highlight views. */
    public ArrayList<HighlightView> mHighlightViews = new ArrayList<HighlightView>();
    
    /** The m motion highlight view. */
    HighlightView mMotionHighlightView = null;
    
    /** The m last y. */
    float mLastX, mLastY;
    
    /** The m motion edge. */
    int mMotionEdge;
    
    /** The m crop image. */
    private CropImage mCropImage;

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @param changed the changed
     * @param left the left
     * @param top the top
     * @param right the right
     * @param bottom the bottom
     * @see com.ab.view.cropimage.CropViewBase#onLayout(boolean, int, int, int, int)
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mBitmapDisplayed.getBitmap() != null) {
            for (HighlightView hv : mHighlightViews) {
                hv.mMatrix.set(getImageMatrix());
                hv.invalidate();
                if (hv.mIsFocused) {
                    centerBasedOnHighlightView(hv);
                }
            }
        }
    }

    /**
     * Instantiates a new crop image view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public CropImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @param scale the scale
     * @param centerX the center x
     * @param centerY the center y
     * @see com.ab.view.cropimage.CropViewBase#zoomTo(float, float, float)
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    protected void zoomTo(float scale, float centerX, float centerY) {
        super.zoomTo(scale, centerX, centerY);
        for (HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @see com.ab.view.cropimage.CropViewBase#zoomIn()
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    protected void zoomIn() {
        super.zoomIn();
        for (HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @see com.ab.view.cropimage.CropViewBase#zoomOut()
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    protected void zoomOut() {
        super.zoomOut();
        for (HighlightView hv : mHighlightViews) {
            hv.mMatrix.set(getImageMatrix());
            hv.invalidate();
        }
    }

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @param deltaX the delta x
     * @param deltaY the delta y
     * @see com.ab.view.cropimage.CropViewBase#postTranslate(float, float)
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    protected void postTranslate(float deltaX, float deltaY) {
        super.postTranslate(deltaX, deltaY);
        for (int i = 0; i < mHighlightViews.size(); i++) {
            HighlightView hv = mHighlightViews.get(i);
            hv.mMatrix.postTranslate(deltaX, deltaY);
            hv.invalidate();
        }
    }

    // According to the event's position, change the focus to the first
    // hitting cropping rectangle.
    /**
     * Recompute focus.
     *
     * @param event the event
     */
    private void recomputeFocus(MotionEvent event) {
        for (int i = 0; i < mHighlightViews.size(); i++) {
            HighlightView hv = mHighlightViews.get(i);
            hv.setFocus(false);
            hv.invalidate();
        }

        for (int i = 0; i < mHighlightViews.size(); i++) {
            HighlightView hv = mHighlightViews.get(i);
            int edge = hv.getHit(event.getX(), event.getY());
            if (edge != HighlightView.GROW_NONE) {
                if (!hv.hasFocus()) {
                    hv.setFocus(true);
                    hv.invalidate();
                }
                break;
            }
        }
        invalidate();
    }

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @param event the event
     * @return true, if successful
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	CropImage cropImage = mCropImage;
        if (cropImage.mSaving) {
            return false;
        }

        switch (event.getAction()) {
        // CR: inline case blocks.
        case MotionEvent.ACTION_DOWN: 
            if (cropImage.mWaitingToPick) {
                recomputeFocus(event);
            } else {
                for (int i = 0; i < mHighlightViews.size(); i++) { 
                    HighlightView hv = mHighlightViews.get(i);
                    int edge = hv.getHit(event.getX(), event.getY());
                    if (edge != HighlightView.GROW_NONE) {
                        mMotionEdge = edge;
                        mMotionHighlightView = hv;
                        mLastX = event.getX();
                        mLastY = event.getY();
                        // CR: get rid of the extraneous parens below.
                        mMotionHighlightView.setMode((edge == HighlightView.MOVE) ? HighlightView.ModifyMode.Move
                                : HighlightView.ModifyMode.Grow);
                        break;
                    }
                }
            }
            break;
        // CR: vertical space before case blocks.
        case MotionEvent.ACTION_UP:
            if (cropImage.mWaitingToPick) {
                for (int i = 0; i < mHighlightViews.size(); i++) {
                    HighlightView hv = mHighlightViews.get(i);
                    if (hv.hasFocus()) {
                        cropImage.mCrop = hv;
                        for (int j = 0; j < mHighlightViews.size(); j++) {
                            if (j == i) { 
                            	 //if j != i do your shit; no need,for continue.
                                continue;
                            }
                            mHighlightViews.get(j).setHidden(true);
                        }
                        centerBasedOnHighlightView(hv);
                        mCropImage.mWaitingToPick = false;
                        return true;
                    }
                }
            } else if (mMotionHighlightView != null) {
                centerBasedOnHighlightView(mMotionHighlightView);
                mMotionHighlightView.setMode(HighlightView.ModifyMode.None);
            }
            mMotionHighlightView = null;
            break;
        case MotionEvent.ACTION_MOVE:
            if (cropImage.mWaitingToPick) {
                recomputeFocus(event);
            } else if (mMotionHighlightView != null) {
                mMotionHighlightView.handleMotion(mMotionEdge, event.getX() - mLastX, event.getY() - mLastY);
                mLastX = event.getX();
                mLastY = event.getY();

                if (true) {
                    // This section of code is optional. It has some user
                    // benefit in that moving the crop rectangle against
                    // the edge of the screen causes scrolling but it means
                    // that the crop rectangle is no longer fixed under
                    // the user's finger.
                    ensureVisible(mMotionHighlightView);
                }
            }
            break;
        }

        switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
            center(true, true);
            break;
        case MotionEvent.ACTION_MOVE:
            // if we're not zoomed then there's no point in even allowing
            // the user to move the image around. This call to center puts
            // it back to the normalized location (with false meaning don't
            // animate).
			//if (getScale() == 1F) {
			//center(true, true);
			//}
            center(true, true);
            break;
        }

        return true;
    }

    // Pan the displayed image to make sure the cropping rectangle is visible.
    /**
     * Ensure visible.
     *
     * @param hv the hv
     */
    private void ensureVisible(HighlightView hv) {
        Rect r = hv.mDrawRect;

        int panDeltaX1 = Math.max(0, getLeft() - r.left);
        int panDeltaX2 = Math.min(0, getRight() - r.right);

        int panDeltaY1 = Math.max(0, getTop() - r.top);
        int panDeltaY2 = Math.min(0, getBottom() - r.bottom);

        int panDeltaX = panDeltaX1 != 0 ? panDeltaX1 : panDeltaX2;
        int panDeltaY = panDeltaY1 != 0 ? panDeltaY1 : panDeltaY2;

        if (panDeltaX != 0 || panDeltaY != 0) {
            panBy(panDeltaX, panDeltaY);
        }
    }

    // If the cropping rectangle's size changed significantly, change the
    // view's center and scale according to the cropping rectangle.
    //hv.mDrawRect.width<0.54*thisWidth||width>0.66*thisWidth,need to zoom
    /**
     * Center based on highlight view.
     *
     * @param hv the hv
     */
    private void centerBasedOnHighlightView(HighlightView hv) {
        Rect drawRect = hv.mDrawRect;
        
        float width = drawRect.width();
        float height = drawRect.height();

        float thisWidth = getWidth();
        float thisHeight = getHeight();

        float z1 = thisWidth / width * .6F;
        float z2 = thisHeight / height * .6F;

        float zoom = Math.min(z1, z2);
        zoom = zoom * this.getScale();
        zoom = Math.max(1F, zoom);//assure getScale()>1

        if ((Math.abs(zoom - getScale()) / zoom) > 0.1) {
            float[] coordinates = new float[] { hv.mCropRect.centerX(), hv.mCropRect.centerY() };
            getImageMatrix().mapPoints(coordinates);
            zoomTo(zoom, coordinates[0], coordinates[1], 300F); // CR: 300.0f.
        }

        ensureVisible(hv);
    }

    /**
     * 描述：TODO.
     *
     * @version v1.0
     * @param canvas the canvas
     * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
     * @author: amsoft.cn
     * @date：2013-6-17 上午9:04:49
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mHighlightViews.size(); i++) {
            mHighlightViews.get(i).draw(canvas);
        }
    }

    /**
     * Adds the.
     *
     * @param hv the hv
     */
    public void add(HighlightView hv) {
    	mHighlightViews.clear();
        mHighlightViews.add(hv);
        invalidate();
    }
    
    /**
     * Sets the crop image.
     *
     * @param cropImage the new crop image
     */
    public void setCropImage(CropImage cropImage){
    	mCropImage = cropImage;
    }
    
    /**
     * Reset view.
     *
     * @param b the b
     */
    public void resetView(Bitmap b){
    	setImageBitmap(b);
		setImageBitmapResetBase(b, true);
		setImageMatrix(getImageViewMatrix());
		int width = mBitmapDisplayed.getWidth();
        int height = mBitmapDisplayed.getHeight();
        Rect imageRect = new Rect(0, 0, width, height);
        int cropWidth = Math.min(width, height) * 4 / 5;
        int cropHeight = cropWidth;
        int x = (width - cropWidth) / 2;
        int y = (height - cropHeight) / 2;
        RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
        HighlightView hv = new HighlightView(this);
        hv.setup(getImageViewMatrix(), imageRect, cropRect, false, true);
        hv.setFocus(true);
		add(hv);
		centerBasedOnHighlightView(hv);
		hv.setMode(HighlightView.ModifyMode.None);
		center(true, true);
		invalidate();
    }
    
}
