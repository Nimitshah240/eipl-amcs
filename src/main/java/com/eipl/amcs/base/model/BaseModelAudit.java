package com.eipl.amcs.base.model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class BaseModelAudit implements Serializable {
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    private String xCol1;
    private String xCol2;
    private String xCol3;

    private LocalDateTime auditCreatedAt;
    private String auditCreatedBy;
    private String operationType;

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

    public String getxCol1() {
        return xCol1;
    }

    public void setxCol1(String xCol1) {
        this.xCol1 = xCol1;
    }

    public String getxCol2() {
        return xCol2;
    }

    public void setxCol2(String xCol2) {
        this.xCol2 = xCol2;
    }

    public String getxCol3() {
        return xCol3;
    }

    public void setxCol3(String xCol3) {
        this.xCol3 = xCol3;
    }

    public LocalDateTime getAuditCreatedAt() {
        return auditCreatedAt;
    }

    public void setAuditCreatedAt(LocalDateTime auditCreatedAt) {
        this.auditCreatedAt = auditCreatedAt;
    }

    public String getAuditCreatedBy() {
        return auditCreatedBy;
    }

    public void setAuditCreatedBy(String auditCreatedBy) {
        this.auditCreatedBy = auditCreatedBy;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}
