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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.ImageView;

// TODO: Auto-generated Javadoc
/**
 * The Class CropViewBase.
 */
public abstract class CropViewBase extends ImageView {

	/** The Constant TAG. */
	private static final String TAG = "ImageViewTouchBase";

	// This is the base transformation which is used to show the image
	// initially. The current computation for this shows the image in
	// it's entirety, letterboxing as needed. One could choose to
	// show the image as cropped instead.
	//
	// This matrix is recomputed when we go from the thumbnail image to
	// the full size image.
	/** The m base matrix. */
	protected Matrix mBaseMatrix = new Matrix();

	// This is the supplementary transformation which reflects what
	// the user has done in terms of zooming and panning.
	//
	// This matrix remains the same when we go from the thumbnail image
	// to the full size image.
	/** The m supp matrix. */
	protected Matrix mSuppMatrix = new Matrix();

	// This is the final matrix which is computed as the concatentation
	// of the base matrix and the supplementary matrix.
	/** The m display matrix. */
	private final Matrix mDisplayMatrix = new Matrix();

	// Temporary buffer used for getting the values out of a matrix.
	/** The m matrix values. */
	private final float[] mMatrixValues = new float[9];

	// The current bitmap being displayed.
	/** The m bitmap displayed. */
	final public RotateBitmap mBitmapDisplayed = new RotateBitmap(null);

	/** The m this height. */
	int mThisWidth = -1, mThisHeight = -1;

	/** The m max zoom. */
	float mMaxZoom;

	/** 高亮状态. */
	public static final int STATE_HIGHLIGHT = 0x0;

	/** 涂鸦状态. */
	public static final int STATE_DOODLE = STATE_HIGHLIGHT + 1;

	/** 没有任何操作. */
	public static final int STATE_NONE = STATE_HIGHLIGHT + 2;
	
	/** The m state. */
	protected int mState = STATE_HIGHLIGHT;

	// ImageViewTouchBase will pass a Bitmap to the Recycler if it has finished
	// its use of that Bitmap.
	/**
	 * The Interface Recycler.
	 */
	public interface Recycler {
		
		/**
		 * Recycle.
		 *
		 * @param b the b
		 */
		public void recycle(Bitmap b);
	}

	/**
	 * Sets the recycler.
	 *
	 * @param r the new recycler
	 */
	public void setRecycler(Recycler r) {
		mRecycler = r;
	}

