package com.magispec.shield.domain;

public class GoodInfo {
	private String id;
	private String name;
	private String type;
	private String desc;
	private String imageUrl;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public GoodInfo(String id, String name, String type, String desc, String imageUrl) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.desc = desc;
		this.imageUrl = imageUrl;
	}
	public GoodInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
