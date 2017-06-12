package com.magispec.shield.fragment;

import java.util.HashMap;

import org.json.JSONException;

import com.baidu.location.LocationClient;
import com.google.gson.Gson;
import com.magispec.shield.R;
import com.magispec.shield.activity.CategoryActivity;
import com.magispec.shield.activity.MainActivity;
import com.magispec.shield.ble.BluetoothLeService;
import com.magispec.shield.ble.DeviceScanActivity;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.MilkInfo;
import com.magispec.shield.https.CloudComm;
import com.magispec.shield.service.BaseApplicaton;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.ToastUtil;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import okhttp3.Call;
public class FragmentHome extends Fragment {
	private String paired;
	private SharedPreferences sp;
	public static TextView tv_live, tv_scanResult;
	public static LinearLayout layout_detection, layout_scanCode, layout_live, layout_scanResult;
	private LinearLayout ll_home,ll_record,ll_detection,ll_discovery,ll_auth;
	private static ImageView iv_scan;
	private ImageView iv_scan_item;
	private static TextView tv_scan;
	private static View blackLine;
	public LocationClient mLocationClient = null;
	BluetoothLeService mBluetoothLeService = new BluetoothLeService();
	public static int BTstate;
	public static HashMap<String, Integer> milkData;
	public static Activity ac;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		System.out.println("onCreateView");
		return inflater.inflate(R.layout.fragment_home, container, false);
	}

	public static Handler homeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				switch (msg.what) {
				case Constant.CONN_AUTHORIZE:
					System.out.println("我得到了mainActivity的值" + MainActivity.ISCONNECT);
					homeHandler.post(new Runnable() {
						@Override
						public void run() {
							MainActivity.ISCONNECT = true;
							layout_live.setVisibility(View.GONE);
							blackLine.setVisibility(View.VISIBLE);
							System.out.println("蓝牙设备已经连接");
							// 连接设备后检测和扫码功能实现
							layout_detection.setEnabled(true);
							layout_scanCode.setEnabled(true);
						}
					});
					break;
				case Constant.BT_DISCONNECTED:
					 homeHandler.post(new Runnable() {
					 
					  @Override public void run() {
					 layout_live.setVisibility(View.VISIBLE);
					 MainActivity.ISCONNECT = false;
					 System.out.println("我尝试改变text的内容了"); // 连接中断 两者不能点击
					  layout_detection.setEnabled(false);
					layout_scanCode.setEnabled(false); } });
					break;
				case Constant.MILLINFO:
					// 尝试查找设备,规定时间内未完成的话就出现重新绑定
					/*
					 * homeHandler.post(new Runnable() {
					 * 
					 * @Override public void run() { MainActivity.ISCONNECT=
					 * true; layout_btConnectting.setVisibility(View.GONE);
					 * layout_btConnect.setVisibility(View.VISIBLE);
					 * FragmentHome.tv_btConnect.setText("点击查找设备");
					 * System.out.println("我尝试改变text的内容了"); } }); break;
					 */
					tv_scan.setText(Constant.ScanmilkInfo.getFullName());
					if (Constant.ScanmilkInfo.photoPath != null) {
						new Thread(
								new Runnable() {
									public void run() {
										System.out.println("------------子线程开启--------" + Constant.ScanmilkInfo.photoPath
												+ Constant.ScanmilkInfo.fullName);
									/*	OkHttpUtils.get().url(Constant.ScanmilkInfo.photoPath).build()
												.execute(new BitmapCallback() {
													@Override
													public void onResponse(Bitmap bitmap) {
														iv_scan.setImageBitmap(bitmap);
														tv_scan.setText(Constant.ScanmilkInfo.getFullName());
														MainActivity.mhander.sendEmptyMessage(Constant.CHECKGOOD);
														MainActivity.HAVEGOOD = true;
													}
													@Override
													public void onError(Call arg0, Exception e) {
														System.out.println("图片设置成功");
														iv_scan.setImageResource(R.drawable.fragment_home_scan_unkown);
													}
												});*/
						Picasso.with(ac).load(Constant.ScanmilkInfo.photoPath).fetch(new com.squareup.picasso.Callback() {
							@Override
							public void onSuccess() {
								Picasso.with(ac).load(Constant.ScanmilkInfo.photoPath).fit() .into(iv_scan);
								MainActivity.mhander.sendEmptyMessage(Constant.CHECKGOOD);
								MainActivity.HAVEGOOD = true;
							}
							@Override
							public void onError() {
								iv_scan.setImageResource(R.drawable.fragment_home_scan_unkown);
							}
						});}
					}
						).start();
						/*
						 * //使用Volley 缓存图片 HttpsTrustManager.allowAllSSL();
						 * RequestQueue requestQueue =
						 * Volley.newRequestQueue(BaseApplicaton.getAppContext()
						 * ); //1.实例化ImageLoader ImageLoader loader = new
						 * ImageLoader(requestQueue, new BitmapCache());
						 * //2.设置监听器 ImageLoader.ImageListener listener =
						 * ImageLoader.getImageListener(iv_scan,
						 * R.drawable.iv_loading, R.drawable.iv_load_eroor);
						 * //3.获取图片 loader.get(Constant.ScanmilkInfo.photoPath ,
						 * listener);
						 */
					} else {
						MainActivity.HAVEGOOD = false;
						System.out.println("设置未知图片");
						iv_scan.setImageResource(R.drawable.fragment_home_scan_unkown);
						MainActivity.mhander.sendEmptyMessage(Constant.CHECKGOOD);
					}
					break;
				case Constant.SCAN_OVER: // 扫描完毕 将码放在首页
					homeHandler.post(new Runnable() {
						@Override
						public void run() {

						}
					});

				default:
					break;
				}
			}
		}
	};
	/**
	 * onActivityCreated
	 */
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		System.out.println("onActivityCreated");
		ac=getActivity();
		sp = getActivity().getSharedPreferences("ISPAIRED", Context.MODE_PRIVATE);
		layout_detection = (LinearLayout) getActivity().findViewById(R.id.fragment_home_layout_detection);
		layout_scanCode = (LinearLayout) getActivity().findViewById(R.id.fragment_home_layout_scanCode);
		layout_live = (LinearLayout) getActivity().findViewById(R.id.fragment_home_bt_layout_liveView);
		tv_live = (TextView) getActivity().findViewById(R.id.fragment_home_bt_liveText);
		// 实例化布局对象
		ll_home = (LinearLayout) getActivity().findViewById(R.id.ll_home);
		ll_auth = (LinearLayout)getActivity(). findViewById(R.id.ll_auth);
		ll_record = (LinearLayout)getActivity(). findViewById(R.id.ll_record);
		ll_discovery = (LinearLayout)getActivity(). findViewById(R.id.ll_discovery);
		ll_detection = (LinearLayout)getActivity(). findViewById(R.id.ll_detection);
		blackLine = getActivity().findViewById(R.id.fragment_home_blackLine);
		iv_scan = (ImageView) getActivity().findViewById(R.id.fragment_home_iv_scan);
		tv_scan = (TextView) getActivity().findViewById(R.id.fragment_home_tv_scan);
		iv_scan_item = (ImageView) getActivity().findViewById(R.id.fragment_home_item_scanCode);
		// 判断是否扫码
		if (!MainActivity.ISCONNECT) {
			layout_live.setVisibility(View.VISIBLE);
			layout_scanCode.setEnabled(false);
			layout_detection.setEnabled(false);
		} else {
			layout_live.setVisibility(View.GONE);
		}
		// 点击跳转至搜索设备页面
		tv_live.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), DeviceScanActivity.class);
				startActivity(intent);
			}
		});
		// 扫描功能的点击事件
		layout_scanCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), CategoryActivity.class);
				startActivity(intent);
			}
		});
		// 点击跳转到检测页面
		layout_detection.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (MainActivity.HAVEGOOD) {
					FragmentDetection fragmentDetection = new FragmentDetection();
					FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
							.beginTransaction();
					// 替换当前的页面
					fragmentTransaction.replace(R.id.frame_content, fragmentDetection);
					// 事务管理提交
					fragmentTransaction.commit();
					ll_auth.setSelected(false);
					ll_detection.setSelected(true);
					ll_discovery.setSelected(false);
					ll_record.setSelected(false);
					ll_home.setSelected(false);
				} else {
					ToastUtil.showToast(getContext(), "未添加有效目标");
				}
			}
		});
	}
	@Override
	public void onStart() {
		System.out.println("fragmenthome_onstart");
		super.onStart();
	}
	@Override
	public void onResume() {
		super.onResume();
		System.out.println("fragmentHome----onResume-----");
		if(MainActivity.category!=null){
			if (Constant.modelobjectNumber!=null){
				tv_scan.setText(MainActivity.category+"---"+Constant.modelobjectNumber);
			}else{
				tv_scan.setText(MainActivity.category);
			}
		}
		if(MainActivity.middleUrl!=null){
			Picasso.with(getContext()).load(MainActivity.middleUrl)
					.fit().into(iv_scan);
		}
		if (SharePreferenceUtil.getAttributeByKey(getContext(), "SP", "ScanResult", 0) != null
				&& SharePreferenceUtil.getAttributeByKey(getContext(), "SP", "ScanResult", 0) != ""
				&& Constant.SESSIONID != null) {
			CloudComm cc = new CloudComm();
			try {
				cc.getMilkInfo((byte) 0,(String) SharePreferenceUtil.getAttributeByKey(getContext(), "SP", "ScanResult", 0),
						Constant.OPENID, Constant.SESSIONID);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String temp = (String) SharePreferenceUtil.getAttributeByKey(BaseApplicaton.getAppContext(), "SP",
				"ScanmilkInfo", SharePreferenceUtil.VALUE_IS_STRING);
		if (temp != null && temp != "") {
			Gson gson = new Gson();
			Constant.ScanmilkInfo = gson.fromJson(temp, MilkInfo.class);
			System.out.println("----马上开启子线程-----");
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("fragmentHome------OnDestory");
		super.onDestroy();
	}
}
