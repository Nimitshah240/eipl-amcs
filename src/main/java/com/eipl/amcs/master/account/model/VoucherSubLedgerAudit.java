package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.deserialize.SubLedgerDeserializer;
import com.eipl.amcs.deserialize.VoucherDeserializer;
import com.eipl.amcs.deserialize.VoucherTransactionDeserializer;
import com.eipl.amcs.serialize.SubLedgerSerialize;
import com.eipl.amcs.serialize.VoucherSerialize;
import com.eipl.amcs.serialize.VoucherTransactionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    @JsonSerialize(using = SubLedgerSerialize.class)
    @JsonDeserialize(using = SubLedgerDeserializer.class)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society"})
    private SubLedger subLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherSerialize.class)
    @JsonDeserialize(using = VoucherDeserializer.class)
    @JoinColumn(name = "voucher_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "voucherType"})
    private Voucher voucher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherTransactionSerialize.class)
    @JsonDeserialize(using = VoucherTransactionDeserializer.class)
    @JoinColumn(name = "voucher_transaction_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledger", "voucher"})
    private VoucherTransaction voucherTransaction;

    @Override
    public String getTableName() {
        return "voucher_sub_ledger_audit";
    }


}
