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


// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn 
 * 名称：AbHtmlUtil.java 
 * 描述：HTML处理类.
 * 
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-18 下午11:52:13
 */
public class AbHtmlUtil {

	/**
	 * 
	 * 描述：替换标记以正常显示.
	 * @param text
	 * @return
	 */
	public static String replaceTag(String text) {
		if (!hasSpecialChars(text)) {
			return text;
		}
		StringBuffer filtered = new StringBuffer(text.length());
		char c;
		for (int i = 0; i <= text.length() - 1; i++) {
			c = text.charAt(i);
			switch (c) {
			case '<':
				filtered.append("&lt;");
				break;
			case '>':
				filtered.append("&gt;");
				break;
			case '"':
				filtered.append("&quot;");
				break;
			case '&':
				filtered.append("&amp;");
				break;
			default:
				filtered.append(c);
			}

		}
		return (filtered.toString());
	}

	/**
	 * 
	 * 描述：判断标记是否存在.
	 * @param text
	 * @return
	 */
	public static boolean hasSpecialChars(String text) {
		boolean flag = false;
		if ((text != null) && (text.length() > 0)) {
			char c;
			for (int i = 0; i <= text.length() - 1; i++) {
				c = text.charAt(i);
				switch (c) {
				case '>':
					flag = true;
					break;
				case '<':
					flag = true;
					break;
				case '"':
					flag = true;
					break;
				case '&':
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

}
