package com.magispec.shield.domain;

public class Goods {
	private int id;
	private int name;
	private int result;
	private int backID;

	public Goods(int id, int name, int result, int backID) {
		super();
		this.id = id;
		this.name = name;
		this.result = result;
		this.backID = backID;
	}
	public int getBackID() {
		return backID;
	}
	public void setBackID(int backID) {
		this.backID = backID;
	}
	public int getId() {
		return id;
	}
	public int getResult() {
		return result;
	}
	public void setResult(int result) {
		this.result = result;
	}

	public void setId(int id) {
		this.id = id;
	}
	public int getName() {
		return name;
	}
	public void setName(int name) {
		this.name = name;
	}
	public Goods() {
		super();
		// TODO Auto-generated constructor stub
	}
}
