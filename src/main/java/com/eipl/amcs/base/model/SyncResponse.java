package com.eipl.amcs.base.model;


import java.time.LocalDateTime;

public class SyncResponse {

    private String forceSyncRequestCode;
    private String dcsCode;
    private String unionCode;
    private String plantCode;
    private String bmcCode;
    private String tableName;
    private String fromShift;
    private String toShift;
    private String isDownload;
    private String originatingOrgCode;
    private String originatingOrgType;
    private String originatingType;
    protected LocalDateTime fromDatetime;
    protected LocalDateTime toDatetime;
    protected LocalDateTime createdAt;
    protected String createdBy;
    protected LocalDateTime updatedAt;
    protected String updatedBy;


    public String getForceSyncRequestCode() {
        return forceSyncRequestCode;
    }

    public void setForceSyncRequestCode(String forceSyncRequestCode) {
        this.forceSyncRequestCode = forceSyncRequestCode;
    }

    public String getDcsCode() {
        return dcsCode;
    }

    public void setDcsCode(String dcsCode) {
        this.dcsCode = dcsCode;
    }

    public String getUnionCode() {
        return unionCode;
    }

    public void setUnionCode(String unionCode) {
        this.unionCode = unionCode;
    }

    public String getPlantCode() {
        return plantCode;
    }

    public void setPlantCode(String plantCode) {
        this.plantCode = plantCode;
    }

    public String getBmcCode() {
        return bmcCode;
    }

    public void setBmcCode(String bmcCode) {
        this.bmcCode = bmcCode;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getFromShift() {
        return fromShift;
    }

    public void setFromShift(String fromShift) {
        this.fromShift = fromShift;
    }

    public String getToShift() {
        return toShift;
    }

    public void setToShift(String toShift) {
        this.toShift = toShift;
    }

    public String getIsDownload() {
        return isDownload;
    }

    public void setIsDownload(String isDownload) {
        this.isDownload = isDownload;
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

    public String getOriginatingType() {
        return originatingType;
    }

    public void setOriginatingType(String originatingType) {
        this.originatingType = originatingType;
    }

    public LocalDateTime getFromDatetime() {
        return fromDatetime;
    }

    public void setFromDatetime(LocalDateTime fromDatetime) {
        this.fromDatetime = fromDatetime;
    }

    public LocalDateTime getToDatetime() {
        return toDatetime;
    }

    public void setToDatetime(LocalDateTime toDatetime) {
        this.toDatetime = toDatetime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}