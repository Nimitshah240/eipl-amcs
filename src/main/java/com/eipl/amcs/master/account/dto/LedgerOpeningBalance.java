package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import com.eipl.amcs.master.account.model.Ledger;

public class LedgerOpeningBalance extends BaseModelTxn {

	private String code;
	private BigDecimal balance;
	private Boolean creditDebit;
	private Boolean autoManual;
	private Society society;
	private String financialYearsCode;
	private Ledger ledger;
	private String unionCode;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Boolean getCreditDebit() {
		return creditDebit;
	}

	public void setCreditDebit(Boolean creditDebit) {
		this.creditDebit = creditDebit;
	}

	public Boolean getAutoManual() {
		return autoManual;
	}

	public void setAutoManual(Boolean autoManual) {
		this.autoManual = autoManual;
	}

	public Society getSociety() {
		return society;
	}

	public void setSociety(Society society) {
		this.society = society;
	}

	public String getFinancialYearsCode() {
		return financialYearsCode;
	}

	public void setFinancialYearsCode(String financialYearsCode) {
		this.financialYearsCode = financialYearsCode;
	}

	public Ledger getLedger() {
		return ledger;
	}

	public void setLedger(Ledger ledger) {
		this.ledger = ledger;
	}

	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}
}
