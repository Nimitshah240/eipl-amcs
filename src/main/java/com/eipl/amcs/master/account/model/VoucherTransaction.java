package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.LedgerDeserializer;
import com.eipl.amcs.deserialize.VoucherDeserializer;
import com.eipl.amcs.deserialize.VoucherTransactionDeserializer;
import com.eipl.amcs.serialize.LedgerSerialize;
import com.eipl.amcs.serialize.VoucherSerialize;
import com.eipl.amcs.serialize.VoucherTransactionSerialize;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Table(name = "voucher_transaction")
public class VoucherTransaction extends BaseModelTxn {

    @Id
    private String code;
    private BigDecimal amount;
    private Boolean creditDebit; //T-Credit, F-Debit
    private String narration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_voucher_transaction_ledger_code"))
    @JsonIgnoreProperties(value = {"society", "ledgerGroup"})
    private Ledger ledger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherSerialize.class)
    @JsonDeserialize(using = VoucherDeserializer.class)
    @JoinColumn(name = "voucher_code", foreignKey = @ForeignKey(name = "fk_voucher_transaction_voucher_code"))
    @JsonIgnoreProperties(value = {"society", "voucherType"})
    private Voucher voucher;

    @Transient
    @JsonIgnore
    private List<VoucherSubLedger> voucherSubLedgers;

    @Override
    public String getTableName() {
        return "voucher_transaction";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        VoucherTransactionAudit audit = new VoucherTransactionAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setAmount(this.getAmount());
        audit.setCreditDebit(this.getCreditDebit());
        audit.setNarration(this.getNarration());
        audit.setLedger(this.getLedger());
        audit.setVoucher(this.getVoucher());

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
