package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;

import java.time.LocalDate;

public class CommitteeMembers extends BaseModelTxn {

	private String code;
	private String memberName;
	private LocalDate tenureFromDate;
	private LocalDate electionDate;
	private LocalDate tenureToDate;
	private String unionCode;
	private Society society;
	private String memberCode;
	private Designation designation;
	public String getMemberCode() {
		return memberCode;
	}

	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}



	public Society getSociety() {
		return society;
	}

	public void setSociety(Society society) {
		this.society = society;
	}



	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public LocalDate getTenureFromDate() {
		return tenureFromDate;
	}

	public void setTenureFromDate(LocalDate tenureFromDate) {
		this.tenureFromDate = tenureFromDate;
	}

	public LocalDate getElectionDate() {
		return electionDate;
	}

	public void setElectionDate(LocalDate electionDate) {
		this.electionDate = electionDate;
	}

	public LocalDate getTenureToDate() {
		return tenureToDate;
	}

	public void setTenureToDate(LocalDate tenureToDate) {
		this.tenureToDate = tenureToDate;
	}

	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}


	public Designation getDesignation() {
		return designation;
	}

	public void setDesignation(Designation designation) {
		this.designation = designation;
	}
}
