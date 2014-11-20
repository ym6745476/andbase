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
package com.ab.view.level;

import java.io.Serializable;

import android.graphics.Canvas;
import android.graphics.Paint;

// TODO: Auto-generated Javadoc
/**
 * The Class AbLevelAbstractChart.
 */
public abstract class AbLevelAbstractChart implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Draw.
	 *
	 * @param canvas the canvas
	 * @param x the x
	 * @param y the y
	 * @param measureWidth the measure width
	 * @param measureHeight the measure height
	 * @param screenWidth the screen width
	 * @param screenHeight the screen height
	 * @param paint the paint
	 */
	public abstract void draw(Canvas canvas, int x, int y, int measureWidth,int measureHeight,int screenWidth, int screenHeight, Paint paint);

}
