package com.magispec.shield.activity;
import java.net.URLEncoder;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.domain.WX;
import com.magispec.shield.domain.WxInfo;
import com.magispec.shield.https.CloudComm;
import com.magispec.shield.network.GsonRequest;
import com.magispec.shield.service.BaseApplicaton;
import com.magispec.shield.utils.SharePreferenceUtil;
import com.magispec.shield.utils.ToastUtil;
import com.magispec.shield.utils.Utils;
import com.magispec.shield.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import okhttp3.Call;
public class WXLoginActivity extends Activity {
	private Button wxlogin, gslogion;
	private static ProgressDialog dialog;
	public static BaseResp resp;
	public static IWXAPI WXapi;
	private String weixinCode;
	private static String get_access_token = "";
	public static HashMap<String, WxInfo> hashMap;
	WxInfo wxInfo;
	String openid = "";
	String nickname = "";
	String headimgurl = "";
	RequestQueue mQueue;
	private String mSessionId = "";
	private String mIfNewUser = "";
	private String mOpenId = "";
	private String DarkRefId = "";
	String imei;
	// 获取第一步的code后，请求以下链接获取access_token
	public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	// 获取用户个人信息
	public static String GetUserInfo = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	// 利用refresh刷新access_token
	public static String RefreshAccess_token = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_wxlogin);
		TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		imei = telephonyManager.getDeviceId();
		Build bd=new Build();
		System.out.println("imei" + imei+"position"+Constant.Location+"bd.MODEL:"+bd.MODEL+":"+ Build.VERSION.CODENAME + ","
				+ android.os.Build.VERSION.RELEASE+"："+Build.VERSION.SDK_INT+":+Build.USER:"+Build.USER);
		if (WXEntryActivity.resp != null) {
			resp = WXEntryActivity.resp;
		}
		WXapi = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
		WXapi.registerApp(Constant.APP_ID);
		mQueue = Volley.newRequestQueue(this);
		dialog = new ProgressDialog(WXLoginActivity.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("请求网络中...");
		String taken = (String) SharePreferenceUtil.getAttributeByKey(getApplicationContext(), "SP", "Token", 0);
		System.out.println("--------" + taken + "--------");
		if (!taken.isEmpty() && taken.length() > 5) {
			//dialog.show();
			get_access_token = refreshCode(taken);
			System.out.println("taken不为null");
			if (Utils.isNetworkConnected(getBaseContext())){
				WxGetAccessToken();
			}else{
				ToastUtil.showToast(WXLoginActivity.this,"无法连接到网络,请检查网络设置");
			}
		}
		System.out.println("onCreate++++++");
		init(); // initpopwindow();
	}
	private void init() {
		wxlogin = (Button) findViewById(R.id.bt_wxlogin);
		gslogion = (Button) findViewById(R.id.gs_login);
	//	wxlogin.setEnabled(false);
		wxlogin.setOnClickListener(listener);
		gslogion.setOnClickListener(listener);
	}
	private OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.bt_wxlogin) {
				/*if (isWXAppInstalledAndSupported()) {
					System.out.println("执行登录");
					if(Utils.isNetworkConnected(getBaseContext())){
					WXLogin();
					}else{
						ToastUtil.showToast(WXLoginActivity.this,"无法连接到网络,请检查网络设置");
					}
				} else {
					ToastUtil.showToast(getBaseContext(), "您手机的微信版本太低,请升级微信版本");
				}*/
				/*
				 * CloudComm a=new CloudComm(); try {
				 * a.createSession("MAGISPEC_UOLOAD_SPECTRUM_FF"); } catch
				 * (JSONException e) { // TODO Auto-generated catch block
				 * e.printStackTrace(); }
				 */
				/*
				 * Intent intent = new Intent();
				 * intent.setClass(WXLoginActivity.this, OADActivity.class);
				 * startActivity(intent); finish();
				 */
				ToastUtil.showToast(getBaseContext(), "暂未开放");
			} else if (v.getId() == R.id.gs_login) {
				if(Utils.isNetworkConnected(getBaseContext())){
				try {
					createSession(imei, "APPTYPE_SHIELD","0", "1");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ToastUtil.showToast(getBaseContext(),"服务器异常");
					e.printStackTrace();
				}
				}else{
					ToastUtil.showToast(WXLoginActivity.this,"无法连接到网络,请检查网络设置");
				}
			}
		};
	};
	protected void skipActivity() {
		Intent intent = new Intent();
		intent.setClass(this, MainActivity.class);
		startActivity(intent);
		finish();
		overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
	}
	/**
	 * 登录微信
	 */
	private void WXLogin() {
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo";
		WXapi.sendReq(req);
	}
	/**
	 * 获取taken
	 */
	private void Get_Taken() {
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo";
		WXapi.sendReq(req);
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			super.onBackPressed();
			SVProgressHUD.dismiss(WXLoginActivity.this);
			return true;
		}
		return false;
	}
	private boolean isWXAppInstalledAndSupported() {
		IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
		msgApi.registerApp(Constant.APP_ID);
		boolean sIsWXAppInstalledAndSupported = msgApi.isWXAppInstalled() && msgApi.isWXAppSupportAPI();
		return sIsWXAppInstalledAndSupported;
	}
	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("OnResume++++++");
		/*
		 * resp是你保存在全局变量中的
		 */
		if (resp != null) {
			dialog.show();
			if (resp.errCode == BaseResp.ErrCode.ERR_OK && resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
				System.out.println("我执行了 resp不为空");
				// code返回
				weixinCode = ((SendAuth.Resp) resp).code;
				System.out.println("weixinCode" + "---" + weixinCode);
				/*
				 * 将你前面得到的AppID、AppSecret、code，拼接成URL
				 */
				get_access_token = getCodeRequest(weixinCode);
				resp = null;
				// SVProgressHUD.showWithStatus(getBaseContext(), "加载中",
				// SVProgressHUD.SVProgressHUDMaskType.Black);
				Thread thread = new Thread(downloadRun);
				thread.start();
				try {
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	/**
	 * 获取access_token的URL（微信）
	 * 
	 * @param code
	 *            授权时，微信回调给的
	 * @return URL
	 */
	public static String getCodeRequest(String code) {
		String result = null;
		result = GetCodeRequest.replace("APPID", urlEnodeUTF8(Constant.APP_ID));
		result = result.replace("SECRET", urlEnodeUTF8(Constant.App_SECRET));
		result = result.replace("CODE", urlEnodeUTF8(code));
		return result;
	}
	public static String refreshCode(String refresh) {
		String result = null;
		result = RefreshAccess_token.replace("APPID", urlEnodeUTF8(Constant.APP_ID));

		result = result.replace("REFRESH_TOKEN", urlEnodeUTF8(refresh));
		System.out.println(result + "-result");
		return result;
	}
	/**
	 * 获取用户个人信息的URL（微信）
	 * 
	 * @param access_token
	 *            获取access_token时给的
	 * @param openid
	 *            获取access_token时给的
	 * @return URL
	 */
	public static String getUserInfo(String access_token, String openid) {
		// System.out.println("at:"+access_token+"###openid:");
		String result = null;
		if (urlEnodeUTF8(access_token) != null) {
			result = GetUserInfo.replace("ACCESS_TOKEN", urlEnodeUTF8(access_token));
			result = result.replace("OPENID", urlEnodeUTF8(openid));
		} else {
			System.out.println("--------为null---------");
		}
		return result;
	}
	public static String urlEnodeUTF8(String str) {
		String result = str;
		System.out.println("---str----" + str);
		try {
			if (str != null) {
				result = URLEncoder.encode(str, "UTF-8");
			} else {
				ToastUtil.showToast(BaseApplicaton.getAppContext(), "微信登录已过期,请重新登陆");
				dialog.dismiss();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	public Runnable downloadRun = new Runnable() {

		@Override
		public void run() {
			WxGetAccessToken();
		}
	};
	// 自定义Json Request
	private void WxGetAccessToken() {
		GsonRequest<WX> gsonRequest1 = new GsonRequest<WX>(get_access_token, WX.class, new Response.Listener<WX>() {
			@Override
			public void onResponse(WX a) {
				System.out.println("token" + get_access_token);
				String access_token = a.getAccess_token();
				String open_id = a.getOpenid();
				String get_user_info_url = getUserInfo(access_token, open_id);
				// System.out.println(get_user_info_url);
				Log.d("TAG", "getOpenid is " + open_id);
				Log.d("TAG", "getAccess_token is " + access_token);
				Log.d("TAG", "getRefresh_token is " + a.getRefresh_token());
				Log.d("TAG", "getRefresh_token is " + a.getExpires_in());
				Log.d("TAG", "getRefresh_token is " + a.getUnionid());
				WxGetInformation(get_user_info_url);
				if (a.getRefresh_token() != null) {
					System.out.println("Sp保存");
					SharePreferenceUtil.saveOrUpdateAttribute(getApplicationContext(), "SP", "Token",
							a.getRefresh_token());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
			}
		});
		mQueue.add(gsonRequest1);
	}
	private void WxGetInformation(String url) {
		System.out.println("url:" + url);
		GsonRequest<WxInfo> gsonRequest = new GsonRequest<WxInfo>(url, WxInfo.class, new Response.Listener<WxInfo>() {
			@Override
			public void onResponse(WxInfo a) {
				if (a == null) {
					System.out.println("a=null");
					wxInfo = a;
				}
				System.out.println(
						a.getHeadimgurl() + "url" + a.getCity() + a.getCountry() + a.getUnionid() + a.getOpenid());
				hashMap = new HashMap<>();
				hashMap.put("a", a);
				if (a.getOpenid() != null) {
					Constant.OPENID = a.getOpenid();
					try {
						CreateSession();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						ToastUtil.showToast(WXLoginActivity.this, "服务器异常");
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e("TAG", error.getMessage(), error);
				System.out.println("我执行了");
			}
		});
		mQueue.add(gsonRequest);
	}
	private void CreateSession()  {
		if (Constant.OPENID != null) {
			try {
				createSession(Constant.OPENID, "APPTYPE_MILKDEFENDER", "0","1");
			} catch (Exception e) {
				e.printStackTrace();
				ToastUtil.showToast(getBaseContext(),"服务器异常");
			}
		}
	}
	public void createSession(final String openId, String type, String appRole,String logintype) {
		JSONObject json = new JSONObject();
		JSONObject json_resp = null;
		if (!mSessionId.isEmpty()) {
			System.out.println("Session just can create for once ...");
		}
		try {
			json.put(Constant.JSON_KEY_SESSIONID, null);
			json.put(Constant.JSON_KEY_OPENID, openId);
			json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_CREATE_SESSION);
			json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
			JSONObject data = new JSONObject();
			data.put("apptype", type);
			data.put("appRole",appRole);
			data.put("logintype", logintype);
			data.put("username", "usermagispec");// need to hide ...???
			data.put("password", "11qqaazZ");
			json.put(Constant.JSON_KEY_DATA, data);
		} catch (JSONException e) {
			ToastUtil.showToast(getBaseContext(),"数据处理异常");
			e.printStackTrace();
		}
		System.out.println("json" + json);
		OkHttpUtils.post().url(Constant.HTTPS_URL_LOGIN).addParams("json", json.toString()).build()
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
							} catch (Exception  e) {
								ToastUtil.showToast(WXLoginActivity.this, "服务器异常");
							}
							System.out.println("string:" + string + "json:" + json_resp);
							try {
								JSONObject data = (JSONObject) json_resp.get(Constant.JSON_KEY_DATA);
								if (data != null) {
									mSessionId = data.getString("sessionID");
									mIfNewUser = data.getString("ifNewUser");
									System.out.println("createOk" + "mId" + mSessionId + "NU" + mIfNewUser);
									Constant.OPENID = openId;
									Constant.SESSIONID = mSessionId;
									dialog.dismiss();
									ToastUtil.showToast(getApplicationContext(), "登录成功");
									CloudComm cc = new CloudComm();
									String userName = null;
									String IsUserNameChanged = (String) SharePreferenceUtil
											.getAttributeByKey(getApplication(), "SP", "IsUserNameChanged", 0);

									if (WXLoginActivity.hashMap != null && WXLoginActivity.hashMap.get("a") != null) {
										wxInfo = WXLoginActivity.hashMap.get("a");
										userName = wxInfo.getNickname();

									} else if (IsUserNameChanged == "" || IsUserNameChanged == " ") {
										userName = "游客";
									} else {
										userName = IsUserNameChanged;
									}
									if (!IsUserNameChanged.equals(userName)) {
										SharePreferenceUtil.saveOrUpdateAttribute(getApplication(), "SP",
												"IsUserNameChanged", userName);
										cc.setUserName(Constant.OPENID, Constant.SESSIONID, userName);
									}
									skipActivity();
								} else {
									ToastUtil.showToast(WXLoginActivity.this, "服务器异常");
								}
							} catch (Exception e) {
								ToastUtil.showToast(WXLoginActivity.this, "服务器异常");
							}
						} else {

						}
					}
				});
	}
}
