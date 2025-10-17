package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.operation.billing.model.MemberBill;

import java.math.BigDecimal;

public class MemberBillTransaction {
    private String code;
    private BigDecimal amount;
    private BigDecimal adjustment;
    private BigDecimal prevDue;
    private BigDecimal due;
    private short type; //1-Addition, 2-Deduction
    private String refNo;
    private String formula;
    private String fraction;
    private String unionCode;
    private String societyCode;
    private MemberBill memberBill;
    private BillHead billHead;

    public BigDecimal getDue() {
        return due;
    }

    public void setDue(BigDecimal due) {
        this.due = due;
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

    public BigDecimal getAdjustment() {
        return adjustment;
    }

    public void setAdjustment(BigDecimal adjustment) {
        this.adjustment = adjustment;
    }

    public BigDecimal getPrevDue() {
        return prevDue;
    }

    public void setPrevDue(BigDecimal prevDue) {
        this.prevDue = prevDue;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public String getFraction() {
        return fraction;
    }

    public void setFraction(String fraction) {
        this.fraction = fraction;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getSocietyCode() {
        return societyCode;
    }

    public void setSocietyCode(String societyCode) {
        this.societyCode = societyCode;
    }

    public MemberBill getMemberBill() {
        return memberBill;
    }

    public void setMemberBill(MemberBill memberBill) {
        this.memberBill = memberBill;
    }

    public BillHead getBillHead() {
        return billHead;
    }

    public void setBillHead(BillHead billHead) {
        this.billHead = billHead;
    }
}