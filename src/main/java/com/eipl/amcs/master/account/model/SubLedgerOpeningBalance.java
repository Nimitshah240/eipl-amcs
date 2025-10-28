package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.LedgerDeserializer;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.deserialize.SubLedgerDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.LedgerSerialize;
import com.eipl.amcs.serialize.SocietySerialize;
import com.eipl.amcs.serialize.SubLedgerSerialize;
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
@Table(name = "sub_ledger_opening_balance")
public class SubLedgerOpeningBalance extends BaseModelTxn {

    @Id
    private String code;
    private BigDecimal balance;
    private Boolean creditDebit;
    private Boolean autoManual;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_sub_ledger_opening_balance_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String financialYearsCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledgers_code", foreignKey = @ForeignKey(name = "fk_sub_ledger_opening_balance_ledgers_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SubLedgerSerialize.class)
    @JsonDeserialize(using = SubLedgerDeserializer.class)
    @JoinColumn(name = "sub_ledger_code", foreignKey = @ForeignKey(name = "fk_sub_ledger_opening_balance_ledgers_code"))
    @JsonIgnoreProperties(value = {"society", "union"})
    private SubLedger subLedger;
    private String unionCode;

    @Override
    public String getTableName() {
        return "sub_ledger_opening_balance";
    }


    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        SubLedgerOpeningBalanceAudit audit = new SubLedgerOpeningBalanceAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setBalance(this.getBalance());
        audit.setCreditDebit(this.getCreditDebit());
        audit.setAutoManual(this.getAutoManual());
        audit.setFinancialYearsCode(this.getFinancialYearsCode());
        audit.setSubLedger(this.getSubLedger());
        audit.setLedger(this.getLedger());
        audit.setSociety(this.getSociety());
        audit.setUnionCode(this.getUnionCode());

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
