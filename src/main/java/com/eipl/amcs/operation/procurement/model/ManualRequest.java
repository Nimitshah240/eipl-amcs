package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.ShiftDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.ShiftSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.global.model.Shift;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
@Table(name = "manual_request")
public class ManualRequest extends BaseModelTxn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_milk_collection_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private int status;
    private LocalDateTime fromDate;
    private LocalDateTime toDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "from_shift", foreignKey = @ForeignKey(name = "fk_milk_collection_shift_code"))
    private Shift fromShift;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = ShiftSerialize.class)
    @JsonDeserialize(using = ShiftDeserializer.class)
    @JoinColumn(name = "to_shift", foreignKey = @ForeignKey(name = "fk_milk_collection_shift_code"))
    private Shift toShift;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private LocalDateTime closedAt;
    private String closedBy;
    private String remarks;

    @Override
    public String getTableName() {
        return "manual_request";
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        ManualRequestAudit audit = new ManualRequestAudit();
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
        audit.setId(this.getId());
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
