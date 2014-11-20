package com.andbase.im.util;

import java.io.File;

public interface IMRecordListener{
    
    public void onPreRecording();
    
    public void onRecording();

    public void onCancel();

     /**
      * 录音完成
      * @param file  文件
      * @param time  录制时间
      */
    public void onFinish(File file, long time);

    public void onError(int errorCode,String errorMessage);

}
