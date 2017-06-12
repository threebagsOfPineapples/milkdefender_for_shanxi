package com.magispec.shield.activity;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.android.volley.RequestQueue;
import com.google.gson.Gson;
import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.UpdateInfo;
import com.magispec.shield.service.UpdateInfoService;
import com.magispec.shield.utils.ReadTxtFileUtils;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import okhttp3.Call;
/**
 * 欢迎界面，包括检查版本更新、同步记录等功能。
 * 
 * @author Xp
 */
public class WelcomeActivity extends Activity {
	// 更新版本要用到的一些信息
	public static UpdateInfo info;
	private ProgressDialog progressDialog;
	UpdateInfoService updateInfoService;
	public static InputStream in;
	public static final int UPDATE = 0x101;
	public static final int ISFIRST = 0x102;
	private SharedPreferences sp;
	private ImageView welocmePage;
	private  String LOCATION;
	public static  ArrayList<String>cataegory;
	//声明AMapLocationClient类对象
	public AMapLocationClient mLocationClient = null;
	//声明定位回调监听器
	public AMapLocationClientOption mLocationOption = null;
	//设置定位回调监听
	RequestQueue mQueue;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		SharePreferenceUtil.deleteAttributeByKey(WelcomeActivity.this, "SP", "ScanResult");
		/*
		初始化定位
		声明mLocationOption对象
		初始化定位
		*/
		mLocationClient = new AMapLocationClient(getApplicationContext());
		mLocationClient.setLocationListener(mLocationListener);
		// 初始化定位参数
		mLocationOption = new AMapLocationClientOption();
		// 设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
		mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
		// 设置是否返回地址信息（默认返回地址信息）
		mLocationOption.setNeedAddress(true);
		// 设置是否只定位一次,默认为false
		mLocationOption.setOnceLocation(false);
		// 设置是否强制刷新WIFI，默认为强制刷新
		mLocationOption.setWifiActiveScan(true);
		// 设置是否允许模拟位置,默认为false，不允许模拟位置
		mLocationOption.setMockEnable(false);
		// 设置定位间隔,单位毫秒,默认为2000ms
		mLocationOption.setInterval(1000);
		// 给定位客户端对象设置定位参数
		mLocationClient.setLocationOption(mLocationOption);
		// 启动定位
		mLocationClient.startLocation();
		sp = getSharedPreferences("config", MODE_PRIVATE);
		welocmePage = (ImageView) findViewById(R.id.welocmePage);
		handler.sendEmptyMessageDelayed(ISFIRST, 2000);
	/*	if(Utils.isNetworkConnected(getBaseContext())){
			OkHttpUtils.get().url("http://123.56.229.50/download/project-res/milkdefender/home.png").build()
					.execute(new BitmapCallback() {
						@Override
						public void onResponse(Bitmap arg0) {
							welocmePage.setImageBitmap(arg0);
							System.out.println("下载成功");
							checkUpdate();
						}
						@Override
						public void onError(Call arg0, Exception e) {
							welocmePage.setBackgroundResource(R.drawable.homepage);
							System.out.println("error" + e.getMessage());
						}
					});
		}else{
			ToastUtil.showToast(getBaseContext(),"无法连接到网络,请检查网络设置");
			Intent intent=new Intent();
			intent.setClass(WelcomeActivity.this,WXLoginActivity.class);
			startActivity(intent);
		}*/
	}
	private void checkUpdate() {
		// Toast.makeText(MainActivity.this, "正在检查版本更新..",
		// Toast.LENGTH_SHORT).show();
		// 自动检查有没有新版本 如果有新版本就提示更新
		new Thread() {
			public void run() {
				/*
				 * try {
				 * 
				 * info = updateInfoService.getUpDateInfo();
				 * System.out.println("----info---"+info.getDescription());
				 * handler.sendEmptyMessage(UPDATE); } catch (Exception e) {
				 * e.printStackTrace(); }
				 */
				updateInfoService = new UpdateInfoService(WelcomeActivity.this);
				try {
					getAppLastestVersion("MAGISPEC_NO_SESSION_ID_FF", "");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			//	handler.sendEmptyMessage(ISFIRST);
			};
		}.start();
	}
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				switch (msg.what) {
				case UPDATE:
					// 如果有更新就提示
					if (updateInfoService.isNeedUpdate()) {
						showUpdateDialog();
					} else {
						handler.sendEmptyMessageDelayed(ISFIRST, 1000);
					}
					break;
				case ISFIRST:
					// 判断是否是第一次启动应用
					boolean isFirstRunning = sp.getBoolean("IsFirstRunning", true);
					// 如果是，进入欢迎向导
					if (false) {
						Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
						startActivity(intent);
					} else {
						// 否则，进入主页面
						Intent intent = new Intent(WelcomeActivity.this, WXLoginActivity.class);
						startActivity(intent);
						overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
					}
					finish();
					break;
				default:
					break;
				}
			}
		};
	};
	// 显示是否要更新的对话框
	private void showUpdateDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("软件升级");
		builder.setMessage(info.getDesc());
		builder.setCancelable(false);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					System.out.println(info.getDownloadPath() + "----geturl---");
					downFile(info.getDownloadPath());
				} else {
					Toast.makeText(WelcomeActivity.this, "SD卡不可用，请插入SD卡", Toast.LENGTH_SHORT).show();
				}
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Constant.IsAppNew = true;
				handler.sendEmptyMessageDelayed(ISFIRST, 200);
			}
		});
		builder.create().show();
	}
	void downFile(final String url) {
		progressDialog = new ProgressDialog(WelcomeActivity.this); // 进度条，在下载的时候实时更新进度，提高用户友好度
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setTitle("正在下载");
		progressDialog.setMessage("请稍候...");
		progressDialog.setProgress(0);
		progressDialog.show();
		updateInfoService.downLoadFile(url, progressDialog, handler);
	}
	// 请求版本
	public void getAppLastestVersion(String mOpenId, String mSessionId) throws JSONException {
		JSONObject json = new JSONObject();
		JSONObject json_resp = null;
		json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
		json.put(Constant.JSON_KEY_OPENID, mOpenId);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_GET_APP_LATEST_VERSION);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		json.put(Constant.JSON_KEY_DATA, "");
		System.out.println("json" + json);
		OkHttpUtils.post().url(Constant.HTTPS_URL_MSG_AppVersion).addParams("json", json.toString()).build()
				.execute(new StringCallback() {
					@Override
					public void onError(Call arg0, Exception arg1) {
						System.out.println("Error:" + arg1);
					}
					@Override
					public void onResponse(String string) {
						JSONObject json_resp = null;
						if (string != null) {
							try {
								json_resp = new JSONObject(string);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("string:" + string + "json:" + json_resp);
							try {
								if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")
										&& json_resp.get(Constant.JSON_KEY_DATA) != null) {
									Gson gson = new Gson();
									/*
									 * MainActivity.versionInfo =
									 * gson.fromJson(json_resp.get(JSON_KEY_DATA
									 * ).toString(), VersionInfo.class);
									 */ 
									info = gson.fromJson(json_resp.get(Constant.JSON_KEY_DATA).toString(),
											UpdateInfo.class);
									System.out.println(
											info.getDownloadPath() + info.getVersionCode() + info.getVersionName());
									handler.sendEmptyMessage(UPDATE);
								} else {
									ToastUtil.showToast(WelcomeActivity.this, "服务器异常");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								ToastUtil.showToast(WelcomeActivity.this, "服务器异常");
								e.printStackTrace();
							}
						} else {
						}
					}
				});
	}
	AMapLocationListener mLocationListener = new AMapLocationListener() {
		@Override
		public void onLocationChanged(AMapLocation amapLocation) {
			System.out.print("------------------------回调测试---------------------");
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
					LOCATION = amapLocation.getCountry() + amapLocation.getProvince() + amapLocation.getCity() + "";
					Constant.Location =  amapLocation.getProvince() +"."+ amapLocation.getCity() + "."+amapLocation.getDistrict();
					System.out.print("----------------------Constant.Loacttion------------------------"+amapLocation.getStreetNum());
					mLocationClient.stopLocation();
				} else {
					// 显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
					Log.e("AmapError", "location Error,  ErrCode:" + amapLocation.getErrorCode() + ", errInfo:"
							+ amapLocation.getErrorInfo()+"-----------------------------------");
				}
			}else{
				ToastUtil.showToast(getBaseContext(),"出错");
			}
		}
	};
	@Override
	protected void onResume() {
		super.onResume();
		if (LOCATION != null) {
			mLocationClient.stopLocation();
			System.out.println(LOCATION);
			System.out.println("停止定位");
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
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
}
