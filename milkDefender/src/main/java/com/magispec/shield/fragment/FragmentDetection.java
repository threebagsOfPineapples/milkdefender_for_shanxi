package com.magispec.shield.fragment;

import java.net.URLEncoder;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.zip.CRC32;

import org.json.JSONException;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.magispec.shield.R;
import com.magispec.shield.achart.CubicLineChart;
import com.magispec.shield.activity.MainActivity;
import com.magispec.shield.activity.PieActicity;
import com.magispec.shield.activity.WXLoginActivity;
import com.magispec.shield.ble.BluetoothLeService;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.https.CloudComm;
import com.magispec.shield.network.BitmapCache;
import com.magispec.shield.network.HttpsTrustManager;
import com.magispec.shield.service.BaseApplicaton;
import com.magispec.shield.utils.ToastUtil;
import com.magispec.shield.utils.WxUtils;
import com.magispec.shield.widgets.CustomAlertDialog;
import com.magispec.shield.widgets.CustomDialog;
import com.magispec.shield.widgets.CustomDialogAgain;
import com.magispec.shield.widgets.ShareDialog;
import com.magispec.shield.widgets.ShareDialog.OnWindowItemClickListener;
import com.squareup.picasso.Picasso;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
public class FragmentDetection extends Fragment implements OnClickListener {
	private static LinearLayout result_layout;
	private static LinearLayout click_ll, ll_wait, ll_keep;
	private static LinearLayout loading_layout, content1, content2, content3, content4;
	private ImageView iv_share_wx1, iv_share_wx2;
	private static TextView tv_scan_milkDescription;
	private static TextView tv_milkDescription, tv_result, tv_environmentCalibration, tv_certain1,
			tv_certain2,tv_certain3, tv_certain4,tv_click;
	private ImageView iv_scanMilk;
	TextView iv_testAgain;
	private static ImageView iv1, iv2, iv3, iv11, result_circle, negativeButton,result_click;
	private ImageView iv_share;
	private ImageView iv_view;
	public static double[] Absorbancy;
	public static String path;
	String resultString;
	private PopupWindow mPopupWindow;
	private LinearLayout ll_popup;
	private static CustomDialog cd;
	boolean a = false;
	private int[] halfRef;
	private static int[] sample;
	private int[] dark;
	public static BluetoothLeService mBluetoothLeService;// 自定义的一个继承自Service的服务
	public static BluetoothGattCharacteristic mCharacteristic, mCharacteristicNotify;
	public static BluetoothGattService mnotyGattService;// 三个长得很像，由大到小的对象BluetoothGatt、
	ShareDialog sd;
	AlphaAnimation alphaAnimation_wait, alphaAnimation_keep;
	RotateAnimation animation_out, animation_mid;
	static Activity ac;
	public  static Boolean ISpausing;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_detection_test, container, false);
	}
	@Override
	public void onPause() {
		Log.d("detection:","--------onPause========="+ISpausing);
		ISpausing=true;
		//sample = null;
		super.onPause();
	}
	@Override
	public void onResume() {
		Log.d("detection:","--------------------onResume-------------------------");
		super.onResume();
	}
	@Override
	public void onStop() {
		Log.d("detection:","-------------------------onStop----------------");
		super.onStop();
	}
	@Override
	public void onStart() {
		Log.d("detection:","------------------onStart--------------------");
		super.onStart();
	}
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		ac = getActivity();
		sample = MainActivity.Sample;
		halfRef = MainActivity.HalfRef;
		dark = MainActivity.Dark;
		System.out.println("fragmentDetection------onActivityCreated------");
		ISpausing=false;
		initView();
		initData();
		sd = new ShareDialog(getContext(), R.style.MyDialog);
		Window window = sd.getWindow();
		window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置
		window.getAttributes();
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
		WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值
		p.height = (int) (d.getHeight() * 0.55); // 高度设置为屏幕的0.6
		p.width = (int) (p.height * 0.4); // 宽度设置为屏幕的0.65
		System.out.println("屏幕高和宽" + p.height + ":" + p.width);
		window.setAttributes(p);
		sd.setOnWindowItemClickListener(new OnWindowItemClickListener() {
			@Override
			public void onClickToMoments() {
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
				Constant.tm = formatter.format(curDate);
				String urlBranch = urlBranchCreate(Constant.tt, Constant.ps, Constant.tm, Constant.byps, Constant.bypc,
						Constant.tmn, Constant.tmpu, Constant.matchingDegree);
				String urlBranchCrc = urlBranchCreateCrc(Constant.tt, Constant.ps, Constant.tm, Constant.byps,
						Constant.bypc, Constant.tmn, Constant.tmpu, Constant.matchingDegree);
				CRC32 crc32 = new CRC32();
				crc32.update(urlBranchCrc.getBytes());
				Constant.cs = crc32.getValue() + "";
				System.out.println("-------urlBranchCrc--------" + urlBranchCrc + "Constant.cs:" + Constant.cs);
				String url = Constant.shareUrlHead + urlBranch + "&CS=" + Constant.cs;
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = url;
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = Constant.tt;
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
				msg.thumbData = WxUtils.bmpToByteArray(thumb, true);
				req.message = msg;
				// req.scene = SendMessageToWX.Req.WXSceneTimeline;// 分享
				req.scene = SendMessageToWX.Req.WXSceneTimeline; // api.sendReq(req);
				WXLoginActivity.WXapi.sendReq(req);
			}
			@Override
			public void onClickToFriends() {
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日    HH:mm:ss     ");
				Constant.tm = formatter.format(curDate);
				String urlBranch = urlBranchCreate(Constant.tt, Constant.ps, Constant.tm, Constant.byps, Constant.bypc,
						Constant.tmn, Constant.tmpu, Constant.matchingDegree);
				String urlBranchCrc = urlBranchCreateCrc(Constant.tt, Constant.ps, Constant.tm, Constant.byps,
						Constant.bypc, Constant.tmn, Constant.tmpu, Constant.matchingDegree);
				CRC32 crc32 = new CRC32();
				crc32.update(urlBranchCrc.getBytes());
				Constant.cs = crc32.getValue() + "";
				System.out.println("-------urlBranchCrc--------" + urlBranchCrc + "Constant.cs:" + Constant.cs);
				String url = Constant.shareUrlHead + urlBranch + "&CS=" + Constant.cs;
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = url;
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = Constant.tt;
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
				msg.thumbData = WxUtils.bmpToByteArray(thumb, true);
				req.message = msg;
				// req.scene = SendMessageToWX.Req.WXSceneTimeline;// 分享
				req.scene = SendMessageToWX.Req.WXSceneSession; // api.sendReq(req);
				WXLoginActivity.WXapi.sendReq(req);
			}
			@Override
			public void onClickToDismiss() {
				// TODO Auto-generated method stub
				sd.dismiss();
			}
		});
		// 加载popwindow
		 initpopwindow();
		if (MainActivity.isClicked == true) {
			System.out.println("MainActivity.isClicked=true");
			System.out.println("xp:我发起了采集命令");
			ll_wait.setVisibility(View.GONE);
			ll_keep.setVisibility(View.VISIBLE);
			iv1.setVisibility(View.GONE);
			iv11.setVisibility(View.VISIBLE);
			iv11.setAnimation(alphaAnimation_keep);
			iv3.setAnimation(animation_out);
			iv2.setAnimation(animation_mid);
			animation_mid.startNow();
			animation_out.startNow();
			alphaAnimation_keep.startNow();
		}else{
			{
				System.out.println("-----------------MainActivity.isClicked=false------------");
				ll_keep.setVisibility(View.GONE);
				ll_wait.setVisibility(View.VISIBLE);
				iv11.setVisibility(View.GONE);
				iv1.setVisibility(View.VISIBLE);
				iv3.setAnimation(null);
				iv2.setAnimation(null);
			}

		}
		if ( MainActivity.ISCONNECT&&MainActivity.category!=null&&MainActivity.middleUrl!=null) {
			System.out.println("执行扫描奶粉1"+"Mainactivity:"+ MainActivity.ACTION_TO_DETECTION );
			if (Constant.modelobjectNumber!=null){
				tv_scan_milkDescription.setText(MainActivity.category+"---"+Constant.modelobjectNumber);
			}else{
				tv_scan_milkDescription.setText(MainActivity.category);
			}
			Picasso.with(getContext()).load(MainActivity.middleUrl)
					.fit().into(iv_scanMilk);
		}
		if (sample != null && dark != null && halfRef != null&& MainActivity.ACTION_TO_DETECTION == true) {
			MainActivity.isClicked = false;
			if (Constant.SESSIONID != null && Constant.DARKREFID != null && Constant.OPENID != null) {
				System.out.println("开始上传sample");
				CloudComm cc = new CloudComm();
				try {
					if (Constant.ScanmilkInfo == null || Constant.ScanmilkInfo.id == null) {
						System.out.println("开始上传sampleOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
						cc.RecognizeMilk( MainActivity.mDeviceAddress,Constant.Location, Constant.DARKREFID,
								Constant.modeltype, Constant.modelid, Constant.modelobjectid
								, sample, Constant.OPENID, Constant.SESSIONID);
					} else {
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}
	private void initDialog() {
		cd = new CustomDialog(getActivity(), R.style.MyDialog);
		View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_dialog, null);
		cd.setContentView(contentView);
		tv_certain1 = (TextView) contentView.findViewById(R.id.tv_certain1);
		tv_certain2 = (TextView) contentView.findViewById(R.id.tv_certain2);
		tv_certain3 = (TextView) contentView.findViewById(R.id.tv_certain3);
		tv_certain4 = (TextView) contentView.findViewById(R.id.tv_certain4);
		content1 = (LinearLayout) contentView.findViewById(R.id.content1);
		content2 = (LinearLayout) contentView.findViewById(R.id.content2);
		content3 = (LinearLayout) contentView.findViewById(R.id.content3);
		content4 = (LinearLayout) contentView.findViewById(R.id.content4);
		negativeButton = (ImageView) contentView.findViewById(R.id.negativeButton);
		tv_certain1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!MainActivity.WRITE_FONT_BUSY) {
					ToastUtil.showToast(getContext(),"开始校准");
					MainActivity.mhander.sendEmptyMessage(Constant.TEST_DARK);
					content1.setVisibility(View.GONE);
					content2.setVisibility(View.VISIBLE);
				} else {
					ToastUtil.showToast(getContext(), "数据传输中，请稍后");
				}
			}
		});
		tv_certain3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ToastUtil.showToast(getContext(), "开始校准");
				MainActivity.mhander.sendEmptyMessage(Constant.TEST_DARK);
				content3.setVisibility(View.GONE);
				content2.setVisibility(View.VISIBLE);
			}
		});
		tv_certain4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// ToastUtil.showToast(getContext(), "");
				// MainActivity.mhander.sendEmptyMessage(Constant.TEST_DARK);
				tv_result.setText("重新检测 开始测试");
				tv_milkDescription.setText("环境校准完成,点击");
				cd.dismiss();
			}
		});
		negativeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				cd.dismiss();
			}
		});
	}
	private void initpopwindow() {
		mPopupWindow = new PopupWindow(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.share_popupwindow, null);
		ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
		int h = getActivity().getWindowManager().getDefaultDisplay().getHeight();
		int w = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		mPopupWindow.setWidth(w / 2 + 200);
		mPopupWindow.setHeight(h / 2);
		mPopupWindow.setFocusable(true);
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setContentView(view);
		mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		iv_share_wx1 = (ImageView) view.findViewById(R.id.iv_share_wx1);
		iv_share_wx2 = (ImageView) view.findViewById(R.id.iv_share_wx2);
		iv_share_wx1.setEnabled(false);
		iv_share_wx2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mPopupWindow.dismiss();
				ToastUtil.showToast(getContext(), "实验点击事件");
			}
		});
	}
	private void initView() {
		System.out.println("initview...");
		// iv_circle=(ImageView) getActivity().findViewById(R.id.iv_circle);
		iv11 = (ImageView) getActivity().findViewById(R.id.iv11);
		iv1 = (ImageView) getActivity().findViewById(R.id.iv1);
		iv2 = (ImageView) getActivity().findViewById(R.id.iv2);
		iv3 = (ImageView) getActivity().findViewById(R.id.iv3);
		ll_wait = (LinearLayout) getActivity().findViewById(R.id.ll_wait);
		ll_keep = (LinearLayout) getActivity().findViewById(R.id.ll_keep);
		result_layout = (LinearLayout) getActivity().findViewById(R.id.fragment_detection_LL_result);
		click_ll = (LinearLayout) getActivity().findViewById(R.id.fragment_detection_LL_bg);
		tv_scan_milkDescription = (TextView) getActivity().findViewById(R.id.fragment_detection_tv_scanMilkBrand);
		iv_scanMilk = (ImageView) getActivity().findViewById(R.id.fragment_detection_iv_scanMilk);
		iv_testAgain = (TextView) getActivity().findViewById(R.id.fragment_detection_iv_testAgain);
		iv_share = (ImageView) getActivity().findViewById(R.id.fragment_detection_iv_share);
		iv_view = (ImageView) getActivity().findViewById(R.id.fragment_detection_iv_view);
		tv_result = (TextView) getActivity().findViewById(R.id.fragment_detection_tv_result);
		tv_click=(TextView)getActivity().findViewById(R.id.fragment_detection_tv_click);
		result_circle = (ImageView) getActivity().findViewById(R.id.fragment_detection_iv_circle);
		result_click= (ImageView) getActivity().findViewById(R.id.fragment_detection_iv_click);
		animation_mid = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation_mid.setDuration(2500);// 设置动画持续时间
		animation_mid.setRepeatCount(1000);
		animation_mid.setInterpolator(new LinearInterpolator());
		animation_out = new RotateAnimation(360f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		animation_out.setDuration(2000);// 设置动画持续时间
		animation_out.setRepeatCount(1000);
		animation_out.setInterpolator(new LinearInterpolator());
		alphaAnimation_keep = new AlphaAnimation(0.1f, 1.0f);
		alphaAnimation_keep.setDuration(1000);
		alphaAnimation_keep.setRepeatCount(2500);
		alphaAnimation_keep.setInterpolator(new LinearInterpolator());
		alphaAnimation_keep.setRepeatCount(Animation.INFINITE);
		alphaAnimation_keep.setRepeatMode(Animation.REVERSE);
	}
	// 初始化数据
	private void initData() {
		// 设置监听事件
		System.out.println("initData");
		iv_view.setOnClickListener(this);
		click_ll.setOnClickListener(this);
		iv_testAgain.setOnClickListener(this);
		// result.setOnClickListener(this);
		iv_share.setOnClickListener(this);
		iv_view.setOnClickListener(this);
		iv1.setOnClickListener(this);
		//result_click.setOnClickListener(this);
	}
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.fragment_detection_LL_bg:
			if (true) {
				startCollect();
				System.out.print("++++++++++++++尝试+++++++++++++++++++++++==");
			} else {
				ToastUtil.showToast(getContext(), "请添加扫码目标");
			}
			break;
		case R.id.fragment_detection_iv_testAgain:
			System.out.println("测试是否可以重测");
			if (!MainActivity.WRITE_FONT_BUSY) {
				testAgain();
			} else {
				ToastUtil.showToast(getContext(), "传输数据中..请稍后..");
			}
			break;
		case R.id.fragment_detection_iv_view:
			 if (dark != null || sample != null || halfRef != null) {
			 System.out.println("我点击了曲线按钮");
			 Intent lineIntent = new CubicLineChart().getIntent(getContext());
			  startActivity(lineIntent); }
			break;
			case R.id.iv1:
				startCollect();
				System.out.print("++++++++++++++测试+++++++++++++++++++++++==");
				break;
		case R.id.fragment_detection_iv_share:
		//	sd.show();
			break;
		case R.id.fragment_detection_iv_click:
			if (!MainActivity.WRITE_FONT_BUSY) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), PieActicity.class);
				startActivity(intent);
			} else {
				ToastUtil.showToast(getContext(), "传输数据中..请稍后..");
			}
			break;
		default:
			break;
		}
	}
	private void testAgain() {
		CustomDialogAgain.Builder builder = new CustomDialogAgain.Builder(ac);
		builder.setMessage("再测一次？");
		// builder.setTitle("提示");
		builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("这是确定");
				dialog.dismiss();
				// 设置你的操作事项
			}
		});
		builder.setNegativeButton("确定", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				System.out.println("这是查看结果");
				startCollect();
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	private void startCollect() {
		if (MainActivity.ISCONNECT) {
			MainActivity.mhander.sendEmptyMessage(Constant.STRAT_COLLECT);
			System.out.println("xp:我发起了采集命令");
		//	click_ll.setEnabled(false);
			ll_wait.setVisibility(View.GONE);
			ll_keep.setVisibility(View.VISIBLE);
			iv1.setVisibility(View.GONE);
			iv11.setVisibility(View.VISIBLE);
			iv3.setAnimation(animation_out);
			iv2.setAnimation(animation_mid);
			iv11.setAnimation(alphaAnimation_keep);
		}
	}
	private static void initialize() {
		content1.setVisibility(View.GONE);
		content2.setVisibility(View.GONE);
		content3.setVisibility(View.GONE);
		content4.setVisibility(View.GONE);
	}
	/**
	 * 
	 * @param tt
	 *            标题
	 * @param pos
	 *            测试位置
	 * @param tim
	 *            测试时间
	 * @param byps
	 *            购买位置
	 * @param bypc
	 *            购买价格
	 * @param tmn
	 *            目标奶粉名称
	 * @param tmpu
	 *            目标奶粉图片链接地址
	 * @param rmd
	 *            匹配度
	 * @return
	 */
	private String urlBranchCreate(String tt, String pos, String tim, String byps, String bypc, String tmn, String tmpu,
			String rmd) {
		String url = null;
		url = Constant.shareUrlBranch.replace("tt", urlEnodeUTF8(tt));
		url = url.replace("pos", urlEnodeUTF8(pos));
		url = url.replace("tim", urlEnodeUTF8(tim));
		url = url.replace("byps", urlEnodeUTF8(byps));
		url = url.replace("bypc", urlEnodeUTF8(bypc));
		url = url.replace("tmn", urlEnodeUTF8(tmn));
		url = url.replace("tmpu", urlEnodeUTF8(tmpu));
		url = url.replace("rmd", urlEnodeUTF8(rmd));
		return url;
	}
	private String urlBranchCreateCrc(String tt, String pos, String tim, String byps, String bypc, String tmn,
			String tmpu, String rmd) {
		String url = null;
		url = Constant.shareUrlBranch.replace("tt", tt);
		url = url.replace("pos", pos);
		url = url.replace("tim", tim);
		url = url.replace("byps", byps);
		url = url.replace("bypc", bypc);
		url = url.replace("tmn", tmn);
		url = url.replace("tmpu", tmpu);
		url = url.replace("rmd", rmd);
		return url;
	}
	public static String urlEnodeUTF8(String str) {
		String result = str;
		System.out.println("---str----" + str);
		try {
			if (str != null) {
				result = URLEncoder.encode(str, "UTF-8");
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public static Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				switch (msg.what) {
					case Constant.MILLINFO:
						if (Constant.curve != null && Constant.curve != "" && Constant.degree != "") {
							if (Integer.valueOf(Constant.modeltype) == 0) {
								if (Integer.valueOf(Constant.degree) == 0) {
									//不匹配
									tv_result.setText("检测结果与目标不匹配");
									result_circle.setImageResource(R.drawable.bg_test_red);
									result_click.setImageResource(R.drawable.test_error);
								} else {
									//匹配
									tv_result.setText("检测结果与目标匹配");
									result_circle.setImageResource(R.drawable.bg_fragment_detection_iv_circle1);
									result_click.setImageResource(R.drawable.test_right);
								}
							} else if (Integer.valueOf(Constant.modeltype) == 1) {
								Constant.StandardCurve=null;
								tv_result.setText(MainActivity.category + "的含量为：");
								result_click.setVisibility(View.GONE);
								tv_click.setVisibility(View.VISIBLE);
								tv_click.setText(Constant.degree+"%");
							}
							sample=null;
							Constant.curve = "";
							Constant.degree = "";
							MainActivity.ACTION_TO_DETECTION = false;
							//click_ll.setEnabled(true);
							click_ll.setVisibility(View.GONE);
							result_layout.setVisibility(View.VISIBLE);
							//MainActivity.mhander.sendEmptyMessage(Constant.MilkName);
						}
				}
			}
		}
		};
	}




