package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.SubLedgerDeserializer;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.SubLedgerSerialize;
import com.eipl.amcs.master.org.model.Society;
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
@Table(name = "ledger_sub_ledgers_mapping")
public class LedgerSubLedgerMapping extends BaseModelTxn {

    @Id
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_ledgers_sub_ledgers_mapping_ledger_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubLedgerSerialize.class)
    @JsonDeserialize(using = SubLedgerDeserializer.class)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(name = "fk_ledgers_sub_ledgers_mapping_sub_ledger_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private SubLedger subLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledgers_sub_ledgers_mapping_society_code"))
    @JsonIgnoreProperties(value = {"hamlet", "village", "subDistrict", "district", "state", "route", "bmc", "mcc", "plant", "union", "branch", "bank"})
    private Society society;

    private String unionCode;

    @Override
    public String getTableName() {
        return "ledgers_sub_ledgers_mapping";
    }


    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        LedgerSubLedgerMappingAudit audit = new LedgerSubLedgerMappingAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setLedger(this.getLedger());
        audit.setSubLedger(this.getSubLedger());
        audit.setUnionCode(this.getUnionCode());
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
