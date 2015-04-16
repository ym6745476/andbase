package com.andbase.demo.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ab.activity.AbActivity;
import com.ab.task.AbTask;
import com.ab.task.AbTaskItem;
import com.ab.task.AbTaskListener;
import com.ab.task.AbTaskObjectListener;
import com.ab.task.thread.AbTaskPool;
import com.ab.task.thread.AbTaskQueue;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * 异步的使用参照
 * http://www.amsoft.cn/post-133.html
 * @author 还如一梦中
 *
 */
public class ThreadControlActivity extends AbActivity {
	
	private MyApplication application;
	private AbTitleBar mAbTitleBar = null;
	private AbTaskQueue mAbTaskQueue;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAbContentView(R.layout.thread_main);
        application = (MyApplication)abApplication;
        
        mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.thread_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
        
        initTitleRightLayout();
        
        Button threadBtn  = (Button)this.findViewById(R.id.threadBtn);
        Button queueBtn  = (Button)this.findViewById(R.id.queueBtn);
        Button poolBtn  = (Button)this.findViewById(R.id.poolBtn);
        Button taskBtn1  = (Button)this.findViewById(R.id.taskBtn1);
        Button taskBtn2  = (Button)this.findViewById(R.id.taskBtn2);
        
        //单个线程
        threadBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//显示进度框
				AbDialogUtil.showProgressDialog(ThreadControlActivity.this,R.drawable.progress_circular,"正在查询...");
				AbTask mAbTask = AbTask.newInstance();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(ThreadControlActivity.this);
						AbToastUtil.showToast(ThreadControlActivity.this,"执行完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	AbToastUtil.showToastInThread(ThreadControlActivity.this,"开始执行");
			   		    	Thread.sleep(3000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				//开始执行
				mAbTask.execute(item);
			}
        	
        });
        
        //线程队列
        mAbTaskQueue = AbTaskQueue.newInstance();
        queueBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//显示进度框
				AbDialogUtil.showProgressDialog(ThreadControlActivity.this,R.drawable.progress_circular,"正在查询...");
				//获取队列
				//定义异步执行的对象
		    	AbTaskItem item1 = new AbTaskItem();
				item1.setListener(new AbTaskObjectListener() {

					
					@SuppressWarnings("unchecked")
					@Override
					public String getObject() {
						String msg1 = "amsoft";
						AbToastUtil.showToastInThread(ThreadControlActivity.this,"开始执行1,"+msg1);
		   		    	try {
							Thread.sleep(2000);
						} catch (Exception e) {
						}
		   		    	//下面写要执行的代码，如下载数据
						return msg1;
					}

					@Override
					public <T> void update(T obj) {
						AbToastUtil.showToast(ThreadControlActivity.this,"执行完成1,"+(String)obj);
					}

				});
				
				AbTaskItem item2 = new AbTaskItem();
				item2.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbToastUtil.showToast(ThreadControlActivity.this,"执行完成2");
						AbDialogUtil.removeDialog(ThreadControlActivity.this);
					}

					@Override
					public void get() {
			   		    try {
			   		    	String msg1 = "amsoft";
			   		    	Thread.sleep(2000);
			   		    	AbToastUtil.showToastInThread(ThreadControlActivity.this,"开始执行2");
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				
				//开始执行
				mAbTaskQueue.execute(item1);
				mAbTaskQueue.execute(item2);
				
				//强制停止
				//mAbTaskQueue.cancel(true);
				
				//强制停止前面的请求
				//mAbTaskQueue.execute(item2,true);
			}
        	
        });
        
        
        //线程池
        poolBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				//显示进度框
				AbDialogUtil.showProgressDialog(ThreadControlActivity.this,R.drawable.progress_circular,"正在查询...");
				AbTaskPool mAbTaskPool = AbTaskPool.getInstance();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(ThreadControlActivity.this);
						AbToastUtil.showToast(ThreadControlActivity.this,"执行完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	AbToastUtil.showToastInThread(ThreadControlActivity.this,"开始执行");
			   		    	Thread.sleep(1000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
				//开始执行
				mAbTaskPool.execute(item);
			}
        	
        });
        
        //异步任务(void)
        taskBtn1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AbDialogUtil.showProgressDialog(ThreadControlActivity.this,R.drawable.progress_circular,"正在查询...");
				AbTask task = AbTask.newInstance();
				//定义异步执行的对象
		    	final AbTaskItem item = new AbTaskItem();
				item.setListener(new AbTaskListener() {

					@Override
					public void update() {
						AbDialogUtil.removeDialog(ThreadControlActivity.this);
						AbToastUtil.showToast(ThreadControlActivity.this,"执行完成");
					}

					@Override
					public void get() {
			   		    try {
			   		    	AbToastUtil.showToastInThread(ThreadControlActivity.this,"开始执行");
			   		    	Thread.sleep(3000);
			   		    	//下面写要执行的代码，如下载数据
			   		    } catch (Exception e) {
			   		    }
				  };
				});
		        task.execute(item);
			}
        	
        });
        
        //异步任务(对象)
        taskBtn2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AbDialogUtil.showProgressDialog(ThreadControlActivity.this,R.drawable.progress_circular,"正在查询...");
			    //执行任务
			    loadObjectDataTask();
			}
        	
        });
        
    }
    
    
    private void initTitleRightLayout(){
    	mAbTitleBar.clearRightView();
    }

	@Override
	protected void onResume() {
		super.onResume();
		initTitleRightLayout();
	}
	
	public void onPause() {
		super.onPause();
	}
	
	public void loadObjectDataTask(){
        AbTask task = new AbTask();
        final AbTaskItem item = new AbTaskItem();
        item.setListener(new AbTaskObjectListener(){

            @Override
            public <T> void update(T entity) {
                AbDialogUtil.removeDialog(ThreadControlActivity.this);
                AbToastUtil.showToast(ThreadControlActivity.this,(String)entity);
                Log.d("TAG", "执行完成:"+(String)entity);
            }

            @SuppressWarnings("unchecked")
            @Override
            public String getObject() {
                String result = null;
                try {
                    Thread.sleep(3000);
                    result = "hello andbase";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }
            
        });
        
        task.execute(item);
        
        //task.cancel(mayInterruptIfRunning)
    }


	@Override
	public void finish() {
		if(mAbTaskQueue!=null){
			mAbTaskQueue.cancel(true);
		}
		super.finish();
	}
	
	
   
}