	/** The m recycler. */
	private Recycler mRecycler;

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param changed the changed
	 * @param left the left
	 * @param top the top
	 * @param right the right
	 * @param bottom the bottom
	 * @see android.view.View#onLayout(boolean, int, int, int, int)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:50
	 */
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		mThisWidth = right - left;
		mThisHeight = bottom - top;
		Runnable r = mOnLayoutRunnable;
		if (r != null) {
			mOnLayoutRunnable = null;
			r.run();
		}
		if (mBitmapDisplayed.getBitmap() != null) {
			getProperBaseMatrix(mBitmapDisplayed, mBaseMatrix);
			setImageMatrix(getImageViewMatrix());
		}
	}

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param keyCode the key code
	 * @param event the event
	 * @return true, if successful
	 * @see android.view.View#onKeyDown(int, android.view.KeyEvent)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:50
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && getScale() > 1.0f) {
			// If we're zoomed in, pressing Back jumps out to show the entire
			// image, otherwise Back returns the user to the gallery.
			zoomTo(1.0f);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** The m handler. */
	protected Handler mHandler = new Handler();

	/** The m last x touch pos. */
	protected int mLastXTouchPos;
	
	/** The m last y touch pos. */
	protected int mLastYTouchPos;

	/**
	 * 描述：TODO.
	 *
	 * @version v1.0
	 * @param bitmap the new image bitmap
	 * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
	 * @author: amsoft.cn
	 * @date：2013-6-17 上午9:04:50
	 */
	@Override
	public void setImageBitmap(Bitmap bitmap) {
		setImageBitmap(bitmap, 0);
	}

	/**
	 * Sets the image bitmap.
	 *
	 * @param bitmap the bitmap
	 * @param rotation the rotation
	 */
	private void setImageBitmap(Bitmap bitmap, int rotation) {
		super.setImageBitmap(bitmap);
		Drawable d = getDrawable();
		if (d != null) {
			d.setDither(true);
		}

		Bitmap old = mBitmapDisplayed.getBitmap();
		mBitmapDisplayed.setBitmap(bitmap);
		mBitmapDisplayed.setRotation(rotation);

		if (old != null && old != bitmap && mRecycler != null) {
			mRecycler.recycle(old);
		}
	}

	/**
	 * Clear.
	 */
	public void clear() {
		setImageBitmapResetBase(null, true);
	}

	/** The m on layout runnable. */
	private Runnable mOnLayoutRunnable = null;

	// This function changes bitmap, reset base matrix according to the size
	// of the bitmap, and optionally reset the supplementary matrix.
	/**
	 * Sets the image bitmap reset base.
	 *
	 * @param bitmap the bitmap
	 * @param resetSupp the reset supp
	 */
	public void setImageBitmapResetBase(final Bitmap bitmap,
			final boolean resetSupp) {
		setImageRotateBitmapResetBase(new RotateBitmap(bitmap), resetSupp);
	}

	/**
	 * Sets the image rotate bitmap reset base.
	 *
	 * @param bitmap the bitmap
	 * @param resetSupp the reset supp
	 */
	public void setImageRotateBitmapResetBase(final RotateBitmap bitmap,
			final boolean resetSupp) {
		final int viewWidth = getWidth();

		if (viewWidth <= 0) {
			mOnLayoutRunnable = new Runnable() {
				public void run() {
					setImageRotateBitmapResetBase(bitmap, resetSupp);
				}
			};
			return;
		}

		if (bitmap.getBitmap() != null) {
			getProperBaseMatrix(bitmap, mBaseMatrix);
			setImageBitmap(bitmap.getBitmap(), bitmap.getRotation());
		} else {
			mBaseMatrix.reset();
			setImageBitmap(null);
		}

		if (resetSupp) {
			mSuppMatrix.reset();
		}
		setImageMatrix(getImageViewMatrix());
		mMaxZoom = maxZoom();
	}

	// Center as much as possible in one or both axis. Centering is
	// defined as follows: if the image is scaled down below the
	// view's dimensions then center it (literally). If the image
	// is scaled larger than the view and is translated out of view
	// then translate it back into view (i.e. eliminate black bars).
	/**
	 * Center.
	 *
	 * @param horizontal the horizontal
	 * @param vertical the vertical
	 */
	public void center(boolean horizontal, boolean vertical) {
		if (mBitmapDisplayed.getBitmap() == null) {
			return;
		}

		Matrix m = getImageViewMatrix();

		RectF rect = new RectF(0, 0, mBitmapDisplayed.getBitmap().getWidth(),
				mBitmapDisplayed.getBitmap().getHeight());

		m.mapRect(rect);

		float height = rect.height();
		float width = rect.width();

		float deltaX = 0, deltaY = 0;

		if (vertical) {
			int viewHeight = getHeight();
			if (height < viewHeight) {
				deltaY = (viewHeight - height) / 2 - rect.top;
			} else if (rect.top > 0) {
				deltaY = -rect.top;
			} else if (rect.bottom < viewHeight) {
				deltaY = getHeight() - rect.bottom;
			}
		}

		if (horizontal) {
			int viewWidth = getWidth();
			if (width < viewWidth) {
				deltaX = (viewWidth - width) / 2 - rect.left;
			} else if (rect.left > 0) {
				deltaX = -rect.left;
			} else if (rect.right < viewWidth) {
				deltaX = viewWidth - rect.right;
			}
		}

		postTranslate(deltaX, deltaY);
		setImageMatrix(getImageViewMatrix());
	}

	/**
	 * Instantiates a new crop view base.
	 *
	 * @param context the context
	 */
	public CropViewBase(Context context) {
		super(context);
		init();
	}

	/**
	 * Instantiates a new crop view base.
	 *
	 * @param context the context
	 * @param attrs the attrs
	 */
	public CropViewBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Inits the.
	 */
	private void init() {
		setScaleType(ImageView.ScaleType.MATRIX);
	}

	/**
	 * Gets the value.
	 *
	 * @param matrix the matrix
	 * @param whichValue the which value
	 * @return the value
	 */
	protected float getValue(Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		return mMatrixValues[whichValue];
	}

	// Get the scale factor out of the matrix.
	/**
	 * Gets the scale.
	 *
	 * @param matrix the matrix
	 * @return the scale
	 */
	protected float getScale(Matrix matrix) {
		return getValue(matrix, Matrix.MSCALE_X);
	}

	/**
	 * Gets the scale.
	 *
	 * @return the scale
	 */
	public float getScale() {
		return getScale(mSuppMatrix);
	}

	// Setup the base matrix so that the image is centered and scaled properly.
	/**
	 * Gets the proper base matrix.
	 *
	 * @param bitmap the bitmap
	 * @param matrix the matrix
	 * @return the proper base matrix
	 */
	private void getProperBaseMatrix(RotateBitmap bitmap, Matrix matrix) {
		float viewWidth = getWidth();
		float viewHeight = getHeight();

		float w = bitmap.getWidth();
		float h = bitmap.getHeight();
		matrix.reset();

		// We limit up-scaling to 2x otherwise the result may look bad if it's
		// a small icon.
		float widthScale = Math.min(viewWidth / w, 2.0f);
		float heightScale = Math.min(viewHeight / h, 2.0f);
		float scale = Math.min(widthScale, heightScale);

		matrix.postConcat(bitmap.getRotateMatrix());
		matrix.postScale(scale, scale);

		matrix.postTranslate((viewWidth - w * scale) / 2F, (viewHeight - h
				* scale) / 2F);
	}

	// Combine the base matrix and the supp matrix to make the final matrix.
	/**
	 * Gets the image view matrix.
	 *
	 * @return the image view matrix
	 */
	protected Matrix getImageViewMatrix() {
		// The final matrix is computed as the concatentation of the base matrix
		// and the supplementary matrix.
		mDisplayMatrix.set(mBaseMatrix);
		mDisplayMatrix.postConcat(mSuppMatrix);
		return mDisplayMatrix;
	}

	/** The Constant SCALE_RATE. */
	static final float SCALE_RATE = 1.25F;

	// Sets the maximum zoom, which is a scale relative to the base matrix. It
	// is calculated to show the image at 400% zoom regardless of screen or
	// image orientation. If in the future we decode the full 3 megapixel image,
	// rather than the current 1024x768, this should be changed down to 200%.
	/**
	 * Max zoom.
	 *
	 * @return the float
	 */
	protected float maxZoom() {
		if (mBitmapDisplayed.getBitmap() == null) {
			return 1F;
		}

		float fw = (float) mBitmapDisplayed.getWidth() / (float) mThisWidth;
		float fh = (float) mBitmapDisplayed.getHeight() / (float) mThisHeight;
		float max = Math.max(fw, fh) * 4;
		max = (max < 1.0f) ? 1.0f : max;
		return max;
	}

	/**
	 * Zoom to.
	 *
	 * @param scale the scale
	 * @param centerX the center x
	 * @param centerY the center y
	 */
	protected void zoomTo(float scale, float centerX, float centerY) {
		if (scale > mMaxZoom) {
			scale = mMaxZoom;
		}

		float oldScale = getScale();
		float deltaScale = scale / oldScale;

		mSuppMatrix.postScale(deltaScale, deltaScale, centerX, centerY);
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	/**
	 * Zoom to.
	 *
	 * @param scale the scale
	 * @param centerX the center x
	 * @param centerY the center y
	 * @param durationMs the duration ms
	 */
	protected void zoomTo(final float scale, final float centerX,
			final float centerY, final float durationMs) {
		final float incrementPerMs = (scale - getScale()) / durationMs;
		final float oldScale = getScale();
		final long startTime = System.currentTimeMillis();

		mHandler.post(new Runnable() {
			public void run() {
				long now = System.currentTimeMillis();
				float currentMs = Math.min(durationMs, now - startTime);
				float target = oldScale + (incrementPerMs * currentMs);
				zoomTo(target, centerX, centerY);

				if (currentMs < durationMs) {
					mHandler.post(this);
				}
			}
		});
	}

	/**
	 * Zoom to.
	 *
	 * @param scale the scale
	 */
	protected void zoomTo(float scale) {
		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		zoomTo(scale, cx, cy);
	}

	/**
	 * Zoom in.
	 */
	protected void zoomIn() {
		zoomIn(SCALE_RATE);
	}

	/**
	 * Zoom out.
	 */
	protected void zoomOut() {
		zoomOut(SCALE_RATE);
	}

	/**
	 * Zoom in.
	 *
	 * @param rate the rate
	 */
	protected void zoomIn(float rate) {
		if (getScale() >= mMaxZoom) {
			return; // Don't let the user zoom into the molecular level.
		}
		if (mBitmapDisplayed.getBitmap() == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		mSuppMatrix.postScale(rate, rate, cx, cy);
		setImageMatrix(getImageViewMatrix());
	}

	/**
	 * Zoom out.
	 *
	 * @param rate the rate
	 */
	protected void zoomOut(float rate) {
		if (mBitmapDisplayed.getBitmap() == null) {
			return;
		}

		float cx = getWidth() / 2F;
		float cy = getHeight() / 2F;

		// Zoom out to at most 1x.
		Matrix tmp = new Matrix(mSuppMatrix);
		tmp.postScale(1F / rate, 1F / rate, cx, cy);

		if (getScale(tmp) < 1F) {
			mSuppMatrix.setScale(1F, 1F, cx, cy);
		} else {
			mSuppMatrix.postScale(1F / rate, 1F / rate, cx, cy);
		}
		setImageMatrix(getImageViewMatrix());
		center(true, true);
	}

	/**
	 * Post translate.
	 *
	 * @param dx the dx
	 * @param dy the dy
	 */
	protected void postTranslate(float dx, float dy) {
		mSuppMatrix.postTranslate(dx, dy);
	}

	/**
	 * Pan by.
	 *
	 * @param dx the dx
	 * @param dy the dy
	 */
	protected void panBy(float dx, float dy) {
		postTranslate(dx, dy);
		setImageMatrix(getImageViewMatrix());
	}

}
