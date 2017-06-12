package com.magispec.shield.ble;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import com.magispec.shield.*;;

public class LeDeviceListAdapter extends BaseAdapter {
	private final LayoutInflater inflater;

	private final ArrayList<BluetoothDevice> leDevices;
	private final HashMap<BluetoothDevice, Integer> rssiMap = new HashMap<BluetoothDevice, Integer>();

	public LeDeviceListAdapter(Context context) {
		leDevices = new ArrayList<BluetoothDevice>();
		inflater = LayoutInflater.from(context);
	}

	public void addDevice(BluetoothDevice device, int rssi) {
		if (!leDevices.contains(device)) {
			leDevices.add(device);
		}
		rssiMap.put(device, rssi);
	}

	public BluetoothDevice getDevice(int position) {
		return leDevices.get(position);
	}

	public void clear() {
		leDevices.clear();
	}
	@Override
	public int getCount() {
		return leDevices.size();
	}
	@Override
	public Object getItem(int i) {
		return leDevices.get(i);
	}
	@Override
	public long getItemId(int i) {
		return i;
	}
	@Override
	public View getView(int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		// General ListView optimization code.
		if (view == null) {
			view = inflater.inflate(R.layout.listitem_device, null);
			viewHolder = new ViewHolder();
			viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
			viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
			viewHolder.deviceRssi = (TextView) view.findViewById(R.id.device_rssi);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		BluetoothDevice device = leDevices.get(i);
		final String deviceName = device.getName();
		if (deviceName != null && deviceName.length() > 0) {
			viewHolder.deviceName.setText(deviceName);
			System.out.println(device.getName() + device.getAddress() + rssiMap.get(device));
		}
		else {
			viewHolder.deviceName.setText(R.string.unknown_device);
		}
		viewHolder.deviceAddress.setText(device.getAddress());
		viewHolder.deviceRssi.setText("" + rssiMap.get(device) + " dBm");
		return view;
	}
	private static class ViewHolder {
		TextView deviceName;
		TextView deviceAddress;
		TextView deviceRssi;
	}
}
