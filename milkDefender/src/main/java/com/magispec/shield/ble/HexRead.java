package com.magispec.shield.ble;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.magispec.shield.utils.Utils;

public class HexRead {
	public static ArrayList<String> readFileByLines() {
		StringBuffer sf = new StringBuffer();
		File file = new File("//data/data//com.magispec.milkdefender//files//SBP_OAD_ImgB.hex");
		String remain = "";
		BufferedReader reader = null;
		ArrayList<String> value = new ArrayList<>();
		try {			
			reader = new BufferedReader(new FileReader(file));
			while (sf.length() < 32) {
				String temp = reader.readLine();				
				if (temp != null & !temp.substring(7, 9).equals("05")) {
					int datalength = Integer.parseInt(temp.substring(1, 3), 16);				
					if (datalength != 0 && sf.length() < 32 && temp.substring(7, 9).equals("00")) {
						
						if ((32 - sf.length()) > datalength * 2) {							
							sf.append(temp.substring(9, 9 + datalength * 2));
							remain = "";						
						} else {						
							int remains = 32 - sf.length();
							sf.append(temp.substring(9, remains + 9));
							value.add(sf.toString());
							
							sf.setLength(0);
							remain = temp.substring(remains + 9, 9 + datalength * 2);
							if (remain.length() != 0) {
								sf.append(remain);
							}
						}
					} else {					
						if (sf.length() == 32) {
						
							sf.setLength(0);
							}
					}
				} else {
					// 读取完毕后中段循环
					break;
				}
			}
		
			byte[] test = Utils.hexStringToByte(value.get(0));
			for (int i = 0; i < test.length; i++) {
				
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return value;
	}	
	public static  byte[] getHexLength(){
		StringBuffer sf = new StringBuffer();
		File file = new File("//data/data//com.magispec.milkdefender//files//SBP_OAD_ImgB.hex");
		String remain = "";		
		byte[]a = null;
		BufferedReader reader = null;		
		try {
			reader = new BufferedReader(new FileReader(file));
			reader.readLine();
			remain=	reader.readLine();
		if(remain.length()>0){
			a=Utils.hexStringToByte(remain.substring(9,13));			
		}		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}
		return a;		
	}

}
