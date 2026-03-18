package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.model.BaseModelTxnAudit;
import com.eipl.amcs.json.deserialize.EventDeserializer;
import com.eipl.amcs.json.deserialize.LedgerDeserializer;
import com.eipl.amcs.json.deserialize.SocietyDeserializer;
import com.eipl.amcs.json.deserialize.VoucherTypeDeserializer;
import com.eipl.amcs.json.serialize.EventSerialize;
import com.eipl.amcs.json.serialize.LedgerSerialize;
import com.eipl.amcs.json.serialize.SocietySerialize;
import com.eipl.amcs.json.serialize.VoucherTypeSerialize;
import com.eipl.amcs.master.org.model.Society;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
    private String voucherNarration;
    private String voucherTxnCreditNarration;
    private String voucherTxnDebitNarration;
    private String voucherNarrationLocal;
    private String voucherTxnCreditNarrationLocal;
    private String voucherTxnDebitNarrationLocal;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_event_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = EventSerialize.class)
    @JsonDeserialize(using = EventDeserializer.class)
    @JoinColumn(name = "event_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union"})
    private Events events;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "credit_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "ledgerGroup"})
    private Ledger creditLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "debit_ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"society", "union", "ledgerGroup"})
    private Ledger debitLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherTypeSerialize.class)
    @JsonDeserialize(using = VoucherTypeDeserializer.class)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private VoucherType voucherType;

    @Override
    public String getTableName() {
        return "ledger_mapping_event_audit";
    }
}
