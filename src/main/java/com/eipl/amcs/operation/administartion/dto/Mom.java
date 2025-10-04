package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

public class Mom extends BaseModelTxn {
	private String code;
	private String mom;
	private Integer status;
	private short meetingType;
	private MeetingAgenda meetingAgenda;
	private Society society;
	private Union union;

	public Society getSociety() {
		return society;
	}

	public void setSociety(Society society) {
		this.society = society;
	}

	public Union getUnion() {
		return union;
	}

	public void setUnion(Union union) {
		this.union = union;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMom() {
		return mom;
	}

	public void setMom(String mom) {
		this.mom = mom;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public short getMeetingType() {
		return meetingType;
	}

	public void setMeetingType(short meetingType) {
		this.meetingType = meetingType;
	}

	public MeetingAgenda getMeetingAgenda() {
		return meetingAgenda;
	}

	public void setMeetingAgenda(MeetingAgenda meetingAgenda) {
		this.meetingAgenda = meetingAgenda;
	}

}
