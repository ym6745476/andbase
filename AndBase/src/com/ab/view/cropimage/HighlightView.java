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


import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.RegionIterator;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.ab.util.AbFileUtil;
import com.ab.util.AbImageUtil;


// TODO: Auto-generated Javadoc
// This class is used by CropImage to display a highlighted cropping rectangle
// overlayed with the image. There are two coordinate spaces in use. One is
// image, another is screen. computeLayout() uses mMatrix to map from image
// space to screen space.
/**
 * The Class HighlightView.
 */
public class HighlightView {

    /** The Constant TAG. */
    @SuppressWarnings("unused")
    private static final String TAG = "HighlightView";
    
    /** The m context. */
    View mContext; // The View displaying the image.

    /** The Constant GROW_NONE. */
    public static final int GROW_NONE = (1 << 0);
    
    /** The Constant GROW_LEFT_EDGE. */
    public static final int GROW_LEFT_EDGE = (1 << 1);
    
    /** The Constant GROW_RIGHT_EDGE. */
    public static final int GROW_RIGHT_EDGE = (1 << 2);
    
    /** The Constant GROW_TOP_EDGE. */
    public static final int GROW_TOP_EDGE = (1 << 3);
    
    /** The Constant GROW_BOTTOM_EDGE. */
    public static final int GROW_BOTTOM_EDGE = (1 << 4);
    
    /** The Constant MOVE. */
    public static final int MOVE = (1 << 5);

    /**
     * Instantiates a new highlight view.
     *
     * @param ctx the ctx
     */
    public HighlightView(View ctx) {
        mContext = ctx;
    }

    /**
     * Inits the.
     */
    private void init() {
        mResizeDrawableDiagonal = AbImageUtil.bitmapToDrawable(AbFileUtil.getBitmapFromSrc("image/crop_big.png"));
        mResizeDrawableDiagonal2 = AbImageUtil.bitmapToDrawable(AbFileUtil.getBitmapFromSrc("image/crop_small.png"));
    }

    /** The m is focused. */
    public boolean mIsFocused;
    
    /** The m hidden. */
    boolean mHidden;

    /**
     * Checks for focus.
     *
     * @return true, if successful
     */
    public boolean hasFocus() {
        return mIsFocused;
    }

    /**
     * Sets the focus.
     *
     * @param f the new focus
     */
    public void setFocus(boolean f) {
        mIsFocused = f;
    }

    /**
     * Sets the hidden.
     *
     * @param hidden the new hidden
     */
    public void setHidden(boolean hidden) {
        mHidden = hidden;
    }

