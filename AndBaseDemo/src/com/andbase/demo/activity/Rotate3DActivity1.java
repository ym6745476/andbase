package com.andbase.demo.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;

import com.ab.activity.AbActivity;
import com.ab.view.app.AbRotate3dAnimation;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.Constant;
import com.andbase.global.MyApplication;


public class Rotate3DActivity1 extends AbActivity {

	private String TAG = "3DRotateActivity";
	private static final boolean D = Constant.DEBUG;
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	private ViewGroup mContainer = null;
	private RelativeLayout mRelativeLayout01,mRelativeLayout02;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setAbContentView(R.layout.rotate3d1);

		application = (MyApplication) abApplication;
		mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.rotate3d_name1);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		
		mContainer = (ViewGroup) findViewById(R.id.container);
		// Since we are caching large views, we want to keep their cache
        // between each animation
        mContainer.setPersistentDrawingCache(ViewGroup.PERSISTENT_ANIMATION_CACHE);
        
        mRelativeLayout01 = (RelativeLayout) findViewById(R.id.layout01);
        mRelativeLayout02 = (RelativeLayout) findViewById(R.id.layout02);
		
		
		mAbTitleBar.getLogoView().setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mRelativeLayout01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				applyRotation(0,0,90);
			}
		});
		
        mRelativeLayout02.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				applyRotation(-1, 180, 90);
			}
		});
		
		
		
		initTitleRightLayout();
	
	}

	
	public void finish() {
		super.finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	
	private void initTitleRightLayout(){
    	mAbTitleBar.clearRightView();
    }
	
	/**
     * Setup a new 3D rotation on the container view.
     *
     * @param position the item that was clicked to show a picture, or -1 to show the list
     * @param start the start angle at which the rotation must begin
     * @param end the end angle of the rotation
     */
    private void applyRotation(int position, float start, float end) {
        // Find the center of the container
        final float centerX = mContainer.getWidth() / 2.0f;
        final float centerY = mContainer.getHeight() / 2.0f;

        // Create a new 3D rotation with the supplied parameter
        // The animation listener is used to trigger the next animation
        final AbRotate3dAnimation rotation =
                new AbRotate3dAnimation(start, end, centerX, centerY, 310.0f, true);
        rotation.setDuration(500);
        rotation.setFillAfter(true);
        rotation.setInterpolator(new AccelerateInterpolator());
        rotation.setAnimationListener(new DisplayNextView(position));

        mContainer.startAnimation(rotation);
    }
    
    /**
     * This class listens for the end of the first half of the animation.
     * It then posts a new action that effectively swaps the views when the container
     * is rotated 90 degrees and thus invisible.
     */
    private final class DisplayNextView implements Animation.AnimationListener {
        private final int mPosition;

        private DisplayNextView(int position) {
            mPosition = position;
        }

        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
            mContainer.post(new SwapViews(mPosition));
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    /**
     * This class is responsible for swapping the views and start the second
     * half of the animation.
     */
    private final class SwapViews implements Runnable {
        private final int mPosition;

        public SwapViews(int position) {
            mPosition = position;
        }

        public void run() {
            final float centerX = mContainer.getWidth() / 2.0f;
            final float centerY = mContainer.getHeight() / 2.0f;
            AbRotate3dAnimation rotation;
            
            if (mPosition > -1) {
            	mRelativeLayout01.setVisibility(View.GONE);
            	mRelativeLayout02.setVisibility(View.VISIBLE);

                rotation = new AbRotate3dAnimation(90,180, centerX, centerY, 310.0f, false);
            } else {
            	mRelativeLayout02.setVisibility(View.GONE);
            	mRelativeLayout01.setVisibility(View.VISIBLE);
                rotation = new AbRotate3dAnimation(90, 0, centerX, centerY, 310.0f, false);
            }

            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new DecelerateInterpolator());

            mContainer.startAnimation(rotation);
        }
    }
}
