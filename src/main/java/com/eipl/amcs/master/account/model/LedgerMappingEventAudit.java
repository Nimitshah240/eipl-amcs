package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "ledger_mapping_event_audit")
public class LedgerMappingEventAudit extends BaseModelTxnAudit {

    @Id
    private Integer code;
    private Boolean creditSubLedger;
    private Boolean debitSubLedger;
    private int eventcode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_event_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union"})
    private Events events;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "ledgerGroup"})
    private Ledger creditLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "ledgerGroup"})
    private Ledger debitLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private VoucherType voucherType;

    @Override
    public String getTableName() {
        return "ledger_mapping_event_audit";
    }
}