    /**
     * Draw.
     *
     * @param canvas the canvas
     */
    public void draw(Canvas canvas) {
        if (mHidden) {
            return;
        }
        canvas.save();
        Path path = new Path();
        if (!hasFocus()) {
            mOutlinePaint.setColor(0xFF000000);
            canvas.drawRect(mDrawRect, mOutlinePaint);
        } else {
            Rect viewDrawingRect = new Rect();
            mContext.getDrawingRect(viewDrawingRect);
            if (mCircle) {
                float width = mDrawRect.width();
                float height = mDrawRect.height();
                path.addCircle(mDrawRect.left + (width / 2), mDrawRect.top + (height / 2), width / 2, Path.Direction.CW);
                mOutlinePaint.setColor(0xFFEF04D6);
            } else {
                path.addRect(new RectF(mDrawRect), Path.Direction.CW);
                mOutlinePaint.setColor(0xFFFF8A00);
            }
            Region region = new Region(); 
            region.set(viewDrawingRect);
            region.op(mDrawRect, Region.Op.DIFFERENCE);
            RegionIterator iter = new RegionIterator(region);
            Rect r = new Rect();
            while (iter.next(r)) {
            	canvas.drawRect(r, hasFocus() ? mFocusPaint : mNoFocusPaint);
            }
            //android 4.0 Support bad.java.lang.UnsupportedOperationException
			//canvas.clipPath(path, Region.Op.DIFFERENCE);
			//canvas.drawRect(viewDrawingRect, hasFocus() ? mFocusPaint : mNoFocusPaint);
           
            canvas.restore();
            canvas.drawPath(path, mOutlinePaint);

            if (mMode == ModifyMode.Grow) {
                if (mCircle) {
                    int width = mResizeDrawableDiagonal.getIntrinsicWidth();
                    int height = mResizeDrawableDiagonal.getIntrinsicHeight();

                    int d = (int) Math.round(Math.cos(/* 45deg */Math.PI / 4D) * (mDrawRect.width() / 2D));
                    int x = mDrawRect.left + (mDrawRect.width() / 2) + d - width / 2;
                    int y = mDrawRect.top + (mDrawRect.height() / 2) - d - height / 2;
                    mResizeDrawableDiagonal.setBounds(x, y, x + mResizeDrawableDiagonal.getIntrinsicWidth(), y
                            + mResizeDrawableDiagonal.getIntrinsicHeight());
                    mResizeDrawableDiagonal.draw(canvas);
                } else {
//                    int left = mDrawRect.left + 1;
//                    int right = mDrawRect.right + 1;
//                    int top = mDrawRect.top + 4;
//                    int bottom = mDrawRect.bottom + 3;
//
//                    int widthWidth = mResizeDrawableWidth.getIntrinsicWidth() / 2;
//                    int widthHeight = mResizeDrawableWidth.getIntrinsicHeight() / 2;
//                    int heightHeight = mResizeDrawableHeight.getIntrinsicHeight() / 2;
//                    int heightWidth = mResizeDrawableHeight.getIntrinsicWidth() / 2;
//
//                    int xMiddle = mDrawRect.left + ((mDrawRect.right - mDrawRect.left) / 2);
//                    int yMiddle = mDrawRect.top + ((mDrawRect.bottom - mDrawRect.top) / 2);
//
//                    mResizeDrawableWidth.setBounds(left - widthWidth, yMiddle - widthHeight, left + widthWidth, yMiddle
//                            + widthHeight);
//                    mResizeDrawableWidth.draw(canvas);
//
//                    mResizeDrawableWidth.setBounds(right - widthWidth, yMiddle - widthHeight, right + widthWidth, yMiddle
//                            + widthHeight);
//                    mResizeDrawableWidth.draw(canvas);
//
//                    mResizeDrawableHeight.setBounds(xMiddle - heightWidth, top - heightHeight, xMiddle + heightWidth, top
//                            + heightHeight);
//                    mResizeDrawableHeight.draw(canvas);
//
//                    mResizeDrawableHeight.setBounds(xMiddle - heightWidth, bottom - heightHeight, xMiddle + heightWidth, bottom
//                            + heightHeight);
//                    mResizeDrawableHeight.draw(canvas);
                }
            }
            if (mCircle) {
            	
            }else{
	            int left = mDrawRect.left + 1;
	            int right = mDrawRect.right + 1;
	            int top = mDrawRect.top + 4;
	            int bottom = mDrawRect.bottom + 3;
	
	            int widthWidth = mResizeDrawableDiagonal.getIntrinsicWidth() / 2;
	            int widthHeight = mResizeDrawableDiagonal.getIntrinsicHeight() / 2;

	            mResizeDrawableDiagonal2.setBounds(left - widthWidth, top - widthHeight, left + widthWidth, top+ widthHeight);
	            mResizeDrawableDiagonal2.draw(canvas);
	            mResizeDrawableDiagonal.setBounds(right - widthWidth, top - widthHeight, right + widthWidth, top+ widthHeight);
	            mResizeDrawableDiagonal.draw(canvas);
	            mResizeDrawableDiagonal.setBounds(left - widthWidth, bottom - widthHeight, left + widthWidth, bottom+ widthHeight);
	            mResizeDrawableDiagonal.draw(canvas);
	            mResizeDrawableDiagonal2.setBounds(right - widthWidth, bottom - widthHeight, right + widthWidth, bottom+ widthHeight);
	            mResizeDrawableDiagonal2.draw(canvas);
            }
        }
    }

    /**
     * Sets the mode.
     *
     * @param mode the new mode
     */
    public void setMode(ModifyMode mode) {
        if (mode != mMode) {
            mMode = mode;
            mContext.invalidate();
        }
    }

