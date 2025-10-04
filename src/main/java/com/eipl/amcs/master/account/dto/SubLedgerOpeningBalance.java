package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.master.account.model.SubLedger;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import com.eipl.amcs.master.account.model.Ledger;

public class SubLedgerOpeningBalance extends BaseModelTxn {

	private String code;
	private BigDecimal balance;
	private Boolean creditDebit;
	private Boolean autoManual;
	private Society society;
	private String financialYearsCode;
	private Ledger ledger;
	private SubLedger subLedger;
	private String unionCode;

	public SubLedgerOpeningBalance() {
	}

	public SubLedgerOpeningBalance(String code, BigDecimal balance, Boolean creditDebit, Boolean autoManual, Society society, String financialYearsCode, Ledger ledger, SubLedger subLedger, String unionCode) {
		this.code = code;
		this.balance = balance;
		this.creditDebit = creditDebit;
		this.autoManual = autoManual;
		this.society = society;
		this.financialYearsCode = financialYearsCode;
		this.ledger = ledger;
		this.subLedger = subLedger;
		this.unionCode = unionCode;
	}

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

	public SubLedger getSubLedger() {
		return subLedger;
	}

	public void setSubLedger(SubLedger subLedger) {
		this.subLedger = subLedger;
	}

	public String getUnionCode() {
		return unionCode;
	}

	public void setUnionCode(String unionCode) {
		this.unionCode = unionCode;
	}
}
