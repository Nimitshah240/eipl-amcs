package com.eipl.amcs.base.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@MappedSuperclass
@Getter
@Setter
public class BaseModelAudit implements Serializable, JsonAndTableBuilder {

    private LocalDateTime createdAt;
    @Size(max = 15)
    private String createdBy;
    private LocalDateTime updatedAt;
    @Size(max = 15)
    private String updatedBy;
    @Column(name = "is_active")
    private boolean active;

    @Column(name = "x_col1", length = 255)
    private String xCol1;
    @Column(name = "x_col2", length = 255)
    private String xCol2;
    @Column(name = "x_col3", length = 255)
    private String xCol3;

    private LocalDateTime auditCreatedAt;
    @Size(max = 15)
    private String auditCreatedBy;
    @Size(max = 15)
    private String operationType;

    public BaseModelAudit() {
        auditCreatedAt = LocalDateTime.now();
    }

    public void setInitData() {
        auditCreatedAt = LocalDateTime.now();
        if (auditCreatedBy == null || auditCreatedBy.isEmpty())
            auditCreatedBy = "System";
    }

}
