package com.magispec.shield.activity;

import com.magispec.shield.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
public class AboutActivity extends Activity {
	private RelativeLayout rl_appVersionName, rl_telePhoneNumber;
	private TextView tv_appVersionName,tv_phoneNumber;
	private ImageView iv_back;
	private PopupWindow pop=null;
	private LinearLayout ll_popup;
	private View parentView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		parentView = getLayoutInflater().inflate(R.layout.activity_about, null);
		setContentView(parentView);
		initWindow();
		popwindow();
		rl_appVersionName = (RelativeLayout) findViewById(R.id.activity_about_rl_appVersionName);
		rl_telePhoneNumber=(RelativeLayout) findViewById(R.id.activity_about_rl_telePhoneNumber);
		tv_appVersionName = (TextView) findViewById(R.id.activity_about_tv_appVersionName);
		tv_phoneNumber=(TextView) findViewById(R.id.activity_about_tv_phoneNumber);
		iv_back = (ImageView) findViewById(R.id.title_back_imageButton);
		tv_appVersionName.setText(MainActivity.getVersionName(this));
		iv_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
		rl_telePhoneNumber.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//whetherToQuit();
				pop.showAtLocation(parentView, Gravity.BOTTOM, 0, 0);
			}
		});	
	}
	@SuppressWarnings("deprecation")
	private void popwindow() {
		pop = new PopupWindow(AboutActivity.this);  
        View view = getLayoutInflater().inflate(R.layout.item_popupwindows, null);  
        ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);  
        pop.setWidth(LayoutParams.MATCH_PARENT);  
        pop.setHeight(LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);  
        pop.setOutsideTouchable(true);  
        pop.setContentView(view);  
		Button bt1=(Button) view.findViewById(R.id.item_popupwindows_time);
		Button bt2=(Button) view.findViewById(R.id.item_popupwindows_call);
		Button bt3=(Button) view.findViewById(R.id.item_popupwindows_cancel);
		bt1.setEnabled(false);
		bt2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
				Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + tv_phoneNumber.getText()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
			}
		});
		bt3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pop.dismiss();
			}
		});
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
			window.setStatusBarColor(Color.TRANSPARENT);// calculateStatusColor(Color.WHITE,													// (int) alphaValue)
		}
	}
	private void whetherToQuit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(AboutActivity.this);
		builder.setTitle("提示");
		builder.setMessage("呼叫客服");
		builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();			
				Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + tv_phoneNumber.getText()));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				finish();
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
