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

// TODO: Auto-generated Javadoc
/**
 * The Class AbLevelSeriesRenderer.
 */
public class AbLevelSeriesRenderer extends AbLevelDefaultRenderer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** 等级图形的宽. */
	private int width;
	
	/** 等级图形的高. */
	private int height;
	
	/** 各等级段的颜色. */
	private int [] color = null;
	
	/** 假如 每段的百分比  2  3  2  1  1  1. */
	private float [] part = null;
	
	/** 各等级段的值. */
	private float [] partValue = null;
	
	/** 当前值. */
	private String textValue = null;
	
	/** 当前值描述. */
	private String textDesc = null;
	
	/** 当前值的等级. */
	private int textlevelIndex = 0;
	
	/** 当前值文字大小. */
	private int textLevelSize = 30;
	
	/** 当前值文字与顶部的距离. */
	private int marginTop = 30;
	
	/** 指示三角形的宽度. */
	private int arrowWidth  = 20;
	
	/** 指示三角形的高度. */
	private int arrowHeight = 10;
	
	/** 等级条的高度. */
	private int levelHeight = 20;
	
	/** 指示三角形与其他间距. */
	private int arrowMarginTop = 10;
	
	/** 等级坐标文字大小. */
	private int partTextSize = 15;
	
	/** 等级说明文字大小. */
	private int textDescSize = 22;
	
	/** 等级说明文字外边框的宽. */
	private int textRectWidth = 120;
	
	/** 等级说明文字外边框的搞. */
	private int textRectHeight = 60;

	/**
	 * Instantiates a new ab level series renderer.
	 */
	public AbLevelSeriesRenderer() {
		super();
	}

	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Sets the width.
	 *
	 * @param width the new width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Gets the color.
	 *
	 * @return the color
	 */
	public int[] getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 *
	 * @param color the new color
	 */
	public void setColor(int[] color) {
		this.color = color;
	}

	/**
	 * Gets the part.
	 *
	 * @return the part
	 */
	public float[] getPart() {
		return part;
	}

	/**
	 * Sets the part.
	 *
	 * @param part the new part
	 */
	public void setPart(float[] part) {
		this.part = part;
	}

	/**
	 * Gets the part value.
	 *
	 * @return the part value
	 */
	public float[] getPartValue() {
		return partValue;
	}

	/**
	 * Sets the part value.
	 *
	 * @param partValue the new part value
	 */
	public void setPartValue(float[] partValue) {
		this.partValue = partValue;
	}

	/**
	 * Gets the text value.
	 *
	 * @return the text value
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Sets the text value.
	 *
	 * @param textValue the new text value
	 */
	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

	/**
	 * Gets the text desc.
	 *
	 * @return the text desc
	 */
	public String getTextDesc() {
		return textDesc;
	}

	/**
	 * Sets the text desc.
	 *
	 * @param textDesc the new text desc
	 */
	public void setTextDesc(String textDesc) {
		this.textDesc = textDesc;
	}

	/**
	 * Gets the textlevel index.
	 *
	 * @return the textlevel index
	 */
	public int getTextlevelIndex() {
		return textlevelIndex;
	}

	/**
	 * Sets the textlevel index.
	 *
	 * @param textlevelIndex the new textlevel index
	 */
	public void setTextlevelIndex(int textlevelIndex) {
		this.textlevelIndex = textlevelIndex;
	}

	/**
	 * Gets the text level size.
	 *
	 * @return the text level size
	 */
	public int getTextLevelSize() {
		return textLevelSize;
	}

	/**
	 * Sets the text level size.
	 *
	 * @param textLevelSize the new text level size
	 */
	public void setTextLevelSize(int textLevelSize) {
		this.textLevelSize = textLevelSize;
	}

	/**
	 * Gets the margin top.
	 *
	 * @return the margin top
	 */
	public int getMarginTop() {
		return marginTop;
	}

	/**
	 * Sets the margin top.
	 *
	 * @param marginTop the new margin top
	 */
	public void setMarginTop(int marginTop) {
		this.marginTop = marginTop;
	}

	/**
	 * Gets the arrow width.
	 *
	 * @return the arrow width
	 */
	public int getArrowWidth() {
		return arrowWidth;
	}

	/**
	 * Sets the arrow width.
	 *
	 * @param arrowWidth the new arrow width
	 */
	public void setArrowWidth(int arrowWidth) {
		this.arrowWidth = arrowWidth;
	}

	/**
	 * Gets the arrow height.
	 *
	 * @return the arrow height
	 */
	public int getArrowHeight() {
		return arrowHeight;
	}

	/**
	 * Sets the arrow height.
	 *
	 * @param arrowHeight the new arrow height
	 */
	public void setArrowHeight(int arrowHeight) {
		this.arrowHeight = arrowHeight;
	}

	/**
	 * Gets the level height.
	 *
	 * @return the level height
	 */
	public int getLevelHeight() {
		return levelHeight;
	}

	/**
	 * Sets the level height.
	 *
	 * @param levelHeight the new level height
	 */
	public void setLevelHeight(int levelHeight) {
		this.levelHeight = levelHeight;
	}

	/**
	 * Gets the arrow margin top.
	 *
	 * @return the arrow margin top
	 */
	public int getArrowMarginTop() {
		return arrowMarginTop;
	}

	/**
	 * Sets the arrow margin top.
	 *
	 * @param arrowMarginTop the new arrow margin top
	 */
	public void setArrowMarginTop(int arrowMarginTop) {
		this.arrowMarginTop = arrowMarginTop;
	}

	/**
	 * Gets the part text size.
	 *
	 * @return the part text size
	 */
	public int getPartTextSize() {
		return partTextSize;
	}

	/**
	 * Sets the part text size.
	 *
	 * @param partTextSize the new part text size
	 */
	public void setPartTextSize(int partTextSize) {
		this.partTextSize = partTextSize;
	}

	/**
	 * Gets the text desc size.
	 *
	 * @return the text desc size
	 */
	public int getTextDescSize() {
		return textDescSize;
	}

	/**
	 * Sets the text desc size.
	 *
	 * @param textDescSize the new text desc size
	 */
	public void setTextDescSize(int textDescSize) {
		this.textDescSize = textDescSize;
	}

	/**
	 * Gets the text rect width.
	 *
	 * @return the text rect width
	 */
	public int getTextRectWidth() {
		return textRectWidth;
	}

	/**
	 * Sets the text rect width.
	 *
	 * @param textRectWidth the new text rect width
	 */
	public void setTextRectWidth(int textRectWidth) {
		this.textRectWidth = textRectWidth;
	}

	/**
	 * Gets the text rect height.
	 *
	 * @return the text rect height
	 */
	public int getTextRectHeight() {
		return textRectHeight;
	}

	/**
	 * Sets the text rect height.
	 *
	 * @param textRectHeight the new text rect height
	 */
	public void setTextRectHeight(int textRectHeight) {
		this.textRectHeight = textRectHeight;
	}
	
	
  
}
