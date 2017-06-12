package com.magispec.shield.domain;

public class DATA {
	private String password;

	private String username;

	public DATA() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DATA(String password, String username) {
		super();
		this.password = password;
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUsername() {
		return this.username;
	}
}
