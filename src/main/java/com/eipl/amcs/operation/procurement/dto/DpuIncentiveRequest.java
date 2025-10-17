package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;

import java.time.LocalDate;
import java.time.LocalTime;

public class DpuIncentiveRequest extends BaseModelTxn {

    private Long incentiveMasterCode;
    private LocalTime mctime;
    private LocalTime ectime;
    private LocalTime mstime;
    private LocalTime estime;
    private LocalTime mltime;
    private LocalTime eltime;
    private Integer incRate;
    private Integer incDeduction;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String xCol4;
    private String xCol5;
    private String unionCode;
    private Shift shift;
    private Society society;


    public Long getIncentiveMasterCode() {
        return incentiveMasterCode;
    }

    public void setIncentiveMasterCode(Long incentiveMasterCode) {
        this.incentiveMasterCode = incentiveMasterCode;
    }

    public LocalTime getMctime() {
        return mctime;
    }

    public void setMctime(LocalTime mctime) {
        this.mctime = mctime;
    }

    public LocalTime getEctime() {
        return ectime;
    }

    public void setEctime(LocalTime ectime) {
        this.ectime = ectime;
    }

    public LocalTime getMstime() {
        return mstime;
    }

    public void setMstime(LocalTime mstime) {
        this.mstime = mstime;
    }

    public LocalTime getEstime() {
        return estime;
    }

    public void setEstime(LocalTime estime) {
        this.estime = estime;
    }

    public LocalTime getMltime() {
        return mltime;
    }

    public void setMltime(LocalTime mltime) {
        this.mltime = mltime;
    }

    public LocalTime getEltime() {
        return eltime;
    }

    public void setEltime(LocalTime eltime) {
        this.eltime = eltime;
    }

    public Integer getIncRate() {
        return incRate;
    }

    public void setIncRate(Integer incRate) {
        this.incRate = incRate;
    }

    public Integer getIncDeduction() {
        return incDeduction;
    }

    public void setIncDeduction(Integer incDeduction) {
        this.incDeduction = incDeduction;
    }

    public String getOriginatingOrgCode() {
        return originatingOrgCode;
    }

    public void setOriginatingOrgCode(String originatingOrgCode) {
        this.originatingOrgCode = originatingOrgCode;
    }

    public String getOriginatingOrgType() {
        return originatingOrgType;
    }

    public void setOriginatingOrgType(String originatingOrgType) {
        this.originatingOrgType = originatingOrgType;
    }

    public Integer getOriginatingType() {
        return originatingType;
    }

    public void setOriginatingType(Integer originatingType) {
        this.originatingType = originatingType;
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

    public String getxCol4() {
        return xCol4;
    }

    public void setxCol4(String xCol4) {
        this.xCol4 = xCol4;
    }

    public String getxCol5() {
        return xCol5;
    }

    public void setxCol5(String xCol5) {
        this.xCol5 = xCol5;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Shift getShift() {
        return shift;
    }

    public void setShift(Shift shift) {
        this.shift = shift;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }
}
