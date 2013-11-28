/*
 * Copyright (C) 2013 www.418log.org
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
package com.ab.view.carousel;

import android.content.Context;
import android.view.animation.AnimationUtils;

// TODO: Auto-generated Javadoc
/**
 * This class encapsulates rotation. The duration of the rotation can be passed
 * in the constructor and specifies the maximum time that the rotation animation
 * should take. Past this time, the rotation is automatically moved to its final
 * stage and computeRotationOffset() will always return false to indicate that
 * scrolling is over.
 */
public class Rotator {

	/** The m mode. */
	private int mMode;

	/** The m start angle. */
	private float mStartAngle;

	/** The m curr angle. */
	private float mCurrAngle;

	/** The m start time. */
	private long mStartTime;

	/** The m duration. */
	private long mDuration;

	/** The m delta angle. */
	private float mDeltaAngle;

	/** The m finished. */
	private boolean mFinished;

	/** The m coeff velocity. */
	private float mCoeffVelocity = 0.05f;

	/** The m velocity. */
	private float mVelocity;

	/** The Constant DEFAULT_DURATION. */
	private static final int DEFAULT_DURATION = 250;

	/** The Constant SCROLL_MODE. */
	private static final int SCROLL_MODE = 0;

	/** The Constant FLING_MODE. */
	private static final int FLING_MODE = 1;

	/** The m deceleration. */
	private final float mDeceleration = 240.0f;

	/**
	 * Create a Scroller with the specified interpolator. If the interpolator is
	 * null, the default (viscous) interpolator will be used.
	 * 
	 * @param context
	 *            the context
	 */
	public Rotator(Context context) {
		mFinished = true;
	}

	/**
	 * 
	 * Returns whether the scroller has finished scrolling.
	 * 
	 * @return True if the scroller has finished scrolling, false otherwise.
	 */
	public final boolean isFinished() {
		return mFinished;
	}

	/**
	 * Force the finished field to a particular value.
	 * 
	 * @param finished
	 *            The new finished value.
	 */
	public final void forceFinished(boolean finished) {
		mFinished = finished;
	}

	/**
	 * Returns how long the scroll event will take, in milliseconds.
	 * 
	 * @return The duration of the scroll in milliseconds.
	 */
	public final long getDuration() {
		return mDuration;
	}

	/**
	 * Returns the current X offset in the scroll.
	 * 
	 * @return The new X offset as an absolute distance from the origin.
	 */
	public final float getCurrAngle() {
		return mCurrAngle;
	}

	/**
	 * Gets the curr velocity.
	 * 
	 * @return The original velocity less the deceleration. Result may be
	 *         negative.
	 * @hide Returns the current velocity.
	 */
	public float getCurrVelocity() {
		return mCoeffVelocity * mVelocity - mDeceleration * timePassed() /*
																		 * /
																		 * 2000.0f
																		 */;
	}

	/**
	 * Returns the start X offset in the scroll.
	 * 
	 * @return The start X offset as an absolute distance from the origin.
	 */
	public final float getStartAngle() {
		return mStartAngle;
	}

	/**
	 * Returns the time elapsed since the beginning of the scrolling.
	 * 
	 * @return The elapsed time in milliseconds.
	 */
	public int timePassed() {
		return (int) (AnimationUtils.currentAnimationTimeMillis() - mStartTime);
	}

	/**
	 * Extend the scroll animation. This allows a running animation to scroll
	 * further and longer, when used with {@link #setFinalX(int)} or
	 * {@link #setFinalY(int)}.
	 * 
	 * @param extend
	 *            Additional time to scroll in milliseconds.
	 * @see #setFinalX(int)
	 * @see #setFinalY(int)
	 */
	public void extendDuration(int extend) {
		int passed = timePassed();
		mDuration = passed + extend;
		mFinished = false;
	}

	/**
	 * Stops the animation. Contrary to {@link #forceFinished(boolean)},
	 * aborting the animating cause the scroller to move to the final x and y
	 * position
	 * 
	 * @see #forceFinished(boolean)
	 */
	public void abortAnimation() {
		mFinished = true;
	}

	/**
	 * Call this when you want to know the new location. If it returns true, the
	 * animation is not yet finished. loc will be altered to provide the new
	 * location.
	 * 
	 * @return true, if successful
	 */
	public boolean computeAngleOffset() {
		if (mFinished) {
			return false;
		}

		long systemClock = AnimationUtils.currentAnimationTimeMillis();
		long timePassed = systemClock - mStartTime;

		if (timePassed < mDuration) {
			switch (mMode) {
			case SCROLL_MODE:

				float sc = (float) timePassed / mDuration;
				mCurrAngle = mStartAngle + Math.round(mDeltaAngle * sc);
				break;

			case FLING_MODE:

				float timePassedSeconds = timePassed / 1000.0f;
				float distance;

				if (mVelocity < 0) {
					distance = mCoeffVelocity
							* mVelocity
							* timePassedSeconds
							- (mDeceleration * timePassedSeconds
									* timePassedSeconds / 2.0f);
				} else {
					distance = -mCoeffVelocity
							* mVelocity
							* timePassedSeconds
							- (mDeceleration * timePassedSeconds
									* timePassedSeconds / 2.0f);
				}

				mCurrAngle = mStartAngle - Math.signum(mVelocity)
						* Math.round(distance);

				break;
			}
			return true;
		} else {
			mFinished = true;
			return false;
		}
	}

	/**
	 * Start scrolling by providing a starting point and the distance to travel.
	 * 
	 * @param startAngle
	 *            the start angle
	 * @param dAngle
	 *            the d angle
	 * @param duration
	 *            Duration of the scroll in milliseconds.
	 */
	public void startRotate(float startAngle, float dAngle, int duration) {
		mMode = SCROLL_MODE;
		mFinished = false;
		mDuration = duration;
		mStartTime = AnimationUtils.currentAnimationTimeMillis();
		mStartAngle = startAngle;
		mDeltaAngle = dAngle;
	}

	/**
	 * Start scrolling by providing a starting point and the distance to travel.
	 * The scroll will use the default value of 250 milliseconds for the
	 * duration.
	 * 
	 * @param startAngle
	 *            the start angle
	 * @param dAngle
	 *            the d angle
	 */
	public void startRotate(float startAngle, float dAngle) {
		startRotate(startAngle, dAngle, DEFAULT_DURATION);
	}

	/**
	 * Start scrolling based on a fling gesture. The distance travelled will
	 * depend on the initial velocity of the fling.
	 * 
	 * @param velocityAngle
	 *            Initial velocity of the fling (X) measured in pixels per
	 *            second.
	 */
	public void fling(float velocityAngle) {

		mMode = FLING_MODE;
		mFinished = false;

		float velocity = velocityAngle;

		mVelocity = velocity;
		mDuration = (int) (1000.0f * Math.sqrt(2.0f * mCoeffVelocity
				* Math.abs(velocity) / mDeceleration));

		mStartTime = AnimationUtils.currentAnimationTimeMillis();

	}

}
