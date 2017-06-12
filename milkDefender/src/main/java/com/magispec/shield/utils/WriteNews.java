package com.magispec.shield.utils;

import com.magispec.shield.ble.BTMsg;
import com.magispec.shield.ble.BluetoothLeService;

import android.bluetooth.BluetoothGattCharacteristic;

public class WriteNews {
	private static byte[] msgData;
	private int msgDataPos=0;
	public static BluetoothLeService mBluetoothLeService;// 自定义的一个继承自Service的服务
	public static byte[] AssemblyMessage(byte[]news,byte a,byte b ){
		byte[]payload=news;
		BTMsg msg = new BTMsg(a, b, payload);
		msgData = msg.getRawData();
		return  msgData;
	}
	public void SendNews(final BluetoothGattCharacteristic characteristic,byte[] news){
		new Thread(
				
				new Runnable() {
					public void run() {
						int rest = msgData.length - msgDataPos;
						while(msgDataPos < msgData.length){
							rest = msgData.length - msgDataPos;
							int sendByte = rest > 20 ? 20 : rest;
							byte[] send = new byte[sendByte];
							for (int i = 0; i < send.length; i++) {
								send[i] = msgData[msgDataPos + i];
							}
							mBluetoothLeService.writeCharacteristic(characteristic, send);
							msgDataPos += send.length;
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}	
					
					}
				}			
				).start();
		
		
		
		
		
		
		
		
		
	
		
		
		
		
	}
}
