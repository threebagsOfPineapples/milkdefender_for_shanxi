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

import java.util.HashMap;
import java.util.UUID;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {
    private static HashMap<String, String> attributes = new HashMap<String, String>();
    //这样写只是赋了一个常量值
    public static String HEART_RATE_MEASUREMENT = "00002a37-0000-1000-8000-00805f9b34fb";    
    //the descriptor of battery characteristic(battery service)
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";    
    public static String BLE_Service = "0000fff0-0000-1000-8000-00805f9b34fb";
    public static String BLE_READ_WRITE = "0000fff4-0000-1000-8000-00805f9b34fb";
    public static String BLE_NOTIFY = "0000fff6-0000-1000-8000-00805f9b34fb"; 
    public static final String OAD_SERVICE="f000ffc0-0000-1000-8000-00805f9b34fb";
    public static final String OAD_WRITE_READ="0000ffc2-0000-1000-8000-00805f9b34fb";
    public static final UUID OAD_SERVICE_UUID = UUID.fromString("f000ffc0-0451-4000-b000-000000000000"); 
    public static final UUID OAD_CHARACTER = UUID.fromString(OAD_WRITE_READ);
    public static final UUID BLE_SERVICE_UUID=UUID.fromString(BLE_Service);
    public static final UUID BLE_READ_WRITE_UUID=UUID.fromString(BLE_READ_WRITE);
    public static final UUID BLE_NOTIFY_UUID=UUID.fromString(BLE_NOTIFY);
	static {       
    	// Sample Services.给自己用到的服务命名
    	attributes.put("0000fff0-0000-1000-8000-00805f9b34fb", "奶粉卫士");
        attributes.put("0000180a-0000-1000-8000-00805f9b34fb", "设备信息");
        attributes.put(OAD_SERVICE, "OAD");
        //Sample Characteristics.给自己用到的特征值命名
        attributes.put(BLE_READ_WRITE, "READ_WRITE");		
        attributes.put("00002a37-0000-1000-8000-00805f9b34fb", "YJ Name");
        attributes.put("00002a29-0000-1000-8000-00805f9b34fb", "Manufacturer Name String");
    } 
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
