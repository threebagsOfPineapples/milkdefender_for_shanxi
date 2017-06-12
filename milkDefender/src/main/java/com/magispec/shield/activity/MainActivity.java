package com.magispec.shield.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationClientOption.AMapLocationMode;
import com.magispec.shield.R;
import com.magispec.shield.ble.BTMsg;
import com.magispec.shield.ble.BinRead;
import com.magispec.shield.ble.BluetoothLeService;
import com.magispec.shield.ble.Font;
import com.magispec.shield.ble.SampleGattAttributes;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.fragment.FragmentAuth;
import com.magispec.shield.fragment.FragmentAuth.OnDarkClickListener;
import com.magispec.shield.fragment.FragmentDetection;
import com.magispec.shield.fragment.FragmentDiscovery;
import com.magispec.shield.fragment.FragmentHome;
import com.magispec.shield.fragment.FragmentRecord;
import com.magispec.shield.https.CloudComm;
import com.magispec.shield.utils.DigitalTrans;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.TimeUtils;
import com.magispec.shield.utils.ToastUtil;
import com.magispec.shield.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.amap.api.location.AMapLocationListener;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import dmax.dialog.SpotsDialog;
import okhttp3.Call;

public class MainActivity extends FragmentActivity
		implements OnClickListener, AMapLocationListener, OnDarkClickListener {
	// 定义Fragment页面
	private final static String TAG = MainActivity.class.getSimpleName();
	public static String middleUrl;
	private static boolean IsScanCode = false;
	private boolean Connectted;
	public static int[] Ref1;
	public static int[] HalfRef;
	public static int[] Ref2;
	public static int[] Dark;
	public static int[] Sample;
	public static int[] Time;
	public static short[] Map;
	public static byte[] CloudMap;
	public static byte[] getcalibrateDark;
	public static byte[] getcalibrateRef;
	private String str;
	private long touchTime;
	private BluetoothGatt gatt;
	private Window mWindow;
	int hour, min;
	public static String category=null;
	private static String spAddress;
	public static String mDeviceAddress;
	public static String Loctaion;
	private FragmentHome fragmentHome;
	private FragmentRecord fragmentRecord;
	private FragmentAuth fragmentAuth;
	private FragmentDetection fragmentDetection;
	private FragmentDiscovery fragmentDiscovery;
	private ProgressDialog dialog;
	private static byte[] msgData;
	private SharedPreferences sp;
	public static boolean isClicked = false;
	public static boolean ISCONNECT = false;
	public static boolean upGrade = false;
	public static boolean RTOAD = false;
	public static boolean IsShowing = false;
	public static boolean CALIBRATE_TIME = false;
	public static boolean HAVEGOOD = false;
	private boolean isconnect = false;
	public static boolean WRITE_FONT_BUSY = false;
	private boolean upGrading = false;
	private ImageView iv_title_set;
	public static SpotsDialog mdialog;
	public static String fwVer;
	public static boolean ACTION_TO_DETECTION = false;
	public static InputStream FONT;
	public static ArrayList<byte[]> block = null;
	private LinearLayout ll_home, ll_record, ll_detection, ll_discovery, ll_auth;
	public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
	public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";
	public static BluetoothLeService mBluetoothLeService;// 自定义的一个继承自Service的服务
	public static BluetoothGattCharacteristic mCharacteristic, mCharacteristicNotify;
	public static BluetoothGattService mnotyGattService;// 三个长得很像，由大到小的对象BluetoothGatt、
	private List<BluetoothGattService> mServiceList = null;
	public static BluetoothGattService mOadService = null;
	public static BluetoothGattService mConnControlService = null;
	private static int msgDataPos; // BluetoothGattService、BluetoothGattCharacteristic、
	// 奶粉卫士的服务和特征值
	private static final UUID uuid = UUID.fromString(SampleGattAttributes.BLE_Service);
	private boolean mServicesRdy = false;
	private boolean IsPaired;
	public AMapLocationClientOption mLocationOption = null;
	public AMapLocationClient mLocationClient = null;
	public static int milkNumber;
	public static String milkName;
	// 位置
	public static String LOCATION = null;
	// 定义文本组件对象
	@SuppressWarnings("unused")
	private TextView text_home, text_detection, text_discovery, text_auth, text_record;
	// 定义图片组件对象
	private ImageView homeIv, authIv, recordIv, discoveryIv, detectionIv;
	// 定义蓝牙适配器
	private BluetoothAdapter bluetoothAdapter;
	AlertDialog.Builder builder;
	public static Handler mhander = new Handler() {
		public void handleMessage(Message msg) {
			if (msg != null) {
				switch (msg.what) {
					case Constant.DATA_SEND:
						System.out.println("开始传输" + msgData.length);
						int rest = msgData.length - msgDataPos;
						int sendByte = rest > 20 ? 20 : rest;
						byte[] send = new byte[sendByte];
						for (int i = 0; i < send.length; i++) {
							send[i] = msgData[msgDataPos + i];
						}
						mBluetoothLeService.writeCharacteristic(mCharacteristic, send);
						msgDataPos += send.length;
						if (msgDataPos < msgData.length) {
							mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 200);
						}
						break;
					case Constant.STRAT_COLLECT:// 点击后发送采集sample命令
						startCollect();
						break;
					case Constant.MilkName:
						System.out.println("字体传输内容："+WRITE_FONT_BUSY);
						if (!WRITE_FONT_BUSY) {
							System.out.println("字体传输："+WRITE_FONT_BUSY);
							try {
								WRITE_FONT_BUSY = true;
								writeFront(milkName);
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						break;
					case Constant.OAD:
						oad();
						break;
					case Constant.FW_CHECK:
						System.out.println("执行检查固件方法1");
						fwCheck();
						break;
					case Constant.CHECKGOOD:
						System.out.println("收到检查物品的信息");
						if (ISCONNECT) {
							System.out.println("判断是否连接");
							if (HAVEGOOD) {
								checkGood();
							} else {
								checkGood1();
							}
						}
						break;
					case Constant.CALIBRATE_TIME:
						setCalibrateTime(FragmentAuth.time);
						break;
					case Constant.TEST_DARK:
						Constant.IsTesting = true;
						testDark();
						break;
					case Constant.DISCONNECT:
						mBluetoothLeService.disconnect();
						ISCONNECT = false;
					default:
						break;
				}
			}
		}
	};
	private static void writeFront(String milkName2) throws UnsupportedEncodingException {
		String temp = Utils.StringTOSpiltString(milkName2);
		System.out.println("temp" + temp);
		byte[] payload = Font.FontLibraryGeneration(temp);
		for (byte b : payload) {
			System.out.println("payload" + b);
		}
		BTMsg msg = new BTMsg((byte) 0x51, (byte) 0x00, payload);
		msgData = msg.getRawData();
		for (int i = 0; i < msgData.length; i++) {
			System.out.println("传输字库：" + "msgData" + msgData[i] + "----" + i);
		}
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 500);
	}
	protected static void setCalibrateTime(int time2) {
		byte[] c = Utils.hexStringToByte(DigitalTrans.algorismToHEXString(time2));
		byte[] payload = new byte[c.length];
		for (int i = 0; i < payload.length; i++) {
			payload[i] = c[c.length - 1];
		}
		BTMsg msg = new BTMsg((byte) 0x63, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected static void checkGood1() {
		System.out.println("检查是否添加 目标1");
		byte[] payload = {0x01};
		BTMsg msg = new BTMsg((byte) 0x67, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected static void checkGood() {
		System.out.println("检查是否添加 目标");
		byte[] payload = {0x00};
		BTMsg msg = new BTMsg((byte) 0x67, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}

	protected static void fwCheck() {
		System.out.println("检查固件方法");
		byte[] payload = null;
		BTMsg msg = new BTMsg((byte) 0x59, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected static void oad() {
		byte[] data = {0x09, 0x02, 0x00, 0x2c, 0x42, 0x42, 0x42, 0x42, (byte) 0xff, (byte) 0xff, (byte) 0xff,
				(byte) 0xff};
		BTMsg msg = new BTMsg((byte) 0x60, (byte) 0x00, data);
		msgData = msg.getRawData();
		mBluetoothLeService.writeCharacteristic(mCharacteristic, msgData);
	}
	private static void WriteFrontName() throws UnsupportedEncodingException {
		String s = Utils.StringTOSpiltString(milkName);
		byte[] payload = Font.FontLibraryGenerationName(s);
		byte[] payloadNew = new byte[payload.length + 2];
		payloadNew[0] = 0x02;
		payloadNew[1] = (byte) 0x00;
		for (int i = 0; i < payload.length; i++) {
			payloadNew[i + 2] = payload[i];
		}
		System.out.println("milkName" + milkName + "payload长度：" + payload.length);
		BTMsg msg = new BTMsg((byte) 0x52, (byte) 0x00, payloadNew);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(1, 100);
	}
	protected void onCreate(Bundle savedInstanceState) {
		System.out.println("++++++++++++++onCreate+++++++++++++++++++");
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		System.out.println(
				"getResource测试:" + getResources().getIdentifier("circle_result_" + "1", "drawable", getPackageName()));
		if (Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		initWindow();
		CloudComm cc=new CloudComm();
		try {
			cc.updateSession(Constant.OPENID,Constant.SESSIONID,Constant.Location);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		dialog = new ProgressDialog(MainActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("设备重新连接中...");
		builder = new AlertDialog.Builder(MainActivity.this);
		// 初始化定位
		mLocationClient = new AMapLocationClient(getApplicationContext());
		// 设置定位回调监听
		mLocationClient.setLocationListener(this);
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(2000);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mLocationClient.startLocation();
		Intent intent = getIntent();
		mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);
		System.out.println("MDA" + mDeviceAddress);
		spAddress = (String) SharePreferenceUtil.getAttributeByKey(getApplicationContext(), "SP", "ISPAIRED", 0);
		// 绑定服务
		if (spAddress != null && spAddress != "" && mDeviceAddress == null) {
			/*
			 * System.out.println("spAddress"+spAddress.length()+spAddress);
			 * isconnect=mBluetoothLeService.connect(spAddress);
			 * while(!isconnect){
			 * isconnect=mBluetoothLeService.connect(spAddress);
			 */
			mDeviceAddress = spAddress;
		} else if (mDeviceAddress != null) {
			// mBluetoothLeService.connect(mDeviceAddress);
		}
		Intent gattServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
		this.getApplicationContext().bindService(gattServiceIntent, mServiceConnection, MainActivity.BIND_AUTO_CREATE);
		new Thread() {
			@SuppressWarnings("resource")
			public void run() {
				// 这儿是耗时操作
				InputStream input = null;
				FileOutputStream out = null;
				byte[] buffer = new byte[1024 * 4];
				try {
					input = getResources().getAssets().open("HZK16");
					out = openFileOutput("HZK16", MODE_PRIVATE);
					int read = 0;
					while ((read = input.read(buffer)) != -1) {
						out.write(buffer, 0, read);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (out != null) {
						try {
							System.out.println("读写字体矩阵");
							input.close();
							out.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OAD.bin");
				if (f.exists()) {
					System.out.println("发现文件");
					block = BinRead.ReadBIn(Environment.getExternalStorageDirectory().getAbsolutePath() + "/OAD.bin");
				} else {
					System.out.println("未发现文件");
				}
			}

			;
		}.start();
		// MPermissions.requestPermissions(MainActivity.this,
		// Constant.REQUECT_CODE_SDCARD, Manifest.permission_group.STORAGE);
		// 获取当前系统的时间
		SimpleDateFormat myFmt = new SimpleDateFormat("yy/MM/dd HH:mm");
		System.out.println("系统当前时间1：" + TimeUtils.getCurrentTimeInLong());
		System.out.println("系统当前时间2：" + TimeUtils.getCurrentTimeInString());
		System.out.println("系统当前时间3：" + TimeUtils.getCurrentTimeInString(myFmt));
		Calendar c = Calendar.getInstance();
		String month = Integer.toString(c.get(Calendar.MONTH));
		String day = Integer.toString(c.get(Calendar.DAY_OF_MONTH));
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		System.out.println("month" + month + 1 + "day" + day + "hour" + hour + "min" + min);
		int a = 27;
		long b = (a - c.get(Calendar.HOUR_OF_DAY)) * 3600 * 1000;
		System.out.println("距离凌晨三点的时间" + b + "小时数" + c.get(Calendar.HOUR_OF_DAY));
		initView();
		initData();
		// 初始化默认为选中点击了“主页”按钮
		clickHomeBtn();
		// 获取蓝牙适配器
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (bluetoothAdapter.isEnabled()) {
			ToastUtil.showToast(MainActivity.this, "蓝牙已打开");
			return;
		}
		{
			openBtDialog();
		}
	}
	@SuppressLint("NewApi")
	// 进行状态栏透明处理
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
		}
	}
	/**
	 * 广播接收
	 */
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
			// 通过intent获得的不同action，来区分广播该由谁接收(只有action一致,才能接收)。
			if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
				System.out.println("接收到连接成功广播");
				ISCONNECT = true;
			} else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
				ISCONNECT = false;
				FragmentHome.homeHandler.sendEmptyMessage(Constant.BT_DISCONNECTED);
			}
			// 发现服务后，自动执行回调方法onServicesDiscovered(),发送一个action=ACTION_GATT_SERVICES_DISCOVERED的广播，其他情况同理
			else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
				System.out.println("接收到发现服务的广播");
				if (status == BluetoothGatt.GATT_SUCCESS) {
					displayServices();
				} else {
					return;
				}
			} else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
				System.out.println("设备可用啊");
				Log.e("广播", "通知了吗");
				str = intent.getStringExtra(BluetoothLeService.EXTRA_DATA); // 接收广播发送的数据，不管读写，一律接收
				System.out.println(
						mCharacteristic.getProperties() + "\n" + mCharacteristic.getPermissions() + "\n" + str);
				// 必须偶数个16进制字符发送，否则易丢失
			} else if (BluetoothLeService.ACTION_TO_DETECTION.equals(action)) {
				if (HAVEGOOD == true) {
					MainActivity.ACTION_TO_DETECTION = true;
					restartAll();
					clickDetectionBtn();
				} else {
					ToastUtil.showToast(MainActivity.this, "未添加有效目标");
				}
			} else if (BluetoothLeService.ACTION_PAIR.equals(action)) {
				System.out.println("绑定成功");
				mdialog.dismiss();
				// FragmentHome.homeHandler.sendEmptyMessage(Constant.BTPAIR);
				// FragmentHome.homeHandler.sendEmptyMessage(Constant.CONN_AUTHORIZE);
				readDark();
				System.out.println("mDeviceAddress+++" + mDeviceAddress);
			} else if (BluetoothLeService.ACTION_COLLECT.equals(action)) {
				MainActivity.isClicked = true;
				restartAll();
				clickDetectionBtn();
				System.out.println("接收到采集消息 并且isclicked=true");
			} else if (BluetoothLeService.ACTION_FONT.equals(action)) {
				System.out.println("字库发送完毕");
				try {
					WriteFrontName();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else if (BluetoothLeService.ACTION_FONT_Name.equals(action)) {
				System.out.println("传输字体完毕");
				WRITE_FONT_BUSY = false;
			} else if (BluetoothLeService.ACTION_CONN_AUTHORIZE.equals(action)) {
				getWaveMap();
			} else if (BluetoothLeService.ACTION_GET_WAVE_MAP.equals(action)) {
				/*try {
					ToastUtil.showToast(MainActivity.this, "对比云端数据");
					GET_DAMAP(Constant.SESSIONID, Constant.OPENID, mDeviceAddress);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
				readDark();
			} else if (BluetoothLeService.ACTION_SET_WAVE_MAP.equals(action)) {
				ToastUtil.showToast(MainActivity.this, "设置damap数据完成，开启同步数据");
				readDark();
			} else if (BluetoothLeService.ACTION_CALI_DARK_FINISHED.equals(action)) {
				System.out.println("dark结束" + CALIBRATE_TIME);
				if (CALIBRATE_TIME) {
					readRef();
				} else {
					testRef();
				}
			} else if (BluetoothLeService.ACTION_CALI_REF1_FINISHED.equals(action)) {
				// 获取上次校准的时间
				System.out.println("ref完毕" + CALIBRATE_TIME);
				if (CALIBRATE_TIME) {
					getcalibrateTime();
				} else {
					int sumDark = 0;
					int sumRef = 0;
					for (int i = 0; i < Dark.length; i++) {
						sumDark += Dark[i];
						sumRef += HalfRef[i];
					}
					int darkAverage = sumDark / Dark.length;
					int refAverage = sumRef / HalfRef.length;
					System.out.println("darkAverage:" + darkAverage + "refAverage:" + refAverage);
					ToastUtil.showToast(MainActivity.this, "darkAverage:" + darkAverage + "refAverage:" + refAverage);
					if ((1500 <= darkAverage && darkAverage < 4500) && (40000 <= refAverage && refAverage <= 64000)) {
						setDark();
					} else {
						if (Constant.IsTesting) {
							FragmentDetection.mhandler.sendEmptyMessage(2);
						} else {
							testDark();
						}
					}
				}
			} else if (BluetoothLeService.ACTION_SET_DARK_SPECTRUM_DATA.equals(action)) {
				ToastUtil.showToast(MainActivity.this, "设置dark完毕");
				setRef();
			} else if (BluetoothLeService.ACTION_SET_REF_SPECTRUM_DATA.equals(action)) {
				ToastUtil.showToast(MainActivity.this, "设置ref完毕");
				CloudComm cc = new CloudComm();
				try {
					if (Constant.SESSIONID != null && Constant.OPENID != null) {
						System.out.println("开始执行上传dark和ref" + Dark.length + "R" + HalfRef.length);
						cc.updateDarkRefData(Dark, HalfRef, Constant.OPENID, Constant.SESSIONID);
					} else {
						System.out.println("Session或者openid为空");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				mdialog.dismiss();
			} else if (BluetoothLeService.ACTION_UPGRADE.equals(action)) {
				dialog.show();
				System.out.println("--------接收到OAD升级命令--------");
				try {
					Thread.sleep(2000);
					System.out.println("------停顿2s-----");
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				new Thread(new Runnable() {
					public void run() {
						System.out.println("----开始子线程---");
						// Thread.sleep(3000);
						System.out.println("-----停顿3s-----");
						BluetoothLeService.refreshDeviceCachea(null);
						mBluetoothLeService.connect(mDeviceAddress);
						System.out.println(
								"mBluetoothLeService:" + mBluetoothLeService + "mDeviceAddress:" + mDeviceAddress);
						if (gatt != null) {
							System.out.println("蓝牙连接为：" + gatt.connect());
						} else {
							System.out.println("gatt为null");
						}
						System.out.println("---打开蓝牙---");
						// mBluetoothLeService.connect(mDeviceAddress);
						Intent gattServiceIntent = new Intent(MainActivity.this, BluetoothLeService.class);
						getApplicationContext().bindService(gattServiceIntent, mServiceConnection,
								MainActivity.BIND_AUTO_CREATE);
					}
				}).start();
			} else if (BluetoothLeService.ACTION_GET_FW_VER.equals(action)) {
				System.out.println("接收到服务关于固件的回复" + Constant.FW_VERSION);
				sp = getSharedPreferences("fwVer", MODE_APPEND);
				String NewFwVer = sp.getString("fwVersion", fwVer);
				// 对固件的版本进行判断 确认是否升级
				if (sp != null && NewFwVer != null
						&& Double.parseDouble(Constant.FW_VERSION) > Double.parseDouble(NewFwVer)) {
					sp.edit().putString("fwVersion", Constant.FW_VERSION).commit();
				} else {
					sp.edit().putString("fwVersion", Constant.FW_VERSION).commit();
				}
				FragmentAuth.mHandler.sendEmptyMessage(1);
				CloudComm cc = new CloudComm();
				try {
					cc.getFwLastestVersion(Constant.OPENID, Constant.SESSIONID);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (BluetoothLeService.ACTION_GET_CALIBRATE_TIME.equals(action)) {
				System.out.println("收到action_get_calibrate_time");
				CloudComm cc = new CloudComm();
				try {
					int sumDark = 0;
					int sumRef = 0;
					for (int i = 0; i < Dark.length; i++) {
						sumDark += Dark[i];
						sumRef += HalfRef[i];
					}
					int darkAverage = sumDark / Dark.length;
					int refAverage = sumRef / HalfRef.length;
					System.out.println("darkAverage:" + darkAverage + "refAverage:" + refAverage);

					if (Constant.SESSIONID != null && Constant.OPENID != null) {
						CALIBRATE_TIME = false;
						System.out.println("开始执行上传dark和ref" + Dark.length + "R" + HalfRef.length);
						cc.updateDarkRefData(Dark, HalfRef, Constant.OPENID, Constant.SESSIONID);
					} else {
						System.out.println("Session或者openid为空");
					}
					if ((1500 <= darkAverage && darkAverage < 4500) && (49500 <= refAverage && refAverage <= 62500)) {
					} else {
						// testDark();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else if (BluetoothLeService.ACTION_SET_TEST_OBJ.equals(action)) {
				mdialog.dismiss();
				String s = (String) SharePreferenceUtil.getAttributeByKey(getApplicationContext(), "SP",
						"CALIBRATE_TIME", 0);
				if (s == null || s == "" || s == " ") {
					setCalibrateTime();
				} else {
					String time[] = s.split(":");
					int hourOfDay = Integer.valueOf(time[0]);
					int minute = Integer.valueOf(time[1]);
					int timehour = 0, timemin;
					if (hourOfDay >= hour && minute > min) {
						timehour = (hourOfDay - hour) * 3600 * 1000;
						System.out.println("timehour:" + timehour + "hour" + hour);
					} else {
						System.out.println("timehour:" + timehour + "hour" + hour);
						timehour = (hourOfDay + 24 - hour) * 3600 * 1000;
					}
					timemin = (minute - min) * 60 * 1000;
					setCalibrateTime(timehour + timemin);
				}
			} else if (BluetoothLeService.ACTION_SET_AUTO_CALIBRATE_TIME.equals(action)) {
				System.out.println("接收到自动校准时间的回复");
				if (spAddress == null || spAddress == "") {
					SharePreferenceUtil.saveOrUpdateAttribute(getApplicationContext(), "SP", "ISPAIRED",
							mDeviceAddress);
					//ToastUtil.showToast(MainActivity.this, "绑定成功");
				}
				FragmentHome.homeHandler.sendEmptyMessage(Constant.CONN_AUTHORIZE);
			} else if (BluetoothLeService.ACTION_ERROR_INFO.equals(action)) {
				ToastUtil.showToast(MainActivity.this, "未添加有效目标");
				milkName = "未知目标";
				if(!WRITE_FONT_BUSY){
				try {
					writeFront(milkName);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			}
		}
	};
	private static void startCollect() {
		byte[] payload = null;
		BTMsg msg = new BTMsg((byte) 0x09, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected void getWaveMap() {
		byte[] payload = null;
		BTMsg msg = new BTMsg((byte) 0x066, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected void setCalibrateTime() {
		byte[] payload = {80, (byte) 0xee, 36, 00};
		BTMsg msg = new BTMsg((byte) 0x63, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected void showPairDialog() {
		builder.setMessage("请在设备上点击按钮 完成绑定");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.create().show();
	}
	protected void setDark() {
		byte[] payload = getcalibrateDark;
		System.out.println("设置dark" + payload.length);
		ToastUtil.showToast(MainActivity.this, "长度为:" + payload.length);
		BTMsg msg = new BTMsg((byte) 0x56, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected void setRef() {
		System.out.println("设置ref");
		byte[] payload = getcalibrateRef;
		BTMsg msg = new BTMsg((byte) 0x57, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected void getcalibrateTime() {
		System.out.println("读取距离上次校准的时间");
		byte[] payload = null;
		BTMsg msg = new BTMsg((byte) 0x62, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(1, 150);
	}
	protected void readRef() {
		// TODO Auto-generated method stub
		System.out.println("读取ref");
		byte[] payload = {0x00};
		BTMsg msg = new BTMsg((byte) 0x55, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(1, 150);
	}
	protected void testRef() {
		System.out.println("校准ref");
		byte[] payload = {0x01};
		BTMsg msg = new BTMsg((byte) 0x55, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(1, 150);
	}
	protected static void testDark() {
		System.out.println("校准dark");
		byte[] payload = {0x01};
		BTMsg msg = new BTMsg((byte) 0x54, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(1, 150);
	}
	protected void readDark() {
		System.out.println("读取dark");
		CALIBRATE_TIME = true;
		byte[] payload = {0x00};
		BTMsg msg = new BTMsg((byte) 0x54, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(1, 150);
	}
	protected void writeStep() {
		System.out.println("测试");
		byte[] payload = {0x05};
		BTMsg msg = new BTMsg((byte) 0x06, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	protected void checkOad() {
		// Check if OAD is supported (needs OAD and Connection Control service)
		mOadService = null;
		mConnControlService = null;
		// 查找OAD 服务
		for (int i = 0; i < mServiceList.size() && (mOadService == null || mConnControlService == null); i++) {
			BluetoothGattService srv = mServiceList.get(i);
			if (srv.getUuid().equals(SampleGattAttributes.OAD_SERVICE_UUID)) {
				mOadService = srv;
				System.out.println("--OadService--" + mOadService);
				mCharacteristic = mOadService.getCharacteristic(SampleGattAttributes.OAD_CHARACTER);// 获取可读写的特征值
				System.out.println("mCharacteristic:" + mCharacteristic);
				List<BluetoothGattCharacteristic> temp = mOadService.getCharacteristics();
				for (BluetoothGattCharacteristic bluetoothGattCharacteristic : temp) {
					System.out.println(bluetoothGattCharacteristic.getUuid().toString());
				}
				// mCharacteristic.setValue(list.get(0));
				mCharacteristicNotify = mOadService.getCharacteristic(SampleGattAttributes.OAD_CHARACTER);// 获取有通知特性的特征值
				System.out
						.println("mCharacteristicNotify" + mCharacteristicNotify + "mCharacteristic" + mCharacteristic);
				mBluetoothLeService.setCharacteristicNotification(mCharacteristicNotify, true);
				RTOAD = true;
				System.out.println("准备进行OAD升级");
				ToastUtil.showToast(MainActivity.this, "准备进行OAD升级");
				dialog.dismiss();
				Intent intenta = new Intent();
				intenta.setClass(MainActivity.this, OADActivity.class);
				startActivity(intenta);
			} else if (srv.getUuid().equals(SampleGattAttributes.BLE_SERVICE_UUID)) {
				System.out.println("-----普通模式----");
				mnotyGattService = srv;
				mCharacteristic = mnotyGattService.getCharacteristic(SampleGattAttributes.BLE_READ_WRITE_UUID);// 获取可读写的特征值
				mCharacteristicNotify = mnotyGattService.getCharacteristic(SampleGattAttributes.BLE_NOTIFY_UUID);
				mBluetoothLeService.setCharacteristicNotification(mCharacteristicNotify, true);
				RTOAD = false;
				try {
					Thread.sleep(1000);
					write();
					mdialog = new SpotsDialog(MainActivity.this, R.style.readDark);
					mdialog.show();
					System.out.println("write执行了");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	// 进行身份校验
	private void write() {
		byte[] payload = null;
		BTMsg msg = new BTMsg((byte) 0x53, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 500);
	}
	// 进行配对
	private static void pair() {
		byte[] payload = null;
		BTMsg msg = new BTMsg((byte) 0x58, (byte) 0x00, payload);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	/**
	 * OAD升级准备
	 *
	 * @param fileName
	 * @param line
	 * @return
	 */
	private final ServiceConnection mServiceConnection = new ServiceConnection() {
		// 服务连接建立之后的回调函数。
		@Override
		public void onServiceConnected(ComponentName componentName, IBinder service) {
			mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();
			// 初始化
			if (!mBluetoothLeService.initialize()) {
				Log.e(TAG, "Unable to initialize Bluetooth");
				finish();
			}
			// TODO
			// 定时效果未能实现
			// Date curDate = new Date(System.currentTimeMillis());
			if (mDeviceAddress != null) {
				Connectted = mBluetoothLeService.connect(mDeviceAddress);
				while (!Connectted) {
					mBluetoothLeService.connect(mDeviceAddress);
					Connectted = mBluetoothLeService.connect(mDeviceAddress);
					System.out.print("--------connnected----------"+Connectted);
					// Date endDate = new Date(System.currentTimeMillis());
				}
			}
		}
		@Override
		public void onServiceDisconnected(ComponentName componentName) {
			mBluetoothLeService = null;
		}
	};
	protected void onResume() {
		System.out.println("++++++++++++++onResume+++++++++++++++++++");
		super.onResume();
		/*
		 * if (SharePreferenceUtil.getAttributeByKey(MainActivity.this, "SP",
		 * "ScanResult", 0) != null &&
		 * SharePreferenceUtil.getAttributeByKey(MainActivity.this, "SP",
		 * "ScanResult", 0) != "" && Constant.SESSIONID != null) { CloudComm cc
		 * = new CloudComm(); try { cc.getMilkInfo((byte) 0, (String)
		 * SharePreferenceUtil.getAttributeByKey(MainActivity.this, "SP",
		 * "ScanResult", 0), Constant.OPENID, Constant.SESSIONID); } catch
		 * (JSONException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 */
		if (LOCATION != null) {
			mLocationClient.stopLocation();
			System.out.println(LOCATION);
			System.out.println("停止定位");
		}
		// Register a BroadcastReceiver to be run in the main activity thread.

		if (mDeviceAddress != null) {
			registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		}
		if (IsScanCode == true) {
			IsScanCode = false;
			restartAll();
			clickDetectionBtn();
		}
		if (!bluetoothAdapter.isEnabled()) {
			bluetoothAdapter.enable();
			return;
		}
	}
	@Override
	protected void onPause() {
		if (mDeviceAddress != null) {
			System.out.println("MAIN-pause");
			unregisterReceiver(mGattUpdateReceiver);
		}
		System.out.println("++++++++++++++onpause--------");
		super.onPause();
	}
	/**
	 * 开启应用弹出对话框 提示开始蓝牙
	 */
	private void openBtDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
		builder.setMessage("应用请求打开蓝牙");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				bluetoothAdapter.enable();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	/**
	 * 初始化组件
	 */
	public void initView() {
		// 实例化布局对象
		ll_home = (LinearLayout) findViewById(R.id.ll_home);
		ll_auth = (LinearLayout) findViewById(R.id.ll_auth);
		ll_record = (LinearLayout) findViewById(R.id.ll_record);
		ll_discovery = (LinearLayout) findViewById(R.id.ll_discovery);
		ll_detection = (LinearLayout) findViewById(R.id.ll_detection);
		/*
		 * // 实例化图片组件对象 homeIv = (ImageView) findViewById(R.id.image_home);
		 * authIv = (ImageView) findViewById(R.id.image_auth); recordIv =
		 * (ImageView) findViewById(R.id.image_record); discoveryIv =
		 * (ImageView) findViewById(R.id.image_discovery); detectionIv =
		 * (ImageView) findViewById(R.id.image_detection);
		 */
		// 初始化文本组件对象
		iv_title_set = (ImageView) findViewById(R.id.title_button);
	}
	/**
	 * 初始化数据
	 */
	public void initData() {
		// 给布局对象设置监听
		ll_home.setOnClickListener(this);
		ll_auth.setOnClickListener(this);
		ll_record.setOnClickListener(this);
		ll_detection.setOnClickListener(this);
		ll_discovery.setOnClickListener(this);
		// iv_title_set.setOnClickListener(this);
	}
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}
	// 版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pi;
	}
	public void writeFileData(String fileName, String message) {
		try {
			FileOutputStream fout = openFileOutput(fileName, MODE_APPEND);
			byte[] bytes = message.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 处理点击事件
	 */
	public void onClick(View v) {
		if (v.getId() == R.id.ll_home) {
			restartAll();
			clickHomeBtn();
		} else if (v.getId() == R.id.ll_auth) {
			restartAll();
			clickAuthBtn();
		} else if (v.getId() == R.id.ll_record) {
			restartAll();
			clickRecordBtn();
		} else if (v.getId() == R.id.ll_discovery) {
			restartAll();
			clickDiscoveryBtn();
		} else if (v.getId() == R.id.ll_detection) {
			restartAll();
			clickDetectionBtn();
		}
	}
	public  void restartAll() {
		ll_auth.setSelected(false);
		ll_detection.setSelected(false);
		ll_discovery.setSelected(false);
		ll_record.setSelected(false);
		ll_home.setSelected(false);
	}
	/**
	 * 点击了“主页”按钮
	 */
	@SuppressLint("ResourceAsColor")
	private void clickHomeBtn() {
		// 实例化Fragment页面
		fragmentHome = new FragmentHome();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentHome);
		// 事务管理提交
		fragmentTransaction.commit();
		// 改变选中状态
		ll_home.setSelected(true);
	}
	/**
	 * 点击了“个人中心”按钮
	 */
	private void clickAuthBtn() {
		// 实例化Fragment页面
		fragmentAuth = new FragmentAuth();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentAuth);
		// 事务管理提交
		fragmentTransaction.commit();
		ll_auth.setSelected(true);
	}
	/**
	 * 点击了"记录”按钮
	 */
	private void clickRecordBtn() {
		// 实例化Fragment页面
		fragmentRecord = new FragmentRecord();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentRecord);
		// 事务管理提交
		fragmentTransaction.commit();
		ll_record.setSelected(true);
	}
	/**
	 * 点击“检测”按钮
	 */
	public void clickDetectionBtn() {
		// 实例化Fragment页面
		fragmentDetection = new FragmentDetection();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentDetection);
		// 事务管理提交
		fragmentTransaction.commitAllowingStateLoss();
		ll_detection.setSelected(true);
	}
	private void clickDiscoveryBtn() {
		// 实例化Fragment页面
		fragmentDiscovery = new FragmentDiscovery();
		// 得到Fragment事务管理器
		FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
		// 替换当前的页面
		fragmentTransaction.replace(R.id.frame_content, fragmentDiscovery);
		// 事务管理提交
		fragmentTransaction.commit();
		ll_discovery.setSelected(true);
	}
	private long waitTime = 2000;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				ToastUtil.showToast(MainActivity.this, "再按一次退出程序");
				touchTime = currentTime;
			} else {
				if (mBluetoothLeService != null) {
					MainActivity.ISCONNECT = false;
					System.out.println("我");
					this.getApplicationContext().unbindService(mServiceConnection);
					mBluetoothLeService.close();
				}
				closeAllActivity();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	public void closeAllActivity() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
			ActivityCompat.finishAffinity(this);
		} else {
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
	// TODO
	private void GET_DAMAP(String mSessionId, String mOpenId, String mAddress) throws JSONException {
		JSONObject json = new JSONObject();
		json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
		json.put(Constant.JSON_KEY_OPENID, mOpenId);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_DAMAP);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		JSONObject data = new JSONObject();
		data.put("mac", mAddress);
		json.put(Constant.JSON_KEY_DATA, data);
		System.out.println("json" + json);
		OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_CENTRAL).addParams("json", json.toString()).build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call arg0, Exception arg1) {
						System.out.println("Error:" + arg1);
					}
					@Override
					public void onResponse(String string) {
						JSONObject json_resp = null;
						JSONObject json_data = null;
						if (string != null) {
							try {
								json_resp = new JSONObject(string);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("string:" + string + "json:" + json_resp);
							try {
								if (json_resp != null)
									if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
											&& json_resp.get(Constant.JSON_KEY_DATA) != null) {
										ToastUtil.showToast(MainActivity.this, "服务器获取damap成功");
										json_data = json_resp.getJSONObject(Constant.JSON_KEY_DATA);
										String damapcount = (String) json_data.get("damapcount");
										String damap = json_data.get("damap").toString();
										System.out.println("length:++++" + damap.length());
										if (damap == "" || damap == null || damap.length() < 8) {
											System.out.println("++++++++++++++++++++");
											readDark();
										} else {
											String[] daMap = damap.split(" ");
											String six = "";
											short[] cloudMap = new short[daMap.length];
											CloudMap = new byte[cloudMap.length * 2];
											for (int i = 0; i < daMap.length; i++) {
												cloudMap[i] = Short.valueOf(daMap[i]);
												// six+=Integer.toHexString(cloudMap[i]);
												// System.out.println("cloudMap["+i+"]："+cloudMap[i]+"String:"+Integer.toHexString(cloudMap[i]));

												CloudMap[i * 2] = (byte) (cloudMap[i] & 0x00FF);
												CloudMap[i * 2 + 1] = (byte) ((cloudMap[i] >>> 8) & 0x00FF);
											}
											System.out.println("CloudMap:" + CloudMap[0] + ":" + CloudMap[1] + "++"
													+ new Integer((-1790) & 0xff).byteValue());
											boolean ifEqual = true;
											for (int j = 0; j < cloudMap.length; j++) {
												if (Map[j] != cloudMap[j]) {
													ifEqual = false;
												}
											}
											if (ifEqual) {
												readDark();
											} else {
												WriteDaMap();
											}
										}
									} else {
										ToastUtil.showToast(MainActivity.this, "服务器异常,请稍后再试");
										readDark();
									}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								ToastUtil.showToast(MainActivity.this, "服务器数据异常,请稍后再试");
								e.printStackTrace();
							}
						} else {
							ToastUtil.showToast(MainActivity.this, "服务器异常,请稍后再试");
							readDark();
						}
					}
				});
	}
	protected void WriteDaMap() {
		byte[] data = CloudMap;
		System.out.println("开始写damap CloudMap[0]" + data[0] + ":" + data[1] + ":" + data.length);
		BTMsg msg = new BTMsg((byte) 0x0C, (byte) 0x00, data);
		msgData = msg.getRawData();
		msgDataPos = 0;
		mhander.sendEmptyMessageDelayed(Constant.DATA_SEND, 150);
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		SharePreferenceUtil.deleteAttributeByKey(MainActivity.this, "SP", "ScanResult");
		if (mLocationClient != null) {
			/**
			 * 如果AMapLocationClient是在当前Activity实例化的，
			 * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
			 */
			mLocationClient.onDestroy();
			mLocationClient = null;
			mLocationClient = null;
		}
	}
	/**
	 * 设置监听
	 *
	 * @return intentFilter
	 */
	private static IntentFilter makeGattUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
		intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_TO_DETECTION);
		intentFilter.addAction(BluetoothLeService.ACTION_PAIR);
		intentFilter.addAction(BluetoothLeService.ACTION_FONT);
		intentFilter.addAction(BluetoothLeService.ACTION_FONT_Name);
		intentFilter.addAction(BluetoothLeService.ACTION_CONN_AUTHORIZE);
		intentFilter.addAction(BluetoothLeService.ACTION_CALI_DARK_FINISHED);
		intentFilter.addAction(BluetoothLeService.ACTION_CALI_REF1_FINISHED);
		intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
		intentFilter.addAction(BluetoothLeService.ACTION_COLLECT);
		intentFilter.addAction(BluetoothLeService.ACTION_IMAGB);
		intentFilter.addAction(BluetoothLeService.ACTION_GET_CALIBRATE_TIME);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_AUTO_CALIBRATE_TIME);
		intentFilter.addAction(BluetoothLeService.ACTION_GET_FW_VER);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_DARK_SPECTRUM_DATA);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_REF_SPECTRUM_DATA);
		intentFilter.addAction(BluetoothLeService.ACTION_UPGRADE);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_TEST_OBJ);
		intentFilter.addAction(BluetoothLeService.ACTION_ERROR_INFO);
		intentFilter.addAction(BluetoothLeService.ACTION_GET_WAVE_MAP);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_WAVE_MAP);
		return intentFilter;
	}
	private void displayServices() {
		System.out.println("列出所有服务");
		mServicesRdy = true;
		try {
			mServiceList = mBluetoothLeService.getSupportedGattServices();
			checkOad();
			for (int i = 0; i < mServiceList.size(); i++) {
				System.out.println("------获取的服务列表----" + i + ":" + mServiceList.get(i).getUuid());
			}
		} catch (Exception e) {
			e.printStackTrace();
			mServicesRdy = false;
		}
	}
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (amapLocation != null) {
			if (amapLocation.getErrorCode() == 0) {
				// 定位成功回调信息，设置相关消息
				amapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
				amapLocation.getLatitude();// 获取纬度
				amapLocation.getLongitude();// 获取经度
				amapLocation.getAccuracy();// 获取精度信息
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = new Date(amapLocation.getTime());
				df.format(date);// 定位时间
				amapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
				amapLocation.getCountry();// 国家信息
				Constant.LOCATION_PROVICE = amapLocation.getProvince();// 省信息
				Constant.LOCATION_CITY = amapLocation.getCity();// 城市信息
				Constant.LOCATION_DISTRICT = amapLocation.getDistrict();// 城区信息
				Constant.ps = amapLocation.getProvince() + amapLocation.getCity();
				amapLocation.getStreet();// 街道信息
				amapLocation.getStreetNum();// 街道门牌号信息
				amapLocation.getCityCode();// 城市编码
				amapLocation.getAdCode();// 地区编码
				Constant.Location = amapLocation.getProvince() +"."+ amapLocation.getCity() + amapLocation.getDistrict();
				CloudComm cc=new CloudComm();
				LOCATION = amapLocation.getCountry() + amapLocation.getProvince() + amapLocation.getCity() + "";
				mLocationClient.stopLocation();
			} else {
				// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
				Log.e("AmapError", "location Error,  ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
						+ amapLocation.getErrorInfo());
			}
		}
	}
	@Override
	public void OnDarkClickListener(String showText) {
		mdialog = new SpotsDialog(MainActivity.this);
		mdialog.show();
		testDark();
	}
	@Override
	public void onStart() {
		super.onStart();
		Intent intent=getIntent();
		category=intent.getStringExtra("category");
		Constant.modeltype=intent.getStringExtra("largeID");
		Constant.modelid=intent.getStringExtra("middleID");
		middleUrl=intent.getStringExtra("middleUrl");
		Constant.modelobjectid=intent.getStringExtra("littleID");
		Constant.modelobjectNumber=intent.getStringExtra("littleNumber");
		if(category!=null){
		MainActivity.mhander.sendEmptyMessage(Constant.CHECKGOOD);
		MainActivity.HAVEGOOD = true;}
		System.out.println("++++++++++++++onStart+++++++++++++++++++"+"category:"+category
				+"largeID:"+Constant.modeltype+"middleID:"+Constant.modelid+"middleUrl:"+middleUrl
				+"littleID"+Constant.modelobjectid
		);
	}
}
