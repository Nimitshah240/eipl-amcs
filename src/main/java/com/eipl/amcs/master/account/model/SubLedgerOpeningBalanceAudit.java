package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "sub_ledger_opening_balance_audit")
public class SubLedgerOpeningBalanceAudit extends BaseModelTxnAudit {

    @Id
    private String code;
    private BigDecimal balance;
    private Boolean creditDebit;
    private Boolean autoManual;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String financialYearsCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ledgers_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union"})
    private SubLedger subLedger;
    private String unionCode;

    @Override
    public String getTableName() {
        return "sub_ledger_opening_balance_audit";
    }
}
