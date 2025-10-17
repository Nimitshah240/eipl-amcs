package com.eipl.amcs.master.insurance.dto;

import com.eipl.amcs.base.model.BaseModel;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class InsuranceMaster extends BaseModel {

    private Integer insuranceMasterCode;
    private LocalDate insuranceStartDate;
    private LocalDate insuranceEndDate;
    private LocalDate dcsEditStartDate;
    private LocalDate dcsEditEndDate;
    private Integer memberMinAge;
    private Integer memberMaxAge;
    private LocalDate insuranceFinalDate;
    private String insuranceDescription;
    private String unionCode;
    private String status;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private String originatingOrgCode;
    private String originatingOrgType;
    private Integer originatingType;
    private String xCol1;
    private String xCol2;
    private String xCol3;
    private String xCol4;
    private String xCol5;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getInsuranceMasterCode() {
        return insuranceMasterCode;
    }

    public void setInsuranceMasterCode(Integer insuranceMasterCode) {
        this.insuranceMasterCode = insuranceMasterCode;
    }

    public LocalDate getInsuranceStartDate() {
        return insuranceStartDate;
    }

    public void setInsuranceStartDate(LocalDate insuranceStartDate) {
        this.insuranceStartDate = insuranceStartDate;
    }

    public LocalDate getInsuranceEndDate() {
        return insuranceEndDate;
    }

    public void setInsuranceEndDate(LocalDate insuranceEndDate) {
        this.insuranceEndDate = insuranceEndDate;
    }

    public LocalDate getDcsEditStartDate() {
        return dcsEditStartDate;
    }

    public void setDcsEditStartDate(LocalDate dcsEditStartDate) {
        this.dcsEditStartDate = dcsEditStartDate;
    }

    public LocalDate getDcsEditEndDate() {
        return dcsEditEndDate;
    }

    public void setDcsEditEndDate(LocalDate dcsEditEndDate) {
        this.dcsEditEndDate = dcsEditEndDate;
    }

    public Integer getMemberMinAge() {
        return memberMinAge;
    }

    public void setMemberMinAge(Integer memberMinAge) {
        this.memberMinAge = memberMinAge;
    }

    public Integer getMemberMaxAge() {
        return memberMaxAge;
    }

    public void setMemberMaxAge(Integer memberMaxAge) {
        this.memberMaxAge = memberMaxAge;
    }

    public LocalDate getInsuranceFinalDate() {
        return insuranceFinalDate;
    }

    public void setInsuranceFinalDate(LocalDate insuranceFinalDate) {
        this.insuranceFinalDate = insuranceFinalDate;
    }

    public String getInsuranceDescription() {
        return insuranceDescription;
    }

    public void setInsuranceDescription(String insuranceDescription) {
        this.insuranceDescription = insuranceDescription;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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
