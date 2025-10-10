package com.eipl.amcs.master.operation.dto;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.org.model.Society;

import java.math.BigDecimal;

public class MemberCreditLimit extends BaseModelTxn {

    private String code;
    private BigDecimal balance;
    private String consumerCode;
    private short consumerType;
    private Society societyCode;
    private String unionCode;

    public MemberCreditLimit() {
        createdBy = MainApp.getUser() != null ? MainApp.getUser().getUsername() : null;
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

    public String getConsumerCode() {
        return consumerCode;
    }

    public void setConsumerCode(String consumerCode) {
        this.consumerCode = consumerCode;
    }

    public short getConsumerType() {
        return consumerType;
    }

    public void setConsumerType(short consumerType) {
        this.consumerType = consumerType;
    }

    public Society getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(Society societyCode) {
        this.societyCode = societyCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }
}
