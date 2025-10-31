package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.master.global.model.Shift;
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
@Table(name = "allow_dcs_manual_collection_range")
public class AllowDcsManualCollectionRange extends BaseModelTxn {

    @Id
    private Long code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_milk_collection_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private Integer status;
    private String unionCode;
    private String plantCode;
    private String mccPlantCode;
    private String bmcCode;
    private String xCol4;
    private String xCol5;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_shift", foreignKey = @ForeignKey(name = "fk_milk_collection_shift_code"))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_shift", foreignKey = @ForeignKey(name = "fk_milk_collection_shift_code"))
    private Shift toShift;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    @Column(name = "is_weight_manual")
    private Boolean weightManual;
    @Column(name = "is_quality_manual")
    private Boolean qualityManual;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private LocalDateTime closedAt;
    private String closedBy;
    private String remarks;

    public Object getId() {
        return this.getCode();
    }

    @Override
    public String getTableName() {
        return "allow_dcs_manual_collection_range";
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        AllowDcsManualCollectionRangeAudit audit = new AllowDcsManualCollectionRangeAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);
        audit.setApprovedDate(this.getApprovedDate());
        audit.setApprovedBy(this.getApprovedBy());
        audit.setCancelledAt(this.getCancelledAt());
        audit.setUnionCode(this.getUnionCode());
        audit.setCancelledBy(this.getCancelledBy());
        audit.setClosedAt(this.getClosedAt());
        audit.setClosedBy(this.getClosedBy());
        audit.setFromDate(this.getFromDate());
        audit.setFromShift(this.getFromShift());
        audit.setToShift(this.getToShift());
        audit.setToDate(this.getToDate());
        audit.setRemarks(this.getRemarks());
        audit.setStatus(this.getStatus());
        audit.setSociety(this.getSociety());
        audit.setCode(this.getCode());
        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());

        return audit;
    }
}
