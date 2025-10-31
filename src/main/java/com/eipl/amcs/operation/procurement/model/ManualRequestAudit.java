package com.eipl.amcs.operation.procurement.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
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
@Table(name = "manual_request_audit")
public class ManualRequestAudit extends BaseModelTxnAudit {

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
        return "manual_request_audit";
    }


}
