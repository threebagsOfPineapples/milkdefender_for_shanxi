package com.magispec.shield.domain;

public class Session {
	private DATA DATA;

	private int ACTION;

	private int TYPE;

	private String OPENID;

	public Session() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Session(DATA dATA, int aCTION, int tYPE, String oPENID) {
		super();
		DATA = dATA;
		ACTION = aCTION;
		TYPE = tYPE;
		OPENID = oPENID;
	}

	public void setDATA(DATA DATA) {
		this.DATA = DATA;
	}

	public DATA getDATA() {
		return this.DATA;
	}

	public void setACTION(int ACTION) {
		this.ACTION = ACTION;
	}

	public int getACTION() {
		return this.ACTION;
	}

	public void setTYPE(int TYPE) {
		this.TYPE = TYPE;
	}

	public int getTYPE() {
		return this.TYPE;
	}

	public void setOPENID(String OPENID) {
		this.OPENID = OPENID;
	}

	public String getOPENID() {
		return this.OPENID;
	}
}
