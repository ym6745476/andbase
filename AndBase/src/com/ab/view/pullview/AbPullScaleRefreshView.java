package com.ab.view.pullview;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;

public class AbPullScaleRefreshView extends ScrollView {

	/** 最外层子View */
	private View childView;

	/** x上一次保存的. */
	private int mLastMotionX;

	/** y上一次保存的. */
	private int mLastMotionY;
	
	/** 是否第一次加载 */
	private boolean isFirst = true;

	private ImageView imageView;
	
	/** 用于记录拖拉图片移动的坐标位置 */
	private int imageHeight;
	
	private Rect childViewRect = new Rect();
	
	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
		ViewGroup.LayoutParams lp = imageView.getLayoutParams();
		imageHeight = lp.height;
		
	}

	public AbPullScaleRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		if (getChildCount() > 0) {
			childView = getChildAt(0);
		}
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionX = x;
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,< 0是向上运动
			int deltaX = x - mLastMotionX;
			int deltaY = y - mLastMotionY;
			//解决点击与移动的冲突
			if(Math.abs(deltaX)<Math.abs(deltaY) && Math.abs(deltaY) > 10){
					return true;
			}
			break;
		}
		return false;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		int x = (int) event.getX();
		int y = (int) event.getY();
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_MOVE:
			
			int deltaY = y - mLastMotionY;
			
			ViewGroup.LayoutParams lp = imageView.getLayoutParams();
			if(lp.height + deltaY/3 > 0){
				lp.height = lp.height + deltaY/3;
				imageView.setLayoutParams(lp);
			}
			
			if(isFirst){
				childViewRect.set(childView.getLeft(), childView.getTop(), childView.getRight(), childView.getBottom());
				isFirst = false;
			}
			// 移动布局
			childView.layout(childView.getLeft(), childView.getTop() + deltaY / 3,
					childView.getRight(), childView.getBottom() + deltaY / 3);
			mLastMotionY = y;
			
			break;
		//UP和CANCEL执行相同的方法
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			ViewGroup.LayoutParams lp2 = imageView.getLayoutParams();
			lp2.height = imageHeight;
			imageView.setLayoutParams(lp2);
			
			// 移动布局
			childView.layout(childViewRect.left, childViewRect.top,
					childViewRect.right, childViewRect.bottom);
			mLastMotionY = 0;
			break;
			
		}
		return super.onTouchEvent(event);
	}

}
