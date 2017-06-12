package com.magispec.shield.activity;

import org.json.JSONException;
import org.json.JSONObject;
import com.magispec.shield.R;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.utils.ToastUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import okhttp3.Call;
public class TellUsActivity extends Activity{
	private Window mWindow;
	private ImageView title_back;
	private EditText et1,et2;
	private Button btn_tellUs;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_tellus);
		initWindow();
		et1=(EditText) findViewById(R.id.et1);
		et2=(EditText) findViewById(R.id.et2);
		btn_tellUs=(Button) findViewById(R.id.btn_tellus);
		/**
		 * 设置设定标题栏返回按钮
		 */
		title_back = (ImageView) findViewById(R.id.title_back_imageButton);
		title_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});
		btn_tellUs.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				 String suggestion=et1.getText().toString();
				 String phoneno=et2.getText().toString();
				 if( suggestion.length()>1){
			try {
				tellUs(Constant.SESSIONID, Constant.OPENID, suggestion, phoneno);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	} else{
				ToastUtil.showToast(TellUsActivity.this, "不能为空");
			}
			}
		});
	}
	@SuppressLint({ "InlinedApi", "NewApi" })
	private void initWindow() {
		// TODO Auto-generated method stub
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){//4.4 全透明状态栏
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			}
			if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0 全透明实现
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
			| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);//calculateStatusColor(Color.WHITE, (int) alphaValue)
			}
	}
	private void tellUs( String mSessionId,String  mOpenId,String suggestion,String phoneno) throws JSONException{
	 JSONObject json = new JSONObject();
		json.put(Constant.JSON_KEY_SESSIONID, mSessionId);
		json.put(Constant.JSON_KEY_OPENID, mOpenId);
		json.put(Constant.JSON_KEY_ACTION, Constant.ACTION_MILKDEFENDER_SUGGESTION);
		json.put(Constant.JSON_KEY_TYPE, Constant.MSG_REQ);
		JSONObject data = new JSONObject();
		data.put("suggestion", suggestion);
		data.put("phoneno", phoneno);
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
						if (string != null) {
							try {
								json_resp = new JSONObject(string);
							} catch (JSONException e) {
								e.printStackTrace();
							}
							System.out.println("string:" + string + "json:" + json_resp);
							try {
								if (json_resp.get(Constant.JSON_KEY_RESULT).equals("OK")) {
									ToastUtil.showToast(TellUsActivity.this, "反馈成功，感谢您的支持");
									onBackPressed();
								} else {
									ToastUtil.showToast(TellUsActivity.this, "服务器异常,请稍后再试");
								}
							} catch (JSONException e) {
								ToastUtil.showToast(TellUsActivity.this, "服务器数据异常,请稍后再试");
								e.printStackTrace();
							}
						} else {
							ToastUtil.showToast(TellUsActivity.this, "服务器异常,请稍后再试");
						}
					}
				});
	}
}
