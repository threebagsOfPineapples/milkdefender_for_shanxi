package com.magispec.shield.domain;

public class WX {
	private String openid;

	private int expires_in;

	private String scope;

	private String refresh_token;

	private String access_token;

	private String unionid;

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getOpenid() {
		return this.openid;
	}

	public void setExpires_in(int expires_in) {
		this.expires_in = expires_in;
	}

	public int getExpires_in() {
		return this.expires_in;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getScope() {
		return this.scope;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public String getRefresh_token() {
		return this.refresh_token;
	}

	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public String getAccess_token() {
		return this.access_token;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getUnionid() {
		return this.unionid;
	}

}