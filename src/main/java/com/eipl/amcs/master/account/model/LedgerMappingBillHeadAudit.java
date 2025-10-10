package com.eipl.amcs.master.account.model;


import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.operation.model.BillCriteria;
import com.eipl.amcs.master.operation.model.BillHead;
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
@Table(name = "ledger_mapping_bill_head_audit")
public class LedgerMappingBillHeadAudit extends BaseModelTxnAudit {

    @Id
    private String code;
    private Integer type;
    private Boolean hasSubLedger;
    private Boolean creditDebit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_head_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union"})
    private BillHead billHead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_criteria_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"formulaCode", "union", "society"})
    private BillCriteria billCriteria;

    private String unionCode;


    @Override
    public String getTableName() {
        return "ledger_mapping_bill_head_audit";
    }


}
