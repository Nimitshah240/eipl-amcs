package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
import com.eipl.amcs.deserialize.LedgerDeserializer;
import com.eipl.amcs.deserialize.VoucherDeserializer;
import com.eipl.amcs.deserialize.VoucherTransactionDeserializer;
import com.eipl.amcs.serialize.LedgerSerialize;
import com.eipl.amcs.serialize.VoucherSerialize;
import com.eipl.amcs.serialize.VoucherTransactionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@SuppressWarnings("serial")
@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "voucher_transaction_audit")
public class VoucherTransactionAudit extends BaseModelTxnAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private BigDecimal amount;
    private Boolean creditDebit;
    private String narration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Ledger ledger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherSerialize.class)
    @JsonDeserialize(using = VoucherDeserializer.class)
    @JoinColumn(name = "voucher_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Voucher voucher;

    @Transient
    @JsonIgnore
    private List<VoucherSubLedger> voucherSubLedgers;

    @Override
    public String getTableName() {
        return "voucher_transaction_audit";
    }

}
