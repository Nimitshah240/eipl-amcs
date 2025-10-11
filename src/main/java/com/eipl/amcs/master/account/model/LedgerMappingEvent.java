package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
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
@Table(name = "ledger_mapping_event")
public class LedgerMappingEvent extends BaseModelTxn {

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
    @JoinColumn(name = "event_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_event_event_code"))
    @JsonIgnoreProperties(value = {"society"})
    private Events events;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_ledger_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_event_credit_ledger_code"))
    @JsonIgnoreProperties(value = {"society", "ledgerGroup"})
    private Ledger creditLedger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debit_ledger_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_event_debit_ledger_code"))
    @JsonIgnoreProperties(value = {"society", "ledgerGroup"})
    private Ledger debitLedger;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(name = "fk_ledger_mapping_event_voucher_type_code"))
    private VoucherType voucherType;

    @Override
    public String getTableName() {
        return "ledger_mapping_event";
    }


    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        LedgerMappingEventAudit audit = new LedgerMappingEventAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setCreditLedger(this.getCreditLedger());
        audit.setDebitLedger(this.getDebitLedger());
        audit.setCreditSubLedger(this.getCreditSubLedger());
        audit.setDebitSubLedger(this.getDebitSubLedger());
        audit.setEvents(this.getEvents());
        audit.setEventcode(this.getEventcode());
        audit.setSociety(this.getSociety());
        audit.setUnionCode(this.getUnionCode());
        audit.setVoucherType(this.getVoucherType());

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
