package com.eipl.amcs.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Transient;

public interface JsonAndTableBuilder {
	@JsonIgnore
	default String getTableName() {
		return null;
	}

	@Transient
	default Object getId() {
		return null;
	}

	@JsonIgnore
	default JsonAndTableBuilder getAuditModel(String operation, String user) {
		return null;
	}

	default String getUserInfo() {
		return null;
	}
}
