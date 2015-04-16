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
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
	
	/**
     * Simple wrapper around {@link InputStream#read()} that throws EOFException
     * instead of returning -1.
     *
     * @param is the is
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static int read(InputStream is) throws IOException {
        int b = is.read();
        if (b == -1) {
            throw new EOFException();
        }
        return b;
    }

    /**
     * Write int.
     *
     * @param os the os
     * @param n the n
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static void writeInt(OutputStream os, int n) throws IOException {
        os.write((n >> 0) & 0xff);
        os.write((n >> 8) & 0xff);
        os.write((n >> 16) & 0xff);
        os.write((n >> 24) & 0xff);
    }

    /**
     * Read int.
     *
     * @param is the is
     * @return the int
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static int readInt(InputStream is) throws IOException {
        int n = 0;
        n |= (read(is) << 0);
        n |= (read(is) << 8);
        n |= (read(is) << 16);
        n |= (read(is) << 24);
        return n;
    }

    /**
     * Write long.
     *
     * @param os the os
     * @param n the n
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static void writeLong(OutputStream os, long n) throws IOException {
        os.write((byte)(n >>> 0));
        os.write((byte)(n >>> 8));
        os.write((byte)(n >>> 16));
        os.write((byte)(n >>> 24));
        os.write((byte)(n >>> 32));
        os.write((byte)(n >>> 40));
        os.write((byte)(n >>> 48));
        os.write((byte)(n >>> 56));
    }

    /**
     * Read long.
     *
     * @param is the is
     * @return the long
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static long readLong(InputStream is) throws IOException {
        long n = 0;
        n |= ((read(is) & 0xFFL) << 0);
        n |= ((read(is) & 0xFFL) << 8);
        n |= ((read(is) & 0xFFL) << 16);
        n |= ((read(is) & 0xFFL) << 24);
        n |= ((read(is) & 0xFFL) << 32);
        n |= ((read(is) & 0xFFL) << 40);
        n |= ((read(is) & 0xFFL) << 48);
        n |= ((read(is) & 0xFFL) << 56);
        return n;
    }

    /**
     * Write string.
     *
     * @param os the os
     * @param s the s
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static void writeString(OutputStream os, String s) throws IOException {
        byte[] b = s.getBytes("UTF-8");
        writeLong(os, b.length);
        os.write(b, 0, b.length);
    }

    /**
     * Read string.
     *
     * @param is the is
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static String readString(InputStream is) throws IOException {
        int n = (int) readLong(is);
        byte[] b = AbStreamUtil.stream2Bytes(is, n);
        return new String(b, "UTF-8");
    }

    /**
     * Write string string map.
     *
     * @param map the map
     * @param os the os
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static void writeStringStringMap(Map<String, String> map, OutputStream os) throws IOException {
        if (map != null) {
            writeInt(os, map.size());
            for (Map.Entry<String, String> entry : map.entrySet()) {
                writeString(os, entry.getKey());
                writeString(os, entry.getValue());
            }
        } else {
            writeInt(os, 0);
        }
    }

    /**
     * Read string string map.
     *
     * @param is the is
     * @return the map
     * @throws IOException Signals that an I/O exception has occurred.
     */
	public static Map<String, String> readStringStringMap(InputStream is) throws IOException {
        int size = readInt(is);
        Map<String, String> result = (size == 0)
                ? Collections.<String, String>emptyMap()
                : new HashMap<String, String>(size);
        for (int i = 0; i < size; i++) {
            String key = readString(is).intern();
            String value = readString(is).intern();
            result.put(key, value);
        }
        return result;
    }
}
