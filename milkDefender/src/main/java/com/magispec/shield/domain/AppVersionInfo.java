package com.magispec.shield.domain;
public class AppVersionInfo {
	public String versionCode;
	public String versionNumber;
	public String downloadPath;
	public AppVersionInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public AppVersionInfo(String versionCode, String versionNumber, String downloadPath) {
		super();
		this.versionCode = versionCode;
		this.versionNumber = versionNumber;
		this.downloadPath = downloadPath;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	}
	public String getDownloadPath() {
		return downloadPath;
	}
	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
}
