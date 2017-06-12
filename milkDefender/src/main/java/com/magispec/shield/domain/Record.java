package com.magispec.shield.domain;
public class Record {
	public String count;
	public String rec_result;
	public String rec_testMilkName;
	public String rec_resultMilkName;
	public String rec_time;
	public String rec_position;

	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getRec_result() {
		return rec_result;
	}

	public void setRec_result(String rec_result) {
		this.rec_result = rec_result;
	}

	public String getRec_testMilkName() {
		return rec_testMilkName;
	}

	public void setRec_testMilkName(String rec_testMilkName) {
		this.rec_testMilkName = rec_testMilkName;
	}

	public String getRec_resultMilkName() {
		return rec_resultMilkName;
	}

	public void setRec_resultMilkName(String rec_resultMilkName) {
		this.rec_resultMilkName = rec_resultMilkName;
	}

	public String getRec_time() {
		return rec_time;
	}

	public void setRec_time(String rec_time) {
		this.rec_time = rec_time;
	}

	public String getRec_position() {
		return rec_position;
	}

	public void setRec_position(String rec_position) {
		this.rec_position = rec_position;
	}

	public Record(String count, String rec_result, String rec_testMilkName, String rec_resultMilkName,
			String rec_time, String rec_position) {
		super();
		this.count = count;
		this.rec_result = rec_result;
		this.rec_testMilkName = rec_testMilkName;
		this.rec_resultMilkName = rec_resultMilkName;
		this.rec_time = rec_time;
		this.rec_position = rec_position;
	}

	public Record() {
		super();
		// TODO Auto-generated constructor stub
	}

}