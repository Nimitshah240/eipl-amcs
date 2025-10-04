package com.eipl.amcs.base.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class BaseModelTxn {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    protected LocalDateTime createdAt;
    protected String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    protected LocalDateTime updatedAt;
    protected String updatedBy;
    protected String xCol1;
    protected String xCol2;
    protected String xCol3;

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
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

    public void setInitData() {
        createdAt = LocalDateTime.now();
        createdBy = "System";
    }

    public void setupdateData() {
        updatedAt = LocalDateTime.now();
        updatedBy = "System";
    }
}
