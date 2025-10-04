package com.eipl.amcs.master.account.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.List;
import com.eipl.amcs.master.account.model.VoucherTransaction;
import com.eipl.amcs.master.account.model.VoucherType;


public class Voucher extends BaseModelTxn {

    private String code;
    private Boolean autoPosted;
    private Boolean cancelled;
    private LocalDate billDate;
    private LocalDate voucherDate;
    private String billNo;
    private String remarks;
    private Society society;
    private VoucherType voucherType;
    private String unionCode;
    private String dockCode;
    private String financialYearsCode;


    @JsonIgnore
    private List<VoucherTransaction> voucherTransactions;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getAutoPosted() {
        return autoPosted;
    }

    public void setAutoPosted(Boolean autoPosted) {
        this.autoPosted = autoPosted;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public LocalDate getVoucherDate() {
        return voucherDate;
    }

    public void setVoucherDate(LocalDate voucherDate) {
        this.voucherDate = voucherDate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public VoucherType getVoucherType() {
        return voucherType;
    }

    public void setVoucherType(VoucherType voucherType) {
        this.voucherType = voucherType;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getDockCode() {
        return dockCode;
    }

    public void setDockCode(String dockCode) {
        this.dockCode = dockCode;
    }

    public String getFinancialYearsCode() {
        return financialYearsCode;
    }

    public void setFinancialYearsCode(String financialYearsCode) {
        this.financialYearsCode = financialYearsCode;
    }

    public List<VoucherTransaction> getVoucherTransactions() {
        return voucherTransactions;
    }

    public void setVoucherTransactions(List<VoucherTransaction> voucherTransactions) {
        this.voucherTransactions = voucherTransactions;
    }
}
