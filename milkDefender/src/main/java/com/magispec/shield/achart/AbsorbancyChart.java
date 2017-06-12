package com.magispec.shield.achart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.magispec.shield.fragment.FragmentDetection;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class AbsorbancyChart implements AChartAbstract {	
	private double[]absorbancy=FragmentDetection.Absorbancy; 
	@Override
	public Intent getIntent(Context context) {
		System.out.println("我要画曲线了");		
		XYMultipleSeriesDataset lineDataset = new XYMultipleSeriesDataset();  
	    XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();		
		XYSeriesRenderer render=new XYSeriesRenderer();		
		if(absorbancy!=null){
			XYSeriesRenderer r = new XYSeriesRenderer();
			XYSeries Series = new XYSeries("absorbancy");
			for (int i = 0; i < absorbancy.length; i++) {
				Series.add(i, absorbancy[i]);			
			}
		   r.setColor(Color.GREEN); 
           r.setPointStyle(PointStyle.TRIANGLE); 
           r.setFillPoints(true); 
           r.setLineWidth(5.0f);
           renderer.addSeriesRenderer(r); 
		   lineDataset.addSeries(Series);			
		}
		//renderer.setXTitle("波长"); 
	      renderer.setYTitle("吸收度"); 	      
	      renderer.setYAxisMax(0.01f);
	      renderer.setYAxisMin(-0.01f);	     
	      renderer.setAxesColor(Color.WHITE);  
	      renderer.setLabelsColor(Color.WHITE);   
	      
	      //设置显示字体
	      renderer.setAxisTitleTextSize(40); 
	      renderer.setLegendTextSize(40);
	      renderer.setMargins(new int[] { 0, 60, 80, 0 });  
	      renderer.setYLabelsAlign(Align.LEFT);
	      renderer.setYLabelsColor(0,Color.BLACK);
	      // X轴的近似坐标数           
	      renderer.setXLabels(10);
	      renderer.setYLabels(6);
	      // Y轴的近似坐标数  
	      // 设置渲染器允许放大缩小  
	      //renderer.setZoomEnabled(true);  
	      renderer.setZoomEnabled(false, true);
	      renderer.setPanEnabled(false, true);
	      renderer.setLabelsTextSize(20);	      
	      // 消除锯齿  
	      renderer.setAntialiasing(true);  
	      // 设置背景颜色          
	      renderer.setApplyBackgroundColor(true);  
	      renderer.setBackgroundColor(Color.WHITE); 	       
	      //是否显示网格
	      renderer.setShowGrid(true);		
	      Intent mIntent=ChartFactory.getCubicLineChartIntent(context, lineDataset, renderer, 0);  
	      return mIntent;
	}

}
