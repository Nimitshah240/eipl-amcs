package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
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
@Table(name = "allow_dcs_manual_collection_range_audit")
public class AllowDcsManualCollectionRangeAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long code;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_milk_collection_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private int status;
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
    private Boolean isWeightManual;
    @Column(name = "is_quality_manual")
    private Boolean isQualityManual;
    private String approvedBy;
    private LocalDateTime approvedDate;
    private String cancelledBy;
    private LocalDateTime cancelledAt;
    private LocalDateTime closedAt;
    private String closedBy;
    private String remarks;

    @Override
    public String getTableName() {
        return "allow_dcs_manual_collection_range_audit";
    }
}
