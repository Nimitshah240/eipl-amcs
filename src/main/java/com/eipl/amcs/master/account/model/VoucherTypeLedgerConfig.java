package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxn;
import com.eipl.amcs.base.JsonAndTableBuilder;
import com.eipl.amcs.deserialize.LedgerDeserializer;
import com.eipl.amcs.deserialize.SocietyDeserializer;
import com.eipl.amcs.deserialize.VoucherTypeDeserializer;
import com.eipl.amcs.master.org.model.Society;
import com.eipl.amcs.serialize.LedgerSerialize;
import com.eipl.amcs.serialize.SocietySerialize;
import com.eipl.amcs.serialize.VoucherTypeSerialize;
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
@Table(name = "voucher_type_ledger_config")
public class VoucherTypeLedgerConfig extends BaseModelTxn {

    @Id
    private String code;
    private Boolean creditDebit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(name = "fk_voucher_type_ledger_config_society_code"))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(name = "fk_voucher_type_ledger_config_ledger_code"))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherTypeSerialize.class)
    @JsonDeserialize(using = VoucherTypeDeserializer.class)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(name = "fk_voucher_type_ledger_config_voucher_type"))
    private VoucherType voucherType;

    @Override
    public String getTableName() {
        return "voucher_type_ledger_config";
    }

    @Override
    public Object getId() {
        return this.getCode();
    }

    @Override
    public JsonAndTableBuilder getAuditModel(String operation, String user) {
        VoucherTypeLedgerConfigAudit audit = new VoucherTypeLedgerConfigAudit();
        audit.setOperationType(operation);
        audit.setAuditCreatedBy(user);

        audit.setCode(this.getCode());
        audit.setCreditDebit(this.getCreditDebit());
        audit.setSociety(this.getSociety());
        audit.setUnionCode(this.getUnionCode());
        audit.setLedger(this.getLedger());
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
