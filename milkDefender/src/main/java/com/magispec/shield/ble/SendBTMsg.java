package com.magispec.shield.ble;

public class SendBTMsg {
	public byte[] CombinedMessage(byte[] msg,byte Type,byte Cmd){
		int length=msg.length;	
		Short payload=(short) length;
		byte[]Msg=new byte[6+length];
		Msg[0] =  (byte) 0xF4;
		Msg[1] =  0x52;
		Msg[2] =  Type;
		Msg[3] =  Cmd;
		Msg[4]=(byte) ((payload) & 0x00FF);
		Msg[5]=(byte) ((payload >>> 8) & 0x00FF);
		for (int i = 0; i < length; i++) {
			Msg[6 + i] = msg[i];
		}
		return Msg;		
	}

}
