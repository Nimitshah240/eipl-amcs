package com.eipl.amcs.operation.administartion.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.operation.model.Member;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.master.procurement.model.SocietyPaymentCycle;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CashAdvance extends BaseModel {
	private String code;
	private BigDecimal amount;
	private Integer noOfInstallment;
	private LocalDate date;
	private LocalDate installmentDate;
	private String unionCode;
	private Society society;
	private SocietyPaymentCycle societyPaymentCycle;
	private Member member;

	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Integer getNoOfInstallment() {
		return noOfInstallment;
	}

	public void setNoOfInstallment(Integer noOfInstallment) {
		this.noOfInstallment = noOfInstallment;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public LocalDate getInstallmentDate() {
		return installmentDate;
	}

	public void setInstallmentDate(LocalDate installmentDate) {
		this.installmentDate = installmentDate;
	}


	public Society getSociety() {
		return society;
	}

	public void setSociety(Society society) {
		this.society = society;
	}

	public SocietyPaymentCycle getSocietyPaymentCycle() {
		return societyPaymentCycle;
	}

	public void setSocietyPaymentCycle(SocietyPaymentCycle societyPaymentCycle) {
		this.societyPaymentCycle = societyPaymentCycle;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}
}

