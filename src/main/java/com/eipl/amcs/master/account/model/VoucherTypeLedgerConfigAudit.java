package com.eipl.amcs.master.account.model;

import com.eipl.amcs.base.BaseModelTxnAudit;
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
@Table(name = "voucher_type_ledger_config_audit")
public class VoucherTypeLedgerConfigAudit extends BaseModelTxnAudit {

    @Id
    private String code;
    private Boolean creditDebit;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = SocietySerialize.class)
    @JsonDeserialize(using = SocietyDeserializer.class)
    @JoinColumn(name = "society_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"bank", "branch", "union", "plant", "mcc", "bmc", "route", "state", "district", "subDistrict", "village", "hamlet"})
    private Society society;
    private String unionCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = LedgerSerialize.class)
    @JsonDeserialize(using = LedgerDeserializer.class)
    @JoinColumn(name = "ledger_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    @JsonIgnoreProperties(value = {"ledgerGroup", "society", "union"})
    private Ledger ledger;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonSerialize(using = VoucherTypeSerialize.class)
    @JsonDeserialize(using = VoucherTypeDeserializer.class)
    @JoinColumn(name = "voucher_type_code", foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private VoucherType voucherType;

    @Override
    public String getTableName() {
        return "voucher_types_audit";
    }
}
