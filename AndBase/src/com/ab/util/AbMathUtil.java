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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.ab.model.AbCircle;
import com.ab.model.AbPoint;


// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbMathUtil.java 
 * 描述：数学处理类.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-01-17 下午11:52:13
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
   * 二进制转为十六进制.
   *
   * @param binary the binary
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
	 *  
	 * 一维数组转为二维数组 
	 *  
	 *
	 * @param m the m
	 * @param width the width
	 * @param height the height
	 * @return the int[][]
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
	 *  
	 * 二维数组转为一维数组 
	 *  
	 *
	 * @param m the m
	 * @return the double[]
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
     * 描述：int数组转换为double数组.
     *
     * @param input the input
     * @return the double[]
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
     * 描述：int二维数组转换为double二维数组.
     *
     * @param input the input
     * @return the double[][]
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
     * 计算数组的平均值.
     *
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
     * 计算数组的平均值.
     *
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
    
    /**
	 * 描述：计算对数
	 * @param value value的对数
	 * @param base  以base为底
	 * @return
	 */
	public static double log(double value, double base) {
		return Math.log(value) / Math.log(base);
	}
    
    /**
     * 
     * 描述：点在直线上.
     * 点A（x，y）,B(x1,y1),C(x2,y2) 点A在直线BC上吗?
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public boolean pointOnLine(double x,double y,double x1,double y1,double x2,double y2){
        double result = ( x - x1 ) * ( y2 - y1 ) - ( y - y1 ) * ( x2 - x1 );
    	if(result==0){
			return true;
		}else{
			return false;
		}
    }
    
    
	/**
	 * 
	 * 描述：点在线段上.
	 * 点A（x，y）,B(x1,y1),C(x2,y2)   点A在线段BC上吗?
	 * @param x
	 * @param y
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
    public static boolean pointAtELine(double x,double y,double x1,double y1,double x2,double y2){
    	double result = ( x - x1 ) * ( y2 - y1 ) - ( y - y1 ) * ( x2 - x1 );
    	if(result==0){
    		if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
    		    && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
    		    return true;
	    	}else{
	    	    return false;
	    	}
    	}else{
    		return false;
    	}
    }
    
    /**
     * 
     * 描述：两条直线相交.
     * 点A（x1，y1）,B(x2,y2),C(x3,y3),D(x4,y4)   直线AB与直线CD相交吗?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public  static boolean LineOnLine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
	    double k1 = ( y2-y1 )/(x2-x1);
	    double k2 = ( y4-y3 )/(x4-x3);
		if(k1==k2){
			//System.out.println("平行线");
			return false;
		}else{
		  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
		  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
		  //System.out.println("直线的交点("+x+","+y+")");
		  return true;
		}
	}
    
    /**
     * 
     * 描述：线段与线段相交.
     * 点A（x1，y1）,B(x2,y2),C(x3,y3),D(x4,y4)   
     * 线段AB与线段CD相交吗?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean eLineOnELine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		    double k1 = ( y2-y1 )/(x2-x1);
		    double k2 = ( y4-y3 )/(x4-x3);
			if(k1==k2){
				//System.out.println("平行线");
				return false;
			}else{
			  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
			  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
			  //System.out.println("直线的交点("+x+","+y+")");
			  if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
					  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)
				      && x >= Math.min(x3, x4) && x <= Math.max(x3,x4) 
				      && y >= Math.min(y3, y4) && y <= Math.max(y3,y4) ){
					//System.out.println("交点（"+x+","+y+"）在线段上");
				return true;
			  }else{
				//System.out.println("交点（"+x+","+y+"）不在线段上");
				return false;
			  } 
	       }
	}
    
    /**
     * 
     * 描述：线段直线相交.
     * 点A（x1，y1）,B(x2,y2),C(x3,y3),D(x4,y4)   
     * 线段AB与直线CD相交吗?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean eLineOnLine(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
		    double k1 = ( y2-y1 )/(x2-x1);
		    double k2 = ( y4-y3 )/(x4-x3);
			if(k1==k2){
				//System.out.println("平行线");
				return false;
			}else{
			  double x = ((x1*y2-y1*x2)*(x3-x4)-(x3*y4-y3*x4)*(x1-x2))/((y2-y1)*(x3-x4)-(y4-y3)*(x1-x2));
			  double y = ( x1*y2-y1*x2 - x*(y2-y1) ) / (x1-x2);
			  //System.out.println("交点("+x+","+y+")");
			  if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
					  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
					//System.out.println("交点（"+x+","+y+"）在线段上");
				  return true;
			  }else{
							//System.out.println("交点（"+x+","+y+"）不在线段上");
				return false;
			  } 
		}
	}
    
    /**
     * 
     * 描述：点在矩形内.
     * 矩形的边都是与坐标系平行或垂直的。
     * 只要判断该点的横坐标和纵坐标是否夹在矩形的左右边和上下边之间。
     * 点A（x，y）,B(x1,y1),C(x2,y2)   点A在以直线BC为对角线的矩形中吗?
     * @param x
     * @param y
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean pointOnRect(double x,double y,double x1,double y1,double x2,double y2){
	      if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
    	     //System.out.println("点（"+x+","+y+"）在矩形内上");
    	     return true;
	      }else{
	    	 //System.out.println("点（"+x+","+y+"）不在矩形内上");
	    	 return false;
		  }
	}
    
    /**
     * 
     * 描述：矩形在矩形内.
     * 只要对角线的两点都在另一个矩形中就可以了.
     * 点A(x1,y1),B(x2,y2)，C(x1,y1),D(x2,y2) 以直线AB为对角线的矩形在以直线BC为对角线的矩形中吗?
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @param x3
     * @param y3
     * @param x4
     * @param y4
     * @return
     */
    public static boolean rectOnRect(double x1,double y1,double x2,double y2,double x3,double y3,double x4,double y4){
	      if(x1 >= Math.min(x3, x4) && x1 <= Math.max(x3,x4) 
			  && y1 >= Math.min(y3, y4) && y1 <= Math.max(y3,y4)
			  && x2 >= Math.min(x3, x4) && x2 <= Math.max(x3,x4) 
			  && y2 >= Math.min(y3, y4) && y2 <= Math.max(y3,y4)){
    	     //System.out.println("矩形在矩形内");
    	     return true;
	      }else{
    	     //System.out.println("矩形不在矩形内");
    	     return false;
		  }
	}
    
    /**
     * 
     * 描述：圆心在矩形内 .
     * 圆心在矩形中且圆的半径小于等于圆心到矩形四边的距离的最小值。
     * 圆心(x,y) 半径r  矩形对角点A（x1，y1），B(x2，y2)
     * @param x
     * @param y
     * @param r
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static boolean circleOnRect(double x,double y,double r,double x1,double y1,double x2,double y2){
		//圆心在矩形内   
		if(x >= Math.min(x1, x2) && x <= Math.max(x1,x2) 
						  && y >= Math.min(y1, y2) && y <= Math.max(y1,y2)){
		//圆心到4条边的距离		  
        double l1= Math.abs(x-x1);
		double l2= Math.abs(y-y2);
		double l3= Math.abs(x-x2);
		double l4= Math.abs(y-y2);
    	if(r<=l1 && r<=l2 && r<=l3 && r<=l4){
    		  //System.out.println("圆在矩形内");
	    	  return true;
    	  }else{
    		  //System.out.println("圆不在矩形内");
	    	  return false;
    	  }
    	 
       }else{
    	     //System.out.println("圆不在矩形内");
    	    return false;
	   }
	}
    
    /**
	 * 
	 * 描述：点是否是两个圆的交点.
	 * @param point
	 * @param c1
	 * @param c2
	 * @return
	 */
    public static boolean pointOnCircle(AbPoint point,AbCircle c1,AbCircle c2){
		
		if(Math.pow(point.x-c2.point.x,2) + Math.pow(point.y-c2.point.y,2)==Math.pow(c2.r,2)
				&& Math.pow(point.x-c1.point.x,2) + Math.pow(point.y-c1.point.y,2)==Math.pow(c1.r,2)){
			return true;
		}
		return false;
		
	}
    
    /**
 	 * 
 	 * 描述：点是否是两个圆的交点,允许0.01误差.
 	 * @param point
 	 * @param c1
 	 * @param c2
 	 * @param offset
 	 * @return
 	 */
     public static boolean pointOnCircle(AbPoint point,AbCircle c1,AbCircle c2,float offset){
 		
 		if((Math.pow(point.x-c2.point.x,2) + Math.pow(point.y-c2.point.y,2)<=Math.pow(c2.r,2)+offset && Math.pow(point.x-c2.point.x,2) + Math.pow(point.y-c2.point.y,2)>=Math.pow(c2.r,2)-offset)
 				&& (Math.pow(point.x-c1.point.x,2) + Math.pow(point.y-c1.point.y,2)<=Math.pow(c1.r,2)+offset && Math.pow(point.x-c1.point.x,2) + Math.pow(point.y-c1.point.y,2)>=Math.pow(c1.r,2)-offset)){
 			return true;
 		}
 		return false;
 		
 	}
    
    /**
     * 
     * 描述：点在圆上.
     * @param point
     * @param circle
     * @return
     */
    public static boolean pointInCircle(AbPoint point,AbCircle circle){
		//圆的方程 (x-x0)^2 + (y-y0)^2 <=r^2
		if(Math.pow(point.x-circle.point.x,2) + Math.pow(point.y-circle.point.y,2) <= Math.pow(circle.r,2)){
			return true;
        }else{
    	    return false;
	   }
	}
    
    
    /**
     * 
     * 描述：获取两点间的距离.
     * @param p1
     * @param p2
     * @return
     */
    public static double getDistance(AbPoint p1,AbPoint p2) {  
        return getDistance(p1.x,p1.y,p2.x,p2.y);  
    }  

    /**
     *  
     * 描述：获取两点间的距离.
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static double getDistance(double x1,double y1,double x2,double y2) {  
    	double x = x1 - x2;  
    	double y = y1 - y2;  
        return Math.sqrt(x * x + y * y);  
    }  
    
    
    /**
	 * 矩形碰撞检测 参数为x,y,width,height
	 * @param x1 第一个矩形的x
	 * @param y1 第一个矩形的y
	 * @param w1 第一个矩形的w
	 * @param h1 第一个矩形的h
	 * @param x2 第二个矩形的x
	 * @param y2 第二个矩形的y
	 * @param w2 第二个矩形的w
	 * @param h2 第二个矩形的h
	 * @return 是否碰撞
	 */
	public static boolean isRectCollision(float x1, float y1, float w1,
			float h1, float x2, float y2, float w2, float h2) {
		if (x2 > x1 && x2 > x1 + w1) {
			return false;
		} else if (x2 < x1 && x2 < x1 - w2) {
			return false;
		} else if (y2 > y1 && y2 > y1 + h1) {
			return false;
		} else if (y2 < y1 && y2 < y1 - h2) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * 
	 * 描述：求两个圆的交点.
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static List<AbPoint> circleCrossoverPoint(AbCircle circle1,AbCircle circle2){
		List<AbPoint> pointList = new ArrayList<AbPoint>();
		//第一个圆的参数方程:
		// x = r1 * cosCita + x1
		// y = r1 * sinCita + y1
		
		//第二个圆的方程
		//(x0-x2)^2 + (y0-y2)^2 = r2^2
		
		//第一个带入第二个
		//2r1(x1-x2)cosCita + 2r1(y1-y2)sinCita = r2^2 - r1^2 - (x1-x2)^2 - (y1-y2)^2
		//令 :
		//a = 2r1(x1-x2)
		//b = 2r1(y1-y2)
		//c = r2^2 - r1^2 - (x1-x2)^2 - (y1-y2)^2
				
		double a = 2 * circle1.r * (circle1.point.x - circle2.point.x);
		
		double b = 2 * circle1.r * (circle1.point.y - circle2.point.y);
		
		double c = (Math.pow(circle2.r, 2) - Math.pow(circle1.r, 2) - Math.pow((circle1.point.x - circle2.point.x), 2) - Math.pow((circle1.point.y - circle2.point.y), 2));
		
		//得到：
		//acosCita + bsinCita = c
		
		//double sinCita = Math.sqrt(1-cosCita);
		
		//a*cosCita + b* Math.sqrt(1-cosCita) = c
				
		//得到：
		//(a^2+b^2)cosCita^2 - 2ac*cosCita + (c^2-b^2) = 0
		
		//根据 ax^2+bx + c = 0 式
		//令：p = a^2+b^2
		//   q = - 2ac,
		//   r = c^2-b^2
		
		//a
		double p = (Math.pow(a, 2) + Math.pow(b, 2));
		//b
		double q = -2 * a * c;
		//c
		double r = (Math.pow(c, 2) - Math.pow(b, 2));
		
		//cosCita = (-b+-(b^2-4ac)^(1/2))/2a
		double t = (Math.pow(q, 2) - 4 * p * r);
		double cosCita = (Math.sqrt(t) - q) / (2 * p);
		double cosCita2 = (-Math.sqrt(t) - q) / (2 * p);
		
		double x_1 = cosCita * circle1.r + circle1.point.x;
		double y_1_1 = Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_1-circle1.point.x,2))+circle1.point.y;
		double y_1_2 = -Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_1-circle1.point.x,2))+circle1.point.y;
		
		Set<AbPoint> pointSet = new HashSet<AbPoint>();
		AbPoint p1_1 = new AbPoint(x_1,y_1_1);
		if(pointOnCircle(p1_1,circle1,circle2)){
			pointSet.add(p1_1);
		}
		
		AbPoint p1_2 = new AbPoint(x_1,y_1_2);
		if(pointOnCircle(p1_2,circle1,circle2)){
			pointSet.add(p1_2);
		}
		
		double x_2 = cosCita2 * circle1.r + circle1.point.x;
		double y_2_1 = Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_2-circle1.point.x,2))+circle1.point.y;
		double y_2_2 = -Math.sqrt(Math.pow(circle1.r, 2)-Math.pow(x_2-circle1.point.x,2))+circle1.point.y;
		
		AbPoint p2_1 = new AbPoint(x_2,y_2_1);
		if(pointOnCircle(p2_1,circle1,circle2)){
			pointSet.add(p2_1);
		}
		
		AbPoint p2_2 = new AbPoint(x_2,y_2_2);
		if(pointOnCircle(p2_2,circle1,circle2)){
			pointSet.add(p2_2);
		}
		for(Iterator<AbPoint> iter = pointSet.iterator();iter.hasNext();){   
			AbPoint point = (AbPoint)iter.next();   
			pointList.add(point);
        }  
		return pointList;
	}
	
 
}