    // Determines which edges are hit by touching at (x, y).
    /**
     * Gets the hit.
     *
     * @param x the x
     * @param y the y
     * @return the hit
     */
    public int getHit(float x, float y) {
        Rect r = computeLayout();
        final float hysteresis = 20F;
        int retval = GROW_NONE;

        if (mCircle) {
            float distX = x - r.centerX();
            float distY = y - r.centerY();
            int distanceFromCenter = (int) Math.sqrt(distX * distX + distY * distY);
            int radius = mDrawRect.width() / 2;
            int delta = distanceFromCenter - radius;
            if (Math.abs(delta) <= hysteresis) {
                if (Math.abs(distY) > Math.abs(distX)) {
                    if (distY < 0) {
                        retval = GROW_TOP_EDGE;
                    } else {
                        retval = GROW_BOTTOM_EDGE;
                    }
                } else {
                    if (distX < 0) {
                        retval = GROW_LEFT_EDGE;
                    } else {
                        retval = GROW_RIGHT_EDGE;
                    }
                }
            } else 
            	if (distanceFromCenter < radius) {
                retval = MOVE;
            } else {
                retval = GROW_NONE;
            }
        } else {
            // verticalCheck makes sure the position is between the top and
            // the bottom edge (with some tolerance). Similar for horizCheck.
            boolean verticalCheck = (y >= r.top - hysteresis) && (y < r.bottom + hysteresis);
            boolean horizCheck = (x >= r.left - hysteresis) && (x < r.right + hysteresis);

            // Check whether the position is near some edge(s).
            if ((Math.abs(r.left - x) < hysteresis) && verticalCheck) {
                retval |= GROW_LEFT_EDGE;
            }
            if ((Math.abs(r.right - x) < hysteresis) && verticalCheck) {
                retval |= GROW_RIGHT_EDGE;
            }
            if ((Math.abs(r.top - y) < hysteresis) && horizCheck) {
                retval |= GROW_TOP_EDGE;
            }
            if ((Math.abs(r.bottom - y) < hysteresis) && horizCheck) {
                retval |= GROW_BOTTOM_EDGE;
            }

            // Not near any edge but inside the rectangle: move.
            if (retval == GROW_NONE && r.contains((int) x, (int) y)) {
                retval = MOVE;
            }
        }
        return retval;
    }

    // Handles motion (dx, dy) in screen space.
    // The "edge" parameter specifies which edges the user is dragging.
    /**
     * Handle motion.
     *
     * @param edge the edge
     * @param dx the dx
     * @param dy the dy
     */
    public void handleMotion(int edge, float dx, float dy) {
        Rect r = computeLayout();
        if (edge == GROW_NONE) {
            return;
        } else if (edge == MOVE) {
            // Convert to image space before sending to moveBy().
            moveBy(dx * (mCropRect.width() / r.width()), dy * (mCropRect.height() / r.height()));
        } else {
            if (((GROW_LEFT_EDGE | GROW_RIGHT_EDGE) & edge) == 0) {
                dx = 0;
            }

            if (((GROW_TOP_EDGE | GROW_BOTTOM_EDGE) & edge) == 0) {
                dy = 0;
            }

            // Convert to image space before sending to growBy().
            float xDelta = dx * (mCropRect.width() / r.width());
            float yDelta = dy * (mCropRect.height() / r.height());
            growBy((((edge & GROW_LEFT_EDGE) != 0) ? -1 : 1) * xDelta, (((edge & GROW_TOP_EDGE) != 0) ? -1 : 1) * yDelta);
        }
    }

    // Grows the cropping rectange by (dx, dy) in image space.
    /**
     * Move by.
     *
     * @param dx the dx
     * @param dy the dy
     */
    void moveBy(float dx, float dy) {
        Rect invalRect = new Rect(mDrawRect);

        mCropRect.offset(dx, dy);

        // Put the cropping rectangle inside image rectangle.
        mCropRect.offset(Math.max(0, mImageRect.left - mCropRect.left), Math.max(0, mImageRect.top - mCropRect.top));

        mCropRect.offset(Math.min(0, mImageRect.right - mCropRect.right), Math.min(0, mImageRect.bottom - mCropRect.bottom));

        mDrawRect = computeLayout();
        invalRect.union(mDrawRect);
        invalRect.inset(-10, -10);
//        mContext.invalidate(invalRect);
        mContext.invalidate();
    }

