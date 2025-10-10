package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "voucher_sub_ledger_audit")
public class VoucherSubLedgerAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigDecimal amount;
    private Boolean creditDebit;
    private String narration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society"})
    private SubLedger subLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "voucherType"})
    private Voucher voucher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_transaction_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledger", "voucher"})
    private VoucherTransaction voucherTransaction;

    @Override
    public String getTableName() {
        return "voucher_sub_ledger_audit";
    }


}
