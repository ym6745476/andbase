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
package com.ab.download;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.ab.util.AbLogUtil;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbDownloadThread.java 
 * 描述：下载线程类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-10-16 下午1:33:39
 */
public class AbDownloadThread extends Thread {
	
	/** The save file. */
	private File saveFile;
	
	/** The m down file. */
	private DownFile mDownFile = null;
	
	/** The finish. */
	private boolean finish = false;
	
	/** The flag. */
	private boolean flag = false;
	
	/** The downloader. */
	private AbFileDownloader downloader;
	
	/**
	 * Instantiates a new download thread.
	 *
	 * @param downloader the downloader
	 * @param downFile the down file
	 * @param saveFile the save file
	 */
	public AbDownloadThread(AbFileDownloader downloader, DownFile downFile, File saveFile) {
		this.saveFile = saveFile;
		this.downloader = downloader;
		this.mDownFile = downFile;
	}
	
	/**
	 * 描述：运行.
	 *
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		flag = true;
		InputStream inStream = null;
		RandomAccessFile threadfile = null;
		//未下载完成
		if(mDownFile.getDownLength() < mDownFile.getTotalLength()){
			try {
				//使用Get方式下载
				URL mUrl = new URL(mDownFile.getDownUrl());
				HttpURLConnection http = (HttpURLConnection) mUrl.openConnection();
				http.setConnectTimeout(5 * 1000);
				http.setRequestMethod("GET");
				http.setRequestProperty("Accept", "image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*");
				http.setRequestProperty("Accept-Language", "zh-CN");
				http.setRequestProperty("Referer", mDownFile.getDownUrl()); 
				http.setRequestProperty("Charset", "UTF-8");
				//设置获取实体数据的范围
				http.setRequestProperty("Range", "bytes=" + mDownFile.getDownLength() + "-"+ mDownFile.getTotalLength());
				http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.2; Trident/4.0; .NET CLR 1.1.4322; .NET CLR 2.0.50727; .NET CLR 3.0.04506.30; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				http.setRequestProperty("Connection", "Keep-Alive");
				
				inStream = http.getInputStream();
				byte[] buffer = new byte[1024];
				int offset = 0;
				threadfile = new RandomAccessFile(this.saveFile, "rwd");
				threadfile.seek(mDownFile.getDownLength());
				
				while (flag && (offset = inStream.read(buffer, 0, 1024)) != -1) {
					AbLogUtil.d(AbDownloadThread.class, "offset:"+offset);
					if(offset!=0){
						threadfile.write(buffer, 0, offset);
						mDownFile.setDownLength(mDownFile.getDownLength()+offset);
						offset = 0;
						AbLogUtil.d(AbDownloadThread.class, "DownLength:"+mDownFile.getDownLength()+"/"+mDownFile.getTotalLength());
						downloader.update(mDownFile);
						
						if(mDownFile.getDownLength() == mDownFile.getTotalLength()){
							this.finish = true;
							flag = false;
							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				mDownFile.setDownLength(-1);
			} finally{
				try {
					if(threadfile!=null){
						threadfile.close();
					}
					if(inStream!=null){
						inStream.close();
					}
				} catch (IOException e) {
				}
			}
		}
	}
	
	
	/**
	 * 下载是否完成.
	 *
	 * @return true, if is finish
	 */
	public boolean isFinish() {
		return finish;
	}

	/**
	 * Gets the save file.
	 *
	 * @return the save file
	 */
	public File getSaveFile() {
		return saveFile;
	}

	/**
	 * Sets the flag.
	 *
	 * @param flag the new flag
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
}
