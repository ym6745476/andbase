package com.andbase.im.util;

import java.io.File;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import com.ab.util.AbFileUtil;


public class IMRecorder {

	private static final String TAG = IMRecorder.class.getSimpleName();
	
	private Context context;
	
	/**SD卡未找到的错误码*/
	private static final int SD_NOT_FOUND = 404;
	private static final String SD_NOT_FOUND_MSG = "SD卡未找到";
	
	private static final int CREATE_FILE_ERROR = 500;
    private static final String CREATE_FILE_ERROR_MSG = "创建文件失败";
	
	/** 默认音频下载文件地址. */
    private static  String downPathAudioFileDir = null;

	private IMRecordListener recordListener;
	
	private MediaRecorder mediaRecord = null;
	
	/** 准备录音 */
	private boolean isPreRecording = false;

	/** 正在录音 */
	private boolean isRecording = false;

	private boolean isCancel = false;
	
	private File audioFile;
	
	private String fileName = null;
	
	private long startRecordTime = 0;
	
	public IMRecorder(Context context, IMRecordListener recordListener) {

		this.recordListener = recordListener;
		this.context = context;
	}

	/**
	 * 开始录音
	 */
	public void startRecording() {
		
		//录音的文件类型
	    String AMR_FILE = ".amr";
	    
		if (isPreRecording || isRecording){
		    return;
		}
		
		if(fileName == null){
		    fileName = System.currentTimeMillis()+AMR_FILE;
		}
		
		if(!AbFileUtil.isCanUseSD()){
		    recordListener.onError(SD_NOT_FOUND, SD_NOT_FOUND_MSG);
		    return;
		}
		
		downPathAudioFileDir = AbFileUtil.getDownloadRootDir(context)+ File.separator+ "audio" + File.separator;
		
		audioFile = new File(downPathAudioFileDir+fileName);
		
		
		try{
            if(!audioFile.getParentFile().exists()){
                audioFile.getParentFile().mkdirs();
            }
            if(!audioFile.exists()){
                audioFile.createNewFile();
            }
        }catch (Exception e1){
            e1.printStackTrace();
            recordListener.onError(CREATE_FILE_ERROR, CREATE_FILE_ERROR_MSG);
        }
		
		isPreRecording = true;	
		
		//准备录音
		recordListener.onPreRecording();

		mediaRecord = new MediaRecorder();
		mediaRecord.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecord.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mediaRecord.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecord.setAudioSamplingRate(8000);
		mediaRecord.setAudioEncodingBitRate(24);
		mediaRecord.setOutputFile(audioFile.getPath());

		long currTime = 0;
		try {
		    //准备录音
		    Log.i(TAG, "准备录音");
			currTime = System.currentTimeMillis();
			mediaRecord.prepare();
			Log.i(TAG, "准备消耗时间：" + String.valueOf(System.currentTimeMillis() - currTime));
			
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		
		startRecordTime = System.currentTimeMillis();
		
		mediaRecord.start();
		
		//准备录音
        recordListener.onRecording();
		
		Log.i(TAG, "开始消耗时间：" + String.valueOf(System.currentTimeMillis() - currTime));
		Log.i(TAG, "开始录音");

		isRecording = true;
	}

	/**
	 * 停止录音
	 */
	public void stopRecording(final boolean isCancel) {
	        if(!isRecording){
	            return;
	        }
    	    if(isCancel){
    	        Log.i(TAG, "取消录音");
    	        this.isCancel = isCancel;
    	        recordListener.onCancel();
    	    }else{
    	        Log.i(TAG, "停止录音");
    	        mediaRecord.stop();
    	        recordListener.onFinish(audioFile, System.currentTimeMillis()-startRecordTime);
    	    }
    	    isPreRecording = false;
			isRecording = false;
			fileName = null;
	}

    public String getFileName(){
        return fileName;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }

}