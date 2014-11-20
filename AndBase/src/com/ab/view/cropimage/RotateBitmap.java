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

import android.graphics.Bitmap;
import android.graphics.Matrix;

// TODO: Auto-generated Javadoc
/**
 * The Class RotateBitmap.
 */
public class RotateBitmap {
    
    /** The Constant TAG. */
    public static final String TAG = "RotateBitmap";
    
    /** The m bitmap. */
    private Bitmap mBitmap;
    
    /** The m rotation. */
    private int mRotation;

    /**
     * Instantiates a new rotate bitmap.
     *
     * @param bitmap the bitmap
     */
    public RotateBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mRotation = 0;
    }

    /**
     * Instantiates a new rotate bitmap.
     *
     * @param bitmap the bitmap
     * @param rotation the rotation
     */
    public RotateBitmap(Bitmap bitmap, int rotation) {
        mBitmap = bitmap;
        mRotation = rotation % 360;
    }

    /**
     * Sets the rotation.
     *
     * @param rotation the new rotation
     */
    public void setRotation(int rotation) {
        mRotation = rotation;
    }

    /**
     * Gets the rotation.
     *
     * @return the rotation
     */
    public int getRotation() {
        return mRotation;
    }

    /**
     * Gets the bitmap.
     *
     * @return the bitmap
     */
    public Bitmap getBitmap() {
        return mBitmap;
    }

    /**
     * Sets the bitmap.
     *
     * @param bitmap the new bitmap
     */
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    /**
     * Gets the rotate matrix.
     *
     * @return the rotate matrix
     */
    public Matrix getRotateMatrix() {
        Matrix matrix = new Matrix();
        if (mRotation != 0) {
            int cx = mBitmap.getWidth() / 2;
            int cy = mBitmap.getHeight() / 2;
            matrix.preTranslate(-cx, -cy);
            matrix.postRotate(mRotation);
            matrix.postTranslate(getWidth() / 2, getHeight() / 2);
        }
        return matrix;
    }

    /**
     * Checks if is orientation changed.
     *
     * @return true, if is orientation changed
     */
    public boolean isOrientationChanged() {
        return (mRotation / 90) % 2 != 0;
    }

    /**
     * Gets the height.
     *
     * @return the height
     */
    public int getHeight() {
        if (isOrientationChanged()) {
            return mBitmap.getWidth();
        } else {
            return mBitmap.getHeight();
        }
    }

    /**
     * Gets the width.
     *
     * @return the width
     */
    public int getWidth() {
        if (isOrientationChanged()) {
            return mBitmap.getHeight();
        } else {
            return mBitmap.getWidth();
        }
    }

    /**
     * Recycle.
     */
    public void recycle() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
    }
}
