package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelAudit;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @JoinColumn(name = "ledger_group_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerType"})
    private LedgerGroup ledgerGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;

    @Override
    public String getTableName() {
        return "ledgers_audit";
    }


}
