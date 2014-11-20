package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ab.activity.AbActivity;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.demo.dao.UserInsideDao;
import com.andbase.demo.model.LocalUser;
import com.andbase.demo.model.Stock;
import com.andbase.global.MyApplication;
/**
 * 名称：DBOne2ManyActivity
 * 描述：一对多关联
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class DBOne2ManyActivity extends AbActivity {
	
	private MyApplication application;
	//定义数据库操作实现类
	private UserInsideDao userDao = null;
	private TextView sourseData  = null;
	private TextView resultData = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.db_show);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.db_one2many_name);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        Button insertBtn  = (Button)this.findViewById(R.id.insertBtn);
        Button queryBtn  = (Button)this.findViewById(R.id.queryBtn);
        Button deleteBtn  = (Button)this.findViewById(R.id.deleteBtn);
        sourseData  = (TextView)this.findViewById(R.id.sourseData);
        resultData  = (TextView)this.findViewById(R.id.resultData);
        
        //初始化数据库操作实现类
	    userDao = new UserInsideDao(DBOne2ManyActivity.this);
	    
	    //测试数据
		final LocalUser mLocalUser  = new LocalUser();
		mLocalUser.setuId("100");
		mLocalUser.setName("测试内容");
		
		final Stock mStock1 = new Stock();
		mStock1.setuId("100");
		mStock1.setText1("关联内容1");
		
		final Stock mStock2 = new Stock();
		mStock2.setuId("100");
		mStock2.setText1("关联内容2");
		
		List<Stock> stocks = new ArrayList<Stock>();
		
		stocks.add(mStock1);
		stocks.add(mStock2);
		
		mLocalUser.setStocks(stocks);
		
        insertBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//显示插入的数据
				sourseData.setText("插入数据：");
				sourseData.append("\nlocal_user{\n");
				sourseData.append("uId:"+mLocalUser.get_id());
				sourseData.append(",name:"+mLocalUser.getName());
				sourseData.append(",\nstocks:{");
				sourseData.append("\n{uId:"+mStock1.getuId());
				sourseData.append(",text1:"+mStock1.getText1());
				sourseData.append("}");
				
				sourseData.append(",\n{uId:"+mStock2.getuId());
				sourseData.append(",text1:"+mStock2.getText1());
				sourseData.append("}");
				sourseData.append("\n}\n}");
				
				//保存
				//(1)获取数据库
				userDao.startWritableDatabase(false);
				//(2)执行查询
				long id = userDao.insert(mLocalUser);
				//(3)关闭数据库
				userDao.closeDatabase();
				
			}
		});
        
        queryBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				queryData();
			}
        });
        
        
        deleteBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//保存
				//(1)获取数据库
				userDao.startWritableDatabase(false);
				//(2)执行查询
				userDao.deleteAll();
				//(3)关闭数据库
				userDao.closeDatabase();
			}
		});
       
       
      } 
    
     public void queryData(){
    	    //查询出结果检查是否成功了
			userDao.startReadableDatabase();
			List<LocalUser>  mLocalUserList = userDao.queryList();
			userDao.closeDatabase();
			resultData.setText("查询结果为：");
			if(mLocalUserList==null || mLocalUserList.size()==0){
				resultData.append("查询结果为：0");
			}else{
				for(LocalUser localUser:mLocalUserList){
					resultData.append("\nlocal_user{\n_id:"+localUser.get_id()+",uId:"+localUser.getuId()+",name:"+localUser.getName());
					
					List<Stock> stocks = localUser.getStocks();
					if(stocks!=null && stocks.size()>0){
						resultData.append(",\nstocks{");
						for(int i=0;i<stocks.size();i++){
							Stock stock = stocks.get(i);
							if(stock!=null){
                                if(i!=0){
                                	resultData.append(",");
								}
								resultData.append("\n{_id:"+stock.get_id()+",uId:"+stock.getuId()+",text1:"+stock.getText1()+"}");
								
							}
						}
						resultData.append("\n}");
					}
					resultData.append("\n}");
					
					resultData.append("\n-------------------------");
				}
			}
     }
    
}
