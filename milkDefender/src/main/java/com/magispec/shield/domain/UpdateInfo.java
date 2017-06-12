package com.magispec.shield.domain;

public class UpdateInfo {
	private String versionCode;
	private String versionName;
	private String downloadPath;
	private String desc;


	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}


	public UpdateInfo(String versionCode, String versionName, String downloadPath, String desc) {
		super();
		this.versionCode = versionCode;
		this.versionName = versionName;
		this.downloadPath = downloadPath;
		this.desc = desc;
	}

	public UpdateInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

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

}
