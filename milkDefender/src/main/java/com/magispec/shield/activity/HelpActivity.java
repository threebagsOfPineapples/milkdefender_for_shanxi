package com.magispec.shield.activity;
import com.magispec.shield.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
public class HelpActivity extends Activity implements OnClickListener{	
	private Window mWindow;	
	private ImageView title_tellUs, title_back;	
	@Override	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_help);
		initWindow();
		/**
		 * 设置设定标题栏返回按钮
		 */
		title_back = (ImageView) findViewById(R.id.title_back_imageButton);
		title_back.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				onBackPressed();
			}
		});		
		//初始化组件
		initView();
		//初始化数据
		initData();
	}
	/**
	 * 初始化组件
	 */	
	private void initView() {
		title_tellUs=(ImageView) findViewById(R.id.title_help_tellUs);
		}
	private void initData(){
	title_tellUs.setOnClickListener(this);		
	}
	@Override
	public void onClick(View view) {
	if (view.getId()==R.id.title_help_tellUs) {
		Intent intent =new Intent();
		intent.setClass(getBaseContext(), TellUsActivity.class);
		startActivity(intent);
	}
 }
	@SuppressLint("NewApi")
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
}
