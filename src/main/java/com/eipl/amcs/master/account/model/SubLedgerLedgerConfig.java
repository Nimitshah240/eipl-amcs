package com.eipl.amcs.master.account.model;


import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sub_ledger_ledger_config")
public class SubLedgerLedgerConfig extends BaseModelTxn {

    @Id
    private String code;
    private Integer subLedgerType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_ledger_subledger_mapping_ledger_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledger_sub_ledger_mapping_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    private String unionCode;


    @Override
    public String getTableName() {
        return "sub_ledger_ledger_config";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        SubLedgerLedgerConfigAudit audit = new SubLedgerLedgerConfigAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setSubLedgerType(this.getSubLedgerType());
        audit.setLedger(this.getLedger());
        audit.setSociety(this.getSociety());
        audit.setUnionCode(this.getUnionCode());

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
