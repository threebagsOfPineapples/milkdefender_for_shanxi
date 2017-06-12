package com.magispec.shield.ble;

public class BTMsg {
	public static final int MSG_STATE_IDLE = 0;
	public static final int MSG_STATE_RECV_HDR = 1;
	public static final int MSG_STATE_RECV_PAYLOAD = 2;
	public static final int MSG_STATE_PROCESSING = 3;	
	public static final byte MSG_MAGISPEC_ID_LSB = (byte) 0xF4;
	public static final byte MSG_MAGISPEC_ID_MSB = 0x52;
	public static final byte MSG_MAGISPEC_TYPE=0x00; 
	public static final byte MSG_CMD_COLLECT=0x09; 
	public static final byte MSG_CMD_SPECTRUM_NOTIFY = (byte) 0xa0;
	public static final byte MSG_CMD_FONT=(byte)0x51;
	public static final byte MSG_CMD_FONT_NAME=(byte)0x52;
	public static final byte MSG_CMD_CONN_AUTHORIZE=(byte)0x53;
	public static final byte MSG_CMD_DARKEVN_CALIB = (byte) 0x54;
	public static final byte MSG_CMD_REF1_CALIB = (byte) 0x55;
	public static final byte MSG_CMD_SET_DARK_SPECTRUM_DATA = (byte) 0x56;	
	public static final byte MSG_CMD_SET_REF_SPECTRUM_DATA = (byte) 0x57;		
	public static final byte MSG_CMD_PAIR=(byte)0x58;
	//获取固件版本
	public static final byte MSG_CMD_GET_FW_VER=(byte)0x59;
	public static final byte MSG_CMD_UPGRADE=(byte)0x60;
	//获取上次校准的时间
	public static final byte MSG_GET_CALIBRATE_TIME = (byte) 0x62;
	//设置自动校准的时间
	public static final byte MSG_SET_AUTO_CALIBRATE_TIME = (byte) 0x63;
	//获取波长步进
	public static final byte MSG_GET_WAVE_STEP=(byte) 0x64;
	//设置起始波长
	public static final byte MSG_SET_WAVE_START=(byte) 0x65;
	
	//8获取波长映射数据 (0x66)-MS_TM_GET_WAVE_MAP
	public static final byte MSG_GET_WAVE_MAP=(byte)0x66;
	public static final byte MSG_SET_WAVE_MAP=(byte)0x0C;
	//设置测试目标
	public static final byte MS_TM_SET_TEST_OBJ=(byte) 0x67;
	public static final byte MS_TM_ERROR_INFO=(byte)0x68;
	private byte mMagispeIDLsb;
	private byte mMagispeIDMsb;
	private byte mCmd;
	private byte mType;
	private int mPayloadLen;
	private byte[] mPayload;
	private int mState = MSG_STATE_IDLE;
	private int mPos = 0;

	public byte getmCmd() {
		return mCmd;
	}

	public void setmCmd(byte mCmd) {
		this.mCmd = mCmd;
	}

	public int getmPayloadLen() {
		return mPayloadLen;
	}
	public void setmPayloadLen(short mPayloadLen) {
		this.mPayloadLen = mPayloadLen;
	}

	public byte[] getmPayload() {
		return mPayload;
	}

	public void setmPayload(byte[] mPayload) {
		this.mPayload = mPayload;
	}
	public void setType(byte mType) {
		this.mType = mType;
	}
	
	public int getType(){
		
		return mType;
	}

	public BTMsg(byte Cmd, byte Type, byte[]payload)
	{
		 mMagispeIDLsb =  MSG_MAGISPEC_ID_LSB;
		 mMagispeIDMsb =  MSG_MAGISPEC_ID_MSB;
		 mCmd = Cmd;
		 mType = Type;
		 this.setPayload(payload);
	}
	public BTMsg() {
		super();
	}	
	public int assembleMsgByByte(byte[] data) {
		if (mState == MSG_STATE_IDLE) {
			System.out.println("我执行了MSG_STATE_IDLE"+"--length:"+data.length);	
			if (data[0] == MSG_MAGISPEC_ID_MSB && data[1] == MSG_MAGISPEC_ID_LSB) {
				mMagispeIDLsb = data[0];
				mMagispeIDMsb = data[1];
				mType = data[2];
				mCmd = data[3];
				mPayloadLen = data[4];
				mPayloadLen |= ((short) data[5] << 8) & 0xFF00;
				mPayload = new byte[mPayloadLen];	
				System.out.println(mPayloadLen+ "----mPayloadLen---");
				for (int i = 6; i < data.length; i++) {
					mPayload[mPos] = data[i];
					mPos++;
					System.out.println(mPos);
				}
				if(mPos< mPayload.length){
					mState=MSG_STATE_RECV_PAYLOAD;
					System.out.println("我执行了MSG——MSG_STATE_RECV_PAYLOAD"+mState+"mpos"+mPos);
				}else{
					mState = MSG_STATE_PROCESSING;
					System.out.println("我执行了MSG——MSG_STATE_PROCESSING"+",pos"+mPos);
				}
				System.out.println("我执行了MSG_STATE_IDLE" +"mPos" +mPos);
			}
		} else if (mState == MSG_STATE_RECV_PAYLOAD) {
			System.out.println("我开始执行了这个---payload"+"mos--"+mPos);
			for (int i = 0; i < data.length; i++) {
				if (mPos <mPayloadLen) {

					mPayload[mPos] = data[i];
					mPos++;
				
					if(mPos==mPayloadLen){
					
						System.out.println("我收集完了啊");
						mState = MSG_STATE_PROCESSING;
					}
				} else {
					System.out.println("失败");
				
				}
				// System.out.println(mPos+"--pos");
			}
			
		}

		else if (mState == MSG_STATE_PROCESSING) {
			System.out.println("This msg is processing ...");
		} else {
			System.out.println("Should not run here ...");
		}
		return mState;
	}

	public void setPayload(byte[] payload) {
		if (payload == null)
		{
			mPayloadLen = 0;
		}
		else
		{
			mPayloadLen = (short) payload.length;
			mPayload = new byte[payload.length];
			for (int i = 0; i < payload.length; i++)
			{
				mPayload[i] = payload[i];
			}
		}
	}
	public byte[] getRawData() {
		
		byte[] rawData = new byte[6 + mPayloadLen];
		rawData[0] = mMagispeIDMsb;
		rawData[1] = mMagispeIDLsb;
		rawData[2] = MSG_MAGISPEC_TYPE;
		rawData[3] = mCmd;
		rawData[4] = (byte) ((mPayloadLen) & 0x00FF);
		rawData[5] = (byte) ((mPayloadLen >>> 8) & 0x00FF);
		for (int i = 0; i < mPayloadLen; i++) {
			rawData[6 + i] = mPayload[i];
		}
		System.out.println("消息组装完毕"+rawData.length);
		
		return rawData;
	}

	public void clear() {
		mState = MSG_STATE_IDLE;
		mPos = 0;
	}

}