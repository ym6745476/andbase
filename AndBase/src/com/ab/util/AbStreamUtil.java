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
package com.ab.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

// TODO: Auto-generated Javadoc
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbStreamUtil.java 
 * 描述：流工具类
 * @author 还如一梦中
 * @date 2015年4月3日 下午1:42:44
 * @version v1.0
 */
public class AbStreamUtil {

	/**
	 * 获取ByteArrayInputStream.
	 *
	 * @param buf the buf
	 * @return the input stream
	 */
	public static InputStream bytes2Stream(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}

	/**
	 * 从流中读取数据到byte[]..
	 *
	 * @param inStream the in stream
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static byte[] stream2bytes(InputStream inStream) throws IOException {
		byte[] buff = new byte[1024];
		byte[] data = null;
		try {
			ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
			int read = 0;
			while ((read = inStream.read(buff, 0, 100)) > 0) {
				swapStream.write(buff, 0, read);
			}
			data = swapStream.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	/**
     * 从流中读取指定的长度到byte[].
     *
     * @param in the in
     * @param length the length
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static byte[] stream2Bytes(InputStream in, int length) throws IOException {
        byte[] bytes = new byte[length];
        int count;
        int pos = 0;
        while (pos < length && ((count = in.read(bytes, pos, length - pos)) != -1)) {
            pos += count;
        }
        if (pos != length) {
            throw new IOException("Expected " + length + " bytes, read " + pos + " bytes");
        }
        return bytes;
    }
}
