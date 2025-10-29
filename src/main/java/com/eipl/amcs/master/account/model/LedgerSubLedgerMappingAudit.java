package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.SubLedgerDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.SubLedgerSerialize;
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
@Table(name = "ledger_sub_ledgers_mapping_audit")
public class LedgerSubLedgerMappingAudit extends BaseModelTxnAudit {

    @Id
    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubLedgerSerialize.class)
    @JsonDeserialize(using = SubLedgerDeserializer.class)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union"})
    private SubLedger subLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"hamlet", "village", "subDistrict", "district", "state", "route", "bmc", "mcc", "plant", "union", "branch", "bank"})
    private Society society;

    private String unionCode;

    @Override
    public String getTableName() {
        return "ledgers_sub_ledgers_mapping_audit";
    }


}
