/*
 * Copyright (C) 2013 www.418log.org
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

import java.math.BigDecimal;

// TODO: Auto-generated Javadoc
/**
 * 描述：数学处理类.
 *
 * @author zhaoqp
 * @date：2013-1-18 上午10:14:44
 * @version v1.0
 */
public class AbMathUtil{

  /**
   * 四舍五入.
   *
   * @param number  原数
   * @param decimal 保留几位小数
   * @return 四舍五入后的值
   */
  public static BigDecimal round(double number, int decimal){
    return new BigDecimal(number).setScale(decimal, BigDecimal.ROUND_HALF_UP);
  }
  
  /**
   * 描述：字节数组转换成16进制串.
   *
   * @param b the b
   * @param length the length
   * @return the string
   */
  public static String byte2HexStr(byte[] b, int length){
    String hs = "";
    String stmp = "";
    for (int n = 0; n < length; ++n) {
      stmp = Integer.toHexString(b[n] & 0xFF);
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else {
        hs = hs + stmp;
      }
      hs = hs + ",";
    }
    return hs.toUpperCase();
  } 
  
  /**
	* 二进制转为十六进制
	* @param int binary
    * @return char hex
	*/
	public static char binaryToHex(int binary) {
		char ch = ' ';
		switch (binary){
		case 0:
			ch = '0';
			break;
		case 1:
			ch = '1';
			break;
		case 2:
			ch = '2';
			break;
		case 3:
			ch = '3';
			break;
		case 4:
			ch = '4';
			break;
		case 5:
			ch = '5';
			break;
		case 6:
			ch = '6';
			break;
		case 7:
			ch = '7';
			break;
		case 8:
			ch = '8';
			break;
		case 9:
			ch = '9';
			break;
		case 10:
			ch = 'a';
			break;
		case 11:
			ch = 'b';
			break;
		case 12:
			ch = 'c';
			break;
		case 13:
			ch = 'd';
			break;
		case 14:
			ch = 'e';
			break;
		case 15:
			ch = 'f';
			break;
		default:
			ch = ' ';
		}
		return ch;
	}
	
	
	/** 
     * 一维数组转为二维数组 
     *  
     * @param m 
     * @param width 
     * @param height 
     * @return 
     */  
    public static int[][] arrayToMatrix(int[] m, int width, int height) {  
        int[][] result = new int[height][width];  
        for (int i = 0; i < height; i++) {  
            for (int j = 0; j < width; j++) {  
                int p = j * height + i;  
                result[i][j] = m[p];  
            }  
        }  
        return result;  
    }  

	
	/** 
     * 二维数组转为一维数组 
     *  
     * @param m 
     * @return 
     */  
    public static double[] matrixToArray(double[][] m) {  
        int p = m.length * m[0].length;  
        double[] result = new double[p];  
        for (int i = 0; i < m.length; i++) {  
            for (int j = 0; j < m[i].length; j++) {  
                int q = j * m.length + i;  
                result[q] = m[i][j];  
            }  
        }  
        return result;  
    }  

    /**
     * 描述：int数组转换为double数组
     * @param input
     * @return
     */
    public static double[] intToDoubleArray(int[] input) {  
        int length = input.length;  
        double[] output = new double[length];  
        for (int i = 0; i < length; i++){  
            output[i] = Double.valueOf(String.valueOf(input[i]));  
        }
        return output;  
    }  
    
    /**
     * 描述：int二维数组转换为double二维数组
     * @param input
     * @return
     */
    public static double[][] intToDoubleMatrix(int[][] input) {  
        int height = input.length;  
        int width = input[0].length;  
        double[][] output = new double[height][width];  
        for (int i = 0; i < height; i++) {  
            // 列   
            for (int j = 0; j < width; j++) {  
                // 行   
                output[i][j] = Double.valueOf(String.valueOf(input[i][j]));  
            }  
        }  
        return output;  
    }  

    /**
	 * 计算数组的平均值
	 * @param pixels 数组
	 * @return int 平均值
	 */
    public static int average(int[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
    
    /**
	 * 计算数组的平均值
	 * @param pixels 数组
	 * @return int 平均值
	 */
    public static int average(double[] pixels) {
		float m = 0;
		for (int i = 0; i < pixels.length; ++i) {
			m += pixels[i];
		}
		m = m / pixels.length;
		return (int) m;
	}
 
}
