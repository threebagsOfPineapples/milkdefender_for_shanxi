package com.magispec.shield.domain;

import java.io.Serializable;

public class MilkInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String fullName;
	public String shortName;
	public String type;
	public String desc;
	public String photoPath ;
	public String band;
	public String origin;
	public String barcode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getShortName() {
		return shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
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
		return photoPath;
	}

	public void setImageUrl(String imageUrl) {
		this.photoPath = imageUrl;
	}

	public String getBand() {
		return band;
	}

	public void setBand(String band) {
		this.band = band;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public MilkInfo(String id, String fullName, String shortName, String type, String desc, String imageUrl,
			String band, String origin, String barcode) {
		super();
		this.id = id;
		this.fullName = fullName;
		this.shortName = shortName;
		this.type = type;
		this.desc = desc;
		this.photoPath = imageUrl;
		this.band = band;
		this.origin = origin;
		this.barcode = barcode;
	}
	public MilkInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MilkInfo [id=" + id + ", fullName=" + fullName + ", shortName=" + shortName + ", type=" + type
				+ ", desc=" + desc + ", imageUrl=" + photoPath + ", band=" + band + ", origin=" + origin + ", barcode="
				+ barcode + "]";
	}
	
	
	
}
