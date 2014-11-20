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
package com.ab.view.app;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn
 * 名称：AbCameraView.java 
 * 描述：摄像机view
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-11-7 下午2:35:05
 */
public class AbCameraView extends SurfaceView implements SurfaceHolder.Callback {

    /** The surface holder. */
    private SurfaceHolder surfaceHolder;
    
    /** The camera. */
    private Camera camera;

    /**
     * Instantiates a new ab camera view.
     *
     * @param context the context
     */
    public AbCameraView(Context context) {
        super(context);

        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    /* (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
            camera = Camera.open();
            camera.setPreviewDisplay(surfaceholder);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

    /* (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
    	if(camera!=null){
            camera.startPreview();
    	}
    }

    /* (non-Javadoc)
     * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
     */
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
