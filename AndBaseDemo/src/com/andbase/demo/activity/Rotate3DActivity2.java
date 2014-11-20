package com.andbase.demo.activity;



import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ab.view.app.AbRotate3dAnimation2;
import com.andbase.R;

/**
 * 
 * © 2012 amsoft.cn
 * 名称：Rotate3DActivity2.java 
 * 描述：3d旋转View
 * @author 原作者  QQ：250333410
 * @date：2013-12-11 上午11:34:16
 * @version v1.0
 */
public class Rotate3DActivity2 extends Activity {
	
	private int mCenterX = 160;   
    private int mCenterY = 0;   
    //A
    private ViewGroup layoutFront; 
    //C
    private ViewGroup layoutBack;
    //B
    private ViewGroup layoutRight; 
    //D
    private ViewGroup layoutLeft;
    
    private AbRotate3dAnimation2 leftAnimation;   
    private AbRotate3dAnimation2 rightAnimation;
    
    private int toLeft = 0;
    private int toRight = 1;
 	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //显示front
        setContentView(R.layout.layout_front);
        
        //显示正面
        layoutFront = (ViewGroup)findViewById(R.id.layout_front);
        Button leftBtn = (Button)findViewById(R.id.front_leftBtn);
        Button rightBtn = (Button)findViewById(R.id.front_rightBtn);
        leftBtn.setEnabled(true); 
        rightBtn.setEnabled(true); 
        
        leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) { 
            	frontMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
    }
        
    //左旋转
    public void initFirst(){   
        leftAnimation = new AbRotate3dAnimation2(0, -90, 0.0f, 0.0f, mCenterX, mCenterY);   
        rightAnimation = new AbRotate3dAnimation2(90, 0, 0.0f, 0.0f, mCenterX, mCenterY);   
        leftAnimation.setFillAfter(true);   
        leftAnimation.setDuration(1000);   
        rightAnimation.setFillAfter(true);   
        rightAnimation.setDuration(1000);   
    }
    
    //右旋转
    public void initSecond(){   
        leftAnimation = new AbRotate3dAnimation2(-90, 0, 0.0f, 0.0f, mCenterX, mCenterY);   
        rightAnimation = new AbRotate3dAnimation2(0, 90, 0.0f, 0.0f, mCenterX, mCenterY);   
        leftAnimation.setFillAfter(true);   
        leftAnimation.setDuration(1000);   
        rightAnimation.setFillAfter(true);   
        rightAnimation.setDuration(1000);   
    } 
    
    //B面转到A面所在位置
    public void B2A(AbRotate3dAnimation2 rightAnimation){
    	setContentView(R.layout.layout_right);
    	layoutRight = (ViewGroup)findViewById(R.id.layout_right);
    	layoutRight.startAnimation(rightAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.right_leftBtn);
    	Button rightBtn = (Button)findViewById(R.id.right_rightBtn);
    	leftBtn.setEnabled(true); rightBtn.setEnabled(true); 
    	
    	leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });	
    }
    
    //D面转到A面所在位置
    public void D2A(AbRotate3dAnimation2 leftAnimation){
    	setContentView(R.layout.layout_left);
    	layoutLeft = (ViewGroup)findViewById(R.id.layout_left);
    	layoutLeft.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.left_leftBtn);
    	Button rightBtn = (Button)findViewById(R.id.left_rightBtn);
    	leftBtn.setEnabled(true); rightBtn.setEnabled(true);
    	
    	leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });	
    }
    
    //A面转到D面所在位置
    public void A2D(AbRotate3dAnimation2 rightAnimation){
    	setContentView(R.layout.layout_front);
		layoutFront = (ViewGroup)findViewById(R.id.layout_front);
		layoutFront.startAnimation(rightAnimation);
		
		Button leftBtn = (Button)findViewById(R.id.front_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.front_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
	}
    
    //C面转到D面所在位置
    public void C2D(AbRotate3dAnimation2 leftAnimation){
    	setContentView(R.layout.layout_back);
    	layoutBack = (ViewGroup)findViewById(R.id.layout_back);
    	layoutBack.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.back_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.back_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
    }
    
    //C面转到B面所在位置
    public void C2B(AbRotate3dAnimation2 rightAnimation){
    	setContentView(R.layout.layout_back);
    	layoutBack = (ViewGroup)findViewById(R.id.layout_back);
    	layoutBack.startAnimation(rightAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.back_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.back_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	backMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });
    }
    
    //A面转到B面所在位置
    public void A2B(AbRotate3dAnimation2 leftAnimation){
    	setContentView(R.layout.layout_front);
    	layoutFront = (ViewGroup)findViewById(R.id.layout_front);
    	layoutFront.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.front_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.front_rightBtn);
    	
    	leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	frontMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        }); 
    }
    
    //D面转到C面所在位置
    public void D2C(AbRotate3dAnimation2 rightAnimation){
    	setContentView(R.layout.layout_left);
    	layoutLeft = (ViewGroup)findViewById(R.id.layout_left);
    	layoutLeft.startAnimation(rightAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.left_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.left_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	leftMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });
    }
    
    //B面转到C面所在位置
    public void B2C(AbRotate3dAnimation2 leftAnimation){
    	setContentView(R.layout.layout_right);
    	layoutRight = (ViewGroup)findViewById(R.id.layout_right);
    	layoutRight.startAnimation(leftAnimation);
    	
    	Button leftBtn = (Button)findViewById(R.id.right_leftBtn);
		Button rightBtn = (Button)findViewById(R.id.right_rightBtn);
		
		leftBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toRight);   
                v.setEnabled(false);   
            }   
        }); 
        rightBtn.setOnClickListener(new Button.OnClickListener() {   
            public void onClick(View v) {   
            	rightMoveHandle(toLeft);   
                v.setEnabled(false);   
            }   
        });
    }
    
    //A在正面时
    public void frontMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutFront.startAnimation(leftAnimation);
        	B2A(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutFront.startAnimation(rightAnimation);
        	D2A(leftAnimation);
    	}
    	
    }
    
    //B位于正面时
    public void rightMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutRight.startAnimation(leftAnimation);
        	C2B(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutRight.startAnimation(rightAnimation);
        	A2B(leftAnimation);
    	}
    	
    }
    
    //D位于正面时
    public void leftMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutLeft.startAnimation(leftAnimation);
        	A2D(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutLeft.startAnimation(rightAnimation);
        	C2D(leftAnimation);
    	}
    	
    }
    
    //C位于正面时
    public void backMoveHandle(int to){
    	if(to == toLeft){
    		initFirst();
        	layoutBack.startAnimation(leftAnimation);
        	D2C(rightAnimation);
    	}else if(to == toRight){
    		initSecond();
        	layoutBack.startAnimation(rightAnimation);
        	B2C(leftAnimation);
    	}
    	
    	
    }
    
    
}