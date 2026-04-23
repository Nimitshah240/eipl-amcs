package com.eipl.amcs.master.operation.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModel;
import com.eipl.amcs.json.deserialize.MilkTypeDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.UnionDeserializer;
import com.eipl.amcs.json.serialize.MilkTypeSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.UnionSerialize;
import com.eipl.amcs.master.global.model.MilkType;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.master.org.model.Union;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "bill_head")
public class BillHead extends BaseModel {

    @Id
    private String code;
    private String name;
    private String nameLocal;
    @Column(name = "is_default_head")
    private Boolean defaultHead;
    @Column(name = "is_disburse_allowed")
    private Boolean disburseAllowed;
    private short allowAdjustment;
    private short headType;
    private String calculationBasedOn;
    private String billHeadFor;
    private Integer defaultBillHeadCode;
    private String generalFormula;
    private String generalFormulaCode;
    private String generalFormulaComma;
    @Column(name = "has_slab")
    private Boolean hasSlab;
    @Column(name = "is_hold")
    private Boolean hold;
    @Column(name = "is_reserved")
    private Boolean reserved;
    private String paymentCycleType;
    private Integer sapSeqNo;
    private Integer sequenceNo;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = MilkTypeSerialize.class)
    @JsonDeserialize(using = MilkTypeDeserializer.class)
    @JoinColumn(name = "milk_type_code", foreignKey = @ForeignKey(name = "fk_bill_head_milk_type_code"))
    private MilkType milkType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_bill_head_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet", "hibernateLazyInitializer", "handler"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = UnionSerialize.class)
    @JsonDeserialize(using = UnionDeserializer.class)
    @JoinColumn(name = "union_code", foreignKey = @ForeignKey(name = "fk_bill_head_union_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "state", "district", "subDistrict", "village", "hamlet", "hibernateLazyInitializer", "handler"})
    private Union union;

    @Override
    public String getTableName() {
        return "bill_head";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        BillHeadAudit audit = new BillHeadAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setName(this.getName());
        audit.setNameLocal(this.getNameLocal());
        audit.setDefaultHead(this.getDefaultHead());
        audit.setDisburseAllowed(this.getDisburseAllowed());
        audit.setAllowAdjustment(this.getAllowAdjustment());
        audit.setHeadType(this.getHeadType());
        audit.setUnion(this.getUnion());
        audit.setSociety(this.getSociety());

        audit.setCreatedAt(this.getCreatedAt());
        audit.setCreatedBy(this.getCreatedBy());
        audit.setUpdatedAt(this.getUpdatedAt());
        audit.setUpdatedBy(this.getUpdatedBy());
        audit.setActive(this.isActive());
        audit.setXCol1(this.getXCol1());
        audit.setXCol2(this.getXCol2());
        audit.setXCol3(this.getXCol3());
        audit.setCalculationBasedOn(this.getCalculationBasedOn());
        audit.setBillHeadFor(this.getBillHeadFor());
        audit.setDefaultBillHeadCode(this.getDefaultBillHeadCode());
        audit.setGeneralFormula(this.getGeneralFormula());
        audit.setGeneralFormulaCode(this.getGeneralFormulaCode());
        audit.setGeneralFormulaComma(this.getGeneralFormulaComma());
        audit.setHasSlab(this.getHasSlab());
        audit.setHold(this.getHold());
        audit.setReserved(this.getReserved());
        audit.setPaymentCycleType(this.getPaymentCycleType());
        audit.setSapSeqNo(this.getSapSeqNo());
        audit.setSequenceNo(this.getSequenceNo());
        audit.setMilkType(this.getMilkType());
        return audit;
    }
}
