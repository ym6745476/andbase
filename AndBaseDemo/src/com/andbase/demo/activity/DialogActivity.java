package com.andbase.demo.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.ab.activity.AbActivity;
import com.ab.fragment.AbAlertDialogFragment.AbDialogOnClickListener;
import com.ab.fragment.AbDialogFragment;
import com.ab.fragment.AbDialogFragment.AbDialogOnLoadListener;
import com.ab.fragment.AbLoadDialogFragment;
import com.ab.fragment.AbRefreshDialogFragment;
import com.ab.http.AbHttpUtil;
import com.ab.http.AbStringHttpResponseListener;
import com.ab.util.AbDialogUtil;
import com.ab.util.AbToastUtil;
import com.ab.view.ioc.AbIocView;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

/**
 * 名称：DemoAbActivity 描述：AbActivity基本用法
 * 
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class DialogActivity extends AbActivity {

	private MyApplication application;
	private AbHttpUtil httpUtil;

	
    @AbIocView(id = R.id.button2,click="btnClick")Button button2;
    @AbIocView(id = R.id.button3,click="btnClick")Button button3;
    @AbIocView(id = R.id.button4,click="btnClick")Button button4;
    @AbIocView(id = R.id.button5,click="btnClick")Button button5;
    @AbIocView(id = R.id.button6,click="btnClick")Button button6;
    @AbIocView(id = R.id.button7,click="btnClick")Button button7;
    @AbIocView(id = R.id.button8,click="btnClick")Button button8;
    @AbIocView(id = R.id.button9,click="btnClick")Button button9;
    @AbIocView(id = R.id.button10,click="btnClick")Button button10;
    @AbIocView(id = R.id.button11,click="btnClick")Button button11;
    @AbIocView(id = R.id.button12,click="btnClick")Button button12;
    @AbIocView(id = R.id.button13,click="btnClick")Button button13;
    @AbIocView(id = R.id.button14,click="btnClick")Button button14;
    @AbIocView(id = R.id.button15,click="btnClick")Button button15;
    @AbIocView(id = R.id.button16,click="btnClick")Button button16;
    @AbIocView(id = R.id.button17,click="btnClick")Button button17;
    @AbIocView(id = R.id.button18,click="btnClick")Button button18;
    @AbIocView(id = R.id.button19,click="btnClick")Button button19;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setAbContentView(R.layout.dialog_main);

		AbTitleBar mAbTitleBar = this.getTitleBar();
		mAbTitleBar.setTitleText(R.string.dialog_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_back);
		mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
		mAbTitleBar.setLogoLine(R.drawable.line);

		application = (MyApplication) abApplication;
		httpUtil = AbHttpUtil.getInstance(this);
	}

	/**
	 * 下载数据
	 * 
	 * @param mDialogFragment
	 */
	public void downRss(final AbDialogFragment mDialogFragment) {
		// 一个url地址
		String urlString = "http://www.amsoft.cn/rss.php";
		httpUtil.get(urlString, new AbStringHttpResponseListener(){

			@Override
			public void onSuccess(int statusCode, String content) {
				
				mDialogFragment.loadFinish();
				AbDialogUtil.showAlertDialog(DialogActivity.this,
						R.drawable.ic_alert, "返回结果", content,
						new AbDialogOnClickListener() {

							@Override
							public void onPositiveClick() {
								AbToastUtil.showToast(DialogActivity.this,
										"点击了确认");

							}

							@Override
							public void onNegativeClick() {
								AbToastUtil.showToast(DialogActivity.this,
										"点击了取消");

							}

						});
			}

			@Override
			public void onStart() {
				
			}

			@Override
			public void onFinish() {
				
			}

			@Override
			public void onFailure(int statusCode, String content,
					Throwable error) {
				mDialogFragment.loadStop();

				// 模拟用，真是开发中需要直接调用run内的内容
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {
						// 显示重试的框
						showRefreshDialog();
					}

				}, 3000);

				// 错误提示
				AbToastUtil.showToast(DialogActivity.this, content);
			}
			
		});
				
		
	}

	/**
	 * 显示刷新弹出框有背景层
	 */
	public void showLoadDialog() {

		final AbLoadDialogFragment mDialogFragment = AbDialogUtil
				.showLoadDialog(this, R.drawable.ic_load, "正在查询,请稍候");
		mDialogFragment.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 下载网络数据
				downRss(mDialogFragment);
			}

		});
		// 取消的监听
		mDialogFragment.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				AbToastUtil.showToast(DialogActivity.this, "Load框被取消");
			}

		});
	}

	/**
	 * 显示加载弹出框无背景层
	 */
	public void showLoadPanel() {

		final AbLoadDialogFragment mDialogFragment = AbDialogUtil
				.showLoadDialog(this, R.drawable.ic_load, "正在查询,请稍候",AbDialogUtil.ThemeLightPanel);
		mDialogFragment.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 下载网络数据
				downRss(mDialogFragment);
			}

		});
		// 取消的监听
		mDialogFragment.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				AbToastUtil.showToast(DialogActivity.this, "Load框被取消");
			}

		});
	}

	/**
	 * 显示刷新弹出框有背景层
	 */
	public void showRefreshDialog() {
		// 显示重新刷新的框
		final AbRefreshDialogFragment mDialogFragment = AbDialogUtil
				.showRefreshDialog(this, R.drawable.ic_refresh, "请求出错，请重试");
		mDialogFragment.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 下载网络数据
				downRss(mDialogFragment);
			}

		});
		// 取消的监听
		mDialogFragment.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				AbToastUtil.showToast(DialogActivity.this, "refresh框被取消");
			}

		});
	}

	/**
	 * 显示刷新弹出框无背景层
	 */
	public void showRefreshPanel() {
		// 显示重新刷新的框
		final AbRefreshDialogFragment mDialogFragment = AbDialogUtil
				.showRefreshDialog(this, R.drawable.ic_refresh, "请求出错，请重试",AbDialogUtil.ThemeLightPanel);
		mDialogFragment.setAbDialogOnLoadListener(new AbDialogOnLoadListener() {

			@Override
			public void onLoad() {
				// 下载网络数据
				downRss(mDialogFragment);
			}

		});

		mDialogFragment.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				AbToastUtil.showToast(DialogActivity.this, "load框被取消");
			}

		});
	}
	
	
	public void btnClick(View v){
		View mView = null;
		switch (v.getId()) {
		case R.id.button2:
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showDialog(mView,
			new DialogInterface.OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					AbToastUtil.showToast(DialogActivity.this,
							"弹出框被取消");
				}
			});
			break;
		case R.id.button3:
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showDialog(mView,AbDialogUtil.ThemeLightPanel,
				new DialogInterface.OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						AbToastUtil.showToast(DialogActivity.this,
								"弹出框被取消");
					}
			});
			break;
		case R.id.button4:
			// 显示有背景层的加载的弹出框
			showLoadDialog();
			break;
		case R.id.button5:
			// 显示无背景层的加载的弹出框
			showLoadPanel();
			break;
		case R.id.button6:
			// 显示有背景层的刷新的弹出框
			showRefreshDialog();
			break;
		case R.id.button7:
			// 显示无背景层的刷新的弹出框
			showRefreshPanel();
			break;
		case R.id.button8:
			AbDialogUtil.showAlertDialog(DialogActivity.this,
				R.drawable.ic_alert, "这里是标题", "这里写一些描述",
				new AbDialogOnClickListener() {

					@Override
					public void onPositiveClick() {
						AbToastUtil.showToast(DialogActivity.this,
								"点击了确认");

					}

					@Override
					public void onNegativeClick() {
						AbToastUtil.showToast(DialogActivity.this,
								"点击了取消");

					}
			});
			break;
		case R.id.button9:
			AbDialogUtil.showAlertDialog(DialogActivity.this,
					R.drawable.ic_alert, "这里是标题", "这里写一些描述",
					null);
			break;
		case R.id.button10:
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showAlertDialog(mView);
			break;
		case R.id.button11:
			//无按钮＋动画
			mView = mInflater.inflate(R.layout.dialog_text,null);
			AbDialogUtil.showDialog(mView,R.animator.fragment_top_enter,R.animator.fragment_top_exit,R.animator.fragment_pop_top_enter,R.animator.fragment_pop_top_exit);
			break;
		case R.id.button12:
			//按钮＋列表
			mView = mInflater.inflate(R.layout.dialog_button_listview,null);
			AbDialogUtil.showDialog(mView,R.animator.fragment_top_enter,R.animator.fragment_top_exit,R.animator.fragment_pop_top_enter,R.animator.fragment_pop_top_exit);
			ListView listView = (ListView)mView.findViewById(R.id.list);
			String[] mStrings = {
		            "对话框选项item1", "对话框选项item2", "对话框选项item3", "对话框选项item4"};
			listView.setAdapter(new ArrayAdapter<String>(this,
		               R.layout.dialog_list_item_1, mStrings));
			Button leftBtn = (Button)mView.findViewById(R.id.left_btn);
			Button rightBtn = (Button)mView.findViewById(R.id.right_btn);
			leftBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					AbDialogUtil.removeDialog(DialogActivity.this);
				}
				
			});
			
			rightBtn.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					AbDialogUtil.removeDialog(DialogActivity.this);
				}
				
			});
			
			break;
		case R.id.button13:
			//按钮＋文本
			//动画未生效
			mView = mInflater.inflate(R.layout.dialog_text_button,null);
			AbDialogUtil.showDialog(mView,R.animator.fragment_top_enter,R.animator.fragment_top_exit,R.animator.fragment_pop_top_enter,R.animator.fragment_pop_top_exit);
			Button leftBtn1 = (Button)mView.findViewById(R.id.left_btn);
			Button rightBtn1 = (Button)mView.findViewById(R.id.right_btn);
			leftBtn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					AbDialogUtil.removeDialog(DialogActivity.this);
				}
				
			});
			
			rightBtn1.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					AbDialogUtil.removeDialog(DialogActivity.this);
				}
				
			});
			break;
			
		case R.id.button14:
			//上
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showDialog(mView,Gravity.TOP);
			break;
		case R.id.button15:
			//中
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showDialog(mView,Gravity.CENTER);
			break;
		case R.id.button16:
			//下
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showDialog(mView,Gravity.BOTTOM);
			break;
		case R.id.button17:
			//全屏
			mView = mInflater.inflate(R.layout.dialog_custom_view,null);
			AbDialogUtil.showFullScreenDialog(mView);
			break;
		case R.id.button18:
			AbDialogUtil.showProgressDialog(DialogActivity.this, 0,
					"查询中...");
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					AbDialogUtil.removeDialog(DialogActivity.this);
				}
			}, 2000);
			break;
		case R.id.button19:
			AbToastUtil.showToast(DialogActivity.this, "Toast提示框");
			break;
		default:
			break;
		}
	}

}
