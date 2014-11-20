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
package com.ab.view.sample;


import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.text.Layout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbTextView.java 
 * 描述：中英混合乱换行问题 .
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2013-05-17 下午6:46:29
 */
public class AbTextView extends TextView {
    
    /** padding. */
    private float leftPadding = 0;
    
    /** The top padding. */
    private float topPadding = 0;
    
    /** The right padding. */
    private float rightPadding = 0;
    
    /** The bottom padding. */
    private float bottomPadding = 0;
    
    /** The line spacing. */
    private float lineSpacing = 0;
    
    /** 最大行数. */
    private int maxLines = 1;
    
    /** 文字大小. */
    private float textSize = 14;
    
    /** 文字颜色. */
    private int textColor = Color.WHITE;
    
    /** TextPaint. */
    private TextPaint mTextPaint = null;

    /**
     * Instantiates a new ab text view.
     *
     * @param context the context
     * @param attrs the attrs
     */
    public AbTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTextPaint = this.getPaint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(textSize);
    }

    /* (non-Javadoc)
     * @see android.widget.TextView#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // 清屏幕
        canvas.drawColor(Color.TRANSPARENT);
        drawText(canvas, this.getText().toString(), this.getWidth(), this.getPaint());
    }

    /**
     * Sets the padding.
     *
     * @param left the left
     * @param top the top
     * @param right the right
     * @param bottom the bottom
     */
    public void setPadding(float left,float top,float right,float bottom){
        leftPadding = left;
        topPadding = top;
        rightPadding = right;
        bottomPadding = bottom;
        this.invalidate();
    }


    /**
     * Sub string length.
     *
     * @param str the str
     * @param maxPix the max pix
     * @param paint the paint
     * @return the int
     */
    public int subStringLength(String str, int maxPix, TextPaint paint) {
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int currentIndex = 0;
        for (int i = 0; i < str.length(); i++) {
            // 获取一个字符
            String temp = str.substring(0, i + 1);
            float valueLength = paint.measureText(temp)+leftPadding+rightPadding;
            if (valueLength > maxPix) {
                currentIndex = i - 1;
                break;
            } else if (valueLength == maxPix) {
                currentIndex = i;
                break;
            }
        }
        // 短于最大像素返回最后一个字符位置
        if (currentIndex == 0) {
            currentIndex = str.length() - 1;
        }
        return currentIndex;
    }

    /**
     * Gets the string width.
     *
     * @param str the str
     * @param paint the paint
     * @return the string width
     */
    public float getStringWidth(String str, TextPaint paint) {
        float strWidth = paint.measureText(str);
        return strWidth;
    }

    /**
     * Gets the desired width.
     *
     * @param str the str
     * @param paint the paint
     * @return the desired width
     */
    public float getDesiredWidth(String str, TextPaint paint) {
        float strWidth = Layout.getDesiredWidth(str, paint);
        return strWidth;
    }

    /**
     * Gets the draw row str.
     *
     * @param text the text
     * @param maxWPix the max w pix
     * @param paint the paint
     * @return the draw row str
     */
    public List<String> getDrawRowStr(String text, int maxWPix,
            TextPaint paint) {
        String[] texts = null;
        if (text.indexOf("\n") != -1) {
            texts = text.split("\n");
        } else {
            texts = new String[1];
            texts[0] = text;
        }
        // 共多少行
        List<String> mStrList = new ArrayList<String>();

        for (int i = 0; i < texts.length; i++) {
            String textLine = texts[i];
            // 计算这个文本显示为几行
            while (true) {
                // 可容纳的最后一个字的位置
                int endIndex = subStringLength(textLine, maxWPix, paint);
                if (endIndex <= 0) {
                    mStrList.add(textLine);
                } else {
                    if (endIndex == textLine.length() - 1) {
                        mStrList.add(textLine);
                    } else {
                        mStrList.add(textLine.substring(0, endIndex + 1));
                    }

                }
                // 获取剩下的
                if (textLine.length() > endIndex + 1) {
                    // 还有剩下的
                    textLine = textLine.substring(endIndex + 1);
                } else {
                    break;
                }
            }
        }

        return mStrList;
    }

    /**
     * Gets the draw row count.
     *
     * @param text the text
     * @param maxWPix the max w pix
     * @param paint the paint
     * @return the draw row count
     */
    public int getDrawRowCount(String text, int maxWPix, TextPaint paint) {
        String[] texts = null;
        if (text.indexOf("\n") != -1) {
            texts = text.split("\n");
        } else {
            texts = new String[1];
            texts[0] = text;
        }
        // 共多少行
        List<String> mStrList = new ArrayList<String>();

        for (int i = 0; i < texts.length; i++) {
            String textLine = texts[i];
            // 计算这个文本显示为几行
            while (true) {
                // 可容纳的最后一个字的位置
                int endIndex = subStringLength(textLine, maxWPix, paint);
                if (endIndex <= 0) {
                    mStrList.add(textLine);
                } else {
                    if (endIndex == textLine.length() - 1) {
                        mStrList.add(textLine);
                    } else {
                        mStrList.add(textLine.substring(0, endIndex + 1));
                    }

                }
                // 获取剩下的
                if (textLine.length() > endIndex + 1) {
                    // 还有剩下的
                    textLine = textLine.substring(endIndex + 1);
                } else {
                    break;
                }
            }
        }

        return mStrList.size();
    }

    /**
     * Draw text.
     *
     * @param canvas the canvas
     * @param text the text
     * @param maxWPix the max w pix
     * @param paint the paint
     * @return the int
     */
    public int drawText(Canvas canvas, String text, int maxWPix,
            TextPaint paint) {
        if (TextUtils.isEmpty(text)) {
            return 1;
        }
        // 需要根据文字长度控制换行
        // 测量文字的长度
        List<String> mStrList = getDrawRowStr(text, maxWPix, paint);

        FontMetrics fm = paint.getFontMetrics();
        int hSize = (int)Math.ceil(fm.descent - fm.ascent);

        for (int i = 0; i < mStrList.size(); i++) {
            // 计算坐标
            float x = leftPadding;
            float y = topPadding+hSize/2+i*(hSize+lineSpacing)+bottomPadding;
            String textLine = mStrList.get(i);
            if(i < maxLines){
                canvas.drawText(textLine, x, y, paint);
            }
        }
        return mStrList.size();
    }


    /**
     * Gets the max lines.
     *
     * @return the max lines
     */
    @SuppressLint("Override")
    public int getMaxLines(){
        return maxLines;
    }

    /* (non-Javadoc)
     * @see android.widget.TextView#setMaxLines(int)
     */
    @Override
    public void setMaxLines(int maxLines){
        this.maxLines = maxLines;
        this.invalidate();
    }
    
    /**
     * Gets the line spacing.
     *
     * @return the line spacing
     */
    public float getLineSpacing(){
        return lineSpacing;
    }

    /**
     * Sets the line spacing.
     *
     * @param lineSpacing the new line spacing
     */
    public void setLineSpacing(float lineSpacing){
        this.lineSpacing = lineSpacing;
    }

    /* (non-Javadoc)
     * @see android.widget.TextView#setTextSize(float)
     */
    @Override
    public void setTextSize(float size) {
        this.textSize = size;
        Context c = getContext();
        Resources r;

        if (c == null)
            r = Resources.getSystem();
        else
            r = c.getResources();

        setRawTextSize(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, size, r.getDisplayMetrics()));
    }
    
    
    /**
     * Sets the raw text size.
     *
     * @param size the new raw text size
     */
    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            this.invalidate();
        }
    }

    /* (non-Javadoc)
     * @see android.widget.TextView#getTextSize()
     */
    public float getTextSize(){
        return textSize;
    }

    /**
     * Gets the text color.
     *
     * @return the text color
     */
    public int getTextColor(){
        return textColor;
    }

    /* (non-Javadoc)
     * @see android.widget.TextView#setTextColor(int)
     */
    @Override
    public void setTextColor(int textColor){
        this.textColor = textColor;
        mTextPaint.setColor(textColor);
        this.invalidate();
    }
    
    
}
