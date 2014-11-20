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
package com.ab.view.cropimage;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.CountDownLatch;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.os.Environment;
import android.os.Handler;

import com.ab.global.AbConstant;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbFileUtil;


// TODO: Auto-generated Javadoc
/**
 * 裁剪处理.
 */
public class CropImage{
	
    /** The file local. */
    public File FILE_LOCAL = null;
    // Whether we are wait the user to pick a face.
	/** The m waiting to pick. */
    public boolean mWaitingToPick; 
	// Whether the "save" button is already clicked.
    /** The m saving. */
	public boolean mSaving; 
    
    /** The m crop. */
    public HighlightView mCrop;
    
	/** The m context. */
	private Context mContext;
	
	/** The m handler. */
	private Handler mHandler;
	
	/** The m image view. */
	private CropImageView mImageView;
	
	/** The m bitmap. */
	private Bitmap mBitmap;
	
	/**
	 * Instantiates a new crop image.
	 *
	 * @param context the context
	 * @param imageView the image view
	 * @param handler the handler
	 */
	public CropImage(Context context, CropImageView imageView,Handler handler){
		mContext = context;
		mImageView = imageView;
		mImageView.setCropImage(this);
		mHandler = handler;
		//初始化图片保存路径
		FILE_LOCAL = new File(AbFileUtil.getImageDownloadDir(context));
		if(!FILE_LOCAL.exists()){
			FILE_LOCAL.mkdirs();
		}
	}
	
	/**
	 * 图片裁剪.
	 *
	 * @param bm the bm
	 */
	public void crop(Bitmap bm){
		mBitmap = bm;
		startFaceDetection();
	}
	
