package com.magispec.shield.ble;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.magispec.shield.utils.DigitalTrans;

public class BinRead {
	/**
	 * 读取由hex转变而来的bin文件	
	 * @param src
	 * 文件名 (文件所在的绝对地址)
	 */
	public static ArrayList<byte[]> ReadBIn(String src) {
		System.out.println("bin文件名称：" + src);
		FileInputStream in = null;
		ArrayList<byte[]> value = new ArrayList<>();
		try {
			in = new FileInputStream(src);
			int c;			
			int j = 0;
			byte buffer[] = new byte[16];
			while ((c = in.read(buffer)) != -1) {
				byte[]a=DigitalTrans.hex2byte(DigitalTrans.byte2hex(buffer));
				value.add(a);
			} 
			System.out.println("我打印了" + value.size());
		} catch (Exception e) {			
			e.printStackTrace();
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
		}
}
