package com.magispec.shield.activity;

import java.util.ArrayList;

import org.json.JSONException;

import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.BaseMessage;
import com.magispec.shield.domain.MilkInfo;
import com.magispec.shield.https.CloudComm;
import com.magispec.shield.widgets.PieCustomView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PieCustomActivity extends Activity {
	private Integer chartLength;
	private LinearLayout rl;
	private PieCustomView nv;
	private ImageView iv1, iv2, iv3, iv4;
	LinearLayout ll_1,ll_2,ll_3,ll_4,ll_11;
	private ArrayList<BaseMessage> mList;
	private ArrayList<Integer> colors;
	private static ProgressDialog dialog;
	public static String[] label;
	private ImageView point1,point2,point3,point4;
	public static ArrayList<MilkInfo> milkList;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_piecustom);
		Point point = new Point();
		CloudComm cc = new CloudComm();
		nv=(PieCustomView) findViewById(R.id.nv);
		 nv.setTextColor(Color.parseColor("#000000"));
		getWindowManager().getDefaultDisplay().getRealSize(point);
		colors=new ArrayList<>();
		mList=new ArrayList<>();
		milkList=new ArrayList<>();
		dialog = new ProgressDialog(PieCustomActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("请求网络中...");
		iv1 = (ImageView) findViewById(R.id.iv1);
		iv2 = (ImageView) findViewById(R.id.iv2);
		iv3 = (ImageView) findViewById(R.id.iv3);
		iv4 = (ImageView) findViewById(R.id.iv4);
		point1=(ImageView) findViewById(R.id.point1);
		point2=(ImageView) findViewById(R.id.point2);
		point3=(ImageView) findViewById(R.id.point3);
		point4=(ImageView) findViewById(R.id.point4);
		ll_1=(LinearLayout) findViewById(R.id.linearLayout_1);
		ll_2=(LinearLayout) findViewById(R.id.ll_2);
		ll_3=(LinearLayout) findViewById(R.id.ll_3);
		ll_4=(LinearLayout) findViewById(R.id.ll_4);
		ll_11=(LinearLayout) findViewById(R.id.ll_11);
		colors.add(Color.parseColor("#7EC1BB"));
		colors.add(Color.parseColor("#F4CE79"));
		colors.add(Color.parseColor("#FCAC87"));
		colors.add(Color.parseColor("#DFDFDF"));
		nv = (PieCustomView) findViewById(R.id.nv);
		mList = new ArrayList<>();
		String []label= Constant.similarity.split(" ");
		for (int i = 0; i < label.length; i++) {
			System.out.println("label[" + i + "]" + label[i]);
		}
		BaseMessage bm1 = new BaseMessage();
		bm1.color = colors.get(0);
		bm1.percent = Float.valueOf(label[0 + 1]);		
		mList.add(bm1);
		for (int i = 1; i < label.length / 2; i++) {
			BaseMessage bm = new BaseMessage();
			bm.color = colors.get(i);
			bm.percent = Float.valueOf(label[(i + 1)*2-1]);
			mList.add(bm);
			if (label[i].equals("0")) {
				MilkInfo ml = new MilkInfo();
				ml.fullName = "非奶粉物质";
			} else if (label[i].equals("-1")) {
				MilkInfo ml = new MilkInfo();
				ml.fullName = "其他";
			} else if (label[i].equals("-2")) {
				MilkInfo ml = new MilkInfo();
				ml.fullName = "非库中奶粉";
			} else {
				try {
					cc.getMilkInfo((byte) 0x01, label[i], Constant.OPENID, Constant.SESSIONID);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		iv2.post(new Runnable() {
			public void run() {
				Point point_1 = new Point();
				point_1.set(ll_1.getLeft()+point1.getLeft(),ll_1.getTop()+ point1.getTop());
				Point point_2 = new Point();
				point_2.set(point2.getLeft() , point2.getTop()+ll_2.getTop());
				Point point_3 = new Point();
				point_3.set(ll_3.getLeft() , ll_3.getBottom());
				Point point_4 = new Point();
				point_4.set(ll_4.getLeft()+point1.getLeft(), ll_4.getBottom());
				ArrayList<Point> points = new ArrayList<>();
				points.add(point_1);
				points.add(point_2);
				points.add(point_3);
				points.add(point_4);
				nv.setCakeData(mList, points);	
		}
		});
	}
}
