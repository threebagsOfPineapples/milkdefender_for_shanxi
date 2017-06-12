/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.magispec.shield.ble;

import java.lang.reflect.Method;
import java.util.List;
import java.util.UUID;

import com.magispec.shield.activity.MainActivity;

import android.annotation.SuppressLint;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
/**
 * Service for managing connection and data communication with a GATT server
 * hosted on a given Bluetooth LE device.
 */
@SuppressLint("NewApi")
public class BluetoothLeService extends Service {
	private final static String TAG = BluetoothLeService.class.getSimpleName();
	// BLE
	private BluetoothManager mBluetoothManager = null;
	private BluetoothAdapter mBtAdapter = null;
	private static BluetoothGatt mBluetoothGatt = null;
	private static BluetoothLeService mThis = null;
	private volatile boolean mBusy = false; // Write/read pending response
	private String mBluetoothDeviceAddress;
	private int state;
	private int length=0;
	private byte cmd;
	public static boolean OK = false;
	@SuppressWarnings("unused")
	private int mConnectionState = STATE_DISCONNECTED;
	private BTMsg mMsg = new BTMsg();
	private MsgDataProcessing dataProcessing = new MsgDataProcessing();
	private static final int STATE_DISCONNECTED = 0;
	private static final int STATE_CONNECTING = 1;
	private static final int STATE_CONNECTED = 2;
	public static byte[] data;	
	StringBuffer a = new StringBuffer();
	public final static String ACTION_CALI_DARK_FINISHED = "com.example.bluetooth.le.CALI_DARK_FINISHED";
	public final static String ACTION_CALI_REF1_FINISHED = "com.example.bluetooth.le.CALI_REF1_FINISHED";
	public final static String ACTION_CALI_REF2_FINISHED = "com.example.bluetooth.le.CALI_REF2_FINISHED";
	public final static String ACTION_TO_DETECTION = "com.example.bluetooth.le.ACTION_TO_DETECTION";
	public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
	public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
	public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
	public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
	public final static String ACTION_COLLECT = "com.example.bluetooth.le.ACTION_COLLECT";
	public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";
	public final static String IMAGEB_DATA = "com.example.bluetooth.le.IMAGEB_DATA";
	public final static String TEST_TIME = "com.example.bluetooth.le.TEST_TIME";
	public final static String ACTION_PAIR = "com.example.bluetooth.le.ACTION_PAIR";
	public final static String ACTION_CONN_AUTHORIZE = "com.example.bluetooth.le.ACTION_CONN_AUTHORIZE";
	public final static String ACTION_GET_FW_VER = "com.example.bluetooth.le.ACTION_GET_FW_VER";
	public final static String ACTION_FONT = "com.example.bluetooth.le.ACTION_FONT";
	public final static String ACTION_FONT_Name="com.example.bluetooth.le.ACTION_FONT_NAME";
	public final static String ACTION_UPGRADE = "com.example.bluetooth.le.ACTION_UPGRADE";
	public final static String ACTION_IMAGB = "com.example.bluetooth.le.ACTION_IMAGEB";
	public final static String EXTRA_STATUS = "com.example.bluetooth.le.EXTRA_STATUS";
	public final static String EXTRA_ADDRESS = "com.example.bluetooth.le.EXTRA_ADDRESS";
	public final static String ACTION_SET_DARK_SPECTRUM_DATA = "com.example.bluetooth.le.ACTION_DARK_SET";
	public final static String ACTION_SET_REF_SPECTRUM_DATA = "com.example.bluetooth.le.ACTION_REF_SET";
	public final static String ACTION_GET_CALIBRATE_TIME="com.example.bluetooth.le.ACTION_GET_CALIBRATE_TIME";
	public final static String ACTION_SET_TEST_OBJ="com.example.bluetooth.le.ACTION_SET_TEST_OBJ";
	public final static String ACTION_ERROR_INFO="com.example.bluetooth.le.ACTION_ERROR_INFO";
	public final static String ACTION_SET_AUTO_CALIBRATE_TIME="com.example.bluetooth.le.ACTION_SET_AUTO_CALIBRATE_TIME";
	public final static String ACTION_GET_WAVE_MAP="com.example.bluetooth.le.ACTION_GET_WAVE_MAP";
	public final static String ACTION_SET_WAVE_MAP="com.example.bluetooth.le.ACTION_SET_WAVE_MAP";
//	public final static String ACTION_
	// ble特征值UUID
	@SuppressWarnings("unused")
	private static final UUID UUID_READ_WRITE = UUID.fromString(SampleGattAttributes.BLE_READ_WRITE);
	/*
	 * Implements callback methods for GATT events that the app cares about. For
	 * example, connection change and services discovered.
	 * 这里有9个要实现的回调方法，看情况要实现那些，用到那些就实现那些
	 * 如果只是在某个点接收(有客户端请求)，可以用读;如果要一直接收(无客户端请求)，要用notify
	 */
	/**
	 * GATT client callbacks
	 */
	private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {
		@Override
		// 连接或者断开蓝牙，方法一
		public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
			if (mBluetoothGatt == null) {
				Log.e(TAG, "mBluetoothGatt not created!");
				return;
			}
			String intentAction;
			if (newState == BluetoothProfile.STATE_CONNECTED) {
				intentAction = ACTION_GATT_CONNECTED;
				mConnectionState = STATE_CONNECTED;
				broadcastUpdate(intentAction);
				Log.i(TAG, "Connected to GATT server.");
				/*
				 * Attempts to discover services after successful connection.
				 * 函数调用之间存在先后关系。例如首先需要connect上才能discoverServices。
				 */
				Log.i(TAG, "Attempting to start service discovery:" + mBluetoothGatt.discoverServices());
			} else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
				intentAction = ACTION_GATT_DISCONNECTED;
				mConnectionState = STATE_DISCONNECTED;
				//ToastUtil.showToast(getBaseContext(), "连接中断");
				//connect(MainActivity.mDeviceAddress);
				Log.i(TAG, "Disconnected from GATT server.");
				broadcastUpdate(intentAction);
			}
		}
		// 发现服务的回调，发送广播，方法二
		@Override
		public void onServicesDiscovered(BluetoothGatt gatt, int status) {
			System.out.println("服务回调");
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED);
				System.out.println("服务回调===================================");
			} else {
				Log.w(TAG, "onServicesDiscovered received: " + status);
			}
		}
		// write的回调,发送广播，方法三
		@Override
		public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			Log.e("WRITE", "onCharacteristicWrite() - status: " + status + "  - UUID: " + characteristic.getUuid());
			// write回调失败 status=128， read回调失败status=128. status=0,回调成功；=9，数组超长
			if (status == BluetoothGatt.GATT_SUCCESS) {
				// broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
				Log.e("WRITE SUCCESS", "回调成功 " + status + "  - UUID: " + characteristic.getUuid());
			} else {
				Log.e("FAIL", "回调失败 " + status + "  - UUID: " + characteristic.getUuid());
			}
		}
		// read回调，方法四
		@Override
		public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
			if (status == BluetoothGatt.GATT_SUCCESS) {
				broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic);
				Log.e("READ SUCCESS",
						"onCharacteristicRead() - status: " + status + "  - UUID: " + characteristic.getUuid());
			}
		}
		// notification回调，方法五
		@Override
		public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
			data = characteristic.getValue();
			System.out.println("data的长度" + data.length);
			if (MainActivity.RTOAD) {
				broadcastUpdate(ACTION_IMAGB, characteristic);
			} else {
				int state = mMsg.assembleMsgByByte(data);
				if (state == BTMsg.MSG_STATE_PROCESSING) {
					dataProcessing.putMsg(mMsg);
					System.out.println(mMsg.getmCmd() + "getCmd" + "++++++++++++++++++++++++++++++++++++++++++++++++");
					dataProcessing.dataProcessing();
					switch (mMsg.getmCmd()) {
					case BTMsg.MSG_CMD_CONN_AUTHORIZE:
						System.out.println("---身份校验-----");
						broadcastUpdate(ACTION_CONN_AUTHORIZE);
						break;
					case BTMsg.MSG_CMD_SPECTRUM_NOTIFY:
						System.out.println("++++++++" + OK);
						if (OK) {
							broadcastUpdate(ACTION_TO_DETECTION);
						}
						System.out.println("我发送数据了");
						OK = false;
						break;
					case BTMsg.MSG_CMD_DARKEVN_CALIB:
						System.out.println("!!!MSG_CMD_DARKEVN_CALIB");
						broadcastUpdate(ACTION_CALI_DARK_FINISHED);
						break;
					case BTMsg.MSG_CMD_REF1_CALIB:
						System.out.println("!!!MSG_CMD_REF1_CALIB");
						broadcastUpdate(ACTION_CALI_REF1_FINISHED);
						break;
					case BTMsg.MSG_CMD_SET_DARK_SPECTRUM_DATA:
						System.out.println("!!!MSG_CMD_SET_DARK_CALIB");
						broadcastUpdate(ACTION_SET_DARK_SPECTRUM_DATA);
						break;
					case BTMsg.MSG_CMD_SET_REF_SPECTRUM_DATA:
						System.out.println("!!!MSG_CMD_SET_REF_CALIB");
						broadcastUpdate(ACTION_SET_REF_SPECTRUM_DATA);
						break;
					case BTMsg.MSG_CMD_PAIR:
						System.out.println("!!!MSG_CMD_PAIR");
						 broadcastUpdate(ACTION_PAIR);
						break;
					case BTMsg.MSG_CMD_GET_FW_VER:
						System.out.println("MSG_CMD_GET_FW_VER-获取版本号-");
						broadcastUpdate(ACTION_GET_FW_VER);
						break;
					case BTMsg.MSG_CMD_COLLECT:
						System.out.println("MSG_CMD_COLLECT-收集");
						broadcastUpdate(ACTION_COLLECT);
						break;
					case BTMsg.MSG_CMD_FONT:
						System.out.println("----------字库传输---------");
						broadcastUpdate(ACTION_FONT);
						break;
					case BTMsg.MSG_CMD_FONT_NAME:
						System.out.println("----------字库名称传输---------");
						broadcastUpdate(ACTION_FONT_Name);
						break;
						// TODO OAD升级
					case BTMsg.MSG_CMD_UPGRADE:
						System.out.println("----------OAD升级传输---------");
						broadcastUpdate(ACTION_UPGRADE);
						break;
					case BTMsg.MSG_GET_CALIBRATE_TIME:
						System.out.println("-----获取上次校准时间------");
						broadcastUpdate(ACTION_GET_CALIBRATE_TIME);
						break;
					case BTMsg.MSG_SET_AUTO_CALIBRATE_TIME:
						System.out.println("-----设置校准时间------");
						broadcastUpdate(ACTION_SET_AUTO_CALIBRATE_TIME);
						break;
					case BTMsg.MSG_GET_WAVE_MAP	:
						System.out.println("---获取波长映射数据---");
						broadcastUpdate(ACTION_GET_WAVE_MAP);
						break;
					case BTMsg.MSG_SET_WAVE_MAP	:
						System.out.println("---设置波长映射数据---");
						broadcastUpdate(ACTION_SET_WAVE_MAP);
						break;
					case BTMsg.MS_TM_SET_TEST_OBJ:
						System.out.println("--设置为未知物品--");
						broadcastUpdate(ACTION_SET_TEST_OBJ);
						break;
					case BTMsg.MS_TM_ERROR_INFO:
						System.out.println("--设置错误信息--");
						broadcastUpdate(ACTION_ERROR_INFO);						
						break;
					default:
						break;
					}
					mMsg.clear();
				}
				Log.e("zmx", "--------onCharacteristicChanged-----"+"state:  "+state);
			}
		}
	};
	// 当一个特定的回调函数被触发，它会调用适当的broadcastUpdate()辅助方法并传递一个action。
	// 这个broadcastUpdate方法，实现蓝牙状态（即方法一、二）的广播
	private void broadcastUpdate(final String action) {
		final Intent intent = new Intent(action);
		sendBroadcast(intent);
	}
	// 复写broadcastUpdate方法，来实现蓝牙其他状态的广播（方法三、四、五的回调，蓝牙最重要的三个方法）
	// 注意,本节中的数据解析执行按照蓝牙心率测量概要文件规范
	// Parameters intent ： The Intent to broadcast;
	// all receivers matching this Intent will receive the broadcast.
	// an intent with a given action.
	private void broadcastUpdate(String action, BluetoothGattCharacteristic characteristic) {
		Intent intent = new Intent(action);
		// 配置文件，用十六进制发送和接收数据。
		byte[] data = characteristic.getValue();
		StringBuilder stringBuilder = new StringBuilder(data.length);// StringBuilder非线程安全，执行速度最快
		if (data != null && data.length > 0) {
			for (byte byteChar : data)
				stringBuilder.append(String.format("%02X", byteChar));// "%02x "以FF FF形式解析数据(注意有无空格)
			intent.putExtra(EXTRA_DATA, stringBuilder.toString());
		}
		Log.e("广播发送", "特征值长度" + characteristic.getValue().length + "  " + stringBuilder.toString());
		sendBroadcast(intent);
	}
	public class LocalBinder extends Binder {
		public BluetoothLeService getService() {
			return BluetoothLeService.this;	}
	}
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		System.out.println("是否解除绑定");
		close();
		return super.onUnbind(intent);
	}
	private final IBinder mBinder = new LocalBinder();
	/**
	 * Initializes a reference to the local Bluetooth adapter.
	 *
	 * @return Return true if the initialization is successful.
	 */
	public boolean initialize() {
		if (mBluetoothManager == null) {
			mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
			if (mBluetoothManager == null) {
				Log.e(TAG, "Unable to initialize BluetoothManager.");
				return false;
			}
		}
		mBtAdapter = mBluetoothManager.getAdapter();
		if (mBtAdapter == null) {
			Log.e(TAG, "Unable to obtain a BluetoothAdapter.");
			return false;
		}
		return true;
	}
	/**
	 * Connects to the GATT server hosted on the Bluetooth LE device.
	 *
	 * @param address
	 *            The device address of the destination device.
	 *
	 * @return Return true if the connection is initiated successfully. The
	 *         connection result is reported asynchronously through the
	 *         {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 *         callback.
	 */
	public boolean connect(final String address) {
		if (mBtAdapter == null || address == null) {
			Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
			System.out.println(address + "");
			return false;
		}
		// Previously connected device. Try to reconnect.
		if (mBluetoothDeviceAddress != null && address.equals(mBluetoothDeviceAddress) && mBluetoothGatt != null) {
			Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
		List<BluetoothGattService> a=getSupportedGattServices();
		if(a!=null){
			for (int i = 0; i < a.size(); i++) {
				System.out.println("getUuid"+i+":"+a.get(i).getUuid());
			}
		}else{
			System.out.println("a为null");
		}
			if (mBluetoothGatt.connect()) {
				System.out.println("connect 为true");
				mConnectionState = STATE_CONNECTING;
				return true;
			} else {
				return false;
			}
		}
		final BluetoothDevice device = mBtAdapter.getRemoteDevice(address);
		if (device == null) {
			Log.w(TAG, "Device not found.  Unable to connect.");
			return false;
		}
		/**
		 * We want to directly connect to the device, so we are setting the
		 * autoConnect parameter to false. device.connectGatt连接到GATT
		 * Server,并返回一个BluetoothGatt实例.
		 * mGattCallback为回调函数BluetoothGattCallback（抽象类）的实例。
		 */
		mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
		// 刷新缓存
		refreshDeviceCache(mBluetoothGatt);
		Log.d(TAG, "Trying to create a new connection.");
		System.out.println("尝试清理缓存");
		mBluetoothDeviceAddress = address;
		mConnectionState = STATE_CONNECTING;
		return true;
	}
	/**
	 * Disconnects an existing connection or cancel a pending connection. The
	 * disconnection result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onConnectionStateChange(android.bluetooth.BluetoothGatt, int, int)}
	 * callback.
	 */
	public void disconnect() {
		if (mBtAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.disconnect();
		mBluetoothGatt.close();
	}
	/**
	 * 利用反射调用Gatt中的方法
	 * 
	 * @param gatt
	 * @return
	 */
	private boolean refreshDeviceCache(BluetoothGatt gatt) {
		try {
			BluetoothGatt localBluetoothGatt = gatt;
			Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
			if (localMethod != null) {
				boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
				return bool;
			}
		} catch (Exception localException) {
			Log.e(TAG, "An exception occured while refreshing device");
		}
		return false;
	}
	/**
	 * 清理缓存
	 */
	public static boolean refreshDeviceCachea(BluetoothGatt gatt) {
        /*
         * There is a refresh() method in BluetoothGatt class but for now it's hidden. We will call it using reflections.
		 */
        try {
            final Method refresh = BluetoothGatt.class.getMethod("refresh");
            if (refresh != null) {
                final boolean success = (Boolean) refresh.invoke(mBluetoothGatt);
                Log.i(TAG, "Refreshing result: " + success);
                return success;
            }
        } catch (Exception e) {
            Log.e(TAG, "An exception occured while refreshing device", e);
        }
        return false;
    }
	/**
	 * After using a given BLE device, the app must call this method to ensure
	 * resources are released properly.
	 */
	public void close() {
		if (mBluetoothGatt == null) {
			return;
		}
		mBluetoothGatt.close();
		mBluetoothGatt = null;
		System.out.println("关闭服务");
	}
	/**
	 * Request a read on a given {@code BluetoothGattCharacteristic}. The read
	 * result is reported asynchronously through the
	 * {@code BluetoothGattCallback#onCharacteristicRead(android.bluetooth.BluetoothGatt, 
	 * android.bluetooth.BluetoothGattCharacteristic, int)} callback.
	 *
	 * @param characteristic
	 *            The characteristic to read from.
	 * 
	 *            为应用方便，复写了readCharacteristic()方法
	 */
	public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
		if (mBtAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.readCharacteristic(characteristic);
	}
	/**
	 * Enables or disables notification on a give characteristic.
	 *
	 * @param characteristic
	 *            Characteristic to act on.
	 * @param enabled
	 *            If true, enable notification. False otherwise.
	 * 
	 *            复写setCharacteristicNotification()
	 */
	public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic, boolean enabled) {
		if (mBtAdapter == null || mBluetoothGatt == null) {
			Log.w(TAG, "BluetoothAdapter not initialized");
			return;
		}
		mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);
		BluetoothGattDescriptor clientConfig = characteristic
				.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
		if (enabled) {
			clientConfig.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
		} else {
			clientConfig.setValue(BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE);
		}
		mBluetoothGatt.writeDescriptor(clientConfig);
		// 设置characteristic的描述值。
		// 所有的服务、特征值、描述值都用UUID来标识，先根据characteristic的UUID找到characteristic，再根据BluetoothGattDescriptor的
		// UUID找到BluetoothGattDescriptor，然后设定其值。
		// 关于descriptor，可以通过getDescriptor()方法的返回值来理解,
		// Returns a descriptor with a given UUID out of the list of descriptors
		// for this characteristic.5
	}
	public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, byte[] test) {
		boolean flag = false;
		characteristic.setValue(test);
		flag = mBluetoothGatt.writeCharacteristic(characteristic);
		System.out.println("=======>done");
		return flag;
	}
	public BluetoothGattService getSupportedGattServices(UUID uuid) {
		BluetoothGattService mBluetoothGattService;
		if (mBluetoothGatt == null)
			return null;
		mBluetoothGattService = mBluetoothGatt.getService(uuid);
		return mBluetoothGattService;
	}
	/**
	 * Retrieves a list of supported GATT services on the connected device. This
	 * should be invoked only after {@code BluetoothGatt#discoverServices()}
	 * completes successfully.
	 *
	 * @return A {@code List} of supported services.
	 */
	public List<BluetoothGattService> getSupportedGattServices() {
		if (mBluetoothGatt == null)
			return null;
		return mBluetoothGatt.getServices();
	}
	// Utility functions
	//
	public static BluetoothGatt getBtGatt() {
		return mThis.mBluetoothGatt;
	}

	public static BluetoothManager getBtManager() {
		return mThis.mBluetoothManager;
	}

	public static BluetoothLeService getInstance() {
		return mThis;
	}
}
