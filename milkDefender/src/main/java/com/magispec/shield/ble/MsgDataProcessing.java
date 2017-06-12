package com.magispec.shield.ble;

import com.magispec.shield.activity.MainActivity;
import com.magispec.shield.constantDefinitions.Constant;
import com.magispec.shield.utils.DigitalTrans;

public class MsgDataProcessing {
	BTMsg mMsg;
	int choose;
	int[] data;
	String fwVersion;
	int[][] s = new int[61][5];
	int[] Sample;
	int []sample1,sample2,sample3,sample4,sample5;
	public void putMsg(BTMsg mMsg) {
		this.mMsg = mMsg;
	}	
	public int[] dataProcessing() {		
		System.out.println("+++" + mMsg.getmCmd() + "+++");
		switch (mMsg.getmCmd()) {
		case BTMsg.MSG_CMD_SPECTRUM_NOTIFY: {
			int sampleCount = mMsg.getmPayloadLen() / 2;
			byte[] payload = mMsg.getmPayload();
			int[] samples = new int[sampleCount];
			for (int i = 0; i < sampleCount; i++) {
				samples[i] = payload[i * 2 ] & 0xff;
				samples[i] |= (int) (payload[i * 2 +1 ] & 0xff) << 8;
				System.out.println("sample+++++++" + samples[i] + "---" + i);
			}			
			MainActivity.Sample=samples;
			System.out.println("---得到Sample");
			BluetoothLeService.OK=true;
			System.out.println(BluetoothLeService.OK);
			}
			break;
		case BTMsg.MSG_CMD_FONT:
			System.out.println("MSG_CMD_FONT-----");
			break;	
		case BTMsg.MSG_CMD_DARKEVN_CALIB: {
			int darkCount = (mMsg.getmPayloadLen() - 4) / 2;
			byte[] payload = mMsg.getmPayload();
			int[] darks = new int[darkCount];
			MainActivity.getcalibrateDark=payload;
			for (int i = 0; i < darkCount; i++) {
				
				darks[i] = payload[i * 2 + 2] & 0xff;
				darks[i] |= (int) (payload[i * 2 + 3] & 0xff) << 8;
				System.out.println("drak+++++++" + darks[i] + "---" + i);
			}
			MainActivity.Dark = darks;
		}
			break;
		case BTMsg.MSG_CMD_REF1_CALIB: {
			int ref1Count = (mMsg.getmPayloadLen() - 4) / 2;
			byte[] payload = mMsg.getmPayload();
			MainActivity.getcalibrateRef=payload;
			int[] ref1 = new int[ref1Count];
			for (int i = 0; i < ref1Count; i++) {
				ref1[i] = payload[i * 2 + 2] & 0xff;
				ref1[i] |= (int) (payload[i * 2 + 3] & 0xff) << 8;
				System.out.println("ref1+++++++" + ref1[i] + "---" + i);
			}
			MainActivity.HalfRef = ref1;
		}
			break;
		case BTMsg.MSG_CMD_GET_FW_VER:
			String temp="";
			System.out.println("MSG_CMD_GET_FW_VER"+"ssssss");			
			byte[] payload = mMsg.getmPayload();			
			for (int i = payload.length-1; i > 0; i--) {
				temp+=payload[i]+".";
			}
			temp+=payload[0];
			Constant.FW_VERSION=temp;
			break;			
		case BTMsg.MSG_GET_CALIBRATE_TIME:
			System.out.println("MSG_CMD_GET_CALIBRATE_TIME-----");
			int Count = (mMsg.getmPayloadLen() ) / 4;
			byte[] payload1 = mMsg.getmPayload();
			System.out.println("payload第一个值："+payload1[0]+"--"+DigitalTrans.algorismToHEXString(payload1[0]));
			if(payload1[0]==0){	
				System.out.println("开始读取time-----");
				int[] time = new int[Count];
				for (int i = 0; i < Count; i++) {
					time[i] = payload1[i * 2 + 1] & 0xff;
					time[i] |= (int) (payload1[i * 2 + 2] & 0xff) << 8;
					time[i]|=(int)(payload1[i * 2 + 3] & 0xff) << 16;
					time[i]|=(int)(payload1[i * 2 + 4] & 0xff) << 24;
					System.out.println("time+++++++" + time[i] + "---" + i);				
				}
			MainActivity.Time=time;	
			System.out.println("开始读取time-----"+time[0]);
			}else{
				System.out.println("读取time错误");
			int	time[]={0x01};
				MainActivity.Time=time;		
			}
			break;	
		case BTMsg.MSG_GET_WAVE_MAP:
			System.out.println("得到Map type："+mMsg.getType()+"长度："+mMsg.getmPayloadLen());
			int sampleCount = mMsg.getmPayloadLen() / 2;
			byte[] mapPayload = mMsg.getmPayload();
			short[] map = new short[sampleCount];
			for (int i = 0; i < sampleCount; i++) {
				map[i] = (short) (mapPayload[i * 2 ] & 0xff);
				map[i] |=  (mapPayload[i * 2 +1 ] & 0xff) << 8;
				System.out.println("map+++++++" + map[i] + "---" + i);
			}			
			MainActivity.Map=map;
			System.out.println("---得到map");
			
			break;
		default:
			data = null;
			break;
		}
		return data;
	}

}
