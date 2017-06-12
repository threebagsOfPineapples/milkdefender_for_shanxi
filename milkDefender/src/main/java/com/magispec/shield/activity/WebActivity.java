package com.magispec.shield.activity;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.magispec.shield.R;
import com.magispec.shield.network.HttpConnect;
import com.magispec.shield.utils.WxUtils;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class WebActivity extends Activity {
	private WebView webview;
	private ImageView iv_share, iv_back;
	String url, title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_discovery);
		initWindow();
		Intent intent = getIntent();
		url = intent.getStringExtra("url");
		title = intent.getStringExtra("title");
		System.out.println("url-----------------------" + url);
		webview = (WebView) findViewById(R.id.Discovery_webView);
		iv_share = (ImageView) findViewById(R.id.discovery_title_iv_share);
		iv_back = (ImageView) findViewById(R.id.discovery_title_iv_back);
		iv_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		iv_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = url;
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = title;
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.icon);
				msg.thumbData = WxUtils.bmpToByteArray(thumb, true);
				req.message = msg;
				//req.scene = SendMessageToWX.Req.WXSceneTimeline;// 分享
				req.scene=	SendMessageToWX.Req.WXSceneTimeline;										// api.sendReq(req);
			WXLoginActivity.WXapi.sendReq(req);
			}
		});
		// 设置WebView属性，能够执行Javascript脚本
		if (HttpConnect.isNetworkConnected(WebActivity.this)) {
			webview.loadUrl(url);
		} else {
			SVProgressHUD.showErrorWithStatus(WebActivity.this, "请检查网络连接",
					SVProgressHUD.SVProgressHUDMaskType.GradientCancel);
		}
		webview.getSettings().setJavaScriptEnabled(true);
		webview.setWebViewClient(new HelloWebViewClient());
		webview.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				if (newProgress == 100) {
					// 网页加载完成
					// SVProgressHUD.dismiss(getContext());
					// dialog.dismiss();
				} else {
					/*
					 * if (dismiss) { SVProgressHUD.showWithStatus(getContext(),
					 * "加载中", SVProgressHUD.SVProgressHUDMaskType.Black);
					 * dismiss = false; }
					 */
					/*
					 * if( !dialog.isShowing() ){ dialog.show(); }
					 */
				}
			}
		});
	}
	// Web视图
	private class HelloWebViewClient extends WebViewClient {
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}
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
		}
	}
}
