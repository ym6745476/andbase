package com.ab.view.carousel;



import android.content.Context;
import android.graphics.Matrix;
import android.util.Log;
import android.widget.ImageView;
/**
 * 
 * Copyright (c) 2012 All rights reserved
 * 名称：CarouselItemImage.java 
 * 描述：旋转木马的图片View
 * @author zhaoqp
 * @date：2013-8-23 下午2:01:42
 * @version v1.0
 */
public class CarouselItemImage extends ImageView 
	implements Comparable<CarouselItemImage> {
	
	private int index;
	private float currentAngle;
	private float itemX;
	private float itemY;
	private float itemZ;
	private boolean drawn;	

	// It's needed to find screen coordinates
	private Matrix mCIMatrix;
	
	public CarouselItemImage(Context context) {
		super(context);
	}	
	
	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}
	

	public void setCurrentAngle(float currentAngle) {
		
		if(index == 0 && currentAngle > 5){
			Log.d("", "");
		}
		
		this.currentAngle = currentAngle;
	}

	public float getCurrentAngle() {
		return currentAngle;
	}

	public int compareTo(CarouselItemImage another) {
		return (int)(another.itemZ - this.itemZ);
	}

	public void setItemX(float x) {
		this.itemX = x;
	}

	public float getItemX() {
		return itemX;
	}

	public void setItemY(float y) {
		this.itemY = y;
	}

	public float getItemY() {
		return itemY;
	}

	public void setItemZ(float z) {
		this.itemZ = z;
	}

	public float getItemZ() {
		return itemZ;
	}

	public void setDrawn(boolean drawn) {
		this.drawn = drawn;
	}

	public boolean isDrawn() {
		return drawn;
	}
	
	
	Matrix getCIMatrix() {
		return mCIMatrix;
	}

	void setCIMatrix(Matrix mMatrix) {
		this.mCIMatrix = mMatrix;
	}	
	
}
