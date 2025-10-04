package com.eipl.amcs.operation.inventory.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "product_requisition")
public class ProductRequisition extends BaseModelTxn {
    @Id
    private String code;
    private LocalDateTime cancelledAt;
    private String cancelledBy;
    private LocalDateTime requisitionDate;
    private String description;
    private int entryType;

    @Column(name = "is_cancel")
    private Boolean cancel;
    private String status;
    private String unionCode;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_product_requisition_society_code"))
    @JsonIgnoreProperties(value = {"union", "mcc", "plant", "bmc", "state", "district", "subDistrict", "village", "hamlet", "bank", "branch", "route"})
    private Society society;

    @Transient
    private Boolean isDelete;
    @Transient
    private String syncStatus;
    @Transient
    private LocalDateTime syncTimestamp;


    @Override
    public String getTableName() {
        return "product_requisition";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ProductRequisitionAudit audit = new ProductRequisitionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setCancelledAt(this.getCancelledAt());
        audit.setCancelledBy(this.getCancelledBy());
        audit.setRequisitionDate(this.getRequisitionDate());
        audit.setSociety(this.getSociety());
        audit.setDescription(this.getDescription());
        audit.setEntryType(this.getEntryType());
        audit.setUnionCode(this.getUnionCode());
        audit.setIsCancel(this.getCancel());
        audit.setStatus(this.getStatus());


        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }

    public Boolean getDelete() {
        return isDelete;
    }

    public void setDelete(Boolean delete) {
        isDelete = delete;
    }
}
