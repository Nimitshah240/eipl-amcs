package com.eipl.amcs.base.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
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

    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @Column(name = "x_col1")
    private String xCol1;
    @Column(name = "x_col2")
    private String xCol2;
    @Column(name = "x_col3")
    private String xCol3;

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
