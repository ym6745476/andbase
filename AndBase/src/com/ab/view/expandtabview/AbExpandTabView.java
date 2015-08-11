package com.ab.view.expandtabview;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.ab.util.AbAppUtil;
import com.ab.util.AbViewUtil;

/**
 * 菜单控件头部，封装了下拉动画，动态生成头部按钮个数
 * @author Administrator
 *
 */

public class AbExpandTabView extends LinearLayout implements OnDismissListener {

	private ToggleButton selectedButton;
	private ArrayList<RelativeLayout> mViewArray = new ArrayList<RelativeLayout>();
	private ArrayList<ToggleButton> mToggleButton = new ArrayList<ToggleButton>();
	private Context mContext;
	private final int SMALL = 0;
	private int displayWidth;
	private int displayHeight;
	private PopupWindow popupWindow;
	private int selectPosition;

	public AbExpandTabView(Context context) {
		super(context);
		init(context);
	}

	public AbExpandTabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	/**
	 * 根据选择的位置设置tabitem显示的值
	 */
	public void setTitle(String valueText, int position) {
		if (position < mToggleButton.size()) {
			mToggleButton.get(position).setText(valueText);
		}
	}

	/**
	 * 根据选择的位置获取tabitem显示的值
	 */
	public String getTitle(int position) {
		if (position < mToggleButton.size() && mToggleButton.get(position).getText() != null) {
			return mToggleButton.get(position).getText().toString();
		}
		return "";
	}

	/**
	 * 设置tabitem的个数和初始值
	 */
	public void setValue(ArrayList<View> viewArray,int tabSelectorResId,int tabLineResId) {
		if (mContext == null) {
			return;
		}

		for (int i = 0; i < viewArray.size(); i++) {
			final RelativeLayout r = new RelativeLayout(mContext);
			int maxHeight = (int) (displayHeight * 0.4);
			RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, maxHeight);
			rl.leftMargin = 0;
			rl.rightMargin = 0;
			r.addView(viewArray.get(i), rl);
			mViewArray.add(r);
			r.setTag(SMALL);
			
			ToggleButton tButton = new ToggleButton(mContext);
			tButton.setGravity(Gravity.CENTER);
			tButton.setTextOn(null);
			tButton.setTextOff(null);
			tButton.setBackgroundResource(tabSelectorResId);
			tButton.setTextColor(Color.parseColor("#FF4C4C4C"));
			tButton.setSingleLine(true);
			tButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,AbViewUtil.scaleTextValue(tButton.getContext(), 25));
			
			addView(tButton,new LayoutParams(0,LayoutParams.WRAP_CONTENT,1));
			
			View line = new TextView(mContext);
			line.setBackgroundResource(tabLineResId);
			
			if (i < viewArray.size() - 1) {
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(2, LinearLayout.LayoutParams.MATCH_PARENT);
				addView(line, lp);
			}
			mToggleButton.add(tButton);
			tButton.setTag(i);

			r.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					onPressBack();
				}
			});

			r.setBackgroundColor(Color.parseColor("#b0000000"));
			tButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					ToggleButton tButton = (ToggleButton) view;

					if (selectedButton != null && selectedButton != tButton) {
						selectedButton.setChecked(false);
					}
					selectedButton = tButton;
					selectPosition = (Integer) selectedButton.getTag();
					startAnimation();
					if (mOnButtonClickListener != null && tButton.isChecked()) {
						mOnButtonClickListener.onClick(selectPosition);
					}
				}
			});
		}
	}

	private void startAnimation() {

		if (popupWindow == null) {
			popupWindow = new PopupWindow(mViewArray.get(selectPosition), displayWidth, displayHeight);
			//popupWindow.setAnimationStyle(R.style.popupWindowAnimation);
			popupWindow.setFocusable(false);
			popupWindow.setOutsideTouchable(true);
		}

		if (selectedButton.isChecked()) {
			if (!popupWindow.isShowing()) {
				showPopup(selectPosition);
			} else {
				popupWindow.setOnDismissListener(this);
				popupWindow.dismiss();
				hideView();
			}
		} else {
			if (popupWindow.isShowing()) {
				popupWindow.dismiss();
				hideView();
			}
		}
	}

	private void showPopup(int position) {
		if (popupWindow.getContentView() != mViewArray.get(position)) {
			popupWindow.setContentView(mViewArray.get(position));
		}
		popupWindow.showAsDropDown(this, 0, 0);
	}

	/**
	 * 如果菜单成展开状态，则让菜单收回去
	 */
	public boolean onPressBack() {
		if (popupWindow != null && popupWindow.isShowing()) {
			popupWindow.dismiss();
			hideView();
			if (selectedButton != null) {
				selectedButton.setChecked(false);
			}
			return true;
		} else {
			return false;
		}

	}

	private void hideView() {
		View tView = mViewArray.get(selectPosition).getChildAt(0);
	}

	private void init(Context context) {
		mContext = context;
		DisplayMetrics display = AbAppUtil.getDisplayMetrics(context);
		displayWidth = display.widthPixels;
		displayHeight = display.heightPixels;
		setOrientation(LinearLayout.HORIZONTAL);
	}

	@Override
	public void onDismiss() {
		showPopup(selectPosition);
		popupWindow.setOnDismissListener(null);
	}

	private OnButtonClickListener mOnButtonClickListener;

	/**
	 * 设置tabitem的点击监听事件
	 */
	public void setOnButtonClickListener(OnButtonClickListener l) {
		mOnButtonClickListener = l;
	}

	/**
	 * 自定义tabitem点击回调接口
	 */
	public interface OnButtonClickListener {
		public void onClick(int selectPosition);
	}

	/**
	 * 
	 * 设置Tab上文字的大小.
	 * @param size
	 */
	public void setTabTextSize(float size){
		for(ToggleButton tab:mToggleButton){
			AbViewUtil.setTextSize(tab, size);
		}
	}
	
	/**
	 * 
	 * 设置Tab上文字的颜色.
	 * @param color
	 */
	public void setTabTextColor(int color){
		for(ToggleButton tab:mToggleButton){
			tab.setTextColor(color);
		}
	}
}

