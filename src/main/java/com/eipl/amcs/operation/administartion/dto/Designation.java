package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModel;

public class Designation extends BaseModel {

	private Integer code;
	private String name;
	private Integer type;

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
