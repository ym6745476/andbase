package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.view.chart.ChartFactory;
import com.ab.view.chart.PointStyle;
import com.ab.view.chart.TimeSeries;
import com.ab.view.chart.XYMultipleSeriesDataset;
import com.ab.view.chart.XYMultipleSeriesRenderer;
import com.ab.view.chart.XYSeriesRenderer;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;

/**
 * © 2012 amsoft.cn。
 * 名称：XYLineChartActivity
 * 描述：线图
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class TimeChartActivity extends AbActivity {
	
	private MyApplication application;
	
	private static final long HOUR = 3600 * 1000;
	private static final long DAY = HOUR * 24;
	private static final int HOURS = 24;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.chart);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.chart_line);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        
    	//要显示图形的View
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chart01);
		//说明文字
		String[] titles = new String[] { "第一条线", "第二条线" };
		
		List<Date[]> x = new ArrayList<Date[]>();
		List<double[]> values = new ArrayList<double[]>();
		
	    long now = Math.round(new Date().getTime() / DAY) * DAY;
	    for (int i = 0; i < titles.length; i++) {
	      Date[] dates = new Date[HOURS];
	      for (int j = 0; j < HOURS; j++) {
	        dates[j] = new Date(now - (HOURS - j) * HOUR);
	      }
	      x.add(dates);
	    }

	    values.add(
	    new double[] { 15, 16, 17, 18, 19, 20, 19, 18, 17, 16, 15,15, 16, 17, 18, 19, 20, 19, 18, 17, 16, 15,16,17});
	    values.add(
	    new double[] {0, 1, 2, 3, 4, 5, 4,3,2,1,0,0, 1, 2, 3, 4, 5, 4,3,2,1,0,1,2});

	    int[] mSeriescolors = new int[] { Color.rgb(153, 204, 0),Color.rgb(51, 181, 229) };
	    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
	    
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    int length = mSeriescolors.length;
	    for (int i = 0; i < length; i++) {
		      XYSeriesRenderer r = new XYSeriesRenderer();
		      r.setColor(mSeriescolors[i]);
		      r.setPointStyle(styles[i]);
		      r.setLineWidth(1);
		      r.setFillPoints(true);
			  r.setChartValuesTextSize(16);
			  
		      renderer.addSeriesRenderer(r);
	    }
	    //点的大小
	    renderer.setPointSize(2f);
	    //坐标轴标题文字大小
  		renderer.setAxisTitleTextSize(16);
  		//图形标题文字大小
  		renderer.setChartTitleTextSize(25);
  		//轴线上标签文字大小
  		renderer.setLabelsTextSize(15);
  		//说明文字大小
  		renderer.setLegendTextSize(15);
	    //图表标题
	    renderer.setChartTitle("我是图表的标题");
	    //X轴标题
	    renderer.setXTitle("X轴");
	    //Y轴标题
	    renderer.setYTitle("Y轴");
	    //X轴最小坐标点
	    renderer.setXAxisMin(x.get(0)[0].getTime());
	    //X轴最大坐标点
	    renderer.setXAxisMax(x.get(0)[HOURS - 1].getTime());
	    //Y轴最小坐标点
	    renderer.setYAxisMin(-5);
	    //Y轴最大坐标点
	    renderer.setYAxisMax(30);
	    //坐标轴颜色
	    renderer.setAxesColor(Color.rgb(51, 181, 229));
	    renderer.setXLabelsColor(Color.rgb(51, 181, 229));
	    renderer.setYLabelsColor(0,Color.rgb(51, 181, 229));
	    //设置图表上标题与X轴与Y轴的说明文字颜色
	    renderer.setLabelsColor(Color.GRAY);
	    //renderer.setGridColor(Color.GRAY);
	    //设置字体加粗
		renderer.setTextTypeface("sans_serif", Typeface.BOLD);
		//设置在图表上是否显示值标签
	    renderer.getSeriesRendererAt(0).setDisplayChartValues(true);
	    renderer.getSeriesRendererAt(1).setDisplayChartValues(true);
	    renderer.setMargins(new int[] { 20, 30, 15, 20 });
	    renderer.setXLabels(6);
	    renderer.setYLabels(10);
	    renderer.setShowGrid(true);
	    renderer.setXLabelsAlign(Align.CENTER);
	    renderer.setYLabelsAlign(Align.RIGHT);
	    
	    // 创建渲染器数据填充器
		XYMultipleSeriesDataset mXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
	    for (int i = 0; i < length; i++) {
		      TimeSeries series = new TimeSeries(titles[i]);
		      Date[] xV = x.get(i);
		      double[] yV = values.get(i);
		      int seriesLength = xV.length;
		      for (int k = 0; k < seriesLength; k++) {
		        series.add(xV[k], yV[k]);
		      }
		      mXYMultipleSeriesDataset.addSeries(series);
	    }
	    
	    //Y刻度标签相对Y轴位置
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(true, true);
	    renderer.setZoomEnabled(true);
	    renderer.setZoomRate(1.1f);
	    renderer.setBarSpacing(0.5f);
	    
	    //标尺开启
	    renderer.setScaleLineEnabled(true);
	    //设置标尺提示框高
	    renderer.setScaleRectHeight(60);
	    //设置标尺提示框宽
	    renderer.setScaleRectWidth(150);
	    //设置标尺提示框背景色
	    renderer.setScaleRectColor(Color.argb(150, 52, 182, 232));
	    renderer.setScaleLineColor(Color.argb(175, 150, 150, 150));
	    renderer.setScaleCircleRadius(35);
	    //第一行文字的大小
	    renderer.setExplainTextSize1(20);
	    //第二行文字的大小
	    renderer.setExplainTextSize2(20);
	    
	    
	    double[] limit = new double[]{0,5,15,20};
		renderer.setmYLimitsLine(limit);
		int[] colorsLimit = new int[] { Color.rgb(221, 241,248),Color.rgb(221, 241,248),Color.rgb(233, 242,222),Color.rgb(233, 242,222) };
		renderer.setmYLimitsLineColor(colorsLimit);
	    
	    //背景
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.rgb(222, 222, 200));
	    renderer.setMarginsColor(Color.rgb(222, 222, 200));
	    
	    //线图
	    View chart = ChartFactory.getTimeChartView(this,mXYMultipleSeriesDataset,renderer,"MM-dd HH:mm");
        linearLayout.addView(chart);
		
      } 
    
}
