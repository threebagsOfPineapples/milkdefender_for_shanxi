package com.magispec.shield.achart;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.json.JSONException;
import org.json.JSONObject;

import com.magispec.shield.activity.MainActivity;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.fragment.FragmentDetection;
import com.magispec.shield.https.CloudComm;
import com.magispec.shield.service.BaseApplicaton;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.ToastUtil;
import com.magispec.shield.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.os.Message;

import java.util.Arrays;

import okhttp3.Call;

public class CubicLineChart implements AChartAbstract {
    private double[] sample = Constant.SampleCurve;
    private double[] dark = Constant.StandardCurve;
    ;
    private double maxa = -600000;
    private double mina = 600000;

    public Intent getIntent(Context context) {
        System.out.println("我要画曲线了" + sample.length);
        XYMultipleSeriesDataset lineDataset = new XYMultipleSeriesDataset();
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer render = new XYSeriesRenderer();
        if (sample != null) {
            System.out.println("sample不为null" + sample[0]);
            XYSeries Series = new XYSeries("测试曲线");
            XYSeriesRenderer r = new XYSeriesRenderer();
            maxa = Utils.getMax(sample);
            mina = Utils.getMin(sample);
            for (int i = 1; i < sample.length - 1; i++) {
                Series.add(i, sample[i]);
                System.out.println("sample[i]:" + sample[i]);
            }
            r.setColor(Color.YELLOW);
            r.setPointStyle(PointStyle.CIRCLE);
            r.setFillPoints(true);
            r.setLineWidth(3.0f);
            renderer.addSeriesRenderer(r);
            lineDataset.addSeries(Series);
        } else {
            System.out.println("sample为null");
        }
        if (dark != null) {
            XYSeries Series = new XYSeries("标准曲线");
            XYSeriesRenderer r = new XYSeriesRenderer();
            if (maxa < Utils.getMax(dark)) {
                maxa = Utils.getMax(dark);

            }
            if (mina > Utils.getMin(dark)) {
                mina = Utils.getMin(dark);

            }

            for (int i = 0; i < dark.length; i++) {
                System.out.println("dark[i]:" + dark[i]);

                Series.add(i, dark[i]);
            }
            r.setColor(Color.GREEN);
            r.setPointStyle(PointStyle.DIAMOND);
            r.setFillPoints(true);
            r.setLineWidth(4.0f);
            renderer.addSeriesRenderer(r);
            lineDataset.addSeries(Series);
        }
        //renderer.setXTitle("波长");
        renderer.setYTitle("吸收度");


        renderer.setYAxisMax(maxa +( (Math.abs(maxa) +Math.abs(mina) ) /7 ));
        renderer.setYAxisMin(mina -( (Math.abs(maxa) +Math.abs(mina) ) / 7));


        renderer.setAxesColor(Color.WHITE);
        renderer.setLabelsColor(Color.WHITE);
        //设置显示字体
        renderer.setAxisTitleTextSize(40);
        renderer.setLegendTextSize(40);
        renderer.setMargins(new int[]{0, 60, 80, 0});
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setYLabelsColor(0, Color.BLACK);
        // X轴的近似坐标数
        renderer.setXLabels(10);
        renderer.setYLabels(6);
        // Y轴的近似坐标数
        // 设置渲染器允许放大缩小
        renderer.setZoomEnabled(false, false);
        renderer.setPanEnabled(false, false);
        renderer.setLabelsTextSize(20);
        // 消除锯齿
        renderer.setAntialiasing(true);
        // 设置背景颜色
        renderer.setApplyBackgroundColor(true);
        renderer.setBackgroundColor(Color.BLACK);
        //是否显示网格
        renderer.setShowGrid(true);
        Intent mIntent = ChartFactory.getCubicLineChartIntent(context, lineDataset, renderer, 0);
        return mIntent;
    }
}
