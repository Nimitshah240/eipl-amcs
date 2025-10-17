package com.eipl.amcs.operation.procurement.dto;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;

import java.time.LocalDateTime;

public class AllowDcsManualCollectionRange extends BaseModelTxn {

    private Long code;
    private String unionCode;
    private Society society;
    private int status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    private Shift fromShift;
    private Shift toShift;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private LocalDateTime closedAt;
    private String closedBy;
    private String remarks;
    private String plantCode;
    private String mccPlantCode;
    private String bmcCode;
    private String xCol4;
    private String xCol5;
    private Boolean weightManual;
    private Boolean qualityManual;


//            xCol1 = 0 means Manual Request for Allow collection beyond shift time
//            xCol1 = 1 means Manual Request for Manual Quality and Quantity Collection

    public Boolean getWeightManual() {
        return weightManual;
    }

    public void setWeightManual(Boolean weightManual) {
        this.weightManual = weightManual;
    }

    public Boolean getQualityManual() {
        return qualityManual;
    }

    public void setQualityManual(Boolean qualityManual) {
        this.qualityManual = qualityManual;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getMccPlantCode() {
        return mccPlantCode;
    }

    public void setMccPlantCode(String mccPlantCode) {
        this.mccPlantCode = mccPlantCode;
    }

    public String getBmcCode() {
        return bmcCode;
    }

    public void setBmcCode(String bmcCode) {
        this.bmcCode = bmcCode;
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

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public Society getSociety() {
        return society;
    }

    public void setSociety(Society society) {
        this.society = society;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LocalDateTime getFromDate() {
        return fromDate;
    }

    public void setFromDate(LocalDateTime fromDate) {
        this.fromDate = fromDate;
    }

    public LocalDateTime getToDate() {
        return toDate;
    }

    public void setToDate(LocalDateTime toDate) {
        this.toDate = toDate;
    }

    public Shift getFromShift() {
        return fromShift;
    }

    public void setFromShift(Shift fromShift) {
        this.fromShift = fromShift;
    }

    public Shift getToShift() {
        return toShift;
    }

    public void setToShift(Shift toShift) {
        this.toShift = toShift;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(LocalDateTime approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public LocalDateTime getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(LocalDateTime cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public LocalDateTime getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDateTime closedAt) {
        this.closedAt = closedAt;
    }

    public String getClosedBy() {
        return closedBy;
    }

    public void setClosedBy(String closedBy) {
        this.closedBy = closedBy;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
