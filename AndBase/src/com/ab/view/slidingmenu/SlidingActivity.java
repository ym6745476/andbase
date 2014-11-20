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
package com.ab.view.slidingmenu;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

// TODO: Auto-generated Javadoc
/**
 * The Class SlidingActivity.
 */
public class SlidingActivity extends Activity implements SlidingActivityBase {

	/** The m helper. */
	private SlidingActivityHelper mHelper;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SlidingActivityHelper(this);
		mHelper.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onPostCreate(android.os.Bundle)
	 */
	@Override
	public void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#findViewById(int)
	 */
	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v != null)
			return v;
		return mHelper.findViewById(id);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mHelper.onSaveInstanceState(outState);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(int)
	 */
	@Override
	public void setContentView(int id) {
		setContentView(getLayoutInflater().inflate(id, null));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View)
	 */
	@Override
	public void setContentView(View v) {
		setContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#setContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	@Override
	public void setContentView(View v, LayoutParams params) {
		super.setContentView(v, params);
		mHelper.registerAboveContentView(v, params);
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(int)
	 */
	public void setBehindContentView(int id) {
		setBehindContentView(getLayoutInflater().inflate(id, null));
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View)
	 */
	public void setBehindContentView(View v) {
		setBehindContentView(v, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setBehindContentView(android.view.View, android.view.ViewGroup.LayoutParams)
	 */
	public void setBehindContentView(View v, LayoutParams params) {
		mHelper.setBehindContentView(v, params);
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#getSlidingMenu()
	 */
	public SlidingMenu getSlidingMenu() {
		return mHelper.getSlidingMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#toggle()
	 */
	public void toggle() {
		mHelper.toggle();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showAbove()
	 */
	public void showContent() {
		mHelper.showContent();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showBehind()
	 */
	public void showMenu() {
		mHelper.showMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#showSecondaryMenu()
	 */
	public void showSecondaryMenu() {
		mHelper.showSecondaryMenu();
	}

	/* (non-Javadoc)
	 * @see com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivityBase#setSlidingActionBarEnabled(boolean)
	 */
	public void setSlidingActionBarEnabled(boolean b) {
		mHelper.setSlidingActionBarEnabled(b);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onKeyUp(int, android.view.KeyEvent)
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		boolean b = mHelper.onKeyUp(keyCode, event);
		if (b) return b;
		return super.onKeyUp(keyCode, event);
	}

}
