package com.magispec.shield.domain;

public class FWVersionInfo {
	public String versionCode;
	public String versionName;
	public String downloadPath;

	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	public FWVersionInfo(String versionCode, String versionName, String downloadPath) {
		super();
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.downloadPath = downloadPath;
	}

	public FWVersionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

}