    // Grows the cropping rectange by (dx, dy) in image space.
    /**
     * Grow by.
     *
     * @param dx the dx
     * @param dy the dy
     */
    void growBy(float dx, float dy) {
        if (mMaintainAspectRatio) {
            if (dx != 0) {
                dy = dx / mInitialAspectRatio;
            } else if (dy != 0) {
                dx = dy * mInitialAspectRatio;
            }
        }

        // Don't let the cropping rectangle grow too fast.
        // Grow at most half of the difference between the image rectangle and
        // the cropping rectangle.
        RectF r = new RectF(mCropRect);
        if (dx > 0F && r.width() + 2 * dx > mImageRect.width()) {

            float adjustment = (mImageRect.width() - r.width()) / 2F;
            dx = adjustment;
            if (mMaintainAspectRatio) {
                dy = dx / mInitialAspectRatio;
            }
        }
        if (dy > 0F && r.height() + 2 * dy > mImageRect.height()) {
            float adjustment = (mImageRect.height() - r.height()) / 2F;
            dy = adjustment;
            if (mMaintainAspectRatio) {
                dx = dy * mInitialAspectRatio;
            }
        }

        r.inset(-dx, -dy);

        // Don't let the cropping rectangle shrink too fast.
        final float widthCap = 25F;
        if (r.width() < widthCap) {
        	return;
//            r.inset(-(widthCap - r.width()) / 2F, 0F);
        }
        float heightCap = mMaintainAspectRatio ? (widthCap / mInitialAspectRatio) : widthCap;
        if (r.height() < heightCap) {
        	return;
//            r.inset(0F, -(heightCap - r.height()) / 2F);
        }

        // Put the cropping rectangle inside the image rectangle.
        if (r.left < mImageRect.left) {
            r.offset(mImageRect.left - r.left, 0F);
        } else if (r.right > mImageRect.right) {
            r.offset(-(r.right - mImageRect.right), 0);
        }
        if (r.top < mImageRect.top) {
            r.offset(0F, mImageRect.top - r.top);
        } else if (r.bottom > mImageRect.bottom) {
            r.offset(0F, -(r.bottom - mImageRect.bottom));
        }

        mCropRect.set(r);
        mDrawRect = computeLayout();
        mContext.invalidate();
    }

    // Returns the cropping rectangle in image space.
    /**
     * Gets the crop rect.
     *
     * @return the crop rect
     */
    public Rect getCropRect() {
        return new Rect((int) mCropRect.left, (int) mCropRect.top, (int) mCropRect.right, (int) mCropRect.bottom);
    }

    // Maps the cropping rectangle from image space to screen space.
    /**
     * Compute layout.
     *
     * @return the rect
     */
    private Rect computeLayout() {
		RectF r = new RectF(mCropRect.left, mCropRect.top, mCropRect.right, mCropRect.bottom);
        mMatrix.mapRect(r);
        return new Rect(Math.round(r.left), Math.round(r.top), Math.round(r.right), Math.round(r.bottom));
    }

    /**
     * Invalidate.
     */
    public void invalidate() {
        mDrawRect = computeLayout();
    }

    /**
     * Setup.
     *
     * @param m the m
     * @param imageRect the image rect
     * @param cropRect the crop rect
     * @param circle the circle
     * @param maintainAspectRatio the maintain aspect ratio
     */
    public void setup(Matrix m, Rect imageRect, RectF cropRect, boolean circle, boolean maintainAspectRatio) {
        if (circle) {
            maintainAspectRatio = true;
        }
        mMatrix = new Matrix(m);

        mCropRect = cropRect;
        mImageRect = new RectF(imageRect);
        mMaintainAspectRatio = maintainAspectRatio;
        mCircle = circle;

        mInitialAspectRatio = mCropRect.width() / mCropRect.height();
        mDrawRect = computeLayout();

        mFocusPaint.setARGB(125, 50, 50, 50);
        mNoFocusPaint.setARGB(125, 50, 50, 50);
        mOutlinePaint.setStrokeWidth(3F);
        mOutlinePaint.setStyle(Paint.Style.STROKE);
        mOutlinePaint.setAntiAlias(true);

        mMode = ModifyMode.None;
        init();
    }

    /**
     * The Enum ModifyMode.
     */
    public enum ModifyMode {
        
        /** The None. */
        None, 
 /** The Move. */
 Move, 
 /** The Grow. */
 Grow
    }

    /** The m mode. */
    private ModifyMode mMode = ModifyMode.None;

    /** The m draw rect. */
    public Rect mDrawRect; // in screen space
    
    /** The m image rect. */
    public RectF mImageRect; // in image space
    
    /** The m crop rect. */
    public RectF mCropRect; // in image space
    
    /** The m matrix. */
    public Matrix mMatrix;

    /** The m maintain aspect ratio. */
    private boolean mMaintainAspectRatio = false;
    
    /** The m initial aspect ratio. */
    private float mInitialAspectRatio;
    
    /** The m circle. */
    private boolean mCircle = false;

//    private Drawable mResizeDrawableWidth;
//    private Drawable mResizeDrawableHeight;
    /** The m resize drawable diagonal. */
private Drawable mResizeDrawableDiagonal;
    
    /** The m resize drawable diagonal2. */
    private Drawable mResizeDrawableDiagonal2;

    /** The m focus paint. */
    private final Paint mFocusPaint = new Paint();
    
    /** The m no focus paint. */
    private final Paint mNoFocusPaint = new Paint();
    
    /** The m outline paint. */
    private final Paint mOutlinePaint = new Paint();
}
