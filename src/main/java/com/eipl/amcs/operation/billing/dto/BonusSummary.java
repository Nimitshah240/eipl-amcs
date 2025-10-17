package com.eipl.amcs.operation.billing.dto;

import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BonusSummary extends BaseModel {
    private String code;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal totalMilkQty;
    private BigDecimal totalMilkAmount;
    private BigDecimal bonusCriteriaAmount;
    private LocalDate disbursedDate;
    private short type; //0-Union,1-Society
    private short status; //0-PENDING ,1-DISBURSED
    private short bonusCriteria;
    private Society society;
    private Union union;
    private BigDecimal bonusCriteriaValue;

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

    public BigDecimal getBonusCriteriaValue() {
        return bonusCriteriaValue;
    }

    public void setBonusCriteriaValue(BigDecimal bonusCriteriaValue) {
        this.bonusCriteriaValue = bonusCriteriaValue;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(LocalDate toDate) {
        this.toDate = toDate;
    }

    public BigDecimal getTotalMilkQty() {
        return totalMilkQty;
    }

    public void setTotalMilkQty(BigDecimal totalMilkQty) {
        this.totalMilkQty = totalMilkQty;
    }

    public BigDecimal getTotalMilkAmount() {
        return totalMilkAmount;
    }

    public void setTotalMilkAmount(BigDecimal totalMilkAmount) {
        this.totalMilkAmount = totalMilkAmount;
    }

    public BigDecimal getBonusCriteriaAmount() {
        return bonusCriteriaAmount;
    }

    public void setBonusCriteriaAmount(BigDecimal bonusCriteriaAmount) {
        this.bonusCriteriaAmount = bonusCriteriaAmount;
    }

    public LocalDate getDisbursedDate() {
        return disbursedDate;
    }

    public void setDisbursedDate(LocalDate disbursedDate) {
        this.disbursedDate = disbursedDate;
    }

    public short getStatus() {
        return status;
    }

    public void setStatus(short status) {
        this.status = status;
    }

    public short getBonusCriteria() {
        return bonusCriteria;
    }

    public void setBonusCriteria(short bonusCriteria) {
        this.bonusCriteria = bonusCriteria;
    }
}