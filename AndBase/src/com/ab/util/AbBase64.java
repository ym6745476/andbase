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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

// TODO: Auto-generated Javadoc
/**
 * © 2012 amsoft.cn 
 * 名称：AbBase64.java 
 * 描述：Base64工具类.
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2011-11-10 下午11:52:13
 */
public class AbBase64 {

	private static final char[] legalChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
			.toCharArray();

	/** The Constant base64EncodeChars. */
	private static final char[] base64EncodeChars = new char[] { 'A', 'B', 'C',
			'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '+', '/' };

	/** The Constant base64DecodeChars. */
	private static final byte[] base64DecodeChars = new byte[] { -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57,
			58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24,
			25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35,
			36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1,
			-1, -1, -1, -1 };

	/**
	 * Base64编码
	 * 
	 * @param data
	 * @return
	 */
	public static String encode(byte[] data) {
		int start = 0;
		int len = data.length;
		StringBuffer buf = new StringBuffer(data.length * 3 / 2);

		int end = len - 3;
		int i = start;

		while (i <= end) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 0x0ff) << 8)
					| (((int) data[i + 2]) & 0x0ff);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append(legalChars[d & 63]);

			i += 3;
		}

		if (i == start + len - 2) {
			int d = ((((int) data[i]) & 0x0ff) << 16)
					| ((((int) data[i + 1]) & 255) << 8);

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append(legalChars[(d >> 6) & 63]);
			buf.append("=");
		} else if (i == start + len - 1) {
			int d = (((int) data[i]) & 0x0ff) << 16;

			buf.append(legalChars[(d >> 18) & 63]);
			buf.append(legalChars[(d >> 12) & 63]);
			buf.append("==");
		}

		return buf.toString();
	}

	/**
	 * Base64解码
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] decode(String s) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			decode(s, bos);
		} catch (IOException e) {
			throw new RuntimeException();
		}
		byte[] decodedBytes = bos.toByteArray();
		try {
			bos.close();
			bos = null;
		} catch (IOException ex) {
			System.err.println("Error while decoding BASE64: " + ex.toString());
		}
		return decodedBytes;
	}

	private static void decode(String s, OutputStream os) throws IOException {
		int i = 0;

		int len = s.length();

		while (true) {
			while (i < len && s.charAt(i) <= ' ')
				i++;

			if (i == len)
				break;

			int tri = (decode(s.charAt(i)) << 18)
					+ (decode(s.charAt(i + 1)) << 12)
					+ (decode(s.charAt(i + 2)) << 6)
					+ (decode(s.charAt(i + 3)));

			os.write((tri >> 16) & 255);
			if (s.charAt(i + 2) == '=')
				break;
			os.write((tri >> 8) & 255);
			if (s.charAt(i + 3) == '=')
				break;
			os.write(tri & 255);

			i += 4;
		}
	}

	private static int decode(char c) {
		if (c >= 'A' && c <= 'Z')
			return ((int) c) - 65;
		else if (c >= 'a' && c <= 'z')
			return ((int) c) - 97 + 26;
		else if (c >= '0' && c <= '9')
			return ((int) c) - 48 + 26 + 26;
		else
			switch (c) {
			case '+':
				return 62;
			case '/':
				return 63;
			case '=':
				return 0;
			default:
				throw new RuntimeException("unexpected code: " + c);
			}
	}

	/**
	 * Encode.
	 * 
	 * @param str
	 *            the str
	 * @param charsetName
	 *            the charset name
	 * @return the string
	 */
	public static final String encode(String str, String charsetName) {
		return encode(str, charsetName, 0);
	}

	/**
	 * Encode.
	 * 
	 * @param str
	 *            the str
	 * @param charsetName
	 *            the charset name
	 * @param width
	 *            the width
	 * @return the string
	 */
	public static final String encode(String str, String charsetName, int width) {
		byte[] data = null;
		try {
			data = str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		int length = data.length;
		int size = (int) Math.ceil(length * 1.36);
		int splitsize = width > 0 ? size / width : 0;
		StringBuffer sb = new StringBuffer(size + splitsize);
		int r = length % 3;
		int len = length - r;
		int i = 0;
		int c;
		while (i < len) {
			c = (0x000000ff & data[i++]) << 16 | (0x000000ff & data[i++]) << 8
					| (0x000000ff & data[i++]);
			sb.append(base64EncodeChars[c >> 18]);
			sb.append(base64EncodeChars[c >> 12 & 0x3f]);
			sb.append(base64EncodeChars[c >> 6 & 0x3f]);
			sb.append(base64EncodeChars[c & 0x3f]);
		}
		if (r == 1) {
			c = 0x000000ff & data[i++];
			sb.append(base64EncodeChars[c >> 2]);
			sb.append(base64EncodeChars[(c & 0x03) << 4]);
			sb.append("==");
		} else if (r == 2) {
			c = (0x000000ff & data[i++]) << 8 | (0x000000ff & data[i++]);
			sb.append(base64EncodeChars[c >> 10]);
			sb.append(base64EncodeChars[c >> 4 & 0x3f]);
			sb.append(base64EncodeChars[(c & 0x0f) << 2]);
			sb.append("=");
		}
		if (splitsize > 0) {
			char split = '\n';
			for (i = width; i < sb.length(); i++) {
				sb.insert(i, split);
				i += width;
			}
		}
		return sb.toString();
	}

	/**
	 * Decode.
	 * 
	 * @param str
	 *            the str
	 * @param charsetName
	 *            the charset name
	 * @return the string
	 */
	public static final String decode(String str, String charsetName) {
		byte[] data = null;
		try {
			data = str.getBytes(charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		int len = data.length;
		ByteArrayOutputStream buf = new ByteArrayOutputStream(
				(int) (len * 0.67));
		int i = 0;
		int b1, b2, b3, b4;
		while (i < len) {
			do {
				if (i >= len) {
					b1 = -1;
					break;
				}
				b1 = base64DecodeChars[data[i++]];
			} while (i < len && b1 == -1);
			if (b1 == -1) {
				break;
			}
			do {
				if (i >= len) {
					b2 = -1;
					break;
				}
				b2 = base64DecodeChars[data[i++]];
			} while (i < len && b2 == -1);
			if (b2 == -1) {
				break;
			}
			buf.write((int) ((b1 << 2) | ((b2 & 0x30) >>> 4)));
			do {
				if (i >= len) {
					b3 = -1;
					break;
				}
				b3 = data[i++];
				if (b3 == 61) {
					b3 = -1;
					break;
				}
				b3 = base64DecodeChars[b3];
			} while (i < len && b3 == -1);
			if (b3 == -1) {
				break;
			}
			buf.write((int) (((b2 & 0x0f) << 4) | ((b3 & 0x3c) >>> 2)));
			do {
				if (i >= len) {
					b4 = -1;
					break;
				}
				b4 = data[i++];
				if (b4 == 61) {
					b4 = -1;
					break;
				}
				b4 = base64DecodeChars[b4];
			} while (b4 == -1);
			if (b4 == -1) {
				break;
			}
			buf.write((int) (((b3 & 0x03) << 6) | b4));
		}
		try {
			return buf.toString(charsetName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
