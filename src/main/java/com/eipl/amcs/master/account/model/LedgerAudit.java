package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelAudit;
import com.eipl.amcs.json.deserialize.LedgerGroupDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.serialize.LedgerGroupSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Size;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ledgers_audit")
public class LedgerAudit extends BaseModelAudit {

    @Id
    @Size(max = 15)
    private String code;

    @Size(max = 200)
    private String name;
    @Size(max = 255)
    private String nameLocal;
    private Boolean hasSubLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerGroupSerialize.class)
    @JsonDeserialize(using = LedgerGroupDeserializer.class)
    @JoinColumn(name = "ledger_group_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerType"})
    private LedgerGroup ledgerGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;

    @Override
    public String getTableName() {
        return "ledgers_audit";
    }


}
