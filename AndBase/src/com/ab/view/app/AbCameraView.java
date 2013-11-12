package com.ab.view.app;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class AbCameraView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public AbCameraView(Context context) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceholder);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    	if(camera!=null){
            camera.startPreview();
    	}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	if(camera!=null){
    		camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
    	}
    }
    
    
}
