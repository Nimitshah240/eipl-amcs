package com.eipl.amcs.master.account.model;


import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.json.deserialize.BillCriteriaDeserializer;
import com.eipl.amcs.json.deserialize.BillHeadDeserializer;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.model.BillHead;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.json.serialize.BillCriteriaSerialize;
import com.eipl.amcs.json.serialize.BillHeadSerialize;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
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
@Table(name = "ledger_mapping_bill_head")
public class LedgerMappingBillHead extends BaseModelTxn {

    @Id
    private String code;
    private Integer type;
    private Boolean hasSubLedger;
    private Boolean creditDebit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_bill_head_ledger_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BillHeadSerialize.class)
    @JsonDeserialize(using = BillHeadDeserializer.class)
    @JoinColumn(name = "bill_head_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_bill_head_bill_head_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private BillHead billHead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_bill_head_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = BillCriteriaSerialize.class)
    @JsonDeserialize(using = BillCriteriaDeserializer.class)
    @JoinColumn(name = "bill_criteria_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_bill_head_bill_criteria_code"))
    @JsonIgnoreProperties(value = {"formulaCode", "union", "society"})
    private BillCriteria billCriteria;

    private String unionCode;


    @Override
    public String getTableName() {
        return "ledger_mapping_bill_head";
    }


    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        LedgerMappingBillHeadAudit audit = new LedgerMappingBillHeadAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setType(this.getType());
        audit.setHasSubLedger(this.getHasSubLedger());
        audit.setCreditDebit(this.getCreditDebit());
        audit.setLedger(this.getLedger());
        audit.setBillHead(this.getBillHead());
        audit.setSociety(this.getSociety());
        audit.setUnionCode(this.getUnionCode());
        audit.setBillCriteria(this.getBillCriteria());

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
