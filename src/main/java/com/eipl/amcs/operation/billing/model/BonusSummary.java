package com.eipl.amcs.operation.billing.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.deserialize.UnionDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.eipl.amcs.serialize.SocietySerialize;
import com.eipl.amcs.serialize.UnionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bonus_summary")
public class BonusSummary extends BaseModelTxn {
    @Id
    private String code;
    private LocalDate fromDate;
    private LocalDate toDate;
    private BigDecimal totalMilkQty;
    private BigDecimal totalMilkAmount;
    private BigDecimal bonusCriteriaAmount;
    private BigDecimal bonusCriteriaValue;
    private LocalDate disbursedDate;
    private short status; //0-PENDING ,1-DISBURSED
    private short bonusCriteria; //0-percentage ,1-rs/ltr
    private short type; //0-Union,1-Society


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_bonus_summary_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "route", "bmc", "mcc", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_bonus_summary_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet"})
    private Union union;

    @Override
    public String getTableName() {
        return "bonus_summary";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        BonusSummaryAudit audit = new BonusSummaryAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setFromDate(this.getFromDate());
        audit.setToDate(this.getToDate());
        audit.setTotalMilkAmount(this.getTotalMilkAmount());
        audit.setTotalMilkQty(this.getTotalMilkQty());
        audit.setBonusCriteria(this.getBonusCriteria());
        audit.setBonusCriteriaAmount(this.getBonusCriteriaAmount());
        audit.setBonusCriteriaValue(this.getBonusCriteriaValue());
        audit.setUnion(this.getUnion());
        audit.setStatus(this.getStatus());
        audit.setDisbursedDate(this.getDisbursedDate());
        audit.setType(this.getType());
        audit.setSociety(this.getSociety());

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
