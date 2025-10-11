package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
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
@Table(name = "voucher_sub_ledger")
public class VoucherSubLedger extends BaseModelTxn {

    @Id
    private String code;
    private BigDecimal amount;
    private Boolean creditDebit;
    private String narration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(name = "fk_voucher_sub_ledger_sub_ledger_code"))
    @JsonIgnoreProperties(value = {"society"})
    private SubLedger subLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_code", foreignKey = @ForeignKey(name = "fk_voucher_sub_ledger_voucher_code"))
    @JsonIgnoreProperties(value = {"society", "voucherType"})
    private Voucher voucher;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_transaction_code", foreignKey = @ForeignKey(name = "fk_voucher_sub_ledger_voucher_transaction_code"))
    @JsonIgnoreProperties(value = {"ledger", "voucher"})
    private VoucherTransaction voucherTransaction;

    @Override
    public String getTableName() {
        return "voucher_sub_ledger";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        VoucherSubLedgerAudit audit = new VoucherSubLedgerAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setAmount(this.getAmount());
        audit.setCreditDebit(this.getCreditDebit());
        audit.setNarration(this.getNarration());
        audit.setSubLedger(this.getSubLedger());
        audit.setVoucher(this.getVoucher());
        audit.setVoucherTransaction(this.getVoucherTransaction());

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