	/**
	 * Start rotate.
	 *
	 * @param d the d
	 */
	public void startRotate(float d) {
        if (((Activity)mContext).isFinishing()) {
            return;
        }
        final float degrees = d;
        final CountDownLatch latch = new CountDownLatch(1);
        Runnable mRunnable =  new Runnable() {
            public void run() {
                mHandler.post(new Runnable() {
                    public void run() {
                        try{
	                        Matrix m = new Matrix();
	                		m.setRotate(degrees);
	                		Bitmap tb = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(),m,false);
	                		mBitmap = tb;
	                		mImageView.resetView(tb);
	                		if (mImageView.mHighlightViews.size() > 0) {
	                            mCrop = mImageView.mHighlightViews.get(0);
	                            mCrop.setFocus(true);
	                        }
                        }catch (Exception e) {
						}
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (Exception e) {
                	throw new RuntimeException(e);
                }
                //mRunFaceDetection.run();
            }
        };
        new Thread(new BackgroundJob("", mRunnable, mHandler)).start();
    }
	
	/**
	 * Start face detection.
	 */
	private void startFaceDetection() {
        if (((Activity)mContext).isFinishing()) {
            return;
        }
        Runnable mRunnable = new Runnable() {
            public void run() {
                final CountDownLatch latch = new CountDownLatch(1);
                final Bitmap b = mBitmap;
                mHandler.post(new Runnable() {
                    public void run() {
                        if (b != mBitmap && b != null) {
                            mImageView.setImageBitmapResetBase(b, true);
                            mBitmap.recycle();
                            mBitmap = b;
                        }
                        if (mImageView.getScale() == 1.0f) {
                            mImageView.center(true, true);
                        }
                        latch.countDown();
                    }
                });
                try {
                    latch.await();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                mRunFaceDetection.run();
            }
        };
        new Thread(new BackgroundJob("", mRunnable, mHandler)).start();
    }

	/**
	 * 裁剪并保存.
	 *
	 * @return the bitmap
	 */
	public Bitmap cropAndSave(){
		final Bitmap bmp = onSaveClicked(mBitmap);
		mImageView.mHighlightViews.clear();
		return bmp;
	}
	
	/**
	 * 裁剪并保存.
	 *
	 * @param bm the bm
	 * @return the bitmap
	 */
	public Bitmap cropAndSave(Bitmap bm){
		final Bitmap bmp = onSaveClicked(bm);
		mImageView.mHighlightViews.clear();
		return bmp;
	}
	
	/**
	 * 取消裁剪.
	 */
	public void cropCancel(){
		mImageView.mHighlightViews.clear();
		mImageView.invalidate();
	}
	
    /**
     * On save clicked.
     *
     * @param bm the bm
     * @return the bitmap
     */
    private Bitmap onSaveClicked(Bitmap bm) {
        if (mSaving)
            return bm;

        if (mCrop == null) {
            return bm;
        }

        mSaving = true;

        Rect r = mCrop.getCropRect();
        int width = 360;   
        int height = 360;  
        Bitmap croppedImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(croppedImage);
        Rect dstRect = new Rect(0, 0, width, height);
        canvas.drawBitmap(bm, r, dstRect, null);
        return croppedImage;
    }

    /**
     * Save to local.
     *
     * @param bitmap the bitmap
     * @return the string
     */
    public String saveToLocal(Bitmap bitmap){
    	//需要裁剪后保存为新图片
        String mFileName = System.currentTimeMillis() + ".jpg";
    	String path = FILE_LOCAL+File.separator+mFileName;
    	try{
			FileOutputStream fos = new FileOutputStream(path);
			bitmap.compress(CompressFormat.JPEG, 75, fos);
			fos.flush();
			fos.close();
		} catch (Exception e){
			e.printStackTrace();
			return null;
		}
		return path;
    }
    
    /** The m run face detection. */
    Runnable mRunFaceDetection = new Runnable() {
        float mScale = 1F;
        Matrix mImageMatrix;
        FaceDetector.Face[] mFaces = new FaceDetector.Face[3];
        int mNumFaces;

        // For each face, we create a HightlightView for it.
        private void handleFace(FaceDetector.Face f) {
            PointF midPoint = new PointF();

            int r = ((int) (f.eyesDistance() * mScale)) * 2;
            f.getMidPoint(midPoint);
            midPoint.x *= mScale;
            midPoint.y *= mScale;

            int midX = (int) midPoint.x;
            int midY = (int) midPoint.y;

            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            RectF faceRect = new RectF(midX, midY, midX, midY);
            faceRect.inset(-r, -r);
            if (faceRect.left < 0) {
                faceRect.inset(-faceRect.left, -faceRect.left);
            }

            if (faceRect.top < 0) {
                faceRect.inset(-faceRect.top, -faceRect.top);
            }

            if (faceRect.right > imageRect.right) {
                faceRect.inset(faceRect.right - imageRect.right, faceRect.right - imageRect.right);
            }

            if (faceRect.bottom > imageRect.bottom) {
                faceRect.inset(faceRect.bottom - imageRect.bottom, faceRect.bottom - imageRect.bottom);
            }

            hv.setup(mImageMatrix, imageRect, faceRect, false, true);

            mImageView.add(hv);
        }

        // Create a default HightlightView if we found no face in the picture.
        private void makeDefault() {
            HighlightView hv = new HighlightView(mImageView);

            int width = mBitmap.getWidth();
            int height = mBitmap.getHeight();

            Rect imageRect = new Rect(0, 0, width, height);

            // CR: sentences!
            // make the default size about 4/5 of the width or height
            int cropWidth = Math.min(width, height) * 4 / 5;
            int cropHeight = cropWidth;

            int x = (width - cropWidth) / 2;
            int y = (height - cropHeight) / 2;

            RectF cropRect = new RectF(x, y, x + cropWidth, y + cropHeight);
            hv.setup(mImageMatrix, imageRect, cropRect, false, true);
            mImageView.add(hv);
        }

        // Scale the image down for faster face detection.
        private Bitmap prepareBitmap() {
            if (mBitmap == null) {
                return null;
            }

            // 256 pixels wide is enough.
            // CR: F => f (or change all f to F).
            if (mBitmap.getWidth() > 256) {
                mScale = 256.0F / mBitmap.getWidth(); 
            }
            Matrix matrix = new Matrix();
            matrix.setScale(mScale, mScale);
            Bitmap faceBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
            return faceBitmap;
        }

        public void run() {
            mImageMatrix = mImageView.getImageMatrix();
            Bitmap faceBitmap = prepareBitmap();

            mScale = 1.0F / mScale;
            if (faceBitmap != null) {
                FaceDetector detector = new FaceDetector(faceBitmap.getWidth(), faceBitmap.getHeight(), mFaces.length);
                mNumFaces = detector.findFaces(faceBitmap, mFaces);
            }

            if (faceBitmap != null && faceBitmap != mBitmap) {
                faceBitmap.recycle();
            }

            mHandler.post(new Runnable() {
                public void run() {
                    mWaitingToPick = mNumFaces > 1;
                    makeDefault();
                    mImageView.invalidate();
                    if (mImageView.mHighlightViews.size() > 0) {
                        mCrop = mImageView.mHighlightViews.get(0);
                        mCrop.setFocus(true);
                    }

                    if (mNumFaces > 1) {
                    }
                }
            });
        }
    };
	
	/**
	 * The Class BackgroundJob.
	 */
	public class BackgroundJob implements Runnable{
    	
	    /** The message. */
	    private String message;
    	
	    /** The m job. */
	    private Runnable mJob;
    	
	    /** The m handler. */
	    private Handler mHandler;

    	/**
	     * Instantiates a new background job.
	     *
	     * @param m the m
	     * @param job the job
	     * @param handler the handler
	     */
	    public BackgroundJob(String m, Runnable job, Handler handler){
    		message = m;
    		mJob = job;
    		mHandler = handler;
    	}
    	
	    public void run(){
    		final CountDownLatch latch = new CountDownLatch(1);
    		mHandler.post(new Runnable() {
                public void run() {
                    latch.countDown();
                }
            });
    		try {
                 latch.await();
            } catch (Exception e) {
                 throw new RuntimeException(e);
            }
    		try {
    			mJob.run();
    		}catch (Exception e) {
    			e.printStackTrace();
            }
    	}
    }
}
