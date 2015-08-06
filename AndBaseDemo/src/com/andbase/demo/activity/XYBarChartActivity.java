package com.andbase.demo.activity;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.ab.activity.AbActivity;
import com.ab.view.chart.BarChart;
import com.ab.view.chart.CategorySeries;
import com.ab.view.chart.ChartFactory;
import com.ab.view.chart.PointStyle;
import com.ab.view.chart.XYMultipleSeriesDataset;
import com.ab.view.chart.XYMultipleSeriesRenderer;
import com.ab.view.chart.XYSeriesRenderer;
import com.ab.view.titlebar.AbTitleBar;
import com.andbase.R;
import com.andbase.global.MyApplication;
/**
 * © 2012 amsoft.cn。
 * 名称：XYBarChartActivity
 * 描述：柱图
 * @author 还如一梦中
 * @date 2011-12-13
 * @version
 */
public class XYBarChartActivity extends AbActivity {
	
	private MyApplication application;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setAbContentView(R.layout.chart);
        
        AbTitleBar mAbTitleBar = this.getTitleBar();
        mAbTitleBar.setTitleText(R.string.chart_bar);
        mAbTitleBar.setLogo(R.drawable.button_selector_back);
        mAbTitleBar.setTitleBarBackground(R.drawable.top_bg);
        mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
        mAbTitleBar.setLogoLine(R.drawable.line);
	    
        application = (MyApplication)abApplication;
        
    	//要显示图形的View
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chart01);
		//说明文字
		String[] titles = new String[] { "第一组", "第二组" };
		//数据
	    List<double[]> values = new ArrayList<double[]>();
	    //每个数据点的颜色
	    List<int[]> colors = new ArrayList<int[]>();
	    //每个数据点的简要 说明
	    List<String[]> explains = new ArrayList<String[]>();
	    
	    values.add(new double[] { 14230, 0, 0, 0, 15900, 17200, 12030});
	    values.add(new double[] { 5230, 0, 0, 0, 7900, 9200, 13030});
	    
	    colors.add(new int[] { Color.RED, 0, 0, 0, 0, 0, 0});
	    colors.add(new int[] { 0, 0, Color.BLUE, 0, Color.GREEN, 0, 0});
	    
	    explains.add(new String[] { "红色", "点2", "点3", "点4", "", "点6", ""});
	    explains.add(new String[] { "没有颜色", "没有颜色", "蓝色的点\n第二行的文字\n第三行的文字", "没有颜色\n第二行的文字\n第三行的文字\n第四行的文字\n第五行的文字", "没有颜色", "没有颜色", ""});
	    
	    //柱体或者线条颜色
	    int[] mSeriescolors = new int[] { Color.rgb(153, 204, 0),Color.rgb(51, 181, 229) };
	    //创建渲染器
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
	    int length = mSeriescolors.length;
	    for (int i = 0; i < length; i++) {
	      //创建SimpleSeriesRenderer单一渲染器
	      XYSeriesRenderer r = new XYSeriesRenderer();
	      //SimpleSeriesRenderer r = new SimpleSeriesRenderer();
	      //设置渲染器颜色
	      r.setColor(mSeriescolors[i]);
	      r.setFillPoints(true);
		  r.setPointStyle(PointStyle.CIRCLE);
		  r.setLineWidth(1);
		  r.setChartValuesTextSize(16);
	      //加入到集合中
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
	    renderer.setXAxisMin(0.5);
	    //X轴最大坐标点
	    renderer.setXAxisMax(7.5);
	    //Y轴最小坐标点
	    renderer.setYAxisMin(0);
	    //Y轴最大坐标点
	    renderer.setYAxisMax(24000);
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
	    //显示屏幕可见取区的XY分割数
	    renderer.setXLabels(7);
	    renderer.setYLabels(10);
	    //X刻度标签相对X轴位置
	    renderer.setXLabelsAlign(Align.CENTER);
	    //Y刻度标签相对Y轴位置
	    renderer.setYLabelsAlign(Align.LEFT);
	    renderer.setPanEnabled(true, false);
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
	    
	    //临界线
	    double[] limit = new double[]{15000,12000,4000,9000};
	    renderer.setmYLimitsLine(limit);
	    int[] colorsLimit = new int[] { Color.rgb(100, 255,255),Color.rgb(100, 255,255),Color.rgb(0, 255, 255),Color.rgb(0, 255, 255) };
	    renderer.setmYLimitsLineColor(colorsLimit);
	    
	    //显示表格线
	    renderer.setShowGrid(true);
	    
	    //如果值是0是否要显示
	    renderer.setDisplayValue0(true);
	    //创建渲染器数据填充器
	    XYMultipleSeriesDataset mXYMultipleSeriesDataset = new XYMultipleSeriesDataset();
	    for (int i = 0; i < length; i++) {
	      CategorySeries series = new CategorySeries(titles[i]);
	      double[] v = values.get(i);
	      int[] c = colors.get(i);
	      String[] e = explains.get(i);
	      int seriesLength = v.length;
	      for (int k = 0; k < seriesLength; k++) {
	    	  //设置每个点的颜色
	          series.add(v[k],c[k],e[k]);
	      }
	      mXYMultipleSeriesDataset.addSeries(series.toXYSeries());
	    }
	    //背景
	    renderer.setApplyBackgroundColor(true);
	    renderer.setBackgroundColor(Color.rgb(222, 222, 200));
	    renderer.setMarginsColor(Color.rgb(222, 222, 200));
	    
	    //线图
	    View chart = ChartFactory.getBarChartView(this,mXYMultipleSeriesDataset,renderer,BarChart.Type.DEFAULT);
        linearLayout.addView(chart);
		
      } 
    
}
