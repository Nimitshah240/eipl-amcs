package com.eipl.amcs.base.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class BaseModelTxnAudit implements Serializable, JsonAndTableBuilder {

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;
    private String createdBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Column(name = "x_col1")
    private String xCol1;
    @Column(name = "x_col2")
    private String xCol2;
    @Column(name = "x_col3")
    private String xCol3;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime auditCreatedAt;
    private String auditCreatedBy;
    private String operationType;

    public BaseModelTxnAudit() {
        auditCreatedAt = LocalDateTime.now();
    }

    public void setInitData() {
        auditCreatedAt = LocalDateTime.now();
        if (auditCreatedBy == null || auditCreatedBy.isEmpty())
            auditCreatedBy = "System";
    }
}
