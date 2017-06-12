package com.magispec.shield.activity;

import java.util.ArrayList;

import org.json.JSONException;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.MilkInfo;
import com.magispec.shield.https.CloudComm;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
public class PieActicity extends Activity {
	private Integer chartLength;
	private ArrayList<Entry> entryArrayList;
	public static ArrayList<Entry> values;
	public static ArrayList<Integer> colors;
	public static String[] label;
	public static CloudComm cc;
	public static PieChart pc;
	private ImageView title_back;
	private ImageView iv1,iv2,iv3,iv4;
	private TextView tv1,tv2,tv3,tv4;
	private static ProgressDialog dialog;
	private Handler scaleHandler = new Handler();
	private Runnable scaleRunnable = new Runnable() {
		@Override
		public void run() {
			setPipscale(); // 具体的处理函数，里面有获取X,Y，宽高等信息，之前是直接在onResume中被调用。
		}
	};
	// 画圆所在的距形区域
	RectF oval;
	Paint paint;
	Canvas c;
	public static MilkInfo milk1 = null;
	// public static String MIlk2=null;
	public static ArrayList<String> labels;
	public static ArrayList<String> labelvalue;
	public static PieData mPieData;
	public static Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				switch (msg.what) {
				case 0x01:
					labels.add(milk1.getShortName());
					colors.add(Color.rgb(148, 212, 212));
					values.add(new Entry(Integer.valueOf(label[1]), 1));
					if (label.length <= 4) {
						try {
							cc.getMilkInfo((byte) 0x01, label[2], Constant.OPENID, Constant.SESSIONID);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						mhandler.sendEmptyMessage(0x03);
					}
					break;
				case 0x02:
					colors.add(Color.rgb(136, 180, 187));
					labels.add(milk1.getShortName());
					values.add(new Entry(Integer.valueOf(label[3]), 2));
					mhandler.sendEmptyMessage(0x03);
					break;
				case 0x03:
					showChart(pc, mPieData);
					milk1 = null;
					dialog.dismiss();
				default:
					break;
				}
			}
		};

	};
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cc = new CloudComm();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_piechart);
		initWindow();
		iv1=(ImageView) findViewById(R.id.piechart_iv1);
		iv2=(ImageView) findViewById(R.id.piechart_iv2);
		iv3=(ImageView) findViewById(R.id.piechart_iv3);
		iv4=(ImageView) findViewById(R.id.piechart_iv4);
		tv1=(TextView) findViewById(R.id.piechart_tv1);
		tv2=(TextView) findViewById(R.id.piechart_tv2);
		tv3=(TextView) findViewById(R.id.piechart_tv3);
		tv4=(TextView) findViewById(R.id.piechart_tv4);
		title_back = (ImageView) findViewById(R.id.title_back_imageButton);
		title_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		label = Constant.similarity.split(" ");
		for (int i = 0; i < label.length; i++) {
			System.out.println("label[" + i + "]" + label[i]);
		}
		labels = new ArrayList<String>();
		values = new ArrayList<Entry>();
		colors = new ArrayList<Integer>();
		labelvalue=new ArrayList<>();
		pc = (PieChart) findViewById(R.id.piechart1);
		Display display = getWindowManager().getDefaultDisplay(); // 为获取屏幕宽、高
		android.view.ViewGroup.LayoutParams lp;
		lp = pc.getLayoutParams();
		lp.width = (int) (display.getWidth() * 0.8);
		lp.height = lp.width;
		pc.setLayoutParams(lp);
		dialog = new ProgressDialog(PieActicity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("请求网络中...");
		// dialog.show();
		pc.setFilterTouchesWhenObscured(false);
		//Legend legend = pc.getLegend();
		mPieData = getPieData();
		showChart(pc, mPieData);
	}
	protected void setPipscale() {
		System.out.println("pc的Center中心" + pc.getCenter() + "pc的上" + pc.getTop() + "" + pc.getBottom());
	}
	@SuppressLint("NewApi")
	private void initWindow() {
		// TODO Auto-generated method stub
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {// 4.4 全透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		}
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {// 5.0 全透明实现
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView()
					.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);// calculateStatusColor(Color.WHITE,
			// (int) alphaValue)
		}
	}
	public static void showChart(PieChart pieChart, PieData pieData) {
		pieChart.setExtraOffsets(5, 10, 5, 5);
		pieChart.setTransparentCircleRadius(64f); // 半透明圈
		// 设置表格的描述
		pieChart.setHoleRadius(30f); // 半径
		// 饼状图中间可以添加文字
		pieChart.setDrawCenterText(false);
		pieChart.setDescription(null);
		//pieChart.setDescriptionTextSize(10);
		// 饼状图中间的文字
		pieChart.setCenterText("");
		// 是否显示饼状图上的文字
		pieChart.setDrawSliceText(false);
		// 是否显示饼状图上的数值
		pieData.setDrawValues(false);
		pieData.setValueTextColor(Color.WHITE);
		// 是否是空心
		pieChart.setDrawHoleEnabled(true);
		// 可以手动旋转
		pieChart.setRotationEnabled(false);
		// 初始旋转角度
		pieChart.setRotationAngle(-90);
		// 显示成百分比, 若为false 则显示真实数据
		pieChart.setUsePercentValues(false);
		// 设置数据
		pieChart.setData(pieData);
		// 设置比例图
		Legend mLegend = pieChart.getLegend();
		mLegend.setForm(LegendForm.CIRCLE);
		// 是否显示模块提示
		mLegend.setEnabled(true);
		mLegend.setWordWrapEnabled(true);
		pieChart.animateXY(1000, 1000); // 设置动
		// 模块提示出现的位置
		mLegend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
	}

	private PieData getPieData() {
		
		
		if(label.length/2==1){
			labels.add("..."+label[1]+"%");
			values.add(new Entry(Integer.valueOf(label[1]), 0));
			colors.add(Color.rgb(102, 209, 7));
			
		}else if(label.length/2==2){
			labels.add("..."+label[1]+"%");
			values.add(new Entry(Integer.valueOf(label[1]), 0));
			colors.add(Color.rgb(102, 209, 7));
			if(label[2].equals("-1")){
				labels.add("..."+(100-Integer.valueOf(label[1]))+"%");
				values.add(new Entry(100-Integer.valueOf(label[1]), 0));
				colors.add(Color.rgb(207, 210, 213));
				
			}else{
				colors.add(Color.rgb(251, 193, 9));
				labels.add(label[3]+"%");
				values.add(new Entry(Integer.valueOf(label[3]), 0));
			}
			
		}else if(label.length/2==3){
			colors.add(Color.rgb(102, 209, 7));
			labels.add("..."+label[1]+"%");
			values.add(new Entry(Integer.valueOf(label[1]), 0));
			labels.add("..."+label[3]+"%");
			values.add(new Entry(Integer.valueOf(label[3]), 0));
			colors.add(Color.rgb(251, 193, 9));
			
			if(label[4].equals("-1")){
				labels.add("..."+(100-Integer.valueOf(label[1])-Integer.valueOf(label[3]))+"%");
				values.add(new Entry(100-Integer.valueOf(label[1])-Integer.valueOf(label[3]), 0));
				colors.add(Color.rgb(207, 210, 213));
			}else{
				labels.add("..."+label[5]+"%");
				values.add(new Entry(Integer.valueOf(label[5]), 0));
				colors.add(Color.rgb(108, 197, 205));
			}
			
		}else if(label.length/2==4){
			System.out.println("分析度为4---------------------------------------");
			colors.add(Color.rgb(102, 209, 7));
			labels.add("..."+label[1]+"%");
			values.add(new Entry(Integer.valueOf(label[1]), 0));
			
			labels.add("..."+label[3]+"%");
			values.add(new Entry(Integer.valueOf(label[3]), 0));
			colors.add(Color.rgb(251, 193, 9));
			labels.add("..."+label[5]+"%");
			values.add(new Entry(Integer.valueOf(label[5]), 0));
			colors.add(Color.rgb(108, 197, 205));
			if(label[6].equals("-1")){
				labels.add("..."+(100-Integer.valueOf(label[1])-Integer.valueOf(label[3])-Integer.valueOf(label[5]))+"%");
				values.add(new Entry(100-Integer.valueOf(label[1])-Integer.valueOf(label[3])-Integer.valueOf(label[5]), 0));
				colors.add(Color.rgb(207, 210, 213));
			}else{
				labels.add("..."+label[7]+"%");
				values.add(new Entry(Integer.valueOf(label[5]), 0));
				colors.add(Color.rgb(207, 210, 213));
			}
		}
		/*if(Constant.labels.size()==1){
			tv1.setText("测试1");
			iv1.setImageResource(R.drawable.point_green);
			
		}else if(Constant.labels.size()==2){
			
			tv1.setText("测试1");
			iv1.setImageResource(R.drawable.point_green);
			tv2.setText("测试2");
			iv2.setImageResource(R.drawable.point_grey);
			
		}else if(Constant.labels.size()==3){
			
		}else if(Constant.labels.size()==4){
		}*/
		for (int i = 0; i < Constant.labels.size(); i++) {
			labelvalue.add(Constant.labels.get(i)+labels.get(i));;
			System.out.println("条目："+Constant.labels.get(i)+labels.get(i));
		}
		//chartLength = labels.size();
		// 将一个饼形图分成四部分
		entryArrayList = values;
		PieDataSet pieDataSet = new PieDataSet(values, "");
		pieDataSet.setColors(colors);
		pieDataSet.setSliceSpace(1f);
		// pieDataSet.setLabel("奶粉名");
		return new PieData(labelvalue, pieDataSet);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getWindow().getDecorView().post(new Runnable() {

			@Override
			public void run() {
				scaleHandler.post(scaleRunnable);
			}
		});

	}
}