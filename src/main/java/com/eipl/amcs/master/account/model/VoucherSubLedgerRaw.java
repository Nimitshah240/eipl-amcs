package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxn;
import com.eipl.amcs.json.deserialize.SubLedgerDeserializer;
import com.eipl.amcs.json.serialize.SubLedgerSerialize;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "voucher_sub_ledger_raw")
public class VoucherSubLedgerRaw extends BaseModelTxn {

    @Id
    private String code;
    private BigDecimal amount;
    private Boolean creditDebit;
    private String narration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubLedgerSerialize.class)
    @JsonDeserialize(using = SubLedgerDeserializer.class)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(name = "fk_voucher_sub_ledger_sub_ledger_code"))
    @JsonIgnoreProperties(value = {"society"})
    private SubLedger subLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_raw_code")
    @JsonIgnoreProperties(value = {"society", "voucherType"})
    @JsonBackReference
    private VoucherRaw voucherRaw;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_transaction_raw_code")
    @JsonIgnoreProperties(value = {"ledger", "voucher"})
    private VoucherTransactionRaw voucherTransactionRaw;

}