package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class RecordingParameter extends BaseModel {
	private Long code;
	private Boolean isActive;
	private LocalDateTime recordingDateTime;
	private BigDecimal temperature;
	private BigDecimal weight;
	private String chillerNo;
	private String societyCode;
	private String unionCode;

	public Long getCode() {
		return code;
	}

	public void setCode(Long code) {
		this.code = code;
	}

	public String getSocietyCode() {
		return societyCode;
	}

	public void setSocietyCode(String societyCode) {
		this.societyCode = societyCode;
	}

	public Boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}

	public LocalDateTime getRecordingDateTime() {
		return recordingDateTime;
	}

	public void setRecordingDateTime(LocalDateTime recordingDateTime) {
		this.recordingDateTime = recordingDateTime;
	}

	public BigDecimal getTemperature() {
		return temperature;
	}

	public void setTemperature(BigDecimal temperature) {
		this.temperature = temperature;
	}

	public BigDecimal getWeight() {
		return weight;
	}

	public void setWeight(BigDecimal weight) {
		this.weight = weight;
	}

	public String getChillerNo() {
		return chillerNo;
	}

	public void setChillerNo(String chillerNo) {
		this.chillerNo = chillerNo;
	}


	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}
}


