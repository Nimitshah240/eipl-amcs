package com.eipl.amcs.master.insurance.dto;

import com.eipl.amcs.base.model.BaseModel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InsuranceDetailSummary extends BaseModel {
    private Integer insuranceDetailSummaryCode;
    private Integer insuranceMasterCode;
    private String dcsCode;

    private String plantCode;
    private String unionCode;
    private String bmcCode;
    private String mccPlantCode;
    private LocalDate fromDate;
    private LocalDate toDate;
    private String status;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
//    private String xCol1;
//    private String xCol2;
//    private String xCol3;
    private String xCol4;
    private String xCol5;
    private String dcsName;

    public String getDcsName() {
        return dcsName;
    }

    public void setDcsName(String dcsName) {
        this.dcsName = dcsName;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getBmcCode() {
        return bmcCode;
    }

    public void setBmcCode(String bmcCode) {
        this.bmcCode = bmcCode;
    }

    public String getMccPlantCode() {
        return mccPlantCode;
    }

    public void setMccPlantCode(String mccPlantCode) {
        this.mccPlantCode = mccPlantCode;
    }

    public Integer getInsuranceDetailSummaryCode() {
        return insuranceDetailSummaryCode;
    }

    public void setInsuranceDetailSummaryCode(Integer insuranceDetailSummaryCode) {
        this.insuranceDetailSummaryCode = insuranceDetailSummaryCode;
    }

    public Integer getInsuranceMasterCode() {
        return insuranceMasterCode;
    }

    public void setInsuranceMasterCode(Integer insuranceMasterCode) {
        this.insuranceMasterCode = insuranceMasterCode;
    }

    public String getDcsCode() {
        return dcsCode;
    }

    public void setDcsCode(String dcsCode) {
        this.dcsCode = dcsCode;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String getCreatedBy() {
        return createdBy;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String getUpdatedBy() {
        return updatedBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    @Override
    public String getxCol1() {
        return xCol1;
    }

    @Override
    public void setxCol1(String xCol1) {
        this.xCol1 = xCol1;
    }

    @Override
    public String getxCol2() {
        return xCol2;
    }

    @Override
    public void setxCol2(String xCol2) {
        this.xCol2 = xCol2;
    }

    @Override
    public String getxCol3() {
        return xCol3;
    }

    @Override
    public void setxCol3(String xCol3) {
        this.xCol3 = xCol3;
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
}
