package com.magispec.shield.activity;

import java.util.List;
import java.util.Timer;

import com.magispec.shield.R;
import com.magispec.shield.ble.BluetoothLeService;
import com.magispec.shield.ble.SampleGattAttributes;
import com.magispec.shield.utils.DigitalTrans;
import com.magispec.shield.widgets.CircleProgress;
import com.magispec.shield.widgets.DonutProgress;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class OADActivity extends Activity {
	private Timer timer;
	private DonutProgress donutProgress;
	private CircleProgress circle;
	// 定义蓝牙适配器
	private BluetoothAdapter bluetoothAdapter;
	public static BluetoothLeService mBluetoothLeService;// 自定义的一个继承自Service的服务
	public static BluetoothGattCharacteristic mCharacteristic, mCharacteristicNotify;
	public static BluetoothGattService mnotyGattService;// 三个长得很像，由大到小的对象BluetoothGatt、
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_oad);
		initWindow();
		// donutProgress=(DonutProgress) findViewById(R.id.circle_progress);
		circle = (CircleProgress) findViewById(R.id.circle_progress1);
		circle.setMaxProgress(MainActivity.block.size()-1);
		timer = new Timer();
		if (MainActivity.mBluetoothLeService != null && MainActivity.ISCONNECT) {
			System.out.println("我执行了这一步");
			mBluetoothLeService = MainActivity.mBluetoothLeService;
			mnotyGattService = mBluetoothLeService.getSupportedGattServices(SampleGattAttributes.OAD_SERVICE_UUID);// 找特定的某个服务
	List<BluetoothGattService> a=mBluetoothLeService.getSupportedGattServices();
	for (int i = 0; i < a.size(); i++) {
		System.out.println("所支持的服务"+i+":"+a.get(i).getUuid());
	}
	List<BluetoothGattCharacteristic> b=     mnotyGattService.getCharacteristics();
	
	for (int i = 0; i < b.size(); i++) {
		System.out.println("所支持的服务特征"+i+":"+b.get(i).getUuid());
	}
			mCharacteristic = mnotyGattService.getCharacteristic(SampleGattAttributes.OAD_CHARACTER);// 获取可读写的特征值
			 byte[] sen = { 0x52, (byte) 0xf4 };
				mBluetoothLeService.writeCharacteristic(mCharacteristic,
				 sen);
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
														// (int) alphaValue)
		}
	}
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
		intentFilter.addAction(BluetoothLeService.ACTION_GET_FW_VER);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_DARK_SPECTRUM_DATA);
		intentFilter.addAction(BluetoothLeService.ACTION_SET_REF_SPECTRUM_DATA);
		intentFilter.addAction(BluetoothLeService.ACTION_UPGRADE);
		return intentFilter;
	}
	private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			final String action = intent.getAction();
			int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
			// 通过intent获得的不同action，来区分广播该由谁接收(只有action一致,才能接收)。
			if (BluetoothLeService.ACTION_IMAGB.equals(action)) {
				int value;
				byte[] data = BluetoothLeService.data;
				value = data[0] & 0xff;
				value |= (data[1] & 0xff) << 8;
				// System.out.println("value : "+value);
				// System.out.println("data的长度为"+data.length);
				if (value < MainActivity.block.size()) {
					byte[] send = new byte[18];
					send[0] = data[0];
					send[1] = data[1];
					circle.setProgress(value);
					for (int i = 0; i < 16; i++) {
						send[i + 2] = MainActivity.block.get(value)[i];
					}
					System.out.println(value + ":" + DigitalTrans.byte2hex(send));
					mBluetoothLeService.writeCharacteristic(mCharacteristic, send);
				} else if (value < 6553) {
					
					System.out.println("--要求传输错误--超过block的长度-");
					bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
					bluetoothAdapter.disable();
					finish();
				}
			}
		}
	};
	protected void onPause() {
		unregisterReceiver(mGattUpdateReceiver);
		super.onPause();
	};
	protected void onResume() {
		registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		super.onResume();
	};
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
	    if(keyCode == KeyEvent.KEYCODE_BACK) { //监控/拦截/屏蔽返回键 
	        return true; 
	    } else if(keyCode == KeyEvent.KEYCODE_MENU) {//MENU键 
	        //监控/拦截菜单键 
	         return true; 
	    }     
	return super.onKeyDown(keyCode, event); 
	}

}
