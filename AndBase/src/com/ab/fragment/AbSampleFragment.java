package com.ab.fragment;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * © 2012 amsoft.cn 
 * 名称：AbSampleFragment.java 
 * 描述：全屏的Fragment
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-07-30 下午16:00:52
 */
public class AbSampleFragment extends DialogFragment {

	private View mContentView;

	public AbSampleFragment() {
		super();
	}
	
	/**
	 * Create a new instance of AbSampleFragment.
	 */
	public static AbSampleFragment newInstance() {
		AbSampleFragment f = new AbSampleFragment();
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return mContentView;
	}

	public View getContentView() {
		return mContentView;
	}

	public void setContentView(View mContentView) {
		this.mContentView = mContentView;
	}

}
