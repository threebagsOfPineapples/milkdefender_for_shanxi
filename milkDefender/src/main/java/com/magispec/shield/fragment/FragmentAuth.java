package com.magispec.shield.fragment;

import java.io.File;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.magispec.shield.R;
import com.magispec.shield.activity.AboutActivity;
import com.magispec.shield.activity.GuideActivity;
import com.magispec.shield.activity.HelpActivity;
import com.magispec.shield.activity.MainActivity;
import com.magispec.shield.activity.TellUsActivity;
import com.magispec.shield.activity.WXLoginActivity;
import com.magispec.shield.activity.WelcomeActivity;
import com.magispec.shield.ble.BinRead;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.UpdateInfo;
import com.magispec.shield.domain.WxInfo;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.ToastUtil;
import com.magispec.shield.widgets.CustomDialog;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.BitmapCallback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import okhttp3.Call;

public class FragmentAuth extends Fragment implements OnClickListener {
	private RelativeLayout RL_about, RL_myDevice, RL_EnvironmentCalibration, RL_update, RL_firmwareVersion, RL_tellUs,
			RL_welcomePage, RL_help, RL_time;
	private ImageView WxIcon,app_version_new;
	public static TextView WxName, fwVersion, tv_time;
	private Button exit;
	OnDarkClickListener mListener;
	WxInfo wxInfo;
	public static String fwVer;
	private SharedPreferences sp;
	static Activity ac;
	UpdateInfo info;
	int hour;
	int min;
	int timehour, timemin;
	public static int time;
	public static Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg != null) {
				switch (msg.what) {
				case 1:
					fwVersion.setText(Constant.FW_VERSION);
					break;
				case 2:
					if (Constant.fWVersionInfo != null && Constant.fWVersionInfo.downloadPath != null) {
						System.out.println("code:" + Constant.fWVersionInfo.getVersionCode() + "number:"
								+ Constant.fWVersionInfo.getVersionName());
						AlertDialog.Builder builder = new AlertDialog.Builder(ac);
						builder.setTitle("提示");
						builder.setMessage("是否进行固件升级?");
						builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
								OkHttpUtils.get().url(Constant.fWVersionInfo.downloadPath).build()
										.execute(new FileCallBack(
												Environment.getExternalStorageDirectory().getAbsolutePath(),
												"OAD.bin") {
											@Override
											public void onResponse(File file) {
												// TODO Auto-generated method
												// stub
												Log.e("下载", "onResponse :" + file.getAbsolutePath());
												// ToastUtil.showToast(BaseApplicaton.getAppContext(),
												// "下载完毕");
												File f = new File(
														Environment.getExternalStorageDirectory().getAbsolutePath()
																+ "/OAD.bin");
												if (f.exists()) {
													System.out.println("发现文件---开始读取---准备进行OAD");
													MainActivity.block = BinRead.ReadBIn(
															Environment.getExternalStorageDirectory().getAbsolutePath()
																	+ "/OAD.bin");
													MainActivity.mhander.sendEmptyMessage(Constant.OAD);
												} else {
													System.out.println("未发现文件");
												}
											}
											@Override
											public void onError(Call arg0, Exception arg1) {
												Log.e("下载", "onResponse :" + "onError");
											}
											@Override
											public void inProgress(float arg0, long arg1) {
												// TODO Auto-generated method
												// stub
											}
										});
							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						if (Float.valueOf(Constant.fWVersionInfo.getVersionName().substring(0, 3)) > Float
								.valueOf(Constant.FW_VERSION)) {
							builder.create().show();
						} else {
						}
					} else {
						Log.e("下载", "error :" + "地址为空");
					}
					break;
				default:
					break;
				}
			}
		};
	};
	public interface OnDarkClickListener {
		public void OnDarkClickListener(String showText);
	}
	@Override
	public void onAttach(Context context) {
		// TODO Auto-generated method stub
		super.onAttach(context);
		try {
			mListener = (OnDarkClickListener) context;
		} catch (Exception e) {
			throw new ClassCastException(context.toString() + "must implement OnButton2ClickListener");
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_auth, container, false);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
		if (Constant.IsAppNew) {
			app_version_new.setVisibility(View.VISIBLE);
		}else{
			app_version_new.setVisibility(View.GONE);
		}
		String time = (String) SharePreferenceUtil.getAttributeByKey(getContext(), "SP", "CALIBRATE_TIME", 0);
		if (time == null || time == "" || time == " ") {
			tv_time.setText("未设置");
		} else {
			tv_time.setText("每天 " + time + "开始");
		}
		Calendar c = Calendar.getInstance();
		hour = c.get(Calendar.HOUR_OF_DAY);
		min = c.get(Calendar.MINUTE);
		ac = getActivity();
		if (WXLoginActivity.hashMap != null && WXLoginActivity.hashMap.get("a") != null) {
			wxInfo = WXLoginActivity.hashMap.get("a");
			if (wxInfo.getHeadimgurl() != null && wxInfo.getHeadimgurl().length() != 0) {
				System.out.println("头像地址为：" + wxInfo.getHeadimgurl().length());
				OkHttpUtils.get().url(wxInfo.getHeadimgurl()).build().execute(new BitmapCallback() {
					@Override
					public void onResponse(Bitmap bitmap) {
						WxIcon.setImageBitmap(bitmap);
					}
					@Override
					public void onError(Call arg0, Exception e) {
						// TODO Auto-generated method stub
					}
				});
			}
			if (wxInfo.getNickname() != null) {
				WxName.setText(wxInfo.getNickname());
			}
		}
		sp = getActivity().getSharedPreferences("fwVer", Context.MODE_APPEND);
		String NewFwVer = sp.getString("fwVersion", fwVer);
		if (NewFwVer != null) {
			fwVersion.setText(NewFwVer);
		}
	}
	private void initData() {
		RL_myDevice.setOnClickListener(this);
		RL_EnvironmentCalibration.setOnClickListener(this);
		RL_firmwareVersion.setOnClickListener(this);
		RL_help.setOnClickListener(this);
		RL_tellUs.setOnClickListener(this);
		RL_update.setOnClickListener(this);
		RL_welcomePage.setOnClickListener(this);
		RL_about.setOnClickListener(this);
		RL_time.setOnClickListener(this);
		exit.setOnClickListener(this);
	}
	private void initView() {
		WxIcon = (ImageView) getActivity().findViewById(R.id.fragment_auth_imageView_icon_wx);
		WxName = (TextView) getActivity().findViewById(R.id.fragment_auth_wxName);
		RL_myDevice = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_myDevice);
		RL_EnvironmentCalibration = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_DarkBackground);
		RL_firmwareVersion = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_fw_version);
		RL_help = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_help);
		RL_tellUs = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_tellUs);
		RL_update = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_upDate);
		RL_welcomePage = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_welcomePage);
		RL_about = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_about);
		RL_time = (RelativeLayout) getActivity().findViewById(R.id.fragment_auth_RL_Time);
		exit = (Button) getActivity().findViewById(R.id.fragment_auth_btn_exit);
		fwVersion = (TextView) getActivity().findViewById(R.id.tv_fw_version);
		tv_time = (TextView) getActivity().findViewById(R.id.tv_fragment_detection_time);
		app_version_new=(ImageView) getActivity().findViewById(R.id.app_version_new);
	}
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.fragment_auth_RL_myDevice) {
			deviceManagement();
		} else if (v.getId() == R.id.fragment_auth_RL_DarkBackground) {
			final CustomDialog.Builder builder = new CustomDialog.Builder(getActivity());  
	        builder.setMessage("这个就是自定义的提示框");  
	        builder.setTitle("提示");  
	        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
	            public void onClick(DialogInterface dialog, int which) { 
	            	System.out.println("-----这是确定-----");
	            	testDark();
	                dialog.dismiss();  
	                //设置你的操作事项  
	            }  
	        });  
	        builder.setNegativeButton("取消",  
	                new android.content.DialogInterface.OnClickListener() {  
	                    public void onClick(DialogInterface dialog, int which) { 
	                    	System.out.println("-----这是取消-----");
	                      dialog.dismiss();  
	                    }  
	                });  
	        builder.create().show(); 
	/*	CustomDialog cd=new  CustomDialog(getActivity(), R.style.MyDialog);
	//	View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.select_list_dialog,null);
		cd.setContentView(R.layout.customdialog);
			cd.show();*/
		} else if (v.getId() == R.id.fragment_auth_RL_fw_version) {
			// 检查固件版本
			//clickFw_version();
		} else if (v.getId() == R.id.fragment_auth_RL_upDate) {
			/*try {
				getAppLastestVersion("MAGISPEC_NO_SESSION_ID_FF", "");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
			/*Intent intent=new Intent();
			intent.setClass(getContext(), WelcomeActivity.class);
			startActivity(intent);*/
			//ToastUtil.showToast(getContext(), "已升级到最新版");
		} else if (v.getId() == R.id.fragment_auth_RL_help) {
			/*Intent intent = new Intent();
			intent.setClass(getContext(), HelpActivity.class);
			startActivity(intent);*/
		} else if (v.getId() == R.id.fragment_auth_RL_tellUs) {
			/*Intent intent = new Intent();
			intent.setClass(getContext(), TellUsActivity.class);
			startActivity(intent);*/

		} else if (v.getId() == R.id.fragment_auth_RL_Time) {
			TimePickerDialog pc = new TimePickerDialog(getActivity(), new OnTimeSetListener() {
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					System.out.println(hourOfDay + ":" + minute);

					if (hourOfDay >= hour && minute > min) {
						timehour = (hourOfDay - hour) * 3600 * 1000;
						System.out.println("timehour:" + timehour + "hour" + hour);
					} else {
						System.out.println("timehour:" + timehour + "hour" + hour);
						timehour = (hourOfDay + 24 - hour) * 3600 * 1000;
					}
					timemin = (minute - min) * 60 * 1000;
					time = timehour + timemin;
					String hour = null,min = null;
					if(0<=hourOfDay&&hourOfDay<=9){
						hour="0"+hourOfDay;
					}else{
						hour=""+hourOfDay;
					}
					if(0<=minute&&minute<=9){
						min="0"+minute;
					}else{
						min=""+minute;
					}
					SharePreferenceUtil.saveOrUpdateAttribute(getContext(), "SP", "CALIBRATE_TIME",
							hour + ":" + min);
					System.out.println(time + "::" + timehour + "::" + timemin);
					// ToastUtil.showToast(getActivity(), time + "::" + timehour
					// + "::" + timemin);
					// System.out.println("测试" +
					// DigitalTrans.algorismToHEXString(time) + ":" + time);
					tv_time.setText("每天 " + hour + ":" + min + "开始");
					MainActivity.mhander.sendEmptyMessage(Constant.CALIBRATE_TIME);

				}
			}, 0, 0, true);
			if (MainActivity.ISCONNECT) {
				pc.show();
			} else {
				ToastUtil.showToast(getContext(), "请先连接设备");
			}
		} else if (v.getId() == R.id.fragment_auth_RL_welcomePage) {
			/*Intent intent = new Intent(getContext(), GuideActivity.class);
			startActivity(intent);*/
			
		} else if (v.getId() == R.id.fragment_auth_RL_about) {
			/*Intent intent = new Intent(getContext(), AboutActivity.class);
			startActivity(intent);*/
			/*
			 * String text="测试"; WXTextObject textObj = new WXTextObject();
			 * textObj.text = text; WXMediaMessage msg = new WXMediaMessage();
			 * msg.mediaObject = textObj; msg.description = text;
			 * SendMessageToWX.Req req = new SendMessageToWX.Req(); req.message
			 * = msg; req.scene = SendMessageToWX.Req.WXSceneSession;// 分享
			 * api.sendReq(req);
			 */
			// transaction字段用于唯一标识一个请求

		} else if (v.getId() == R.id.fragment_auth_btn_exit) {
			//
			whetherToQuit();
		}
	}
	private void clickFw_version() {
		if (MainActivity.ISCONNECT) {
			MainActivity.mhander.sendEmptyMessage(Constant.FW_CHECK);
		} else {
			ToastUtil.showToast(getContext(), "请先连接设备");
		}
	}
	private void testDark() {
		if (MainActivity.ISCONNECT) {
			mListener.OnDarkClickListener("设置");
		//	MainActivity.mhander.sendEmptyMessageAtTime(Constant.TEST_DARK, 150);
		} else {
			ToastUtil.showToast(getContext(), "请先连接设备");
		}
	}
	// 是否退出diaglog
	private void whetherToQuit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("提示");
		builder.setMessage("确认退出吗?");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				SharePreferenceUtil.deleteAttributeByKey(getContext(), "SP", "Token");
				Intent intent = new Intent();
				intent.setClass(getContext(), WXLoginActivity.class);
				startActivity(intent);
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
	protected void whetherToOAD() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		builder.setTitle("提示");
		builder.setMessage("是否进行固件升级?");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				MainActivity.mhander.sendEmptyMessage(Constant.OAD);
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
	private void deviceManagement() {
		String temp = (String) SharePreferenceUtil.getAttributeByKey(getContext(), "SP", "ISPAIRED", 0);
		if (temp == null || temp == "") {
			ToastUtil.showToast(getContext(), "你还没有绑定设备");
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
			builder.setTitle("你所绑定的设备mac:");
			builder.setMessage(temp);
			builder.setPositiveButton("解除绑定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					MainActivity.mhander.sendEmptyMessage(Constant.DISCONNECT);
					SharePreferenceUtil.deleteAttributeByKey(getContext(), "SP", "ISPAIRED");
					ToastUtil.showToast(getContext(), "已解绑");
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
	}
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
									String new_version = WelcomeActivity.info.getVersionCode(); // 最新版本的版本号
									System.out.println("new_version:"+"当前版本号为："+new_version);
									// 获取当前版本号
									int now_version = 0;
									try {
										PackageManager packageManager = getActivity().getPackageManager();
										PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
										now_version = packageInfo.versionCode;
									} catch (NameNotFoundException e) {
										e.printStackTrace();
									}
									if (Integer.valueOf(new_version) <=now_version) {
										ToastUtil.showToast(getContext(), "已是最新版本");
									} else {
										ToastUtil.showToast(getContext(), "发现新的版本");
									}
								} else {
									ToastUtil.showToast(getContext(), "服务器异常");
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
						}
					}
				});
	}
}
